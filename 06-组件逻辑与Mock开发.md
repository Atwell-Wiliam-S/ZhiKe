# 4.3-4.6 前端组件逻辑与开发规范

> 来源：智能教学闭环系统重构设计文档 v1.2
>
> 本文档定义前端核心组件的完整代码逻辑、状态管理架构、路由守卫、前后端对接规范及 Mock 数据开发模式。所有 TypeScript 代码可直接复制到项目中使用，DTO 类型与 `03-API接口规范.md` 3.9 节严格一致。

---

## 4.3 前端项目结构

### 4.3.1 目录规范

```
src/
├── api/                          # API 请求层
│   ├── auth.ts                   # 认证相关 API
│   ├── video.ts                  # 视频相关 API
│   ├── qa.ts                     # RAG 问答 API
│   ├── analytics.ts              # 学情分析 API
│   ├── student.ts                # 学生课程/笔记 API
│   └── admin.ts                  # 管理员 API
├── composables/                  # Vue 3 Composables（核心业务逻辑）
│   ├── useSSEChat.ts             # SSE 流式问答
│   ├── useTheme.ts               # 多角色主题切换
│   ├── useVideoPlayer.ts         # 视频播放器控制
│   ├── useNotes.ts               # 笔记 CRUD
│   ├── useNotification.ts        # 通知中心
│   └── usePagination.ts          # 通用分页逻辑
├── stores/                       # Pinia 状态管理
│   ├── user.ts                   # 用户认证状态
│   ├── theme.ts                  # 主题状态（委托 useTheme）
│   └── notification.ts           # 通知状态
├── router/                       # Vue Router
│   ├── index.ts                  # 路由实例 + 守卫
│   ├── routes.ts                 # 路由表定义
│   └── guards.ts                 # 导航守卫逻辑
├── types/                        # TypeScript 类型定义
│   ├── api.ts                    # API DTO（与 3.9 节一致）
│   ├── env.d.ts                  # 环境变量类型
│   └── component.ts              # 组件 Props/Emits 类型
├── utils/                        # 工具函数
│   ├── request.ts                # Axios 实例 + 拦截器
│   ├── storage.ts                # localStorage 封装
│   ├── format.ts                 # 格式化工具
│   └── snake-case.ts             # snake_case ↔ camelCase 转换
├── mocks/                        # MSW Mock 数据
│   ├── browser.ts                # Service Worker 入口
│   ├── handlers/                 # 按模块分 Handler
│   │   ├── auth.handler.ts
│   │   ├── video.handler.ts
│   │   ├── qa.handler.ts
│   │   ├── analytics.handler.ts
│   │   ├── student.handler.ts
│   │   └── admin.handler.ts
│   └── data/                     # Mock 数据源
│       ├── auth.data.ts
│       ├── video.data.ts
│       ├── qa.data.ts
│       ├── analytics.data.ts
│       ├── student.data.ts
│       └── admin.data.ts
├── components/                   # 通用组件
│   ├── ThemeToggle.vue           # 主题切换按钮
│   ├── ReferenceTag.vue          # 引用溯源标签
│   ├── VideoTimeline.vue         # 知识点时间轴
│   ├── FragmentCard.vue          # 知识片段卡片
│   ├── KpiCard.vue               # KPI 指标卡片
│   └── EmptyState.vue            # 空状态占位
├── views/                        # 页面级组件
│   ├── login/
│   ├── student/
│   ├── teacher/
│   └── admin/
├── styles/                       # 全局样式
│   ├── variables.css             # CSS 变量（与 04 文档一致）
│   ├── reset.css                 # 样式重置
│   └── transitions.css           # 路由转场动画
├── App.vue
└── main.ts
```

### 4.3.2 技术栈版本

| 依赖 | 版本 | 用途 |
| --- | --- | --- |
| Vue | 3.4+ | 前端框架 (Composition API) |
| TypeScript | 5.3+ | 类型系统 |
| Vite | 5.0+ | 构建工具 |
| Pinia | 2.1+ | 状态管理 |
| Vue Router | 4.2+ | 路由管理 |
| Axios | 1.6+ | HTTP 客户端 |
| Element Plus | 2.5+ | UI 组件库 |
| Lucide Vue Next | 0.300+ | 图标库 |
| ECharts | 5.5+ | 数据可视化（学情分析） |
| MSW | 2.0+ | Mock Service Worker |

---

## 4.4 核心 Composables

### 4.4.1 useSSEChat — SSE 流式问答

处理 RAG 问答的 SSE 流式响应，支持逐字输出、引用溯源、错误重试。

