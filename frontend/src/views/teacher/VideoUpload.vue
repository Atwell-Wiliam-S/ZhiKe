<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useThemeStore } from '@/stores/theme'
import { useNotificationStore } from '@/stores/notification'
import { useUserStore } from '@/stores/user'
import {
  BarChart3,
  FileText,
  TrendingUp,
  Video,
  Settings,
  ChevronLeft,
  ChevronRight,
  ChevronDown,
  Sun,
  Moon,
  Bell,
  LogOut,
  GraduationCap,
  Upload,
  Sparkles,
  Check,
  AlertCircle
} from 'lucide-vue-next'

const themeStore = useThemeStore()
const notificationStore = useNotificationStore()
const userStore = useUserStore()
const router = useRouter()

// Layout state
const isSidebarCollapsed = ref(false)
const isNotificationsOpen = ref(false)
const isUserMenuOpen = ref(false)

// Layout computed properties
const isDarkTheme = computed(() => themeStore.isDark)
const unreadNotifications = computed(() => notificationStore.unreadCount)
const userName = computed(() => userStore.userInfo?.username || '教师')
const userEmail = computed(() => userStore.userInfo?.email || 'teacher@example.com')
const userNameInitial = computed(() => {
  const name = userName.value
  return name.charAt(0).toUpperCase()
})

// Layout methods
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value
}

const toggleTheme = () => {
  themeStore.toggleTheme()
}

const toggleNotifications = () => {
  isNotificationsOpen.value = !isNotificationsOpen.value
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const userMenuRef = ref<HTMLElement | null>(null)
const closeUserMenu = (e: MouseEvent) => {
  if (userMenuRef.value && !userMenuRef.value.contains(e.target as Node)) {
    isUserMenuOpen.value = false
  }
}

// 上传状态
type UploadStatus = 'idle' | 'dragging' | 'uploading' | 'success' | 'error'

const uploadStatus = ref<UploadStatus>('idle')
const uploadProgress = ref(0)
const uploadedFile = ref<File | null>(null)
const videoPreview = ref<string | null>(null)

// 表单数据
const formData = ref({
  course: '',
  title: '',
  description: '',
  order: ''
})

// 表单验证
const formErrors = ref({
  course: '',
  title: ''
})

// 模拟课程数据
const courses = [
  { value: '1', label: '高等数学' },
  { value: '2', label: '大学物理' },
  { value: '3', label: '计算机基础' },
  { value: '4', label: '英语' }
]

// 拖拽事件处理
const handleDragOver = (e: DragEvent) => {
  e.preventDefault()
  if (uploadStatus.value === 'idle') {
    uploadStatus.value = 'dragging'
  }
}

const handleDragLeave = () => {
  if (uploadStatus.value === 'dragging') {
    uploadStatus.value = 'idle'
  }
}

const handleDrop = (e: DragEvent) => {
  e.preventDefault()
  uploadStatus.value = 'idle'

  const files = e.dataTransfer?.files
  if (files && files.length > 0) {
    handleFileSelect(files[0])
  }
}

// 文件选择处理
const fileInput = ref<HTMLInputElement | null>(null)

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileInputChange = (e: Event) => {
  const target = e.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    handleFileSelect(target.files[0])
  }
}

const handleFileSelect = (file: File) => {
  // 验证文件类型
  const validTypes = ['video/mp4', 'video/webm', 'video/quicktime']
  if (!validTypes.includes(file.type)) {
    uploadStatus.value = 'error'
    return
  }

  // 验证文件大小（500MB）
  if (file.size > 500 * 1024 * 1024) {
    uploadStatus.value = 'error'
    return
  }

  uploadedFile.value = file
  uploadStatus.value = 'uploading'

  // 生成视频预览
  const reader = new FileReader()
  reader.onload = (e) => {
    videoPreview.value = e.target?.result as string
  }
  reader.readAsDataURL(file)

  // 模拟上传进度
  simulateUpload()
}

const simulateUpload = () => {
  let progress = 0
  const interval = setInterval(() => {
    progress += 5
    uploadProgress.value = progress

    if (progress >= 100) {
      clearInterval(interval)
      uploadStatus.value = 'success'
    }
  }, 200)
}

// 重新上传
const handleRetry = () => {
  uploadStatus.value = 'idle'
  uploadProgress.value = 0
  uploadedFile.value = null
  videoPreview.value = null
}

