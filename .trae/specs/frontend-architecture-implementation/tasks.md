# 智能教学闭环系统前端架构 - 实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现完整的智能教学闭环系统前端架构，包含学生端、教师端和管理员端的所有核心功能。

**Architecture:** 基于 Vue 3 Composition API + TypeScript + Vite 技术栈，采用 Composables 模式实现核心业务逻辑，Pinia 管理状态，Vue Router 处理路由，MSW 提供 Mock 数据。

**Tech Stack:** Vue 3.4+, TypeScript 5.3+, Vite 5.0+, Pinia 2.1+, Vue Router 4.2+, Axios 1.6+, Element Plus 2.5+, MSW 2.0+

---

## [ ] Task 1: 项目结构搭建
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 按照 06-组件逻辑与Mock开发.md 文档创建完整的目录结构
  - 初始化必要的配置文件和基础代码
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**: 
  - `programmatic` TR-1.1: 所有目录结构符合文档规范
  - `programmatic` TR-1.2: 基础配置文件正确初始化
- **Notes**: 确保遵循文档中定义的目录规范和技术栈版本

### Task 1 Steps:
- [ ] **Step 1: 创建目录结构**

```bash
# 创建主要目录结构
mkdir -p frontend/src/api frontend/src/composables frontend/src/stores frontend/src/router frontend/src/types frontend/src/utils frontend/src/mocks/handlers frontend/src/mocks/data frontend/src/components frontend/src/views/login frontend/src/views/student frontend/src/views/teacher frontend/src/views/admin frontend/src/styles
```

- [ ] **Step 2: 初始化 package.json**

```json
{
  "name": "zhike-frontend",
  "private": true,
  "version": "0.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc && vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "vue": "^3.4.0",
    "pinia": "^2.1.0",
    "vue-router": "^4.2.0",
    "axios": "^1.6.0",
    "element-plus": "^2.5.0",
    "lucide-vue-next": "^0.300.0",
    "echarts": "^5.5.0",
    "msw": "^2.0.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "typescript": "^5.3.0",
    "vite": "^5.0.0",
    "vue-tsc": "^1.8.0"
  }
}
```

- [ ] **Step 3: 创建基础配置文件**

```bash
# 创建 tsconfig.json
touch frontend/tsconfig.json frontend/tsconfig.node.json frontend/vite.config.ts frontend/index.html
```

## [ ] Task 2: 类型定义实现
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 实现 TypeScript 类型定义，包括 API DTO、环境变量类型和组件类型
  - 确保类型定义与 03-API接口规范.md 3.9 节严格一致
- **Acceptance Criteria Addressed**: AC-1, AC-6
- **Test Requirements**: 
  - `programmatic` TR-2.1: 所有类型定义正确，无 TypeScript 错误
  - `programmatic` TR-2.2: 类型定义覆盖所有 API 接口和组件
- **Notes**: 类型定义是整个项目的基础，确保准确性和完整性

### Task 2 Steps:
- [ ] **Step 1: 创建类型定义文件**

```typescript
// frontend/src/types/api.ts
export interface UserInfoDTO {
  userId: number
  username: string
  email: string
  avatarUrl: string | null
  roleCode: string
  roleName: string
}

export interface RoleDTO {
  roleCode: string
  roleName: string
  description: string
}

export interface LoginResponse {
  token: string
  userInfo: UserInfoDTO
  roles: RoleDTO[]
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
}

export interface QaLogDTO {
  id: number
  question: string
  answer: string
  contextFragments: ContextFragment[]
  modelVersion: string
  tokenUsage: number
  createdAt: string
}

export interface ContextFragment {
  fragmentId: number
  startTime: number
  endTime: number
  relevance: number
}

export interface FragmentDTO {
  id: number
  videoId: number
  startTime: number
  endTime: number
  content: string
  keywords: string[]
  orderNum: number
}

export interface NoteDTO {
  id: number
  videoId: number
  content: string
  timestamp: number
  isPublic: boolean
  createdAt: string
  updatedAt: string
}

export interface NoteRequest {
  content: string
  timestamp: number
  isPublic: boolean
}

export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}

export interface Result<T> {
  code: number
  message: string
  data: T
  timestamp: string
}
```

- [ ] **Step 2: 创建环境变量类型**

```typescript
// frontend/src/types/env.d.ts
/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

enum ImportMetaEnv {
  VITE_API_BASE_URL: string
  VITE_ENABLE_MOCK: string
}
```

