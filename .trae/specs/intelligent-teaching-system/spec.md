# 基于多模态大模型的智能教学闭环系统 - 产品需求文档

## Overview
- **Summary**: 构建一个基于Vue 3和Spring Boot的智能教学闭环系统，集成多模态大模型（阿里云通义千问），实现视频学习、智能问答、学情分析等功能，支持学生、教师、管理员三种角色。
- **Purpose**: 解决传统教学中个性化学习不足、教师工作量大、学情分析困难等问题，通过AI技术提升教学效果和学习体验。
- **Target Users**: 学生、教师、系统管理员

## Goals
- 实现完整的用户认证与授权系统（JWT + RBAC）
- 开发视频管理与知识片段解析功能
- 构建基于RAG的智能问答系统（SSE流式响应）
- 实现学情分析与可视化功能
- 提供多角色、多主题的前端界面
- 确保前后端数据一致性和代码风格统一

## Non-Goals (Out of Scope)
- 不涉及硬件设备集成
- 不支持实时视频会议功能
- 不开发移动端原生应用（仅支持响应式Web）
- 不包含支付系统

## Background & Context
- 这是一个毕业设计重构项目，技术栈为Vue 3 + Spring Boot 3 + MySQL 8 + Redis + Elasticsearch
- AI模型接入阿里云通义千问 (Qwen)
- 采用前后端分离架构，模块化设计
- 前端使用TypeScript确保类型安全，后端使用MyBatis-Plus简化数据库操作

## Functional Requirements
- **FR-1**: 用户认证与授权
  - 支持登录、注册、登出功能
  - 基于JWT的无状态认证
  - RBAC角色权限控制（学生、教师、管理员）

- **FR-2**: 视频管理
  - 教师上传视频
  - 视频异步解析与知识片段提取
  - 视频播放与断点续播

- **FR-3**: 智能问答
  - 基于RAG的上下文问答
  - SSE流式响应
  - 答案溯源与引用

- **FR-4**: 学情分析
  - 学习进度跟踪
  - 难点热力图
  - 班级学情概览
  - 学生学习画像

- **FR-5**: 笔记系统
  - 时间点关联的学习笔记
  - 公开/私有笔记管理

- **FR-6**: 多角色界面
  - 学生端：视频学习、AI助教、笔记
  - 教师端：课程管理、知识片段编辑、学情分析
  - 管理员端：用户管理、系统配置

## Non-Functional Requirements
- **NFR-1**: 性能
  - 页面加载时间 < 2秒
  - SSE响应延迟 < 500ms
  - 数据库查询响应时间 < 200ms

- **NFR-2**: 安全性
  - JWT token加密传输
  - 密码BCrypt加密存储
  - 请求频率限制
  - SQL注入防护

- **NFR-3**: 可扩展性
  - 模块化架构
  - 支持水平扩展
  - 插件化AI模型接入

- **NFR-4**: 可用性
  - 系统 uptime > 99.9%
  - 故障自动恢复
  - 数据备份与恢复机制

- **NFR-5**: 可维护性
  - 代码风格统一
  - 完整的文档
  - 单元测试覆盖率 > 80%

## Constraints
- **Technical**: 
  - Vue 3 + TypeScript + Pinia + Element Plus
  - Spring Boot 3 + Java 17 + MySQL 8 + Redis + Elasticsearch
  - 阿里云通义千问SDK
  - 前后端字段名严格遵循SnakeCase/CamelCase转换规则

- **Business**: 
  - 毕业设计项目，时间有限
  - 预算有限，优先使用开源技术

- **Dependencies**: 
  - 阿里云DashScope SDK
  - 阿里云OSS
  - RabbitMQ
  - FFmpeg

## Assumptions
- 系统运行环境为Linux服务器
- 网络连接稳定，可访问阿里云服务
- 数据库和缓存服务已正确配置
- 开发团队熟悉Vue 3和Spring Boot技术栈

## Acceptance Criteria

### AC-1: 用户认证功能
- **Given**: 用户打开系统登录页
- **When**: 输入正确的用户名和密码
- **Then**: 系统返回JWT token并跳转到对应角色的首页
- **Verification**: `programmatic`
- **Notes**: 支持三种角色的登录

### AC-2: 视频上传与解析
- **Given**: 教师登录系统
- **When**: 上传视频并触发解析
- **Then**: 系统异步处理视频，生成知识片段
- **Verification**: `programmatic`
- **Notes**: 解析状态应实时更新

### AC-3: 智能问答功能
- **Given**: 学生在学习页面
- **When**: 提交问题
- **Then**: 系统返回流式回答，包含知识来源引用
- **Verification**: `programmatic`
- **Notes**: 回答应基于视频内容

### AC-4: 学情分析功能
- **Given**: 教师或管理员查看学情分析页面
- **When**: 选择课程和时间范围
- **Then**: 系统展示热力图、班级概览等分析数据
- **Verification**: `human-judgment`
- **Notes**: 数据应准确反映学习情况

### AC-5: 多角色界面
- **Given**: 不同角色用户登录系统
- **When**: 访问系统功能
- **Then**: 系统展示对应角色的界面和功能
- **Verification**: `human-judgment`
- **Notes**: 界面应符合设计文档中的视觉规范

### AC-6: 前后端数据一致性
- **Given**: 系统运行中
- **When**: 前后端进行数据交互
- **Then**: 数据字段名和类型完全一致
- **Verification**: `programmatic`
- **Notes**: 遵循SnakeCase/CamelCase转换规则

## Open Questions
- [ ] 阿里云通义千问的具体API调用细节
- [ ] 视频解析的具体实现方案
- [ ] Elasticsearch向量检索的配置参数
- [ ] 系统部署的具体环境要求
- [ ] 数据迁移方案（如果需要）