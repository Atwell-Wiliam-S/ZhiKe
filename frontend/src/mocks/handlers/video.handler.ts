import { http, HttpResponse, delay } from 'msw'
import { mockVideos, mockFragments, mockParseStatus, mockUploadSignature, mockSignUrl } from '../data/video.data'

export const videoHandlers = [
  http.post('/api/video/upload-signature', async () => {
    await delay(400)
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockUploadSignature,
      timestamp: new Date().toISOString(),
    })
  }),

  http.post('/api/video/upload-complete', async () => {
    await delay(300)
    return HttpResponse.json({
      code: 200,
      message: '视频上传完成，正在解析...',
      data: { videoId: 99 },
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/video/:id/status', async ({ params }) => {
    await delay(200)
    const videoId = parseInt(params.id as string)
    const status = mockParseStatus.find(s => s.videoId === videoId)
    if (!status) {
      return HttpResponse.json({ code: 404, message: '视频不存在', data: null, timestamp: new Date().toISOString() }, { status: 404 })
    }
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: status,
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/video/:id/sign-url', async ({ params }) => {
    await delay(200)
    const videoId = parseInt(params.id as string)
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockSignUrl(videoId),
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/teacher/videos', async () => {
    await delay(300)
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockVideos,
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/teacher/fragments', async ({ request }) => {
    await delay(200)
    const url = new URL(request.url)
    const videoId = url.searchParams.get('videoId')
    const fragments = videoId ? mockFragments.filter(f => f.videoId === parseInt(videoId)) : mockFragments
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: fragments,
      timestamp: new Date().toISOString(),
    })
  }),
]