## [ ] Task 3: 核心 Composables 实现
- **Priority**: P0
- **Depends On**: Task 2
- **Description**: 
  - 实现核心 Composables：useSSEChat、useTheme、useVideoPlayer、useNotes、usePagination、useNotification
  - 确保所有 Composables 功能完整，类型正确
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**: 
  - `programmatic` TR-3.1: 所有 Composables 实现完整，无 TypeScript 错误
  - `programmatic` TR-3.2: Composables 功能符合文档要求
- **Notes**: 核心 Composables 是业务逻辑的核心，确保实现质量

### Task 3 Steps:
- [ ] **Step 1: 实现 useSSEChat**

```typescript
// frontend/src/composables/useSSEChat.ts
import { ref, type Ref } from 'vue'
import type { QaLogDTO, ContextFragment } from '@/types/api'

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

interface UseSSEChatOptions {
  videoId: () => number
  apiBase?: string
  onError?: (error: Error) => void
}

export function useSSEChat(options: UseSSEChatOptions) {
  const messages: Ref<ChatMessage[]> = ref([])
  const isStreaming = ref(false)
  const abortController = ref<AbortController | null>(null)

  function genId(): string {
    return `msg_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`
  }

  async function sendMessage(question: string, currentTime?: number) {
    if (isStreaming.value) return

    const userMsg: ChatMessage = {
      id: genId(),
      role: 'user',
      content: question,
      createdAt: new Date(),
    }
    messages.value.push(userMsg)

    const aiMsg: ChatMessage = {
      id: genId(),
      role: 'assistant',
      content: '',
      isStreaming: true,
      sources: [],
      createdAt: new Date(),
    }
    messages.value.push(aiMsg)

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

        const events = buffer.split('\n\n')
        buffer = events.pop() ?? ''

        for (const event of events) {
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
                aiMsg.content += data.content ?? ''
                break
              case 'source':
                aiMsg.sources = data.fragments ?? []
                break
              case 'done':
                aiMsg.isStreaming = false
                aiMsg.tokenUsage = data.tokenUsage ?? 0
                break
              case 'error':
                aiMsg.isStreaming = false
                aiMsg.error = data.message ?? 'AI 回答出错'
                break
            }
          } catch {
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

  function abortStream() {
    abortController.value?.abort()
  }

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

- [ ] **Step 2: 实现 useTheme**

```typescript
// frontend/src/composables/useTheme.ts
import { ref, watchEffect } from 'vue'

const STORAGE_KEY = 'zhike-theme'

type ThemeValue = 'student-dark' | 'student-light' | 'teacher-light'

