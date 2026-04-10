import type { HeatmapDTO, ClassOverviewDTO, StudentProfileDTO, WordCloudDTO } from '@/types/api'

export const mockHeatmap: (videoId: number) => HeatmapDTO = (videoId) => ({
  videoId,
  duration: 3600,
  windowSize: 60,
  data: Array.from({ length: 60 }, (_, i) => ({
    startTime: i * 60,
    endTime: (i + 1) * 60,
    score: 0.3 + Math.random() * 0.6,
    isDifficulty: Math.random() > 0.7,
  })),
})

export const mockClassOverview: ClassOverviewDTO = {
  totalStudents: 45,
  avgCompletionRate: 0.82,
  avgWatchDuration: 1800,
  activeTodayCount: 32,
  totalQuestions: 156,
  difficultyPoints: 12,
}

export const mockStudentProfiles: StudentProfileDTO[] = [
  {
    userId: 1,
    username: 'zhangsan',
    totalWatchDuration: 12000,
    completionRate: 0.95,
    questionCount: 24,
    weakPoints: ['链式法则', '泰勒展开'],
    learningTrend: Array.from({ length: 7 }, (_, i) => ({
      date: new Date(Date.now() - (6 - i) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
      watchDuration: 1500 + Math.floor(Math.random() * 1000),
      questionCount: 2 + Math.floor(Math.random() * 4),
    })),
  },
  {
    userId: 2,
    username: 'lisi',
    totalWatchDuration: 9800,
    completionRate: 0.78,
    questionCount: 18,
    weakPoints: ['矩阵乘法', '行列式'],
    learningTrend: Array.from({ length: 7 }, (_, i) => ({
      date: new Date(Date.now() - (6 - i) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
      watchDuration: 1200 + Math.floor(Math.random() * 800),
      questionCount: 1 + Math.floor(Math.random() * 3),
    })),
  },
]

export const mockWordCloud: (courseId: number) => WordCloudDTO = (courseId) => ({
  courseId,
  words: [
    { text: '导数', count: 128, weight: 0.95 },
    { text: '极限', count: 96, weight: 0.88 },
    { text: '微积分', count: 82, weight: 0.82 },
    { text: '链式法则', count: 68, weight: 0.75 },
    { text: '积分', count: 64, weight: 0.72 },
    { text: '泰勒展开', count: 48, weight: 0.65 },
    { text: '矩阵', count: 42, weight: 0.60 },
    { text: '行列式', count: 36, weight: 0.55 },
    { text: 'Python', count: 32, weight: 0.50 },
    { text: '函数', count: 28, weight: 0.45 },
  ],
})