```typescript
// src/composables/useSSEChat.ts
import { ref, type Ref } from 'vue'
import type { QaLogDTO, ContextFragment } from '@/types/api'

/** 单条消息 */
export interface ChatMessage {
  id: string
  role: 'user' | 'assistant'
  content: string
  isStreaming?: boolean
  sources?: ContextFragment[]
  tokenUsage?: number
  error?: string
  createdAt: Date
}

/** useSSEChat 配置 */
interface UseSSEChatOptions {
  videoId: () => number           // 响应式获取当前视频 ID
  apiBase?: string                // API 基础路径，默认 /api
  onError?: (error: Error) => void
}

export function useSSEChat(options: UseSSEChatOptions) {
  const messages: Ref<ChatMessage[]> = ref([])
  const isStreaming = ref(false)
  const abortController = ref<AbortController | null>(null)

  /** 生成唯一 ID */
  function genId(): string {
    return `msg_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`
  }

  /**
   * 发送问题并接收 SSE 流式响应
   * @param question 用户问题
   * @param currentTime 当前视频播放时间（秒），用于 Rerank 加权
   */
  async function sendMessage(question: string, currentTime?: number) {
    if (isStreaming.value) return

    // 1. 添加用户消息
    const userMsg: ChatMessage = {
      id: genId(),
      role: 'user',
      content: question,
      createdAt: new Date(),
    }
    messages.value.push(userMsg)

    // 2. 创建 AI 占位消息
    const aiMsg: ChatMessage = {
      id: genId(),
      role: 'assistant',
      content: '',
      isStreaming: true,
      sources: [],
      createdAt: new Date(),
    }
    messages.value.push(aiMsg)

    // 3. 建立 SSE 连接
    isStreaming.value = true
    abortController.value = new AbortController()

    try {
      const response = await fetch(
        `${options.apiBase ?? '/api'}/qa/chat-stream`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
          },
          body: JSON.stringify({
            question,
            videoId: options.videoId(),
            currentTime,
          }),
          signal: abortController.value.signal,
        }
      )

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`)
      }

      const reader = response.body?.getReader()
      if (!reader) throw new Error('ReadableStream 不可用')

      const decoder = new TextDecoder()
      let buffer = ''

      while (true) {
        const { done, value } = await reader.read()
        if (done) break

        buffer += decoder.decode(value, { stream: true })

        // 按 \n\n 分割 SSE 事件
        const events = buffer.split('\n\n')
        buffer = events.pop() ?? ''  // 保留不完整的部分

        for (const event of events) {
          // 提取 data: 行
          const dataLine = event
            .split('\n')
            .find(line => line.startsWith('data:'))
          if (!dataLine) continue

          const jsonStr = dataLine.slice(5).trim()
          if (!jsonStr) continue

          try {
            const data = JSON.parse(jsonStr)

            switch (data.type) {
              case 'chunk':
                // 追加文本片段
                aiMsg.content += data.content ?? ''
                break

              case 'source':
                // 保存引用来源
                aiMsg.sources = data.fragments ?? []
                break

              case 'done':
                // 标记完成，记录 token 消耗
                aiMsg.isStreaming = false
                aiMsg.tokenUsage = data.tokenUsage ?? 0
                break

              case 'error':
                // 服务端错误
                aiMsg.isStreaming = false
                aiMsg.error = data.message ?? 'AI 回答出错'
                break
            }
          } catch {
            // JSON 解析失败，跳过该事件
          }
        }
      }
    } catch (error: any) {
      if (error.name === 'AbortError') {
        aiMsg.isStreaming = false
        aiMsg.content += '\n\n[已中断]'
      } else {
        aiMsg.isStreaming = false
        aiMsg.error = error.message ?? '网络错误'
        options.onError?.(error)
      }
    } finally {
      isStreaming.value = false
      abortController.value = null
    }
  }

  /** 中断当前流式响应 */
  function abortStream() {
    abortController.value?.abort()
  }

  /** 清空对话历史 */
  function clearMessages() {
    messages.value = []
  }

  return {
    messages,
    isStreaming,
    sendMessage,
    abortStream,
    clearMessages,
  }
}
```

#### 关键设计点

| 设计点 | 说明 |
| --- | --- |
| **AbortController** | 支持用户中断生成，避免资源浪费 |
| **buffer 拼接** | SSE 事件可能跨 chunk 到达，需缓冲拼接后再分割 |
| **data: 提取** | 只处理 `data:` 开头的行，忽略 `event:`、`id:`、`retry:` |
| **token 消耗** | 从 `done` 事件中提取，用于展示和统计 |
| **错误隔离** | 单条消息错误不影响历史消息，错误信息内联显示 |

---

### 4.4.2 useTheme — 多角色主题切换

根据用户角色自动切换主题，学生端支持暗色/亮色手动切换。

```typescript
// src/composables/useTheme.ts
import { ref, watchEffect } from 'vue'

const STORAGE_KEY = 'zhike-theme'

type ThemeValue = 'student-dark' | 'student-light' | 'teacher-light'

export function useTheme() {
  const currentTheme = ref<ThemeValue>('student-dark')

  /**
   * 初始化主题
   * @param roleCode 用户角色代码
   */
  function initTheme(roleCode: string) {
    // 1. 优先读取用户手动设置
    const saved = localStorage.getItem(STORAGE_KEY) as ThemeValue | null
    if (saved && isValidTheme(saved)) {
      currentTheme.value = saved
      applyTheme()
      return
    }

    // 2. 根据角色设置默认主题
    if (roleCode === 'STUDENT') {
      // 检测系统偏好
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      currentTheme.value = prefersDark ? 'student-dark' : 'student-light'
    } else {
      currentTheme.value = 'teacher-light'
    }
    applyTheme()
  }

  /** 验证主题值合法性 */
  function isValidTheme(value: string): value is ThemeValue {
    return ['student-dark', 'student-light', 'teacher-light'].includes(value)
  }

  /** 应用主题到 DOM */
  function applyTheme() {
    const root = document.documentElement
    root.setAttribute('data-theme', currentTheme.value)
    // 同步 color-scheme 属性，让原生控件跟随主题
    root.style.colorScheme = currentTheme.value === 'student-dark' ? 'dark' : 'light'
  }

  /** 切换学生端暗色/亮色 */
  function toggleTheme() {
    if (currentTheme.value === 'student-dark') {
      currentTheme.value = 'student-light'
    } else if (currentTheme.value === 'student-light') {
      currentTheme.value = 'student-dark'
    } else {
      return // 教师端不可切换
    }
    localStorage.setItem(STORAGE_KEY, currentTheme.value)
    applyTheme()
  }

  /** 是否为暗色主题 */
  const isDark = ref(true)

  watchEffect(() => {
    isDark.value = currentTheme.value === 'student-dark'
  })

  // 监听系统主题变化（仅学生端未手动设置时）
  if (typeof window !== 'undefined') {
    window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', (e) => {
        if (!localStorage.getItem(STORAGE_KEY)) {
          currentTheme.value = e.matches ? 'student-dark' : 'student-light'
          applyTheme()
        }
      })
  }

  return { currentTheme, isDark, initTheme, toggleTheme }
}
```

#### 主题映射表

| `data-theme` 值 | 角色 | 外观 | 可手动切换 |
| --- | --- | --- | --- |
| `student-dark` | 学生 | 暗色 (#1a1a2e 背景) | ↔ `student-light` |
| `student-light` | 学生 | 亮色 (#ffffff 背景) | ↔ `student-dark` |
| `teacher-light` | 教师/管理员 | 浅色 (#fdfdf8 背景) | ✗ 固定 |

---

### 4.4.3 useVideoPlayer — 视频播放器控制

封装视频播放、断点续播、心跳上报、知识点联动。

```typescript
// src/composables/useVideoPlayer.ts
import { ref, watch, onUnmounted } from 'vue'
import type { FragmentDTO } from '@/types/api'

interface UseVideoPlayerOptions {
  videoId: () => number
  signUrl: () => string
  duration: () => number
  fragments: () => FragmentDTO[]
  onProgress?: (currentTime: number) => void
}

