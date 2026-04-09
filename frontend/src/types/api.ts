// 认证相关DTO
export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  userInfo: UserInfoDTO
  roles: RoleDTO[]
}

export interface UserInfoDTO {
  userId: number
  username: string
  email: string | null
  avatarUrl: string | null
  roleCode: string
  roleName: string
}

export interface RoleDTO {
  roleCode: string
  roleName: string
  description: string | null
}

// 视频相关DTO
export interface VideoDTO {
  id: number
  courseId: number
  title: string
  duration: number
  parseStatus: number
  thumbnailUrl: string | null
  orderNum: number
  createdAt: string
}

export interface FragmentDTO {
  id: number
  videoId: number
  startTime: number
  endTime: number
  contentText: string
  summary: string | null
  keywords: string[] | null
  vectorId: string | null
}

// RAG问答DTO
export interface RagChatRequest {
  question: string
  videoId: number
  currentTime?: number
}

export interface QaLogDTO {
  id: number
  question: string
  answer: string
  contextFragments: ContextFragment[] | null
  modelVersion: string | null
  tokenUsage: number | null
  createdAt: string
}

export interface ContextFragment {
  fragmentId: number
  startTime: number
  endTime: number
  relevance: number
}

// 学情分析DTO
export interface HeatmapDTO {
  videoId: number
  duration: number
  windowSize: number
  data: HeatPoint[]
}

export interface HeatPoint {
  startTime: number
  endTime: number
  score: number
  isDifficulty: boolean
}

export interface ClassOverviewDTO {
  totalStudents: number
  avgCompletionRate: number
  avgWatchDuration: number
  activeTodayCount: number
  totalQuestions: number
}

// 学生接口DTO
export interface HeartbeatRequest {
  videoId: number
  currentTime: number
  status: 'PLAYING' | 'PAUSED' | 'SEEKING'
}

export interface ProgressRequest {
  videoId: number
  currentTime: number
  duration: number
}

export interface NoteRequest {
  videoId: number
  timestamp: number
  content: string
}

export interface PlayInfoDTO {
  videoId: number
  title: string
  signUrl: string
  duration: number
  fragments: FragmentDTO[]
  lastWatchTime: number | null
}

// 通用响应格式
export interface Result<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}
