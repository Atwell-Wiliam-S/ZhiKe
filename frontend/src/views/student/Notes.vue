<template>
  <div class="notes-page">
    <!-- 顶部导航 -->
    <StudentTopNav />

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-left">
        <select v-model="courseFilter" class="course-select" @change="handleCourseChange">
          <option value="">选择课程</option>
          <option v-for="course in courses" :key="course.id" :value="course.id">
            {{ course.title }}
          </option>
        </select>
        <select v-model="videoFilter" class="video-select" :disabled="!courseFilter" @change="handleVideoChange">
          <option value="">选择视频</option>
          <option v-for="video in videos" :key="video.id" :value="video.id">
            {{ video.title }}
          </option>
        </select>
      </div>
      <div class="filter-right">
        <div class="search-container">
          <Input
            v-model="searchQuery"
            placeholder="搜索笔记内容..."
            class="search-input"
            @input="handleSearchChange"
          >
            <template #prefix>
              <Search class="w-4 h-4 text-text-tertiary" />
            </template>
          </Input>
        </div>
      </div>
    </div>

    <!-- 笔记卡片瀑布流 -->
    <div class="notes-grid">
      <article
        v-for="note in notesList"
        :key="note.id"
        class="note-card"
      >
        <div class="note-header">
          <h3 class="video-title">{{ getVideoTitle(note.videoId) }}</h3>
          <span class="timestamp-pill" :aria-label="`视频时间点 ${formatTime(note.timestamp)}`">
            [{{ formatTime(note.timestamp) }}]
          </span>
        </div>
        <div class="note-content">
          <div v-if="!editingNoteId || editingNoteId !== note.id">
            {{ note.content }}
          </div>
          <div v-else class="note-edit">
            <textarea
              v-model="editContent"
              class="edit-textarea"
              rows="4"
              @keyup.enter.exact="saveEdit(note.id)"
              @keyup.esc="cancelEdit"
            ></textarea>
            <div class="edit-actions">
              <button class="edit-save" @click="saveEdit(note.id)">保存</button>
              <button class="edit-cancel" @click="cancelEdit">取消</button>
            </div>
          </div>
        </div>
        <div class="note-footer">
          <span class="create-time">{{ formatDate(note.createdAt) }}</span>
          <div class="note-actions">
            <button
              class="action-button"
              :class="{ active: note.isPublic }"
              :aria-checked="note.isPublic"
              :aria-label="`${note.isPublic ? '公开' : '私有'}笔记`"
              @click="togglePublic(note.id)"
            >
              <Lock v-if="!note.isPublic" class="w-4 h-4" />
              <Unlock v-else class="w-4 h-4" />
            </button>
            <button class="action-button" @click="startEdit(note)" aria-label="编辑笔记">
              <Pencil class="w-4 h-4" />
            </button>
            <button class="action-button danger" @click="confirmDelete(note.id)" aria-label="删除笔记">
              <Trash2 class="w-4 h-4" />
            </button>
          </div>
        </div>
      </article>

      <!-- 空状态 -->
      <div v-if="notesList.length === 0" class="empty-state">
        <StickyNote class="empty-icon" />
        <h3>暂无笔记</h3>
        <button class="empty-button" @click="goToVideoLearning">
          去视频学习页创建笔记
        </button>
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <div v-if="showDeleteConfirm" class="delete-confirm" role="alertdialog" aria-labelledby="delete-title">
      <div class="delete-confirm-content">
        <h4 id="delete-title">确认删除</h4>
        <p>确定要删除这条笔记吗？此操作不可恢复。</p>
        <div class="delete-confirm-actions">
          <button class="cancel-button" @click="showDeleteConfirm = false">取消</button>
          <button class="delete-button" @click="deleteNote">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, StickyNote, Lock, Unlock, Pencil, Trash2 } from 'lucide-vue-next'
import Input from '@/components/ui/Input.vue'
import StudentTopNav from '@/components/ui/StudentTopNav.vue'
import { studentApi } from '@/api/student'
import type { NoteDTO, CourseDTO, VideoDTO } from '@/types/api'
import { formatDate } from '@/utils/format'

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

// 删除状态
const showDeleteConfirm = ref(false)
const noteIdToDelete = ref<number | null>(null)

// 格式化视频时间
const formatTime = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

// 获取视频标题
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
  } catch (error) {
    console.error('获取课程列表失败:', error)
  }
}

// 获取视频列表
const fetchVideos = async (courseId: number) => {
  try {
    const response = await studentApi.getCourseDetail(courseId)
    videos.value = response.data.videos
  } catch (error) {
    console.error('获取视频列表失败:', error)
  }
}

// 获取笔记列表
const fetchNotes = async () => {
  isLoading.value = true
  try {
    const response = await studentApi.getNotes({
      videoId: videoFilter.value ? (videoFilter.value as unknown as number) : undefined,
      page: 1,
      pageSize: 100
    })
    let notes = response.data.records
    
    // 应用搜索过滤
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      notes = notes.filter(note => 
        note.content.toLowerCase().includes(query) ||
        getVideoTitle(note.videoId).toLowerCase().includes(query)
      )
    }
    
    notesList.value = notes
  } catch (error) {
    console.error('获取笔记列表失败:', error)
  } finally {
    isLoading.value = false
  }
}

