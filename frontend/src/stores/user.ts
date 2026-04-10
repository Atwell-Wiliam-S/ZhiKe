import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfoDTO, RoleDTO, LoginResponse } from '@/types/api'

const TOKEN_KEY = 'zhike-token'
const USER_KEY = 'zhike-user'

export const useUserStore = defineStore('user', () => {
  // ─── State ───
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) ?? '')
  const userInfo = ref<UserInfoDTO | null>(
    JSON.parse(localStorage.getItem(USER_KEY) ?? 'null')
  )
  const roles = ref<RoleDTO[]>([])

  // ─── Getters ───
  const isLoggedIn = computed(() => !!token.value)
  const roleCode = computed(() => userInfo.value?.roleCode ?? '')
  const isStudent = computed(() => roleCode.value === 'STUDENT')
  const isTeacher = computed(() => roleCode.value === 'TEACHER')
  const isAdmin = computed(() => roleCode.value === 'ADMIN')

  const homeRoute = computed(() => {
    switch (roleCode.value) {
      case 'STUDENT': return '/student/home'
      case 'TEACHER': return '/teacher/home'
      case 'ADMIN':   return '/admin/users'
      default:        return '/login'
    }
  })

  // ─── Actions ───
  async function login(username: string, password: string) {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    })
    const json = await response.json()
    if (json.code !== 200) throw new Error(json.message ?? '登录失败')
    const data = json.data as LoginResponse
    setAuth(data)
    return data
  }

  async function fetchUserInfo() {
    const response = await fetch('/api/auth/info', {
      headers: { 'Authorization': `Bearer ${token.value}` },
    })
    const json = await response.json()
    if (json.code === 200) {
      userInfo.value = json.data.userInfo
      roles.value = json.data.roles ?? []
      localStorage.setItem(USER_KEY, JSON.stringify(userInfo.value))
    }
  }

  function setAuth(data: LoginResponse) {
    token.value = data.token
    userInfo.value = data.userInfo
    roles.value = data.roles ?? []
    localStorage.setItem(TOKEN_KEY, data.token)
    localStorage.setItem(USER_KEY, JSON.stringify(data.userInfo))
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    roles.value = []
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  return {
    token, userInfo, roles,
    isLoggedIn, roleCode, isStudent, isTeacher, isAdmin, homeRoute,
    login, fetchUserInfo, setAuth, logout,
  }
})
