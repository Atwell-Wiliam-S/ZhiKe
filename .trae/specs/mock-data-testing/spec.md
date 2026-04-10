# Mock 数据与测试规范 - Product Requirement Document

## Overview
- **Summary**: 根据 06d-Mock数据与测试规范.md 文档实现完整的 MSW Mock 数据系统，包括 Mock 数据源、Mock Handler、MSW 配置启动、环境变量配置，以及单元测试框架配置
- **Purpose**: 为智能教学系统提供完整的 Mock 数据支持，实现前后端并行开发，支持 SSE 流式问答、多角色登录等核心功能
- **Target Users**: 前端开发团队、测试团队

## Goals
- 创建完整的 Mock 数据源文件（auth.data.ts、qa.data.ts 等）
- 实现完整的 Mock Handler 文件（auth.handler.ts、qa.handler.ts 等）
- 配置 MSW 并集成到 main.ts
- 配置环境变量开关控制
- 确保所有 Mock 数据类型安全且符合 DTO 定义

## Non-Goals (Out of Scope)
- 业务组件开发
- 页面视图实现
- 实际后端 API 开发

## Background & Context
- 06a-项目结构与类型定义已完成
- 06b-核心Composables已完成
- 06c-通用Composables与状态管理已完成
- 项目目录结构已搭建
- TypeScript 类型定义已完善

## Functional Requirements
- **FR-1**: 创建完整的 Mock 数据源文件，包括认证数据、问答历史数据等
- **FR-2**: 实现完整的 Mock Handler 文件，覆盖所有核心 API 接口
- **FR-3**: 配置 MSW 并集成到 main.ts，支持条件启动
- **FR-4**: 配置环境变量文件，支持 Mock 开关控制
- **FR-5**: 实现 SSE 流式问答 Mock，支持逐字输出和引用溯源

## Non-Functional Requirements
- **NFR-1**: 所有 Mock 数据类型安全，符合 DTO 定义
- **NFR-2**: Mock 数据有合理的延迟，模拟真实网络体验
- **NFR-3**: MSW 可通过环境变量开关控制
- **NFR-4**: 代码结构清晰，易于维护和扩展

## Constraints
- **Technical**: MSW 2.0+, TypeScript 5.3+, Vue 3.4+
- **Business**: 必须完全按照 06d 文档中的实现
- **Dependencies**: 已有类型定义和 API 接口

## Assumptions
- 浏览器支持 Service Worker
- localStorage 可用
- Vite 开发环境可用

## Acceptance Criteria

### AC-1: 环境变量配置完整
- **Given**: 环境变量文件已创建
- **When**: 查看 .env.development 和 .env.production
- **Then**: 包含 VITE_ENABLE_MOCK 和 VITE_API_BASE_URL 配置
- **Verification**: `programmatic`

### AC-2: Mock 数据源文件完整
- **Given**: Mock 数据源文件已创建
- **When**: 查看 mocks/data/ 目录
- **Then**: 包含 auth.data.ts、qa.data.ts 等数据源文件
- **Verification**: `programmatic`

### AC-3: Mock Handler 文件完整
- **Given**: Mock Handler 文件已创建
- **When**: 查看 mocks/handlers/ 目录
- **Then**: 包含 auth.handler.ts、qa.handler.ts 等 Handler 文件
- **Verification**: `programmatic`

### AC-4: MSW 配置文件完整
- **Given**: MSW 配置已创建
- **When**: 查看 mocks/browser.ts
- **Then**: 包含完整的 MSW worker 配置
- **Verification**: `programmatic`

### AC-5: main.ts 已集成 MSW
- **Given**: main.ts 已更新
- **When**: 查看 src/main.ts
- **Then**: 包含 MSW 条件启动逻辑
- **Verification**: `programmatic`

### AC-6: TypeScript 编译无错误
- **Given**: 所有 Mock 文件已创建
- **When**: 运行 TypeScript 编译检查
- **Then**: 无类型错误
- **Verification**: `programmatic`

## Open Questions
- 无
