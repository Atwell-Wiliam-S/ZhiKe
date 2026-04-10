import type { Router } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useTheme } from '@/composables/useTheme'

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to) => {
    const userStore = useUserStore()

    // 1. 公开页面直接放行
    if (to.meta.requiresAuth === false) {
      if (to.name === 'Login' && userStore.isLoggedIn) {
        return userStore.homeRoute
      }
      return true
    }

    // 2. 未登录 → 跳转登录页
    if (!userStore.isLoggedIn) {
      return { name: 'Login', query: { redirect: to.fullPath } }
    }

    // 3. 角色权限检查
    const requiredRole = to.meta.role as string | undefined
    if (requiredRole && userStore.roleCode !== requiredRole) {
      return userStore.homeRoute
    }

    // 4. 初始化主题（首次进入时）
    if (userStore.roleCode) {
      const { initTheme } = useTheme()
      initTheme(userStore.roleCode)
    }

    // 5. 设置页面标题
    if (to.meta.title) {
      document.title = `${to.meta.title} - ZhiKe`
    }

    return true
  })
}