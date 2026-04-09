import { http } from 'msw'
import type { HeatmapDTO, ClassOverviewDTO } from '@/types/api'

// Mock 数据
const mockHeatmap: HeatmapDTO = {
  videoId: 1,
  duration: 600,
  windowSize: 30,
  data: [
    { startTime: 0, endTime: 30, score: 0.2, isDifficulty: false },
    { startTime: 30, endTime: 60, score: 0.8, isDifficulty: true },
    { startTime: 60, endTime: 90, score: 0.3, isDifficulty: false },
    { startTime: 90, endTime: 120, score: 0.6, isDifficulty: false },
    { startTime: 120, endTime: 150, score: 0.9, isDifficulty: true },
    { startTime: 150, endTime: 180, score: 0.4, isDifficulty: false },
    { startTime: 180, endTime: 210, score: 0.5, isDifficulty: false },
    { startTime: 210, endTime: 240, score: 0.7, isDifficulty: true },
    { startTime: 240, endTime: 270, score: 0.3, isDifficulty: false },
    { startTime: 270, endTime: 300, score: 0.4, isDifficulty: false },
    { startTime: 300, endTime: 330, score: 0.6, isDifficulty: false },
    { startTime: 330, endTime: 360, score: 0.8, isDifficulty: true },
    { startTime: 360, endTime: 390, score: 0.3, isDifficulty: false },
    { startTime: 390, endTime: 420, score: 0.4, isDifficulty: false },
    { startTime: 420, endTime: 450, score: 0.5, isDifficulty: false },
    { startTime: 450, endTime: 480, score: 0.7, isDifficulty: true },
    { startTime: 480, endTime: 510, score: 0.3, isDifficulty: false },
    { startTime: 510, endTime: 540, score: 0.4, isDifficulty: false },
    { startTime: 540, endTime: 570, score: 0.6, isDifficulty: false },
    { startTime: 570, endTime: 600, score: 0.2, isDifficulty: false }
  ]
}

const mockClassOverview: ClassOverviewDTO = {
  totalStudents: 100,
  avgCompletionRate: 75.5,
  avgWatchDuration: 1200,
  activeTodayCount: 50,
  totalQuestions: 200
}

// Handlers
export const analyticsHandlers = [
  // 难点热力图
  http.get('/api/analytics/heatmap', (req, res, ctx) => {
    const { courseId, videoId } = req.url.searchParams
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: mockHeatmap,
        timestamp: Date.now()
      })
    )
  }),
  
  // 班级学情概览
  http.get('/api/analytics/class-overview', (req, res, ctx) => {
    const { courseId } = req.url.searchParams
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: mockClassOverview,
        timestamp: Date.now()
      })
    )
  }),
  
  // 学生学习画像
  http.get('/api/analytics/student-profile', (req, res, ctx) => {
    const { studentId } = req.url.searchParams
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: {
          studentId: parseInt(studentId || '1'),
          username: 'zhangsan',
          avgCompletionRate: 85.0,
          avgWatchDuration: 1500,
          totalQuestions: 50,
          weakPoints: ['Composition API', '响应式原理'],
          strongPoints: ['基础语法', '组件通信']
        },
        timestamp: Date.now()
      })
    )
  }),
  
  // 高频困惑词云
  http.get('/api/analytics/wordcloud', (req, res, ctx) => {
    const { courseId } = req.url.searchParams
    
    return res(
      ctx.status(200),
      ctx.json({
        code: 200,
        message: '操作成功',
        data: {
          words: [
            { text: 'Composition API', value: 100 },
            { text: '响应式原理', value: 80 },
            { text: '生命周期', value: 60 },
            { text: '组件通信', value: 40 },
            { text: '路由', value: 30 },
            { text: '状态管理', value: 20 }
          ]
        },
        timestamp: Date.now()
      })
    )
  }),
]
