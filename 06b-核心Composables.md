# 4.4 核心 Composables（上）

> 来源：`06-组件逻辑与Mock开发.md` 拆分文件 B
>
> 本文件包含 3 个核心业务 Composable 的完整实现代码。
>
> **关联文件**：
> - `06a-项目结构与类型定义.md` — 项目结构 / DTO 类型定义 / 字段映射
> - `06c-通用Composables与状态管理.md` — useNotes / usePagination / useNotification / Store / 路由 / Axios
> - `06d-Mock数据与测试规范.md` — MSW Mock / 单元测试

---

## 4.4.1 useSSEChat — SSE 流式问答

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

## 4.4.2 useTheme — 多角色主题切换

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

## 4.4.3 useVideoPlayer — 视频播放器控制

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
