<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Star, ChevronDown, ChevronUp, Play, Check, Lock, Clock, Users, Video, Award, BookOpen, MessageSquare } from 'lucide-vue-next'
import { useThemeStore } from '@/stores/theme'
import StudentTopNav from '@/components/ui/StudentTopNav.vue'

const route = useRoute()
const themeStore = useThemeStore()
const courseId = computed(() => route.params.courseId as string)

// 模拟课程数据
const course = ref({
  id: courseId.value,
  title: '高等数学：微积分与线性代数',
  teacher: {
    name: '张教授',
    title: '数学博士',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20teacher%20avatar%20portrait&image_size=square_hd',
    courses: 12,
    bio: '张教授拥有20年教学经验，专注于高等数学教育，发表论文30余篇，曾获国家级教学成果奖。'
  },
  cover: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mathematics%20education%20classroom%20scene&image_size=landscape_16_9',
  rating: 4.8,
  reviewCount: 156,
  videoCount: 24,
  duration: '12 小时',
  studentCount: 1256,
  level: '中级',
  category: '数学',
  lastUpdated: '2024-03-15',
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
    },
    {
      id: 3,
      title: '微积分进阶',
      videoCount: 5,
      duration: '4 小时 30 分',
      videos: [
        { id: 8, title: '多元函数微积分', duration: '60:00', status: 'locked' },
        { id: 9, title: '级数', duration: '55:00', status: 'locked' },
        { id: 10, title: '常微分方程', duration: '65:00', status: 'locked' },
        { id: 11, title: '偏微分方程初步', duration: '50:00', status: 'locked' },
        { id: 12, title: '微积分应用', duration: '60:00', status: 'locked' }
      ]
    }
  ],
  progress: 25,
  totalVideos: 24,
  completedVideos: 6
})

// 状态管理
const activeTab = ref('overview')
const expandedChapters = ref<number[]>([1])
const showFullDescription = ref(false)
const showFullTeacherBio = ref(false)

// 计算属性
const descriptionDisplay = computed(() => {
  if (showFullDescription.value) return course.value.description
  return course.value.description.slice(0, 150) + '...'
})

