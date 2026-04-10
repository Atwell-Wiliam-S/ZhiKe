<template>
  <div class="video-learning">
    <!-- Main Content -->
    <div class="content-container">
      <!-- Left Column (70%) -->
      <div class="left-column">
        <!-- Video Player -->
        <div class="video-player-container">
          <div class="video-wrapper">
            <video
              ref="videoRef"
              class="video-player"
              controls
              @play="handlePlay"
              @pause="handlePause"
              @timeupdate="handleTimeUpdate"
            >
              <source src="https://example.com/video.mp4" type="video/mp4" />
              Your browser does not support the video tag.
            </video>
            <div class="video-overlay">
              <div class="video-title">{{ videoTitle }}</div>
            </div>
          </div>

          <!-- Knowledge Point Timeline -->
          <div class="timeline-container">
            <div class="timeline">
              <div class="timeline-track">
                <div
                  v-for="(point, index) in knowledgePoints"
                  :key="index"
                  class="timeline-marker"
                  :class="{ 'active': isActivePoint(point) }"
                  :style="{ left: point.position + '%' }"
                  @click="seekTo(point.time)"
                  :aria-label="`知识点: ${point.title}`"
                >
                  <div class="timeline-tooltip">{{ point.title }}</div>
                </div>
                <div
                  class="timeline-progress"
                  :style="{ width: videoProgress + '%' }"
                ></div>
                <div
                  class="timeline-handle"
                  :style="{ left: videoProgress + '%' }"
                  @mousedown="startDrag"
                ></div>
              </div>
            </div>
          </div>
        </div>

        <!-- AI Assistant Panel -->
        <div class="ai-panel">
          <div class="ai-panel-header">
            <div class="ai-panel-title">
              <span>AI 助教</span>
              <span class="ai-status">
                <span class="status-indicator"></span>
                在线
              </span>
            </div>
            <button class="ai-panel-clear" @click="clearChat" aria-label="清空对话">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="3 6 5 6 21 6"></polyline>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                <line x1="10" y1="11" x2="10" y2="17"></line>
                <line x1="14" y1="11" x2="14" y2="17"></line>
              </svg>
            </button>
          </div>
          <div class="ai-messages" ref="messagesRef" aria-live="polite" role="log">
            <div
              v-for="(message, index) in messages"
              :key="index"
              class="ai-message"
              :class="{ 'user-message': message.isUser }"
            >
              <div class="message-content">
                <div class="message-text">{{ message.text }}</div>
                <div v-if="message.references && message.references.length > 0" class="message-references">
                  <span
                    v-for="(ref, refIndex) in message.references"
                    :key="refIndex"
                    class="reference-tag"
                    @click="seekTo(ref.time)"
                    :aria-label="`跳转到 ${formatTime(ref.time)} - ${ref.title}`"
                  >
                    {{ ref.title }}
                  </span>
                </div>
              </div>
            </div>
            <div v-if="isTyping" class="ai-message">
              <div class="message-content">
                <div class="message-text">
                  <span class="typing-indicator">
                    <span class="dot"></span>
                    <span class="dot"></span>
                    <span class="dot"></span>
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="ai-input-container">
            <textarea
              v-model="inputMessage"
              class="ai-input"
              placeholder="向 AI 助教提问..."
              @keydown.enter.exact="sendMessage"
              @keydown.enter.shift="$event.preventDefault()"
              aria-label="向 AI 助教提问"
            ></textarea>
            <button
              class="ai-send-button"
              @click="sendMessage"
              :disabled="!inputMessage.trim() || isSending"
              aria-label="发送消息"
            >
              <svg v-if="!isSending" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
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
        <div class="knowledge-outline">
          <div class="section-header">
            <h3>知识大纲</h3>
            <button class="collapse-button" @click="toggleOutline" aria-label="折叠知识大纲">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="6 9 12 15 18 9"></polyline>
              </svg>
            </button>
          </div>
          <div v-if="showOutline" class="outline-content">
            <details v-for="(chapter, chapterIndex) in chapters" :key="chapterIndex" class="chapter">
              <summary class="chapter-title">{{ chapter.title }}</summary>
              <ul class="chapter-items">
                <li
                  v-for="(item, itemIndex) in chapter.items"
                  :key="itemIndex"
                  class="chapter-item"
                  :class="{ 'active': isCurrentItem(item) }"
                  @click="seekTo(item.time)"
                >
                  <span class="item-title">{{ item.title }}</span>
                  <span class="item-time">{{ formatTime(item.time) }}</span>
                </li>
              </ul>
            </details>
          </div>
        </div>

        <!-- My Notes -->
        <div class="my-notes">
          <div class="section-header">
            <h3>我的笔记</h3>
            <button class="add-note-button" @click="addNote" aria-label="添加笔记">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
              </svg>
            </button>
          </div>
          <div class="notes-list">
            <div
              v-for="(note, noteIndex) in notes"
              :key="noteIndex"
              class="note-item"
            >
              <div class="note-header">
                <span class="note-time">{{ formatTime(note.time) }}</span>
                <div class="note-actions">
                  <button class="note-action-button" @click="editNote(noteIndex)" aria-label="编辑笔记">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                      <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                    </svg>
                  </button>
                  <button class="note-action-button" @click="deleteNote(noteIndex)" aria-label="删除笔记">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <polyline points="3 6 5 6 21 6"></polyline>
                      <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                      <line x1="10" y1="11" x2="10" y2="17"></line>
                      <line x1="14" y1="11" x2="14" y2="17"></line>
                    </svg>
                  </button>
                </div>
              </div>
              <div class="note-content">{{ note.content }}</div>
            </div>
            <div v-if="notes.length === 0" class="empty-notes">
              <p>暂无笔记</p>
              <p class="empty-notes-hint">在学习过程中添加笔记</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useThemeStore } from '@/stores/theme';

