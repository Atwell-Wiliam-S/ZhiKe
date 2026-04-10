# 智能教学闭环系统 Code Wiki

## 1. 项目概述

智能教学闭环系统是一个基于前后端分离架构的教育平台，支持视频教学、AI智能问答、学情分析等核心功能。系统采用模块化设计，分为前端和后端两大部分，后端负责业务逻辑处理和数据存储，前端负责用户界面展示和交互。

### 1.1 技术栈

| 分类 | 技术 | 版本 | 用途 |
|------|------|------|------|
| **后端框架** | Spring Boot | 3.2.0 | 应用核心框架 |
| **后端语言** | Java | 17 | 后端开发语言 |
| **ORM框架** | MyBatis-Plus | 3.5.5 | 数据库操作 |
| **安全框架** | Spring Security | 6.2.x | 认证与授权 |
| **认证方案** | JWT | - | 无状态认证 |
| **数据库** | MySQL | 8.0 | 关系型数据存储 |
| **缓存** | Redis | 7.0 | 缓存和会话管理 |
| **搜索引擎** | Elasticsearch | 8.x | 全文搜索 |
| **消息队列** | RabbitMQ | 3.12 | 异步消息处理 |
| **前端框架** | Vue | 3.5.32 | 前端核心框架 |
| **状态管理** | Pinia | 2.1.7 | 前端状态管理 |
| **路由** | Vue Router | 4.2.5 | 前端路由管理 |
| **HTTP客户端** | Axios | 1.6.7 | API调用 |
| **UI组件库** | Element Plus | 2.5.0 | 前端UI组件 |
| **图表库** | ECharts | 5.5.0 | 数据可视化 |
| **图标库** | Lucide Vue Next | 0.300.0 | 前端图标 |
| **类型系统** | TypeScript | ~6.0.2 | 类型检查 |
| **构建工具** | Vite | ^8.0.4 | 前端构建 |
| **模拟数据** | MSW | ^2.0.0 | 前端Mock数据 |

## 2. 项目结构

### 2.1 后端结构

```
backend/
├── common/          # 通用模块（CORS配置、Jackson配置、全局异常处理、JwtUtil、Result/PageResult）
├── module-security/ # 安全模块（JWT认证、RBAC权限）
├── module-video/    # 视频模块
├── module-rag/      # RAG智能问答模块
├── module-analytics/# 学情分析模块
├── module-teacher/  # 教师模块
├── module-student/  # 学生模块
├── module-admin/    # 管理员模块
├── module-ai-adapter/# AI服务适配器（DashScope SDK封装）
└── module-app/      # 主应用模块
```

### 2.2 前端结构

```
frontend/src/
├── api/             # API调用模块
├── assets/          # 静态资源
├── components/      # 组件
│   └── ui/          # UI组件
├── composables/     # 组合式函数
├── mocks/           # Mock数据
│   ├── data/        # 模拟数据
│   └── handlers/    # 模拟请求处理器
├── router/          # 路由配置
├── stores/          # 状态管理
├── types/           # 类型定义
├── utils/           # 工具函数
├── views/           # 页面视图
│   ├── login/       # 登录页面
│   ├── student/     # 学生页面
│   ├── teacher/     # 教师页面
│   └── NotFound.vue # 404页面
├── App.vue          # 根组件
├── main.ts          # 入口文件
└── style.css        # 全局样式
```

## 3. 核心模块

### 3.1 认证与授权模块

#### 3.1.1 后端认证模块

- **功能**：处理用户登录、注册、JWT token生成与验证
- **API端点**：
  - `POST /api/auth/login` - 用户登录
  - `POST /api/auth/register` - 用户注册
  - `GET /api/auth/info` - 获取用户信息
  - `POST /api/auth/refresh` - 刷新token

#### 3.1.2 前端认证模块

