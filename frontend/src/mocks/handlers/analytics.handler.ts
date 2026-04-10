import { http, HttpResponse, delay } from 'msw'
import { mockHeatmap, mockClassOverview, mockStudentProfiles, mockWordCloud } from '../data/analytics.data'

export const analyticsHandlers = [
  http.get('/api/analytics/heatmap', async ({ request }) => {
    await delay(250)
    const url = new URL(request.url)
    const videoId = parseInt(url.searchParams.get('videoId') ?? '1')
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockHeatmap(videoId),
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/analytics/class-overview', async () => {
    await delay(300)
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockClassOverview,
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/analytics/student-profile', async ({ request }) => {
    await delay(200)
    const url = new URL(request.url)
    const userId = parseInt(url.searchParams.get('userId') ?? '1')
    const profile = mockStudentProfiles.find(p => p.userId === userId) || mockStudentProfiles[0]
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: profile,
      timestamp: new Date().toISOString(),
    })
  }),

  http.get('/api/analytics/wordcloud', async ({ request }) => {
    await delay(200)
    const url = new URL(request.url)
    const courseId = parseInt(url.searchParams.get('courseId') ?? '1')
    return HttpResponse.json({
      code: 200,
      message: '操作成功',
      data: mockWordCloud(courseId),
      timestamp: new Date().toISOString(),
    })
  }),
]
