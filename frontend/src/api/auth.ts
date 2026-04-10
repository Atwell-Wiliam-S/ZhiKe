import request from '@/utils/request'
import type { LoginRequest, RegisterRequest, LoginResponse, UserInfoDTO, TokenDTO } from '@/types/api'

export const authApi = {
  login: (data: LoginRequest) =>
    request.post<LoginResponse>('/auth/login', data),

  register: (data: RegisterRequest) =>
    request.post<LoginResponse>('/auth/register', data),

  getUserInfo: () =>
    request.get<UserInfoDTO>('/auth/info'),

  refreshToken: () =>
    request.post<TokenDTO>('/auth/refresh'),
}
