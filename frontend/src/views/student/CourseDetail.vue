<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Star, ChevronDown, ChevronUp, Play, Check, Lock } from 'lucide-vue-next'

const route = useRoute()
const courseId = computed(() => route.params.courseId as string)

// 模拟课程数据
const course = ref({
  id: courseId.value,
  title: '高等数学：微积分与线性代数',
  teacher: {
    name: '张教授',
    title: '数学博士',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20teacher%20avatar%20portrait&image_size=square_hd',
    courses: 12
  },
  cover: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mathematics%20education%20classroom%20scene&image_size=landscape_16_9',
  rating: 4.8,
  reviewCount: 156,
  videoCount: 24,
  duration: '12 小时',
  studentCount: 156,
  description: '本课程涵盖微积分基础、线性代数等高等数学核心内容，通过深入浅出的讲解帮助学生掌握数学思维方法。课程包含大量实例和练习，适合大学新生和需要复习数学的学生。',
  chapters: [
    {
      id: 1,
      title: '微积分基础',
      videoCount: 3,
      duration: '2 小时 15 分',
      videos: [
        { id: 1, title: '函数与极限', duration: '45:00', status: 'completed' },
        { id: 2, title: '导数与微分', duration: '50:00', status: 'learning' },
        { id: 3, title: '积分入门', duration: '40:00', status: 'locked' }
      ]
    },
    {
      id: 2,
      title: '线性代数',
      videoCount: 4,
      duration: '3 小时 20 分',
      videos: [
        { id: 4, title: '矩阵与行列式', duration: '55:00', status: 'locked' },
        { id: 5, title: '线性方程组', duration: '45:00', status: 'locked' },
        { id: 6, title: '特征值与特征向量', duration: '50:00', status: 'locked' },
        { id: 7, title: '二次型', duration: '50:00', status: 'locked' }
      ]
    }
  ],
  progress: 25
})

// 状态管理
const activeTab = ref('overview')
const expandedChapters = ref<number[]>([1])
const showFullDescription = ref(false)

// 计算属性
const descriptionDisplay = computed(() => {
  if (showFullDescription.value) return course.value.description
  return course.value.description.slice(0, 150) + '...'
})

// 方法
const toggleChapter = (chapterId: number) => {
  const index = expandedChapters.value.indexOf(chapterId)
  if (index > -1) {
    expandedChapters.value.splice(index, 1)
  } else {
    expandedChapters.value.push(chapterId)
  }
}

const getVideoStatusIcon = (status: string) => {
  switch (status) {
    case 'completed': return Check
    case 'learning': return Play
    case 'locked': return Lock
    default: return null
  }
}

const getVideoStatusClass = (status: string) => {
  switch (status) {
    case 'completed': return 'text-success'
    case 'learning': return 'text-accent'
    case 'locked': return 'text-tertiary'
    default: return ''
  }
}

const getVideoStatusLabel = (status: string) => {
  switch (status) {
    case 'completed': return '已完成'
    case 'learning': return '正在学习'
    case 'locked': return '未解锁'
    default: return ''
  }
}
</script>

