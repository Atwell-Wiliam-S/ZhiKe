# 基于多模态大模型的智能教学闭环系统 - 产品需求文档

## Overview
- **Summary**: 基于多模态大模型的智能教学闭环系统，采用前后端分离架构，集成阿里云通义千问 (Qwen) AI 模型，提供视频学习、智能问答、学情分析等核心功能，支持学生、教师、管理员三种角色。
- **Purpose**: 解决传统教学中知识传递效率低、个性化学习不足、学情分析困难等问题，通过 AI 技术提升教学效果和学习体验。
- **Target Users**: 学生、教师、管理员

## Goals
- 构建完整的智能教学闭环系统，包括视频学习、AI 问答、学情分析等核心功能
- 实现多角色权限管理，支持学生、教师、管理员三种角色的差异化功能
- 集成阿里云通义千问 (Qwen) AI 模型，实现智能问答和视频内容分析
- 提供响应式前端界面，支持多设备访问
- 实现数据可视化，包括学情热力图、学习概览等
- 确保系统安全性和性能稳定性

## Non-Goals (Out of Scope)
- 不支持实时视频直播功能
- 不集成第三方支付系统
- 不支持多语言国际化
- 不包含移动端原生应用开发
- 不实现复杂的课程编排系统

## Background & Context
- 这是一个毕业设计重构项目，技术栈为 Vue 3 (Vite) + TypeScript + Pinia + Element Plus | Spring Boot 3 + Java 17 + MySQL 8 + Redis + Elasticsearch
- AI 模型接入：阿里云通义千问 (Qwen)
- 开发模式：功能垂直开发，同时生成前后端代码

## Functional Requirements
- **FR-1**: 用户认证与授权
  - 支持学生、教师、管理员三种角色的登录和注册
  - 基于 JWT + RBAC 的权限管理
  - 支持 Token 刷新和登出功能

- **FR-2**: 视频管理
  - 教师上传视频到阿里云 OSS
  - 视频异步解析（提取关键帧、ASR 语音转写、视觉分析）
  - 视频播放与断点续播
  - 知识片段管理

- **FR-3**: RAG 智能问答
  - 基于视频内容的智能问答
  - SSE 流式响应，支持逐字渲染
  - 问答历史记录
  - 引用溯源高亮

- **FR-4**: 学情分析
  - 难点热力图生成
  - 班级学习概览
  - 学生学习画像
  - 高频困惑词云

- **FR-5**: 学生学习
  - 课程列表与详情
  - 学习进度记录
  - 视频观看心跳上报
  - 学习笔记管理

- **FR-6**: 教师管理
  - 课程管理
  - 视频管理
  - 知识片段管理
  - 学情查看

- **FR-7**: 管理员功能
  - 用户管理
  - 角色权限管理
  - 系统配置管理
  - 操作日志审计

- **FR-8**: AI 适配器
  - 封装阿里云 DashScope SDK 调用
  - 支持 Qwen-VL 视觉分析
  - 支持 ASR 语音转写
  - 支持 Embedding 向量化

## Non-Functional Requirements
- **NFR-1**: 性能
  - 视频播放流畅，支持 1080p 高清视频
  - AI 问答响应时间 < 3 秒
  - 页面加载时间 < 2 秒

- **NFR-2**: 安全
  - 密码采用 BCrypt 加密存储
  - JWT Token 验证与黑名单机制
  - 角色权限严格控制
  - 操作日志审计

- **NFR-3**: 可扩展性
  - 模块化架构，支持功能扩展
  - 支持水平扩展部署
  - 数据存储可扩展

- **NFR-4**: 可用性
  - 系统可用性 > 99.9%
  - 数据备份与恢复机制
  - 错误处理与容错机制

- **NFR-5**: 用户体验
  - 响应式设计，支持多设备
  - 流畅的动画效果
  - 清晰的视觉层次
  - 友好的错误提示

## Constraints
- **Technical**: Spring Boot 3 + Java 17, Vue 3 + TypeScript, MySQL 8, Redis, Elasticsearch, RabbitMQ, 阿里云 OSS, 阿里云 DashScope SDK
- **Business**: 毕业设计项目，时间有限
- **Dependencies**: 阿里云云服务，外部 AI 模型

## Assumptions
- 阿里云服务已配置并可用
- 开发环境已搭建完成
- 数据库设计已确定
- 前端设计规范已明确

## Acceptance Criteria

### AC-1: 用户认证功能
- **Given**: 用户输入正确的用户名和密码
- **When**: 点击登录按钮
- **Then**: 系统返回 JWT Token 和用户信息
- **Verification**: `programmatic`

### AC-2: 视频上传与解析
- **Given**: 教师上传视频文件
- **When**: 系统接收到视频文件
- **Then**: 视频被存储到 OSS，状态更新为解析中，异步任务开始解析
- **Verification**: `programmatic`

### AC-3: RAG 智能问答
- **Given**: 学生在视频学习页输入问题
- **When**: 点击发送按钮
- **Then**: 系统通过 SSE 流式返回 AI 回答，并显示引用溯源
- **Verification**: `programmatic`

### AC-4: 学情热力图
- **Given**: 教师进入学情分析页面
- **When**: 选择课程和视频
- **Then**: 系统生成并显示难点热力图
- **Verification**: `human-judgment`

### AC-5: 学习进度记录
- **Given**: 学生观看视频
- **When**: 学生暂停或结束观看
- **Then**: 系统自动保存学习进度
- **Verification**: `programmatic`

### AC-6: 多角色权限控制
- **Given**: 不同角色用户登录系统
- **When**: 访问系统功能
- **Then**: 系统根据角色权限控制访问
- **Verification**: `programmatic`

### AC-7: 前端主题切换
- **Given**: 学生登录系统
- **When**: 切换主题按钮
- **Then**: 系统在暗色和亮色主题之间切换
- **Verification**: `human-judgment`

## Open Questions
- [ ] 阿里云 OSS 的具体配置和费用
- [ ] 阿里云 DashScope SDK 的具体使用方式和费用
- [ ] 系统部署环境的具体配置
- [ ] 测试数据的准备和管理
- [ ] 系统性能测试的具体方案