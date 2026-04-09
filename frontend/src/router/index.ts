import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/student',
    name: 'Student',
    redirect: '/student/home',
    children: [
      {
        path: 'home',
        name: 'StudentHome',
        component: () => import('@/views/student/Home.vue')
      },
      {
        path: 'learn/:videoId',
        name: 'StudentLearn',
        component: () => import('@/views/student/Learn.vue')
      },
      {
        path: 'courses',
        name: 'StudentCourses',
        component: () => import('@/views/student/Courses.vue')
      }
    ]
  },
  {
    path: '/teacher',
    name: 'Teacher',
    redirect: '/teacher/home',
    children: [
      {
        path: 'home',
        name: 'TeacherHome',
        component: () => import('@/views/teacher/Home.vue')
      },
      {
        path: 'courses',
        name: 'TeacherCourses',
        component: () => import('@/views/teacher/Courses.vue')
      },
      {
        path: 'knowledge',
        name: 'TeacherKnowledge',
        component: () => import('@/views/teacher/Knowledge.vue')
      },
      {
        path: 'analytics',
        name: 'TeacherAnalytics',
        component: () => import('@/views/teacher/Analytics.vue')
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    redirect: '/admin/home',
    children: [
      {
        path: 'home',
        name: 'AdminHome',
        component: () => import('@/views/admin/Home.vue')
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue')
      },
      {
        path: 'logs',
        name: 'AdminLogs',
        component: () => import('@/views/admin/Logs.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const roleCode = localStorage.getItem('roleCode')
  
  // 登录页不需要认证
  if (to.path === '/login') {
    next()
    return
  }
  
  // 其他页面需要认证
  if (!token) {
    next('/login')
    return
  }
  
  // 根据角色重定向
  if (to.path.startsWith('/student') && roleCode !== 'STUDENT') {
    next('/login')
    return
  }
  
  if (to.path.startsWith('/teacher') && roleCode !== 'TEACHER') {
    next('/login')
    return
  }
  
  if (to.path.startsWith('/admin') && roleCode !== 'ADMIN') {
    next('/login')
    return
  }
  
  next()
})

export default router