const themeStore = useThemeStore();

// Video player
const videoRef = ref<HTMLVideoElement | null>(null);
const videoTitle = ref('机器学习基础 - 第1讲');
const videoProgress = ref(0);
const isPlaying = ref(false);

// Knowledge points
const knowledgePoints = ref([
  { id: 1, title: '什么是机器学习', time: 60, position: 10 },
  { id: 2, title: '监督学习与无监督学习', time: 180, position: 30 },
  { id: 3, title: '线性回归', time: 300, position: 50 },
  { id: 4, title: '模型评估', time: 420, position: 70 },
  { id: 5, title: '过拟合与欠拟合', time: 540, position: 90 }
]);

// AI chat
const messages = ref([
  {
    id: 1,
    text: '你好！我是你的AI助教，有什么可以帮助你的吗？',
    isUser: false,
    references: []
  }
]);
const inputMessage = ref('');
const isTyping = ref(false);
const isSending = ref(false);
const messagesRef = ref<HTMLElement | null>(null);

// Knowledge outline
const showOutline = ref(true);
const chapters = ref([
  {
    title: '第一章：机器学习基础',
    items: [
      { title: '1.1 什么是机器学习', time: 60 },
      { title: '1.2 监督学习与无监督学习', time: 180 },
      { title: '1.3 机器学习工作流程', time: 300 }
    ]
  },
  {
    title: '第二章：线性回归',
    items: [
      { title: '2.1 线性回归原理', time: 420 },
      { title: '2.2 模型训练', time: 540 },
      { title: '2.3 模型评估', time: 660 }
    ]
  }
]);

// Notes
const notes = ref([
  {
    id: 1,
    time: 60,
    content: '机器学习是人工智能的一个分支，通过算法让计算机从数据中学习'
  },
  {
    id: 2,
    time: 180,
    content: '监督学习需要标签数据，无监督学习不需要'
  }
]);

// Methods
const handlePlay = () => {
  isPlaying.value = true;
};

const handlePause = () => {
  isPlaying.value = false;
};

const handleTimeUpdate = () => {
  if (videoRef.value) {
    const duration = videoRef.value.duration || 600; // 假设视频时长为10分钟
    const currentTime = videoRef.value.currentTime;
    videoProgress.value = (currentTime / duration) * 100;
  }
};

