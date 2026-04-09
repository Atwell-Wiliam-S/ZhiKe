import { defineStore } from 'pinia'
import type { UserInfoDTO, RoleDTO } from '@/types/api'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null as UserInfoDTO | null,
    roles: [] as RoleDTO[],
    roleCode: localStorage.getItem('roleCode') || ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    isStudent: (state) => state.roleCode === 'STUDENT',
    isTeacher: (state) => state.roleCode === 'TEACHER',
    isAdmin: (state) => state.roleCode === 'ADMIN'
  },
  actions: {
    login(token: string, userInfo: UserInfoDTO, roles: RoleDTO[]) {
      this.token = token
      this.userInfo = userInfo
      this.roles = roles
      this.roleCode = userInfo.roleCode
      
      // 持久化到 localStorage
      localStorage.setItem('token', token)
      localStorage.setItem('roleCode', userInfo.roleCode)
    },
    logout() {
      this.token = ''
      this.userInfo = null
      this.roles = []
      this.roleCode = ''
      
      // 清除 localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('roleCode')
    },
    setUserInfo(userInfo: UserInfoDTO) {
      this.userInfo = userInfo
      this.roleCode = userInfo.roleCode
      localStorage.setItem('roleCode', userInfo.roleCode)
    }
  }
})
