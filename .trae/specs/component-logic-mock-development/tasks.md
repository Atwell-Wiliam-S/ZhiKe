# 组件逻辑与Mock开发 - 实现计划

## [ ] Task 1: 实现核心TypeScript类型定义
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建src/types/api.ts文件，定义所有DTO类型
  - 确保类型定义与03-API接口规范.md一致
  - 支持snake_case到camelCase的转换
- **Acceptance Criteria Addressed**: AC-5, AC-6
- **Test Requirements**:
  - `programmatic` TR-1.1: TypeScript编译无错误
  - `programmatic` TR-1.2: 类型定义完整，覆盖所有API接口

## [ ] Task 2: 实现工具函数
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 创建src/utils/request.ts: Axios实例与拦截器
  - 创建src/utils/storage.ts: localStorage封装
  - 创建src/utils/format.ts: 格式化工具
  - 创建src/utils/snake-case.ts: 字段命名转换
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-2.1: Axios拦截器正确处理token和错误
  - `programmatic` TR-2.2: 工具函数测试通过

## [ ] Task 3: 实现核心Composables
- **Priority**: P0
- **Depends On**: Task 2
- **Description**: 
  - 创建src/composables/useSSEChat.ts: SSE流式问答
  - 创建src/composables/useTheme.ts: 多角色主题切换
  - 创建src/composables/useVideoPlayer.ts: 视频播放器控制
  - 创建src/composables/useNotes.ts: 笔记管理
  - 创建src/composables/usePagination.ts: 通用分页
  - 创建src/composables/useNotification.ts: 通知中心
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4
- **Test Requirements**:
  - `programmatic` TR-3.1: useSSEChat正确处理SSE流式响应
  - `programmatic` TR-3.2: useVideoPlayer正确控制视频播放
  - `programmatic` TR-3.3: useNotes正确处理CRUD操作
  - `human-judgment` TR-3.4: useTheme正确切换主题

## [ ] Task 4: 实现状态管理
- **Priority**: P0
- **Depends On**: Task 3
- **Description**: 
  - 创建src/stores/user.ts: 用户认证Store
  - 创建src/stores/theme.ts: 主题状态
  - 创建src/stores/notification.ts: 通知状态
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-4.1: useUserStore正确处理登录/登出
  - `programmatic` TR-4.2: 状态持久化正确

## [ ] Task 5: 实现路由配置与守卫
- **Priority**: P0
- **Depends On**: Task 4
- **Description**: 
  - 创建src/router/routes.ts: 路由表定义
  - 创建src/router/guards.ts: 导航守卫逻辑
  - 创建src/router/index.ts: 路由实例
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-5.1: 路由守卫正确控制权限
  - `programmatic` TR-5.2: 不同角色正确跳转到对应首页

## [ ] Task 6: 实现API请求层
- **Priority**: P1
- **Depends On**: Task 2
- **Description**: 
  - 创建src/api/auth.ts: 认证相关API
  - 创建src/api/video.ts: 视频相关API
  - 创建src/api/qa.ts: RAG问答API
  - 创建src/api/analytics.ts: 学情分析API
  - 创建src/api/student.ts: 学生课程/笔记API
  - 创建src/api/admin.ts: 管理员API
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-6.1: API请求函数类型正确
  - `programmatic` TR-6.2: 请求参数和返回值类型匹配

## [ ] Task 7: 实现Mock数据系统
- **Priority**: P1
- **Depends On**: Task 6
- **Description**: 
  - 创建src/mocks/browser.ts: Service Worker入口
  - 创建src/mocks/handlers/: 按模块分Handler
  - 创建src/mocks/data/: Mock数据源
  - 配置环境变量控制Mock开关
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-7.1: MSW正确拦截API请求
  - `programmatic` TR-7.2: Mock数据类型安全
  - `programmatic` TR-7.3: 不同模块Mock数据正确返回

## [ ] Task 8: 集成与测试
- **Priority**: P1
- **Depends On**: Task 7
- **Description**: 
  - 更新src/main.ts: 集成所有模块，启动MSW
  - 测试所有功能的集成
  - 验证TypeScript编译无错误
- **Acceptance Criteria Addressed**: 所有AC
- **Test Requirements**:
  - `programmatic` TR-8.1: 项目构建成功
  - `programmatic` TR-8.2: TypeScript编译无错误
  - `human-judgment` TR-8.3: 功能集成正常