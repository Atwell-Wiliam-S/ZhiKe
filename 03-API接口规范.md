# 第三部分：后端 API 接口规范

> 来源：智能教学闭环系统重构设计文档 v1.2
> 
> 本文档定义系统的完整 API 接口规范，包括统一约定、接口清单、请求/响应示例、SSE 流式协议、分页规范、错误处理及 DTO 定义。

---

## 3.1 接口设计约定

### 3.1.1 通用规范

| 规范项 | 约定 |
| --- | --- |
| **协议** | HTTPS (TLS 1.2+) |
| **基础路径** | `/api` |
| **Content-Type** | `application/json`（文件上传除外） |
| **认证方式** | `Authorization: Bearer {jwt_token}` |
| **字符编码** | UTF-8 |
| **日期格式** | ISO 8601: `2024-01-15T08:30:00+08:00` |
| **时区** | Asia/Shanghai (UTC+8) |

### 3.1.2 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { /* 业务数据 */ },
  "timestamp": "2024-01-15T08:30:00+08:00"
}
```

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| code | int | 业务状态码（200=成功，其他=失败） |
| message | string | 状态描述，供前端 Toast 提示 |
| data | T / null | 业务数据，失败时为 null |
| timestamp | string | 服务器响应时间 |

### 3.1.3 分页规范

**请求参数：**

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| page | int | 1 | 当前页码（从 1 开始） |
| pageSize | int | 10 | 每页条数（最大 100） |
| sortBy | string | createdAt | 排序字段 |
| sortOrder | string | desc | 排序方向：asc / desc |

**分页响应包装：**

```json
{
  "code": 200,
  "data": {
    "records": [ /* 当前页数据 */ ],
    "total": 150,
    "page": 1,
    "pageSize": 10,
    "totalPages": 15
  }
}
```

### 3.1.4 文件上传规范

视频上传采用**分片上传**策略，前端直传 OSS（不经过后端），后端仅负责生成签名 URL。

```
1. 前端请求后端 → POST /api/video/upload-signature
2. 后端返回 OSS 签名 URL + uploadId
3. 前端直传 OSS → PUT {signatureUrl} (分片)
4. 前端通知后端 → POST /api/video/upload-complete
5. 后端创建 videos 记录
```

| 参数 | 约束 |
| --- | --- |
| 文件格式 | MP4, WebM, MOV |
| 单文件大小 | ≤ 500MB |
| 分片大小 | 5MB / 片 |

### 3.1.5 API 版本策略

当前版本为 **v1**，通过 URL 前缀区分：`/api/v1/auth/login`。预留版本升级路径，v1 → v2 切换时旧版本至少维护 6 个月。

---

## 3.2 接口调用流程

![API接口调用流程图](images/api-flow-diagram.jpg)

### 3.2.1 典型调用时序

**学生视频学习流程：**
```
1. POST /api/auth/login          → 获取 JWT Token
2. GET  /api/auth/info           → 获取用户信息 + 角色路由
3. GET  /api/student/courses     → 课程列表
4. GET  /api/student/courses/:id → 课程详情（含视频列表）
5. GET  /api/student/videos/:id/play-info → 播放信息（含签名URL）
6. POST /api/student/records/heartbeat  → 心跳上报（每10秒）
7. POST /api/qa/chat-stream      → SSE 流式问答
8. POST /api/student/records/progress   → 保存进度（离开时）
```

**教师视频管理流程：**
```
1. POST /api/video/upload-signature → 获取 OSS 上传凭证
2. [前端直传 OSS]
3. POST /api/video/upload-complete  → 创建视频记录
4. POST /api/video/:id/parse        → 触发 AI 解析
5. GET  /api/video/:id/status       → 轮询解析状态
6. GET  /api/teacher/fragments      → 查看知识片段
7. PUT  /api/teacher/fragments/:id  → 编辑/审核片段
```

---

## 3.3 认证授权接口组

**Base URL:** `/api/auth` | **权限:** 公开（login/register）+ 已认证（logout/refresh/info）

### 3.3.1 POST /api/auth/login — 登录

**请求体：**
```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

