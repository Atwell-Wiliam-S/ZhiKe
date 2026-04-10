<template>
  <div class="notes-page">
    <StudentTopNav />

    <div class="notes-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <div>
          <h1 class="page-title">我的笔记</h1>
          <p class="page-subtitle">在视频学习过程中记录的知识点</p>
        </div>
        <div class="header-stats">
          <div class="stat-chip">
            <FileText :size="14" />
            <span>共 {{ notesList.length }} 条</span>
          </div>
          <div class="stat-chip public-chip" v-if="publicCount > 0">
            <Globe :size="14" />
            <span>{{ publicCount }} 条公开</span>
          </div>
        </div>
      </div>

      <!-- 筛选栏 -->
      <div class="filter-bar">
        <div class="filter-left">
          <div class="select-wrapper">
            <select v-model="courseFilter" class="custom-select" @change="handleCourseChange">
              <option value="">全部课程</option>
              <option v-for="course in courses" :key="course.id" :value="course.id">
                {{ course.title }}
              </option>
            </select>
            <ChevronDown :size="14" class="select-icon" />
          </div>
          <div class="select-wrapper" :class="{ disabled: !courseFilter }">
            <select v-model="videoFilter" class="custom-select" :disabled="!courseFilter" @change="handleVideoChange">
              <option value="">全部视频</option>
              <option v-for="video in videos" :key="video.id" :value="video.id">
                {{ video.title }}
              </option>
            </select>
            <ChevronDown :size="14" class="select-icon" />
          </div>
        </div>
        <div class="search-box">
          <Search :size="16" class="search-icon" />
          <input
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="搜索笔记内容..."
            @input="handleSearchChange"
          />
          <button v-if="searchQuery" class="search-clear" @click="searchQuery = ''; fetchNotes()" aria-label="清除搜索">
            <X :size="14" />
          </button>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>

      <!-- 笔记列表 -->
      <div v-else-if="notesList.length > 0" class="notes-grid">
        <article
          v-for="note in notesList"
          :key="note.id"
          class="note-card"
          :class="{ editing: editingNoteId === note.id }"
        >
          <!-- 卡片顶部：视频信息 + 时间戳 -->
          <div class="note-card-top">
            <div class="note-source">
              <Video :size="14" />
              <span>{{ getVideoTitle(note.videoId) }}</span>
            </div>
            <div class="note-meta">
              <span class="timestamp-badge">
                <Clock :size="12" />
                {{ formatTime(note.timestamp) }}
              </span>
              <span v-if="note.isPublic" class="public-badge" title="公开笔记">
                <Globe :size="12" />
              </span>
            </div>
          </div>

          <!-- 笔记内容 -->
          <div class="note-body">
            <div v-if="editingNoteId !== note.id" class="note-text">
              {{ note.content }}
            </div>
            <div v-else class="note-edit">
              <textarea
                ref="editTextareaRef"
                v-model="editContent"
                class="edit-textarea"
                rows="4"
                @keydown.enter.ctrl="saveEdit(note.id)"
                @keydown.esc="cancelEdit"
              ></textarea>
              <div class="edit-actions">
                <button class="btn btn-cancel" @click="cancelEdit">取消</button>
                <button class="btn btn-save" @click="saveEdit(note.id)">保存</button>
              </div>
            </div>
          </div>

          <!-- 卡片底部：创建时间 + 操作 -->
          <div class="note-card-bottom">
            <span class="create-time">{{ formatRelativeTime(note.createdAt) }}</span>
            <div class="note-actions">
              <button
                class="action-btn"
                :class="{ active: note.isPublic }"
                :aria-label="note.isPublic ? '设为私有' : '设为公开'"
                @click="togglePublic(note.id)"
              >
                <Globe v-if="note.isPublic" :size="15" />
                <Lock v-else :size="15" />
              </button>
              <button class="action-btn" @click="startEdit(note)" aria-label="编辑">
                <Pencil :size="15" />
              </button>
              <button class="action-btn danger" @click="confirmDelete(note.id)" aria-label="删除">
                <Trash2 :size="15" />
              </button>
            </div>
          </div>
        </article>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <div class="empty-icon-wrap">
          <StickyNote :size="28" />
        </div>
        <h3>{{ searchQuery ? '未找到匹配的笔记' : '暂无笔记' }}</h3>
        <p class="empty-hint">{{ searchQuery ? '试试换个关键词搜索' : '在视频学习页面按快捷键添加笔记' }}</p>
        <button v-if="!searchQuery" class="empty-btn" @click="goToVideoLearning">
          <Video :size="16" />
          去学习页
        </button>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="showDeleteConfirm" class="modal-overlay" @click.self="showDeleteConfirm = false">
          <div class="modal-box">
            <div class="modal-icon danger">
              <AlertTriangle :size="24" />
            </div>
            <h4>确认删除</h4>
            <p>确定要删除这条笔记吗？此操作不可恢复。</p>
            <div class="modal-actions">
              <button class="btn btn-ghost" @click="showDeleteConfirm = false">取消</button>
              <button class="btn btn-danger" @click="deleteNote">删除</button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import {
  Search, StickyNote, Lock, Globe, Pencil, Trash2,
  FileText, Video, Clock, ChevronDown, X, AlertTriangle
} from 'lucide-vue-next'
import StudentTopNav from '@/components/ui/StudentTopNav.vue'
import { studentApi } from '@/api/student'
import type { NoteDTO, CourseDTO, VideoDTO } from '@/types/api'
import { mockVideos } from '@/mocks/data/video.data'

