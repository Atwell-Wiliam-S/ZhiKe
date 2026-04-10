<template>
  <div class="qa-page">
    <!-- 顶部导航 -->
    <StudentTopNav />

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-left">
        <div class="search-container">
          <Input
            v-model="searchQuery"
            placeholder="搜索问题内容..."
            class="search-input"
          >
            <template #prefix>
              <Search class="w-4 h-4 text-text-tertiary" />
            </template>
          </Input>
        </div>
        <div class="time-filter" role="tablist" aria-label="时间筛选">
          <button
            v-for="option in timeOptions"
            :key="option.value"
            :class="['time-pill', { active: timeFilter === option.value }]"
            :aria-selected="timeFilter === option.value"
            @click="timeFilter = option.value"
          >
            {{ option.label }}
          </button>
        </div>
      </div>
      <div class="filter-right">
        <select v-model="sortBy" class="sort-select">
          <option value="latest">最新优先</option>
          <option value="earliest">最早优先</option>
        </select>
      </div>
    </div>

    <!-- 问答卡片列表 -->
    <div class="qa-list">
      <article
        v-for="qa in qaList"
        :key="qa.id"
        class="qa-card"
        @click="viewDetail(qa)"
      >
        <div class="qa-header">
          <h3 class="question-text">{{ qa.question }}</h3>
          <span class="timestamp">{{ formatTime(qa.createdAt) }}</span>
        </div>
        <div class="qa-answer">
          {{ truncateAnswer(qa.answer) }}
        </div>
        <div class="qa-footer">
          <span class="source-pill">{{ qa.contextFragments?.length || 0 }} 个来源</span>
          <span class="token-count">{{ qa.tokenUsage || 0 }} tokens</span>
          <span class="model-tag">{{ qa.modelVersion || '未知' }}</span>
        </div>
        <div class="view-detail">
          查看详情
        </div>
      </article>

      <!-- 空状态 -->
      <div v-if="qaList.length === 0" class="empty-state">
        <MessageSquare class="empty-icon" />
        <h3>暂无问答记录</h3>
        <button class="empty-button" @click="goToVideoLearning">
          去视频学习页提问
        </button>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="pagination.totalPages > 1" class="pagination">
      <button
        class="page-button"
        :disabled="pagination.page === 1"
        @click="pagination.page > 1 && pagination.page--"
      >
        <ChevronLeft class="w-4 h-4" />
      </button>
      <span class="page-info">
        {{ pagination.page }} / {{ pagination.totalPages }}
      </span>
      <button
        class="page-button"
        :disabled="pagination.page === pagination.totalPages"
        @click="pagination.page < pagination.totalPages && pagination.page++"
      >
        <ChevronRight class="w-4 h-4" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search, MessageSquare, ChevronLeft, ChevronRight } from 'lucide-vue-next'
import Input from '@/components/ui/Input.vue'
import StudentTopNav from '@/components/ui/StudentTopNav.vue'
import { usePagination } from '@/composables/usePagination'
import type { QaLogDTO } from '@/types/api'
import { qaApi } from '@/api/qa'
import { formatDate } from '@/utils/format'

const router = useRouter()

// 筛选和排序状态
const searchQuery = ref('')
const timeFilter = ref('all')
const sortBy = ref('latest')

// 时间筛选选项
const timeOptions = [
  { label: '今天', value: 'today' },
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '全部', value: 'all' }
]

// 分页功能
const { data: qaList, pagination } = usePagination<QaLogDTO>({
  fetchFn: async (page, pageSize) => {
    const response = await qaApi.getHistory({ page, pageSize })
    return {
      records: response.data.records,
      total: response.data.total,
      totalPages: response.data.totalPages
    }
  },
  defaultPageSize: 10,
  immediate: true
})

// 格式化时间
const formatTime = (date: string) => {
  return formatDate(date)
}

// 截断回答
const truncateAnswer = (answer: string) => {
  const lines = answer.split('\n')
  if (lines.length > 3) {
    return lines.slice(0, 3).join('\n') + '...'
  }
  return answer
}

// 查看详情
const viewDetail = (qa: QaLogDTO) => {
  // 这里可以跳转到详情页或打开模态框
  console.log('View detail:', qa)
}

// 跳转到视频学习页
const goToVideoLearning = () => {
  router.push('/student/home')
}
</script>

<style scoped>
.qa-page {
  min-height: 100vh;
  background: var(--color-bg-canvas);
  padding-top: 76px;
}

.qa-history {
  padding: 0 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.filter-left {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.search-container {
  position: relative;
  min-width: 300px;
}

.search-input {
  width: 100%;
}

.time-filter {
  display: flex;
  gap: 8px;
}

.time-pill {
  padding: 6px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  background: var(--color-bg-card);
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all 150ms ease;
}

.time-pill:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.time-pill.active {
  background: var(--color-accent);
  color: var(--color-accent-text);
  border-color: var(--color-accent);
}

.filter-right {
  display: flex;
  align-items: center;
}

.sort-select {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: 14px;
}

.qa-list {
  margin-bottom: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.qa-card {
  padding: 20px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  cursor: pointer;
  transition: all 150ms ease;
  position: relative;
  overflow: hidden;
}

.qa-card:hover {
  border-color: var(--color-accent);
  box-shadow: var(--shadow-md);
}

.qa-card:hover .view-detail {
  opacity: 1;
}

.qa-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.question-text {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
  flex: 1;
  margin-right: 12px;
}

.timestamp {
  font-size: 12px;
  color: var(--color-text-tertiary);
  white-space: nowrap;
}

.qa-answer {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.5;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.qa-footer {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
}

.source-pill {
  padding: 4px 8px;
  background: var(--color-accent-subtle);
  color: var(--color-accent);
  border-radius: var(--radius-sm);
  font-size: 12px;
}

.token-count {
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.model-tag {
  padding: 4px 8px;
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  border-radius: var(--radius-sm);
  font-size: 12px;
}

.view-detail {
  position: absolute;
  bottom: 0;
  right: 0;
  background: var(--color-accent);
  color: var(--color-accent-text);
  padding: 8px 16px;
  border-radius: var(--radius-sm) 0 0 0;
  font-size: 12px;
  font-weight: 500;
  opacity: 0;
  transition: opacity 150ms ease;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 64px 24px;
  text-align: center;
}

.empty-icon {
  width: 64px;
  height: 64px;
  color: var(--color-text-tertiary);
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 18px;
  color: var(--color-text-primary);
  margin-bottom: 16px;
}

.empty-button {
  padding: 10px 24px;
  background: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 150ms ease;
}

.empty-button:hover {
  background: var(--color-accent-hover);
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 32px;
}

.page-button {
  width: 36px;
  height: 36px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 150ms ease;
}

.page-button:hover:not(:disabled) {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.page-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: var(--color-text-secondary);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .qa-history {
    padding: 16px;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-left {
    flex-direction: column;
    align-items: stretch;
  }

  .search-container {
    min-width: auto;
  }

  .time-filter {
    justify-content: space-between;
  }

  .qa-list {
    max-width: 100%;
  }
}
</style>