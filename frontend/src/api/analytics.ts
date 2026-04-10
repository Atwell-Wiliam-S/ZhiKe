import request from '@/utils/request'
import type { HeatmapDTO, ClassOverviewDTO, StudentProfileDTO, WordCloudDTO } from '@/types/api'

export const analyticsApi = {
  getHeatmap: (videoId: number) =>
    request.get<HeatmapDTO>(`/analytics/heatmap/${videoId}`),

  getClassOverview: (courseId: number) =>
    request.get<ClassOverviewDTO>(`/analytics/class-overview/${courseId}`),

  getStudentProfile: (userId: number) =>
    request.get<StudentProfileDTO>(`/analytics/student-profile/${userId}`),

  getWordCloud: (courseId: number) =>
    request.get<WordCloudDTO>(`/analytics/word-cloud/${courseId}`),
}
