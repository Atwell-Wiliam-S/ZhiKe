# 通用 Composables 与状态管理 - Product Requirement Document

## Overview
- **Summary**: 根据 06c-通用Composables与状态管理.md 文档实现三个通用 Composables（useNotes、usePagination、useNotification）、Pinia Store（useUserStore）、路由配置与守卫、Axios 实例配置
- **Purpose**: 为智能教学系统提供笔记管理、通用分页、通知中心、用户认证状态管理、路由管理等核心功能
- **Target Users**: 前端开发团队、学生用户、教师用户、管理员用户

## Goals
- 实现 useNotes - 视频学习过程中的笔记 CRUD 操作
- 实现 usePagination - 通用分页状态和请求逻辑
- 实现 useNotification - 通知中心管理
- 实现 useUserStore - 用户认证状态管理
- 实现路由配置与守卫
- 更新 Axios 实例配置
- 确保所有功能类型安全且符合设计规范

## Non-Goals (Out of Scope)
- 业务组件开发
- 页面视图实现
- Mock 数据配置

## Background & Context
- 06a-项目结构与类型定义已完成
- 06b-核心Composables已完成
- 项目目录结构已搭建
- TypeScript 类型定义已完善

## Functional Requirements
- **FR-1**: useNotes 支持笔记的获取、创建、更新、删除、切换公开/私有
- **FR-2**: usePagination 封装分页状态和请求逻辑，支持自动加载、页码切换、每页条数切换
- **FR-3**: useNotification 管理通知列表、未读计数、分类筛选
- **FR-4**: useUserStore 管理用户认证状态、登录、登出、角色权限
- **FR-5**: 路由配置包含学生、教师、管理员的路由，支持权限守卫
- **FR-6**: Axios 实例配置包含请求和响应拦截器，支持 token 管理和错误处理

## Non-Functional Requirements
- **NFR-1**: Composables 代码结构清晰，符合 Vue 3 最佳实践
- **NFR-2**: 类型安全，TypeScript 编译无错误
- **NFR-3**: 代码可维护性高，注释完整
- **NFR-4**: 路由配置合理，权限控制严格

## Constraints
- **Technical**: Vue 3.4+, TypeScript 5.3+, Pinia 2.1+, Vue Router 4.2+
- **Business**: 必须完全按照 06c 文档中的代码实现
- **Dependencies**: 已有类型定义和工具函数

## Assumptions
- 后端 API 接口符合设计规范
- localStorage 可用
- 浏览器支持 Fetch API

## Acceptance Criteria

### AC-1: useNotes 实现完整
- **Given**: useNotes 已实现
- **When**: 查看 composables/useNotes.ts
- **Then**: 包含所有 06c 文档中的功能，包括 fetchNotes、createNote、updateNote、deleteNote、togglePublic
- **Verification**: `programmatic`

### AC-2: usePagination 实现完整
- **Given**: usePagination 已实现
- **When**: 查看 composables/usePagination.ts
- **Then**: 包含所有 06c 文档中的功能，包括 loadData、changePage、changePageSize、refresh
- **Verification**: `programmatic`

### AC-3: useNotification 实现完整
- **Given**: useNotification 已实现
- **When**: 查看 composables/useNotification.ts
- **Then**: 包含所有 06c 文档中的功能，包括未读计数、分类筛选、标记已读
- **Verification**: `programmatic`

### AC-4: useUserStore 实现完整
- **Given**: useUserStore 已实现
- **When**: 查看 stores/user.ts
- **Then**: 包含所有 06c 文档中的功能，包括登录、登出、用户信息管理
- **Verification**: `programmatic`

### AC-5: 路由配置与守卫实现完整
- **Given**: 路由配置已实现
- **When**: 查看 router/routes.ts 和 router/guards.ts
- **Then**: 包含完整的路由配置和守卫逻辑
- **Verification**: `programmatic`

### AC-6: Axios 实例配置实现完整
- **Given**: Axios 配置已实现
- **When**: 查看 utils/request.ts
- **Then**: 包含完整的请求和响应拦截器
- **Verification**: `programmatic`

### AC-7: TypeScript 编译无错误
- **Given**: 所有功能已实现
- **When**: 运行 TypeScript 编译检查
- **Then**: 无类型错误
- **Verification**: `programmatic`

## Open Questions
- 无