// 开始编辑
const startEdit = (note: NoteDTO) => {
  editingNoteId.value = note.id
  editContent.value = note.content
}

// 保存编辑
const saveEdit = async (noteId: number) => {
  try {
    await studentApi.updateNote(noteId, { content: editContent.value })
    const noteIndex = notesList.value.findIndex(n => n.id === noteId)
    if (noteIndex !== -1) {
      notesList.value[noteIndex].content = editContent.value
    }
    editingNoteId.value = null
  } catch (error) {
    console.error('保存笔记失败:', error)
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
      showDeleteConfirm.value = false
      noteIdToDelete.value = null
    } catch (error) {
      console.error('删除笔记失败:', error)
    }
  }
}

// 切换公开/私有
const togglePublic = async (noteId: number) => {
  try {
    const note = notesList.value.find(n => n.id === noteId)
    if (note) {
      await studentApi.updateNote(noteId, { isPublic: !note.isPublic })
      note.isPublic = !note.isPublic
    }
  } catch (error) {
    console.error('切换笔记状态失败:', error)
  }
}

// 跳转到视频学习页
const goToVideoLearning = () => {
  router.push('/student/home')
}

// 监听搜索变化
const debounceTimer = ref<number | null>(null)
const handleSearchChange = () => {
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value)
  }
  debounceTimer.value = window.setTimeout(() => {
    fetchNotes()
  }, 300)
}

// 监听视频筛选变化
const handleVideoChange = () => {
  fetchNotes()
}

// 生命周期
onMounted(async () => {
  await fetchCourses()
  await fetchNotes()
})
</script>

<style scoped>
.notes-page {
  min-height: 100vh;
  background: var(--color-bg-canvas);
  padding-top: 76px;
}

.notes-list {
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
  gap: 12px;
  flex-wrap: wrap;
}

.filter-right {
  display: flex;
  align-items: center;
}

.course-select,
.video-select {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: 14px;
  min-width: 200px;
}

.search-container {
  position: relative;
  min-width: 300px;
}

.search-input {
  width: 100%;
}

.notes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.note-card {
  padding: 20px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  transition: all 150ms ease;
  display: flex;
  flex-direction: column;
}

.note-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  flex-wrap: wrap;
  gap: 8px;
}

.video-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
  flex: 1;
  line-height: 1.4;
}

.timestamp-pill {
  padding: 2px 8px;
  background: var(--color-accent);
  color: white;
  border-radius: var(--radius-sm);
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.note-content {
  font-size: 14px;
  color: var(--color-text-primary);
  line-height: 1.6;
  margin-bottom: 16px;
  flex: 1;
}

.note-edit {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.edit-textarea {
  padding: 8px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-elevated);
  color: var(--color-text-primary);
  font-size: 14px;
  line-height: 1.6;
  resize: vertical;
  min-height: 80px;
}

.edit-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.edit-save {
  padding: 6px 12px;
  background: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  border-radius: var(--radius-sm);
  font-size: 12px;
  cursor: pointer;
  transition: all 150ms ease;
}

.edit-save:hover {
  background: var(--color-accent-hover);
}

.edit-cancel {
  padding: 6px 12px;
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: 12px;
  cursor: pointer;
  transition: all 150ms ease;
}

.edit-cancel:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.note-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.create-time {
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.note-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.action-button {
  width: 32px;
  height: 32px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-bg-card);
  color: var(--color-text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 150ms ease;
}

.action-button:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.action-button.active {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.action-button.danger:hover {
  border-color: var(--color-danger);
  color: var(--color-danger);
}

.empty-state {
  grid-column: 1 / -1;
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

.delete-confirm {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.delete-confirm-content {
  background: var(--color-bg-card);
  border-radius: var(--radius-md);
  padding: 24px;
  max-width: 400px;
  width: 90%;
  box-shadow: var(--shadow-lg);
}

.delete-confirm-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 12px;
}

.delete-confirm-content p {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin-bottom: 24px;
}

.delete-confirm-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.cancel-button {
  padding: 8px 16px;
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: 14px;
  cursor: pointer;
  transition: all 150ms ease;
}

.cancel-button:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.delete-button {
  padding: 8px 16px;
  background: var(--color-danger);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 14px;
  cursor: pointer;
  transition: all 150ms ease;
}

.delete-button:hover {
  background: var(--color-danger-hover);
}

/* 响应式设计 */
@media (max-width: 1023px) {
  .notes-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 12px;
  }
}

@media (max-width: 768px) {
  .notes-list {
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

  .course-select,
  .video-select {
    min-width: auto;
  }

  .search-container {
    min-width: auto;
  }

  .notes-grid {
    grid-template-columns: 1fr;
  }
}
</style>