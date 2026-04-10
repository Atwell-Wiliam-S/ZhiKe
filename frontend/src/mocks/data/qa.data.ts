import type { QaLogDTO } from '@/types/api'

export const mockQaHistory: QaLogDTO[] = Array.from({ length: 25 }, (_, i) => ({
  id: i + 1,
  question: ['导数的几何意义是什么？', '如何理解极限的概念？', '链式法则怎么应用？', '积分和导数有什么关系？', '泰勒展开的用途是什么？'][i % 5],
  answer: ['导数的几何意义是函数曲线在某一点处的切线斜率...', '极限描述的是当自变量趋近于某个值时...', '链式法则用于求复合函数的导数...', '积分是导数的逆运算...', '泰勒展开用多项式近似表示函数...'][i % 5],
  contextFragments: [{ fragmentId: (i % 3) + 1, startTime: 120 + i * 10, endTime: 185 + i * 10, relevance: 0.9 - (i % 3) * 0.1 }],
  modelVersion: 'GLM-4',
  tokenUsage: 200 + Math.floor(Math.random() * 300),
  createdAt: new Date(Date.now() - i * 3600_000).toISOString(),
}))