**成功响应 (200)：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userInfo": {
      "userId": 1,
      "username": "zhangsan",
      "email": "zhangsan@edu.cn",
      "avatarUrl": null,
      "roleCode": "STUDENT",
      "roleName": "学生"
    },
    "roles": [
      { "roleCode": "STUDENT", "roleName": "学生", "description": "可观看视频、提问" }
    ]
  }
}
```

**失败响应 (401)：**
```json
{ "code": 401, "message": "用户名或密码错误", "data": null }
```

### 3.3.2 接口清单

| 路径 | 方法 | 功能 | 请求体 | 响应 Data | 权限 |
| --- | --- | --- | --- | --- | --- |
| /api/auth/login | POST | 登录 | LoginRequest | LoginResponse | 公开 |
| /api/auth/register | POST | 注册 | RegisterRequest | number (userId) | 公开 |
| /api/auth/logout | POST | 登出 | - | void | 已认证 |
| /api/auth/refresh | POST | 刷新Token | - | TokenDTO | 已认证 |
| /api/auth/info | GET | 获取用户信息 | - | UserInfoDTO | 已认证 |

---

## 3.4 视频资源接口组

**Base URL:** `/api/video`, `/api/teacher` | **权限:** TEACHER

### 3.4.1 POST /api/video/:id/parse — 触发解析

**路径参数：** `id` (int) — 视频编号

**成功响应 (200)：**
```json
{ "code": 200, "message": "解析任务已提交", "data": null }
```

**状态冲突 (409)：**
```json
{ "code": 409, "message": "视频正在解析中，请勿重复提交", "data": null }
```

### 3.4.2 GET /api/video/:id/status — 查询解析状态

**成功响应 (200)：**
```json
{
  "code": 200,
  "data": {
    "videoId": 1,
    "parseStatus": 2,
    "parseStatusDesc": "解析完成",
    "fragmentCount": 15,
    "duration": 600
  }
}
```

### 3.4.3 接口清单

| 路径 | 方法 | 功能 | 请求参数 | 响应 Data | 权限 |
| --- | --- | --- | --- | --- | --- |
| /api/video/upload-signature | POST | 获取上传凭证 | {fileName, fileSize, courseId} | UploadSignatureDTO | TEACHER |
| /api/video/upload-complete | POST | 上传完成通知 | {courseId, title, ossUrl, duration} | VideoDTO | TEACHER |
| /api/video/:id/parse | POST | 触发解析 | 路径参数 id | void | TEACHER |
| /api/video/:id/status | GET | 查询解析状态 | 路径参数 id | ParseStatusDTO | TEACHER |
| /api/video/:id/sign-url | GET | 获取播放签名URL | 路径参数 id | SignUrlDTO | STUDENT/TEACHER |
| /api/teacher/videos | GET | 教师视频列表 | courseId, page, pageSize | PageResult\<VideoDTO\> | TEACHER |
| /api/teacher/fragments | GET | 知识片段列表 | videoId | FragmentDTO[] | TEACHER |
| /api/teacher/fragments/:id | PUT | 更新片段 | UpdateFragmentRequest | void | TEACHER |
| /api/teacher/fragments/:id | DELETE | 删除片段 | 路径参数 id | void | TEACHER |

---

## 3.5 RAG 智能问答接口组

**Base URL:** `/api/qa` | **权限:** STUDENT

### 3.5.1 SSE 流式协议规范

**端点：** `POST /api/qa/chat-stream`
**Content-Type:** `application/json`
**Accept:** `text/event-stream`

**请求体：**
```json
{
  "question": "傅里叶变换的物理意义是什么？",
  "videoId": 1,
  "currentTime": 245.5
}
```

**SSE 响应流格式：**

```
event: message
data: {"type":"chunk","content":"傅里叶变换"}

event: message
data: {"type":"chunk","content":"的物理意义"}

event: message
data: {"type":"chunk","content":"是将信号从时域转换到"}

