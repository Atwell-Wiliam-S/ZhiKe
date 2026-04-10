<template>
  <div class="knowledge-graph">
    <!-- Top Filter Bar -->
    <div class="filter-bar">
      <div class="container">
        <!-- Breadcrumb -->
        <nav class="breadcrumb" aria-label="面包屑">
          <ol class="breadcrumb-list">
            <li class="breadcrumb-item">
              <a href="#" class="breadcrumb-link">课程管理</a>
            </li>
            <li class="breadcrumb-item">
              <a href="#" class="breadcrumb-link">{{ selectedCourse }}</a>
            </li>
            <li class="breadcrumb-item active">
              <span class="breadcrumb-link">知识片段</span>
            </li>
          </ol>
        </nav>

        <!-- Filters -->
        <div class="filter-controls">
          <div class="filter-group">
            <select v-model="selectedCourse" class="filter-select">
              <option value="机器学习基础">机器学习基础</option>
              <option value="深度学习进阶">深度学习进阶</option>
              <option value="计算机视觉">计算机视觉</option>
            </select>
            <select v-model="selectedVideo" class="filter-select">
              <option value="第1讲：机器学习简介">第1讲：机器学习简介</option>
              <option value="第2讲：监督学习">第2讲：监督学习</option>
              <option value="第3讲：无监督学习">第3讲：无监督学习</option>
            </select>
            <div class="search-box">
              <input
                type="text"
                v-model="searchQuery"
                placeholder="搜索知识片段..."
                class="search-input"
              />
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="content-container">
      <div class="container">
        <!-- Left Column: Video Preview -->
        <div class="left-column">
          <div class="video-preview">
            <div class="video-wrapper">
              <video class="video-player" controls>
                <source src="https://example.com/video.mp4" type="video/mp4" />
                Your browser does not support the video tag.
              </video>
            </div>
            <div class="timeline-overlay">
              <div
                v-for="(segment, index) in segments"
                :key="index"
                class="timeline-segment"
                :class="segment.status"
                :style="{
                  left: segment.startPosition + '%',
                  width: (segment.endPosition - segment.startPosition) + '%'
                }"
                :aria-label="`${getStatusText(segment.status)}: ${segment.title}`"
              ></div>
            </div>
          </div>
        </div>

        <!-- Right Column: Segment List -->
        <div class="right-column">
          <div class="segment-list">
            <div
              v-for="(segment, index) in filteredSegments"
              :key="index"
              class="segment-card"
            >
              <div class="segment-header">
                <h3 class="segment-title">{{ segment.title }}</h3>
                <div class="segment-time">
                  {{ formatTime(segment.startTime) }} - {{ formatTime(segment.endTime) }}
                </div>
              </div>
              <p class="segment-content">{{ segment.content }}</p>
              <div class="segment-tags">
                <span
                  v-for="(tag, tagIndex) in segment.tags"
                  :key="tagIndex"
                  class="tag"
                >
                  {{ tag }}
                </span>
              </div>
              <div class="segment-actions">
                <button class="action-button" @click="editSegment(index)" aria-label="编辑片段">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                  </svg>
                </button>
                <button class="action-button" @click="previewSegment(index)" aria-label="预览片段">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7z"></path>
                    <circle cx="12" cy="12" r="3"></circle>
                  </svg>
                </button>
                <button class="action-button delete" @click="deleteSegment(index)" aria-label="删除片段">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    <line x1="10" y1="11" x2="10" y2="17"></line>
                    <line x1="14" y1="11" x2="14" y2="17"></line>
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <!-- Bottom Action Bar -->
          <div class="action-bar">
            <div class="batch-actions">
              <label class="batch-checkbox">
                <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" />
                <span class="checkbox-label">全选</span>
              </label>
              <button class="batch-button" @click="batchDelete" :disabled="selectedSegments.length === 0">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="3 6 5 6 21 6"></polyline>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                  <line x1="10" y1="11" x2="10" y2="17"></line>
                  <line x1="14" y1="11" x2="14" y2="17"></line>
                </svg>
                批量删除
              </button>
              <button class="batch-button" @click="batchConfirm" :disabled="selectedSegments.length === 0">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="20 6 9 17 4 12"></polyline>
                </svg>
                批量确认
              </button>
            </div>
            <button class="add-segment-button" @click="addSegment">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
              </svg>
              添加片段
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

// State
const selectedCourse = ref('机器学习基础');
const selectedVideo = ref('第1讲：机器学习简介');
const searchQuery = ref('');
const selectAll = ref(false);
const selectedSegments = ref<number[]>([]);

