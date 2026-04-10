<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search, FileText, GraduationCap, MonitorPlay } from 'lucide-vue-next'

const router = useRouter()

const searchQuery = ref('')
const selectedCourse = ref('all')
const selectedVideo = ref('all')

interface Note {
  id: number
  content: string
  videoTitle: string
  timestamp: number
  createdAt: string
  courseName: string
}

const notes = ref<Note[]>([])

const goToVideoPage = () => {
  router.push('/student/video/1')
}
</script>

<template>
  <div class="notes-container">
    <header class="header">
      <div class="header-content">
        <div class="logo-section">
          <GraduationCap class="logo-icon" :size="32" />
          <h1 class="app-name">ZhiKe</h1>
        </div>
        <nav class="nav-menu">
          <router-link to="/student/home" class="nav-item">首页</router-link>
          <router-link to="/student/course/1" class="nav-item">我的课程</router-link>
          <router-link to="/student/qa-history" class="nav-item">问答历史</router-link>
          <router-link to="/student/notes" class="nav-item active">我的笔记</router-link>
        </nav>
        <div class="user-section">
          <div class="user-avatar">Z</div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="page-header">
        <div class="page-title-section">
          <h2 class="page-title">我的笔记</h2>
          <p class="page-subtitle">在视频学习过程中记录的知识点</p>
        </div>
        <div class="stats-badge">
          <FileText :size="16" />
          <span>共 {{ notes.length }} 条</span>
        </div>
      </div>

      <div class="filter-bar">
        <div class="filter-selects">
          <select v-model="selectedCourse" class="filter-select">
            <option value="all">全部课程</option>
          </select>
          <select v-model="selectedVideo" class="filter-select">
            <option value="all">全部视频</option>
          </select>
        </div>

        <div class="search-box">
          <Search class="search-icon" :size="18" />
          <input
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="搜索笔记内容..."
          />
        </div>
      </div>

      <div class="notes-content">
        <div v-if="notes.length === 0" class="empty-state">
          <div class="empty-icon-wrapper">
            <FileText class="empty-icon" :size="40" />
          </div>
          <h3 class="empty-title">暂无笔记</h3>
          <p class="empty-hint">在视频学习页面按快捷键添加笔记</p>
          <button class="btn btn-primary" @click="goToVideoPage">
            <MonitorPlay :size="18" />
            去学习页
          </button>
        </div>

        <div v-else class="notes-grid">
          <!-- Notes will be rendered here -->
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.notes-container {
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
  padding: var(--spacing-xl) var(--spacing-xl);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-xl);
}

.page-title-section {
  flex: 1;
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

.stats-badge {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-sm) var(--spacing-md);
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(59, 130, 246, 0.15) 100%);
  border-radius: var(--radius-full);
  color: var(--color-primary);
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.filter-bar {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-xl);
  flex-wrap: wrap;
  align-items: center;
}

.filter-selects {
  display: flex;
  gap: var(--spacing-sm);
}

.filter-select {
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  color: var(--color-text-primary);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  min-width: 140px;
}

.filter-select:hover {
  border-color: var(--color-primary);
}

.filter-select:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 240px;
  margin-left: auto;
}

.search-icon {
  position: absolute;
  left: var(--spacing-md);
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-muted);
  pointer-events: none;
}

.search-input {
  width: 100%;
  padding: var(--spacing-sm) var(--spacing-md) var(--spacing-sm) 44px;
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  color: var(--color-text-primary);
  font-size: var(--font-size-sm);
  transition: all var(--transition-fast);
}

.search-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
}

.search-input::placeholder {
  color: var(--color-text-muted);
}

.notes-content {
  margin-top: var(--spacing-lg);
}

.empty-state {
  text-align: center;
  padding: var(--spacing-2xl);
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
}

.empty-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-xs);
}

.empty-hint {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-lg);
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-lg);
  border-radius: var(--radius-md);
  font-weight: 500;
  font-size: var(--font-size-sm);
  transition: all var(--transition-fast);
  cursor: pointer;
  border: none;
}

.btn-primary {
  background: linear-gradient(135deg, var(--color-gradient-start) 0%, var(--color-gradient-end) 100%);
  color: white;
  box-shadow: var(--shadow-md);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.btn-primary:active {
  transform: translateY(0);
}

.notes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--spacing-lg);
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
  
  .page-header {
    flex-direction: column;
    gap: var(--spacing-md);
  }
  
  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-selects {
    width: 100%;
  }
  
  .filter-select {
    flex: 1;
    min-width: auto;
  }
  
  .search-box {
    min-width: 100%;
    margin-left: 0;
  }
  
  .notes-grid {
    grid-template-columns: 1fr;
  }
}
</style>
