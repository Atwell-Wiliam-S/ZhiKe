# 核心 Composables 实现 - The Implementation Plan (Decomposed and Prioritized Task List)

## [ ] Task 1: 实现 useTheme Composable
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 根据 06b 文档实现 useTheme Composable
  - 支持多角色主题切换（student-dark、student-light、teacher-light）
  - 支持学生端手动切换暗色/亮色
  - 支持系统主题检测
- **Acceptance Criteria Addressed**: [AC-2]
- **Test Requirements**:
  - `programmatic` TR-1.1: composables/useTheme.ts 文件已创建
  - `programmatic` TR-1.2: 包含 initTheme、toggleTheme、isDark 功能
  - `programmatic` TR-1.3: TypeScript 编译无错误

## [ ] Task 2: 实现 useSSEChat Composable
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 根据 06b 文档实现 useSSEChat Composable
  - 支持 SSE 流式问答
  - 支持逐字输出
  - 支持引用溯源
  - 支持错误处理和重试
  - 支持中断生成
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `programmatic` TR-2.1: composables/useSSEChat.ts 文件已创建
  - `programmatic` TR-2.2: 包含 sendMessage、abortStream、clearMessages 功能
  - `programmatic` TR-2.3: TypeScript 编译无错误

## [ ] Task 3: 实现 useVideoPlayer Composable
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 根据 06b 文档实现 useVideoPlayer Composable
  - 封装视频播放控制
  - 支持断点续播
  - 支持心跳上报
  - 支持知识点联动
  - 支持键盘快捷键
- **Acceptance Criteria Addressed**: [AC-3]
- **Test Requirements**:
  - `programmatic` TR-3.1: composables/useVideoPlayer.ts 文件已创建
  - `programmatic` TR-3.2: 包含播放控制、断点续播、心跳上报功能
  - `programmatic` TR-3.3: TypeScript 编译无错误

## [ ] Task 4: 完整验证和测试
- **Priority**: P1
- **Depends On**: [Task 1, Task 2, Task 3]
- **Description**: 
  - 验证所有 Composables 已正确实现
  - 运行 TypeScript 编译检查
  - 确保代码符合规范
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `programmatic` TR-4.1: 所有三个 Composables 文件都存在
  - `programmatic` TR-4.2: TypeScript 编译无错误