// Segments data
const segments = ref([
  {
    id: 1,
    title: '什么是机器学习',
    startTime: 60,
    endTime: 180,
    startPosition: 10,
    endPosition: 30,
    content: '机器学习是人工智能的一个分支，通过算法让计算机从数据中学习，而不需要显式编程。',
    tags: ['机器学习', '人工智能', '定义'],
    status: 'confirmed'
  },
  {
    id: 2,
    title: '监督学习与无监督学习',
    startTime: 180,
    endTime: 300,
    startPosition: 30,
    endPosition: 50,
    content: '监督学习使用标记数据进行训练，无监督学习从无标记数据中发现模式。',
    tags: ['监督学习', '无监督学习', '学习方法'],
    status: 'pending'
  },
  {
    id: 3,
    title: '线性回归',
    startTime: 300,
    endTime: 420,
    startPosition: 50,
    endPosition: 70,
    content: '线性回归是一种用于预测连续值的监督学习算法。',
    tags: ['线性回归', '监督学习', '算法'],
    status: 'rejected'
  },
  {
    id: 4,
    title: '模型评估',
    startTime: 420,
    endTime: 540,
    startPosition: 70,
    endPosition: 90,
    content: '模型评估是衡量机器学习模型性能的过程，常用指标包括准确率、精确率、召回率等。',
    tags: ['模型评估', '性能指标', '机器学习'],
    status: 'confirmed'
  }
]);

// Computed
const filteredSegments = computed(() => {
  if (!searchQuery.value) return segments.value;
  const query = searchQuery.value.toLowerCase();
  return segments.value.filter(segment => 
    segment.title.toLowerCase().includes(query) ||
    segment.content.toLowerCase().includes(query) ||
    segment.tags.some(tag => tag.toLowerCase().includes(query))
  );
});

// Methods
const formatTime = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = Math.floor(seconds % 60);
  return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
};

const getStatusText = (status: string): string => {
  switch (status) {
    case 'confirmed': return '已确认';
    case 'pending': return '待审核';
    case 'rejected': return '需修改';
    default: return '未知';
  }
};

const toggleSelectAll = () => {
  if (selectAll.value) {
    selectedSegments.value = segments.value.map((_, index) => index);
  } else {
    selectedSegments.value = [];
  }
};

const addSegment = () => {
  // 简单实现，实际项目中可使用弹窗
  segments.value.push({
    id: Date.now(),
    title: '新片段',
    startTime: 0,
    endTime: 60,
    startPosition: 0,
    endPosition: 10,
    content: '片段内容',
    tags: [],
    status: 'pending'
  });
};

const editSegment = (index: number) => {
  // 简单实现，实际项目中可使用弹窗
  console.log('编辑片段:', segments.value[index]);
};

const previewSegment = (index: number) => {
  // 简单实现，实际项目中可跳转到视频对应时间点
  console.log('预览片段:', segments.value[index]);
};

const deleteSegment = (index: number) => {
  if (confirm('确定要删除这个片段吗？')) {
    segments.value.splice(index, 1);
    selectedSegments.value = selectedSegments.value.filter(i => i !== index);
  }
};

const batchDelete = () => {
  if (selectedSegments.value.length === 0) return;
  if (confirm(`确定要删除选中的 ${selectedSegments.value.length} 个片段吗？`)) {
    // 按索引从大到小删除，避免索引偏移
    selectedSegments.value.sort((a, b) => b - a).forEach(index => {
      segments.value.splice(index, 1);
    });
    selectedSegments.value = [];
    selectAll.value = false;
  }
};

const batchConfirm = () => {
  if (selectedSegments.value.length === 0) return;
  selectedSegments.value.forEach(index => {
    segments.value[index].status = 'confirmed';
  });
  selectedSegments.value = [];
  selectAll.value = false;
};
</script>