export function useVideoPlayer(options: UseVideoPlayerOptions) {
  const videoRef = ref<HTMLVideoElement | null>(null)
  const currentTime = ref(0)
  const isPlaying = ref(false)
  const volume = ref(1)
  const lastWatchTime = ref<number | null>(null)

  // 心跳定时器
  let heartbeatTimer: ReturnType<typeof setInterval> | null = null
  const HEARTBEAT_INTERVAL = 30_000 // 30 秒

  /** 播放/暂停 */
  function togglePlay() {
    if (!videoRef.value) return
    if (videoRef.value.paused) {
      videoRef.value.play()
    } else {
      videoRef.value.pause()
    }
  }

  /** 跳转到指定时间点 */
  function seekTo(time: number) {
    if (!videoRef.value) return
    videoRef.value.currentTime = Math.max(0, Math.min(time, options.duration()))
    if (videoRef.value.paused) {
      videoRef.value.play()
    }
  }

  /** 跳转到知识点并高亮 */
  function seekToFragment(fragmentId: number) {
    const fragment = options.fragments().find(f => f.id === fragmentId)
    if (fragment) {
      seekTo(fragment.startTime)
    }
  }

  /** 获取当前播放位置附近的知识点 */
  function getNearbyFragments(rangeSeconds = 30): FragmentDTO[] {
    return options.fragments().filter(
      f => Math.abs(f.startTime - currentTime.value) <= rangeSeconds
    )
  }

  /** 启动心跳上报 */
  function startHeartbeat() {
    stopHeartbeat()
    heartbeatTimer = setInterval(async () => {
      if (!isPlaying.value) return
      try {
        await fetch('/api/student/heartbeat', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
          },
          body: JSON.stringify({
            videoId: options.videoId(),
            currentTime: Math.floor(currentTime.value),
            status: isPlaying.value ? 'PLAYING' : 'PAUSED',
          }),
        })
      } catch {
        // 心跳失败静默处理
      }
    }, HEARTBEAT_INTERVAL)
  }

  /** 停止心跳 */
  function stopHeartbeat() {
    if (heartbeatTimer) {
      clearInterval(heartbeatTimer)
      heartbeatTimer = null
    }
  }

  /** 上报学习进度 */
  async function reportProgress() {
    try {
      await fetch('/api/student/progress', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
        },
        body: JSON.stringify({
          videoId: options.videoId(),
          currentTime: Math.floor(currentTime.value),
          duration: options.duration(),
        }),
      })
    } catch {
      // 静默处理
    }
  }

  /** 初始化播放器事件监听 */
  function initPlayer() {
    const video = videoRef.value
    if (!video) return

    video.addEventListener('play', () => { isPlaying.value = true; startHeartbeat() })
    video.addEventListener('pause', () => { isPlaying.value = false; reportProgress() })
    video.addEventListener('timeupdate', () => { currentTime.value = video.currentTime })
    video.addEventListener('ended', () => { isPlaying.value = false; reportProgress(); stopHeartbeat() })

    // 断点续播
    if (lastWatchTime.value && lastWatchTime.value > 0) {
      video.currentTime = lastWatchTime.value
    }
  }

  // 键盘快捷键
  function handleKeydown(e: KeyboardEvent) {
    if (!videoRef.value) return
    switch (e.key) {
      case ' ':
        e.preventDefault()
        togglePlay()
        break
      case 'ArrowLeft':
        seekTo(currentTime.value - 5)
        break
      case 'ArrowRight':
        seekTo(currentTime.value + 5)
        break
      case 'ArrowUp':
        e.preventDefault()
        volume.value = Math.min(1, volume.value + 0.1)
        videoRef.value.volume = volume.value
        break
      case 'ArrowDown':
        e.preventDefault()
        volume.value = Math.max(0, volume.value - 0.1)
        videoRef.value.volume = volume.value
        break
      case 'f':
        videoRef.value.requestFullscreen?.()
        break
    }
  }

  onUnmounted(() => {
    stopHeartbeat()
    reportProgress()
  })

  return {
    videoRef,
    currentTime,
    isPlaying,
    volume,
    lastWatchTime,
    togglePlay,
    seekTo,
    seekToFragment,
    getNearbyFragments,
    initPlayer,
    handleKeydown,
  }
}
```

---

### 4.4.4 useNotes — 笔记管理

视频学习过程中的笔记 CRUD 操作。

```typescript
// src/composables/useNotes.ts
import { ref, type Ref } from 'vue'
import type { NoteDTO, NoteRequest } from '@/types/api'

interface UseNotesOptions {
  videoId: () => number
}

