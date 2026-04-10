/** 统一响应包装 */
export interface Result<T> {
  code: number;
  message: string;
  data: T | null;
  timestamp: string;
}

/** 分页结果 */
export interface PageResult<T> {
  records: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}

/** 登录请求 */
export interface LoginRequest {
  username: string;      // 登录用户名
  password: string;      // 密码（明文，HTTPS传输）
}

/** 注册请求 */
export interface RegisterRequest {
  username: string;
  password: string;
  roleCode: 'STUDENT' | 'TEACHER';
  email?: string;
}

/** 登录响应 */
export interface LoginResponse {
  token: string;           // JWT Token
  userInfo: UserInfoDTO;
  roles: RoleDTO[];
}

/** 用户信息 */
export interface UserInfoDTO {
  userId: number;
  username: string;
  email: string | null;
  avatarUrl: string | null;
  roleCode: string;        // 主角色代码
  roleName: string;        // 主角色名称
}

/** 角色信息 */
export interface RoleDTO {
  roleCode: string;
  roleName: string;
  description: string | null;
}

/** Token信息 */
export interface TokenDTO {
  token: string;
  expiresIn: number;       // 过期时间（秒）
}

/** 视频信息 */
export interface VideoDTO {
  id: number;
  courseId: number;
  title: string;
  ossUrl: string;
  duration: number;         // 时长（秒）
  parseStatus: 0 | 1 | 2 | 3;
  parseStatusDesc?: string; // 状态描述
  thumbnailUrl: string | null;
  orderNum: number;
  createdAt: string;
}

/** 知识片段 */
export interface FragmentDTO {
  id: number;
  videoId: number;
  startTime: number;       // 片段起始时间（秒）
  endTime: number;         // 片段结束时间（秒）
  contentText: string;     // 融合文本
  keyframeUrl: string | null;  // 关键帧图片
  asrText: string | null;      // 原始ASR文本
  visualTags: VisualTag[] | null; // 视觉标签
  summary: string | null;
  keywords: string[] | null;
  vectorId: string | null;
}

/** 视觉标签 */
export interface VisualTag {
  tag: '公式' | '图表' | '人脸' | '文字' | '代码' | '动画' | '实验';
  confidence: number;       // 0-1
  bbox?: [number, number, number, number]; // [x, y, w, h]
}

/** 解析状态 */
export interface ParseStatusDTO {
  videoId: number;
  parseStatus: 0 | 1 | 2 | 3;
  parseStatusDesc: string;
  fragmentCount: number;
  duration: number;
}

/** 上传签名 */
export interface UploadSignatureDTO {
  uploadUrl: string;        // OSS 签名上传 URL
  signUrl: string;          // OSS 签名访问 URL
  uploadId?: string;        // 分片上传 ID
}

/** 签名URL */
export interface SignUrlDTO {
  videoId: number;
  signUrl: string;          // 带签名的临时访问 URL
  expiresAt: string;        // URL 过期时间
}

/** 更新片段请求 */
export interface UpdateFragmentRequest {
  contentText?: string;
  summary?: string;
  keywords?: string[];
}

/** RAG 问答请求 */
export interface RagChatRequest {
  question: string;         // 学生提问
  videoId: number;          // 当前视频 ID
  currentTime?: number;     // 当前播放时间点（秒），用于 Rerank 加权
}

/** 问答日志 */
export interface QaLogDTO {
  id: number;
  question: string;
  answer: string;
  contextFragments: ContextFragment[] | null;
  modelVersion: string | null;
  tokenUsage: number | null;
  createdAt: string;
}

/** 上下文片段 */
export interface ContextFragment {
  fragmentId: number;
  startTime: number;
  endTime: number;
  relevance: number;        // 相关度 0-1
}

/** 热力图 */
export interface HeatmapDTO {
  videoId: number;
  duration: number;         // 视频总时长（秒）
  windowSize: number;       // 时间窗口（秒）
  data: HeatPoint[];
}

/** 热力点 */
export interface HeatPoint {
  startTime: number;
  endTime: number;
  score: number;            // 难度分 0-1
  isDifficulty: boolean;    // 是否超过阈值
}

/** 班级概览 */
export interface ClassOverviewDTO {
  totalStudents: number;
  avgCompletionRate: number;  // 平均完成率 0-1
  avgWatchDuration: number;   // 平均观看时长（秒）
  activeTodayCount: number;
  totalQuestions: number;
  difficultyPoints: number;   // 难点数量
}

/** 学生画像 */
export interface StudentProfileDTO {
  userId: number;
  username: string;
  totalWatchDuration: number;
  completionRate: number;
  questionCount: number;
  weakPoints: string[];       // 薄弱知识点
  learningTrend: TrendPoint[];
}

/** 趋势点 */
export interface TrendPoint {
  date: string;               // 日期
  watchDuration: number;
  questionCount: number;
}

/** 词云 */
export interface WordCloudDTO {
  courseId: number;
  words: WordItem[];
}

/** 词项 */
export interface WordItem {
  text: string;
  count: number;
  weight: number;             // 权重 0-1
}

/** 课程 */
export interface CourseDTO {
  id: number;
  title: string;
  teacherName: string;
  description: string | null;
  coverImage: string | null;
  videoCount: number;
  publishedAt: string;
}

/** 课程详情 */
export interface CourseDetailDTO {
  id: number;
  title: string;
  teacherName: string;
  description: string | null;
  coverImage: string | null;
  videos: VideoDTO[];
}

/** 播放信息 */
export interface PlayInfoDTO {
  videoId: number;
  title: string;
  signUrl: string;
  duration: number;
  fragments: FragmentDTO[];
  lastWatchTime: number | null;  // 断点续播时间点
}

/** 心跳请求 */
export interface HeartbeatRequest {
  videoId: number;
  currentTime: number;      // 当前播放时间（秒）
  status: 'PLAYING' | 'PAUSED' | 'SEEKING';
}

/** 进度请求 */
export interface ProgressRequest {
  videoId: number;
  currentTime: number;
  duration: number;
}

/** 笔记请求 */
export interface NoteRequest {
  videoId: number;
  timestamp: number;         // 关联的视频时间点（秒）
  content: string;
  isPublic?: boolean;
}

/** 笔记 */
export interface NoteDTO {
  id: number;
  videoId: number;
  timestamp: number;
  content: string;
  isPublic: boolean;
  createdAt: string;
}

/** 通知 */
export interface Notification {
  id: number;
  type: 'system' | 'course' | 'interaction';
  title: string;
  description: string;
  isRead: boolean;
  link?: string;
  createdAt: string;
}