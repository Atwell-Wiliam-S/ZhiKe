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

const noteContents = [
  '导数的定义是函数增量与自变量增量之比在增量趋于零时的极限。关键要理解 Δy/Δx 的几何意义——割线斜率趋近于切线斜率。',
  '链式法则：若 y = f(g(x))，则 dy/dx = f\'(g(x)) · g\'(x)。外层求导乘内层求导，这个口诀很实用。',
  '不定积分 ∫f(x)dx = F(x) + C，其中 F\'(x) = f(x)。注意常数 C 不能漏掉，因为导数会消去它。',
  '矩阵乘法不满足交换律 AB ≠ BA，但满足结合律 (AB)C = A(BC)。计算时要注意行列对应关系。',
  'Python 中列表推导式 [x**2 for x in range(10)] 比等价的 for 循环更简洁，且通常性能更好。',
  '二分查找的前提是数组有序。时间复杂度 O(log n)，每次排除一半的元素。',
  '特征值 λ 满足 det(A - λI) = 0，特征向量是满足 Ax = λx 的非零向量 x。',
  '快速排序平均时间复杂度 O(n log n)，最坏 O(n²)。选择好的 pivot 可以避免最坏情况。',
  '梯度下降的步长（学习率）太大容易震荡，太小收敛太慢。可以用学习率衰减策略。',
  '拉格朗日乘数法用于求解约束优化问题，将约束条件引入目标函数构造拉格朗日函数。',
  '卷积神经网络中卷积核的感受野决定了每个输出神经元"看到"的输入区域大小。',
  '贝叶斯定理 P(A|B) = P(B|A)P(A)/P(B)，在机器学习中广泛用于分类和参数估计。',
]

const videoMap: Record<number, string> = {
  1: '函数与极限',
  2: '导数与微分',
  3: '积分入门',
  4: '矩阵与行列式',
  5: '线性方程组',
  6: 'Python基础语法',
  7: '控制流与函数',
  8: '排序算法',
  9: '特征值分解',
  10: '梯度下降',
}

export const mockNotes: NoteDTO[] = Array.from({ length: 12 }, (_, i) => ({
  id: i + 1,
  videoId: (i % 5) + 1,
  timestamp: 60 + i * 120,
  content: noteContents[i],
  isPublic: i % 3 !== 0,
  createdAt: new Date(Date.now() - i * 5400000).toISOString(),
}))

// 辅助方法：根据 videoId 获取视频标题
export function getVideoTitle(videoId: number): string {
  return videoMap[videoId] || '未知视频'
}