<style scoped>
.knowledge-graph {
  min-height: 100vh;
  background: var(--color-bg-canvas, #fdfdf8);
  color: var(--color-text-primary, #0f172a);
}

/* Filter Bar */
.filter-bar {
  background: var(--color-bg-elevated, #eeefe9);
  padding: 16px 0;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 24px;
}

/* Breadcrumb */
.breadcrumb {
  margin-bottom: 16px;
}

.breadcrumb-list {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  gap: 8px;
}

.breadcrumb-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breadcrumb-item:not(:last-child)::after {
  content: '/';
  color: var(--color-text-tertiary, #94a3b8);
}

.breadcrumb-link {
  color: var(--color-text-tertiary, #94a3b8);
  text-decoration: none;
  font-size: 0.875rem;
  transition: color 0.2s ease;
}

.breadcrumb-link:hover {
  color: var(--color-text-primary, #0f172a);
}

.breadcrumb-item.active .breadcrumb-link {
  color: var(--color-text-primary, #0f172a);
  font-weight: 600;
}

/* Filter Controls */
.filter-controls {
  margin-bottom: 16px;
}

.filter-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #0f172a);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-select:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 8px 12px 8px 32px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #0f172a);
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.search-box svg {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary, #94a3b8);
}

/* Main Content */
.content-container {
  padding: 24px 0;
}

.content-container .container {
  display: grid;
  grid-template-columns: 40% 60%;
  gap: 24px;
}

/* Left Column */
.left-column {
  position: sticky;
  top: 80px;
  align-self: flex-start;
}

.video-preview {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  overflow: hidden;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.video-wrapper {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 */
}

.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
}

.timeline-overlay {
  position: relative;
  height: 8px;
  background: var(--color-bg-input, #f1f5f9);
  border-radius: var(--radius-full, 9999px);
  margin: 16px;
}

.timeline-segment {
  position: absolute;
  height: 100%;
  border-radius: var(--radius-full, 9999px);
  transition: all 0.2s ease;
  cursor: pointer;
}

.timeline-segment.confirmed {
  background: #008b00;
}

.timeline-segment.pending {
  background: #F7A501;
}

.timeline-segment.rejected {
  background: #fb565b;
}

.timeline-segment:hover {
  opacity: 0.8;
  transform: scaleY(1.2);
}

/* Right Column */
.right-column {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.segment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.segment-card {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  padding: 16px;
  transition: all 0.2s ease;
  cursor: pointer;
}

.segment-card:hover {
  background: var(--color-bg-hover, #f1f5f9);
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  transform: translateY(-1px);
}

.segment-card:hover .segment-title {
  color: #F54E00;
}

.segment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.segment-title {
  font-size: 1.333rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
  font-family: 'IBM Plex Sans', sans-serif;
  transition: color 0.2s ease;
}

.segment-time {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  font-family: 'Source Code Pro', monospace;
  white-space: nowrap;
}

.segment-content {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  line-height: 1.5;
  margin: 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.segment-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin: 12px 0;
}

.tag {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  padding: 4px 8px;
  border-radius: var(--radius-full, 9999px);
  font-size: 0.75rem;
  font-weight: 600;
}

.segment-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  justify-content: flex-end;
}

.action-button {
  background: none;
  border: none;
  color: #1e1f23;
  cursor: pointer;
  padding: 6px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.action-button.delete:hover {
  color: var(--color-danger, #ef4444);
}

/* Action Bar */
.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.batch-checkbox {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
}

.batch-checkbox input[type="checkbox"] {
  accent-color: var(--color-accent, #6366f1);
}

.batch-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: var(--radius-sm, 4px);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.batch-button:hover:not(:disabled) {
  background: #333;
  transform: translateY(-1px);
}

.batch-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.add-segment-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: var(--radius-sm, 4px);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.add-segment-button:hover {
  background: #333;
  transform: translateY(-1px);
}

/* Responsive Design */
@media (max-width: 1023px) {
  .content-container .container {
    grid-template-columns: 35% 65%;
  }
}

@media (max-width: 767px) {
  .content-container .container {
    grid-template-columns: 1fr;
  }

  .left-column {
    position: static;
  }

  .filter-group {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    max-width: none;
  }

  .action-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .batch-actions {
    justify-content: center;
  }

  .add-segment-button {
    justify-content: center;
  }
}

/* Dark Theme Support */
@media (prefers-color-scheme: dark) {
  :root {
    --color-bg-canvas: #0f172a;
    --color-bg-card: #1e293b;
    --color-bg-elevated: #334155;
    --color-bg-input: #334155;
    --color-bg-hover: #334155;
    --color-text-primary: #f8fafc;
    --color-text-secondary: #cbd5e1;
    --color-text-tertiary: #94a3b8;
    --color-border: #334155;
    --color-border-focus: #6366f1;
    --color-accent: #6366f1;
    --color-accent-subtle: rgba(99, 102, 241, 0.1);
    --color-danger: #ef4444;
    --radius-sm: 4px;
    --radius-full: 9999px;
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.3);
  }
}

/* Light Theme */
@media (prefers-color-scheme: light) {
  :root {
    --color-bg-canvas: #fdfdf8;
    --color-bg-card: #ffffff;
    --color-bg-elevated: #eeefe9;
    --color-bg-input: #f1f5f9;
    --color-bg-hover: #f1f5f9;
    --color-text-primary: #0f172a;
    --color-text-secondary: #64748b;
    --color-text-tertiary: #94a3b8;
    --color-border: #e2e8f0;
    --color-border-focus: #0075de;
    --color-accent: #0075de;
    --color-accent-subtle: rgba(0, 117, 222, 0.15);
    --color-danger: #ef4444;
    --radius-sm: 4px;
    --radius-full: 9999px;
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  }
}
</style>