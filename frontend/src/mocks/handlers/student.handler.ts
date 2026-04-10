import { http, HttpResponse, delay } from 'msw'
import { mockCourses, mockCourseDetails, mockPlayInfo, mockNotes } from '../data/student.data'

export const studentHandlers = [
  http.get('/api/student/courses', async () => {
    await delay(300)
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockCourses,
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/student/courses/:id', async ({ params }) => {
    await delay(250)
    const courseId = parseInt(params.id as string)
    const course = mockCourseDetails.find(c => c.id === courseId)
    if (!course) {
      return HttpResponse.json({ code: 404, message: '课程不存在', data: null, timestamp: new Date().toISOString() }, { status: 404 })
    }
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: course,
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/student/videos/:id/play-info', async ({ params }) => {
    await delay(200)
    const videoId = parseInt(params.id as string)
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockPlayInfo(videoId),
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/student/notes', async ({ request }) => {
    await delay(200)
    const url = new URL(request.url)
    const videoId = url.searchParams.get('videoId')
    const notes = videoId ? mockNotes.filter(n => n.videoId === parseInt(videoId)) : mockNotes
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: notes,
      timestamp: new Date().toISOString(),
    })
  }),

  http.post('/api/student/notes', async ({ request }) => {
    await delay(300)
    const body = await request.json()
    const bodyData = body as Record<string, any>
    const newNote = {
      id: mockNotes.length + 1,
      ...bodyData,
      isPublic: bodyData.isPublic ?? false,
      createdAt: new Date().toISOString(),
    }
    return HttpResponse.json({
      code: 200,
      message: '笔记创建成功',
      data: newNote,
      timestamp: new Date().toISOString(),
    })
  }),

  http.post('/api/student/heartbeat', async () => {
    await delay(100)
    return HttpResponse.json({
      code: 200,
      message: '心跳上报成功',
      data: null,
      timestamp: new Date().toISOString(),
    })
  }),

  http.post('/api/student/progress', async () => {
    await delay(150)
    return HttpResponse.json({
      code: 200,
      message: '进度更新成功',
      data: null,
      timestamp: new Date().toISOString(),
    })
  }),
]