const teacherBioDisplay = computed(() => {
  if (showFullTeacherBio.value) return course.value.teacher.bio
  return course.value.teacher.bio.slice(0, 100) + '...'
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

// 无障碍功能
const handleTabChange = (tab: string) => {
  activeTab.value = tab
  // 聚焦到对应的tabpanel
  const tabpanel = document.querySelector(`[data-tab=${tab}]`) as HTMLElement | null
  if (tabpanel) {
    tabpanel.setAttribute('tabindex', '-1')
    tabpanel.focus()
  }
}
</script>

<template>
  <div class="course-detail">
    <!-- 顶部导航 -->
    <StudentTopNav />

    <!-- 面包屑 -->
    <nav aria-label="面包屑" class="breadcrumb">
      <ol class="breadcrumb-list">
        <li class="breadcrumb-item">
          <router-link to="/student/home" class="breadcrumb-link" aria-label="返回首页">首页</router-link>
        </li>
        <li class="breadcrumb-item">
          <router-link to="/student/home" class="breadcrumb-link" aria-label="课程分类">课程</router-link>
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
          <img :src="course.cover" :alt="course.title" class="cover-image" loading="lazy">
        </div>
        <div class="hero-content">
          <div class="course-meta">
            <span class="course-category">{{ course.category }}</span>
            <span class="course-level">{{ course.level }}</span>
          </div>
          <h1 class="course-title">{{ course.title }}</h1>
          <div class="teacher-info">
            <img :src="course.teacher.avatar" :alt="course.teacher.name" class="teacher-avatar" loading="lazy">
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
              <div class="stat-item">
                <Video :size="14" class="stat-icon" />
                <span>{{ course.videoCount }} 个视频</span>
              </div>
              <div class="stat-item">
                <Clock :size="14" class="stat-icon" />
                <span>{{ course.duration }}</span>
              </div>
              <div class="stat-item">
                <Users :size="14" class="stat-icon" />
                <span>{{ course.studentCount }} 名学生</span>
              </div>
            </div>
            <div class="course-info">
              <span class="last-updated">最后更新: {{ course.lastUpdated }}</span>
            </div>
          </div>
          <div class="course-description">
            <p class="description-text">{{ descriptionDisplay }}</p>
            <button 
              v-if="!showFullDescription" 
              @click="showFullDescription = true" 
              class="expand-link"
              :aria-expanded="showFullDescription"
            >
              展开全部
            </button>
          </div>
          <div class="hero-cta">
            <button class="start-button" :aria-label="`开始学习：${course.title}`">
              <Play :size="16" class="play-icon" />
              开始学习
            </button>
            <button class="secondary-button" :aria-label="`收藏课程：${course.title}`">
              <BookOpen :size="16" class="book-icon" />
              收藏
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- 主要内容区 -->
    <div class="main-content">
      <!-- 左侧内容 -->
      <div class="content-left">
        <!-- Tab 导航 -->
        <div role="tablist" class="tab-navigation" aria-label="课程详情导航">
          <button
            role="tab"
            :aria-selected="activeTab === 'overview'"
            :id="'tab-overview'"
            :aria-controls="'panel-overview'"
            @click="handleTabChange('overview')"
            class="tab-button"
          >
            课程概览
          </button>
          <button
            role="tab"
            :aria-selected="activeTab === 'catalog'"
            :id="'tab-catalog'"
            :aria-controls="'panel-catalog'"
            @click="handleTabChange('catalog')"
            class="tab-button"
          >
            课程目录
          </button>
          <button
            role="tab"
            :aria-selected="activeTab === 'reviews'"
            :id="'tab-reviews'"
            :aria-controls="'panel-reviews'"
            @click="handleTabChange('reviews')"
            class="tab-button"
          >
            学员评价
          </button>
        </div>

        <!-- Tab 内容 -->
        <div class="tab-content">
          <!-- 课程概览 -->
          <div 
            v-if="activeTab === 'overview'" 
            role="tabpanel"
            :id="'panel-overview'"
            :aria-labelledby="'tab-overview'"
            :data-tab="'overview'"
            class="tab-panel"
          >
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
              <h2 class="section-title">课程特色</h2>
              <ul class="feature-list">
                <li class="feature-item">
                  <Award :size="16" class="feature-icon" />
                  <span>由经验丰富的教授亲自授课</span>
                </li>
                <li class="feature-item">
                  <Video :size="16" class="feature-icon" />
                  <span>高清视频讲解，内容深入浅出</span>
                </li>
                <li class="feature-item">
                  <MessageSquare :size="16" class="feature-icon" />
                  <span>课后习题和讨论，巩固学习效果</span>
                </li>
                <li class="feature-item">
                  <Clock :size="16" class="feature-icon" />
                  <span>灵活的学习时间，随时可以复习</span>
                </li>
              </ul>
            </div>
          </div>

          <!-- 课程目录 -->
          <div 
            v-if="activeTab === 'catalog'" 
            role="tabpanel"
            :id="'panel-catalog'"
            :aria-labelledby="'tab-catalog'"
            :data-tab="'catalog'"
            class="tab-panel"
          >
            <div class="catalog-content">
              <div class="catalog-header">
                <h2 class="catalog-title">课程目录</h2>
                <p class="catalog-subtitle">{{ course.totalVideos }} 个视频 · {{ course.duration }}</p>
              </div>
              <div class="chapter-list">
                <div
                  v-for="chapter in course.chapters"
                  :key="chapter.id"
                  class="chapter-details"
                >
                  <button
                    class="chapter-summary"
                    @click="toggleChapter(chapter.id)"
                    :aria-expanded="expandedChapters.includes(chapter.id)"
                    :aria-controls="`chapter-${chapter.id}`"
                  >
                    <div class="chapter-info">
                      <span class="chapter-title">{{ chapter.title }}</span>
                      <span class="chapter-meta">{{ chapter.videoCount }} 个视频，{{ chapter.duration }}</span>
                    </div>
                    <div class="chapter-toggle">
                      <ChevronDown v-if="expandedChapters.includes(chapter.id)" :size="16" />
                      <ChevronUp v-else :size="16" />
                    </div>
                  </button>
                  <div
                    :id="`chapter-${chapter.id}`"
                    v-show="expandedChapters.includes(chapter.id)"
                    class="chapter-videos"
                  >
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
                </div>
              </div>
            </div>
          </div>

          <!-- 学员评价 -->
          <div 
            v-if="activeTab === 'reviews'" 
            role="tabpanel"
            :id="'panel-reviews'"
            :aria-labelledby="'tab-reviews'"
            :data-tab="'reviews'"
            class="tab-panel"
          >
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
                <div class="review-item">
                  <div class="review-header">
                    <span class="reviewer-name">学生 C</span>
                    <div class="review-rating">
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                      <Star class="star-icon filled" :size="14" />
                    </div>
                    <span class="review-date">2023-12-28</span>
                  </div>
                  <p class="review-content">作为一名大一新生，这门课对我帮助很大。老师从基础讲起，循序渐进，让我对高等数学有了更清晰的认识。视频质量也很好，观看体验很棒。</p>
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
              <svg class="progress-svg" width="120" height="120" viewBox="0 0 120 120" aria-hidden="true">
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
              <span class="progress-value">{{ course.completedVideos }} / {{ course.totalVideos }} 个视频</span>
              <div class="progress-bar-linear">
                <div 
                  class="progress-fill-linear" 
                  :style="{ width: course.progress + '%' }"
                ></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 教师简介卡片 -->
        <div class="sidebar-card">
          <h3 class="card-title">教师简介</h3>
          <div class="teacher-card">
            <img :src="course.teacher.avatar" :alt="course.teacher.name" class="teacher-avatar" loading="lazy">
            <div class="teacher-info">
              <span class="teacher-name">{{ course.teacher.name }}</span>
              <span class="teacher-title">{{ course.teacher.title }}</span>
              <span class="teacher-courses">{{ course.teacher.courses }} 门课程</span>
              <p class="teacher-bio">{{ teacherBioDisplay }}</p>
              <button 
                v-if="!showFullTeacherBio" 
                @click="showFullTeacherBio = true" 
                class="expand-link"
                :aria-expanded="showFullTeacherBio"
              >
                查看更多
              </button>
            </div>
          </div>
        </div>

        <!-- 相关课程推荐 -->
        <div class="sidebar-card">
          <h3 class="card-title">相关课程</h3>
          <div class="related-courses">
            <div class="related-course" tabindex="0" role="button" aria-label="查看课程：大学物理">
              <img 
                src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=physics%20education%20classroom&image_size=landscape_4_3" 
                alt="大学物理" 
                class="related-course-image"
                loading="lazy"
              >
              <div class="related-course-info">
                <span class="related-course-title">大学物理</span>
                <div class="related-course-meta">
                  <Star class="star-icon small" :size="12" />
                  <span class="related-course-rating">4.9</span>
                  <span class="related-course-students">(1.2k 学生)</span>
                </div>
              </div>
            </div>
            <div class="related-course" tabindex="0" role="button" aria-label="查看课程：数据结构与算法">
              <img 
                src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=computer%20science%20coding%20classroom&image_size=landscape_4_3" 
                alt="数据结构与算法" 
                class="related-course-image"
                loading="lazy"
              >
              <div class="related-course-info">
                <span class="related-course-title">数据结构与算法</span>
                <div class="related-course-meta">
                  <Star class="star-icon small" :size="12" />
                  <span class="related-course-rating">4.7</span>
                  <span class="related-course-students">(856 学生)</span>
                </div>
              </div>
            </div>
            <div class="related-course" tabindex="0" role="button" aria-label="查看课程：概率论与数理统计">
              <img 
                src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=statistics%20data%20analysis%20classroom&image_size=landscape_4_3" 
                alt="概率论与数理统计" 
                class="related-course-image"
                loading="lazy"
              >
              <div class="related-course-info">
                <span class="related-course-title">概率论与数理统计</span>
                <div class="related-course-meta">
                  <Star class="star-icon small" :size="12" />
                  <span class="related-course-rating">4.6</span>
                  <span class="related-course-students">(723 学生)</span>
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
  transition: all var(--duration-normal) var(--ease-default);
  display: flex;
  flex-direction: column;
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
  display: flex;
  align-items: center;
}

.breadcrumb-link:hover {
  color: var(--color-accent);
}

.breadcrumb-item:not(:last-child)::after {
  content: '>';
  margin-left: var(--space-2);
  color: var(--color-text-tertiary);
  font-size: var(--text-sm);
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
  transition: transform var(--duration-normal) var(--ease-default);
}

.hero-image:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--duration-slow) var(--ease-default);
}