const seekTo = (time: number) => {
  if (videoRef.value) {
    videoRef.value.currentTime = time;
  }
};

const isActivePoint = (point: any) => {
  if (!videoRef.value) return false;
  const currentTime = videoRef.value.currentTime;
  return Math.abs(currentTime - point.time) < 30; // 30秒内的知识点标记为活跃
};

const isCurrentItem = (item: any) => {
  if (!videoRef.value) return false;
  const currentTime = videoRef.value.currentTime;
  return Math.abs(currentTime - item.time) < 10; // 10秒内的项目标记为当前
};

const toggleOutline = () => {
  showOutline.value = !showOutline.value;
};

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isSending.value) return;

  const userMessage = inputMessage.value.trim();
  messages.value.push({
    id: Date.now(),
    text: userMessage,
    isUser: true,
    references: []
  });

  inputMessage.value = '';
  isSending.value = true;

  // Simulate AI response
  setTimeout(() => {
    isSending.value = false;
    isTyping.value = true;

    setTimeout(() => {
      isTyping.value = false;
      messages.value.push({
        id: Date.now() + 1,
        text: '这是一个关于机器学习的重要概念。机器学习是通过算法让计算机从数据中学习，而不需要显式编程。',
        isUser: false,
        references: [
          { title: '什么是机器学习', time: 60 }
        ]
      });
      scrollToBottom();
    }, 1500);
  }, 500);

  scrollToBottom();
};

const clearChat = () => {
  messages.value = [
    {
      id: 1,
      text: '你好！我是你的AI助教，有什么可以帮助你的吗？',
      isUser: false,
      references: []
    }
  ];
};

const scrollToBottom = () => {
  setTimeout(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight;
    }
  }, 100);
};

const addNote = () => {
  if (!videoRef.value) return;
  const currentTime = videoRef.value.currentTime;
  notes.value.push({
    id: Date.now(),
    time: currentTime,
    content: '新笔记'
  });
};

const editNote = (index: number) => {
  // 简单实现，实际项目中可使用弹窗
  const newContent = prompt('编辑笔记:', notes.value[index].content);
  if (newContent) {
    notes.value[index].content = newContent;
  }
};

const deleteNote = (index: number) => {
  if (confirm('确定要删除这条笔记吗？')) {
    notes.value.splice(index, 1);
  }
};

const formatTime = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = Math.floor(seconds % 60);
  return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
};

// Drag functionality for timeline
let isDragging = false;

const startDrag = (event: MouseEvent) => {
  isDragging = true;
  document.addEventListener('mousemove', handleDrag);
  document.addEventListener('mouseup', stopDrag);
  handleDrag(event);
};

const handleDrag = (event: MouseEvent) => {
  if (!isDragging || !videoRef.value) return;

  const timeline = event.currentTarget as HTMLElement;
  const rect = timeline.getBoundingClientRect();
  const progress = ((event.clientX - rect.left) / rect.width) * 100;
  const clampedProgress = Math.max(0, Math.min(100, progress));

  videoProgress.value = clampedProgress;

  const duration = videoRef.value.duration || 600;
  const newTime = (clampedProgress / 100) * duration;
  videoRef.value.currentTime = newTime;
};

const stopDrag = () => {
  isDragging = false;
  document.removeEventListener('mousemove', handleDrag);
  document.removeEventListener('mouseup', stopDrag);
};

// Cleanup
onUnmounted(() => {
  document.removeEventListener('mousemove', handleDrag);
  document.removeEventListener('mouseup', stopDrag);
});
</script>

