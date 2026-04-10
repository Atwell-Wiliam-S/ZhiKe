import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false, title: '登录' },
  },
  {
    path: '/student',
    component: () => import('@/layouts/StudentLayout.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' },
    children: [
      { path: 'home', name: 'StudentHome', component: () => import('@/views/student/Home.vue'), meta: { title: '首页' } },
      { path: 'course/:courseId', name: 'CourseDetail', component: () => import('@/views/student/CourseDetail.vue'), meta: { title: '课程详情' } },
      { path: 'learn/:videoId', name: 'VideoLearning', component: () => import('@/views/student/VideoLearning.vue'), meta: { title: '视频学习' } },
      { path: 'qa/history', name: 'QaHistory', component: () => import('@/views/student/QaHistory.vue'), meta: { title: '问答历史' } },
      { path: 'notes', name: 'NotesList', component: () => import('@/views/student/NotesList.vue'), meta: { title: '我的笔记' } },
    ],
  },
  {
    path: '/teacher',
    component: () => import('@/layouts/TeacherLayout.vue'),
    meta: { requiresAuth: true, role: 'TEACHER' },
    children: [
      { path: 'home', name: 'TeacherHome', component: () => import('@/views/teacher/Home.vue'), meta: { title: '仪表盘' } },
      { path: 'knowledge', name: 'KnowledgeManage', component: () => import('@/views/teacher/KnowledgeManage.vue'), meta: { title: '知识片段管理' } },
      { path: 'upload', name: 'VideoUpload', component: () => import('@/views/teacher/VideoUpload.vue'), meta: { title: '视频上传' } },
      { path: 'analytics', name: 'Analytics', component: () => import('@/views/teacher/Analytics.vue'), meta: { title: '学情分析' } },
    ],
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      { path: 'users', name: 'UserManage', component: () => import('@/views/admin/UserManage.vue'), meta: { title: '用户管理' } },
    ],
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/settings/index.vue'),
    meta: { requiresAuth: true, title: '个人设置' },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' },
  },
]