export function useTheme() {
  const currentTheme = ref<ThemeValue>('student-dark')

  function initTheme(roleCode: string) {
    const saved = localStorage.getItem(STORAGE_KEY) as ThemeValue | null
    if (saved && isValidTheme(saved)) {
      currentTheme.value = saved
      applyTheme()
      return
    }

    if (roleCode === 'STUDENT') {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      currentTheme.value = prefersDark ? 'student-dark' : 'student-light'
    } else {
      currentTheme.value = 'teacher-light'
    }
    applyTheme()
  }

  function isValidTheme(value: string): value is ThemeValue {
    return ['student-dark', 'student-light', 'teacher-light'].includes(value)
  }

  function applyTheme() {
    const root = document.documentElement
    root.setAttribute('data-theme', currentTheme.value)
    root.style.colorScheme = currentTheme.value === 'student-dark' ? 'dark' : 'light'
  }

  function toggleTheme() {
    if (currentTheme.value === 'student-dark') {
      currentTheme.value = 'student-light'
    } else if (currentTheme.value === 'student-light') {
      currentTheme.value = 'student-dark'
    } else {
      return
    }
    localStorage.setItem(STORAGE_KEY, currentTheme.value)
    applyTheme()
  }

  const isDark = ref(true)

  watchEffect(() => {
    isDark.value = currentTheme.value === 'student-dark'
  })

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

- [ ] **Step 3: 实现 useVideoPlayer**

```typescript
// frontend/src/composables/useVideoPlayer.ts
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

  let heartbeatTimer: ReturnType<typeof setInterval> | null = null
  const HEARTBEAT_INTERVAL = 30_000

  function togglePlay() {
    if (!videoRef.value) return
    if (videoRef.value.paused) {
      videoRef.value.play()
    } else {
      videoRef.value.pause()
    }
  }

  function seekTo(time: number) {
    if (!videoRef.value) return
    videoRef.value.currentTime = Math.max(0, Math.min(time, options.duration()))
    if (videoRef.value.paused) {
      videoRef.value.play()
    }
  }

  function seekToFragment(fragmentId: number) {
    const fragment = options.fragments().find(f => f.id === fragmentId)
    if (fragment) {
      seekTo(fragment.startTime)
    }
  }

  function getNearbyFragments(rangeSeconds = 30): FragmentDTO[] {
    return options.fragments().filter(
      f => Math.abs(f.startTime - currentTime.value) <= rangeSeconds
    )
  }

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
      }
    }, HEARTBEAT_INTERVAL)
  }

  function stopHeartbeat() {
    if (heartbeatTimer) {
      clearInterval(heartbeatTimer)
      heartbeatTimer = null
    }
  }

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
    }
  }

  function initPlayer() {
    const video = videoRef.value
    if (!video) return

    video.addEventListener('play', () => { isPlaying.value = true; startHeartbeat() })
    video.addEventListener('pause', () => { isPlaying.value = false; reportProgress() })
    video.addEventListener('timeupdate', () => { currentTime.value = video.currentTime })
    video.addEventListener('ended', () => { isPlaying.value = false; reportProgress(); stopHeartbeat() })

    if (lastWatchTime.value && lastWatchTime.value > 0) {
      video.currentTime = lastWatchTime.value
    }
  }

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

- [ ] **Step 4: 实现其他 Composables**

```typescript
// frontend/src/composables/useNotes.ts
import { ref, type Ref } from 'vue'
import type { NoteDTO, NoteRequest } from '@/types/api'

interface UseNotesOptions {
  videoId: () => number
}

export function useNotes(options: UseNotesOptions) {
  const notes: Ref<NoteDTO[]> = ref([])
  const isLoading = ref(false)
  const error = ref<string | null>(null)

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
        notes.value.unshift(note)
        return note
      }
      error.value = json.message
      return null
    } catch (e: any) {
      error.value = e.message
      return null
    }
  }

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

```typescript
// frontend/src/composables/usePagination.ts
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

  function changePage(page: number) {
    loadData(page)
  }

  function changePageSize(size: number) {
    pagination.pageSize = size
    pagination.page = 1
    loadData(1)
  }

  function refresh() {
    loadData()
  }

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

```typescript
// frontend/src/composables/useNotification.ts
import { ref, computed } from 'vue'

export interface Notification {
  id: number
  type: 'system' | 'course' | 'interaction'
  title: string
  description: string
  isRead: boolean
  link?: string
  createdAt: string
}

