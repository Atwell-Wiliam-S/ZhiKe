<template>
  <div class="video-learning">
    <!-- Top Navigation -->
    <header class="top-nav">
      <div class="nav-container">
        <div class="nav-left">
          <button class="back-button" @click="goBack" aria-label="返回">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="15 18 9 12 15 6"></polyline>
            </svg>
          </button>
          <h1 class="page-title">视频学习</h1>
        </div>
        <div class="nav-right">
          <button
            class="nav-button theme-toggle"
            @click="toggleTheme"
            aria-label="切换主题"
          >
            <svg v-if="isDarkTheme" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="5"></circle>
              <line x1="12" y1="1" x2="12" y2="3"></line>
              <line x1="12" y1="21" x2="12" y2="23"></line>
              <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
              <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
              <line x1="1" y1="12" x2="3" y2="12"></line>
              <line x1="21" y1="12" x2="23" y2="12"></line>
              <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
              <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
            </svg>
          </button>
          <div class="role-badge">
            学生
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="main-content">
      <div class="video-container">
        <!-- Left Column (70%) -->
        <div class="left-column">
          <!-- Video Player -->
          <div class="video-player-container">
            <video
              ref="videoRef"
              class="video-player"
              controls
              @timeupdate="handleTimeUpdate"
              @loadedmetadata="handleLoadedMetadata"
            >
              <source src="https://www.w3schools.com/html/mov_bbb.mp4" type="video/mp4">
              Your browser does not support the video tag.
            </video>
            <div class="video-info">
              <h2 class="video-title">机器学习基础 - 线性回归</h2>
              <div class="video-meta">
                <span class="video-duration">15:30</span>
                <span class="video-views">1,234 次观看</span>
                <span class="video-date">2023-10-01</span>
              </div>
            </div>
          </div>

          <!-- Knowledge Points Timeline -->
          <div class="timeline-container">
            <h3 class="timeline-title">知识点时间轴</h3>
            <div class="timeline">
              <div 
                v-for="(point, index) in knowledgePoints" 
                :key="index"
                class="timeline-point"
                :class="{ 'active': isPointActive(point) }"
                :style="{ left: point.position + '%' }"
                @click="seekTo(point.time)"
                :aria-label="`知识点: ${point.title} 在 ${formatTime(point.time)}`"
              >
                <div class="timeline-dot"></div>
                <div class="timeline-tooltip">
                  <div class="tooltip-title">{{ point.title }}</div>
                  <div class="tooltip-time">{{ formatTime(point.time) }}</div>
                </div>
              </div>
              <div class="timeline-progress" :style="{ width: progress + '%' }"></div>
              <div class="timeline-handle" :style="{ left: progress + '%' }"></div>
            </div>
          </div>

          <!-- AI Q&A Panel -->
          <div class="ai-panel">
            <div class="ai-panel-header">
              <div class="ai-title">
                <h3>AI 助教</h3>
                <span class="ai-status">
                  <span class="status-dot"></span>
                  <span class="status-text">在线</span>
                </span>
              </div>
              <button class="clear-button" @click="clearChat" aria-label="清空对话">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="3 6 5 6 21 6"></polyline>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                  <line x1="10" y1="11" x2="10" y2="17"></line>
                  <line x1="14" y1="11" x2="14" y2="17"></line>
                </svg>
              </button>
            </div>
            <div class="ai-messages" ref="messagesRef" role="log" aria-live="polite">
              <div 
                v-for="(message, index) in messages" 
                :key="index"
                class="message"
                :class="{ 'user-message': message.type === 'user', 'ai-message': message.type === 'ai' }"
              >
                <div class="message-content">
                  {{ message.content }}
                </div>
                <div v-if="message.timestamp" class="message-time">{{ message.timestamp }}</div>
              </div>
              <div v-if="isTyping" class="message ai-message">
                <div class="message-content">
                  {{ typingText }}<span class="typing-cursor">▊</span>
                </div>
              </div>
            </div>
            <div class="ai-input">
              <textarea
                v-model="inputMessage"
                class="input-field"
                placeholder="向 AI 助教提问..."
                @keydown.enter.exact="sendMessage"
                @keydown.enter.shift="$event.preventDefault()"
                rows="1"
                aria-label="向 AI 助教提问"
              ></textarea>
              <button 
                class="send-button"
                @click="sendMessage"
                :disabled="!inputMessage.trim() || isTyping"
                aria-label="发送消息"
              >
                <svg v-if="!isTyping" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <line x1="22" y1="2" x2="11" y2="13"></line>
                  <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
                </svg>
                <div v-else class="spinner"></div>
              </button>
            </div>
          </div>
        </div>

        <!-- Right Column (30%) -->
        <div class="right-column">
          <!-- Knowledge Outline -->
          <div class="outline-container">
            <div class="outline-header">
              <h3>知识大纲</h3>
              <button class="toggle-button" @click="toggleOutline" aria-label="折叠/展开大纲">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="6 9 12 15 18 9"></polyline>
                </svg>
              </button>
            </div>
            <div v-if="isOutlineOpen" class="outline-content">
              <details v-for="(chapter, chapterIndex) in knowledgeOutline" :key="chapterIndex" open>
                <summary class="chapter-title">{{ chapter.title }}</summary>
                <ul class="chapter-topics">
                  <li 
                    v-for="(topic, topicIndex) in chapter.topics" 
                    :key="topicIndex"
                    class="topic-item"
                    :class="{ 'active': isTopicActive(topic) }"
                    @click="seekTo(topic.time)"
                  >
                    <span class="topic-title">{{ topic.title }}</span>
                    <span class="topic-time">{{ formatTimeRange(topic.timeRange) }}</span>
                  </li>
                </ul>
              </details>
            </div>
          </div>

          <!-- My Notes -->
          <div class="notes-container">
            <div class="notes-header">
              <h3>我的笔记</h3>
              <button class="add-button" @click="addNote" aria-label="添加笔记">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
              </button>
            </div>
            <div class="notes-list">
              <div 
                v-for="(note, index) in notes" 
                :key="index"
                class="note-item"
              >
                <div class="note-time">{{ note.time }}</div>
                <div class="note-content">{{ note.content }}</div>
                <div class="note-actions">
                  <button class="note-action-button" aria-label="编辑笔记">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                      <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                    </svg>
                  </button>
                  <button class="note-action-button" aria-label="删除笔记">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <polyline points="3 6 5 6 21 6"></polyline>
                      <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                      <line x1="10" y1="11" x2="10" y2="17"></line>
                      <line x1="14" y1="11" x2="14" y2="17"></line>
                    </svg>
                  </button>
                </div>
              </div>
              <div v-if="notes.length === 0" class="empty-notes">
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                  <polyline points="14 2 14 8 20 8"></polyline>
                  <line x1="16" y1="13" x2="8" y2="13"></line>
                  <line x1="16" y1="17" x2="8" y2="17"></line>
                  <polyline points="10 9 9 9 8 9"></polyline>
                </svg>
                <p>还没有笔记，开始添加吧！</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useThemeStore } from '@/stores/theme';

