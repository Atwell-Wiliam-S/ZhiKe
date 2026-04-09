import { rest } from 'msw'
import type { PlayInfoDTO, FragmentDTO } from '@/types/api'

// Mock 数据
const mockCourses = [
  {
    id: 1,
    title: 'Vue 3 从入门到精通',
    teacherId: 2,
    teacherName: '李老师',
    description: '全面学习 Vue 3 的核心概念和实战技巧',
    coverImage: '/covers/vue3-course.png',
    category: '前端开发',
    isPublished: 1,
    createdAt: '2026-04-01T10:00:00'
  },
  {
    id: 2,
    title: 'Spring Boot 3 企业级应用开发',
    teacherId: 2,
    teacherName: '李老师',
    description: '学习 Spring Boot 3 的企业级应用开发',
    coverImage: '/covers/spring-boot3-course.png',
    category: '后端开发',
    isPublished: 1,
    createdAt: '2026-04-02T10:00:00'
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

const mockPlayInfo: PlayInfoDTO = {
  videoId: 1,
  title: 'Vue 3 基础教程',
  signUrl: 'https://example.com/video/1.mp4',
  duration: 600,
  fragments: mockFragments,
  lastWatchTime: 120
}

// Handlers
export const studentHandlers = [
  // 课程列表
  rest.get('/api/student/courses', (req, res, ctx) => {
    const keyword = req.url.searchParams.get('keyword')
    const courses = keyword 
      ? mockCourses.filter(c => c.title.includes(keyword))
      : mockCourses
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: {
          records: courses,
          total: courses.length,
          size: 10,
          current: 1,
          pages: 1
        },
        timestamp: Date.now()
      })
    )
  }),
  
  // 课程详情
  rest.get('/api/student/courses/:id', (req, res, ctx) => {
    const { id } = req.params
    const course = mockCourses.find(c => c.id === parseInt(id))
    
    if (course) {
      return res(
        ctx.status(200),
        ctx.json({
          code: 200,
          message: '操作成功',
          data: course,
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
  
  // 播放信息
  rest.get('/api/student/videos/:id/play-info', (req, res, ctx) => {
    const { id } = req.params
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: mockPlayInfo,
        timestamp: Date.now()
      })
    )
  }),
  
  // 心跳上报
  rest.post('/api/student/records/heartbeat', (req, res, ctx) => {
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
  
  // 保存进度
  rest.post('/api/student/records/progress', (req, res, ctx) => {
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
  
  // 添加笔记
  rest.post('/api/student/notes', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: 1,
        timestamp: Date.now()
      })
    )
  }),
]