export function useNotification() {
  const notifications = ref<Notification[]>([])
  const activeTab = ref<'all' | 'system' | 'course' | 'interaction'>('all')
  const isDrawerOpen = ref(false)

  const unreadCount = computed(() =>
    notifications.value.filter(n => !n.isRead).length
  )

  const filteredNotifications = computed(() => {
    if (activeTab.value === 'all') return notifications.value
    return notifications.value.filter(n => n.type === activeTab.value)
  })

  function openDrawer() {
    isDrawerOpen.value = true
  }

  function closeDrawer() {
    isDrawerOpen.value = false
  }

  function markAsRead(id: number) {
    const notification = notifications.value.find(n => n.id === id)
    if (notification) {
      notification.isRead = true
    }
  }

  function markAllAsRead() {
    notifications.value.forEach(n => { n.isRead = true })
  }

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

## [ ] Task 4: 状态管理实现
- **Priority**: P0
- **Depends On**: Task 3
- **Description**: 
  - 实现 Pinia 状态管理，包括用户认证状态、主题状态和通知状态
  - 确保状态管理功能完整，类型正确
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**: 
  - `programmatic` TR-4.1: Pinia store 实现完整，无 TypeScript 错误
  - `programmatic` TR-4.2: 状态管理功能符合文档要求
- **Notes**: 状态管理是整个应用的核心，确保实现质量

### Task 4 Steps:
- [ ] **Step 1: 实现用户认证 Store**

```typescript
// frontend/src/stores/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfoDTO, RoleDTO, LoginResponse } from '@/types/api'

const TOKEN_KEY = 'zhike-token'
const USER_KEY = 'zhike-user'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) ?? '')
  const userInfo = ref<UserInfoDTO | null>(
    JSON.parse(localStorage.getItem(USER_KEY) ?? 'null')
  )
  const roles = ref<RoleDTO[]>([])

  const isLoggedIn = computed(() => !!token.value)
  const roleCode = computed(() => userInfo.value?.roleCode ?? '')
  const isStudent = computed(() => roleCode.value === 'STUDENT')
  const isTeacher = computed(() => roleCode.value === 'TEACHER')
  const isAdmin = computed(() => roleCode.value === 'ADMIN')

  const homeRoute = computed(() => {
    switch (roleCode.value) {
      case 'STUDENT': return '/student/home'
      case 'TEACHER': return '/teacher/home'
      case 'ADMIN':   return '/admin/users'
      default:        return '/login'
    }
  })

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

- [ ] **Step 2: 实现主题和通知 Store**

```typescript
// frontend/src/stores/theme.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useTheme as useThemeComposable } from '@/composables/useTheme'

export const useThemeStore = defineStore('theme', () => {
  const { currentTheme, isDark, initTheme, toggleTheme } = useThemeComposable()

  return {
    currentTheme,
    isDark,
    initTheme,
    toggleTheme,
  }
})
```

```typescript
// frontend/src/stores/notification.ts
import { defineStore } from 'pinia'
import { useNotification as useNotificationComposable } from '@/composables/useNotification'

export const useNotificationStore = defineStore('notification', () => {
  const {
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
  } = useNotificationComposable()

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
})
```

## [ ] Task 5: 路由系统实现
- **Priority**: P0
- **Depends On**: Task 4
- **Description**: 
  - 实现 Vue Router 路由系统，包括路由表定义和导航守卫
  - 确保路由守卫正常工作，支持角色权限控制
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**: 
  - `programmatic` TR-5.1: 路由系统实现完整，无 TypeScript 错误
  - `programmatic` TR-5.2: 路由守卫正常工作，权限控制有效
- **Notes**: 路由系统是应用的骨架，确保实现正确

### Task 5 Steps:
- [ ] **Step 1: 定义路由表**

```typescript
// frontend/src/router/routes.ts
import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false, title: '登录' },
  },
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
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      { path: 'users', name: 'UserManage', component: () => import('@/views/admin/UserManage.vue'), meta: { title: '用户管理' } },
    ],
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/settings/index.vue'),
    meta: { requiresAuth: true, title: '个人设置' },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' },
  },
]
```

- [ ] **Step 2: 实现路由守卫**

```typescript
// frontend/src/router/guards.ts
import type { Router } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useTheme } from '@/composables/useTheme'

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to, from) => {
    const userStore = useUserStore()

    if (to.meta.requiresAuth === false) {
      if (to.name === 'Login' && userStore.isLoggedIn) {
        return userStore.homeRoute
      }
      return true
    }

    if (!userStore.isLoggedIn) {
      return { name: 'Login', query: { redirect: to.fullPath } }
    }

    const requiredRole = to.meta.role as string | undefined
    if (requiredRole && userStore.roleCode !== requiredRole) {
      return userStore.homeRoute
    }

    if (userStore.roleCode) {
      const { initTheme } = useTheme()
      initTheme(userStore.roleCode)
    }

    if (to.meta.title) {
      document.title = `${to.meta.title} - ZhiKe`
    }

    return true
  })
}
```

- [ ] **Step 3: 初始化路由**

```typescript
// frontend/src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import { routes } from './routes'
import { setupRouterGuards } from './guards'

const router = createRouter({
  history: createWebHistory(),
  routes,
})

setupRouterGuards(router)

export default router
```

## [ ] Task 6: 前后端对接规范实现
- **Priority**: P1
- **Depends On**: Task 5
- **Description**: 
  - 实现 Axios 实例和拦截器，API 请求层
  - 确保 API 请求正常，错误处理完善
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**: 
  - `programmatic` TR-6.1: Axios 实例和拦截器实现完整
  - `programmatic` TR-6.2: API 请求层实现完整，无 TypeScript 错误
- **Notes**: 前后端对接规范是应用与后端交互的基础，确保实现正确

### Task 6 Steps:
- [ ] **Step 1: 实现 Axios 实例和拦截器**

```typescript
// frontend/src/utils/request.ts
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api',
  timeout: 15_000,
  headers: { 'Content-Type': 'application/json' },
})

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