event: message
data: {"type":"chunk","content":"频域进行分析..."}

event: message
data: {"type":"source","fragments":[{"fragmentId":1,"startTime":120.5,"endTime":185.0,"relevance":0.95},{"fragmentId":3,"startTime":240.0,"endTime":310.5,"relevance":0.82}]}

event: message
data: {"type":"done","tokenUsage":256}
```

**SSE 事件类型定义：**

| type | 字段 | 说明 |
| --- | --- | --- |
| chunk | content: string | 文本片段，前端追加渲染 |
| source | fragments: ContextFragment[] | 参考来源，前端渲染引用标签 |
| done | tokenUsage: number | 生成完成，附带 Token 消耗 |
| error | message: string, code: int | 生成失败（模型超时/限流等） |

**前端 EventSource 处理示例：**

```typescript
const res = await fetch('/api/qa/chat-stream', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`,
    'Accept': 'text/event-stream',
  },
  body: JSON.stringify({ question, videoId, currentTime }),
});

const reader = res.body!.getReader();
const decoder = new TextDecoder();

while (true) {
  const { done, value } = await reader.read();
  if (done) break;
  const text = decoder.decode(value);
  // 按 \n\n 分割 SSE 事件
  const events = text.split('\n\n');
  for (const event of events) {
    const dataLine = event.split('\n').find(l => l.startsWith('data:'));
    if (!dataLine) continue;
    const json = JSON.parse(dataLine.slice(5));
    switch (json.type) {
      case 'chunk': appendContent(json.content); break;
      case 'source': setSources(json.fragments); break;
      case 'done': setTokenUsage(json.tokenUsage); break;
      case 'error': showError(json.message); break;
    }
  }
}
```

### 3.5.2 接口清单

| 路径 | 方法 | 功能 | 请求参数 | 响应 Data | 权限 |
| --- | --- | --- | --- | --- | --- |
| /api/qa/chat-stream | POST (SSE) | 流式问答 | RagChatRequest | text/event-stream | STUDENT |
| /api/qa/history | GET | 问答历史 | page, pageSize | PageResult\<QaLogDTO\> | STUDENT |

---

## 3.6 学情分析接口组

**Base URL:** `/api/analytics` | **权限:** TEACHER, ADMIN

### 3.6.1 GET /api/analytics/heatmap — 难点热力图

**查询参数：** `courseId=1&videoId=5`

**成功响应 (200)：**
```json
{
  "code": 200,
  "data": {
    "videoId": 5,
    "duration": 600,
    "windowSize": 30,
    "data": [
      { "startTime": 0, "endTime": 30, "score": 0.2, "isDifficulty": false },
      { "startTime": 30, "endTime": 60, "score": 0.5, "isDifficulty": false },
      { "startTime": 120, "endTime": 150, "score": 0.85, "isDifficulty": true },
      { "startTime": 240, "endTime": 270, "score": 0.92, "isDifficulty": true }
    ]
  }
}
```

### 3.6.2 GET /api/analytics/class-overview — 班级学情概览

**查询参数：** `courseId=1`

**成功响应 (200)：**
```json
{
  "code": 200,
  "data": {
    "totalStudents": 156,
    "avgCompletionRate": 0.72,
    "avgWatchDuration": 2850,
    "activeTodayCount": 89,
    "totalQuestions": 1243,
    "difficultyPoints": 5
  }
}
```

### 3.6.3 接口清单

| 路径 | 方法 | 功能 | 查询参数 | 响应 Data | 权限 |
| --- | --- | --- | --- | --- | --- |
| /api/analytics/heatmap | GET | 难点热力图 | courseId, videoId | HeatmapDTO | TEACHER, ADMIN |
| /api/analytics/class-overview | GET | 班级学情概览 | courseId | ClassOverviewDTO | TEACHER, ADMIN |
| /api/analytics/student-profile | GET | 学生学习画像 | studentId | StudentProfileDTO | TEACHER, ADMIN |
| /api/analytics/wordcloud | GET | 高频困惑词云 | courseId, startDate?, endDate? | WordCloudDTO | TEACHER, ADMIN |

---

## 3.7 学生接口组

**Base URL:** `/api/student` | **权限:** STUDENT

### 3.7.1 GET /api/student/courses — 课程列表

**查询参数：** `keyword=数学&page=1&pageSize=10`

**成功响应 (200)：**
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 1,
        "title": "高等数学 A",
        "teacherName": "李教授",
        "description": "涵盖微积分、线性代数...",
        "coverImage": "/covers/math-a.jpg",
        "videoCount": 24,
        "publishedAt": "2024-01-10T08:00:00+08:00"
      }
    ],
    "total": 8,
    "page": 1,
    "pageSize": 10,
    "totalPages": 1
  }
}
```

### 3.7.2 GET /api/student/videos/:id/play-info — 播放信息

**成功响应 (200)：**
```json
{
  "code": 200,
  "data": {
    "videoId": 5,
    "title": "第三章：傅里叶变换",
    "signUrl": "https://oss.zhike.edu.cn/videos/math-03.mp4?OSSAccessKeyId=xxx&Signature=yyy&Expires=zzz",
    "duration": 600,
    "fragments": [
      { "id": 1, "startTime": 0, "endTime": 120, "summary": "傅里叶变换的定义与公式推导" },
      { "id": 2, "startTime": 120, "endTime": 300, "summary": "频域分析的基本方法" }
    ],
    "lastWatchTime": 245.5
  }
}
```

### 3.7.3 POST /api/student/records/heartbeat — 心跳上报

**请求体：**
```json
{
  "videoId": 5,
  "currentTime": 245.5,
  "status": "PLAYING"
}
```

> 前端每 10 秒发送一次，后端更新 `learning_records.last_watch_time` 和 `total_duration`。

### 3.7.4 接口清单

| 路径 | 方法 | 功能 | 请求参数 | 响应 Data | 权限 |
| --- | --- | --- | --- | --- | --- |
| /api/student/courses | GET | 课程列表 | keyword?, page, pageSize | PageResult\<CourseDTO\> | STUDENT |
| /api/student/courses/:id | GET | 课程详情 | 路径参数 id | CourseDetailDTO | STUDENT |
| /api/student/videos/:id/play-info | GET | 播放信息 | 路径参数 id | PlayInfoDTO | STUDENT |
| /api/student/records/heartbeat | POST | 心跳上报 | HeartbeatRequest | void | STUDENT |
| /api/student/records/progress | POST | 保存进度 | ProgressRequest | void | STUDENT |
| /api/student/notes | POST | 添加笔记 | NoteRequest | number (noteId) | STUDENT |
| /api/student/notes | GET | 笔记列表 | videoId | NoteDTO[] | STUDENT |

---

## 3.8 统一错误码定义

### 3.8.1 业务错误码

| 错误码 | HTTP 状态 | 含义 | 说明 | 前端处理 |
| --- | --- | --- | --- | --- |
| 200 | 200 | 成功 | 请求处理成功 | 正常渲染 |
| 400 | 400 | 参数错误 | 请求参数校验失败 | 显示 message |
| 401 | 401 | 未认证 | Token 缺失/无效/过期 | 跳转登录页 |
| 403 | 403 | 无权限 | 角色权限不足 | 显示"无权限"提示 |
| 404 | 404 | 资源不存在 | 请求资源未找到 | 显示 404 页面 |
| 409 | 409 | 状态冲突 | 视频正在解析中 | 提示"请勿重复操作" |
| 429 | 429 | 请求过频 | 触发速率限制 | 提示"操作过于频繁" |
| 500 | 500 | 服务器错误 | 未预期异常 | 显示"系统异常" |
| 503 | 503 | 服务不可用 | 外部 AI 服务失败 | 提示"AI服务暂不可用" |

### 3.8.2 错误响应格式

```json
{
  "code": 401,
  "message": "Token已过期，请重新登录",
  "data": null,
  "timestamp": "2024-01-15T08:30:00+08:00"
}
```

### 3.8.3 前端 Axios 拦截器处理策略

| 错误码 | 拦截器行为 |
| --- | --- |
| 401 | 清除 Token + 跳转 `/login` + 显示 message |
| 403 | 显示 ElMessage.warning(message) |
| 429 | 显示 ElMessage.error("操作过于频繁，请稍后再试") |
| 500 | 显示 ElMessage.error("系统异常，请稍后重试") |
| 503 | 显示 ElMessage.error("AI 服务暂不可用，请稍后重试") |

---

## 3.9 DTO 完整定义

以下 TypeScript 接口确保前后端字段名、类型完全一致。后端 Jackson `SnakeCaseStrategy` 序列化为 `snake_case` 传输，前端 Axios 拦截器自动转换为 `camelCase`。

### 3.9.1 通用 DTO

```typescript
/** 统一响应包装 */
interface Result<T> {
  code: number;
  message: string;
  data: T | null;
  timestamp: string;
}

