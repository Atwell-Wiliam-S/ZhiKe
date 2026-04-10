import { ref, type Ref } from 'vue'
import type { ContextFragment } from '@/types/api'

export interface ChatMessage {
  id: string
  role: 'user' | 'assistant'
  content: string
  isStreaming?: boolean
  sources?: ContextFragment[]
  tokenUsage?: number
  error?: string
  createdAt: Date
}

interface UseSSEChatOptions {
  videoId: () => number
  apiBase?: string
  onError?: (error: Error) => void
}

export function useSSEChat(options: UseSSEChatOptions) {
  const messages: Ref<ChatMessage[]> = ref([])
  const isStreaming = ref(false)
  const abortController = ref<AbortController | null>(null)

  function genId(): string {
    return `msg_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`
  }

  async function sendMessage(question: string, currentTime?: number) {
    if (isStreaming.value) return

    const userMsg: ChatMessage = {
      id: genId(),
      role: 'user',
      content: question,
      createdAt: new Date(),
    }
    messages.value.push(userMsg)

    const aiMsg: ChatMessage = {
      id: genId(),
      role: 'assistant',
      content: '',
      isStreaming: true,
      sources: [],
      createdAt: new Date(),
    }
    messages.value.push(aiMsg)

    isStreaming.value = true
    abortController.value = new AbortController()

    try {
      const response = await fetch(
        `${options.apiBase ?? '/api'}/qa/chat-stream`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('zhike-token')}`,
          },
          body: JSON.stringify({
            question,
            videoId: options.videoId(),
            currentTime,
          }),
          signal: abortController.value.signal,
        }
      )

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`)
      }

      const reader = response.body?.getReader()
      if (!reader) throw new Error('ReadableStream 不可用')

      const decoder = new TextDecoder()
      let buffer = ''

      while (true) {
        const { done, value } = await reader.read()
        if (done) break

        buffer += decoder.decode(value, { stream: true })

        const events = buffer.split('\n\n')
        buffer = events.pop() ?? ''

        for (const event of events) {
          const dataLine = event
            .split('\n')
            .find(line => line.startsWith('data:'))
          if (!dataLine) continue

          const jsonStr = dataLine.slice(5).trim()
          if (!jsonStr) continue

          try {
            const data = JSON.parse(jsonStr)

            switch (data.type) {
              case 'chunk':
                aiMsg.content += data.content ?? ''
                break

              case 'source':
                aiMsg.sources = data.fragments ?? []
                break

              case 'done':
                aiMsg.isStreaming = false
                aiMsg.tokenUsage = data.tokenUsage ?? 0
                break

              case 'error':
                aiMsg.isStreaming = false
                aiMsg.error = data.message ?? 'AI 回答出错'
                break
            }
          } catch {
          }
        }
      }
    } catch (error: any) {
      if (error.name === 'AbortError') {
        aiMsg.isStreaming = false
        aiMsg.content += '\n\n[已中断]'
      } else {
        aiMsg.isStreaming = false
        aiMsg.error = error.message ?? '网络错误'
        options.onError?.(error)
      }
    } finally {
      isStreaming.value = false
      abortController.value = null
    }
  }

  function abortStream() {
    abortController.value?.abort()
  }

  function clearMessages() {
    messages.value = []
  }

  return {
    messages,
    isStreaming,
    sendMessage,
    abortStream,
    clearMessages,
  }
}