.hero-image:hover .cover-image {
  transform: scale(1.02);
}

.hero-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.course-meta {
  display: flex;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.course-category,
.course-level {
  padding: var(--space-1) var(--space-3);
  background: var(--color-bg-elevated);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: 500;
  color: var(--color-text-secondary);
}

.course-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
  line-height: 1.2;
}

.teacher-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.teacher-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--color-border);
  transition: transform var(--duration-normal) var(--ease-default);
}

.teacher-avatar:hover {
  transform: scale(1.05);
}

.teacher-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.teacher-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
}

.teacher-title {
  font-size: var(--text-xs);
  color: var(--color-text-secondary);
}

.course-stats {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.rating {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.star-icon {
  color: var(--color-warning);
  flex-shrink: 0;
}

.rating-value {
  font-weight: 600;
  color: var(--color-text-primary);
  font-size: var(--text-base);
}

.review-count {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.stats {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  flex-wrap: wrap;
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.stat-icon {
  color: var(--color-text-tertiary);
  flex-shrink: 0;
}

.course-info {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
}

.last-updated {
  display: flex;
  align-items: center;
  gap: var(--space-1);
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
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.expand-link:hover {
  color: var(--color-accent-hover);
  text-decoration: underline;
}

.hero-cta {
  display: flex;
  gap: var(--space-4);
  margin-top: var(--space-2);
}

.start-button {
  flex: 1;
  height: 48px;
  background: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
}

.start-button:hover {
  background: var(--color-accent-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.secondary-button {
  height: 48px;
  padding: 0 var(--space-6);
  background: var(--color-bg-elevated);
  color: var(--color-text-primary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
}

.secondary-button:hover {
  background: var(--color-bg-hover);
  border-color: var(--color-accent);
  transform: translateY(-1px);
}

.play-icon,
.book-icon {
  flex-shrink: 0;
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
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.tab-navigation::-webkit-scrollbar {
  display: none;
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
  white-space: nowrap;
  min-width: 100px;
  text-align: center;
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
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
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
  flex-shrink: 0;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.feature-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  padding: var(--space-3);
  background: var(--color-bg-elevated);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  transition: all var(--duration-normal) var(--ease-default);
}

.feature-item:hover {
  background: var(--color-bg-hover);
  border-color: var(--color-accent);
  transform: translateX(4px);
}

.feature-icon {
  color: var(--color-accent);
  flex-shrink: 0;
}

/* 课程目录 */
.catalog-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.catalog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-3);
}

.catalog-title {
  font-size: var(--text-h2);
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
}

.catalog-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin: 0;
}

.chapter-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.chapter-details {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--color-bg-card);
  transition: all var(--duration-normal) var(--ease-default);
}

.chapter-details:hover {
  border-color: var(--color-accent);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.chapter-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-4);
  cursor: pointer;
  background: var(--color-bg-card);
  border: none;
  width: 100%;
  text-align: left;
  transition: background var(--duration-normal) var(--ease-default);
}

.chapter-summary:hover {
  background: var(--color-bg-hover);
}

.chapter-summary:focus {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
  border-radius: var(--radius-md);
}

.chapter-info {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
  flex: 1;
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
  flex-shrink: 0;
}

.chapter-videos {
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--color-border);
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    max-height: 0;
  }
  to {
    opacity: 1;
    max-height: 500px;
  }
}

.video-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) var(--space-4);
  transition: background var(--duration-normal) var(--ease-default);
  border-left: 3px solid transparent;
  cursor: pointer;
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
  line-height: 1.4;
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
  transition: all var(--duration-normal) var(--ease-default);
}

