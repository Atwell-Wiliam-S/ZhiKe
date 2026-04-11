# Mock 数据与测试规范 - The Implementation Plan (Decomposed and Prioritized Task List)

## [ ] Task 1: 创建环境变量配置
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建 .env.development 文件，配置 VITE_ENABLE_MOCK=true 和 VITE_API_BASE_URL
  - 创建 .env.production 文件，配置 VITE_ENABLE_MOCK=false 和 VITE_API_BASE_URL
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `programmatic` TR-1.1: .env.development 和 .env.production 文件已创建
  - `programmatic` TR-1.2: 包含正确的环境变量配置

## [ ] Task 2: 创建 Mock 数据源文件
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建 mocks/data/auth.data.ts - 认证数据（用户、角色）
  - 创建 mocks/data/qa.data.ts - 问答历史数据
  - 创建 mocks/data/video.data.ts - 视频数据（可选）
  - 创建 mocks/data/student.data.ts - 学生数据（可选）
  - 创建 mocks/data/analytics.data.ts - 学情分析数据（可选）
  - 创建 mocks/data/admin.data.ts - 管理员数据（可选）
- **Acceptance Criteria Addressed**: [AC-2]
- **Test Requirements**:
  - `programmatic` TR-2.1: Mock 数据源文件已创建
  - `programmatic` TR-2.2: 所有数据类型安全，符合 DTO 定义
  - `programmatic` TR-2.3: TypeScript 编译无错误

## [ ] Task 3: 创建 Mock Handler 文件
- **Priority**: P0
- **Depends On**: [Task 2]
- **Description**: 
  - 创建 mocks/handlers/auth.handler.ts - 认证接口 Handler（登录、获取用户信息）
  - 创建 mocks/handlers/qa.handler.ts - QA 接口 Handler（SSE 流式问答、问答历史）
  - 创建 mocks/handlers/video.handler.ts - 视频接口 Handler（可选）
  - 创建 mocks/handlers/student.handler.ts - 学生接口 Handler（可选）
  - 创建 mocks/handlers/analytics.handler.ts - 学情分析接口 Handler（可选）
  - 创建 mocks/handlers/admin.handler.ts - 管理员接口 Handler（可选）
- **Acceptance Criteria Addressed**: [AC-3]
- **Test Requirements**:
  - `programmatic` TR-3.1: Mock Handler 文件已创建
  - `programmatic` TR-3.2: SSE 流式问答支持逐字输出和引用溯源
  - `programmatic` TR-3.3: 所有 Handler 有合理的延迟
  - `programmatic` TR-3.4: TypeScript 编译无错误

## [ ] Task 4: 创建 MSW 配置与启动
- **Priority**: P0
- **Depends On**: [Task 3]
- **Description**: 
  - 创建 mocks/browser.ts - MSW worker 配置，整合所有 Handler
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `programmatic` TR-4.1: mocks/browser.ts 文件已创建
  - `programmatic` TR-4.2: 包含所有 Handler 的整合
  - `programmatic` TR-4.3: TypeScript 编译无错误

## [ ] Task 5: 更新 main.ts 集成 MSW
- **Priority**: P0
- **Depends On**: [Task 4]
- **Description**: 
  - 更新 src/main.ts，添加 MSW 条件启动逻辑
  - 在开发环境且 VITE_ENABLE_MOCK=true 时启动 MSW
- **Acceptance Criteria Addressed**: [AC-5]
- **Test Requirements**:
  - `programmatic` TR-5.1: src/main.ts 已更新
  - `programmatic` TR-5.2: 包含 MSW 条件启动逻辑
  - `programmatic` TR-5.3: TypeScript 编译无错误

## [ ] Task 6: 完整验证和测试
- **Priority**: P1
- **Depends On**: [Task 1, Task 2, Task 3, Task 4, Task 5]
- **Description**: 
  - 验证所有 Mock 文件已正确创建
  - 运行 TypeScript 编译检查
  - 确保代码符合规范
- **Acceptance Criteria Addressed**: [AC-6]
- **Test Requirements**:
  - `programmatic` TR-6.1: 所有 Mock 文件都存在
  - `programmatic` TR-6.2: TypeScript 编译无错误
