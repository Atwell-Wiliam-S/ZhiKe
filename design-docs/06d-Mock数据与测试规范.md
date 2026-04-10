# 4.7-4.8 Mock 数据开发模式与测试规范

> 来源：`06-组件逻辑与Mock开发.md` 拆分文件 D
>
> 本文件包含 MSW Mock 数据系统架构、Handler 完整示例、数据源、覆盖矩阵、类型安全策略和单元测试规范。
>
> **关联文件**：
> - `06a-项目结构与类型定义.md` — 项目结构 / DTO 类型定义（Mock 数据必须与之一致）
> - `06b-核心Composables.md` — useSSEChat / useTheme / useVideoPlayer
> - `06c-通用Composables与状态管理.md` — useNotes / usePagination / useNotification / Store / 路由 / Axios

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

#### SSE 问答 Handler（核心）

```typescript
// src/mocks/handlers/qa.handler.ts
import { http, HttpResponse, delay } from 'msw'
import { mockQaHistory } from '../data/qa.data'

function createSSEStream(answer: string, sources: Array<{
  fragmentId: number; startTime: number; endTime: number; relevance: number
}>) {
  const encoder = new TextEncoder()

  return new ReadableStream({
    async start(controller) {
      // 阶段 1：逐字发送 chunk 事件
      for (let i = 0; i < answer.length; i += Math.floor(2 + Math.random() * 4)) {
        const chunk = answer.slice(i, i + Math.floor(2 + Math.random() * 4))
        const event = `data:${JSON.stringify({ type: 'chunk', content: chunk })}\n\n`
        controller.enqueue(encoder.encode(event))
        await new Promise(r => setTimeout(r, 25 + Math.random() * 45))
      }

      // 阶段 2：发送 source 事件
      await delay(100)
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'source', fragments: sources })}\n\n`
      ))

      // 阶段 3：发送 done 事件
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
      data: { records, total: mockQaHistory.length, page, pageSize, totalPages: Math.ceil(mockQaHistory.length / pageSize) },
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
    userInfo: { userId: 1, username: 'zhangsan', email: 'zhangsan@edu.cn', avatarUrl: null, roleCode: 'STUDENT', roleName: '学生' },
    roles: [mockRoles[0]],
  },
  teacher: {
    token: 'mock-jwt-teacher-token-xxx',
    userInfo: { userId: 2, username: 'lisi', email: 'lisi@edu.cn', avatarUrl: '/avatars/lisi.png', roleCode: 'TEACHER', roleName: '教师' },
    roles: [mockRoles[1]],
  },
  admin: {
    token: 'mock-jwt-admin-token-xxx',
    userInfo: { userId: 3, username: 'admin', email: 'admin@edu.cn', avatarUrl: null, roleCode: 'ADMIN', roleName: '管理员' },
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
  question: ['导数的几何意义是什么？', '如何理解极限的概念？', '链式法则怎么应用？', '积分和导数有什么关系？', '泰勒展开的用途是什么？'][i % 5],
  answer: ['导数的几何意义是函数曲线在某一点处的切线斜率...', '极限描述的是当自变量趋近于某个值时...', '链式法则用于求复合函数的导数...', '积分是导数的逆运算...', '泰勒展开用多项式近似表示函数...'][i % 5],
  contextFragments: [{ fragmentId: (i % 3) + 1, startTime: 120 + i * 10, endTime: 185 + i * 10, relevance: 0.9 - (i % 3) * 0.1 }],
  modelVersion: 'GLM-4',
  tokenUsage: 200 + Math.floor(Math.random() * 300),
  createdAt: new Date(Date.now() - i * 3600_000).toISOString(),
}))
```

### 4.7.5 Mock 文件覆盖矩阵

| Mock 文件 | 覆盖接口 | 核心数据 | 数据量 |
| --- | --- | --- | --- |
| `auth.handler.ts` | `/api/auth/login`, `/api/auth/info`, `/api/auth/refresh` | 多角色用户 | 3 个用户 |
| `video.handler.ts` | `/api/video/upload-signature`, `/api/video/upload-complete`, `/api/video/:id/status`, `/api/video/:id/sign-url`, `/api/teacher/videos`, `/api/teacher/fragments` | 视频列表、片段、解析状态 | 8 视频, 24 片段 |
| `qa.handler.ts` | `/api/qa/chat-stream` (SSE), `/api/qa/history` | SSE 流式模拟 + 历史 | 25 条 |
| `analytics.handler.ts` | `/api/analytics/heatmap`, `/api/analytics/class-overview`, `/api/analytics/student-profile`, `/api/analytics/wordcloud` | 热力图、概览、画像、词云 | 按课程生成 |
| `student.handler.ts` | `/api/student/courses`, `/api/student/courses/:id`, `/api/student/videos/:id/play-info`, `/api/student/notes`, `/api/student/heartbeat`, `/api/student/progress` | 课程、播放、笔记、心跳 | 5 课程, 12 笔记 |
| `admin.handler.ts` | `/api/admin/users`, `/api/admin/users/:id` (PUT/DELETE) | 用户列表、角色管理 | 20 用户 |

### 4.7.6 环境变量与开关控制

```bash
# .env.development
VITE_ENABLE_MOCK=true
VITE_API_BASE_URL=http://localhost:8080

# .env.production
VITE_ENABLE_MOCK=false
VITE_API_BASE_URL=https://api.zhike.edu.cn
```

### 4.7.7 Mock 数据类型安全策略

| 策略 | 实现方式 |
| --- | --- |
| **类型导入** | Mock 数据文件从 `@/types/api` 导入 DTO 类型 |
| **TypeScript 严格模式** | `tsconfig.json` 启用 `strict: true` + `noUncheckedIndexedAccess` |
| **as 类型断言** | Handler 返回数据使用 `as Result<T>` 断言 |
| **MSW 类型推断** | `http.post<PostBody, Result<ResponseType>>()` 泛型参数 |
| **CI 检查** | `tsc --noEmit` 确保类型无错误 |

```typescript
import type { Result, PageResult, QaLogDTO } from '@/types/api'

// ✅ 正确：使用泛型约束
http.get('/api/qa/history', async () => {
  const data: Result<PageResult<QaLogDTO>> = {
    code: 200, message: '操作成功',
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

    const { messages, sendMessage } = useSSEChat({ videoId: () => 1 })
    await sendMessage('测试问题')

    expect(messages.value).toHaveLength(2)
    expect(messages.value[0].role).toBe('user')
    expect(messages.value[1].role).toBe('assistant')
    expect(messages.value[1].content).toContain('你好')
  })

  it('应在 done 事件后标记 isStreaming 为 false', async () => { /* ... */ })
  it('应在网络错误时设置 error 属性', async () => { /* ... */ })
  it('abortStream 应中断请求', async () => { /* ... */ })
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
npm run test                    # 运行所有测试
npx vitest run tests/composables/useSSEChat.test.ts  # 运行特定文件
npm run test:watch              # 监听模式
npm run test:coverage           # 覆盖率报告
```

```json
{ "scripts": {
    "test": "vitest run",
    "test:watch": "vitest",
    "test:coverage": "vitest run --coverage"
} }
```