request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data

    if (code === 200) {
      return data
    }

    ElMessage.error(message ?? '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    if (error.response) {
      const { status } = error.response

      switch (status) {
        case 401:
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

- [ ] **Step 2: 实现 API 请求层**

```typescript
// frontend/src/api/auth.ts
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
// frontend/src/api/qa.ts
import request from '@/utils/request'
import type { QaLogDTO, PageResult } from '@/types/api'

export const qaApi = {
  chatStreamUrl: '/qa/chat-stream',

  getHistory: (params: { page: number; pageSize: number }) =>
    request.get<any, PageResult<QaLogDTO>>('/qa/history', { params }),
}
```

```typescript
// frontend/src/api/video.ts
import request from '@/utils/request'
import type { FragmentDTO, PageResult } from '@/types/api'

export const videoApi = {
  getUploadSignature: (data: { filename: string; fileSize: number }) =>
    request.post<any, { signature: string; uploadId: string }>('/video/upload-signature', data),

  uploadComplete: (data: { uploadId: string; videoId: number }) =>
    request.post<any, { success: boolean }>('/video/upload-complete', data),

  getVideoStatus: (videoId: number) =>
    request.get<any, { status: string; progress: number }>(`/video/${videoId}/status`),

  getSignUrl: (videoId: number) =>
    request.get<any, { signUrl: string }>(`/video/${videoId}/sign-url`),

  getTeacherVideos: (params: { page: number; pageSize: number }) =>
    request.get<any, PageResult<any>>('/teacher/videos', { params }),

  getFragments: (videoId: number) =>
    request.get<any, FragmentDTO[]>(`/teacher/fragments?videoId=${videoId}`),
}
```

```typescript
// frontend/src/api/student.ts
import request from '@/utils/request'
import type { NoteDTO, NoteRequest, PageResult } from '@/types/api'

export const studentApi = {
  getCourses: (params: { page: number; pageSize: number }) =>
    request.get<any, PageResult<any>>('/student/courses', { params }),

  getCourseDetail: (courseId: number) =>
    request.get<any, any>(`/student/courses/${courseId}`),

  getPlayInfo: (videoId: number) =>
    request.get<any, { signUrl: string; duration: number; lastWatchTime: number }>(`/student/videos/${videoId}/play-info`),

  getNotes: (videoId: number) =>
    request.get<any, NoteDTO[]>(`/student/notes?videoId=${videoId}`),

  createNote: (data: NoteRequest & { videoId: number }) =>
    request.post<any, NoteDTO>('/student/notes', data),

  updateNote: (id: number, data: Partial<NoteRequest>) =>
    request.put<any, NoteDTO>(`/student/notes/${id}`, data),

  deleteNote: (id: number) =>
    request.delete<any, { success: boolean }>(`/student/notes/${id}`),

  reportHeartbeat: (data: { videoId: number; currentTime: number; status: string }) =>
    request.post<any, { success: boolean }>('/student/heartbeat', data),

  reportProgress: (data: { videoId: number; currentTime: number; duration: number }) =>
    request.post<any, { success: boolean }>('/student/progress', data),
}
```

```typescript
// frontend/src/api/analytics.ts
import request from '@/utils/request'

export const analyticsApi = {
  getHeatmap: (courseId: number) =>
    request.get<any, any>(`/analytics/heatmap?courseId=${courseId}`),

  getClassOverview: (courseId: number) =>
    request.get<any, any>(`/analytics/class-overview?courseId=${courseId}`),

  getStudentProfile: (studentId: number) =>
    request.get<any, any>(`/analytics/student-profile?studentId=${studentId}`),

  getWordcloud: (courseId: number) =>
    request.get<any, any>(`/analytics/wordcloud?courseId=${courseId}`),
}
```

```typescript
// frontend/src/api/admin.ts
import request from '@/utils/request'
import type { PageResult } from '@/types/api'

export const adminApi = {
  getUsers: (params: { page: number; pageSize: number; roleCode?: string }) =>
    request.get<any, PageResult<any>>('/admin/users', { params }),

  updateUser: (id: number, data: { roleCode: string; status: number }) =>
    request.put<any, { success: boolean }>(`/admin/users/${id}`, data),

  deleteUser: (id: number) =>
    request.delete<any, { success: boolean }>(`/admin/users/${id}`),
}
```

## [ ] Task 7: Mock 数据系统实现
- **Priority**: P1
- **Depends On**: Task 6
- **Description**: 
  - 实现 MSW Mock 数据系统，包括 Service Worker 配置、Handler 和数据源
  - 确保 Mock 数据正常返回，支持 SSE 流式响应
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**: 
  - `programmatic` TR-7.1: MSW 配置和 Handler 实现完整
  - `programmatic` TR-7.2: Mock 数据正常返回，SSE 流式响应支持
- **Notes**: Mock 数据系统是前后端并行开发的关键，确保实现完整

### Task 7 Steps:
- [ ] **Step 1: 配置 MSW Service Worker**

```typescript
// frontend/src/mocks/browser.ts
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

- [ ] **Step 2: 实现 Mock Handlers**

```typescript
// frontend/src/mocks/handlers/auth.handler.ts
import { http, HttpResponse, delay } from 'msw'
import { mockUsers } from '../data/auth.data'

export const authHandlers = [
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

  http.get('/api/auth/info', async ({ request }) => {
    await delay(200)
    const authHeader = request.headers.get('Authorization')
    if (!authHeader) {
      return HttpResponse.json(
        { code: 401, message: '未认证', data: null, timestamp: new Date().toISOString() },
        { status: 401 }
      )
    }
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

```typescript
// frontend/src/mocks/handlers/qa.handler.ts
import { http, HttpResponse, delay } from 'msw'
import { mockQaHistory } from '../data/qa.data'

function createSSEStream(answer: string, sources: Array<{
  fragmentId: number; startTime: number; endTime: number; relevance: number
}>) {
  const encoder = new TextEncoder()

  return new ReadableStream({
    async start(controller) {
      for (let i = 0; i < answer.length; i += Math.floor(2 + Math.random() * 4)) {
        const chunk = answer.slice(i, i + Math.floor(2 + Math.random() * 4))
        const event = `data:${JSON.stringify({ type: 'chunk', content: chunk })}\n\n`
        controller.enqueue(encoder.encode(event))
        await new Promise(r => setTimeout(r, 25 + Math.random() * 45))
      }

      await delay(100)
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'source', fragments: sources })}\n\n`
      ))

      await delay(50)
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'done', tokenUsage: Math.floor(200 + Math.random() * 300) })}\n\n`
      ))

      controller.close()
    },
  })
}

export const qaHandlers = [
  http.post('/api/qa/chat-stream', async ({ request }) => {
    await delay(300)

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

- [ ] **Step 3: 实现 Mock 数据源**

```typescript
// frontend/src/mocks/data/auth.data.ts
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

