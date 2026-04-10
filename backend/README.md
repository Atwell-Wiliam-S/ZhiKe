# 智能教学闭环系统

## 项目概述
智能教学闭环系统是一个基于Spring Boot的后端服务，支持视频教学、AI智能问答、学情分析等核心功能，采用前后端分离的模块化单体架构。

## 技术栈
- **后端框架**: Spring Boot 3.2.0 + Java 17
- **ORM框架**: MyBatis-Plus 3.5.5
- **安全框架**: Spring Security 6.2.x + JWT
- **数据库**: MySQL 8.0 + Redis 7.0 + Elasticsearch 8.x + RabbitMQ 3.12
- **构建工具**: Maven

## 项目结构

```
com.zhike/
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

## 快速开始

### 1. 环境准备
- Java 17+
- Maven 3.6+
- Docker + Docker Compose

### 2. 启动基础设施
```bash
docker-compose up -d
```

### 3. 数据库初始化
数据库会在Docker容器启动时自动执行初始化脚本 `schema.sql`，创建12张表并插入预置数据。

### 4. 构建项目
```bash
mvn clean install -DskipTests
```

### 5. 启动应用
```bash
java -jar module-app/target/module-app-1.0.0.jar
```

## API 端点

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 视频相关
- `GET /api/video/list` - 获取视频列表
- `GET /api/video/{id}` - 获取视频详情
- `POST /api/video/upload` - 上传视频
- `PUT /api/video/{id}` - 更新视频
- `DELETE /api/video/{id}` - 删除视频

### 智能问答相关
- `POST /api/qa/ask` - 提问
- `GET /api/qa/history` - 获取问答历史

### 学情分析相关
- `GET /api/analytics/heatmap/{videoId}` - 获取热力图数据
- `GET /api/analytics/classOverview` - 获取班级概览
- `GET /api/analytics/studentProfile/{userId}` - 获取学生画像
- `GET /api/analytics/wordCloud/{videoId}` - 获取词云数据

### 教师相关
- `GET /api/teacher/courses` - 获取课程列表
- `POST /api/teacher/courses` - 创建课程
- `PUT /api/teacher/courses/{id}` - 更新课程
- `DELETE /api/teacher/courses/{id}` - 删除课程
- `GET /api/teacher/analytics` - 获取学情分析

### 学生相关
- `GET /api/student/courses` - 获取课程列表
- `GET /api/student/progress` - 获取学习进度
- `POST /api/student/notes` - 创建笔记
- `GET /api/student/notes` - 获取笔记列表
- `GET /api/student/analytics` - 获取个人学情分析

### 管理员相关
- `GET /api/admin/users` - 获取用户列表
- `POST /api/admin/users` - 创建用户
- `PUT /api/admin/users/{id}` - 更新用户
- `DELETE /api/admin/users/{id}` - 删除用户
- `GET /api/admin/roles` - 获取角色列表
- `GET /api/admin/logs` - 获取操作日志

## 数据库设计

### 核心表结构
- **用户域**: roles, users, user_roles
- **内容域**: courses, videos, knowledge_fragments
- **学习域**: learning_records, qa_logs, notes, knowledge_mastery
- **系统域**: operation_logs, sys_config

### 预置数据
- 3个角色：ADMIN/TEACHER/STUDENT
- 14条系统配置：包含embedding/rerank/heatmap权重参数

## 部署配置

### Docker Compose
项目提供了完整的Docker Compose配置，包含以下服务：
- MySQL 8.0.35
- Redis 7.0
- Elasticsearch 8.11.0
- RabbitMQ 3.12-management

### 环境配置
- **开发环境**: application.yml (dev profile)
- **生产环境**: application.yml (prod profile)

## 安全设计
- JWT token 24小时过期
- 密码BCrypt加密
- RBAC权限控制
- CORS配置（允许localhost:5173）
- SQL注入防护（MyBatis-Plus参数化查询）

## 注意事项
- 本项目为后端服务，前端需单独开发
- AI模块仅提供适配器接口，实际调用需集成DashScope SDK
- 视频解析功能需集成FFmpeg
- 生产环境部署需修改JWT密钥和数据库密码

## 许可证
MIT License