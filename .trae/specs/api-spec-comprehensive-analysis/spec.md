# 智能教学系统 API 接口规范分析 - 产品需求文档

## 概述
- **Summary**: 根据 API 接口规范，对智能教学系统的现有后端代码进行全面梳理和分析，识别已实现功能和缺失功能，确保系统符合接口规范要求。
- **Purpose**: 确保后端代码与 API 接口规范完全一致，为后续的功能完善和系统集成提供清晰的指导。
- **Target Users**: 后端开发人员、系统架构师、测试人员。

## Goals
- 全面梳理现有后端代码，对照 API 接口规范进行分析
- 识别已实现的接口和功能
- 识别缺失的接口和功能
- 评估现有代码的合规性和质量
- 提供详细的实现建议和改进方案

## Non-Goals (Out of Scope)
- 不进行代码重构或功能实现
- 不修改现有代码结构
- 不处理前端代码或集成
- 不进行性能优化或安全性评估

## Background & Context
- **系统架构**: Spring Boot 3.2.0 后端 + Vue 3 + TypeScript 前端
- **认证方式**: JWT + RBAC 权限控制
- **数据存储**: MySQL 8.0.35 + Redis 缓存 + Elasticsearch 向量检索
- **消息队列**: RabbitMQ 用于异步任务
- **文件存储**: OSS 用于视频存储
- **API 规范**: RESTful API 设计，统一响应格式

## Functional Requirements
- **FR-1**: 认证授权模块 - 登录、注册、登出、刷新Token、获取用户信息
- **FR-2**: 视频资源模块 - 上传签名、上传完成、视频解析、状态查询、签名URL生成、视频管理、知识片段管理
- **FR-3**: RAG 智能问答模块 - 流式问答、问答历史
- **FR-4**: 学情分析模块 - 难点热力图、班级学情概览、学生学习画像、高频困惑词云
- **FR-5**: 学生模块 - 课程列表、课程详情、播放信息、心跳上报、进度保存、笔记管理
- **FR-6**: 教师模块 - 视频管理、知识片段管理
- **FR-7**: 管理员模块 - 系统管理功能

## Non-Functional Requirements
- **NFR-1**: 统一响应格式 - 所有接口返回标准的响应结构
- **NFR-2**: 权限控制 - 基于角色的访问控制
- **NFR-3**: 分页规范 - 统一的分页参数和响应格式
- **NFR-4**: 错误处理 - 统一的错误码和错误响应格式
- **NFR-5**: 代码质量 - 符合 Java 编码规范，结构清晰

## Constraints
- **Technical**: Spring Boot 3.2.0, Java 17+, MySQL 8.0.35
- **Business**: 必须严格遵循 API 接口规范
- **Dependencies**: 现有项目结构和依赖关系

## Assumptions
- 现有代码结构基本符合模块划分
- 数据库表结构已按照设计文档创建
- 基础配置（如 JWT、CORS、Jackson 等）已正确配置

## Acceptance Criteria

### AC-1: 认证授权模块分析完成
- **Given**: 现有 AuthController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-2: 视频资源模块分析完成
- **Given**: 现有 VideoController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-3: RAG 智能问答模块分析完成
- **Given**: 现有 QAController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-4: 学情分析模块分析完成
- **Given**: 现有 AnalyticsController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-5: 学生模块分析完成
- **Given**: 现有 StudentController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-6: 教师模块分析完成
- **Given**: 现有 TeacherController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-7: 管理员模块分析完成
- **Given**: 现有 AdminController 和相关代码
- **When**: 对照 API 接口规范进行分析
- **Then**: 识别已实现和缺失的接口，评估实现质量
- **Verification**: `human-judgment`

### AC-8: 分析报告生成完成
- **Given**: 所有模块的分析结果
- **When**: 汇总分析结果
- **Then**: 生成详细的分析报告，包括已实现功能、缺失功能和改进建议
- **Verification**: `human-judgment`

## Open Questions
- [ ] 数据库表结构是否与 API 接口规范完全匹配？
- [ ] 现有代码是否完全遵循统一响应格式？
- [ ] 权限控制是否正确实现？
- [ ] 错误处理是否符合规范？
- [ ] 分页功能是否正确实现？