// 表单验证
const validateForm = () => {
  let isValid = true

  if (!formData.value.course) {
    formErrors.value.course = '请选择课程'
    isValid = false
  } else {
    formErrors.value.course = ''
  }

  if (!formData.value.title) {
    formErrors.value.title = '请输入视频标题'
    isValid = false
  } else if (formData.value.title.length > 100) {
    formErrors.value.title = '标题不能超过100个字符'
    isValid = false
  } else {
    formErrors.value.title = ''
  }

  return isValid
}

// 提交表单
const handleSubmit = () => {
  if (validateForm()) {
    console.log('提交表单:', formData.value)
    // 这里可以添加实际的提交逻辑
  }
}

// 取消
const handleCancel = () => {
  // 重置表单
  uploadStatus.value = 'idle'
  uploadProgress.value = 0
  uploadedFile.value = null
  videoPreview.value = null
  formData.value = {
    course: '',
    title: '',
    description: '',
    order: ''
  }
  formErrors.value = {
    course: '',
    title: ''
  }
}

onMounted(() => {
  // 确保使用教师端浅色主题
  const root = document.documentElement
  root.setAttribute('data-theme', 'teacher-light')
  root.style.colorScheme = 'light'

  // Click outside handler for user menu
  document.addEventListener('click', closeUserMenu)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeUserMenu)
})
</script>

