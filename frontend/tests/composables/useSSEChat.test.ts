import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useSSEChat } from '@/composables/useSSEChat'

describe('useSSEChat', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    // 模拟 localStorage
    vi.stubGlobal('localStorage', {
      getItem: vi.fn(() => 'mock-token'),
      setItem: vi.fn(),
      removeItem: vi.fn()
    })
  })

  it('应正确解析 SSE chunk 事件并追加内容', async () => {
    const mockFetch = vi.fn(() => ({
      ok: true,
      body: {
        getReader: () => ({
          read: vi.fn()
            .mockResolvedValueOnce({
              done: false,
              value: new TextEncoder().encode(
                `data:${JSON.stringify({ type: 'chunk', content: '你好' })}\n\n`
              ),
            })
            .mockResolvedValueOnce({ done: true, value: undefined }),
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

  it('应正确处理 SSE source 事件', async () => {
    const mockFetch = vi.fn(() => ({
      ok: true,
      body: {
        getReader: () => ({
          read: vi.fn()
            .mockResolvedValueOnce({
              done: false,
              value: new TextEncoder().encode(
                `data:${JSON.stringify({ 
                  type: 'source', 
                  fragments: [{ fragmentId: 1, startTime: 10, endTime: 20, relevance: 0.9 }] 
                })}\n\n`
              ),
            })
            .mockResolvedValueOnce({ done: true, value: undefined }),
        }),
      },
    }))
    vi.stubGlobal('fetch', mockFetch)

    const { messages, sendMessage } = useSSEChat({
      videoId: () => 1,
    })

    await sendMessage('测试问题')

    expect(messages.value[1].sources).toHaveLength(1)
    expect(messages.value[1].sources?.[0].fragmentId).toBe(1)
  })

  it('应正确处理 SSE done 事件', async () => {
    const mockFetch = vi.fn(() => ({
      ok: true,
      body: {
        getReader: () => ({
          read: vi.fn()
            .mockResolvedValueOnce({
              done: false,
              value: new TextEncoder().encode(
                `data:${JSON.stringify({ type: 'done', tokenUsage: 100 })}\n\n`
              ),
            })
            .mockResolvedValueOnce({ done: true, value: undefined }),
        }),
      },
    }))
    vi.stubGlobal('fetch', mockFetch)

    const { messages, sendMessage } = useSSEChat({
      videoId: () => 1,
    })

    await sendMessage('测试问题')

    expect(messages.value[1].isStreaming).toBe(false)
    expect(messages.value[1].tokenUsage).toBe(100)
  })

  it('应正确处理网络错误', async () => {
    const mockFetch = vi.fn(() => Promise.reject(new Error('网络错误')))
    vi.stubGlobal('fetch', mockFetch)

    const { messages, sendMessage } = useSSEChat({
      videoId: () => 1,
    })

    await sendMessage('测试问题')

    expect(messages.value[1].isStreaming).toBe(false)
    expect(messages.value[1].error).toBe('网络错误')
  })
})