const router = useRouter()

// 筛选状态
const courseFilter = ref('')
const videoFilter = ref('')
const searchQuery = ref('')

// 数据状态
const notesList = ref<NoteDTO[]>([])
const courses = ref<CourseDTO[]>([])
const videos = ref<VideoDTO[]>([])
const isLoading = ref(false)

// 编辑状态
const editingNoteId = ref<number | null>(null)
const editContent = ref('')
const editTextareaRef = ref<HTMLTextAreaElement | null>(null)

// 删除状态
const showDeleteConfirm = ref(false)
const noteIdToDelete = ref<number | null>(null)

// 公开笔记数
const publicCount = computed(() => notesList.value.filter(n => n.isPublic).length)

// 获取视频标题（兼容 mock 和真实数据）
const getVideoTitle = (videoId: number): string => {
  const video = videos.value.find(v => v.id === videoId)
  return video?.title || '未知视频'
}

// 处理课程变更
const handleCourseChange = async () => {
  videoFilter.value = ''
  if (courseFilter.value) {
    await fetchVideos(courseFilter.value as unknown as number)
  } else {
    videos.value = []
  }
  await fetchNotes()
}

// 获取课程列表
const fetchCourses = async () => {
  try {
    const response = await studentApi.getCourses({ page: 1, pageSize: 100 })
    courses.value = response.data.records
  } catch {
    // mock fallback
    courses.value = []
  }
}

// 获取视频列表
const fetchVideos = async (courseId: number) => {
  try {
    const response = await studentApi.getCourseDetail(courseId)
    videos.value = response.data.videos
  } catch {
    videos.value = mockVideos.filter(v => v.courseId === courseId)
  }
}

// 获取笔记列表
const fetchNotes = async () => {
  isLoading.value = true
  try {
    const response = await studentApi.getNotes({
      videoId: videoFilter.value ? (videoFilter.value as unknown as number) : undefined,
      page: 1,
      pageSize: 100,
    })
    let notes = response.data.records

    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      notes = notes.filter(note =>
        note.content.toLowerCase().includes(query) ||
        getVideoTitle(note.videoId).toLowerCase().includes(query)
      )
    }
    notesList.value = notes
  } catch {
    notesList.value = []
  } finally {
    isLoading.value = false
  }
}

// 开始编辑
const startEdit = (note: NoteDTO) => {
  editingNoteId.value = note.id
  editContent.value = note.content
  nextTick(() => {
    editTextareaRef.value?.focus()
  })
}

// 保存编辑
const saveEdit = async (noteId: number) => {
  if (!editContent.value.trim()) return
  try {
    await studentApi.updateNote(noteId, { content: editContent.value })
    const idx = notesList.value.findIndex(n => n.id === noteId)
    if (idx !== -1) notesList.value[idx].content = editContent.value.trim()
    editingNoteId.value = null
  } catch {
    // 静默处理
  }
}

// 取消编辑
const cancelEdit = () => {
  editingNoteId.value = null
  editContent.value = ''
}

// 确认删除
const confirmDelete = (noteId: number) => {
  noteIdToDelete.value = noteId
  showDeleteConfirm.value = true
}

// 删除笔记
const deleteNote = async () => {
  if (noteIdToDelete.value) {
    try {
      await studentApi.deleteNote(noteIdToDelete.value)
      notesList.value = notesList.value.filter(n => n.id !== noteIdToDelete.value)
    } catch {
      // 静默处理
    }
    showDeleteConfirm.value = false
    noteIdToDelete.value = null
  }
}

// 切换公开/私有
const togglePublic = async (noteId: number) => {
  const note = notesList.value.find(n => n.id === noteId)
  if (!note) return
  try {
    await studentApi.updateNote(noteId, { isPublic: !note.isPublic })
    note.isPublic = !note.isPublic
  } catch {
    // 静默处理
  }
}

// 跳转
const goToVideoLearning = () => router.push('/student/home')

