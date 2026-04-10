<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { BookOpen, MessageSquare, FileText, Settings, GraduationCap } from 'lucide-vue-next'

const router = useRouter()

const features = ref([
  {
    id: 1,
    title: '课程学习',
    description: '浏览并学习各类课程',
    icon: BookOpen,
    path: '/student/course/1'
  },
  {
    id: 2,
    title: '问答历史',
    description: '查看与AI助教的所有对话记录',
    icon: MessageSquare,
    path: '/student/qa-history'
  },
  {
    id: 3,
    title: '我的笔记',
    description: '在视频学习过程中记录的知识点',
    icon: FileText,
    path: '/student/notes'
  },
  {
    id: 4,
    title: '设置',
    description: '个性化设置和偏好配置',
    icon: Settings,
    path: '/student/settings'
  }
])

const navigateTo = (path: string) => {
  router.push(path)
}
</script>

<template>
  <div class="student-home-container">
    <header class="header">
      <div class="header-content">
        <div class="logo-section">
          <GraduationCap class="logo-icon" :size="32" />
          <h1 class="app-name">ZhiKe</h1>
        </div>
        <nav class="nav-menu">
          <router-link to="/student/home" class="nav-item active">首页</router-link>
          <router-link to="/student/course/1" class="nav-item">我的课程</router-link>
          <router-link to="/student/qa-history" class="nav-item">问答历史</router-link>
          <router-link to="/student/notes" class="nav-item">我的笔记</router-link>
        </nav>
        <div class="user-section">
          <div class="user-avatar">Z</div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="page-header">
        <h2 class="page-title">欢迎回来！</h2>
        <p class="page-subtitle">继续您的学习之旅</p>
      </div>

      <div class="features-grid">
        <div
          v-for="feature in features"
          :key="feature.id"
          class="feature-card"
          @click="navigateTo(feature.path)"
        >
          <div class="feature-icon-wrapper">
            <component :is="feature.icon" class="feature-icon" :size="28" />
          </div>
          <h3 class="feature-title">{{ feature.title }}</h3>
          <p class="feature-description">{{ feature.description }}</p>
        </div>
      </div>

      <section class="recent-section">
        <div class="section-header">
          <h3 class="section-title">最近学习</h3>
        </div>
        <div class="empty-state">
          <div class="empty-icon-wrapper">
            <BookOpen class="empty-icon" :size="48" />
          </div>
          <p class="empty-text">暂无学习记录</p>
          <p class="empty-hint">开始学习一门课程吧</p>
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped>
.student-home-container {
  min-height: 100vh;
  background-color: var(--color-bg-primary);
}

.header {
  background-color: var(--color-bg-secondary);
  border-bottom: 1px solid var(--color-border);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--spacing-md) var(--spacing-xl);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--spacing-lg);
}

.logo-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.logo-icon {
  color: var(--color-primary);
}

.app-name {
  font-size: var(--font-size-xl);
  font-weight: 800;
  background: linear-gradient(135deg, var(--color-gradient-start) 0%, var(--color-gradient-end) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-menu {
  display: flex;
  gap: var(--spacing-md);
}

.nav-item {
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: 500;
  transition: all var(--transition-fast);
  position: relative;
}

.nav-item:hover {
  color: var(--color-text-primary);
  background-color: var(--color-bg-hover);
}

.nav-item.active {
  color: var(--color-primary);
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: -16px;
  left: 50%;
  transform: translateX(-50%);
  width: 32px;
  height: 3px;
  background: linear-gradient(90deg, var(--color-gradient-start), var(--color-gradient-end));
  border-radius: var(--radius-full);
}

.user-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  background: linear-gradient(135deg, var(--color-gradient-start) 0%, var(--color-gradient-end) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: var(--font-size-base);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--spacing-2xl) var(--spacing-xl);
}

.page-header {
  margin-bottom: var(--spacing-2xl);
}

.page-title {
  font-size: var(--font-size-2xl);
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-xs);
}

.page-subtitle {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.feature-card {
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.feature-card:hover {
  background: var(--color-bg-hover);
  border-color: var(--color-primary);
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.feature-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-lg);
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.2) 0%, rgba(59, 130, 246, 0.2) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: var(--spacing-md);
}

.feature-icon {
  color: var(--color-primary);
}

.feature-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-xs);
}

.feature-description {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  line-height: var(--line-height-relaxed);
}

.recent-section {
  margin-top: var(--spacing-2xl);
}

.section-header {
  margin-bottom: var(--spacing-lg);
}

.section-title {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--color-text-primary);
}

.empty-state {
  text-align: center;
  padding: var(--spacing-2xl);
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
}

.empty-icon-wrapper {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-xl);
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(59, 130, 246, 0.1) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto var(--spacing-lg);
}

.empty-icon {
  color: var(--color-primary);
  opacity: 0.6;
}

.empty-text {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-xs);
}

.empty-hint {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

@media (max-width: 768px) {
  .header-content {
    padding: var(--spacing-md);
  }
  
  .nav-menu {
    display: none;
  }
  
  .main-content {
    padding: var(--spacing-xl) var(--spacing-md);
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
