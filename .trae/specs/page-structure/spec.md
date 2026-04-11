# 智能教学系统页面结构 - 产品需求文档

## Overview
- **Summary**: 根据页面视觉描述文档，梳理智能教学系统的页面结构、布局、样式和功能要求，为前端开发提供详细的参考依据。
- **Purpose**: 确保前端开发按照设计文档的要求实现所有页面，保证视觉一致性和功能完整性。
- **Target Users**: 前端开发团队、UI/UX设计师、产品经理

## Goals
- 梳理所有页面的布局结构和响应式设计要求
- 整理各页面的视觉元素、样式和交互效果
- 明确页面间的导航关系和路由配置
- 提供详细的无障碍设计要求

## Non-Goals (Out of Scope)
- 实现具体的后端API功能
- 开发完整的前端代码
- 设计新的页面或功能
- 优化页面性能

## Background & Context
- 基于智能教学闭环系统重构设计文档 v1.2
- 所有页面遵循 04-设计系统规格.md 中定义的设计令牌
- 文档为AI Agent和前端开发者提供每个页面的精确视觉描述

## Functional Requirements
- **FR-1**: 登录/注册页
  - 左右分栏布局（桌面端），移动端堆叠
  - 左侧品牌区：深灰紫蓝到静藤蓝对角渐变背景，浮动抽象几何图形，Logo+标语+3个特性图标
  - 右侧表单区：表单字段（用户名、密码、角色选择、邮箱），记住我+忘记密码，登录按钮，分割线，第三方登录预留，底部链接
  - 交互状态：输入框聚焦效果，登录按钮悬停/按下效果，登录失败反馈，登录中状态，表单验证
  - 无障碍：表单使用<form>标签，密码显示/隐藏按钮aria-label，错误提示关联aria-describedby，角色选择Radio Group用role="radiogroup"
  - 亮色主题差异：右侧背景#ffffff，输入框背景#f8fafc，强调色#0075de
  - 自适应主题

- **FR-2**: 学生首页
  - 单栏滚动布局，最大宽度 1280px 居中，padding 0 24px
  - Top Navigation：64px固定顶部，Logo+品牌色圆点，主题切换按钮，通知铃铛（Badge红点），角色徽章Pill，用户头像Dropdown
  - Hero区域：顶部48px，底部32px，问候语"Hi, {username}"，副标题"今天想学点什么？"
  - 功能卡片网格：2×3 CSS Grid，gap 24px，卡片悬停效果（边框变为--color-accent，--shadow-glow光晕，translateY(-2px)）
  - 卡片内容：我的课程、继续学习（带进度条）、AI助教、我的笔记（带数量Pill）、学习报告（带本周学习时长）、设置
  - 最近活动：时间线样式，每条包含活动图标+描述+相对时间，空状态显示插图和文字
  - 无障碍：功能卡片使用<ul role="list">+<li>，每张卡片为<a>或<button>包含aria-label，进度条使用role="progressbar"，通知铃铛Badge使用aria-label
  - 亮色主题差异：背景#ffffff，卡片#ffffff，边框#e2e8f0，强调色#0075de
  - 双主题支持

- **FR-3**: 沉浸式视频学习页
  - 两栏布局（左70% + 右30%），全宽背景（无最大容器限制）
  - 视频播放器：16:9比例，自定义控件，快捷键支持（Space暂停/播放，←→快退/快进5s，↑↓音量，F全屏）
  - 知识点时间轴：视频播放器正下方，水平时间轴，indigo圆点标记知识点位置，当前进度显示，脉冲动画
  - AI问答面板：容器背景--color-bg-card，标题栏（"AI助教"+在线状态指示灯+清空对话按钮），消息列表（用户消息右对齐，AI消息左对齐，引用标签可点击），输入栏（多行textarea，发送按钮）
  - 知识大纲：右栏Sticky，手风琴样式，章节→知识点（带时间戳），当前播放项标记
  - 我的笔记：时间倒序，每条笔记包含时间戳Pill+内容预览+编辑/删除操作
  - 无障碍：视频播放器role="application"，时间轴标记role="slider"，AI消息列表role="log"+aria-live="polite"，引用标签aria-label
  - 亮色主题差异：全宽背景#ffffff，卡片#ffffff，边框#e2e8f0，引用标签#0075de背景
  - 双主题支持

