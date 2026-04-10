<template>
  <div class="qa-page">
    <StudentTopNav />

    <div class="qa-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <div>
          <h1 class="page-title">问答历史</h1>
          <p class="page-subtitle">查看你与 AI 助教的所有对话记录</p>
        </div>
        <div class="header-stats">
          <div class="stat-chip">
            <MessageSquare :size="14" />
            <span>共 {{ pagination.total }} 条</span>
          </div>
        </div>
      </div>

      <!-- 筛选栏 -->
      <div class="filter-bar">
        <div class="filter-left">
          <div class="search-box">
            <Search :size="16" class="search-icon" />
            <input
              v-model="searchQuery"
              type="text"
              class="search-input"
              placeholder="搜索问题..."
              @input="debouncedSearch"
            />
            <button v-if="searchQuery" class="search-clear" @click="clearSearch" aria-label="清除搜索">
              <X :size="14" />
            </button>
          </div>
          <div class="time-filter" role="tablist" aria-label="时间筛选">
            <button
              v-for="option in timeOptions"
              :key="option.value"
              :class="['time-pill', { active: timeFilter === option.value }]"
              :aria-selected="timeFilter === option.value"
              @click="setTimeFilter(option.value)"
            >
              {{ option.label }}
            </button>
          </div>
        </div>
        <div class="filter-right">
          <button class="sort-btn" @click="toggleSort" aria-label="切换排序">
            <ArrowUpDown :size="14" />
            <span>{{ sortBy === 'latest' ? '最新优先' : '最早优先' }}</span>
          </button>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>

      <!-- 问答卡片列表 -->
      <div v-else-if="filteredQaList.length > 0" class="qa-list">
        <article
          v-for="qa in filteredQaList"
          :key="qa.id"
          class="qa-card"
          tabindex="0"
          @click="openDetail(qa)"
          @keydown.enter="openDetail(qa)"
        >
          <div class="qa-card-header">
            <div class="qa-question">
              <HelpCircle :size="16" class="question-icon" />
              <h3>{{ qa.question }}</h3>
            </div>
            <div class="qa-meta">
              <span class="qa-time">{{ formatRelativeTime(qa.createdAt) }}</span>
            </div>
          </div>

          <div class="qa-answer">
            <p>{{ truncateAnswer(qa.answer) }}</p>
          </div>

          <div class="qa-card-footer">
            <div class="qa-tags">
              <span class="tag source">
                <FileText :size="12" />
                {{ qa.contextFragments?.length || 0 }} 个来源
              </span>
              <span class="tag model">
                <Cpu :size="12" />
                {{ qa.modelVersion || 'GLM-4' }}
              </span>
              <span v-if="qa.tokenUsage" class="tag tokens">
                {{ qa.tokenUsage }} tokens
              </span>
            </div>
            <span class="qa-expand">
              <span>查看详情</span>
              <ChevronRight :size="14" />
            </span>
          </div>
        </article>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <div class="empty-icon-wrapper">
          <MessageSquare :size="32" />
        </div>
        <h3>{{ searchQuery ? '未找到匹配的问答记录' : '暂无问答记录' }}</h3>
        <p class="empty-hint">{{ searchQuery ? '试试换个关键词搜索' : '在视频学习页面点击 AI 助教开始提问' }}</p>
        <button v-if="!searchQuery" class="empty-action" @click="goToVideoLearning">
          <MessageSquare :size="16" />
          去提问
        </button>
      </div>

      <!-- 分页 -->
      <div v-if="pagination.totalPages > 1" class="pagination">
        <button
          class="page-btn"
          :disabled="pagination.page === 1"
          @click="changePage(pagination.page - 1)"
          aria-label="上一页"
        >
          <ChevronLeft :size="16" />
        </button>
        <template v-for="p in visiblePages" :key="p">
          <span v-if="p === '...'" class="page-ellipsis">...</span>
          <button
            v-else
            :class="['page-btn', 'page-number', { active: p === pagination.page }]"
            @click="changePage(p as number)"
          >
            {{ p }}
          </button>
        </template>
        <button
          class="page-btn"
          :disabled="pagination.page === pagination.totalPages"
          @click="changePage(pagination.page + 1)"
          aria-label="下一页"
        >
          <ChevronRight :size="16" />
        </button>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="selectedQa" class="modal-overlay" @click.self="closeDetail">
          <div class="modal-content" role="dialog" aria-labelledby="modal-title">
            <div class="modal-header">
              <h2 id="modal-title">问答详情</h2>
              <button class="modal-close" @click="closeDetail" aria-label="关闭">
                <X :size="18" />
              </button>
            </div>
            <div class="modal-body">
              <div class="detail-section question-section">
                <div class="detail-label">
                  <HelpCircle :size="14" />
                  <span>问题</span>
                </div>
                <p class="detail-text question-text">{{ selectedQa.question }}</p>
              </div>
              <div class="detail-section answer-section">
                <div class="detail-label">
                  <Bot :size="14" />
                  <span>AI 回答</span>
                </div>
                <div class="detail-text answer-text" v-html="formatAnswer(selectedQa.answer)"></div>
              </div>
              <div v-if="selectedQa.contextFragments?.length" class="detail-section sources-section">
                <div class="detail-label">
                  <FileText :size="14" />
                  <span>参考来源 ({{ selectedQa.contextFragments.length }})</span>
                </div>
                <div class="source-list">
                  <div
                    v-for="(frag, idx) in selectedQa.contextFragments"
                    :key="idx"
                    class="source-item"
                  >
                    <span class="source-time">{{ formatSeconds(frag.startTime) }} - {{ formatSeconds(frag.endTime) }}</span>
                    <div class="source-relevance">
                      <div class="relevance-bar">
                        <div class="relevance-fill" :style="{ width: (frag.relevance * 100) + '%' }"></div>
                      </div>
                      <span class="relevance-label">相关度 {{ Math.round(frag.relevance * 100) }}%</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="detail-footer">
                <span class="detail-meta">
                  <Clock :size="12" />
                  {{ formatRelativeTime(selectedQa.createdAt) }}
                </span>
                <span class="detail-meta">
                  <Cpu :size="12" />
                  {{ selectedQa.modelVersion || 'GLM-4' }}
                </span>
                <span v-if="selectedQa.tokenUsage" class="detail-meta">
                  {{ selectedQa.tokenUsage }} tokens
                </span>
              </div>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import {
  Search, MessageSquare, ChevronLeft, ChevronRight, HelpCircle,
  FileText, Cpu, X, ArrowUpDown, Bot, Clock
} from 'lucide-vue-next'
import StudentTopNav from '@/components/ui/StudentTopNav.vue'
import { usePagination } from '@/composables/usePagination'
import type { QaLogDTO } from '@/types/api'
import { qaApi } from '@/api/qa'