.rating-summary:hover {
  border-color: var(--color-accent);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.rating-overall {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-2);
}

.rating-number {
  font-size: 36px;
  font-weight: 700;
  color: var(--color-text-primary);
  line-height: 1;
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
  flex-shrink: 0;
}

.bar-container {
  flex: 1;
  height: 8px;
  background: var(--color-border);
  border-radius: var(--radius-full);
  overflow: hidden;
  transition: all var(--duration-normal) var(--ease-default);
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
  flex-shrink: 0;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.review-item {
  padding: var(--space-5);
  background: var(--color-bg-card);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  transition: all var(--duration-normal) var(--ease-default);
}

.review-item:hover {
  border-color: var(--color-accent);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px);
}

.review-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
  flex-wrap: wrap;
}

.reviewer-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
  flex-shrink: 0;
}

.review-rating {
  display: flex;
  gap: 2px;
  flex-shrink: 0;
}

.review-date {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-left: auto;
  flex-shrink: 0;
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
  transition: all var(--duration-normal) var(--ease-default);
}

.sidebar-card:hover {
  border-color: var(--color-accent);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px);
}

.card-title {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--space-2);
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
  gap: var(--space-3);
  width: 100%;
}

.progress-label {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.progress-value {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-text-primary);
  text-align: center;
}

