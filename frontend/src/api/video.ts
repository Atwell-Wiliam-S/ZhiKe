import request from '@/utils/request'
import type { VideoDTO, FragmentDTO, ParseStatusDTO, UploadSignatureDTO, SignUrlDTO, UpdateFragmentRequest, PageResult } from '@/types/api'

export const videoApi = {
  getUploadSignature: (params: { fileName: string; fileSize: number }) =>
    request.post<UploadSignatureDTO>('/video/upload-signature', params),

  createVideo: (data: { courseId: number; title: string; ossUrl: string; duration: number; thumbnailUrl?: string }) =>
    request.post<VideoDTO>('/video', data),

  startParse: (videoId: number) =>
    request.post<ParseStatusDTO>(`/video/${videoId}/parse`),

  getParseStatus: (videoId: number) =>
    request.get<ParseStatusDTO>(`/video/${videoId}/parse-status`),

  getVideo: (videoId: number) =>
    request.get<VideoDTO>(`/video/${videoId}`),

  getVideos: (params: { courseId: number; page?: number; pageSize?: number }) =>
    request.get<PageResult<VideoDTO>>('/video', { params }),

  getSignUrl: (videoId: number) =>
    request.get<SignUrlDTO>(`/video/${videoId}/sign-url`),

  getFragments: (videoId: number) =>
    request.get<FragmentDTO[]>(`/video/${videoId}/fragments`),

  getFragment: (fragmentId: number) =>
    request.get<FragmentDTO>(`/video/fragment/${fragmentId}`),

  updateFragment: (fragmentId: number, data: UpdateFragmentRequest) =>
    request.put<FragmentDTO>(`/video/fragment/${fragmentId}`, data),
}