const router = useRouter()

// 筛选和排序状态
const searchQuery = ref('')
const timeFilter = ref('all')
const sortBy = ref<'latest' | 'earliest'>('latest')

// 时间筛选选项
const timeOptions = [
  { label: '今天', value: 'today' },
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '全部', value: 'all' },
]

// 详情弹窗
const selectedQa = ref<QaLogDTO | null>(null)

// 分页功能
const { data: qaList, isLoading, pagination, changePage } = usePagination<QaLogDTO>({
  fetchFn: async (page, pageSize) => {
    const response = await qaApi.getHistory({ page, pageSize })
    return {
      records: response.data.records,
      total: response.data.total,
      totalPages: response.data.totalPages,
    }
  },
  defaultPageSize: 8,
  immediate: true,
})

// 按时间筛选
const filteredByTime = computed(() => {
  if (timeFilter.value === 'all') return qaList.value
  const now = Date.now()
  const thresholds: Record<string, number> = {
    today: 86400000,
    week: 604800000,
    month: 2592000000,
  }
  const threshold = thresholds[timeFilter.value]
  if (!threshold) return qaList.value
  return qaList.value.filter(qa => (now - new Date(qa.createdAt).getTime()) < threshold)
})

// 搜索过滤
const filteredQaList = computed(() => {
  let list = filteredByTime.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    list = list.filter(qa =>
      qa.question.toLowerCase().includes(query) ||
      qa.answer.toLowerCase().includes(query)
    )
  }
  if (sortBy.value === 'earliest') {
    list = [...list].sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime())
  }
  return list
})

