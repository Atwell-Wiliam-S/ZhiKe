import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
// import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { convertKeysToSnakeCase, convertKeysToCamelCase } from './snake-case'

/** 创建 Axios 实例 */
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api',
  timeout: 15_000,
  headers: { 'Content-Type': 'application/json' },
})

// ─── 请求拦截器 ───
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('zhike-token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    if (config.params) {
      config.params = convertKeysToSnakeCase(config.params)
    }

    if (config.data) {
      config.data = convertKeysToSnakeCase(config.data)
    }

    return config
  },
  (error) => Promise.reject(error)
)

// ─── 响应拦截器 ───
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const convertedData = convertKeysToCamelCase(response.data)
    const { code, message, data } = convertedData

    if (code === 200) {
      return data  // 直接返回业务数据，剥离外层包装
    }

    // 业务错误
    ElMessage.error(message ?? '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    if (error.response) {
      const { status } = error.response

      switch (status) {
        case 401:
          // Token 过期，自动登出
          // const userStore = useUserStore()
          // userStore.logout()
          window.location.href = '/login'
          ElMessage.error('登录已过期，请重新登录')
          break
        case 403:
          ElMessage.error('无权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(error.response.data?.message ?? '请求失败')
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络')
    } else {
      ElMessage.error('网络连接失败')
    }

    return Promise.reject(error)
  }
)

export default request