export function useNotes(options: UseNotesOptions) {
  const notes: Ref<NoteDTO[]> = ref([])
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  /** 获取当前视频的笔记列表 */
  async function fetchNotes() {
    isLoading.value = true
    error.value = null
    try {
      const res = await fetch(
        `/api/student/notes?videoId=${options.videoId()}`,
        { headers: { 'Authorization': `Bearer ${localStorage.getItem('zhike-token')}` } }
      )
      const json = await res.json()
      if (json.code === 200) {
        notes.value = json.data ?? []
      } else {
        error.value = json.message
      }
    } catch (e: any) {
      error.value = e.message
    } finally {
      isLoading.value = false
    }
  }

  /** 创建笔记 */
  async function createNote(data: NoteRequest): Promise<NoteDTO | null> {
    try {
      const res = await fetch('/api/student/notes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
        },
        body: JSON.stringify({ ...data, videoId: options.videoId() }),
      })
      const json = await res.json()
      if (json.code === 200) {
        const note = json.data as NoteDTO
        notes.value.unshift(note)  // 插入到列表头部
        return note
      }
      error.value = json.message
      return null
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }

  /** 更新笔记 */
  async function updateNote(id: number, data: Partial<NoteRequest>): Promise<boolean> {
    try {
      const res = await fetch(`/api/student/notes/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
        },
        body: JSON.stringify(data),
      })
      const json = await res.json()
      if (json.code === 200) {
        const idx = notes.value.findIndex(n => n.id === id)
        if (idx !== -1) {
          notes.value[idx] = { ...notes.value[idx], ...json.data }
        }
        return true
      }
      return false
    } catch {
      return false
    }
  }

  /** 删除笔记 */
  async function deleteNote(id: number): Promise<boolean> {
    try {
      const res = await fetch(`/api/student/notes/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${localStorage.getItem('zhike-token')}` },
      })
      const json = await res.json()
      if (json.code === 200) {
        notes.value = notes.value.filter(n => n.id !== id)
        return true
      }
      return false
    } catch {
      return false
    }
  }

  /** 切换笔记公开/私有 */
  async function togglePublic(id: number): Promise<boolean> {
    const note = notes.value.find(n => n.id === id)
    if (!note) return false
    return updateNote(id, { isPublic: !note.isPublic })
  }

  return {
    notes,
    isLoading,
    error,
    fetchNotes,
    createNote,
    updateNote,
    deleteNote,
    togglePublic,
  }
}
```

---

### 4.4.5 usePagination — 通用分页

封装分页状态和请求逻辑，适用于所有列表接口。

```typescript
// src/composables/usePagination.ts
import { ref, reactive, type Ref } from 'vue'

interface PaginationState {
  page: number
  pageSize: number
  total: number
  totalPages: number
}

interface UsePaginationOptions<T> {
  fetchFn: (page: number, pageSize: number) => Promise<{
    records: T[]
    total: number
    totalPages: number
  }>
  defaultPageSize?: number
  immediate?: boolean
}

export function usePagination<T>(options: UsePaginationOptions<T>) {
  const data: Ref<T[]> = ref([])
  const isLoading = ref(false)
  const pagination = reactive<PaginationState>({
    page: 1,
    pageSize: options.defaultPageSize ?? 10,
    total: 0,
    totalPages: 0,
  })

  /** 加载指定页数据 */
  async function loadData(page?: number) {
    isLoading.value = true
    try {
      const targetPage = page ?? pagination.page
      const result = await options.fetchFn(targetPage, pagination.pageSize)
      data.value = result.records
      pagination.total = result.total
      pagination.totalPages = result.totalPages
      pagination.page = targetPage
    } catch (error) {
      console.error('分页加载失败:', error)
    } finally {
      isLoading.value = false
    }
  }

  /** 切换页码 */
  function changePage(page: number) {
    loadData(page)
  }

  /** 切换每页条数 */
  function changePageSize(size: number) {
    pagination.pageSize = size
    pagination.page = 1
    loadData(1)
  }

  /** 刷新当前页 */
  function refresh() {
    loadData()
  }

  // 自动加载
  if (options.immediate !== false) {
    loadData()
  }

  return {
    data,
    isLoading,
    pagination,
    loadData,
    changePage,
    changePageSize,
    refresh,
  }
}
```

---

### 4.4.6 useNotification — 通知中心

管理通知列表、未读计数、实时推送。

```typescript
// src/composables/useNotification.ts
import { ref, computed } from 'vue'

export interface Notification {
  id: number
  type: 'system' | 'course' | 'interaction'
  title: string
  description: string
  isRead: boolean
  link?: string          // 点击跳转路由
  createdAt: string
}

export function useNotification() {
  const notifications = ref<Notification[]>([])
  const activeTab = ref<'all' | 'system' | 'course' | 'interaction'>('all')
  const isDrawerOpen = ref(false)

  /** 未读总数 */
  const unreadCount = computed(() =>
    notifications.value.filter(n => !n.isRead).length
  )

  /** 按分类筛选 */
  const filteredNotifications = computed(() => {
    if (activeTab.value === 'all') return notifications.value
    return notifications.value.filter(n => n.type === activeTab.value)
  })

  /** 打开通知面板 */
  function openDrawer() {
    isDrawerOpen.value = true
  }

  /** 关闭通知面板 */
  function closeDrawer() {
    isDrawerOpen.value = false
  }

  /** 标记单条已读 */
  function markAsRead(id: number) {
    const notification = notifications.value.find(n => n.id === id)
    if (notification) {
      notification.isRead = true
    }
  }

  /** 全部标记已读 */
  function markAllAsRead() {
    notifications.value.forEach(n => { n.isRead = true })
  }

  /** 添加新通知（模拟推送） */
  function pushNotification(notification: Notification) {
    notifications.value.unshift(notification)
  }

  return {
    notifications,
    activeTab,
    isDrawerOpen,
    unreadCount,
    filteredNotifications,
    openDrawer,
    closeDrawer,
    markAsRead,
    markAllAsRead,
    pushNotification,
  }
}
```

---

## 4.5 状态管理与路由

### 4.5.1 useUserStore — 用户认证 Store

```typescript
// src/stores/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfoDTO, RoleDTO, LoginResponse } from '@/types/api'

const TOKEN_KEY = 'zhike-token'
const USER_KEY = 'zhike-user'

export const useUserStore = defineStore('user', () => {
  // ─── State ───
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) ?? '')
  const userInfo = ref<UserInfoDTO | null>(
    JSON.parse(localStorage.getItem(USER_KEY) ?? 'null')
  )
  const roles = ref<RoleDTO[]>([])

  // ─── Getters ───
  const isLoggedIn = computed(() => !!token.value)
  const roleCode = computed(() => userInfo.value?.roleCode ?? '')
  const isStudent = computed(() => roleCode.value === 'STUDENT')
  const isTeacher = computed(() => roleCode.value === 'TEACHER')
  const isAdmin = computed(() => roleCode.value === 'ADMIN')

  /** 获取首页路由路径 */
  const homeRoute = computed(() => {
    switch (roleCode.value) {
      case 'STUDENT': return '/student/home'
      case 'TEACHER': return '/teacher/home'
      case 'ADMIN':   return '/admin/users'
      default:        return '/login'
    }
  })

  // ─── Actions ───
  async function login(username: string, password: string) {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    })
    const json = await response.json()

    if (json.code !== 200) {
      throw new Error(json.message ?? '登录失败')
    }

    const data = json.data as LoginResponse
    setAuth(data)
    return data
  }

  async function fetchUserInfo() {
    const response = await fetch('/api/auth/info', {
      headers: { 'Authorization': `Bearer ${token.value}` },
    })
    const json = await response.json()
    if (json.code === 200) {
      userInfo.value = json.data.userInfo
      roles.value = json.data.roles ?? []
      localStorage.setItem(USER_KEY, JSON.stringify(userInfo.value))
    }
  }

  function setAuth(data: LoginResponse) {
    token.value = data.token
    userInfo.value = data.userInfo
    roles.value = data.roles ?? []
    localStorage.setItem(TOKEN_KEY, data.token)
    localStorage.setItem(USER_KEY, JSON.stringify(data.userInfo))
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    roles.value = []
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return {
    token, userInfo, roles,
    isLoggedIn, roleCode, isStudent, isTeacher, isAdmin, homeRoute,
    login, fetchUserInfo, setAuth, logout,
  }
})
```

### 4.5.2 路由配置与守卫

```typescript
// src/router/routes.ts
import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false, title: '登录' },
  },
  // ─── 学生端路由 ───
  {
    path: '/student',
    component: () => import('@/layouts/StudentLayout.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' },
    children: [
      { path: 'home', name: 'StudentHome', component: () => import('@/views/student/Home.vue'), meta: { title: '首页' } },
      { path: 'course/:courseId', name: 'CourseDetail', component: () => import('@/views/student/CourseDetail.vue'), meta: { title: '课程详情' } },
      { path: 'learn/:videoId', name: 'VideoLearning', component: () => import('@/views/student/VideoLearning.vue'), meta: { title: '视频学习' } },
      { path: 'qa/history', name: 'QaHistory', component: () => import('@/views/student/QaHistory.vue'), meta: { title: '问答历史' } },
      { path: 'notes', name: 'NotesList', component: () => import('@/views/student/NotesList.vue'), meta: { title: '我的笔记' } },
    ],
  },
  // ─── 教师端路由 ───
  {
    path: '/teacher',
    component: () => import('@/layouts/TeacherLayout.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' },
    children: [
      { path: 'home', name: 'TeacherHome', component: () => import('@/views/teacher/Home.vue'), meta: { title: '仪表盘' } },
      { path: 'knowledge', name: 'KnowledgeManage', component: () => import('@/views/teacher/KnowledgeManage.vue'), meta: { title: '知识片段管理' } },
      { path: 'upload', name: 'VideoUpload', component: () => import('@/views/teacher/VideoUpload.vue'), meta: { title: '视频上传' } },
      { path: 'analytics', name: 'Analytics', component: () => import('@/views/teacher/Analytics.vue'), meta: { title: '学情分析' } },
    ],
  },
  // ─── 管理员路由 ───
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      { path: 'users', name: 'UserManage', component: () => import('@/views/admin/UserManage.vue'), meta: { title: '用户管理' } },
    ],
  },
  // ─── 通用路由 ───
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/settings/index.vue'),
    meta: { requiresAuth: true, title: '个人设置' },
  },
  // ─── 404 ───
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' },
  },
]
```

```typescript
// src/router/guards.ts
import type { Router } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useTheme } from '@/composables/useTheme'

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to, from) => {
    const userStore = useUserStore()

    // 1. 不需要认证的页面直接放行
    if (to.meta.requiresAuth === false) {
      // 已登录用户访问登录页，重定向到首页
      if (to.name === 'Login' && userStore.isLoggedIn) {
        return userStore.homeRoute
      }
      return true
    }

    // 2. 未登录 → 跳转登录页
    if (!userStore.isLoggedIn) {
      return { name: 'Login', query: { redirect: to.fullPath } }
    }

    // 3. 角色权限检查
    const requiredRole = to.meta.role as string | undefined
    if (requiredRole && userStore.roleCode !== requiredRole) {
      // 无权访问，重定向到自己的首页
      return userStore.homeRoute
    }

    // 4. 初始化主题（首次进入时）
    if (userStore.roleCode) {
      const { initTheme } = useTheme()
      initTheme(userStore.roleCode)
    }

    // 5. 设置页面标题
    if (to.meta.title) {
      document.title = `${to.meta.title} - ZhiKe`
    }

    return true
  })
}
```

---

## 4.6 前后端对接规范

### 4.6.1 Axios 实例与拦截器

```typescript
// src/utils/request.ts
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

/** 创建 Axios 实例 */
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api',
  timeout: 15_000,
  headers: { 'Content-Type': 'application/json' },
})

// ─── 请求拦截器 ───
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('zhike-token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// ─── 响应拦截器 ───
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data

    if (code === 200) {
      return data  // 直接返回业务数据，剥离外层包装
    }

    // 业务错误
    ElMessage.error(message ?? '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    if (error.response) {
      const { status } = error.response

      switch (status) {
        case 401:
          // Token 过期，自动登出
          const userStore = useUserStore()
          userStore.logout()
          window.location.href = '/login'
          ElMessage.error('登录已过期，请重新登录')
          break
        case 403:
          ElMessage.error('无权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(error.response.data?.message ?? '请求失败')
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络')
    } else {
      ElMessage.error('网络连接失败')
    }

    return Promise.reject(error)
  }
)

export default request
```

### 4.6.2 API 请求层示例

```typescript
// src/api/auth.ts
import request from '@/utils/request'
import type { LoginRequest, RegisterRequest, LoginResponse, UserInfoDTO } from '@/types/api'

export const authApi = {
  login: (data: LoginRequest) =>
    request.post<any, LoginResponse>('/auth/login', data),

  register: (data: RegisterRequest) =>
    request.post<any, LoginResponse>('/auth/register', data),

  getUserInfo: () =>
    request.get<any, UserInfoDTO>('/auth/info'),

  refreshToken: () =>
    request.post<any, { token: string }>('/auth/refresh'),
}
```

```typescript
// src/api/qa.ts
import request from '@/utils/request'
import type { RagChatRequest, QaLogDTO, PageResult } from '@/types/api'

export const qaApi = {
  /** SSE 流式问答（不使用 Axios，直接 fetch） */
  chatStreamUrl: '/qa/chat-stream',

  getHistory: (params: { page: number; pageSize: number }) =>
    request.get<any, PageResult<QaLogDTO>>('/qa/history', { params }),
}
```

### 4.6.3 字段命名转换

后端 Java `camelCase` → Jackson `SnakeCaseStrategy` → 网络 `snake_case` → Axios 拦截器 → 前端 TypeScript `camelCase`。

| 前端 TS 字段 | 网络 snake_case | 后端 Java 字段 | 数据库字段 |
| --- | --- | --- | --- |
| userId | user_id | userId | id (users) |
| createdAt | created_at | createdAt | created_at |
| parseStatus | parse_status | parseStatus | parse_status |
| videoId | video_id | videoId | video_id |
| tokenUsage | token_usage | tokenUsage | token_usage |
| avatarUrl | avatar_url | avatarUrl | avatar_url |
| roleCode | role_code | roleCode | role_code |
| courseId | course_id | courseId | course_id |
| ossUrl | oss_url | ossUrl | oss_url |
| orderNum | order_num | orderNum | order_num |
| keyframeUrl | keyframe_url | keyFrameUrl | keyframe_url |
| asrText | asr_text | asrText | asr_text |
| visualTags | visual_tags | visualTags | visual_tags |
| contentText | content_text | contentText | content_text |
| vectorId | vector_id | vectorId | vector_id |
| fragmentId | fragment_id | fragmentId | fragment_id |
| currentTime | current_time | currentTime | — (请求参数) |
| lastWatchTime | last_watch_time | lastWatchTime | last_watch_time |
| isCompleted | is_completed | isCompleted | is_completed |
| isPublished | is_published | isPublished | is_published |
| totalPages | total_pages | totalPages | — (计算字段) |
| signUrl | sign_url | signUrl | — (签名 URL) |
| uploadId | upload_id | uploadId | — (OSS) |

> **规则**：前端代码统一 `camelCase`，网络传输统一 `snake_case`。Axios 响应拦截器负责自动转换。DTO 类型定义（`src/types/api.ts`）与 `03-API接口规范.md` 3.9 节严格一致，直接复制使用。

---

## 4.7 Mock 数据开发模式

### 4.7.1 架构概述

前端采用 **MSW (Mock Service Worker)** 拦截网络请求，在浏览器 Service Worker 层返回 Mock 数据，实现前后端并行开发。

```
┌─────────────┐    ┌─────────────┐    ┌──────────────┐
│  Vue 组件    │───→│  Axios/Fetch │───→│  MSW Service  │
│  (业务代码)  │    │  (正常请求)   │    │  Worker 拦截  │
└─────────────┘    └─────────────┘    └──────┬───────┘
                                            │
                                   ┌────────▼────────┐
                                   │  Mock Handler    │
                                   │  (返回假数据)    │
                                   └─────────────────┘

联调时关闭 MSW → 请求直接转发到真实后端
```

**开发流程：**

```
1. 启动 Vite 开发服务器 (npm run dev)
2. MSW 自动注入 Service Worker (main.ts 条件启动)
3. 前端发起请求 → MSW 拦截 → 返回 Mock 数据 → 前端正常渲染
4. 联调时设置 VITE_ENABLE_MOCK=false → 请求转发到真实后端
```

### 4.7.2 MSW 配置与启动

```typescript
// src/mocks/browser.ts
import { setupWorker } from 'msw/browser'
import { authHandlers } from './handlers/auth.handler'
import { videoHandlers } from './handlers/video.handler'
import { qaHandlers } from './handlers/qa.handler'
import { analyticsHandlers } from './handlers/analytics.handler'
import { studentHandlers } from './handlers/student.handler'
import { adminHandlers } from './handlers/admin.handler'

export const worker = setupWorker(
  ...authHandlers,
  ...videoHandlers,
  ...qaHandlers,
  ...analyticsHandlers,
  ...studentHandlers,
  ...adminHandlers,
)
```

```typescript
// src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

async function bootstrap() {
  const app = createApp(App)
  app.use(createPinia())
  app.use(router)

  // 开发环境启动 MSW
  if (import.meta.env.DEV && import.meta.env.VITE_ENABLE_MOCK === 'true') {
    const { worker } = await import('./mocks/browser')
    await worker.start({ onUnhandledRequest: 'bypass' })
    console.log('[MSW] Mock Service Worker 已启动')
  }

  app.mount('#app')
}

bootstrap()
```

### 4.7.3 Mock Handler 完整示例

#### 认证 Handler

```typescript
// src/mocks/handlers/auth.handler.ts
import { http, HttpResponse, delay } from 'msw'
import { mockUsers, mockAdminUsers } from '../data/auth.data'

export const authHandlers = [
  // 登录
  http.post('/api/auth/login', async ({ request }) => {
    await delay(500)
    const body = await request.json() as { username: string; password: string }

    const user = mockUsers[body.username]
    if (!user) {
      return HttpResponse.json(
        { code: 401, message: '用户名或密码错误', data: null, timestamp: new Date().toISOString() },
        { status: 401 }
      )
    }

    return HttpResponse.json({
      code: 200,
      message: '登录成功',
      data: user,
      timestamp: new Date().toISOString(),
    })
  }),

  // 获取用户信息
  http.get('/api/auth/info', async ({ request }) => {
    await delay(200)
    const authHeader = request.headers.get('Authorization')
    if (!authHeader) {
      return HttpResponse.json(
        { code: 401, message: '未认证', data: null, timestamp: new Date().toISOString() },
        { status: 401 }
      )
    }
    // 从 token 中解析用户（Mock 简化处理）
    const token = authHeader.replace('Bearer ', '')
    const mockUser = Object.values(mockUsers).find(u => u.token === token)
    if (!mockUser) {
      return HttpResponse.json({ code: 401, message: 'Token 无效', data: null, timestamp: new Date().toISOString() }, { status: 401 })
    }
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: { userInfo: mockUser.userInfo, roles: mockUser.roles },
      timestamp: new Date().toISOString(),
    })
  }),
]
```

#### SSE 问答 Handler（核心）

```typescript
// src/mocks/handlers/qa.handler.ts
import { http, HttpResponse, delay } from 'msw'
import { mockQaHistory } from '../data/qa.data'

/** 创建 SSE 流式响应 */
function createSSEStream(answer: string, sources: Array<{
  fragmentId: number; startTime: number; endTime: number; relevance: number
}>) {
  const encoder = new TextEncoder()

  return new ReadableStream({
    async start(controller) {
      // 阶段 1：逐字发送 chunk 事件（模拟流式输出）
      for (let i = 0; i < answer.length; i += Math.floor(2 + Math.random() * 4)) {
        const chunk = answer.slice(i, i + Math.floor(2 + Math.random() * 4))
        const event = `data:${JSON.stringify({ type: 'chunk', content: chunk })}\n\n`
        controller.enqueue(encoder.encode(event))
        await new Promise(r => setTimeout(r, 25 + Math.random() * 45))
      }

      // 阶段 2：发送 source 事件（引用来源）
      await delay(100)
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'source', fragments: sources })}\n\n`
      ))

      // 阶段 3：发送 done 事件（完成标记）
      await delay(50)
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'done', tokenUsage: Math.floor(200 + Math.random() * 300) })}\n\n`
      ))

      controller.close()
    },
  })
}

export const qaHandlers = [
  // SSE 流式问答
  http.post('/api/qa/chat-stream', async ({ request }) => {
    await delay(300) // 模拟 RAG 检索延迟

    const body = await request.json() as { question: string; videoId: number }
    const mockAnswer = `关于"${body.question}"，根据视频内容分析：\n\n` +
      `这个知识点主要讲解了微积分中的导数概念。导数是函数在某一点处的变化率，` +
      `几何意义上表示曲线在该点的切线斜率。\n\n` +
      `关键要点：\n` +
      `1. 导数的定义：lim(Δx→0) [f(x+Δx) - f(x)] / Δx\n` +
      `2. 常见求导法则：幂法则、乘法法则、链式法则\n` +
      `3. 导数的应用：求极值、判断单调性`

    const mockSources = [
      { fragmentId: 1, startTime: 120.5, endTime: 185.0, relevance: 0.95 },
      { fragmentId: 3, startTime: 240.0, endTime: 310.5, relevance: 0.82 },
      { fragmentId: 5, startTime: 450.0, endTime: 520.0, relevance: 0.71 },
    ]

    return new HttpResponse(createSSEStream(mockAnswer, mockSources), {
      headers: {
        'Content-Type': 'text/event-stream',
        'Cache-Control': 'no-cache',
        'Connection': 'keep-alive',
      },
    })
  }),

  // 问答历史
  http.get('/api/qa/history', async ({ request }) => {
    await delay(300)
    const url = new URL(request.url)
    const page = parseInt(url.searchParams.get('page') ?? '1')
    const pageSize = parseInt(url.searchParams.get('pageSize') ?? '10')

    const start = (page - 1) * pageSize
    const records = mockQaHistory.slice(start, start + pageSize)

    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: {
        records,
        total: mockQaHistory.length,
        page,
        pageSize,
        totalPages: Math.ceil(mockQaHistory.length / pageSize),
      },
      timestamp: new Date().toISOString(),
    })
  }),
]
```