.progress-bar-linear {
  width: 100%;
  height: 8px;
  background: var(--color-border);
  border-radius: var(--radius-full);
  overflow: hidden;
  margin-top: var(--space-2);
}

.progress-fill-linear {
  height: 100%;
  background: var(--color-accent);
  border-radius: var(--radius-full);
  transition: width var(--duration-slow) var(--ease-out);
}

/* 教师卡片 */
.teacher-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
}

.teacher-card .teacher-avatar {
  width: 80px;
  height: 80px;
  border: 3px solid var(--color-border);
}

.teacher-card .teacher-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-2);
  text-align: center;
  width: 100%;
}

.teacher-bio {
  font-size: var(--text-sm);
  line-height: 1.5;
  color: var(--color-text-secondary);
  margin: 0;
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
  transition: all var(--duration-normal) var(--ease-default);
  cursor: pointer;
  border: 1px solid transparent;
}

.related-course:hover {
  background: var(--color-bg-hover);
  border-color: var(--color-accent);
  transform: translateX(4px);
}

.related-course:focus {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
}

.related-course-image {
  width: 80px;
  height: 60px;
  border-radius: var(--radius-sm);
  object-fit: cover;
  flex-shrink: 0;
  transition: transform var(--duration-normal) var(--ease-default);
}

.related-course:hover .related-course-image {
  transform: scale(1.05);
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
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.related-course-meta {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  flex-wrap: wrap;
}

.star-icon.small {
  color: var(--color-warning);
  flex-shrink: 0;
}

.related-course-rating {
  font-size: var(--text-xs);
  color: var(--color-text-secondary);
  flex-shrink: 0;
}

.related-course-students {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  flex-shrink: 0;
}

/* 响应式设计 */
@media (max-width: 1023px) {
  .main-content {
    grid-template-columns: 65% 35%;
    gap: var(--space-6);
  }
  
  .course-title {
    font-size: 28px;
  }
  
  .hero-cta {
    flex-direction: column;
  }
  
  .secondary-button {
    width: 100%;
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
    gap: var(--space-4);
  }
  
  .course-title {
    font-size: 24px;
  }
  
  .stats {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-2);
  }
  
  .tab-navigation {
    gap: var(--space-4);
  }
  
  .tab-button {
    font-size: var(--text-sm);
    min-width: 80px;
  }
  
  .sidebar-card {
    padding: var(--space-4);
  }
}

/* 无障碍功能 */
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}

/* 焦点样式 */
button:focus-visible,
a:focus-visible,
[tabindex]:focus-visible {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
  border-radius: var(--radius-sm);
}

/* 高对比度模式支持 */
@media (prefers-contrast: high) {
  .course-detail {
    border-color: currentColor;
  }
  
  .sidebar-card,
  .review-item,
  .chapter-details {
    border: 2px solid currentColor;
  }
}
</style>