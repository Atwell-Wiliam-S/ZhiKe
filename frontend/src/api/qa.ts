import request from '@/utils/request'
import type { QaLogDTO, PageResult } from '@/types/api'

export const qaApi = {
  chatStreamUrl: '/qa/chat-stream',

  getHistory: (params: { page: number; pageSize: number }) =>
    request.get<PageResult<QaLogDTO>>('/qa/history', { params }),

  getQaLog: (qaLogId: number) =>
    request.get<QaLogDTO>(`/qa/history/${qaLogId}`),
}
