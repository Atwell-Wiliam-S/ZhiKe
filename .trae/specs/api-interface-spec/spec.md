# 智能教学闭环系统 - API接口规范实现 - 产品需求文档

## Overview
- **Summary**: 基于API接口规范文档，实现智能教学闭环系统的后端API接口，包括认证授权、视频资源、RAG智能问答、学情分析和学生管理等功能模块。
- **Purpose**: 提供标准化、规范化的API接口，支持前后端分离架构，确保系统各模块间的高效通信和数据交换。
- **Target Users**: 前端开发人员、后端开发人员、系统测试人员。

## Goals
- 实现完整的API接口体系，覆盖认证授权、视频资源、RAG智能问答、学情分析和学生管理等功能模块
- 采用测试驱动开发（TDD）方法，确保接口功能的正确性和可靠性
- 数据库操作采用注解方式，提高代码可读性和维护性
- 确保接口响应格式统一，符合规范要求
- 实现SSE流式协议，支持智能问答的实时响应

## Non-Goals (Out of Scope)
- 前端代码实现
- 第三方服务集成（如OSS、AI模型）
- 性能优化和负载测试
- 部署和运维配置

## Background & Context
- 系统采用前后端分离架构，后端基于Spring Boot 3.2.0 + Java 17
- 使用MyBatis-Plus 3.5.5进行数据库操作，要求采用注解方式
- 采用Spring Security 6.2.x + JWT进行认证授权
- 数据库使用MySQL 8.0，支持JSON字段
- 已完成项目脚手架搭建和数据库初始化

## Functional Requirements
- **FR-1**: 认证授权接口组，包括登录、注册、登出、刷新Token和获取用户信息
- **FR-2**: 视频资源接口组，包括上传凭证获取、上传完成通知、视频解析触发、解析状态查询、播放签名URL获取等
- **FR-3**: RAG智能问答接口组，包括SSE流式问答和问答历史查询
- **FR-4**: 学情分析接口组，包括难点热力图、班级学情概览、学生学习画像和高频困惑词云
- **FR-5**: 学生接口组，包括课程列表、课程详情、播放信息获取、心跳上报、进度保存和笔记管理

## Non-Functional Requirements
- **NFR-1**: 接口响应格式统一，符合规范要求
- **NFR-2**: 采用HTTPS协议，确保数据传输安全
- **NFR-3**: 支持SSE流式协议，实现实时问答响应
- **NFR-4**: 接口性能满足系统需求，响应时间合理
- **NFR-5**: 代码质量高，采用TDD方法确保功能正确性

## Constraints
- **Technical**: Spring Boot 3.2.0 + Java 17, MyBatis-Plus 3.5.5, Spring Security 6.2.x + JWT, MySQL 8.0
- **Business**: 接口规范必须严格按照API接口文档执行
- **Dependencies**: 数据库初始化脚本已完成，基础项目结构已搭建

## Assumptions
- 数据库表结构已按照设计文档创建完成
- 项目基础配置已完成，包括依赖管理、安全配置等
- 第三方服务（如OSS、AI模型）的集成接口已预留

## Acceptance Criteria

### AC-1: 认证授权接口组
- **Given**: 系统已启动，数据库已初始化
- **When**: 调用登录接口，提供正确的用户名和密码
- **Then**: 返回JWT Token和用户信息
- **Verification**: `programmatic`

### AC-2: 视频资源接口组
- **Given**: 教师用户已登录，有视频上传权限
- **When**: 调用视频上传凭证接口
- **Then**: 返回OSS签名URL和上传ID
- **Verification**: `programmatic`

### AC-3: RAG智能问答接口组
- **Given**: 学生用户已登录，当前正在观看视频
- **When**: 调用SSE流式问答接口，发送问题
- **Then**: 接收流式响应，包含文本片段、参考来源和完成状态
- **Verification**: `programmatic`

### AC-4: 学情分析接口组
- **Given**: 教师用户已登录，有权限查看学情分析
- **When**: 调用难点热力图接口，提供课程ID和视频ID
- **Then**: 返回视频的难点热力图数据
- **Verification**: `programmatic`

### AC-5: 学生接口组
- **Given**: 学生用户已登录
- **When**: 调用课程列表接口，提供分页参数
- **Then**: 返回课程列表，包含分页信息
- **Verification**: `programmatic`

## Open Questions
- [ ] 第三方服务（如OSS、AI模型）的具体集成方式需要进一步明确
- [ ] 接口性能测试的具体指标需要进一步确定
- [ ] 异常处理的具体实现细节需要进一步完善