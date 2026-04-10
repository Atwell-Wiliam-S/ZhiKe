import { setupWorker } from 'msw/browser'
import { authHandlers } from './handlers/auth.handler'
import { videoHandlers } from './handlers/video.handler'
import { qaHandlers } from './handlers/qa.handler'
import { analyticsHandlers } from './handlers/analytics.handler'
import { studentHandlers } from './handlers/student.handler'
import { adminHandlers } from './handlers/admin.handler'

export const worker = setupWorker(
  ...authHandlers,
  ...videoHandlers,
  ...qaHandlers,
  ...analyticsHandlers,
  ...studentHandlers,
  ...adminHandlers,
)
