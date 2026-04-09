import { rest } from 'msw'
import type { QaLogDTO, ContextFragment } from '@/types/api'

// Mock 数据
const mockQaLogs: QaLogDTO[] = [
  {
    id: 1,
    question: 'Vue 3 的 Composition API 是什么？',
    answer: 'Vue 3 的 Composition API 是一种新的 API 风格，允许我们使用函数而不是选项来组织组件逻辑。它提供了更好的代码组织、类型推断和逻辑复用能力。',
    contextFragments: [
      { fragmentId: 1, startTime: 0, endTime: 60, relevance: 0.95 },
      { fragmentId: 2, startTime: 60, endTime: 120, relevance: 0.82 }
    ],
    modelVersion: 'qwen-plus',
    tokenUsage: 256,
    createdAt: '2026-04-01T10:00:00'
  },
  {
    id: 2,
    question: '如何安装 Vue 3？',
    answer: '你可以使用 npm 或 yarn 来安装 Vue 3。例如，使用 npm install vue@next 或 yarn add vue@next 命令。',
    contextFragments: [
      { fragmentId: 2, startTime: 60, endTime: 120, relevance: 0.92 }
    ],
    modelVersion: 'qwen-plus',
    tokenUsage: 180,
    createdAt: '2026-04-01T10:30:00'
  }
]

// SSE 模拟函数
function createSSEStream(answer: string, sources: ContextFragment[]) {
  const encoder = new TextEncoder();
  return new ReadableStream({
    async start(controller) {
      // 1. 逐字发送 chunk 事件
      for (let i = 0; i < answer.length; i += 3) {
        const chunk = answer.slice(i, i + 3);
        const event = `data:${JSON.stringify({ type: 'chunk', content: chunk })}\n\n`;
        controller.enqueue(encoder.encode(event));
        await new Promise(r => setTimeout(r, 30 + Math.random() * 50));
      }
      // 2. 发送 source 事件
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'source', fragments: sources })}\n\n`
      ));
      // 3. 发送 done 事件
      controller.enqueue(encoder.encode(
        `data:${JSON.stringify({ type: 'done', tokenUsage: 256 })}\n\n`
      ));
      controller.close();
    }
  });
}

// Handlers
export const qaHandlers = [
  // 流式问答
  rest.post('/api/qa/chat-stream', (req, res, ctx) => {
    const { question, videoId } = req.body as { question: string; videoId: number };
    
    // 模拟回答
    const mockAnswer = `根据视频内容，关于 "${question}" 的问题，我的回答是：这是一个关于 Vue 3 的问题，Vue 3 是 Vue.js 的最新版本，带来了许多新特性和改进，包括 Composition API、更好的 TypeScript 支持、更高效的渲染等。`;
    const mockSources = [
      { fragmentId: 1, startTime: 0, endTime: 60, relevance: 0.95 },
      { fragmentId: 2, startTime: 60, endTime: 120, relevance: 0.82 }
    ];
    
    // 创建 SSE 流
    const stream = createSSEStream(mockAnswer, mockSources);
    
    return res(
      ctx.status(200),
      ctx.set('Content-Type', 'text/event-stream'),
      ctx.body(stream)
    );
  }),
  
  // 问答历史
  rest.get('/api/qa/history', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: {
          records: mockQaLogs,
          total: mockQaLogs.length,
          size: 10,
          current: 1,
          pages: 1
        },
        timestamp: Date.now()
      })
    );
  }),
]
