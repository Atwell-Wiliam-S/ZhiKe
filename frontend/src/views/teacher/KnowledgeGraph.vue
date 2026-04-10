<script setup lang="ts">
import { ref, computed } from 'vue'
import { Plus, Pencil, Eye, Trash2, Search } from 'lucide-vue-next'

// 模拟数据
const courses = [
  { id: 1, name: '高等数学' },
  { id: 2, name: '线性代数' },
  { id: 3, name: '概率统计' },
]

const videos = [
  { id: 1, name: '函数极限与连续' },
  { id: 2, name: '导数与微分' },
  { id: 3, name: '积分及其应用' },
]

const segments = [
  {
    id: 1,
    title: '函数极限的定义',
    timeRange: '02:30 - 05:15',
    content: '本节介绍函数极限的ε-δ定义，通过几何直观和代数分析帮助学生理解极限的本质概念。',
    keywords: ['极限', 'ε-δ定义', '函数'],
    status: 'confirmed', // confirmed, pending, needs_修改
  },
  {
    id: 2,
    title: '导数的几何意义',
    timeRange: '06:45 - 10:20',
    content: '通过切线斜率的概念引入导数，讲解导数的几何意义和物理意义。',
    keywords: ['导数', '切线', '斜率'],
    status: 'pending',
  },
  {
    id: 3,
    title: '不定积分的计算',
    timeRange: '12:10 - 15:30',
    content: '介绍不定积分的基本公式和计算方法，包括换元积分法和分部积分法。',
    keywords: ['不定积分', '换元法', '分部积分'],
    status: 'needs_modification',
  },
  {
    id: 4,
    title: '定积分的应用',
    timeRange: '16:45 - 20:10',
    content: '讲解定积分在几何和物理中的应用，包括面积、体积、功等计算。',
    keywords: ['定积分', '面积', '体积'],
    status: 'confirmed',
  },
  {
    id: 5,
    title: '微分方程初步',
    timeRange: '21:30 - 25:45',
    content: '介绍一阶微分方程的解法，包括可分离变量方程和线性微分方程。',
    keywords: ['微分方程', '可分离变量', '线性方程'],
    status: 'pending',
  },
]

// 响应式数据
const selectedCourse = ref(1)
const selectedVideo = ref(1)
const searchQuery = ref('')
const selectedSegments = ref<number[]>([])
const isSelectAll = ref(false)

