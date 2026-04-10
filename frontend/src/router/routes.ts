import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue')
  },
  {
    path: '/',
    redirect: '/login'
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
        path: 'course/:id',
        name: 'CourseDetail',
        component: () => import('@/views/student/CourseDetail.vue')
      },
      {
        path: 'video/:id',
        name: 'VideoLearning',
        component: () => import('@/views/student/VideoLearning.vue')
      },
      {
        path: 'qa-history',
        name: 'QaHistory',
        component: () => import('@/views/student/QaHistory.vue')
      },
      {
        path: 'notes',
        name: 'Notes',
        component: () => import('@/views/student/Notes.vue')
      },
      {
        path: 'settings',
        name: 'StudentSettings',
        component: () => import('@/views/student/Settings.vue')
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
        path: 'knowledge',
        name: 'KnowledgeGraph',
        component: () => import('@/views/teacher/KnowledgeGraph.vue')
      },
      {
        path: 'analytics',
        name: 'Analytics',
        component: () => import('@/views/teacher/Analytics.vue')
      },
      {
        path: 'upload',
        name: 'VideoUpload',
        component: () => import('@/views/teacher/VideoUpload.vue')
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    redirect: '/admin/users',
    children: [
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/admin/UserManagement.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