// 分页页码
const visiblePages = computed(() => {
  const total = pagination.totalPages
  const current = pagination.page
  if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1)
  const pages: (number | string)[] = [1]
  if (current > 3) pages.push('...')
  for (let i = Math.max(2, current - 1); i <= Math.min(total - 1, current + 1); i++) {
    pages.push(i)
  }
  if (current < total - 2) pages.push('...')
  pages.push(total)
  return pages
})

// 搜索防抖
let searchTimer: number | null = null
const debouncedSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = window.setTimeout(() => {
    // 搜索已在 computed 中实时过滤
  }, 200)
}

const clearSearch = () => {
  searchQuery.value = ''
}

const setTimeFilter = (value: string) => {
  timeFilter.value = value
}

const toggleSort = () => {
  sortBy.value = sortBy.value === 'latest' ? 'earliest' : 'latest'
}

// 截断回答
const truncateAnswer = (answer: string) => {
  const lines = answer.split('\n').filter(l => l.trim())
  if (lines.length > 3) return lines.slice(0, 3).join('\n') + '...'
  return answer
}

// 格式化回答（简单换行处理）
const formatAnswer = (answer: string) => {
  return answer.replace(/\n/g, '<br>')
}

// 相对时间
const formatRelativeTime = (date: string) => {
  const now = Date.now()
  const diff = now - new Date(date).getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes} 分钟前`
  if (hours < 24) return `${hours} 小时前`
  if (days < 30) return `${days} 天前`
  return new Date(date).toLocaleDateString('zh-CN')
}

// 格式化秒数
const formatSeconds = (seconds: number) => {
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

// 详情弹窗
const openDetail = (qa: QaLogDTO) => {
  selectedQa.value = qa
}

const closeDetail = () => {
  selectedQa.value = null
}

// 跳转到视频学习页
const goToVideoLearning = () => {
  router.push('/student/home')
}

// ESC 关闭弹窗
watch(selectedQa, (val) => {
  if (val) {
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') closeDetail()
    }
    window.addEventListener('keydown', handler)
    // 清理
    watch(selectedQa, (newVal) => {
      if (!newVal) window.removeEventListener('keydown', handler)
    }, { once: true })
  }
})
</script>

<style scoped>
.qa-page {
  min-height: 100vh;
  background: var(--color-bg-canvas);
  display: flex;
  flex-direction: column;
}

.qa-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px 24px 48px;
  width: 100%;
}

/* 页面标题 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0 0 4px;
}

.page-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin: 0;
}

.header-stats {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.stat-chip {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: var(--color-accent-subtle);
  color: var(--color-accent);
  border-radius: var(--radius-full);
  font-size: var(--text-sm);
  font-weight: 500;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.filter-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  flex: 1;
}

.search-box {
  position: relative;
  width: 240px;
}

.search-icon {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary);
  pointer-events: none;
}

.search-input {
  width: 100%;
  padding: 8px 32px 8px 34px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: var(--text-sm);
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

.search-input::placeholder {
  color: var(--color-text-tertiary);
}

.search-input:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

.search-clear {
  position: absolute;
  right: 6px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: var(--color-text-tertiary);
  cursor: pointer;
  padding: 2px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
}

.search-clear:hover {
  color: var(--color-text-primary);
  background: var(--color-bg-hover);
}

.time-filter {
  display: flex;
  gap: 4px;
}

.time-pill {
  padding: 6px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  background: var(--color-bg-card);
  color: var(--color-text-secondary);
  cursor: pointer;
  font-size: var(--text-sm);
  transition: all 0.15s ease;
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

.sort-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-secondary);
  cursor: pointer;
  font-size: var(--text-sm);
  transition: all 0.15s ease;
}

.sort-btn:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 64px 0;
  color: var(--color-text-tertiary);
  font-size: var(--text-sm);
}

.loading-spinner {
  width: 24px;
  height: 24px;
  border: 2px solid var(--color-border);
  border-top-color: var(--color-accent);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 问答列表 */
.qa-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.qa-card {
  padding: 20px;
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all 0.2s ease;
}

.qa-card:hover {
  border-color: var(--color-accent);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.qa-card:active {
  transform: translateY(0);
}

.qa-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.qa-question {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  flex: 1;
  min-width: 0;
}

.question-icon {
  color: var(--color-accent);
  flex-shrink: 0;
  margin-top: 2px;
}

.qa-question h3 {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
  line-height: 1.5;
}

.qa-meta {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.qa-time {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  white-space: nowrap;
}

.qa-answer {
  margin-bottom: 16px;
}

.qa-answer p {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.qa-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.qa-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 3px 8px;
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: 500;
}

.tag.source {
  background: var(--color-accent-subtle);
  color: var(--color-accent);
}

.tag.model {
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
}

.tag.tokens {
  background: var(--color-bg-elevated);
  color: var(--color-text-tertiary);
}

.qa-expand {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  transition: color 0.15s ease;
}

.qa-card:hover .qa-expand {
  color: var(--color-accent);
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 64px 24px;
  text-align: center;
}

.empty-icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-full);
  background: var(--color-accent-subtle);
  color: var(--color-accent);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0 0 8px;
}

.empty-hint {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin: 0 0 20px;
}

.empty-action {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s ease;
}

.empty-action:hover {
  background: var(--color-accent-hover);
}

/* 分页 */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-top: 32px;
}

.page-btn {
  min-width: 36px;
  height: 36px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--text-sm);
  padding: 0 4px;
  transition: all 0.15s ease;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-btn.active {
  background: var(--color-accent);
  color: var(--color-accent-text);
  border-color: var(--color-accent);
}

.page-ellipsis {
  width: 36px;
  text-align: center;
  color: var(--color-text-tertiary);
  font-size: var(--text-sm);
}

/* 详情弹窗 */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
  padding: 24px;
}

.modal-content {
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  width: 100%;
  max-width: 640px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--color-border);
  flex-shrink: 0;
}

.modal-header h2 {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.modal-close {
  background: none;
  border: none;
  color: var(--color-text-secondary);
  cursor: pointer;
  padding: 6px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  transition: all 0.15s ease;
}

.modal-close:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-xs);
  font-weight: 600;
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 8px;
}

.detail-text {
  font-size: var(--text-sm);
  color: var(--color-text-primary);
  line-height: 1.7;
  margin: 0;
}

.question-text {
  font-weight: 500;
  font-size: var(--text-base);
}

.answer-section {
  padding: 16px;
  background: var(--color-bg-elevated);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
}

.source-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.source-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  background: var(--color-bg-elevated);
  border-radius: var(--radius-md);
}

.source-time {
  font-family: 'Source Code Pro', monospace;
  font-size: var(--text-xs);
  color: var(--color-accent);
  background: var(--color-accent-subtle);
  padding: 2px 8px;
  border-radius: var(--radius-sm);
  white-space: nowrap;
}

.source-relevance {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.relevance-bar {
  flex: 1;
  max-width: 120px;
  height: 4px;
  background: var(--color-border);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.relevance-fill {
  height: 100%;
  background: var(--color-accent);
  border-radius: var(--radius-full);
  transition: width 0.3s ease;
}

.relevance-label {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  white-space: nowrap;
}

.detail-footer {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  padding-top: 16px;
  border-top: 1px solid var(--color-border);
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
}

/* 弹窗动画 */
.modal-enter-active {
  transition: opacity 0.2s ease;
}
.modal-enter-active .modal-content {
  transition: opacity 0.2s ease, transform 0.2s ease;
}
.modal-leave-active {
  transition: opacity 0.15s ease;
}
.modal-leave-active .modal-content {
  transition: opacity 0.15s ease, transform 0.15s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
.modal-enter-from .modal-content {
  transform: translateY(16px) scale(0.97);
}
.modal-leave-to .modal-content {
  transform: translateY(8px) scale(0.98);
}

/* 响应式 */
@media (max-width: 768px) {
  .qa-container {
    padding: 0 16px 32px;
  }

  .page-header {
    flex-direction: column;
    gap: 12px;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-left {
    flex-direction: column;
  }

  .search-box {
    width: 100%;
  }

  .time-filter {
    justify-content: space-between;
  }

  .qa-card {
    padding: 16px;
  }

  .qa-answer p {
    -webkit-line-clamp: 3;
  }

  .modal-overlay {
    padding: 16px;
  }

  .modal-content {
    max-height: 90vh;
  }
}
</style>
