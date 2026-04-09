import { http } from 'msw';
import type { LoginRequest, LoginResponse, UserInfoDTO, RoleDTO } from '@/types/api';

// Mock 数据
const mockRoles: RoleDTO[] = [
  { roleCode: 'STUDENT', roleName: '学生', description: '可观看视频、提问' },
  { roleCode: 'TEACHER', roleName: '教师', description: '可管理课程' },
  { roleCode: 'ADMIN', roleName: '管理员', description: '系统管理' },
]

const mockUsers: Record<string, LoginResponse> = {
  student: {
    token: 'mock-jwt-student-token-xxx',
    userInfo: { 
      userId: 1, 
      username: 'zhangsan', 
      email: 'zhangsan@edu.cn',
      avatarUrl: null, 
      roleCode: 'STUDENT', 
      roleName: '学生' 
    },
    roles: [mockRoles[0]],
  },
  teacher: {
    token: 'mock-jwt-teacher-token-xxx',
    userInfo: { 
      userId: 2, 
      username: 'lisi', 
      email: 'lisi@edu.cn',
      avatarUrl: '/avatars/lisi.png', 
      roleCode: 'TEACHER', 
      roleName: '教师' 
    },
    roles: [mockRoles[1]],
  },
  admin: {
    token: 'mock-jwt-admin-token-xxx',
    userInfo: { 
      userId: 3, 
      username: 'admin', 
      email: 'admin@edu.cn',
      avatarUrl: '/avatars/admin.png', 
      roleCode: 'ADMIN', 
      roleName: '管理员' 
    },
    roles: [mockRoles[2]],
  },
}

// Handlers
export const authHandlers = [
  // 登录
  http.post('/api/auth/login', (req, res, ctx) => {
    const { username, password } = req.body as LoginRequest
    
    // 简单的用户名验证
    if (mockUsers[username]) {
      return res(
        ctx.status(200),
        ctx.json({
          code: 200,
          message: '操作成功',
          data: mockUsers[username],
          timestamp: Date.now()
        })
      )
    }
    
    return res(
      ctx.status(401),
      ctx.json({
        code: 401,
        message: '用户名或密码错误',
        data: null,
        timestamp: Date.now()
      })
    )
  }),
  
  // 获取用户信息
  http.get('/api/auth/info', (req, res, ctx) => {
    const token = req.headers.get('Authorization')?.replace('Bearer ', '')
    
    // 根据 token 模拟获取用户信息
    let userInfo: UserInfoDTO | null = null
    let roles: RoleDTO[] = []
    
    if (token === 'mock-jwt-student-token-xxx') {
      userInfo = mockUsers.student.userInfo
      roles = mockUsers.student.roles
    } else if (token === 'mock-jwt-teacher-token-xxx') {
      userInfo = mockUsers.teacher.userInfo
      roles = mockUsers.teacher.roles
    } else if (token === 'mock-jwt-admin-token-xxx') {
      userInfo = mockUsers.admin.userInfo
      roles = mockUsers.admin.roles
    }
    
    if (userInfo) {
      return res(
        ctx.status(200),
        ctx.json({
          code: 200,
          message: '操作成功',
          data: userInfo,
          timestamp: Date.now()
        })
      )
    }
    
    return res(
      ctx.status(401),
      ctx.json({
        code: 401,
        message: '未认证',
        data: null,
        timestamp: Date.now()
      })
    )
  }),
  
  // 登出
  http.post('/api/auth/logout', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: null,
        timestamp: Date.now()
      })
    )
  }),
]