// 格式化
const formatTime = (seconds: number): string => {
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

const formatRelativeTime = (date: string) => {
  const diff = Date.now() - new Date(date).getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes} 分钟前`
  if (hours < 24) return `${hours} 小时前`
  if (days < 30) return `${days} 天前`
  return new Date(date).toLocaleDateString('zh-CN')
}

// 搜索防抖
let searchTimer: number | null = null
const handleSearchChange = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = window.setTimeout(fetchNotes, 300)
}

const handleVideoChange = () => fetchNotes()

// ESC 关闭弹窗
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && showDeleteConfirm.value) {
    showDeleteConfirm.value = false
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeydown)
  fetchCourses().then(fetchNotes)
})
</script>

<style scoped>
.notes-page {
  min-height: 100vh;
  background: var(--color-bg-canvas);
  display: flex;
  flex-direction: column;
}

.notes-container {
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

.public-chip {
  background: var(--color-success-bg);
  color: var(--color-success);
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.filter-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.select-wrapper {
  position: relative;
}

.select-wrapper.disabled {
  opacity: 0.5;
  pointer-events: none;
}

.custom-select {
  appearance: none;
  padding: 8px 32px 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: var(--text-sm);
  cursor: pointer;
  min-width: 160px;
  transition: border-color 0.15s ease;
}

.custom-select:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

.select-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary);
  pointer-events: none;
}

.search-box {
  position: relative;
  width: 220px;
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

@keyframes spin { to { transform: rotate(360deg); } }

/* 笔记卡片网格 */
.notes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 16px;
}

.note-card {
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: 0;
  display: flex;
  flex-direction: column;
  transition: all 0.2s ease;
  overflow: hidden;
}

.note-card:hover {
  border-color: var(--color-accent);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.note-card.editing {
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

/* 卡片顶部 */
.note-card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 18px 0;
}

.note-source {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-xs);
  font-weight: 600;
  color: var(--color-accent);
}

.note-meta {
  display: flex;
  align-items: center;
  gap: 6px;
}

.timestamp-badge {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  padding: 2px 8px;
  background: var(--color-accent-subtle);
  color: var(--color-accent);
  border-radius: var(--radius-sm);
  font-size: 0.7rem;
  font-weight: 500;
  font-family: 'Source Code Pro', monospace;
}

.public-badge {
  color: var(--color-success);
  display: flex;
  align-items: center;
}

/* 卡片内容 */
.note-body {
  padding: 12px 18px 14px;
  flex: 1;
}

.note-text {
  font-size: var(--text-sm);
  color: var(--color-text-primary);
  line-height: 1.7;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.note-card:hover .note-text {
  -webkit-line-clamp: 8;
}

.note-edit {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.edit-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-elevated);
  color: var(--color-text-primary);
  font-size: var(--text-sm);
  line-height: 1.6;
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
}

.edit-textarea:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

.edit-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

/* 卡片底部 */
.note-card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 18px;
  border-top: 1px solid var(--color-border);
  background: var(--color-bg-elevated);
}

.create-time {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
}

.note-actions {
  display: flex;
  gap: 4px;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: var(--radius-md);
  background: transparent;
  color: var(--color-text-tertiary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s ease;
}

.action-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.action-btn.active {
  color: var(--color-success);
}

.action-btn.active:hover {
  background: var(--color-success-bg);
}

.action-btn.danger:hover {
  background: var(--color-danger-bg);
  color: var(--color-danger);
}

/* 按钮 */
.btn {
  padding: 6px 14px;
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  border: none;
  transition: all 0.15s ease;
}

.btn-save {
  background: var(--color-accent);
  color: var(--color-accent-text);
}

.btn-save:hover {
  background: var(--color-accent-hover);
}

.btn-cancel,
.btn-ghost {
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
}

.btn-cancel:hover,
.btn-ghost:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.btn-danger {
  background: var(--color-danger);
  color: white;
}

.btn-danger:hover {
  background: var(--color-danger-hover);
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80px 24px;
  text-align: center;
}

.empty-icon-wrap {
  width: 60px;
  height: 60px;
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

.empty-btn {
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

.empty-btn:hover {
  background: var(--color-accent-hover);
}

/* 删除确认弹窗 */
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

.modal-box {
  background: var(--color-bg-card);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  padding: 32px;
  max-width: 360px;
  width: 100%;
  text-align: center;
}

.modal-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.modal-icon.danger {
  background: var(--color-danger-bg);
  color: var(--color-danger);
}

.modal-box h4 {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0 0 8px;
}

.modal-box p {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin: 0 0 24px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: center;
}

/* 弹窗动画 */
.modal-enter-active { transition: opacity 0.2s ease; }
.modal-enter-active .modal-box { transition: opacity 0.2s ease, transform 0.2s ease; }
.modal-leave-active { transition: opacity 0.15s ease; }
.modal-leave-active .modal-box { transition: opacity 0.15s ease, transform 0.15s ease; }
.modal-enter-from, .modal-leave-to { opacity: 0; }
.modal-enter-from .modal-box { transform: scale(0.95); }
.modal-leave-to .modal-box { transform: scale(0.97); }

/* 响应式 */
@media (max-width: 768px) {
  .notes-container {
    padding: 16px;
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

  .custom-select {
    min-width: auto;
    width: 100%;
  }

  .search-box {
    width: 100%;
  }

  .notes-grid {
    grid-template-columns: 1fr;
  }
}
</style>
