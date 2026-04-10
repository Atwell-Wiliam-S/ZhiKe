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
