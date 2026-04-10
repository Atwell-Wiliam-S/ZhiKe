import type { VideoDTO, FragmentDTO, ParseStatusDTO, UploadSignatureDTO, SignUrlDTO } from '@/types/api'

export const mockVideos: VideoDTO[] = [
  { id: 1, courseId: 1, title: '微积分基础：导数入门', ossUrl: 'https://oss.example.com/videos/calc-1.mp4', duration: 3600, parseStatus: 3, parseStatusDesc: '解析完成', thumbnailUrl: 'https://oss.example.com/thumbnails/calc-1.jpg', orderNum: 1, createdAt: '2024-09-01T10:00:00+08:00' },
  { id: 2, courseId: 1, title: '求导法则详解', ossUrl: 'https://oss.example.com/videos/calc-2.mp4', duration: 2800, parseStatus: 3, parseStatusDesc: '解析完成', thumbnailUrl: 'https://oss.example.com/thumbnails/calc-2.jpg', orderNum: 2, createdAt: '2024-09-02T14:30:00+08:00' },
  { id: 3, courseId: 1, title: '链式法则与复合函数', ossUrl: 'https://oss.example.com/videos/calc-3.mp4', duration: 3200, parseStatus: 2, parseStatusDesc: '解析中', thumbnailUrl: null, orderNum: 3, createdAt: '2024-09-03T09:00:00+08:00' },
  { id: 4, courseId: 2, title: '线性代数：矩阵基础', ossUrl: 'https://oss.example.com/videos/linear-1.mp4', duration: 2400, parseStatus: 3, parseStatusDesc: '解析完成', thumbnailUrl: 'https://oss.example.com/thumbnails/linear-1.jpg', orderNum: 1, createdAt: '2024-08-25T16:00:00+08:00' },
  { id: 5, courseId: 2, title: '矩阵乘法与行列式', ossUrl: 'https://oss.example.com/videos/linear-2.mp4', duration: 3000, parseStatus: 1, parseStatusDesc: '待解析', thumbnailUrl: null, orderNum: 2, createdAt: '2024-08-26T11:00:00+08:00' },
  { id: 6, courseId: 3, title: 'Python入门：变量与类型', ossUrl: 'https://oss.example.com/videos/python-1.mp4', duration: 1800, parseStatus: 3, parseStatusDesc: '解析完成', thumbnailUrl: 'https://oss.example.com/thumbnails/python-1.jpg', orderNum: 1, createdAt: '2024-09-05T10:00:00+08:00' },
  { id: 7, courseId: 3, title: '条件语句与循环', ossUrl: 'https://oss.example.com/videos/python-2.mp4', duration: 2200, parseStatus: 3, parseStatusDesc: '解析完成', thumbnailUrl: 'https://oss.example.com/thumbnails/python-2.jpg', orderNum: 2, createdAt: '2024-09-06T14:30:00+08:00' },
  { id: 8, courseId: 3, title: '函数与模块', ossUrl: 'https://oss.example.com/videos/python-3.mp4', duration: 2600, parseStatus: 0, parseStatusDesc: '上传中', thumbnailUrl: null, orderNum: 3, createdAt: '2024-09-07T09:00:00+08:00' },
]

export const mockFragments: FragmentDTO[] = []
for (let v = 1; v <= 8; v++) {
  for (let f = 1; f <= 3; f++) {
    mockFragments.push({
      id: (v - 1) * 3 + f,
      videoId: v,
      startTime: 120 * (f - 1),
      endTime: 120 * f,
      contentText: `视频${v}片段${f}的融合文本内容，包含相关知识点讲解...`,
      keyframeUrl: v % 2 === 0 ? `https://oss.example.com/keyframes/v${v}-f${f}.jpg` : null,
      asrText: v % 3 !== 0 ? `视频${v}片段${f}的ASR识别文本...` : null,
      visualTags: f % 2 === 0 ? [{ tag: '公式', confidence: 0.95 }, { tag: '文字', confidence: 0.88 }] : null,
      summary: f % 3 !== 0 ? `片段${f}的摘要总结` : null,
      keywords: f % 2 === 0 ? ['导数', '极限', '微积分'] : null,
      vectorId: v % 4 !== 0 ? `vec-${v}-${f}` : null,
    })
  }
}

export const mockParseStatus: ParseStatusDTO[] = mockVideos.map(v => ({
  videoId: v.id,
  parseStatus: v.parseStatus,
  parseStatusDesc: v.parseStatusDesc || '',
  fragmentCount: v.parseStatus === 3 ? 3 : v.parseStatus === 2 ? 2 : v.parseStatus === 1 ? 1 : 0,
  duration: v.duration,
}))

export const mockUploadSignature: UploadSignatureDTO = {
  uploadUrl: 'https://oss.example.com/upload?signature=xxx',
  signUrl: 'https://oss.example.com/video/temp.mp4?signature=yyy',
  uploadId: 'upload-123456',
}

export const mockSignUrl: (videoId: number) => SignUrlDTO = (videoId) => ({
  videoId,
  signUrl: `https://oss.example.com/videos/video-${videoId}.mp4?signature=abc123&expires=1728000000`,
  expiresAt: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
})