- **FR-4**: 教师首页
  - 左侧固定侧边栏 + 右侧主内容区
  - 左侧边栏：背景--color-bg-canvas (#fdfdf8)，Logo+教师角色标识Pill，导航项（仪表盘、课程管理、视频管理、知识片段、学情分析、系统设置），底部折叠按钮+用户信息
  - KPI卡片行：4列Grid，gap 16px，卡片显示总学生数、活跃课程、平均完成率（带环比趋势箭头）、今日提问
  - 课程管理表格：表头（课程名称、教师、视频数、学生数、状态、操作），行悬停效果，操作按钮（查看、编辑、删除），分页
  - 最近活动时间线：左侧2px竖线+8px圆点+时间+描述
  - 无障碍：侧边栏导航使用<nav aria-label="主导航">+<ul>/<li>，当前页aria-current="page"，折叠按钮aria-label，表格使用语义化标签，分页使用<nav aria-label="分页">
  - 浅色主题固定

- **FR-5**: 知识图谱管理页
  - 顶部筛选栏 + 左右分栏（左40%视频预览，右60%片段列表）
  - 顶部筛选栏：面包屑导航，课程下拉+视频下拉+搜索框，--color-bg-elevated (#eeefe9)背景
  - 左栏：视频预览（16:9比例，带缩略图），时间轴叠加（绿色已确认，琥珀色待审核，珊瑚色需修改），Sticky定位
  - 右栏：片段卡片列表（标题、时间范围、内容预览、关键词Pill、操作按钮），卡片悬停效果
  - 底部操作栏："添加片段"按钮，批量操作（全选Checkbox、批量删除、批量确认）
  - 无障碍：面包屑使用<ol>有序列表+aria-label="面包屑"，时间轴色条使用aria-label描述各颜色含义，操作按钮aria-label
  - 浅色主题固定

- **FR-6**: 学情分析大屏
  - 顶部操作栏 + 2×2 Grid图表区，padding 24px
  - 顶部操作栏：左侧课程选择下拉+日期范围选择器，右侧导出按钮
  - 左上（2/3）：难点热力图（ECharts水平热力图），X轴视频时间轴，Y轴学生密度，蓝色→黄色→红色渐变，脉冲圆点标记难点峰值
  - 右上（1/3）：班级概览KPI（4个指标卡片），包含完成率（ECharts环形进度图）、活跃学生、平均观看时长、总提问数
  - 左下（1/2）：高频困惑词云（ECharts词云图），高频词大字，低频词小字
  - 右下（1/2）：学生排行榜（ECharts水平柱状图），Top 10学生，头像+姓名+学习时长
  - 无障碍：图表容器使用role="img"+aria-label提供文字摘要，排行榜下方提供<table>格式的可访问数据表
  - 浅色主题固定

- **FR-7**: 课程详情页
  - 顶部面包屑 + Hero区 + Tab内容区 + 右侧边栏
  - Hero区：左侧课程封面图（16:9比例，--radius-lg圆角），右侧课程标题、教师信息、评分、统计、描述（3行截断+"展开全部"链接）、CTA按钮
  - Tab导航：课程概览 | 课程目录 | 学员评价，下划线指示器，激活态文字颜色变化
  - 课程目录：手风琴列表，章节→视频（带时间戳，显示已完成/正在学习/未解锁状态）
  - 右侧边栏：课程统计卡片（完成进度环形图），教师简介卡片，相关课程推荐
  - 无障碍：面包屑<nav aria-label="面包屑"><ol>，Tab使用role="tablist"+role="tab"+role="tabpanel"+aria-selected，手风琴使用原生<details>/<summary>，视频状态图标使用aria-label
  - 亮色主题差异：背景#ffffff，卡片#ffffff，边框#e2e8f0
  - 双主题支持

- **FR-8**: 视频上传页
  - 单栏居中，最大宽度 800px，padding 32px 24px
  - 上传区域：虚线--color-border边框，--radius-lg圆角，padding 48px，居中显示Upload图标+文字提示+文件选择链接+支持格式说明，拖拽悬停效果
  - 上传进度：--color-accent填充进度条，状态文字显示，失败重试链接
  - 视频信息表单：课程选择（Select下拉，必填），视频标题（Input，必填，max 100字符），视频描述（Textarea，可选，max 500字符，字数计数器），排序序号（InputNumber，可选），上传后显示视频缩略图预览
  - 操作按钮：右对齐，"取消"按钮（Ghost样式），"保存并解析"按钮（Dark Primary #1e1f23样式，Sparkles图标+文字）
  - 无障碍：拖拽区使用<input type="file">隐藏，点击触发，拖拽区role="button"+aria-label，进度条role="progressbar"，表单验证错误aria-describedby
  - 浅色主题固定

- **FR-9**: 用户管理页
  - 顶部操作栏 + 筛选栏 + 数据表格 + 分页
  - 顶部操作栏：左侧"用户管理"标题，右侧"添加用户"按钮（Dark Primary样式，UserPlus图标）
  - 筛选栏：角色筛选（Pill徽章切换：全部/学生/教师/管理员），搜索框，状态筛选（启用/禁用Toggle开关）
  - 数据表格：列（用户名、邮箱、角色（Pill徽章）、状态（Toggle开关）、注册时间、操作），行悬停效果，操作按钮（编辑、删除）
  - 角色徽章颜色：学生--color-info-bg，教师--color-success-bg，管理员--color-warning-bg
  - 移动端卡片模式：每个用户一张卡片，包含头像+用户名+角色Pill+状态+操作按钮
  - 分页：底部居中，页码按钮+"每页10条"下拉+"共156条"文字，当前页--color-accent背景
  - 无障碍：表格语义化<table>/<caption>，角色筛选使用role="tablist"+aria-selected，Toggle开关role="switch"+aria-checked，删除确认弹窗role="alertdialog"+aria-describedby
  - 浅色主题固定

- **FR-10**: 问答历史页
  - 顶部筛选 + 问答卡片列表 + 分页
  - 筛选栏：搜索输入框，时间筛选（Pill徽章切换：今天/本周/本月/全部），排序（Select下拉：最新优先/最早优先）
  - 问答卡片：--color-bg-card背景，--color-border边框，--radius-md圆角，顶部问题文本+时间戳，中部AI回答预览（3行截断），底部引用来源数量Pill+Token消耗+模型版本标签，悬停效果
  - 空状态：居中MessageSquare图标+"暂无问答记录"+"去视频学习页提问"按钮
  - 无障碍：筛选Pill role="tablist"+aria-selected，卡片使用<article>语义标签，"查看详情"链接aria-label，空状态插图role="img" aria-label
  - 亮色主题差异：背景#ffffff，卡片#ffffff，边框#e2e8f0，强调色#0075de，Pill背景rgba(0,117,222,0.1)
  - 双主题支持

- **FR-11**: 个人设置页
  - 左侧导航 + 右侧设置表单，最大宽度 960px 居中
  - 左侧设置导航：--color-bg-card背景，--radius-md圆角，导航项（个人信息、主题偏好、安全设置、通知设置），激活态样式
  - 个人信息：圆形头像（80px），"更换头像"按钮，表单字段（用户名、邮箱、手机号、个人简介），保存按钮
  - 主题偏好（学生端）：Radio Group（暗色模式、亮色模式、跟随系统），小型实时预览卡片
  - 安全设置：修改密码表单（旧密码、新密码、确认新密码），密码强度指示器（弱/中/强）
  - 通知设置：Toggle开关列表（课程更新通知、作业提醒、系统公告、邮件通知汇总）
  - 无障碍：设置导航使用<nav aria-label="设置导航">，表单字段<label>关联for属性，密码强度role="progressbar"，Toggle role="switch"+aria-checked
  - 跟随角色主题

- **FR-12**: 笔记列表页
  - 顶部筛选 + 笔记卡片瀑布流
  - 筛选栏：课程筛选（Select下拉），视频筛选（Select下拉，联动课程），搜索输入框
  - 笔记卡片：CSS Grid布局，--color-bg-card背景，--color-border边框，--radius-md圆角，顶部视频标题+时间戳Pill，中部笔记内容，底部创建时间+公开/私有Toggle+编辑/删除按钮，悬停效果
  - 空状态：居中StickyNote图标+"暂无笔记"+"去视频学习页创建笔记"按钮
  - 无障碍：笔记卡片使用<article>语义，时间戳Pill aria-label，公开/私有Toggle role="switch"+aria-checked，删除按钮二次确认role="alertdialog"
  - 亮色主题差异：背景#ffffff，卡片#ffffff，边框#e2e8f0，时间戳Pill #0075de背景
  - 双主题支持

- **FR-13**: 通知中心
  - 右侧滑出Drawer面板，宽度400px（桌面端）/全宽（移动端）
  - 面板头部：标题"通知"，"全部已读"按钮（Ghost样式），关闭按钮（X图标）
  - 通知分类Tab：全部 | 系统 | 课程 | 互动，Pill徽章切换，未读计数Badge
  - 通知列表：列表项padding 14px 16px，未读样式（左侧3px --color-accent边框+--color-accent-subtle行背景），已读样式，内容包含图标+标题+时间+描述
  - 空状态：居中BellOff图标+"暂无通知"
  - 无障碍：Drawer role="dialog"+aria-modal="true"+aria-label="通知中心"，打开时Trap Focus，Escape关闭，通知列表role="list"+aria-label，未读项aria-unread="true"
  - 跟随角色主题

- **FR-14**: 404错误页
  - 全屏居中布局，最小高度100vh
  - 背景：--color-bg-canvas
  - 内容：大号404数字（system-ui 120px 800，--color-text-tertiary，opacity 0.3），标题"页面未找到"，描述"抱歉，您访问的页面不存在或已被移除。"，操作按钮（"返回首页"按钮，"返回上页"链接）
  - 交互：按钮hover效果，自动10秒后倒计时跳转首页
  - 无障碍：role="alert"标记错误状态，主内容区域role="main"，按钮aria-label，倒计时使用aria-live="polite"
  - 自适应主题

## Non-Functional Requirements
- **NFR-1**: 响应式设计
  - 断点：xs (0–639px)、sm (640–767px)、md (768–1023px)、lg (1024–1279px)、xl (1280–1535px)、2xl (1536px+)
  - 不同设备的布局策略：
    - xs: 单栏堆叠，侧边栏收起为Drawer
    - sm: 单栏堆叠，卡片1列
    - md: 双栏适配，侧边栏可折叠
    - lg: 标准双栏/三栏
    - xl: 1280px居中
    - 2xl: 1440px居中

- **NFR-2**: 页面转场动画
  - 页面切换：fade + slide-right (200ms, ease-out)
  - 模态弹窗：fade + scale(0.95→1) (150ms, cubic-bezier(0.16,1,0.3,1))
  - Drawer侧栏：translate-x(-100%→0) (250ms, cubic-bezier(0.16,1,0.3,1))
  - Toast通知：slide-down + fade (200ms, ease-out)
  - 卡片悬停：translateY(-2px) + shadow (150ms, ease-out)
  - 列表项进入：fade + slide-up(8px) (200ms, ease-out, stagger 30ms)
  - 无障碍：所有动画均尊重 prefers-reduced-motion: reduce，此时降级为 opacity 瞬切（0ms）

- **NFR-3**: 无障碍设计
  - 键盘导航：所有可交互元素支持Tab聚焦，Enter/Space激活，Escape关闭弹窗
  - 焦点指示器：outline: 2px solid var(--color-accent); outline-offset: 2px
  - ARIA标签：图标按钮需aria-label，导航需aria-current="page"，进度需aria-valuenow
  - 语义化HTML：导航用<nav>，主内容用<main>，侧边栏用<aside>，表格用<table>
  - 颜色对比度：文本与背景对比度 ≥ 4.5:1 (AA)，大文本 ≥ 3:1
  - 屏幕阅读器：图表提供aria-label文字摘要，图标用role="img" + aria-label
  - 跳过导航：页面顶部隐藏"跳到主内容"链接，Tab聚焦时显示

- **NFR-4**: 主题支持
  - 学生端：双主题（暗色/亮色）
  - 教师/管理员端：浅色主题固定
  - 公开页面：自适应主题

## Constraints
- **Technical**: Vue 3 + TypeScript, 设计系统规格
- **Business**: 基于现有设计文档实现
- **Dependencies**: ECharts（用于数据可视化）

## Assumptions
- 设计系统的基础组件已实现
- 后端API已准备就绪
- 项目已配置好必要的依赖和工具

## Acceptance Criteria

### AC-1: 页面布局和响应式设计
- **Given**: 访问任何页面
- **When**: 在不同设备上查看
- **Then**: 页面布局应符合设计文档的响应式要求
- **Verification**: `human-judgment`

### AC-2: 视觉元素和样式
- **Given**: 访问任何页面
- **When**: 查看页面元素
- **Then**: 视觉元素和样式应符合设计文档的要求
- **Verification**: `human-judgment`

### AC-3: 功能完整性
- **Given**: 访问任何页面
- **When**: 使用页面功能
- **Then**: 所有功能应按照设计文档的要求实现
- **Verification**: `human-judgment`

### AC-4: 导航关系和路由
- **Given**: 在系统中导航
- **When**: 点击导航链接
- **Then**: 系统应正确导航到目标页面
- **Verification**: `human-judgment`

### AC-5: 无障碍设计
- **Given**: 使用屏幕阅读器或键盘导航
- **When**: 访问页面
- **Then**: 页面应符合无障碍设计要求
- **Verification**: `human-judgment`

## 页面间导航关系
- 登录/注册页 → 学生首页/教师首页/管理员首页（根据角色）
- 学生首页 → 课程详情页 → 视频学习页
- 学生首页 → AI助教、我的笔记、学习报告、设置
- 教师首页 → 课程管理、视频管理、知识图谱管理、学情分析
- 教师首页 → 视频上传页
- 管理员首页 → 用户管理页
- 所有角色 → 个人设置页、通知中心
- 任意无效路由 → 404错误页

## 路由表
| 路由路径 | 页面 | 角色 | 权限 |
| --- | --- | --- | --- |
| `/login` | 登录/注册页 | 公开 | 无需认证 |
| `/student/home` | 学生首页 | STUDENT | 需认证 |
| `/student/learn/:videoId` | 视频学习页 | STUDENT | 需认证 |
| `/student/course/:courseId` | 课程详情页 | STUDENT | 需认证 |
| `/student/qa/history` | 问答历史页 | STUDENT | 需认证 |
| `/student/notes` | 笔记列表页 | STUDENT | 需认证 |
| `/teacher/home` | 教师首页 | TEACHER | 需认证 |
| `/teacher/knowledge` | 知识图谱管理页 | TEACHER | 需认证 |
| `/teacher/upload` | 视频上传页 | TEACHER | 需认证 |
| `/teacher/analytics` | 学情分析大屏 | TEACHER/ADMIN | 需认证 |
| `/admin/users` | 用户管理页 | ADMIN | 需认证 |
| `/settings` | 个人设置页 | ALL | 需认证 |
| — | 通知中心 | ALL | Drawer面板，无独立路由（铃铛图标触发） |
| `*` | 404错误页 | 公开 | — |

## Open Questions
- [ ] 具体的后端API实现细节
- [ ] 数据可视化的具体数据结构
- [ ] 通知中心的实时更新机制
- [ ] 视频上传的具体实现方式
- [ ] 笔记存储和同步的具体实现方式