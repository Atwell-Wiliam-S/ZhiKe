<script setup lang="ts">
import { ref } from 'vue'
import { Upload, Sparkles, Check, AlertCircle } from 'lucide-vue-next'

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
</script>

<template>
  <div class="video-upload">
    <div class="container">
      <h1 class="page-title">视频上传</h1>
      
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
          <h2 class="upload-title">拖拽视频文件到此处</h2>
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
          <h2 class="result-title">上传完成</h2>
          <p class="result-text">{{ uploadedFile?.name }}</p>
        </div>
        
        <div v-else-if="uploadStatus === 'error'" class="upload-result error">
          <AlertCircle :size="48" class="result-icon" />
          <h2 class="result-title">上传失败</h2>
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
</template>

<style scoped>
.video-upload {
  min-height: 100vh;
  background: var(--color-bg-canvas);
  padding: var(--space-8) 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 var(--space-6);
}

.page-title {
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

/* 响应式设计 */
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