<template>
  <div class="teacher-home">
    <!-- Left Sidebar -->
    <aside class="sidebar" :class="{ 'collapsed': isSidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <GraduationCap :size="24" class="logo-icon" />
          <h1 v-if="!isSidebarCollapsed">ZhiKe</h1>
          <div class="logo-dot" v-if="!isSidebarCollapsed"></div>
        </div>
        <div class="role-badge" v-if="!isSidebarCollapsed">
          教师
        </div>
      </div>
      <nav class="sidebar-nav" aria-label="主导航">
        <ul class="nav-list">
          <li class="nav-item" :class="{ active: $route.path === '/teacher/home' }">
            <router-link to="/teacher/home" class="nav-link">
              <BarChart3 :size="20" />
              <span v-if="!isSidebarCollapsed">仪表盘</span>
            </router-link>
          </li>
          <li class="nav-item" :class="{ active: $route.path === '/teacher/knowledge' }">
            <router-link to="/teacher/knowledge" class="nav-link">
              <FileText :size="20" />
              <span v-if="!isSidebarCollapsed">知识片段</span>
            </router-link>
          </li>
          <li class="nav-item" :class="{ active: $route.path === '/teacher/analytics' }">
            <router-link to="/teacher/analytics" class="nav-link">
              <TrendingUp :size="20" />
              <span v-if="!isSidebarCollapsed">学情分析</span>
            </router-link>
          </li>
          <li class="nav-item" :class="{ active: $route.path === '/teacher/courses' }">
            <router-link to="/teacher/courses" class="nav-link">
              <BookOpen :size="20" />
              <span v-if="!isSidebarCollapsed">课程管理</span>
            </router-link>
          </li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <button
          class="collapse-button"
          @click="toggleSidebar"
          :aria-label="isSidebarCollapsed ? '展开侧边栏' : '折叠侧边栏'"
        >
          <ChevronLeft v-if="!isSidebarCollapsed" :size="20" />
          <ChevronRight v-else :size="20" />
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
      <!-- Top Navigation -->
      <header class="top-nav">
        <div class="nav-container">
          <div class="nav-left">
            <h1 class="page-title">视频管理</h1>
          </div>
          <div class="nav-right">
            <button
              class="nav-button theme-toggle"
              @click="toggleTheme"
              aria-label="切换主题"
            >
              <Sun v-if="isDarkTheme" :size="20" />
              <Moon v-else :size="20" />
            </button>
            <button
              class="nav-button notification"
              @click="toggleNotifications"
              aria-label="通知"
            >
              <Bell :size="20" />
              <span v-if="unreadNotifications > 0" class="notification-badge" :aria-label="`${unreadNotifications} 条未读通知`">{{ unreadNotifications }}</span>
            </button>
            <div class="nav-user-menu" ref="userMenuRef">
              <button class="nav-user-trigger" @click="isUserMenuOpen = !isUserMenuOpen" aria-label="用户菜单" aria-expanded="isUserMenuOpen">
                <div class="nav-user-avatar">
                  <span>{{ userNameInitial }}</span>
                </div>
                <span class="nav-user-name">{{ userName }}</span>
                <ChevronDown :size="14" class="nav-user-arrow" :class="{ rotated: isUserMenuOpen }" />
              </button>
              <div v-if="isUserMenuOpen" class="nav-user-dropdown">
                <div class="dropdown-user-info">
                  <div class="dropdown-avatar">
                    <span>{{ userNameInitial }}</span>
                  </div>
                  <div class="dropdown-details">
                    <div class="dropdown-name">{{ userName }}</div>
                    <div class="dropdown-email">{{ userEmail }}</div>
                  </div>
                </div>
                <div class="dropdown-divider"></div>
                <button class="dropdown-item dropdown-item-danger" @click="handleLogout">
                  <LogOut :size="16" />
                  <span>退出登录</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- Upload Page Content -->
      <div class="video-upload">
        <div class="container">
          <h2 class="upload-page-title">视频管理</h2>

          <!-- 上传区域 -->
          <div
            class="upload-area"
            :class="{ 'dragging': uploadStatus === 'dragging' }"
            @dragover="handleDragOver"
            @dragleave="handleDragLeave"
            @drop="handleDrop"
            @click="triggerFileInput"
            role="button"
            aria-label="点击或拖拽上传视频文件"
          >
            <input
              ref="fileInput"
              type="file"
              accept="video/mp4,video/webm,video/quicktime"
              class="file-input"
              @change="handleFileInputChange"
            />

            <!-- 上传状态 -->
            <div v-if="uploadStatus === 'idle'" class="upload-content">
              <Upload :size="48" class="upload-icon" />
              <h3 class="upload-title">拖拽视频文件到此处</h3>
              <p class="upload-subtitle">或 <span class="upload-link">点击选择文件</span></p>
              <p class="upload-hint">支持 MP4/WebM/MOV, 最大 500MB</p>
            </div>

            <div v-else-if="uploadStatus === 'uploading'" class="upload-progress">
              <div class="progress-bar">
                <div
                  class="progress-fill"
                  :style="{ width: `${uploadProgress}%` }"
                  role="progressbar"
                  :aria-valuenow="uploadProgress"
                  aria-valuemin="0"
                  aria-valuemax="100"
                ></div>
              </div>
              <p class="progress-text">上传中... {{ uploadProgress }}%</p>
            </div>

            <div v-else-if="uploadStatus === 'success'" class="upload-result success">
              <Check :size="48" class="result-icon" />
              <h3 class="result-title">上传完成</h3>
              <p class="result-text">{{ uploadedFile?.name }}</p>
            </div>

            <div v-else-if="uploadStatus === 'error'" class="upload-result error">
              <AlertCircle :size="48" class="result-icon" />
              <h3 class="result-title">上传失败</h3>
              <p class="result-text">文件格式或大小不符合要求</p>
              <button class="retry-button" @click="handleRetry">重新上传</button>
            </div>
          </div>

          <!-- 视频预览 -->
          <div v-if="videoPreview" class="video-preview">
            <video
              :src="videoPreview"
              class="preview-video"
              controls
              muted
            ></video>
          </div>

          <!-- 视频信息表单 -->
          <form class="video-form" @submit.prevent="handleSubmit">
            <div class="form-row">
              <div class="form-group">
                <label for="course" class="form-label">课程选择 <span class="required">*</span></label>
                <select
                  id="course"
                  v-model="formData.course"
                  class="form-select"
                  :class="{ 'error': formErrors.course }"
                >
                  <option value="">请选择课程</option>
                  <option v-for="course in courses" :key="course.value" :value="course.value">
                    {{ course.label }}
                  </option>
                </select>
                <p v-if="formErrors.course" class="error-message">{{ formErrors.course }}</p>
              </div>

              <div class="form-group">
                <label for="order" class="form-label">排序序号</label>
                <input
                  type="number"
                  id="order"
                  v-model="formData.order"
                  class="form-input"
                  min="0"
                />
              </div>
            </div>

            <div class="form-group">
              <label for="title" class="form-label">视频标题 <span class="required">*</span></label>
              <input
                type="text"
                id="title"
                v-model="formData.title"
                class="form-input"
                :class="{ 'error': formErrors.title }"
                maxlength="100"
                placeholder="请输入视频标题"
              />
              <p v-if="formErrors.title" class="error-message">{{ formErrors.title }}</p>
              <p class="char-count">{{ formData.title.length }}/100</p>
            </div>

            <div class="form-group">
              <label for="description" class="form-label">视频描述</label>
              <textarea
                id="description"
                v-model="formData.description"
                class="form-textarea"
                maxlength="500"
                placeholder="请输入视频描述"
                rows="4"
              ></textarea>
              <p class="char-count">{{ formData.description.length }}/500</p>
            </div>

            <!-- 操作按钮 -->
            <div class="form-actions">
              <button type="button" class="cancel-button" @click="handleCancel">
                取消
              </button>
              <button type="submit" class="submit-button" :disabled="uploadStatus !== 'success'">
                <Sparkles :size="16" class="button-icon" />
                保存并解析
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
/* ==================== Layout Styles (from Home.vue) ==================== */
.teacher-home {
  display: flex;
  min-height: 100vh;
  background: var(--color-bg-canvas, #fdfdf8);
  color: var(--color-text-primary, #0f172a);
}

/* Sidebar */
.sidebar {
  width: 240px;
  background: var(--color-bg-canvas, #fdfdf8);
  border-right: 1px solid var(--color-border, #e2e8f0);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  z-index: 100;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  color: var(--color-accent, #6366f1);
  flex-shrink: 0;
}

.logo h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.logo-dot {
  width: 8px;
  height: 8px;
  background: var(--color-accent, #6366f1);
  border-radius: 50%;
  margin-left: 0.5rem;
}

.role-badge {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full, 9999px);
}

.sidebar-nav {
  flex: 1;
  padding: 16px 0;
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  margin-bottom: 4px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  text-decoration: none;
  color: var(--color-text-secondary, #64748b);
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  margin: 0 8px;
  position: relative;
}

.nav-link:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.nav-item.active .nav-link {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-text-primary, #0f172a);
  border-left: 3px solid var(--color-accent, #6366f1);
  margin-left: 0;
  padding-left: 13px;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.collapse-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

/* Main Content */
.main-content {
  flex: 1;
  margin-left: 240px;
  transition: margin-left 0.3s ease;
  min-height: 100vh;
}

.main-content.sidebar-collapsed {
  margin-left: 64px;
}

/* Top Navigation */
.top-nav {
  height: 64px;
  background: var(--color-bg-card, #ffffff);
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  position: sticky;
  top: 0;
  z-index: 90;
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
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  background: var(--color-danger, #ef4444);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-user-menu {
  position: relative;
}

.nav-user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px 4px 4px;
  border-radius: var(--radius-full, 9999px);
  background: var(--color-bg-elevated, #f8fafc);
  border: 1px solid var(--color-border, #e2e8f0);
  cursor: pointer;
  transition: all 0.2s ease;
}

.nav-user-trigger:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.nav-user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-accent, #6366f1);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.875rem;
}

.nav-user-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--color-text-primary, #0f172a);
}

.nav-user-arrow {
  color: var(--color-text-tertiary, #94a3b8);
  transition: transform 0.2s ease;
}

.nav-user-arrow.rotated {
  transform: rotate(180deg);
}

.nav-user-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  width: 240px;
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 8px);
  box-shadow: var(--shadow-lg, 0 12px 40px rgba(0,0,0,0.12));
  overflow: hidden;
  z-index: 100;
}

.dropdown-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
}

.dropdown-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--color-accent, #6366f1);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 1rem;
  flex-shrink: 0;
}

.dropdown-details {
  overflow: hidden;
}

.dropdown-name {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-email {
  font-size: 0.75rem;
  color: var(--color-text-tertiary, #94a3b8);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-divider {
  height: 1px;
  background: var(--color-border, #e2e8f0);
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 16px;
  border: none;
  background: none;
  color: var(--color-text-primary, #0f172a);
  font-size: 0.875rem;
  cursor: pointer;
  transition: background 0.15s ease;
  text-decoration: none;
}

.dropdown-item:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.dropdown-item-danger {
  color: var(--color-danger, #ef4444);
}

.dropdown-item-danger:hover {
  background: var(--color-danger-bg, rgba(239, 68, 68, 0.08));
}

/* ==================== VideoUpload-specific Styles ==================== */
.video-upload {
  padding: var(--space-8) 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 var(--space-6);
}

.upload-page-title {
  font-family: 'IBM Plex Sans', system-ui, sans-serif;
  font-size: var(--text-h2);
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: var(--space-8);
  text-align: center;
}

/* 上传区域 */
.upload-area {
  border: 2px dashed var(--color-border);
  border-radius: var(--radius-lg);
  padding: var(--space-12);
  text-align: center;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  margin-bottom: var(--space-8);
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-area.dragging {
  background: var(--color-accent-subtle);
  border-color: var(--color-accent);
  border-style: solid;
  transform: scale(1.01);
}

.file-input {
  display: none;
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-4);
}

.upload-icon {
  color: var(--color-text-tertiary);
}

.upload-title {
  font-family: 'IBM Plex Sans', system-ui, sans-serif;
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.upload-subtitle {
  color: var(--color-text-secondary);
  margin: 0;
}

.upload-link {
  color: var(--color-accent);
  text-decoration: underline;
  cursor: pointer;
}

.upload-hint {
  color: var(--color-text-tertiary);
  font-size: var(--text-xs);
  margin: 0;
}

/* 上传进度 */
.upload-progress {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-4);
  width: 100%;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: var(--color-bg-elevated);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: var(--color-accent);
  border-radius: var(--radius-full);
  transition: width 0.3s ease;
}

.progress-text {
  color: var(--color-text-secondary);
  margin: 0;
}

/* 上传结果 */
.upload-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-4);
}

.result-icon {
  color: var(--color-success);
}

.result-icon.error {
  color: var(--color-danger);
}

.result-title {
  font-family: 'IBM Plex Sans', system-ui, sans-serif;
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.result-text {
  color: var(--color-text-secondary);
  margin: 0;
}

.retry-button {
  background: transparent;
  border: 1px solid var(--color-accent);
  color: var(--color-accent);
  padding: var(--space-2) var(--space-4);
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}

.retry-button:hover {
  background: var(--color-accent-subtle);
}

/* 视频预览 */
.video-preview {
  margin-bottom: var(--space-8);
}

.preview-video {
  width: 100%;
  aspect-ratio: 16/9;
  border-radius: var(--radius-md);
  background: var(--color-bg-elevated);
}

/* 表单 */
.video-form {
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  padding: var(--space-6);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-4);
  margin-bottom: var(--space-4);
}

.form-group {
  margin-bottom: var(--space-4);
}

.form-label {
  display: block;
  font-family: 'IBM Plex Sans', system-ui, sans-serif;
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.required {
  color: var(--color-danger);
}

.form-select,
.form-input {
  width: 100%;
  height: 40px;
  padding: 0 var(--space-4);
  background: var(--color-bg-input);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  color: var(--color-text-primary);
  font-size: var(--text-base);
  transition: all var(--duration-normal) var(--ease-default);
}

.form-select:focus,
.form-input:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

.form-select.error,
.form-input.error {
  border-color: var(--color-danger);
}

.form-textarea {
  width: 100%;
  padding: var(--space-3);
  background: var(--color-bg-input);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  color: var(--color-text-primary);
  font-size: var(--text-base);
  resize: vertical;
  transition: all var(--duration-normal) var(--ease-default);
}

.form-textarea:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

.error-message {
  color: var(--color-danger);
  font-size: var(--text-xs);
  margin-top: var(--space-1);
  margin-bottom: 0;
}

.char-count {
  color: var(--color-text-tertiary);
  font-size: var(--text-xs);
  margin-top: var(--space-1);
  margin-bottom: 0;
  text-align: right;
}

/* 操作按钮 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
  margin-top: var(--space-6);
}

.cancel-button {
  background: transparent;
  border: 1px solid var(--color-text-secondary);
  color: var(--color-text-secondary);
  padding: var(--space-3) var(--space-6);
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  height: 36px;
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.cancel-button:hover {
  background: var(--color-bg-hover);
}

.submit-button {
  background: var(--color-cta-bg);
  border: none;
  color: var(--color-cta-text);
  padding: var(--space-3) var(--space-6);
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  height: 36px;
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.submit-button:hover:not(:disabled) {
  opacity: 0.9;
  transform: translateY(-1px);
}

.submit-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.button-icon {
  flex-shrink: 0;
}

/* Responsive Design */
@media (max-width: 1023px) {
  .sidebar {
    width: 64px;
  }

  .sidebar.collapsed {
    width: 0;
  }

  .main-content {
    margin-left: 64px;
  }

  .main-content.sidebar-collapsed {
    margin-left: 0;
  }
}

@media (max-width: 767px) {
  .upload-area {
    min-height: 200px;
    padding: var(--space-8);
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .container {
    padding: 0 var(--space-4);
  }
}
</style>