### 4.7.4 Mock 数据源

#### 认证数据

```typescript
// src/mocks/data/auth.data.ts
import type { LoginResponse, RoleDTO } from '@/types/api'

export const mockRoles: RoleDTO[] = [
  { roleCode: 'STUDENT', roleName: '学生', description: '可观看视频、提问' },
  { roleCode: 'TEACHER', roleName: '教师', description: '可管理课程' },
  { roleCode: 'ADMIN',   roleName: '管理员', description: '系统管理' },
]

export const mockUsers: Record<string, LoginResponse> = {
  student: {
    token: 'mock-jwt-student-token-xxx',
    userInfo: {
      userId: 1, username: 'zhangsan', email: 'zhangsan@edu.cn',
      avatarUrl: null, roleCode: 'STUDENT', roleName: '学生',
    },
    roles: [mockRoles[0]],
  },
  teacher: {
    token: 'mock-jwt-teacher-token-xxx',
    userInfo: {
      userId: 2, username: 'lisi', email: 'lisi@edu.cn',
      avatarUrl: '/avatars/lisi.png', roleCode: 'TEACHER', roleName: '教师',
    },
    roles: [mockRoles[1]],
  },
  admin: {
    token: 'mock-jwt-admin-token-xxx',
    userInfo: {
      userId: 3, username: 'admin', email: 'admin@edu.cn',
      avatarUrl: null, roleCode: 'ADMIN', roleName: '管理员',
    },
    roles: [mockRoles[2]],
  },
}

export const mockAdminUsers = [
  { userId: 1, username: 'zhangsan', email: 'zhangsan@edu.cn', roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-01T10:00:00+08:00' },
  { userId: 2, username: 'lisi', email: 'lisi@edu.cn', roleCode: 'TEACHER', roleName: '教师', status: 1, createdAt: '2024-08-15T14:30:00+08:00' },
  { userId: 3, username: 'wangwu', email: 'wangwu@edu.cn', roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-05T09:00:00+08:00' },
  { userId: 4, username: 'zhaoliu', email: 'zhaoliu@edu.cn', roleCode: 'STUDENT', roleName: '学生', status: 0, createdAt: '2024-09-10T16:00:00+08:00' },
  { userId: 5, username: 'sunqi', email: 'sunqi@edu.cn', roleCode: 'TEACHER', roleName: '教师', status: 1, createdAt: '2024-08-20T11:00:00+08:00' },
]
```

