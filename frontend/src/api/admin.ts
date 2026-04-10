import request from '@/utils/request'
import type { CourseDTO, VideoDTO, PageResult, UserInfoDTO } from '@/types/api'

export const adminApi = {
  getCourses: (params: { page?: number; pageSize?: number }) =>
    request.get<PageResult<CourseDTO>>('/admin/courses', { params }),

  createCourse: (data: { title: string; description?: string; coverImage?: string }) =>
    request.post<CourseDTO>('/admin/courses', data),

  updateCourse: (courseId: number, data: { title?: string; description?: string; coverImage?: string; isPublished?: boolean }) =>
    request.put<CourseDTO>(`/admin/courses/${courseId}`, data),

  deleteCourse: (courseId: number) =>
    request.delete<void>(`/admin/courses/${courseId}`),

  getVideos: (params: { courseId?: number; page?: number; pageSize?: number }) =>
    request.get<PageResult<VideoDTO>>('/admin/videos', { params }),

  deleteVideo: (videoId: number) =>
    request.delete<void>(`/admin/videos/${videoId}`),

  getUsers: (params: { page?: number; pageSize?: number; roleCode?: string }) =>
    request.get<PageResult<UserInfoDTO>>('/admin/users', { params }),

  updateUserRole: (userId: number, data: { roleCode: string }) =>
    request.put<UserInfoDTO>(`/admin/users/${userId}/role`, data),
}