const router = useRouter();
const themeStore = useThemeStore();

// Refs
const videoRef = ref<HTMLVideoElement | null>(null);
const messagesRef = ref<HTMLElement | null>(null);

// State
const isOutlineOpen = ref(true);
const inputMessage = ref('');
const isTyping = ref(false);
const typingText = ref('');
const progress = ref(0);
const duration = ref(0);

// Computed properties
const isDarkTheme = computed(() => themeStore.isDark);

// Data
const knowledgePoints = [
  { id: 1, title: '线性回归介绍', time: 0, position: 0 },
  { id: 2, title: '最小二乘法', time: 300, position: 33 },
  { id: 3, title: '梯度下降', time: 600, position: 66 },
  { id: 4, title: '模型评估', time: 900, position: 100 }
];

const knowledgeOutline = [
  {
    title: '第一章：线性回归基础',
    topics: [
      { title: '1.1 什么是线性回归', time: 0, timeRange: '00:00-02:30' },
      { title: '1.2 线性回归模型', time: 150, timeRange: '02:30-05:00' }
    ]
  },
  {
    title: '第二章：模型训练',
    topics: [
      { title: '2.1 最小二乘法', time: 300, timeRange: '05:00-07:30' },
      { title: '2.2 梯度下降算法', time: 450, timeRange: '07:30-10:00' }
    ]
  },
  {
    title: '第三章：模型评估',
    topics: [
      { title: '3.1 评估指标', time: 600, timeRange: '10:00-12:30' },
      { title: '3.2 过拟合与欠拟合', time: 750, timeRange: '12:30-15:30' }
    ]
  }
];