#### 问答历史数据

```typescript
// src/mocks/data/qa.data.ts
import type { QaLogDTO } from '@/types/api'

export const mockQaHistory: QaLogDTO[] = Array.from({ length: 25 }, (_, i) => ({
  id: i + 1,
  question: [
    '导数的几何意义是什么？',
    '如何理解极限的概念？',
    '链式法则怎么应用？',
    '积分和导数有什么关系？',
    '泰勒展开的用途是什么？',
  ][i % 5],
  answer: [
    '导数的几何意义是函数曲线在某一点处的切线斜率...',
    '极限描述的是当自变量趋近于某个值时，函数值的变化趋势...',
    '链式法则用于求复合函数的导数，外层函数的导数乘以内层函数的导数...',
    '积分是导数的逆运算，牛顿-莱布尼茨公式建立了两者之间的联系...',
    '泰勒展开用多项式近似表示函数，在数值计算和物理中有广泛应用...',
  ][i % 5],
  contextFragments: [
    { fragmentId: (i % 3) + 1, startTime: 120 + i * 10, endTime: 185 + i * 10, relevance: 0.9 - (i % 3) * 0.1 },
  ],
  modelVersion: 'GLM-4',
  tokenUsage: 200 + Math.floor(Math.random() * 300),
  createdAt: new Date(Date.now() - i * 3600_000).toISOString(),
}))
```