<template>
  <div class="course-detail">
    <!-- 面包屑 -->
    <nav aria-label="面包屑" class="breadcrumb">
      <ol class="breadcrumb-list">
        <li class="breadcrumb-item">
          <a href="/student/home" class="breadcrumb-link">首页</a>
        </li>
        <li class="breadcrumb-item">
          <span class="breadcrumb-current">{{ course.title }}</span>
        </li>
      </ol>
    </nav>

    <!-- Hero 区域 -->
    <section class="hero">
      <div class="hero-container">
        <div class="hero-image">
          <img :src="course.cover" :alt="course.title" class="cover-image">
        </div>
        <div class="hero-content">
          <h1 class="course-title">{{ course.title }}</h1>
          <div class="teacher-info">
            <img :src="course.teacher.avatar" :alt="course.teacher.name" class="teacher-avatar">
            <div class="teacher-details">
              <span class="teacher-name">{{ course.teacher.name }}</span>
              <span class="teacher-title">{{ course.teacher.title }}</span>
            </div>
          </div>
          <div class="course-stats">
            <div class="rating">
              <Star class="star-icon" :size="16" />
              <span class="rating-value">{{ course.rating }}</span>
              <span class="review-count">({{ course.reviewCount }} 评价)</span>
            </div>
            <div class="stats">
              <span class="stat-item">{{ course.videoCount }} 个视频</span>
              <span class="stat-separator">·</span>
              <span class="stat-item">{{ course.duration }}</span>
              <span class="stat-separator">·</span>
              <span class="stat-item">{{ course.studentCount }} 名学生</span>
            </div>
          </div>
          <div class="course-description">
            <p class="description-text">{{ descriptionDisplay }}</p>
            <button v-if="!showFullDescription" @click="showFullDescription = true" class="expand-link">
              展开全部
            </button>
          </div>
          <button class="start-button" :aria-label="`开始学习：${course.title}`">
            开始学习
          </button>
        </div>
      </div>
    </section>

    <!-- 主要内容区 -->
    <div class="main-content">
      <!-- 左侧内容 -->
      <div class="content-left">
        <!-- Tab 导航 -->
        <div role="tablist" class="tab-navigation">
          <button
            role="tab"
            :aria-selected="activeTab === 'overview'"
            @click="activeTab = 'overview'"
            class="tab-button"
          >
            课程概览
          </button>
          <button
            role="tab"
            :aria-selected="activeTab === 'catalog'"
            @click="activeTab = 'catalog'"
            class="tab-button"
          >
            课程目录
          </button>
          <button
            role="tab"
            :aria-selected="activeTab === 'reviews'"
            @click="activeTab = 'reviews'"
            class="tab-button"
          >
            学员评价
          </button>
        </div>

        <!-- Tab 内容 -->
        <div class="tab-content">
          <!-- 课程概览 -->
          <div v-if="activeTab === 'overview'" role="tabpanel" class="tab-panel">
            <div class="overview-content">
              <h2 class="section-title">课程介绍</h2>
              <p class="section-text">{{ course.description }}</p>
              <h2 class="section-title">学习目标</h2>
              <ul class="goal-list">
                <li class="goal-item">掌握微积分基本概念和计算方法</li>
                <li class="goal-item">理解线性代数的核心原理</li>
                <li class="goal-item">培养数学思维和解决问题的能力</li>
                <li class="goal-item">为后续专业课程打下坚实基础</li>
              </ul>
            </div>
          </div>

          <!-- 课程目录 -->
          <div v-if="activeTab === 'catalog'" role="tabpanel" class="tab-panel">
            <div class="catalog-content">
              <details
                v-for="chapter in course.chapters"
                :key="chapter.id"
                :open="expandedChapters.includes(chapter.id)"
                class="chapter-details"
              >
                <summary class="chapter-summary" @click="toggleChapter(chapter.id)">
                  <div class="chapter-info">
                    <span class="chapter-title">{{ chapter.title }}</span>
                    <span class="chapter-meta">{{ chapter.videoCount }} 个视频，{{ chapter.duration }}</span>
                  </div>
                  <div class="chapter-toggle">
                    <ChevronDown v-if="expandedChapters.includes(chapter.id)" :size="16" />
                    <ChevronUp v-else :size="16" />
                  </div>
                </summary>
                <div class="chapter-videos">
                  <div
                    v-for="video in chapter.videos"
                    :key="video.id"
                    :class="['video-item', { 'learning': video.status === 'learning' }]"
                  >
                    <div class="video-info">
                      <component
                        :is="getVideoStatusIcon(video.status)"
                        :class="['status-icon', getVideoStatusClass(video.status)]"
                        :aria-label="getVideoStatusLabel(video.status)"
                        :size="16"
                      />
                      <span class="video-title">{{ video.title }}</span>
                    </div>
                    <span class="video-duration">{{ video.duration }}</span>
                  </div>
                </div>
              </details>
            </div>
          </div>

          <!-- 学员评价 -->
          <div v-if="activeTab === 'reviews'" role="tabpanel" class="tab-panel">
            <div class="reviews-content">
              <div class="rating-summary">
                <div class="rating-overall">
                  <span class="rating-number">{{ course.rating }}</span>
                  <div class="rating-stars">
                    <Star class="star-icon filled" :size="16" />
                    <Star class="star-icon filled" :size="16" />
                    <Star class="star-icon filled" :size="16" />
                    <Star class="star-icon filled" :size="16" />
                    <Star class="star-icon half-filled" :size="16" />
                  </div>
                  <span class="rating-count">{{ course.reviewCount }} 条评价</span>
                </div>
                <div class="rating-distribution">
                  <div class="rating-bar">
                    <span class="rating-label">5 星</span>
                    <div class="bar-container">
                      <div class="bar-fill" style="width: 80%"></div>
                    </div>
                    <span class="rating-percentage">80%</span>
                  </div>
                  <div class="rating-bar">
                    <span class="rating-label">4 星</span>
                    <div class="bar-container">
                      <div class="bar-fill" style="width: 15%"></div>
                    </div>
                    <span class="rating-percentage">15%</span>
                  </div>
                  <div class="rating-bar">
                    <span class="rating-label">3 星</span>
                    <div class="bar-container">
                      <div class="bar-fill" style="width: 5%"></div>
                    </div>
                    <span class="rating-percentage">5%</span>
                  </div>
                  <div class="rating-bar">
                    <span class="rating-label">2 星</span>
                    <div class="bar-container">
                      <div class="bar-fill" style="width: 0%"></div>
                    </div>
                    <span class="rating-percentage">0%</span>
                  </div>
                  <div class="rating-bar">
                    <span class="rating-label">1 星</span>
                    <div class="bar-container">
                      <div class="bar-fill" style="width: 0%"></div>
                    </div>
                    <span class="rating-percentage">0%</span>
                  </div>
                </div>
              </div>
              <div class="review-list">
                <div class="review-item">
                  <div class="review-header">
                    <span class="reviewer-name">学生 A</span>
                    <div class="review-rating">
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                    </div>
                    <span class="review-date">2024-01-15</span>
                  </div>
                  <p class="review-content">老师讲解非常清晰，课程内容安排合理，通过大量实例帮助理解抽象概念。练习题也很有针对性，对我的学习帮助很大。</p>
                </div>
                <div class="review-item">
                  <div class="review-header">
                    <span class="reviewer-name">学生 B</span>
                    <div class="review-rating">
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon" :size="14" />
                    </div>
                    <span class="review-date">2024-01-10</span>
                  </div>
                  <p class="review-content">课程质量很高，老师的教学方法很有效，特别是对于难点的讲解非常到位。唯一的建议是希望能增加更多的习题讲解。</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧边栏 -->
      <div class="content-right">
        <!-- 课程统计卡片 -->
        <div class="sidebar-card">
          <h3 class="card-title">学习进度</h3>
          <div class="progress-container">
            <div class="progress-circle">
              <svg class="progress-svg" width="120" height="120" viewBox="0 0 120 120">
                <circle
                  class="progress-bg"
                  cx="60"
                  cy="60"
                  r="50"
                  fill="none"
                  stroke="var(--color-border)"
                  stroke-width="8"
                />
                <circle
                  class="progress-bar"
                  cx="60"
                  cy="60"
                  r="50"
                  fill="none"
                  stroke="var(--color-accent)"
                  stroke-width="8"
                  stroke-linecap="round"
                  :stroke-dasharray="314"
                  :stroke-dashoffset="314 * (1 - course.progress / 100)"
                  transform="rotate(-90 60 60)"
                />
              </svg>
              <div class="progress-text">{{ course.progress }}%</div>
            </div>
            <div class="progress-details">
              <span class="progress-label">已学习</span>
              <span class="progress-value">3 小时</span>
              <span class="progress-separator">/</span>
              <span class="progress-value">{{ course.duration }}</span>
            </div>
          </div>
        </div>

        <!-- 教师简介卡片 -->
        <div class="sidebar-card">
          <h3 class="card-title">教师简介</h3>
          <div class="teacher-card">
            <img :src="course.teacher.avatar" :alt="course.teacher.name" class="teacher-avatar">
            <div class="teacher-info">
              <span class="teacher-name">{{ course.teacher.name }}</span>
              <span class="teacher-title">{{ course.teacher.title }}</span>
              <span class="teacher-courses">{{ course.teacher.courses }} 门课程</span>
            </div>
          </div>
        </div>

        <!-- 相关课程推荐 -->
        <div class="sidebar-card">
          <h3 class="card-title">相关课程</h3>
          <div class="related-courses">
            <div class="related-course">
              <img 
                src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=physics%20education%20classroom&image_size=landscape_4_3" 
                alt="大学物理" 
                class="related-course-image"
              >
              <div class="related-course-info">
                <span class="related-course-title">大学物理</span>
                <div class="related-course-meta">
                  <Star class="star-icon small" :size="12" />
                  <span class="related-course-rating">4.9</span>
                </div>
              </div>
            </div>
            <div class="related-course">
              <img 
                src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=computer%20science%20coding%20classroom&image_size=landscape_4_3" 
                alt="数据结构与算法" 
                class="related-course-image"
              >
              <div class="related-course-info">
                <span class="related-course-title">数据结构与算法</span>
                <div class="related-course-meta">
                  <Star class="star-icon small" :size="12" />
                  <span class="related-course-rating">4.7</span>
                </div>
              </div>
            </div>
            <div class="related-course">
              <img 
                src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=statistics%20data%20analysis%20classroom&image_size=landscape_4_3" 
                alt="概率论与数理统计" 
                class="related-course-image"
              >
              <div class="related-course-info">
                <span class="related-course-title">概率论与数理统计</span>
                <div class="related-course-meta">
                  <Star class="star-icon small" :size="12" />
                  <span class="related-course-rating">4.6</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.course-detail {
  min-height: 100vh;
  background: var(--color-bg-canvas);
  color: var(--color-text-primary);
}

