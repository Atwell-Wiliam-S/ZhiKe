# 组件逻辑与Mock开发 - 产品需求文档

## Overview
- **Summary**: 根据组件逻辑与Mock开发文档，实现前端核心功能，包括SSE流式问答、视频播放器控制、笔记管理、主题切换、状态管理、路由守卫和完整的Mock数据系统。
- **Purpose**: 构建一个功能完整、架构清晰的前端应用，支持学生、教师、管理员多角色，实现前后端并行开发。
- **Target Users**: 学生、教师、管理员

## Goals
- 实现核心Composables：useSSEChat、useTheme、useVideoPlayer、useNotes、usePagination、useNotification
- 实现状态管理：用户认证Store、主题Store、通知Store
- 实现路由配置与守卫：多角色路由权限控制
- 实现API请求层：Axios实例与拦截器
- 实现完整的Mock数据系统：MSW拦截器、多模块Handler、类型安全
- 确保代码符合Web设计规范：响应式、可访问性、性能优化

## Non-Goals (Out of Scope)
- 实际后端API实现
- 生产环境部署
- 第三方服务集成
- 高级数据分析功能

## Background & Context
- 基于Vue 3 + TypeScript + Vite技术栈
- 设计系统已在之前的实现中完成
- API接口规范已在03-API接口规范.md中定义
- 组件逻辑与Mock开发文档提供了完整的实现代码

## Functional Requirements
- **FR-1**: SSE流式问答功能，支持逐字输出、引用溯源、错误处理
- **FR-2**: 视频播放器控制，支持播放/暂停、断点续播、心跳上报、知识点联动
- **FR-3**: 笔记管理，支持CRUD操作、公开/私有切换
- **FR-4**: 多角色主题切换，学生端支持暗色/亮色切换，教师端固定亮色
- **FR-5**: 状态管理，实现用户认证、主题、通知的状态管理
- **FR-6**: 路由守卫，实现多角色权限控制
- **FR-7**: API请求层，实现Axios实例与拦截器，支持token管理、错误处理
- **FR-8**: Mock数据系统，实现MSW拦截器，支持多模块Mock数据

## Non-Functional Requirements
- **NFR-1**: 性能要求：SSE流式响应延迟<300ms，页面加载时间<2s
- **NFR-2**: 可访问性：符合WCAG 2.1 AA标准
- **NFR-3**: 响应式设计：适配桌面、平板、移动设备
- **NFR-4**: 类型安全：TypeScript严格模式，无类型错误
- **NFR-5**: 可维护性：代码结构清晰，模块化设计

## Constraints
- **Technical**: Vue 3.4+, TypeScript 5.3+, Vite 5.0+, Pinia 2.1+, Vue Router 4.2+, Axios 1.6+, Element Plus 2.5+, Lucide Vue Next 0.300+, ECharts 5.5+, MSW 2.0+
- **Dependencies**: 依赖已在设计系统实现中安装
- **Architecture**: 遵循文档中定义的项目结构和架构

## Assumptions
- 前端项目已初始化，依赖已安装
- 设计系统已实现，CSS变量和基础组件已就绪
- API接口规范已确定，DTO类型已定义

## Acceptance Criteria

### AC-1: SSE流式问答功能
- **Given**: 用户在视频学习页面提出问题
- **When**: 系统发送SSE请求并接收流式响应
- **Then**: 页面逐字显示AI回答，包含引用溯源，支持中断和错误处理
- **Verification**: `programmatic`

### AC-2: 视频播放器控制
- **Given**: 用户在视频学习页面
- **When**: 用户操作播放器（播放/暂停/跳转）
- **Then**: 播放器响应操作，自动上报学习进度，支持断点续播
- **Verification**: `programmatic`

### AC-3: 笔记管理
- **Given**: 用户在视频学习页面
- **When**: 用户创建/编辑/删除笔记
- **Then**: 笔记操作成功，实时更新列表，支持公开/私有切换
- **Verification**: `programmatic`

### AC-4: 多角色主题切换
- **Given**: 不同角色用户登录系统
- **When**: 学生切换主题，教师/管理员访问系统
- **Then**: 学生端支持暗色/亮色切换，教师/管理员端固定亮色主题
- **Verification**: `human-judgment`

### AC-5: 状态管理与路由守卫
- **Given**: 用户登录系统
- **When**: 用户访问不同角色的路由
- **Then**: 路由守卫根据角色权限控制访问，状态管理正确维护用户信息
- **Verification**: `programmatic`

### AC-6: API请求层与Mock数据
- **Given**: 开发环境启用Mock
- **When**: 前端发起API请求
- **Then**: MSW拦截请求并返回Mock数据，类型安全，支持多模块Mock
- **Verification**: `programmatic`

## Open Questions
- [ ] 是否需要实现额外的组件测试？
- [ ] Mock数据是否需要与真实后端数据结构完全一致？