### 4.7.5 Mock 文件覆盖矩阵

| Mock 文件 | 覆盖接口 | 核心数据 | 数据量 |
| --- | --- | --- | --- |
| `auth.handler.ts` | `/api/auth/login`, `/api/auth/info`, `/api/auth/refresh` | 多角色用户 (STUDENT/TEACHER/ADMIN) | 3 个用户 |
| `video.handler.ts` | `/api/video/upload-signature`, `/api/video/upload-complete`, `/api/video/:id/status`, `/api/video/:id/sign-url`, `/api/teacher/videos`, `/api/teacher/fragments` | 视频列表、知识片段、解析状态、上传签名 | 8 个视频, 24 个片段 |
| `qa.handler.ts` | `/api/qa/chat-stream` (SSE), `/api/qa/history` | SSE 流式模拟 + 问答历史 | 25 条历史 |
| `analytics.handler.ts` | `/api/analytics/heatmap`, `/api/analytics/class-overview`, `/api/analytics/student-profile`, `/api/analytics/wordcloud` | 热力图、班级概览、学生画像、词云 | 按课程生成 |
| `student.handler.ts` | `/api/student/courses`, `/api/student/courses/:id`, `/api/student/videos/:id/play-info`, `/api/student/notes`, `/api/student/heartbeat`, `/api/student/progress` | 课程列表、播放信息、笔记、心跳 | 5 个课程, 12 个笔记 |
| `admin.handler.ts` | `/api/admin/users`, `/api/admin/users/:id` (PUT/DELETE) | 用户列表、角色管理 | 20 个用户 |

