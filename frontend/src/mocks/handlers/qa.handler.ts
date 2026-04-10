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
      data: { records, total: mockQaHistory.length, page, pageSize, totalPages: Math.ceil(mockQaHistory.length / pageSize) },
      timestamp: new Date().toISOString(),
    })
  }),
]
