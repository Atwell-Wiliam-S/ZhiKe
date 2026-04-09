import { rest } from 'msw'
import type { VideoDTO, FragmentDTO } from '@/types/api'

// Mock 数据
const mockVideos: VideoDTO[] = [
  {
    id: 1,
    courseId: 1,
    title: 'Vue 3 基础教程',
    duration: 600,
    parseStatus: 2,
    thumbnailUrl: '/thumbnails/vue3-basic.png',
    orderNum: 1,
    createdAt: '2026-04-01T10:00:00'
  },
  {
    id: 2,
    courseId: 1,
    title: 'Vue 3 Composition API',
    duration: 900,
    parseStatus: 2,
    thumbnailUrl: '/thumbnails/vue3-composition.png',
    orderNum: 2,
    createdAt: '2026-04-02T10:00:00'
  },
  {
    id: 3,
    courseId: 2,
    title: 'Spring Boot 3 入门',
    duration: 1200,
    parseStatus: 2,
    thumbnailUrl: '/thumbnails/spring-boot3.png',
    orderNum: 1,
    createdAt: '2026-04-03T10:00:00'
  }
]

const mockFragments: FragmentDTO[] = [
  {
    id: 1,
    videoId: 1,
    startTime: 0,
    endTime: 60,
    contentText: 'Vue 3 简介，包括新特性和改进',
    summary: 'Vue 3 新特性介绍',
    keywords: ['Vue 3', '新特性', 'Composition API'],
    vectorId: 'vec1'
  },
  {
    id: 2,
    videoId: 1,
    startTime: 60,
    endTime: 120,
    contentText: 'Vue 3 的安装和环境搭建',
    summary: 'Vue 3 环境搭建',
    keywords: ['安装', '环境搭建', 'Vue CLI'],
    vectorId: 'vec2'
  }
]

// Handlers
export const videoHandlers = [
  // 教师视频列表
  rest.get('/api/teacher/videos', (req, res, ctx) => {
    const courseId = req.url.searchParams.get('courseId')
    const videos = courseId 
      ? mockVideos.filter(v => v.courseId === parseInt(courseId))
      : mockVideos
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: {
          records: videos,
          total: videos.length,
          size: 10,
          current: 1,
          pages: 1
        },
        timestamp: Date.now()
      })
    )
  }),
  
  // 知识片段列表
  rest.get('/api/teacher/fragments', (req, res, ctx) => {
    const videoId = req.url.searchParams.get('videoId')
    const fragments = videoId 
      ? mockFragments.filter(f => f.videoId === parseInt(videoId))
      : mockFragments
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: fragments,
        timestamp: Date.now()
      })
    )
  }),
  
  // 获取视频签名URL
  rest.get('/api/video/:id/sign-url', (req, res, ctx) => {
    const { id } = req.params
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: {
          signUrl: `https://example.com/video/${id}.mp4`
        },
        timestamp: Date.now()
      })
    )
  }),
  
  // 视频解析状态
  rest.get('/api/video/:id/status', (req, res, ctx) => {
    const { id } = req.params
    const video = mockVideos.find(v => v.id === parseInt(id))
    
    if (video) {
      return res(
        ctx.status(200),
        ctx.json({
          code: 200,
          message: '操作成功',
          data: {
            parseStatus: video.parseStatus
          },
          timestamp: Date.now()
        })
      )
    }
    
    return res(
      ctx.status(404),
      ctx.json({
        code: 404,
        message: '资源不存在',
        data: null,
        timestamp: Date.now()
      })
    )
  }),
]