### 4.7.6 环境变量与开关控制

```bash
# .env.development
VITE_ENABLE_MOCK=true
VITE_API_BASE_URL=http://localhost:8080

# .env.production
VITE_ENABLE_MOCK=false
VITE_API_BASE_URL=https://api.zhike.edu.cn
```

```typescript
// src/utils/request.ts
const useMock = import.meta.env.VITE_ENABLE_MOCK === 'true'
// MSW 在 main.ts 中根据此变量决定是否启动
```

### 4.7.7 Mock 数据类型安全策略

为确保 Mock 数据与真实 API 的 DTO 类型严格一致，采用以下策略：

| 策略 | 实现方式 |
| --- | --- |
| **类型导入** | Mock 数据文件从 `@/types/api` 导入 DTO 类型 |
| **TypeScript 严格模式** | `tsconfig.json` 启用 `strict: true` + `noUncheckedIndexedAccess` |
| **as 类型断言** | Handler 返回数据使用 `as Result<T>` 断言 |
| **MSW 类型推断** | `http.post<PostBody, Result<ResponseType>>()` 泛型参数 |
| **CI 检查** | `tsc --noEmit` 确保类型无错误 |

```typescript
// 类型安全示例
import type { Result, PageResult, QaLogDTO } from '@/types/api'

// ✅ 正确：使用泛型约束
http.get('/api/qa/history', async () => {
  const data: Result<PageResult<QaLogDTO>> = {
    code: 200,
    message: '操作成功',
    data: { records: mockQaHistory, total: 25, page: 1, pageSize: 10, totalPages: 3 },
    timestamp: new Date().toISOString(),
  }
  return HttpResponse.json(data)
})

// ❌ 错误：字段名或类型不匹配会在 tsc 时报错
```

---

## 4.8 组件单元测试规范

### 4.8.1 测试工具

| 工具 | 版本 | 用途 |
| --- | --- | --- |
| Vitest | 1.0+ | 单元测试框架 |
| @vue/test-utils | 2.4+ | Vue 组件测试 |
| msw | 2.0+ | 测试中 Mock API |

### 4.8.2 测试策略

| 测试类型 | 覆盖目标 | 示例 |
| --- | --- | --- |
| **Composable 测试** | 核心业务逻辑 | useSSEChat 流式解析、useTheme 切换 |
| **Store 测试** | 状态管理 | useUserStore 登录/登出/权限判断 |
| **组件测试** | UI 交互 | ThemeToggle 点击切换、ReferenceTag 跳转 |
| **Handler 测试** | Mock 数据 | auth.handler 登录成功/失败 |

### 4.8.3 Composable 测试示例

```typescript
// tests/composables/useSSEChat.test.ts
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useSSEChat } from '@/composables/useSSEChat'

describe('useSSEChat', () => {
  it('应正确解析 SSE chunk 事件并追加内容', async () => {
    const mockFetch = vi.fn(() => ({
      ok: true,
      body: {
        getReader: () => ({
          read: () => Promise.resolve({
            done: false,
            value: new TextEncoder().encode(
              `data:${JSON.stringify({ type: 'chunk', content: '你好' })}\n\n`
            ),
          }),
        }),
      },
    }))
    vi.stubGlobal('fetch', mockFetch)

    const { messages, sendMessage } = useSSEChat({
      videoId: () => 1,
    })

    await sendMessage('测试问题')

    expect(messages.value).toHaveLength(2) // 用户消息 + AI 消息
    expect(messages.value[0].role).toBe('user')
    expect(messages.value[1].role).toBe('assistant')
    expect(messages.value[1].content).toContain('你好')
  })

  it('应在 done 事件后标记 isStreaming 为 false', async () => {
    // ... 类似测试，验证 done 事件处理
  })

  it('应在网络错误时设置 error 属性', async () => {
    // ... 验证错误处理
  })

  it('abortStream 应中断请求', async () => {
    // ... 验证中断逻辑
  })
})
```

```typescript
// tests/composables/useTheme.test.ts
import { describe, it, expect, vi } from 'vitest'
import { useTheme } from '@/composables/useTheme'

describe('useTheme', () => {
  it('学生角色应默认暗色主题', () => {
    const { initTheme, currentTheme } = useTheme()
    initTheme('STUDENT')
    expect(currentTheme.value).toBe('student-dark')
  })

  it('教师角色应固定浅色主题', () => {
    const { initTheme, currentTheme } = useTheme()
    initTheme('TEACHER')
    expect(currentTheme.value).toBe('teacher-light')
  })

  it('toggleTheme 应在暗色和亮色间切换', () => {
    const { initTheme, toggleTheme, currentTheme } = useTheme()
    initTheme('STUDENT')
    expect(currentTheme.value).toBe('student-dark')

    toggleTheme()
    expect(currentTheme.value).toBe('student-light')

    toggleTheme()
    expect(currentTheme.value).toBe('student-dark')
  })

  it('教师端 toggleTheme 不应生效', () => {
    const { initTheme, toggleTheme, currentTheme } = useTheme()
    initTheme('TEACHER')
    toggleTheme()
    expect(currentTheme.value).toBe('teacher-light')
  })
})
```

### 4.8.4 测试命令

```bash
# 运行所有测试
npm run test

# 运行特定文件
npx vitest run tests/composables/useSSEChat.test.ts

# 监听模式（开发时使用）
npm run test:watch

# 生成覆盖率报告
npm run test:coverage
```

```json
// package.json scripts
{
  "scripts": {
    "test": "vitest run",
    "test:watch": "vitest",
    "test:coverage": "vitest run --coverage"
  }
}
```