/** 分页结果 */
interface PageResult<T> {
  records: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}
```

### 3.9.2 认证相关 DTO

```typescript
interface LoginRequest {
  username: string;      // 登录用户名
  password: string;      // 密码（明文，HTTPS传输）
}

interface RegisterRequest {
  username: string;
  password: string;
  roleCode: 'STUDENT' | 'TEACHER';
  email?: string;
}

interface LoginResponse {
  token: string;           // JWT Token
  userInfo: UserInfoDTO;
  roles: RoleDTO[];
}

interface UserInfoDTO {
  userId: number;
  username: string;
  email: string | null;
  avatarUrl: string | null;
  roleCode: string;        // 主角色代码
  roleName: string;        // 主角色名称
}

interface RoleDTO {
  roleCode: string;
  roleName: string;
  description: string | null;
}

interface TokenDTO {
  token: string;
  expiresIn: number;       // 过期时间（秒）
}
```

### 3.9.3 视频相关 DTO

```typescript
interface VideoDTO {
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

interface FragmentDTO {
  id: number;
  videoId: number;
  startTime: number;       // 片段起始时间（秒）
  endTime: number;         // 片段结束时间（秒）
  contentText: string;     // 融合文本
  keyframeUrl: string | null;  // 🆕 关键帧图片
  asrText: string | null;      // 🆕 原始ASR文本
  visualTags: VisualTag[] | null; // 🆕 视觉标签
  summary: string | null;
  keywords: string[] | null;
  vectorId: string | null;
}

/** 🆕 视觉标签（对应 knowledge_fragments.visual_tags JSON） */
interface VisualTag {
  tag: '公式' | '图表' | '人脸' | '文字' | '代码' | '动画' | '实验';
  confidence: number;       // 0-1
  bbox?: [number, number, number, number]; // [x, y, w, h]
}

interface ParseStatusDTO {
  videoId: number;
  parseStatus: 0 | 1 | 2 | 3;
  parseStatusDesc: string;
  fragmentCount: number;
  duration: number;
}

interface UploadSignatureDTO {
  uploadUrl: string;        // OSS 签名上传 URL
  signUrl: string;          // OSS 签名访问 URL
  uploadId?: string;        // 分片上传 ID
}

interface SignUrlDTO {
  videoId: number;
  signUrl: string;          // 带签名的临时访问 URL
  expiresAt: string;        // URL 过期时间
}

interface UpdateFragmentRequest {
  contentText?: string;
  summary?: string;
  keywords?: string[];
}
```

### 3.9.4 RAG 问答 DTO

```typescript
interface RagChatRequest {
  question: string;         // 学生提问
  videoId: number;          // 当前视频 ID
  currentTime?: number;     // 当前播放时间点（秒），用于 Rerank 加权
}

interface QaLogDTO {
  id: number;
  question: string;
  answer: string;
  contextFragments: ContextFragment[] | null;
  modelVersion: string | null;
  tokenUsage: number | null;
  createdAt: string;
}

interface ContextFragment {
  fragmentId: number;
  startTime: number;
  endTime: number;
  relevance: number;        // 相关度 0-1
}
```

### 3.9.5 学情分析 DTO

```typescript
interface HeatmapDTO {
  videoId: number;
  duration: number;         // 视频总时长（秒）
  windowSize: number;       // 时间窗口（秒）
  data: HeatPoint[];
}

interface HeatPoint {
  startTime: number;
  endTime: number;
  score: number;            // 难度分 0-1
  isDifficulty: boolean;    // 是否超过阈值
}

interface ClassOverviewDTO {
  totalStudents: number;
  avgCompletionRate: number;  // 平均完成率 0-1
  avgWatchDuration: number;   // 平均观看时长（秒）
  activeTodayCount: number;
  totalQuestions: number;
  difficultyPoints: number;   // 难点数量
}

interface StudentProfileDTO {
  userId: number;
  username: string;
  totalWatchDuration: number;
  completionRate: number;
  questionCount: number;
  weakPoints: string[];       // 薄弱知识点
  learningTrend: TrendPoint[];
}

interface TrendPoint {
  date: string;               // 日期
  watchDuration: number;
  questionCount: number;
}

interface WordCloudDTO {
  courseId: number;
  words: WordItem[];
}

interface WordItem {
  text: string;
  count: number;
  weight: number;             // 权重 0-1
}
```

### 3.9.6 学生接口 DTO

```typescript
interface CourseDTO {
  id: number;
  title: string;
  teacherName: string;
  description: string | null;
  coverImage: string | null;
  videoCount: number;
  publishedAt: string;
}

interface CourseDetailDTO {
  id: number;
  title: string;
  teacherName: string;
  description: string | null;
  coverImage: string | null;
  videos: VideoDTO[];
}

interface PlayInfoDTO {
  videoId: number;
  title: string;
  signUrl: string;
  duration: number;
  fragments: FragmentDTO[];
  lastWatchTime: number | null;  // 断点续播时间点
}

interface HeartbeatRequest {
  videoId: number;
  currentTime: number;      // 当前播放时间（秒）
  status: 'PLAYING' | 'PAUSED' | 'SEEKING';
}

interface ProgressRequest {
  videoId: number;
  currentTime: number;
  duration: number;
}

interface NoteRequest {
  videoId: number;
  timestamp: number;         // 关联的视频时间点（秒）
  content: string;
}

interface NoteDTO {
  id: number;
  videoId: number;
  timestamp: number;
  content: string;
  isPublic: boolean;
  createdAt: string;
}
```

---

## 3.10 前后端字段映射规范

后端 Java `camelCase` → Jackson `SnakeCaseStrategy` → 网络 `snake_case` → Axios 拦截器 → 前端 TypeScript `camelCase`。

| 前端 TS 字段 | 网络 snake_case | 后端 Java 字段 | 数据库字段 |
| --- | --- | --- | --- |
| userId | user_id | userId | id (users) |
| createdAt | created_at | createdAt | created_at |
| parseStatus | parse_status | parseStatus | parse_status |
| videoId | video_id | videoId | video_id |
| tokenUsage | token_usage | tokenUsage | token_usage |
| avatarUrl | avatar_url | avatarUrl | avatar_url |
| roleCode | role_code | roleCode | role_code |
| courseId | course_id | courseId | course_id |
| ossUrl | oss_url | ossUrl | oss_url |
| orderNum | order_num | orderNum | order_num |
| keyframeUrl | keyframe_url | keyframeUrl | keyframe_url |
| asrText | asr_text | asrText | asr_text |
| visualTags | visual_tags | visualTags | visual_tags |
| contentText | content_text | contentText | content_text |
| vectorId | vector_id | vectorId | vector_id |
| fragmentId | fragment_id | fragmentId | fragment_id |
| currentTime | current_time | currentTime | - (请求参数) |
| lastWatchTime | last_watch_time | lastWatchTime | last_watch_time |
| isCompleted | is_completed | isCompleted | is_completed |
| isPublished | is_published | isPublished | is_published |
| totalPages | total_pages | totalPages | - (计算字段) |
