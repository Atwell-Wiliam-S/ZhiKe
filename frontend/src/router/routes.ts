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
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' },
  },
]