<style scoped>
.video-learning {
  min-height: 100vh;
  background: var(--color-bg-canvas, #f8fafc);
  color: var(--color-text-primary, #0f172a);
  padding: 24px;
}

.content-container {
  display: grid;
  grid-template-columns: 70% 30%;
  gap: 24px;
  max-width: 1440px;
  margin: 0 auto;
}

/* Left Column */
.left-column {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Video Player */
.video-player-container {
  background: var(--color-bg-card, #ffffff);
  border-radius: var(--radius-lg, 12px);
  padding: 20px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.video-wrapper {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 */
  margin-bottom: 12px;
}

.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: var(--radius-md, 8px);
  background: #000;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.7) 0%, transparent 30%);
  border-radius: var(--radius-md, 8px);
  display: flex;
  align-items: flex-end;
  padding: 16px;
  color: white;
}

.video-title {
  font-size: 1.125rem;
  font-weight: 600;
}

/* Timeline */
.timeline-container {
  margin-top: 12px;
}

.timeline {
  position: relative;
  height: 32px;
  cursor: pointer;
}

.timeline-track {
  position: relative;
  height: 8px;
  background: var(--color-bg-input, #f1f5f9);
  border-radius: var(--radius-full, 9999px);
  top: 12px;
}

.timeline-progress {
  position: absolute;
  height: 100%;
  background: var(--color-accent, #6366f1);
  border-radius: var(--radius-full, 9999px);
  top: 0;
  left: 0;
  transition: width 0.1s linear;
}

.timeline-handle {
  position: absolute;
  width: 12px;
  height: 12px;
  background: var(--color-accent, #6366f1);
  border-radius: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.2));
  cursor: grab;
}

.timeline-handle:active {
  cursor: grabbing;
}

.timeline-marker {
  position: absolute;
  width: 6px;
  height: 6px;
  background: #6366f1;
  border-radius: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  transition: all 0.2s ease;
  cursor: pointer;
}

.timeline-marker.active {
  width: 8px;
  height: 8px;
  background: var(--color-accent, #6366f1);
  box-shadow: 0 0 0 6px var(--color-accent-subtle, rgba(99, 102, 241, 0.2));
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    box-shadow: 0 0 0 6px var(--color-accent-subtle, rgba(99, 102, 241, 0.2));
  }
  50% {
    box-shadow: 0 0 0 10px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  }
}

.timeline-tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #0f172a);
  padding: 4px 8px;
  border-radius: var(--radius-sm, 4px);
  font-size: 0.75rem;
  white-space: nowrap;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s ease;
  z-index: 10;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  margin-bottom: 8px;
}

.timeline-marker:hover .timeline-tooltip {
  opacity: 1;
}

/* AI Panel */
.ai-panel {
  background: var(--color-bg-card, #ffffff);
  border-radius: var(--radius-lg, 12px);
  padding: 20px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  max-height: 400px;
  display: flex;
  flex-direction: column;
}

.ai-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.ai-panel-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
}

.ai-status {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.75rem;
  color: var(--color-success, #10b981);
}

.status-indicator {
  width: 8px;
  height: 8px;
  background: var(--color-success, #10b981);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.ai-panel-clear {
  background: none;
  border: none;
  color: var(--color-text-tertiary, #94a3b8);
  cursor: pointer;
  padding: 4px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
}

.ai-panel-clear:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.ai-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 240px;
}

.ai-message {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.ai-message.user-message {
  flex-direction: row-reverse;
}

.message-content {
  flex: 1;
  max-width: 85%;
  padding: 12px;
  border-radius: var(--radius-md, 8px);
  background: var(--color-bg-elevated, #f8fafc);
  position: relative;
}

.ai-message.user-message .message-content {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  max-width: 80%;
}

.message-text {
  color: var(--color-text-primary, #0f172a);
  line-height: 1.5;
}

.message-references {
  margin-top: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.reference-tag {
  background: var(--color-accent, #6366f1);
  color: white;
  padding: 4px 8px;
  border-radius: var(--radius-full, 9999px);
  font-size: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.reference-tag:hover {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

.typing-indicator {
  display: flex;
  gap: 4px;
  align-items: center;
}

.typing-indicator .dot {
  width: 8px;
  height: 8px;
  background: var(--color-text-secondary, #64748b);
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out both;
}

.typing-indicator .dot:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator .dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.ai-input-container {
  display: flex;
  gap: 8px;
  align-items: flex-end;
}

.ai-input {
  flex: 1;
  min-height: 40px;
  max-height: 120px;
  padding: 12px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  background: var(--color-bg-input, #f8fafc);
  color: var(--color-text-primary, #0f172a);
  resize: none;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.ai-input:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.ai-send-button {
  width: 36px;
  height: 36px;
  background: var(--color-accent, #6366f1);
  color: white;
  border: none;
  border-radius: var(--radius-md, 8px);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.ai-send-button:hover:not(:disabled) {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

.ai-send-button:disabled {
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
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: sticky;
  top: 80px;
  align-self: flex-start;
}

/* Knowledge Outline */
.knowledge-outline {
  background: var(--color-bg-card, #ffffff);
  border-radius: var(--radius-lg, 12px);
  padding: 20px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h3 {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.collapse-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 4px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
}

.collapse-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.chapter {
  margin-bottom: 12px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  overflow: hidden;
}

.chapter-title {
  padding: 12px 16px;
  background: var(--color-bg-elevated, #f8fafc);
  font-weight: 600;
  cursor: pointer;
  list-style: none;
  transition: all 0.2s ease;
}

.chapter-title:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.chapter-title::marker {
  display: none;
}

.chapter-items {
  list-style: none;
  padding: 0;
  margin: 0;
}

.chapter-item {
  padding: 10px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.chapter-item:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.chapter-item.active {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  border-left-color: var(--color-accent, #6366f1);
  font-weight: 600;
}

.item-title {
  flex: 1;
  margin-right: 12px;
  color: var(--color-text-primary, #0f172a);
}

.item-time {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
  font-family: 'Source Code Pro', monospace;
}

/* My Notes */
.my-notes {
  background: var(--color-bg-card, #ffffff);
  border-radius: var(--radius-lg, 12px);
  padding: 20px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.add-note-button {
  background: var(--color-accent, #6366f1);
  color: white;
  border: none;
  border-radius: var(--radius-sm, 4px);
  padding: 4px 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.add-note-button:hover {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

.notes-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.note-item {
  padding: 12px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  background: var(--color-bg-elevated, #f8fafc);
  transition: all 0.2s ease;
}

.note-item:hover {
  box-shadow: var(--shadow-sm, 0 1px 3px 0 rgba(0, 0, 0, 0.1));
  transform: translateY(-1px);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.note-time {
  font-size: 0.75rem;
  color: var(--color-accent, #6366f1);
  font-family: 'Source Code Pro', monospace;
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  padding: 2px 8px;
  border-radius: var(--radius-full, 9999px);
}

.note-actions {
  display: flex;
  gap: 4px;
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

.note-content {
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
  line-height: 1.5;
}

.empty-notes {
  text-align: center;
  padding: 24px 0;
  color: var(--color-text-tertiary, #94a3b8);
}

.empty-notes-hint {
  font-size: 0.75rem;
  margin-top: 4px;
}

/* Responsive Design */
@media (max-width: 1023px) {
  .content-container {
    grid-template-columns: 65% 35%;
  }
}

@media (max-width: 767px) {
  .content-container {
    grid-template-columns: 1fr;
  }

  .right-column {
    position: static;
  }

  .video-learning {
    padding: 16px;
  }

  .ai-panel {
    max-height: 300px;
  }

  .ai-messages {
    max-height: 180px;
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
    --color-accent-hover: #4f46e5;
    --color-accent-subtle: rgba(99, 102, 241, 0.1);
    --color-success: #10b981;
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;
    --shadow-sm: 0 1px 3px 0 rgba(0, 0, 0, 0.3);
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.3);
  }
}

/* Light Theme */
@media (prefers-color-scheme: light) {
  :root {
    --color-bg-canvas: #f8fafc;
    --color-bg-card: #ffffff;
    --color-bg-elevated: #f8fafc;
    --color-bg-input: #f8fafc;
    --color-bg-hover: #f1f5f9;
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
    --shadow-sm: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  }
}
</style>