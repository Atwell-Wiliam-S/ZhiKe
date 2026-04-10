import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false, title: '登录' },
  },
  {
    path: '/student/home',
    name: 'StudentHome',
    component: () => import('@/views/student/Home.vue'),
    meta: { requiresAuth: true, role: 'STUDENT', title: '首页' },
  },
  {
    path: '/student/learn/:videoId',
    name: 'VideoLearning',
    component: () => import('@/views/student/VideoLearning.vue'),
    meta: { requiresAuth: true, role: 'STUDENT', title: '视频学习' },
  },
  {
    path: '/teacher/home',
    name: 'TeacherHome',
    component: () => import('@/views/teacher/Home.vue'),
    meta: { requiresAuth: true, role: 'TEACHER', title: '仪表盘' },
  },
  {
    path: '/teacher/knowledge-graph',
    name: 'KnowledgeGraph',
    component: () => import('@/views/teacher/KnowledgeGraph.vue'),
    meta: { requiresAuth: true, role: 'TEACHER', title: '知识图谱管理' },
  },
  {
    path: '/teacher/analytics',
    name: 'Analytics',
    component: () => import('@/views/teacher/Analytics.vue'),
    meta: { requiresAuth: true, role: 'TEACHER', title: '学情分析' },
  },
  {
    path: '/teacher/video-upload',
    name: 'VideoUpload',
    component: () => import('@/views/teacher/VideoUpload.vue'),
    meta: { requiresAuth: true, role: 'TEACHER', title: '视频上传' },
  },
  {
    path: '/teacher/users',
    name: 'UserManagement',
    component: () => import('@/views/teacher/UserManagement.vue'),
    meta: { requiresAuth: true, role: 'TEACHER', title: '用户管理' },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' },
  },
]