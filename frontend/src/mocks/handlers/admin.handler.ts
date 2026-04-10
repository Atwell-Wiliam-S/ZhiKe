import { http, HttpResponse, delay } from 'msw'
import { mockAdminUsers } from '../data/admin.data'

export const adminHandlers = [
  http.get('/api/admin/users', async ({ request }) => {
    await delay(300)
    const url = new URL(request.url)
    const page = parseInt(url.searchParams.get('page') ?? '1')
    const pageSize = parseInt(url.searchParams.get('pageSize') ?? '10')

    const start = (page - 1) * pageSize
    const records = mockAdminUsers.slice(start, start + pageSize)

    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: { records, total: mockAdminUsers.length, page, pageSize, totalPages: Math.ceil(mockAdminUsers.length / pageSize) },
      timestamp: new Date().toISOString(),
    })
  }),

  http.put('/api/admin/users/:id', async ({ params, request }) => {
    await delay(300)
    const userId = parseInt(params.id as string)
    const body = await request.json()
    const userIndex = mockAdminUsers.findIndex(u => u.userId === userId)
    if (userIndex === -1) {
      return HttpResponse.json({ code: 404, message: '用户不存在', data: null, timestamp: new Date().toISOString() }, { status: 404 })
    }
    mockAdminUsers[userIndex] = { ...mockAdminUsers[userIndex], ...(body as Record<string, any>) }
    return HttpResponse.json({
      code: 200,
      message: '用户更新成功',
      data: mockAdminUsers[userIndex],
      timestamp: new Date().toISOString(),
    })
  }),

  http.delete('/api/admin/users/:id', async ({ params }) => {
    await delay(300)
    const userId = parseInt(params.id as string)
    const userIndex = mockAdminUsers.findIndex(u => u.userId === userId)
    if (userIndex === -1) {
      return HttpResponse.json({ code: 404, message: '用户不存在', data: null, timestamp: new Date().toISOString() }, { status: 404 })
    }
    mockAdminUsers.splice(userIndex, 1)
    return HttpResponse.json({
      code: 200,
      message: '用户删除成功',
      data: null,
      timestamp: new Date().toISOString(),
    })
  }),
]