/* 面包屑 */
.breadcrumb {
  padding: var(--space-6) var(--space-6) 0;
  max-width: var(--container-2xl);
  margin: 0 auto;
}

.breadcrumb-list {
  display: flex;
  list-style: none;
  gap: var(--space-2);
  padding: 0;
  margin: 0;
}

.breadcrumb-link {
  color: var(--color-text-secondary);
  text-decoration: none;
  font-size: var(--text-sm);
  transition: color var(--duration-normal) var(--ease-default);
}

.breadcrumb-link:hover {
  color: var(--color-accent);
}

.breadcrumb-current {
  color: var(--color-text-primary);
  font-size: var(--text-sm);
}

/* Hero 区域 */
.hero {
  padding: var(--space-8) var(--space-6);
  max-width: var(--container-2xl);
  margin: 0 auto;
}

.hero-container {
  display: grid;
  grid-template-columns: 40% 60%;
  gap: var(--space-8);
  align-items: start;
}

.hero-image {
  position: relative;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-md);
  aspect-ratio: 16/9;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.course-title {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
}

.teacher-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.teacher-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.teacher-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.teacher-name {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-text-primary);
}

.teacher-title {
  font-size: var(--text-xs);
  color: var(--color-text-secondary);
}

.course-stats {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.rating {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.star-icon {
  color: var(--color-warning);
}

.rating-value {
  font-weight: 600;
  color: var(--color-text-primary);
}

.review-count {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.stats {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.stat-separator {
  color: var(--color-text-tertiary);
}

.course-description {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.description-text {
  font-size: var(--text-base);
  line-height: 1.6;
  color: var(--color-text-secondary);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.expand-link {
  align-self: flex-start;
  color: var(--color-accent);
  background: none;
  border: none;
  padding: 0;
  font-size: var(--text-sm);
  cursor: pointer;
  transition: color var(--duration-normal) var(--ease-default);
}

.expand-link:hover {
  color: var(--color-accent-hover);
  text-decoration: underline;
}

.start-button {
  width: 100%;
  height: 44px;
  background: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  margin-top: var(--space-2);
}

.start-button:hover {
  background: var(--color-accent-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

/* 主要内容区 */
.main-content {
  display: grid;
  grid-template-columns: 70% 30%;
  gap: var(--space-8);
  padding: var(--space-8) var(--space-6);
  max-width: var(--container-2xl);
  margin: 0 auto;
}

/* 左侧内容 */
.content-left {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

/* Tab 导航 */
.tab-navigation {
  display: flex;
  border-bottom: 1px solid var(--color-border);
  gap: var(--space-6);
}

.tab-button {
  padding: var(--space-3) 0;
  background: none;
  border: none;
  color: var(--color-text-secondary);
  font-size: var(--text-base);
  font-weight: 500;
  cursor: pointer;
  position: relative;
  transition: color var(--duration-normal) var(--ease-default);
}

.tab-button:hover {
  color: var(--color-text-primary);
}

.tab-button[aria-selected="true"] {
  color: var(--color-text-primary);
}

.tab-button[aria-selected="true"]::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: var(--color-accent);
  transition: all 200ms ease-out;
}

/* Tab 内容 */
.tab-content {
  min-height: 400px;
}

.tab-panel {
  padding: var(--space-6) 0;
}

/* 课程概览 */
.overview-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.section-title {
  font-size: var(--text-h2);
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
}

.section-text {
  font-size: var(--text-base);
  line-height: 1.6;
  color: var(--color-text-secondary);
  margin: 0;
}

.goal-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.goal-item {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  font-size: var(--text-base);
  color: var(--color-text-secondary);
}

.goal-item::before {
  content: '•';
  color: var(--color-accent);
  font-weight: bold;
  margin-top: 2px;
}

/* 课程目录 */
.catalog-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.chapter-details {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--color-bg-card);
}

.chapter-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-4);
  cursor: pointer;
  background: var(--color-bg-card);
  transition: background var(--duration-normal) var(--ease-default);
}

.chapter-summary:hover {
  background: var(--color-bg-hover);
}

.chapter-info {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.chapter-title {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-text-primary);
}

.chapter-meta {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.chapter-toggle {
  color: var(--color-text-secondary);
  transition: transform var(--duration-normal) var(--ease-default);
}

.chapter-videos {
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--color-border);
}

.video-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) var(--space-4);
  transition: background var(--duration-normal) var(--ease-default);
  border-left: 3px solid transparent;
}

.video-item:hover {
  background: var(--color-bg-hover);
}

.video-item.learning {
  border-left-color: var(--color-accent);
  background: var(--color-accent-subtle);
  font-weight: 500;
}

.video-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex: 1;
}

.status-icon {
  flex-shrink: 0;
}

.text-success {
  color: var(--color-success);
}

.text-accent {
  color: var(--color-accent);
  animation: pulse 2s infinite;
}

.text-tertiary {
  color: var(--color-text-tertiary);
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}

.video-title {
  font-size: var(--text-sm);
  color: var(--color-text-primary);
  flex: 1;
}

.video-duration {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  flex-shrink: 0;
}

/* 学员评价 */
.reviews-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.rating-summary {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: var(--space-6);
  padding: var(--space-6);
  background: var(--color-bg-card);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
}

.rating-overall {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
}

.rating-number {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.rating-stars {
  display: flex;
  gap: 2px;
}

.star-icon.filled {
  color: var(--color-warning);
}

.star-icon.half-filled {
  color: var(--color-warning);
  opacity: 0.5;
}

.rating-count {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.rating-distribution {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.rating-bar {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.rating-label {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  width: 30px;
}

.bar-container {
  flex: 1;
  height: 8px;
  background: var(--color-border);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  background: var(--color-warning);
  border-radius: var(--radius-full);
  transition: width var(--duration-slow) var(--ease-out);
}

.rating-percentage {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  width: 40px;
  text-align: right;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.review-item {
  padding: var(--space-4);
  background: var(--color-bg-card);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
}

.review-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}

.reviewer-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
}

.review-rating {
  display: flex;
  gap: 2px;
}

.review-date {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-left: auto;
}

.review-content {
  font-size: var(--text-sm);
  line-height: 1.5;
  color: var(--color-text-secondary);
  margin: 0;
}

/* 右侧边栏 */
.content-right {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
  position: sticky;
  top: var(--space-8);
  align-self: start;
  max-height: calc(100vh - 4rem);
  overflow-y: auto;
}

.sidebar-card {
  padding: var(--space-5);
  background: var(--color-bg-card);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.card-title {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

/* 进度卡片 */
.progress-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-4);
}

.progress-circle {
  position: relative;
  width: 120px;
  height: 120px;
}

.progress-svg {
  transform: rotate(-90deg);
}

.progress-bg {
  stroke: var(--color-border);
}

.progress-bar {
  stroke: var(--color-accent);
  transition: stroke-dashoffset var(--duration-slow) var(--ease-out);
}

.progress-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.progress-details {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-1);
}

.progress-label {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.progress-value {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-text-primary);
}

.progress-separator {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  margin: 0 var(--space-1);
}

/* 教师卡片 */
.teacher-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
}

.teacher-card .teacher-avatar {
  width: 64px;
  height: 64px;
}

.teacher-card .teacher-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  text-align: center;
}

.teacher-courses {
  font-size: var(--text-xs);
  color: var(--color-text-secondary);
}

/* 相关课程 */
.related-courses {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.related-course {
  display: flex;
  gap: var(--space-3);
  align-items: center;
  padding: var(--space-3);
  border-radius: var(--radius-md);
  transition: background var(--duration-normal) var(--ease-default);
  cursor: pointer;
}

.related-course:hover {
  background: var(--color-bg-hover);
}

.related-course-image {
  width: 80px;
  height: 60px;
  border-radius: var(--radius-sm);
  object-fit: cover;
  flex-shrink: 0;
}

.related-course-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.related-course-title {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-text-primary);
  line-height: 1.4;
}

.related-course-meta {
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.star-icon.small {
  color: var(--color-warning);
}

.related-course-rating {
  font-size: var(--text-xs);
  color: var(--color-text-secondary);
}

/* 响应式设计 */
@media (max-width: 1023px) {
  .main-content {
    grid-template-columns: 65% 35%;
    gap: var(--space-6);
  }
}

@media (max-width: 767px) {
  .hero-container {
    grid-template-columns: 1fr;
    gap: var(--space-6);
  }

  .hero-image {
    order: -1;
  }

  .main-content {
    grid-template-columns: 1fr;
  }

  .content-right {
    position: static;
    max-height: none;
  }

  .rating-summary {
    grid-template-columns: 1fr;
    gap: var(--space-4);
  }

  .rating-overall {
    flex-direction: row;
    justify-content: center;
  }
}
</style>