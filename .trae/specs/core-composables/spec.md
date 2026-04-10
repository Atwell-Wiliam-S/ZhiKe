# 核心 Composables 实现 - Product Requirement Document

## Overview
- **Summary**: 根据 06b-核心Composables.md 文档实现三个核心业务 Composables：useSSEChat、useTheme、useVideoPlayer
- **Purpose**: 为智能教学系统提供 SSE 流式问答、多角色主题切换、视频播放器控制等核心功能
- **Target Users**: 前端开发团队、学生用户、教师用户

## Goals
- 实现 useSSEChat - 处理 RAG 问答的 SSE 流式响应
- 实现 useTheme - 多角色主题切换
- 实现 useVideoPlayer - 视频播放器控制
- 确保所有 Composables 类型安全且符合设计规范

## Non-Goals (Out of Scope)
- 业务组件开发
- 页面视图实现
- Mock 数据配置

## Background & Context
- 06a-项目结构与类型定义已完成
- 项目目录结构已搭建
- TypeScript 类型定义已完善

## Functional Requirements
- **FR-1**: useSSEChat 支持 SSE 流式问答、逐字输出、引用溯源、错误重试
- **FR-2**: useTheme 支持根据用户角色自动切换主题，学生端支持暗色/亮色手动切换
- **FR-3**: useVideoPlayer 封装视频播放、断点续播、心跳上报、知识点联动

## Non-Functional Requirements
- **NFR-1**: Composables 代码结构清晰，符合 Vue 3 最佳实践
- **NFR-2**: 类型安全，TypeScript 编译无错误
- **NFR-3**: 代码可维护性高，注释完整

## Constraints
- **Technical**: Vue 3.4+, TypeScript 5.3+
- **Business**: 必须完全按照 06b 文档中的代码实现
- **Dependencies**: 已有类型定义和工具函数

## Assumptions
- 后端 API 接口符合设计规范
- localStorage 可用
- 浏览器支持 AbortController 和 Fetch API

## Acceptance Criteria

### AC-1: useSSEChat 实现完整
- **Given**: useSSEChat 已实现
- **When**: 查看 composables/useSSEChat.ts
- **Then**: 包含所有 06b 文档中的功能，包括 sendMessage、abortStream、clearMessages
- **Verification**: `programmatic`

### AC-2: useTheme 实现完整
- **Given**: useTheme 已实现
- **When**: 查看 composables/useTheme.ts
- **Then**: 包含 initTheme、toggleTheme、isDark 等功能
- **Verification**: `programmatic`

### AC-3: useVideoPlayer 实现完整
- **Given**: useVideoPlayer 已实现
- **When**: 查看 composables/useVideoPlayer.ts
- **Then**: 包含播放控制、断点续播、心跳上报、知识点联动功能
- **Verification**: `programmatic`

### AC-4: TypeScript 编译无错误
- **Given**: 所有 Composables 已实现
- **When**: 运行 TypeScript 编译检查
- **Then**: 无类型错误
- **Verification**: `programmatic`

## Open Questions
- 无
