<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Calendar, MessageSquare, ChevronDown, GraduationCap } from 'lucide-vue-next'

const router = useRouter()

const searchQuery = ref('')
const activeFilter = ref('all')
const sortOrder = ref('latest')

const filters = [
  { id: 'today', label: '今天' },
  { id: 'week', label: '本周' },
  { id: 'month', label: '本月' },
  { id: 'all', label: '全部' }
]

interface QaRecord {
  id: number
  question: string
  answer: string
  videoTitle: string
  timestamp: string
  courseName: string
}

const qaRecords = ref<QaRecord[]>([])

const setFilter = (filterId: string) => {
  activeFilter.value = filterId
}
</script>

<template>
  <div class="qa-history-container">
    <header class="header">
      <div class="header-content">
        <div class="logo-section">
          <GraduationCap class="logo-icon" :size="32" />
          <h1 class="app-name">ZhiKe</h1>
        </div>
        <nav class="nav-menu">
          <router-link to="/student/home" class="nav-item">首页</router-link>
          <router-link to="/student/course/1" class="nav-item">我的课程</router-link>
          <router-link to="/student/qa-history" class="nav-item active">问答历史</router-link>
          <router-link to="/student/notes" class="nav-item">我的笔记</router-link>
        </nav>
        <div class="user-section">
          <div class="user-avatar">Z</div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="page-header">
        <div class="page-title-section">
          <h2 class="page-title">问答历史</h2>
          <p class="page-subtitle">查看你与AI助教的所有对话记录</p>
        </div>
        <div class="stats-badge">
          <MessageSquare :size="16" />
          <span>共 {{ qaRecords.length }} 条</span>
        </div>
      </div>

      <div class="filter-bar">
        <div class="search-box">
          <Search class="search-icon" :size="18" />
          <input
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="搜索问题..."
          />
        </div>

        <div class="filter-tabs">
          <button
            v-for="filter in filters"
            :key="filter.id"
            class="filter-tab"
            :class="{ active: activeFilter === filter.id }"
            @click="setFilter(filter.id)"
          >
            {{ filter.label }}
          </button>
        </div>

        <div class="sort-dropdown">
          <button class="sort-button">
            <Calendar :size="16" />
            <span>最新优先</span>
            <ChevronDown :size="16" />
          </button>
        </div>
      </div>

      <div class="qa-list">
        <div v-if="qaRecords.length === 0" class="empty-state">
          <div class="loading-spinner"></div>
          <p class="loading-text">加载中...</p>
        </div>

        <div v-else class="qa-cards">
          <!-- Q&A cards will be rendered here -->
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.qa-history-container {
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

.search-box {
  position: relative;
  flex: 1;
  min-width: 240px;
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

.filter-tabs {
  display: flex;
  gap: var(--spacing-xs);
}

.filter-tab {
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.filter-tab:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.filter-tab.active {
  background: linear-gradient(135deg, var(--color-gradient-start) 0%, var(--color-gradient-end) 100%);
  border-color: transparent;
  color: white;
}

.sort-dropdown {
  margin-left: auto;
}

.sort-button {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.sort-button:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.qa-list {
  margin-top: var(--spacing-lg);
}

.empty-state {
  text-align: center;
  padding: var(--spacing-2xl);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--color-bg-card);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto var(--spacing-md);
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
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
  
  .search-box {
    min-width: 100%;
  }
  
  .filter-tabs {
    width: 100%;
    justify-content: flex-start;
  }
  
  .sort-dropdown {
    margin-left: 0;
    width: 100%;
  }
  
  .sort-button {
    width: 100%;
    justify-content: center;
  }
}
</style>