const messages = ref([
  {
    type: 'ai',
    content: '你好！我是你的AI助教，有什么问题可以随时问我。',
    timestamp: '10:00'
  }
]);

const notes = ref([
  {
    id: 1,
    time: '02:30',
    content: '线性回归是一种监督学习算法，用于预测连续值。'
  },
  {
    id: 2,
    time: '05:15',
    content: '最小二乘法是通过最小化残差平方和来求解模型参数的方法。'
  }
]);

// Methods
const goBack = () => {
  router.back();
};

const toggleTheme = () => {
  themeStore.toggleTheme();
};

const toggleOutline = () => {
  isOutlineOpen.value = !isOutlineOpen.value;
};

const handleTimeUpdate = () => {
  if (videoRef.value && duration.value > 0) {
    progress.value = (videoRef.value.currentTime / duration.value) * 100;
  }
};

const handleLoadedMetadata = () => {
  if (videoRef.value) {
    duration.value = videoRef.value.duration;
  }
};

const seekTo = (time: number) => {
  if (videoRef.value) {
    videoRef.value.currentTime = time;
  }
};

const isPointActive = (point: { time: number }) => {
  if (!videoRef.value) return false;
  const currentTime = videoRef.value.currentTime;
  return Math.abs(currentTime - point.time) < 30;
};

const isTopicActive = (topic: { time: number }) => {
  if (!videoRef.value) return false;
  const currentTime = videoRef.value.currentTime;
  return currentTime >= topic.time && currentTime < topic.time + 150;
};

const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
};

const formatTimeRange = (range: string) => {
  return range;
};

const sendMessage = () => {
  const message = inputMessage.value.trim();
  if (!message || isTyping.value) return;

  // Add user message
  messages.value.push({
    type: 'user',
    content: message,
    timestamp: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  });

  inputMessage.value = '';
  isTyping.value = true;
  typingText.value = '';

  // Scroll to bottom
  scrollToBottom();

  // Simulate AI response
  setTimeout(() => {
    const responses = [
      '线性回归是一种用于预测连续值的监督学习算法。它假设输入特征和输出之间存在线性关系。',
      '最小二乘法是通过最小化残差平方和来求解线性回归模型参数的方法。',
      '梯度下降是一种优化算法，用于最小化损失函数，在机器学习中广泛使用。',
      '模型评估指标包括均方误差(MSE)、均方根误差(RMSE)和决定系数(R²)等。'
    ];
    const randomResponse = responses[Math.floor(Math.random() * responses.length)];
    
    // Simulate typing effect
    let index = 0;
    const typingInterval = setInterval(() => {
      if (index < randomResponse.length) {
        typingText.value += randomResponse[index];
        index++;
        scrollToBottom();
      } else {
        clearInterval(typingInterval);
        isTyping.value = false;
        messages.value.push({
          type: 'ai',
          content: randomResponse,
          timestamp: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
        });
        scrollToBottom();
      }
    }, 50);
  }, 1000);
};

const clearChat = () => {
  messages.value = [
    {
      type: 'ai',
      content: '你好！我是你的AI助教，有什么问题可以随时问我。',
      timestamp: '10:00'
    }
  ];
};

const addNote = () => {
  if (!videoRef.value) return;
  
  const currentTime = formatTime(videoRef.value.currentTime);
  notes.value.unshift({
    id: notes.value.length + 1,
    time: currentTime,
    content: '新笔记'
  });
};

const scrollToBottom = () => {
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight;
  }
};

// Watch for message changes to scroll to bottom
watch(messages, () => {
  scrollToBottom();
}, { deep: true });

// Initialize
onMounted(() => {
  // Set video time to 0
  if (videoRef.value) {
    videoRef.value.currentTime = 0;
  }
});
</script>

