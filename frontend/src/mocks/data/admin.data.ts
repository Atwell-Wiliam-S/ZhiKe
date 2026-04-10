import type { UserInfoDTO } from '@/types/api'

export const mockAdminUsers: Array<UserInfoDTO & { status: number; createdAt: string }> = [
  { userId: 1, username: 'zhangsan', email: 'zhangsan@edu.cn', avatarUrl: null, roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-01T10:00:00+08:00' },
  { userId: 2, username: 'lisi', email: 'lisi@edu.cn', avatarUrl: '/avatars/lisi.png', roleCode: 'TEACHER', roleName: '教师', status: 1, createdAt: '2024-08-15T14:30:00+08:00' },
  { userId: 3, username: 'wangwu', email: 'wangwu@edu.cn', avatarUrl: null, roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-05T09:00:00+08:00' },
  { userId: 4, username: 'zhaoliu', email: 'zhaoliu@edu.cn', avatarUrl: null, roleCode: 'STUDENT', roleName: '学生', status: 0, createdAt: '2024-09-10T16:00:00+08:00' },
  { userId: 5, username: 'sunqi', email: 'sunqi@edu.cn', avatarUrl: '/avatars/sunqi.png', roleCode: 'TEACHER', roleName: '教师', status: 1, createdAt: '2024-08-20T11:00:00+08:00' },
  { userId: 6, username: 'zhouba', email: 'zhouba@edu.cn', avatarUrl: null, roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-12T08:30:00+08:00' },
  { userId: 7, username: 'wujiu', email: 'wujiu@edu.cn', avatarUrl: '/avatars/wujiu.png', roleCode: 'STUDENT', roleName: '学生', status: 1, createdAt: '2024-09-15T10:00:00+08:00' },
  { userId: 8, username: 'zhengshi', email: 'zhengshi@edu.cn', avatarUrl: null, roleCode: 'TEACHER', roleName: '教师', status: 0, createdAt: '2024-08-10T15:00:00+08:00' },
]
