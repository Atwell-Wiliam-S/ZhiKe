# 组件逻辑与Mock开发 - 验证清单

- [ ] 核心TypeScript类型定义
  - [ ] src/types/api.ts已创建，包含所有DTO类型
  - [ ] 类型定义与03-API接口规范.md一致
  - [ ] TypeScript编译无错误

- [ ] 工具函数
  - [ ] src/utils/request.ts: Axios实例与拦截器实现
  - [ ] src/utils/storage.ts: localStorage封装实现
  - [ ] src/utils/format.ts: 格式化工具实现
  - [ ] src/utils/snake-case.ts: 字段命名转换实现
  - [ ] Axios拦截器正确处理token和错误

- [ ] 核心Composables
  - [ ] src/composables/useSSEChat.ts: SSE流式问答实现
  - [ ] src/composables/useTheme.ts: 多角色主题切换实现
  - [ ] src/composables/useVideoPlayer.ts: 视频播放器控制实现
  - [ ] src/composables/useNotes.ts: 笔记管理实现
  - [ ] src/composables/usePagination.ts: 通用分页实现
  - [ ] src/composables/useNotification.ts: 通知中心实现
  - [ ] useSSEChat正确处理SSE流式响应
  - [ ] useVideoPlayer正确控制视频播放
  - [ ] useNotes正确处理CRUD操作
  - [ ] useTheme正确切换主题

- [ ] 状态管理
  - [ ] src/stores/user.ts: 用户认证Store实现
  - [ ] src/stores/theme.ts: 主题状态实现
  - [ ] src/stores/notification.ts: 通知状态实现
  - [ ] useUserStore正确处理登录/登出
  - [ ] 状态持久化正确

- [ ] 路由配置与守卫
  - [ ] src/router/routes.ts: 路由表定义实现
  - [ ] src/router/guards.ts: 导航守卫逻辑实现
  - [ ] src/router/index.ts: 路由实例实现
  - [ ] 路由守卫正确控制权限
  - [ ] 不同角色正确跳转到对应首页

- [ ] API请求层
  - [ ] src/api/auth.ts: 认证相关API实现
  - [ ] src/api/video.ts: 视频相关API实现
  - [ ] src/api/qa.ts: RAG问答API实现
  - [ ] src/api/analytics.ts: 学情分析API实现
  - [ ] src/api/student.ts: 学生课程/笔记API实现
  - [ ] src/api/admin.ts: 管理员API实现
  - [ ] API请求函数类型正确
  - [ ] 请求参数和返回值类型匹配

- [ ] Mock数据系统
  - [ ] src/mocks/browser.ts: Service Worker入口实现
  - [ ] src/mocks/handlers/: 按模块分Handler实现
  - [ ] src/mocks/data/: Mock数据源实现
  - [ ] 配置环境变量控制Mock开关
  - [ ] MSW正确拦截API请求
  - [ ] Mock数据类型安全
  - [ ] 不同模块Mock数据正确返回

- [ ] 集成与测试
  - [ ] src/main.ts: 集成所有模块，启动MSW
  - [ ] 项目构建成功
  - [ ] TypeScript编译无错误
  - [ ] 功能集成正常

- [ ] Web设计规范
  - [ ] 响应式设计：适配桌面、平板、移动设备
  - [ ] 可访问性：符合WCAG 2.1 AA标准
  - [ ] 性能优化：页面加载时间<2s
  - [ ] 代码可维护性：结构清晰，模块化设计