<style scoped>
.video-learning {
  min-height: 100vh;
  background: var(--color-bg-canvas, #f8fafc);
  color: var(--color-text-primary, #0f172a);
}

/* Top Navigation */
.top-nav {
  height: 64px;
  background: var(--color-bg-card, #ffffff);
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.back-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-md, 8px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.page-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-md, 8px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.role-badge {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full, 9999px);
}

/* Main Content */
.main-content {
  padding: 24px;
}

.video-container {
  display: flex;
  gap: 24px;
  max-width: 1440px;
  margin: 0 auto;
}

/* Left Column */
.left-column {
  flex: 0 0 70%;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Video Player */
.video-player-container {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.video-player {
  width: 100%;
  aspect-ratio: 16/9;
  background: #000;
}

.video-info {
  padding: 16px;
}

.video-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 0.5rem;
  color: var(--color-text-primary, #0f172a);
}

.video-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
}

/* Timeline */
.timeline-container {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  padding: 16px;
}

.timeline-title {
  font-size: 1rem;
  font-weight: 600;
  margin: 0 0 1rem;
  color: var(--color-text-primary, #0f172a);
}

.timeline {
  position: relative;
  height: 32px;
  background: var(--color-bg-input, #f1f5f9);
  border-radius: var(--radius-full, 9999px);
  overflow: hidden;
  cursor: pointer;
}

.timeline-progress {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: var(--color-accent, #6366f1);
  border-radius: var(--radius-full, 9999px);
  transition: width 0.1s ease;
}

.timeline-handle {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background: var(--color-accent, #6366f1);
  border-radius: 50%;
  box-shadow: 0 0 0 2px white;
  transition: left 0.1s ease;
}

.timeline-point {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  cursor: pointer;
  transition: all 0.2s ease;
}

.timeline-dot {
  width: 6px;
  height: 6px;
  background: var(--color-accent, #6366f1);
  border-radius: 50%;
  transition: all 0.2s ease;
}

.timeline-point.active .timeline-dot {
  box-shadow: var(--shadow-glow, 0 0 10px rgba(99, 102, 241, 0.5));
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    box-shadow: var(--shadow-glow, 0 0 10px rgba(99, 102, 241, 0.5));
  }
  50% {
    box-shadow: var(--shadow-glow, 0 0 20px rgba(99, 102, 241, 0.8));
  }
}

.timeline-tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  padding: 8px 12px;
  box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
  white-space: nowrap;
  opacity: 0;
  visibility: hidden;
  transition: all 0.2s ease;
  margin-bottom: 8px;
  z-index: 10;
}

.timeline-point:hover .timeline-tooltip {
  opacity: 1;
  visibility: visible;
}

.tooltip-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
  margin-bottom: 4px;
}

.tooltip-time {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
}

/* AI Panel */
.ai-panel {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.ai-panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.ai-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.ai-title h3 {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.ai-status {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-success, #10b981);
}

.clear-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.25rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.clear-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.ai-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 400px;
}

.message {
  max-width: 85%;
  padding: 12px;
  border-radius: var(--radius-md, 8px);
  position: relative;
}

.user-message {
  align-self: flex-end;
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-text-primary, #0f172a);
  border-bottom-right-radius: 2px;
}

.ai-message {
  align-self: flex-start;
  background: var(--color-bg-elevated, #f8fafc);
  color: var(--color-text-primary, #0f172a);
  border-left: 2px solid var(--color-accent, #6366f1);
  border-bottom-left-radius: 2px;
  animation: slideInLeft 0.3s ease;
}

.user-message {
  align-self: flex-end;
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-text-primary, #0f172a);
  border-bottom-right-radius: 2px;
  animation: slideInRight 0.3s ease;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.message-content {
  font-size: 0.875rem;
  line-height: 1.5;
}

.message-time {
  font-size: 0.75rem;
  color: var(--color-text-tertiary, #94a3b8);
  margin-top: 4px;
  text-align: right;
}

.typing-cursor {
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}

.ai-input {
  padding: 16px;
  border-top: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  gap: 8px;
  align-items: flex-end;
}

.input-field {
  flex: 1;
  padding: 12px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  background: var(--color-bg-input, #f8fafc);
  color: var(--color-text-primary, #0f172a);
  font-size: 0.875rem;
  resize: none;
  min-height: 40px;
  max-height: 120px;
  line-height: 1.5;
  transition: all 0.2s ease;
}

.input-field:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.send-button {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 50%;
  background: var(--color-accent, #6366f1);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.send-button:hover:not(:disabled) {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

.send-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Right Column */
.right-column {
  flex: 0 0 30%;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Outline */
.outline-container {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
}

.outline-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.outline-header h3 {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.toggle-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.25rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.outline-content {
  padding: 0 16px 16px;
}

.chapter-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
  padding: 8px 0;
  cursor: pointer;
  list-style: none;
}

.chapter-title::-webkit-details-marker {
  color: var(--color-accent, #6366f1);
}

.chapter-topics {
  list-style: none;
  padding: 0 0 0 16px;
  margin: 0;
}

.topic-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-radius: var(--radius-sm, 4px);
  cursor: pointer;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.topic-item:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.topic-item.active {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  border-left-color: var(--color-accent, #6366f1);
}

.topic-title {
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
  flex: 1;
}

.topic-time {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
  font-family: 'Source Code Pro', monospace;
  margin-left: 8px;
}

/* Notes */
.notes-container {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.notes-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.notes-header h3 {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.add-button {
  background: var(--color-accent, #6366f1);
  color: white;
  border: none;
  border-radius: var(--radius-sm, 4px);
  padding: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-button:hover {
  background: var(--color-accent-hover, #4f46e5);
}

.notes-list {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.note-item {
  background: var(--color-bg-elevated, #f8fafc);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  padding: 12px;
  position: relative;
}

.note-time {
  font-size: 0.75rem;
  color: var(--color-accent, #6366f1);
  font-family: 'Source Code Pro', monospace;
  margin-bottom: 4px;
}

.note-content {
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
  line-height: 1.5;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.note-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.note-action-button {
  background: none;
  border: none;
  color: var(--color-text-tertiary, #94a3b8);
  cursor: pointer;
  padding: 4px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.note-action-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.empty-notes {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 16px;
  color: var(--color-text-tertiary, #94a3b8);
  text-align: center;
}

.empty-notes svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-notes p {
  margin: 0;
  font-size: 0.875rem;
}

/* Responsive Design */
@media (max-width: 1023px) {
  .video-container {
    flex-direction: column;
  }
  
  .left-column {
    flex: 1;
  }
  
  .right-column {
    flex: 1;
  }
}

@media (max-width: 767px) {
  .main-content {
    padding: 16px;
  }
  
  .nav-container {
    padding: 0 16px;
  }
  
  .page-title {
    font-size: 1.125rem;
  }
  
  .video-meta {
    flex-direction: column;
    gap: 4px;
  }
  
  .ai-messages {
    max-height: 300px;
  }
}

/* Dark Theme Support */
@media (prefers-color-scheme: dark) {
  :root {
    --color-bg-canvas: #0f172a;
    --color-bg-card: #1e293b;
    --color-bg-elevated: #334155;
    --color-bg-hover: #334155;
    --color-bg-input: #334155;
    --color-text-primary: #f8fafc;
    --color-text-secondary: #cbd5e1;
    --color-text-tertiary: #94a3b8;
    --color-border: #334155;
    --color-border-focus: #6366f1;
    --color-accent: #6366f1;
    --color-accent-hover: #4f46e5;
    --color-accent-subtle: rgba(99, 102, 241, 0.1);
    --color-success: #10b981;
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.3);
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.3);
    --shadow-glow: 0 0 20px rgba(99, 102, 241, 0.3);
  }
}

/* Light Theme */
@media (prefers-color-scheme: light) {
  :root {
    --color-bg-canvas: #f8fafc;
    --color-bg-card: #ffffff;
    --color-bg-elevated: #f8fafc;
    --color-bg-hover: #f1f5f9;
    --color-bg-input: #f1f5f9;
    --color-text-primary: #0f172a;
    --color-text-secondary: #64748b;
    --color-text-tertiary: #94a3b8;
    --color-border: #e2e8f0;
    --color-border-focus: #0075de;
    --color-accent: #0075de;
    --color-accent-hover: #0056b3;
    --color-accent-subtle: rgba(0, 117, 222, 0.15);
    --color-success: #10b981;
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
    --shadow-glow: 0 0 20px rgba(0, 117, 222, 0.2);
  }
}
</style>