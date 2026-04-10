# 前端项目结构与类型定义 - The Implementation Plan (Decomposed and Prioritized Task List)

## [ ] Task 1: 安装和配置项目依赖
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 更新 package.json 添加所需依赖（Pinia, Vue Router, Axios, Element Plus, ECharts, MSW等）
  - 安装依赖
- **Acceptance Criteria Addressed**: [AC-5]
- **Test Requirements**:
  - `programmatic` TR-1.1: package.json 包含所有必需依赖
  - `programmatic` TR-1.2: pnpm install 成功执行无错误
- **Notes**: 使用 pnpm 作为包管理器

## [ ] Task 2: 创建项目目录结构
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建 api/、composables/、stores/、router/、mocks/ 及其子目录
  - 创建 styles/ 目录用于全局样式
  - 创建 views/ 目录用于页面组件
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `programmatic` TR-2.1: 所有必需目录都已创建
- **Notes**: 参考 06a 文档中的目录规范

## [ ] Task 3: 完善类型定义文件
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 检查并完善 types/api.ts，确保包含所有DTO定义
  - 创建 types/env.d.ts 环境变量类型
  - 创建 types/component.ts 组件类型
- **Acceptance Criteria Addressed**: [AC-2]
- **Test Requirements**:
  - `programmatic` TR-3.1: types/api.ts 包含 06a 文档中所有接口定义
  - `programmatic` TR-3.2: TypeScript 编译无类型错误

## [ ] Task 4: 创建字段名转换工具
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建 utils/snake-case.ts
  - 实现 snakeCaseToCamelCase 函数
  - 实现 camelCaseToSnakeCase 函数
  - 实现对象和数组的深度转换
- **Acceptance Criteria Addressed**: [AC-3]
- **Test Requirements**:
  - `programmatic` TR-4.1: snake_case → camelCase 转换正确
  - `programmatic` TR-4.2: camelCase → snake_case 转换正确
  - `programmatic` TR-4.3: 嵌套对象转换正确

## [ ] Task 5: 更新 Axios 请求工具
- **Priority**: P0
- **Depends On**: [Task 4]
- **Description**: 
  - 修改 utils/request.ts
  - 在请求拦截器中添加 camelCase → snake_case 转换
  - 在响应拦截器中添加 snake_case → camelCase 转换
- **Acceptance Criteria Addressed**: [AC-3]
- **Test Requirements**:
  - `programmatic` TR-5.1: 请求参数自动转换为 snake_case
  - `programmatic` TR-5.2: 响应数据自动转换为 camelCase

## [ ] Task 6: 创建 API 请求层模块
- **Priority**: P0
- **Depends On**: [Task 3, Task 5]
- **Description**: 
  - 创建 api/auth.ts - 认证相关API
  - 创建 api/video.ts - 视频相关API
  - 创建 api/qa.ts - RAG问答API
  - 创建 api/analytics.ts - 学情分析API
  - 创建 api/student.ts - 学生课程/笔记API
  - 创建 api/admin.ts - 管理员API
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `programmatic` TR-6.1: 所有API模块文件都已创建
  - `programmatic` TR-6.2: 每个API函数都正确导入类型

## [ ] Task 7: 创建其他工具函数
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 创建 utils/storage.ts - localStorage 封装
  - 创建 utils/format.ts - 格式化工具
- **Acceptance Criteria Addressed**: []
- **Test Requirements**:
  - `programmatic` TR-7.1: 工具函数文件创建成功

## [ ] Task 8: 配置路径别名
- **Priority**: P1
- **Depends On**: [Task 2]
- **Description**: 
  - 更新 vite.config.ts 配置 @ 别名
  - 更新 tsconfig.json 路径映射
- **Acceptance Criteria Addressed**: []
- **Test Requirements**:
  - `programmatic` TR-8.1: 路径别名配置正确，TypeScript 无错误
