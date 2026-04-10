import type { LoginResponse, RoleDTO } from '@/types/api'

export const mockRoles: RoleDTO[] = [
  { roleCode: 'STUDENT', roleName: '学生', description: '可观看视频、提问' },
  { roleCode: 'TEACHER', roleName: '教师', description: '可管理课程' },
  { roleCode: 'ADMIN',   roleName: '管理员', description: '系统管理' },
]

export const mockUsers: Record<string, LoginResponse> = {
  student: {
    token: 'mock-jwt-student-token-xxx',
    userInfo: { userId: 1, username: 'zhangsan', email: 'zhangsan@edu.cn', avatarUrl: null, roleCode: 'STUDENT', roleName: '学生' },
    roles: [mockRoles[0]],
  },
  teacher: {
    token: 'mock-jwt-teacher-token-xxx',
    userInfo: { userId: 2, username: 'lisi', email: 'lisi@edu.cn', avatarUrl: '/avatars/lisi.png', roleCode: 'TEACHER', roleName: '教师' },
    roles: [mockRoles[1]],
  },
  admin: {
    token: 'mock-jwt-admin-token-xxx',
    userInfo: { userId: 3, username: 'admin', email: 'admin@edu.cn', avatarUrl: null, roleCode: 'ADMIN', roleName: '管理员' },
    roles: [mockRoles[2]],
  },
}

export const mockAdminUsers = [
  { userId: 1, username: 'zhangsan', email: 'zhangsan@edu.cn', roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-01T10:00:00+08:00' },
  { userId: 2, username: 'lisi', email: 'lisi@edu.cn', roleCode: 'TEACHER', roleName: '教师', status: 1, createdAt: '2024-08-15T14:30:00+08:00' },
  { userId: 3, username: 'wangwu', email: 'wangwu@edu.cn', roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-05T09:00:00+08:00' },
  { userId: 4, username: 'zhaoliu', email: 'zhaoliu@edu.cn', roleCode: 'STUDENT', roleName: '学生', status: 0, createdAt: '2024-09-10T16:00:00+08:00' },
  { userId: 5, username: 'sunqi', email: 'sunqi@edu.cn', roleCode: 'TEACHER', roleName: '教师', status: 1, createdAt: '2024-08-20T11:00:00+08:00' },
]
