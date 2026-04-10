# 智能教学闭环系统 - 产品需求文档

## Overview
- **Summary**: 构建一个基于Spring Boot的智能教学闭环系统，支持视频教学、AI智能问答、学情分析等核心功能，采用前后端分离的模块化单体架构。
- **Purpose**: 解决传统教学中知识难以量化、学习进度难以追踪、个性化学习需求难以满足的问题，通过AI技术提升教学效果和学习体验。
- **Target Users**: 学生、教师、系统管理员

## Goals
- 构建完整的Spring Boot后端项目脚手架，包含8个功能模块
- 实现JWT认证、RBAC权限控制、Redis缓存等核心功能
- 搭建MySQL、Redis、Elasticsearch、RabbitMQ等基础设施
- 提供完整的数据库初始化脚本，包含12张表的DDL和预置数据
- 支持Docker容器化部署

## Non-Goals (Out of Scope)
- 前端实现（仅提供后端API）
- 实际的AI模型调用（仅提供适配器接口）
- 生产环境部署（仅提供部署配置模板）
- 视频解析的具体实现（仅提供消息队列配置）

## Background & Context
- 系统采用模块化单体架构，便于后续拆分为微服务
- 技术栈：Spring Boot 3.2.0 + Java 17 + MyBatis-Plus 3.5.5 + Spring Security 6.2.x
- 数据存储：MySQL 8.0 + Redis 7.0 + Elasticsearch 8.x + RabbitMQ 3.12
- 项目结构遵循严格的包路径隔离，模块间通过接口通信

## Functional Requirements
- **FR-1**: 项目脚手架搭建
  - Maven多模块结构，包含9个模块（common + 8个业务模块）
  - 完整的依赖管理和构建配置
  - 多环境配置（dev/prod）

- **FR-2**: 安全模块实现
  - JWT认证机制
  - RBAC权限控制
  - 统一的认证过滤器
  - Token管理（Redis存储）

- **FR-3**: 数据库初始化
  - 12张表的DDL脚本
  - 预置角色数据（ADMIN/TEACHER/STUDENT）
  - 预置系统配置数据
  - 完整的索引策略

- **FR-4**: 模块化架构实现
  - 严格的包路径隔离
  - 模块间接口通信机制
  - 统一的响应格式（Result<T>）
  - 全局异常处理

- **FR-5**: 基础设施配置
  - Docker Compose配置（MySQL+Redis+ES+RabbitMQ）
  - 应用配置（application.yml）
  - CORS配置

## Non-Functional Requirements
- **NFR-1**: 性能
  - API响应时间 < 200ms (P95)
  - 并发用户数 ≥ 500
  - 视频解析任务异步处理

- **NFR-2**: 安全
  - JWT token 24小时过期
  - 密码BCrypt加密
  - SQL注入防护（MyBatis-Plus参数化查询）
  - XSS防护
  - CORS配置

- **NFR-3**: 可用性
  - 系统可用性 ≥ 99.5%
  - 数据备份机制
  - 故障恢复 < 5分钟

- **NFR-4**: 可维护性
  - 代码结构清晰，遵循模块化设计
  - 统一的异常处理和日志记录
  - 完整的文档

## Constraints
- **Technical**: 
  - Java 17
  - Spring Boot 3.2.0
  - MyBatis-Plus 3.5.5
  - MySQL 8.0
  - Redis 7.0
  - Elasticsearch 8.x
  - RabbitMQ 3.12

- **Business**: 
  - 团队规模：3-5人
  - 部署环境：Docker容器
  - 前端域名：localhost:5173

- **Dependencies**: 
  - DashScope SDK（AI服务）
  - Aliyun OSS（对象存储）

## Assumptions
- 前端已使用Vue 3 + TypeScript + Vite开发
- 开发环境已安装Docker和Docker Compose
- 网络环境可访问DashScope API
- 系统数据量初期较小，无需大规模分布式部署

## Acceptance Criteria

### AC-1: 项目脚手架搭建完成
- **Given**: 开发环境已准备就绪
- **When**: 执行Maven构建命令
- **Then**: 项目成功构建，无编译错误
- **Verification**: `programmatic`

### AC-2: 安全模块功能验证
- **Given**: 系统已启动
- **When**: 调用认证API
- **Then**: 成功生成JWT token，权限控制生效
- **Verification**: `programmatic`

### AC-3: 数据库初始化成功
- **Given**: MySQL服务已启动
- **When**: 执行初始化脚本
- **Then**: 12张表创建成功，预置数据插入完成
- **Verification**: `programmatic`

### AC-4: 模块化架构验证
- **Given**: 项目已构建
- **When**: 检查模块间依赖关系
- **Then**: 无循环依赖，模块间通过接口通信
- **Verification**: `human-judgment`

### AC-5: 基础设施配置验证
- **Given**: Docker环境已准备
- **When**: 启动Docker Compose
- **Then**: 所有服务（MySQL、Redis、ES、RabbitMQ）正常运行
- **Verification**: `programmatic`

## Open Questions
- [ ] DashScope SDK的具体版本和配置参数
- [ ] Aliyun OSS的访问凭证管理
- [ ] 生产环境的具体部署方案
- [ ] 视频解析的具体实现细节
- [ ] 学情分析的算法实现细节