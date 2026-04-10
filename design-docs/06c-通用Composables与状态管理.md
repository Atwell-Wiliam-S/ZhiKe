# 4.4 核心 Composables（下）与状态管理

> 来源：`06-组件逻辑与Mock开发.md` 拆分文件 C
>
> 本文件包含 3 个通用 Composable、Pinia Store、路由配置与守卫、Axios 实例配置。
>
> **关联文件**：
> - `06a-项目结构与类型定义.md` — 项目结构 / DTO 类型定义 / 字段映射
> - `06b-核心Composables.md` — useSSEChat / useTheme / useVideoPlayer
> - `06d-Mock数据与测试规范.md` — MSW Mock / 单元测试

---

## 4.4.4 useNotes — 笔记管理

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

## 4.4.5 usePagination — 通用分页

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

## 4.4.6 useNotification — 通知中心

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
  link?: string
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

  function openDrawer() { isDrawerOpen.value = true }
  function closeDrawer() { isDrawerOpen.value = false }

  function markAsRead(id: number) {
    const notification = notifications.value.find(n => n.id === id)
    if (notification) notification.isRead = true
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
    if (json.code !== 200) throw new Error(json.message ?? '登录失败')
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

```typescript
// src/router/guards.ts
import type { Router } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useTheme } from '@/composables/useTheme'

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to, from) => {
    const userStore = useUserStore()

    // 1. 公开页面直接放行
    if (to.meta.requiresAuth === false) {
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

## 4.6.1 Axios 实例与拦截器

```typescript
// src/utils/request.ts
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

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
    if (code === 200) return data
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
        case 403: ElMessage.error('无权限访问'); break
        case 404: ElMessage.error('请求的资源不存在'); break
        case 500: ElMessage.error('服务器内部错误'); break
        default: ElMessage.error(error.response.data?.message ?? '请求失败')
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
