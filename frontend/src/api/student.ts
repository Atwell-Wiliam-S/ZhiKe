import request from '@/utils/request'
import type { CourseDTO, CourseDetailDTO, PlayInfoDTO, HeartbeatRequest, ProgressRequest, NoteRequest, NoteDTO, PageResult } from '@/types/api'

export const studentApi = {
  getCourses: (params: { page?: number; pageSize?: number }) =>
    request.get<PageResult<CourseDTO>>('/student/courses', { params }),

  getCourseDetail: (courseId: number) =>
    request.get<CourseDetailDTO>(`/student/courses/${courseId}`),

  getPlayInfo: (videoId: number) =>
    request.get<PlayInfoDTO>(`/student/play-info/${videoId}`),

  sendHeartbeat: (data: HeartbeatRequest) =>
    request.post<void>('/student/heartbeat', data),

  updateProgress: (data: ProgressRequest) =>
    request.post<void>('/student/progress', data),

  getNotes: (params: { videoId?: number; page?: number; pageSize?: number }) =>
    request.get<PageResult<NoteDTO>>('/student/notes', { params }),

  createNote: (data: NoteRequest) =>
    request.post<NoteDTO>('/student/notes', data),

  updateNote: (noteId: number, data: { content?: string; isPublic?: boolean }) =>
    request.put<NoteDTO>(`/student/notes/${noteId}`, data),

  deleteNote: (noteId: number) =>
    request.delete<void>(`/student/notes/${noteId}`),
}