- **文件**：[frontend/src/api/auth.ts](file:///workspace/frontend/src/api/auth.ts)
- **功能**：封装认证相关API调用
- **核心函数**：
  - `login(data: LoginRequest)` - 登录
  - `register(data: RegisterRequest)` - 注册
  - `getUserInfo()` - 获取用户信息
  - `refreshToken()` - 刷新token

### 3.2 视频模块

#### 3.2.1 后端视频模块

- **功能**：处理视频上传、视频列表获取、视频详情获取等
- **API端点**：
  - `GET /api/video/list` - 获取视频列表
  - `GET /api/video/{id}` - 获取视频详情
  - `POST /api/video/upload` - 上传视频
  - `PUT /api/video/{id}` - 更新视频
  - `DELETE /api/video/{id}` - 删除视频

#### 3.2.2 前端视频模块

- **文件**：[frontend/src/api/video.ts](file:///workspace/frontend/src/api/video.ts)
- **功能**：封装视频相关API调用
- **页面**：
  - [VideoLearning.vue](file:///workspace/frontend/src/views/student/VideoLearning.vue) - 视频学习页面
  - [VideoUpload.vue](file:///workspace/frontend/src/views/teacher/VideoUpload.vue) - 视频上传页面

### 3.3 智能问答模块

#### 3.3.1 后端问答模块

- **功能**：处理用户提问、智能回答、问答历史记录等
- **API端点**：
  - `POST /api/qa/ask` - 提问
  - `GET /api/qa/history` - 获取问答历史

#### 3.3.2 前端问答模块

- **文件**：[frontend/src/api/qa.ts](file:///workspace/frontend/src/api/qa.ts)
- **功能**：封装问答相关API调用
- **页面**：
  - [QaHistory.vue](file:///workspace/frontend/src/views/student/QaHistory.vue) - 问答历史页面

### 3.4 学情分析模块

#### 3.4.1 后端分析模块

- **功能**：处理学习数据统计、热力图生成、学生画像分析等
- **API端点**：
  - `GET /api/analytics/heatmap/{videoId}` - 获取热力图数据
  - `GET /api/analytics/classOverview` - 获取班级概览
  - `GET /api/analytics/studentProfile/{userId}` - 获取学生画像
  - `GET /api/analytics/wordCloud/{videoId}` - 获取词云数据

#### 3.4.2 前端分析模块

- **文件**：[frontend/src/api/analytics.ts](file:///workspace/frontend/src/api/analytics.ts)
- **功能**：封装学情分析相关API调用
- **页面**：
  - [Analytics.vue](file:///workspace/frontend/src/views/teacher/Analytics.vue) - 学情分析页面

### 3.5 教师模块

#### 3.5.1 后端教师模块

- **功能**：处理教师课程管理、学情分析等
- **API端点**：
  - `GET /api/teacher/courses` - 获取课程列表
  - `POST /api/teacher/courses` - 创建课程
  - `PUT /api/teacher/courses/{id}` - 更新课程
  - `DELETE /api/teacher/courses/{id}` - 删除课程
  - `GET /api/teacher/analytics` - 获取学情分析

#### 3.5.2 前端教师模块

- **文件**：[frontend/src/api/admin.ts](file:///workspace/frontend/src/api/admin.ts)
- **功能**：封装教师相关API调用
- **页面**：
  - [Home.vue (Teacher)](file:///workspace/frontend/src/views/teacher/Home.vue) - 教师仪表盘
  - [KnowledgeGraph.vue](file:///workspace/frontend/src/views/teacher/KnowledgeGraph.vue) - 知识图谱管理

### 3.6 学生模块

#### 3.6.1 后端学生模块

- **功能**：处理学生课程学习、学习进度、笔记管理等
- **API端点**：
  - `GET /api/student/courses` - 获取课程列表
  - `GET /api/student/progress` - 获取学习进度
  - `POST /api/student/notes` - 创建笔记
  - `GET /api/student/notes` - 获取笔记列表
  - `GET /api/student/analytics` - 获取个人学情分析

#### 3.6.2 前端学生模块

- **文件**：[frontend/src/api/student.ts](file:///workspace/frontend/src/api/student.ts)
- **功能**：封装学生相关API调用
- **页面**：
  - [Home.vue (Student)](file:///workspace/frontend/src/views/student/Home.vue) - 学生首页
  - [CourseDetail.vue](file:///workspace/frontend/src/views/student/CourseDetail.vue) - 课程详情
  - [Notes.vue](file:///workspace/frontend/src/views/student/Notes.vue) - 笔记列表

## 4. 核心类与函数

### 4.1 后端核心类

| 类名 | 模块 | 功能描述 | 关键方法 |
|------|------|----------|----------|
| `JwtUtil` | common | JWT token生成与验证 | `generateToken()`, `parseToken()` |
| `SecurityConfig` | module-security | 安全配置 | `configure()` |
| `AuthController` | module-security | 认证相关接口 | `login()`, `register()`, `getUserInfo()` |
| `VideoController` | module-video | 视频相关接口 | `list()`, `getById()`, `upload()`, `update()`, `delete()` |
| `QaController` | module-rag | 智能问答相关接口 | `ask()`, `getHistory()` |
| `AnalyticsController` | module-analytics | 学情分析相关接口 | `getHeatmap()`, `getClassOverview()`, `getStudentProfile()`, `getWordCloud()` |
| `TeacherController` | module-teacher | 教师相关接口 | `getCourses()`, `createCourse()`, `updateCourse()`, `deleteCourse()`, `getAnalytics()` |
| `StudentController` | module-student | 学生相关接口 | `getCourses()`, `getProgress()`, `createNote()`, `getNotes()`, `getAnalytics()` |
| `AdminController` | module-admin | 管理员相关接口 | `getUsers()`, `createUser()`, `updateUser()`, `deleteUser()`, `getRoles()`, `getLogs()` |

### 4.2 前端核心函数

#### 4.2.1 API调用函数

| 函数名 | 模块 | 功能描述 | 参数 | 返回值 |
|--------|------|----------|------|--------|
| `login()` | auth.ts | 用户登录 | `LoginRequest` | `LoginResponse` |
| `register()` | auth.ts | 用户注册 | `RegisterRequest` | `LoginResponse` |
| `getUserInfo()` | auth.ts | 获取用户信息 | 无 | `UserInfoDTO` |
| `refreshToken()` | auth.ts | 刷新token | 无 | `TokenDTO` |
| `getVideoList()` | video.ts | 获取视频列表 | 无 | `VideoDTO[]` |
| `getVideoDetail()` | video.ts | 获取视频详情 | `videoId` | `VideoDTO` |
| `uploadVideo()` | video.ts | 上传视频 | `FormData` | `VideoDTO` |
| `askQuestion()` | qa.ts | 提问 | `QuestionRequest` | `AnswerResponse` |
| `getQaHistory()` | qa.ts | 获取问答历史 | 无 | `QaLogDTO[]` |
| `getHeatmapData()` | analytics.ts | 获取热力图数据 | `videoId` | `HeatmapDataDTO` |
| `getClassOverview()` | analytics.ts | 获取班级概览 | 无 | `ClassOverviewDTO` |
| `getStudentProfile()` | analytics.ts | 获取学生画像 | `userId` | `StudentProfileDTO` |
| `getWordCloudData()` | analytics.ts | 获取词云数据 | `videoId` | `WordCloudDataDTO` |
| `getStudentCourses()` | student.ts | 获取学生课程列表 | 无 | `CourseDTO[]` |
| `getLearningProgress()` | student.ts | 获取学习进度 | 无 | `ProgressDTO` |
| `createNote()` | student.ts | 创建笔记 | `NoteRequest` | `NoteDTO` |
| `getNotes()` | student.ts | 获取笔记列表 | 无 | `NoteDTO[]` |
| `getStudentAnalytics()` | student.ts | 获取个人学情分析 | 无 | `StudentAnalyticsDTO` |
| `getTeacherCourses()` | admin.ts | 获取教师课程列表 | 无 | `CourseDTO[]` |
| `createCourse()` | admin.ts | 创建课程 | `CourseRequest` | `CourseDTO` |
| `updateCourse()` | admin.ts | 更新课程 | `courseId`, `CourseRequest` | `CourseDTO` |
| `deleteCourse()` | admin.ts | 删除课程 | `courseId` | `boolean` |
| `getUsers()` | admin.ts | 获取用户列表 | 无 | `UserDTO[]` |
| `createUser()` | admin.ts | 创建用户 | `UserRequest` | `UserDTO` |
| `updateUser()` | admin.ts | 更新用户 | `userId`, `UserRequest` | `UserDTO` |
| `deleteUser()` | admin.ts | 删除用户 | `userId` | `boolean` |

#### 4.2.2 组合式函数

| 函数名 | 文件 | 功能描述 | 关键方法 |
|--------|------|----------|----------|
| `useNotes` | [useNotes.ts](file:///workspace/frontend/src/composables/useNotes.ts) | 笔记管理 | `createNote()`, `getNotes()`, `deleteNote()` |
| `useNotification` | [useNotification.ts](file:///workspace/frontend/src/composables/useNotification.ts) | 通知管理 | `addNotification()`, `removeNotification()` |
| `usePagination` | [usePagination.ts](file:///workspace/frontend/src/composables/usePagination.ts) | 分页管理 | `setPage()`, `setPageSize()` |
| `useSSEChat` | [useSSEChat.ts](file:///workspace/frontend/src/composables/useSSEChat.ts) | SSE聊天 | `sendMessage()`, `connect()` |
| `useTheme` | [useTheme.ts](file:///workspace/frontend/src/composables/useTheme.ts) | 主题管理 | `toggleTheme()`, `getTheme()` |
| `useVideoPlayer` | [useVideoPlayer.ts](file:///workspace/frontend/src/composables/useVideoPlayer.ts) | 视频播放器 | `play()`, `pause()`, `seek()`, `setVolume()` |

#### 4.2.3 工具函数

| 函数名 | 文件 | 功能描述 | 参数 | 返回值 |
|--------|------|----------|------|--------|
| `request` | [request.ts](file:///workspace/frontend/src/utils/request.ts) | HTTP请求封装 | 配置对象 | Promise |
| `formatDate` | [format.ts](file:///workspace/frontend/src/utils/format.ts) | 日期格式化 | `date`, `format` | 格式化后的日期字符串 |
| `snakeCase` | [snake-case.ts](file:///workspace/frontend/src/utils/snake-case.ts) | 驼峰转蛇形 | `str` | 蛇形字符串 |
| `storage` | [storage.ts](file:///workspace/frontend/src/utils/storage.ts) | 本地存储封装 | 无 | 存储操作对象 |

## 5. 依赖关系

### 5.1 后端依赖

| 依赖 | 版本 | 用途 | 模块 |
|------|------|------|------|
| `spring-boot-starter-web` | 3.2.0 | Web支持 | 所有模块 |
| `spring-boot-starter-security` | 3.2.0 | 安全支持 | module-security |
| `mybatis-plus-boot-starter` | 3.5.5 | ORM框架 | 所有数据访问模块 |
| `mysql-connector-java` | 8.0.35 | MySQL驱动 | 所有数据访问模块 |
| `redis-spring-boot-starter` | 3.2.0 | Redis支持 | module-security |
| `elasticsearch-rest-high-level-client` | 8.11.0 | Elasticsearch客户端 | module-rag |
| `amqp-client` | 5.18.0 | RabbitMQ客户端 | module-video |
| `jjwt-api` | 0.11.5 | JWT API | module-security |
| `jjwt-impl` | 0.11.5 | JWT实现 | module-security |
| `jjwt-jackson` | 0.11.5 | JWT Jackson支持 | module-security |
| `dashscope-sdk-java` | 2.10.0 | 阿里云DashScope SDK | module-ai-adapter |

### 5.2 前端依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| `vue` | ^3.5.32 | 前端核心框架 |
| `pinia` | ^2.1.7 | 状态管理 |
| `vue-router` | ^4.2.5 | 路由管理 |
| `axios` | ^1.6.7 | HTTP客户端 |
| `element-plus` | ^2.5.0 | UI组件库 |
| `echarts` | ^5.5.0 | 图表库 |
| `lucide-vue-next` | ^0.300.0 | 图标库 |
| `msw` | ^2.0.0 | 模拟数据 |

## 6. 项目运行方式

### 6.1 后端运行

1. **环境准备**
   - Java 17+
   - Maven 3.6+
   - Docker + Docker Compose

2. **启动基础设施**
   ```bash
   cd backend
   docker-compose up -d
   ```

3. **构建项目**
   ```bash
   mvn clean install -DskipTests
   ```

4. **启动应用**
   ```bash
   java -jar module-app/target/module-app-1.0.0.jar
   ```

### 6.2 前端运行

1. **环境准备**
   - Node.js 16+
   - pnpm 7+

2. **安装依赖**
   ```bash
   cd frontend
   pnpm install
   ```

3. **启动开发服务器**
   ```bash
   pnpm dev
   ```

4. **构建生产版本**
   ```bash
   pnpm build
   ```

5. **预览生产版本**
   ```bash
   pnpm preview
   ```

## 7. 关键配置

### 7.1 后端配置

| 配置项 | 说明 | 默认值 | 位置 |
|--------|------|--------|------|
| `spring.datasource.url` | 数据库连接URL | `jdbc:mysql://localhost:3306/zhike_db` | application.yml |
| `spring.datasource.username` | 数据库用户名 | `zhike` | application.yml |
| `spring.datasource.password` | 数据库密码 | `zhike123` | application.yml |
| `spring.redis.host` | Redis主机 | `localhost` | application.yml |
| `spring.redis.port` | Redis端口 | `6379` | application.yml |
| `spring.elasticsearch.uris` | Elasticsearch地址 | `http://localhost:9200` | application.yml |
| `spring.rabbitmq.host` | RabbitMQ主机 | `localhost` | application.yml |
| `spring.rabbitmq.port` | RabbitMQ端口 | `5672` | application.yml |
| `jwt.secret` | JWT密钥 | 随机生成 | application.yml |
| `jwt.expiration` | JWT过期时间 | `86400` (24小时) | application.yml |

### 7.2 前端配置

| 配置项 | 说明 | 默认值 | 位置 |
|--------|------|--------|------|
| `VITE_API_BASE_URL` | API基础URL | `/api` | .env.development |
| `VITE_ENABLE_MOCK` | 是否启用Mock数据 | `true` | .env.development |

## 8. 数据库设计

### 8.1 核心表结构

| 表名 | 描述 | 模块 |
|------|------|------|
| `roles` | 角色表 | 安全模块 |
| `users` | 用户表 | 安全模块 |
| `user_roles` | 用户角色关联表 | 安全模块 |
| `courses` | 课程表 | 教师模块 |
| `videos` | 视频表 | 视频模块 |
| `knowledge_fragments` | 知识片段表 | RAG模块 |
| `learning_records` | 学习记录表 | 学生模块 |
| `qa_logs` | 问答记录表 | RAG模块 |
| `notes` | 笔记表 | 学生模块 |
| `knowledge_mastery` | 知识掌握表 | 学情分析模块 |
| `operation_logs` | 操作日志表 | 管理员模块 |
| `sys_config` | 系统配置表 | 通用模块 |

### 8.2 预置数据

- **角色数据**：ADMIN/TEACHER/STUDENT
- **系统配置**：14条配置，包含embedding/rerank/heatmap权重参数

## 9. 安全设计

### 9.1 后端安全

- **JWT认证**：使用JWT进行无状态认证，token有效期24小时
- **密码加密**：使用BCrypt对密码进行加密存储
- **RBAC权限控制**：基于角色的访问控制
- **CORS配置**：允许localhost:5173跨域访问
- **SQL注入防护**：使用MyBatis-Plus参数化查询

### 9.2 前端安全

- **token存储**：使用localStorage存储token
- **请求拦截**：在请求头中添加Authorization token
- **响应拦截**：处理401、403等错误，自动跳转到登录页
- **权限控制**：基于路由元信息的权限控制

## 10. 监控与日志

### 10.1 后端监控

- **操作日志**：记录管理员操作
- **系统日志**：使用Spring Boot默认日志

### 10.2 前端监控

- **控制台日志**：开发环境下的调试日志
- **错误处理**：统一的错误处理机制

## 11. 部署与扩展

### 11.1 Docker部署

- **后端**：使用Docker Compose部署完整的基础设施
- **前端**：构建静态文件后部署到Nginx或CDN

### 11.2 扩展方案

- **水平扩展**：通过负载均衡器扩展后端服务
- **缓存优化**：增加Redis集群
- **搜索引擎优化**：增加Elasticsearch集群
- **消息队列优化**：增加RabbitMQ集群

## 12. 注意事项

1. **AI模块**：仅提供适配器接口，实际调用需集成DashScope SDK
2. **视频解析**：视频解析功能需集成FFmpeg
3. **生产环境**：生产环境部署需修改JWT密钥和数据库密码
4. **前端Mock**：开发环境下启用Mock数据，生产环境需关闭
5. **权限控制**：确保生产环境下严格的权限控制

## 13. 总结

智能教学闭环系统是一个功能完整的教育平台，采用前后端分离架构，支持视频教学、AI智能问答、学情分析等核心功能。系统模块化设计清晰，代码结构合理，便于维护和扩展。通过本Code Wiki文档，开发者可以快速了解系统的整体架构、核心模块、关键类与函数、依赖关系以及运行方式，为系统的开发、维护和扩展提供参考。