```typescript
// frontend/src/mocks/data/qa.data.ts
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

- [ ] **Step 4: 配置环境变量**

```bash
# frontend/.env.development
VITE_ENABLE_MOCK=true
VITE_API_BASE_URL=http://localhost:8080

# frontend/.env.production
VITE_ENABLE_MOCK=false
VITE_API_BASE_URL=https://api.zhike.edu.cn
```

- [ ] **Step 5: 集成 MSW 到主应用**

```typescript
// frontend/src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

async function bootstrap() {
  const app = createApp(App)
  app.use(createPinia())
  app.use(router)

  if (import.meta.env.DEV && import.meta.env.VITE_ENABLE_MOCK === 'true') {
    const { worker } = await import('./mocks/browser')
    await worker.start({ onUnhandledRequest: 'bypass' })
    console.log('[MSW] Mock Service Worker 已启动')
  }

  app.mount('#app')
}

bootstrap()
```

## [ ] Task 8: 类型安全和测试
- **Priority**: P2
- **Depends On**: Task 7
- **Description**: 
  - 运行 TypeScript 类型检查和单元测试
  - 确保代码类型安全，测试覆盖率符合要求
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**: 
  - `programmatic` TR-8.1: TypeScript 类型检查通过，无错误
  - `programmatic` TR-8.2: 单元测试通过，覆盖率符合要求
- **Notes**: 类型安全和测试是保证代码质量的关键，确保严格执行

### Task 8 Steps:
- [ ] **Step 1: 运行 TypeScript 类型检查**

```bash
# 运行 TypeScript 类型检查
cd frontend && npx tsc --noEmit
```

- [ ] **Step 2: 编写单元测试**

```typescript
// frontend/tests/composables/useSSEChat.test.ts
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

    expect(messages.value).toHaveLength(2)
    expect(messages.value[0].role).toBe('user')
    expect(messages.value[1].role).toBe('assistant')
    expect(messages.value[1].content).toBe('你好')
  })
})
```

- [ ] **Step 3: 运行单元测试**

```bash
# 运行单元测试
cd frontend && npm test
```

- [ ] **Step 4: 检查测试覆盖率**

```bash
# 检查测试覆盖率
cd frontend && npm test -- --coverage
```