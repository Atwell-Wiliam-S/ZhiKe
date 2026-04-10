import type { CourseDTO, CourseDetailDTO, PlayInfoDTO, NoteDTO } from '@/types/api'
import { mockVideos, mockFragments } from './video.data'

export const mockCourses: CourseDTO[] = [
  { id: 1, title: '高等数学基础', teacherName: '张教授', description: '微积分与线性代数入门课程', coverImage: 'https://oss.example.com/covers/math.jpg', videoCount: 3, publishedAt: '2024-09-01T00:00:00+08:00' },
  { id: 2, title: '线性代数精讲', teacherName: '李教授', description: '矩阵、行列式与线性变换', coverImage: 'https://oss.example.com/covers/linear.jpg', videoCount: 2, publishedAt: '2024-08-25T00:00:00+08:00' },
  { id: 3, title: 'Python编程入门', teacherName: '王老师', description: '从零基础到Python实战', coverImage: 'https://oss.example.com/covers/python.jpg', videoCount: 3, publishedAt: '2024-09-05T00:00:00+08:00' },
  { id: 4, title: '数据结构与算法', teacherName: '赵教授', description: '核心算法思想与实现', coverImage: null, videoCount: 5, publishedAt: '2024-08-20T00:00:00+08:00' },
  { id: 5, title: '机器学习基础', teacherName: '陈教授', description: '机器学习原理与实践', coverImage: 'https://oss.example.com/covers/ml.jpg', videoCount: 4, publishedAt: '2024-09-10T00:00:00+08:00' },
]

export const mockCourseDetails: CourseDetailDTO[] = mockCourses.map(c => ({
  ...c,
  videos: mockVideos.filter(v => v.courseId === c.id),
}))

export const mockPlayInfo: (videoId: number) => PlayInfoDTO = (videoId) => {
  const video = mockVideos.find(v => v.id === videoId)
  const fragments = mockFragments.filter(f => f.videoId === videoId)
  return {
    videoId,
    title: video?.title || '未知视频',
    signUrl: `https://oss.example.com/videos/video-${videoId}.mp4?signature=abc123`,
    duration: video?.duration || 3600,
    fragments,
    lastWatchTime: videoId % 3 === 0 ? 600 : null,
  }
}

export const mockNotes: NoteDTO[] = Array.from({ length: 12 }, (_, i) => ({
  id: i + 1,
  videoId: (i % 3) + 1,
  timestamp: 120 * (i % 5),
  content: `这是一条关于视频${(i % 3) + 1}的笔记，记录了在${120 * (i % 5)}秒处的重要知识点...`,
  isPublic: i % 2 === 0,
  createdAt: new Date(Date.now() - i * 3600_000).toISOString(),
}))
