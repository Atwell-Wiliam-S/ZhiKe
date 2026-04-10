export interface Result<T> {
  code: number;
  message: string;
  data: T | null;
  timestamp: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  roleCode: 'STUDENT' | 'TEACHER';
  email?: string;
}

export interface LoginResponse {
  token: string;
  userInfo: UserInfoDTO;
  roles: RoleDTO[];
}

export interface UserInfoDTO {
  userId: number;
  username: string;
  email: string | null;
  avatarUrl: string | null;
  roleCode: string;
  roleName: string;
}

export interface RoleDTO {
  roleCode: string;
  roleName: string;
  description: string | null;
}

export interface TokenDTO {
  token: string;
  expiresIn: number;
}

export interface VideoDTO {
  id: number;
  courseId: number;
  title: string;
  ossUrl: string;
  duration: number;
  parseStatus: 0 | 1 | 2 | 3;
  parseStatusDesc?: string;
  thumbnailUrl: string | null;
  orderNum: number;
  createdAt: string;
}

export interface FragmentDTO {
  id: number;
  videoId: number;
  startTime: number;
  endTime: number;
  contentText: string;
  keyframeUrl: string | null;
  asrText: string | null;
  visualTags: VisualTag[] | null;
  summary: string | null;
  keywords: string[] | null;
  vectorId: string | null;
}

export interface VisualTag {
  tag: '公式' | '图表' | '人脸' | '文字' | '代码' | '动画' | '实验';
  confidence: number;
  bbox?: [number, number, number, number];
}

export interface ParseStatusDTO {
  videoId: number;
  parseStatus: 0 | 1 | 2 | 3;
  parseStatusDesc: string;
  fragmentCount: number;
  duration: number;
}

export interface UploadSignatureDTO {
  uploadUrl: string;
  signUrl: string;
  uploadId?: string;
}

export interface SignUrlDTO {
  videoId: number;
  signUrl: string;
  expiresAt: string;
}

export interface UpdateFragmentRequest {
  contentText?: string;
  summary?: string;
  keywords?: string[];
}

export interface RagChatRequest {
  question: string;
  videoId: number;
  currentTime?: number;
}

export interface QaLogDTO {
  id: number;
  question: string;
  answer: string;
  contextFragments: ContextFragment[] | null;
  modelVersion: string | null;
  tokenUsage: number | null;
  createdAt: string;
}

export interface ContextFragment {
  fragmentId: number;
  startTime: number;
  endTime: number;
  relevance: number;
}

export interface HeatmapDTO {
  videoId: number;
  duration: number;
  windowSize: number;
  data: HeatPoint[];
}

export interface HeatPoint {
  startTime: number;
  endTime: number;
  score: number;
  isDifficulty: boolean;
}

export interface ClassOverviewDTO {
  totalStudents: number;
  avgCompletionRate: number;
  avgWatchDuration: number;
  activeTodayCount: number;
  totalQuestions: number;
  difficultyPoints: number;
}

export interface StudentProfileDTO {
  userId: number;
  username: string;
  totalWatchDuration: number;
  completionRate: number;
  questionCount: number;
  weakPoints: string[];
  learningTrend: TrendPoint[];
}

export interface TrendPoint {
  date: string;
  watchDuration: number;
  questionCount: number;
}

export interface WordCloudDTO {
  courseId: number;
  words: WordItem[];
}

export interface WordItem {
  text: string;
  count: number;
  weight: number;
}

export interface CourseDTO {
  id: number;
  title: string;
  teacherName: string;
  description: string | null;
  coverImage: string | null;
  videoCount: number;
  publishedAt: string;
}

export interface CourseDetailDTO {
  id: number;
  title: string;
  teacherName: string;
  description: string | null;
  coverImage: string | null;
  videos: VideoDTO[];
}

export interface PlayInfoDTO {
  videoId: number;
  title: string;
  signUrl: string;
  duration: number;
  fragments: FragmentDTO[];
  lastWatchTime: number | null;
}

export interface HeartbeatRequest {
  videoId: number;
  currentTime: number;
  status: 'PLAYING' | 'PAUSED' | 'SEEKING';
}

export interface ProgressRequest {
  videoId: number;
  currentTime: number;
  duration: number;
}

export interface NoteRequest {
  videoId: number;
  timestamp: number;
  content: string;
  isPublic?: boolean;
}

export interface NoteDTO {
  id: number;
  videoId: number;
  timestamp: number;
  content: string;
  isPublic: boolean;
  createdAt: string;
}

export interface Notification {
  id: number;
  type: 'system' | 'course' | 'interaction';
  title: string;
  description: string;
  isRead: boolean;
  link?: string;
  createdAt: string;
}