// 计算属性
const filteredSegments = computed(() => {
  return segments.filter(segment => 
    segment.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    segment.content.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

// 方法
const handleSelectAll = () => {
  if (isSelectAll.value) {
    selectedSegments.value = filteredSegments.value.map(s => s.id)
  } else {
    selectedSegments.value = []
  }
}

const handleSegmentSelect = (id: number) => {
  const index = selectedSegments.value.indexOf(id)
  if (index > -1) {
    selectedSegments.value.splice(index, 1)
  } else {
    selectedSegments.value.push(id)
  }
  isSelectAll.value = selectedSegments.value.length === filteredSegments.value.length
}

const handleAddSegment = () => {
  // 添加片段逻辑
  console.log('添加片段')
}

const handleEditSegment = (id: number) => {
  // 编辑片段逻辑
  console.log('编辑片段:', id)
}

const handlePreviewSegment = (id: number) => {
  // 预览片段逻辑
  console.log('预览片段:', id)
}

const handleDeleteSegment = (id: number) => {
  // 删除片段逻辑
  console.log('删除片段:', id)
}

const handleBatchDelete = () => {
  // 批量删除逻辑
  console.log('批量删除:', selectedSegments.value)
}

const handleBatchConfirm = () => {
  // 批量确认逻辑
  console.log('批量确认:', selectedSegments.value)
}

// 计算状态颜色
const getStatusColor = (status: string) => {
  switch (status) {
    case 'confirmed': return '#008b00'
    case 'pending': return '#F7A501'
    case 'needs_modification': return '#fb565b'
    default: return '#9ea096'
  }
}

const getStatusText = (status: string) => {
  switch (status) {
    case 'confirmed': return '已确认'
    case 'pending': return '待审核'
    case 'needs_modification': return '需修改'
    default: return '未知'
  }
}
</script>

<template>
  <div class="knowledge-graph-page">
    <!-- 顶部筛选栏 -->
    <div class="top-bar">
      <nav aria-label="面包屑" class="breadcrumb">
        <ol>
          <li><a href="#">课程管理</a></li>
          <li><span>&gt;</span></li>
          <li><a href="#">{{ courses.find(c => c.id === selectedCourse)?.name }}</a></li>
          <li><span>&gt;</span></li>
          <li><span>知识片段</span></li>
        </ol>
      </nav>
      
      <div class="filters">
        <select v-model="selectedCourse" class="filter-select">
          <option v-for="course in courses" :key="course.id" :value="course.id">
            {{ course.name }}
          </option>
        </select>
        
        <select v-model="selectedVideo" class="filter-select">
          <option v-for="video in videos" :key="video.id" :value="video.id">
            {{ video.name }}
          </option>
        </select>
        
        <div class="search-box">
          <Search :size="16" :stroke-width="1.5" />
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索片段..." 
            class="search-input"
          />
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 左栏：视频预览 -->
      <div class="left-column">
        <div class="video-preview">
          <div class="video-player">
            <div class="video-placeholder">
              <span>视频播放器</span>
            </div>
            <!-- 时间轴叠加 -->
            <div class="timeline" aria-label="片段状态时间轴">
              <div 
                v-for="segment in segments" 
                :key="segment.id"
                class="timeline-segment"
                :style="{ backgroundColor: getStatusColor(segment.status) }"
                :aria-label="`${getStatusText(segment.status)}: ${segment.timeRange}`"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右栏：片段列表 -->
      <div class="right-column">
        <!-- 底部操作栏 -->
        <div class="bottom-actions">
          <button class="add-button" @click="handleAddSegment">
            <Plus :size="16" :stroke-width="1.5" />
            <span>添加片段</span>
          </button>
          
          <div class="batch-actions">
            <label class="select-all">
              <input 
                type="checkbox" 
                v-model="isSelectAll" 
                @change="handleSelectAll"
                aria-label="全选所有片段"
              />
              <span>全选</span>
            </label>
            <button 
              class="batch-button" 
              @click="handleBatchDelete"
              :disabled="selectedSegments.length === 0"
            >
              批量删除
            </button>
            <button 
              class="batch-button primary"
              @click="handleBatchConfirm"
              :disabled="selectedSegments.length === 0"
            >
              批量确认
            </button>
          </div>
        </div>

        <!-- 片段卡片列表 -->
        <div class="segment-list">
          <div 
            v-for="segment in filteredSegments" 
            :key="segment.id"
            class="segment-card"
          >
            <div class="card-header">
              <input 
                type="checkbox" 
                :checked="selectedSegments.includes(segment.id)"
                @change="handleSegmentSelect(segment.id)"
                class="segment-checkbox"
              />
              <h3 class="segment-title">{{ segment.title }}</h3>
              <div class="segment-time">{{ segment.timeRange }}</div>
            </div>
            
            <p class="segment-content">{{ segment.content }}</p>
            
            <div class="segment-keywords">
              <span 
                v-for="keyword in segment.keywords" 
                :key="keyword"
                class="keyword-pill"
              >
                {{ keyword }}
              </span>
            </div>
            
            <div class="segment-actions">
              <button 
                class="action-button" 
                @click="handleEditSegment(segment.id)"
                aria-label="编辑片段"
              >
                <Pencil :size="16" :stroke-width="1.5" />
              </button>
              <button 
                class="action-button" 
                @click="handlePreviewSegment(segment.id)"
                aria-label="预览片段"
              >
                <Eye :size="16" :stroke-width="1.5" />
              </button>
              <button 
                class="action-button delete"
                @click="handleDeleteSegment(segment.id)"
                aria-label="删除片段"
              >
                <Trash2 :size="16" :stroke-width="1.5" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.knowledge-graph-page {
  padding: var(--space-6);
  max-width: var(--container-xl);
  margin: 0 auto;
}

/* 顶部筛选栏 */
.top-bar {
  margin-bottom: var(--space-6);
}

.breadcrumb ol {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
  padding: 0;
  list-style: none;
}

.breadcrumb li {
  color: var(--color-text-tertiary);
  font-size: var(--text-sm);
}

.breadcrumb a {
  color: var(--color-text-tertiary);
  text-decoration: none;
  transition: color var(--duration-normal) var(--ease-default);
}

.breadcrumb a:hover {
  color: var(--color-accent);
}

.filters {
  display: flex;
  gap: var(--space-3);
  align-items: center;
  background: var(--color-bg-elevated);
  padding: var(--space-4);
  border-radius: var(--radius-sm);
}

.filter-select {
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: var(--text-sm);
  cursor: pointer;
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 300px;
}

.search-box svg {
  position: absolute;
  left: var(--space-3);
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary);
}

.search-input {
  width: 100%;
  padding: var(--space-2) var(--space-3) var(--space-2) 40px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: var(--text-sm);
}

.search-input:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

/* 主内容区 */
.main-content {
  display: grid;
  grid-template-columns: 40% 60%;
  gap: var(--space-6);
}

/* 左栏：视频预览 */
.left-column {
  position: sticky;
  top: 80px;
  align-self: start;
}

.video-preview {
  width: 100%;
}

.video-player {
  position: relative;
  aspect-ratio: 16/9;
  background: var(--color-bg-elevated);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.video-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: var(--color-text-tertiary);
  font-size: var(--text-sm);
}

.timeline {
  display: flex;
  gap: 2px;
  height: 8px;
  margin-top: var(--space-2);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.timeline-segment {
  flex: 1;
  height: 100%;
  border-radius: var(--radius-full);
}

/* 右栏：片段列表 */
.right-column {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

/* 底部操作栏 */
.bottom-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-4);
}

.add-button {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: var(--color-cta-bg);
  color: var(--color-cta-text);
  border: none;
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}

.add-button:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.select-all {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  cursor: pointer;
}

.batch-button {
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: var(--text-sm);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}

.batch-button:hover:not(:disabled) {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.batch-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.batch-button.primary {
  background: var(--color-cta-bg);
  color: var(--color-cta-text);
  border-color: var(--color-cta-bg);
}

.batch-button.primary:hover:not(:disabled) {
  opacity: 0.9;
  color: var(--color-cta-text);
}

/* 片段卡片列表 */
.segment-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.segment-card {
  padding: var(--space-4);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-bg-card);
  transition: all var(--duration-normal) var(--ease-default);
}

.segment-card:hover {
  background: var(--color-bg-hover);
  border-color: var(--color-border-focus);
}

.segment-card:hover .segment-title {
  color: var(--color-accent);
}

.card-header {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}

.segment-checkbox {
  margin-top: 2px;
}

.segment-title {
  flex: 1;
  margin: 0;
  font-family: 'IBM Plex Sans', system-ui, sans-serif;
  font-size: 21.4px;
  font-weight: 700;
  color: var(--color-text-primary);
  transition: color var(--duration-normal) var(--ease-default);
}

.segment-time {
  font-family: 'Source Code Pro', Consolas, monospace;
  font-size: 14px;
  color: var(--color-text-secondary);
  white-space: nowrap;
}

.segment-content {
  margin: 0 0 var(--space-3) 0;
  color: var(--color-text-secondary);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.segment-keywords {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-1);
  margin-bottom: var(--space-3);
}

.keyword-pill {
  padding: 2px 12px;
  background: var(--color-accent-subtle);
  color: var(--color-accent);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: 500;
}

.segment-actions {
  display: flex;
  gap: var(--space-2);
  justify-content: flex-end;
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: none;
  border-radius: var(--radius-sm);
  background: transparent;
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}

.action-button:hover {
  background: var(--color-bg-hover);
  color: var(--color-accent);
}

.action-button.delete:hover {
  color: var(--color-danger);
}

/* 响应式设计 */
@media (max-width: 1023px) {
  .main-content {
    grid-template-columns: 35% 65%;
  }
}

@media (max-width: 767px) {
  .knowledge-graph-page {
    padding: var(--space-4);
  }
  
  .filters {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-select {
    width: 100%;
  }
  
  .search-box {
    max-width: none;
  }
  
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .left-column {
    position: static;
  }
  
  .bottom-actions {
    flex-direction: column;
    align-items: stretch;
    gap: var(--space-3);
  }
  
  .batch-actions {
    justify-content: space-between;
  }
}
</style>