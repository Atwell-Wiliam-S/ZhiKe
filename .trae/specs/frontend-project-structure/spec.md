# 前端项目结构与类型定义 - Product Requirement Document

## Overview
- **Summary**: 完善智能教学闭环系统前端项目的目录结构、类型定义、工具函数和API请求层，确保与设计文档06a完全一致
- **Purpose**: 建立规范的前端项目架构，为后续业务开发奠定坚实基础
- **Target Users**: 前端开发团队、后端对接团队

## Goals
- 完成前端项目目录结构搭建
- 完善所有TypeScript类型定义（与03-API接口规范一致）
- 创建snake_case ↔ camelCase转换工具
- 实现完整的API请求层模块
- 配置必要的依赖和开发环境

## Non-Goals (Out of Scope)
- 业务组件开发
- 页面视图实现
- Mock数据配置
- 测试用例编写

## Background & Context
- 已有前端基础代码（Vue 3 + Vite）
- 已有部分类型定义文件
- 04设计系统已完成
- 06a文档提供了完整的项目结构和类型定义规范

## Functional Requirements
- **FR-1**: 完成项目目录结构搭建，包括api、composables、stores、router、mocks等目录
- **FR-2**: 完善类型定义，确保与03-API接口规范3.9节完全一致
- **FR-3**: 创建snake_case ↔ camelCase转换工具函数
- **FR-4**: 实现Axios请求拦截器的字段名自动转换
- **FR-5**: 创建完整的API请求层模块（auth、video、qa、analytics、student、admin）
- **FR-6**: 安装并配置必要的第三方依赖

## Non-Functional Requirements
- **NFR-1**: 类型定义完整且类型安全
- **NFR-2**: 代码结构清晰，符合Vue 3最佳实践
- **NFR-3**: 字段转换工具高效准确

## Constraints
- **Technical**: Vue 3.4+, TypeScript 5.3+, Vite 5.0+
- **Business**: 必须与后端API规范完全一致
- **Dependencies**: Pinia 2.1+, Vue Router 4.2+, Axios 1.6+, Element Plus 2.5+

## Assumptions
- 后端使用Jackson SnakeCaseStrategy序列化
- 前端使用camelCase命名规范
- 网络传输使用snake_case

## Acceptance Criteria

### AC-1: 目录结构完整
- **Given**: 前端项目已初始化
- **When**: 查看项目目录结构
- **Then**: 所有06a文档中定义的目录都已创建
- **Verification**: `programmatic`

### AC-2: 类型定义完整
- **Given**: 类型定义文件已创建
- **When**: 检查types/api.ts内容
- **Then**: 包含所有06a文档中定义的DTO接口
- **Verification**: `programmatic`

### AC-3: 字段转换工具正常工作
- **Given**: 转换工具函数已实现
- **When**: 传入snake_case对象
- **Then**: 返回camelCase对象，反之亦然
- **Verification**: `programmatic`

### AC-4: API请求层模块完整
- **Given**: API目录已创建
- **When**: 查看api/目录
- **Then**: 包含auth.ts、video.ts、qa.ts、analytics.ts、student.ts、admin.ts
- **Verification**: `programmatic`

### AC-5: 依赖安装成功
- **Given**: package.json已更新
- **When**: 执行依赖安装
- **Then**: 所有必需依赖都成功安装
- **Verification**: `programmatic`

## Open Questions
- 无
