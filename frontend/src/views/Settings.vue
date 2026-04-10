<template>
  <div class="settings-page">
    <!-- 顶部导航 -->
    <StudentTopNav />

    <div class="settings-container">
      <!-- 左侧设置导航 -->
      <nav class="settings-nav" aria-label="设置导航">
        <div class="nav-item" :class="{ active: activeTab === 'profile' }" @click="activeTab = 'profile'">
          <span class="nav-icon"><User :size="18" /></span>
          <span class="nav-text">个人信息</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'theme' }" @click="activeTab = 'theme'" v-if="userRole === 'STUDENT'">
          <span class="nav-icon"><Palette :size="18" /></span>
          <span class="nav-text">主题偏好</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'security' }" @click="activeTab = 'security'">
          <span class="nav-icon"><Shield :size="18" /></span>
          <span class="nav-text">安全设置</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'notification' }" @click="activeTab = 'notification'">
          <span class="nav-icon"><Bell :size="18" /></span>
          <span class="nav-text">通知设置</span>
        </div>
      </nav>

      <!-- 右侧设置表单 -->
      <div class="settings-content">
        <!-- 个人信息表单 -->
        <div v-if="activeTab === 'profile'" class="settings-section">
          <h2 class="section-title">个人信息</h2>
          <div class="profile-form">
            <!-- 头像区域 -->
            <div class="avatar-section">
              <div class="avatar-container">
                <img :src="user.avatar || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=default%20user%20avatar&image_size=square'" alt="用户头像" class="avatar">
                <button class="avatar-upload-btn" aria-label="更换头像">
                  <Camera :size="12" />
                </button>
              </div>
            </div>

            <!-- 表单字段 -->
            <div class="form-field">
              <label for="username">用户名</label>
              <input type="text" id="username" v-model="user.username" readonly class="readonly-input">
            </div>
            <div class="form-field">
              <label for="email">邮箱</label>
              <input type="email" id="email" v-model="user.email">
            </div>

            <!-- 保存按钮 -->
            <div class="form-actions">
              <button class="save-btn" @click="saveProfile">保存更改</button>
            </div>
          </div>
        </div>

        <!-- 主题偏好设置 -->
        <div v-if="activeTab === 'theme'" class="settings-section">
          <h2 class="section-title">主题偏好</h2>
          <div class="theme-settings">
            <div class="theme-options">
              <div class="theme-option" :class="{ active: selectedTheme === 'student-dark' }" @click="selectedTheme = 'student-dark'">
                <div class="theme-icon"><Moon :size="24" /></div>
                <span class="theme-text">暗色模式</span>
              </div>
              <div class="theme-option" :class="{ active: selectedTheme === 'student-light' }" @click="selectedTheme = 'student-light'">
                <div class="theme-icon"><Sun :size="24" /></div>
                <span class="theme-text">亮色模式</span>
              </div>
              <div class="theme-option" :class="{ active: !hasSavedTheme }" @click="resetTheme">
                <div class="theme-icon"><Monitor :size="24" /></div>
                <span class="theme-text">跟随系统</span>
              </div>
            </div>
            <div class="theme-preview">
              <div class="preview-card" :class="selectedTheme.includes('dark') ? 'dark' : 'light'">
                <div class="preview-header"></div>
                <div class="preview-content"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 安全设置 -->
        <div v-if="activeTab === 'security'" class="settings-section">
          <h2 class="section-title">安全设置</h2>
          <div class="security-settings">
            <div class="form-field">
              <label for="old-password">旧密码</label>
              <div class="password-input">
                <input type="password" id="old-password" v-model="passwords.oldPassword">
                <button class="toggle-password" @click="showOldPassword = !showOldPassword" aria-label="切换密码可见性">
                  <Eye v-if="showOldPassword" :size="16" />
                  <EyeOff v-else :size="16" />
                </button>
              </div>
            </div>
            <div class="form-field">
              <label for="new-password">新密码</label>
              <input type="password" id="new-password" v-model="passwords.newPassword">
            </div>
            <div class="form-field">
              <label for="confirm-password">确认新密码</label>
              <input type="password" id="confirm-password" v-model="passwords.confirmPassword">
            </div>
            
            <!-- 密码强度指示器 -->
            <div class="password-strength" v-if="passwords.newPassword">
              <div class="strength-bar" :class="passwordStrengthClass"></div>
              <div class="strength-text">{{ passwordStrengthText }}</div>
            </div>

            <!-- 保存按钮 -->
            <div class="form-actions">
              <button class="save-btn" @click="changePassword">修改密码</button>
            </div>
          </div>
        </div>

        <!-- 通知设置 -->
        <div v-if="activeTab === 'notification'" class="settings-section">
          <h2 class="section-title">通知设置</h2>
          <div class="notification-settings">
            <div class="notification-option">
              <span class="option-text">课程更新通知</span>
              <label class="toggle-switch">
                <input type="checkbox" v-model="notifications.courseUpdates">
                <span class="toggle-slider"></span>
              </label>
            </div>
            <div class="notification-option">
              <span class="option-text">作业提醒</span>
              <label class="toggle-switch">
                <input type="checkbox" v-model="notifications.homeworkReminders">
                <span class="toggle-slider"></span>
              </label>
            </div>
            <div class="notification-option">
              <span class="option-text">系统公告</span>
              <label class="toggle-switch">
                <input type="checkbox" v-model="notifications.systemAnnouncements">
                <span class="toggle-slider"></span>
              </label>
            </div>
            <div class="notification-option">
              <span class="option-text">邮件通知汇总</span>
              <label class="toggle-switch">
                <input type="checkbox" v-model="notifications.emailDigest">
                <span class="toggle-slider"></span>
              </label>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { User, Palette, Shield, Bell, Camera, Moon, Sun, Monitor, Eye, EyeOff } from 'lucide-vue-next'
import StudentTopNav from '@/components/ui/StudentTopNav.vue'

const userStore = useUserStore()
const themeStore = useThemeStore()

// 激活的标签页
const activeTab = ref('profile')

// 用户信息
const user = ref({
  username: userStore.userInfo?.username || 'student123',
  email: userStore.userInfo?.email || 'student@example.com',
  avatar: userStore.userInfo?.avatarUrl || ''
})

// 用户角色
const userRole = computed(() => userStore.roleCode)

// 主题设置
const selectedTheme = ref(themeStore.currentTheme || 'student-dark')

// 是否有保存的主题设置
const hasSavedTheme = computed(() => {
  return localStorage.getItem('zhike-theme') !== null
})

// 密码设置
const passwords = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const showOldPassword = ref(false)

// 密码强度计算
const passwordStrengthClass = computed(() => {
  const password = passwords.value.newPassword
  if (!password) return ''
  
  let strength = 0
  if (password.length >= 8) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[a-z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^A-Za-z0-9]/.test(password)) strength++
  
  switch (strength) {
    case 0: return 'strength-0'
    case 1: return 'strength-1'
    case 2: return 'strength-2'
    case 3: return 'strength-3'
    case 4: return 'strength-4'
    case 5: return 'strength-5'
    default: return ''
  }
})

const passwordStrengthText = computed(() => {
  const password = passwords.value.newPassword
  if (!password) return ''
  
  let strength = 0
  if (password.length >= 8) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[a-z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^A-Za-z0-9]/.test(password)) strength++
  
  switch (strength) {
    case 0: return '请输入密码'
    case 1: return '弱'
    case 2: return '弱'
    case 3: return '中'
    case 4: return '强'
    case 5: return '强'
    default: return ''
  }
})

// 通知设置
const notifications = ref({
  courseUpdates: true,
  homeworkReminders: true,
  systemAnnouncements: true,
  emailDigest: false
})

// 保存个人信息
const saveProfile = () => {
  // 这里可以添加保存逻辑
  console.log('保存个人信息:', user.value)
  // 模拟保存成功
  alert('个人信息保存成功！')
}

// 修改密码
const changePassword = () => {
  if (passwords.value.newPassword !== passwords.value.confirmPassword) {
    alert('两次输入的密码不一致！')
    return
  }
  
  if (passwords.value.newPassword.length < 8) {
    alert('新密码长度至少为8位！')
    return
  }
  
  // 这里可以添加修改密码逻辑
  console.log('修改密码:', passwords.value)
  // 模拟修改成功
  alert('密码修改成功！')
  // 清空表单
  passwords.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

// 重置主题为跟随系统
const resetTheme = () => {
  localStorage.removeItem('zhike-theme')
  // 重新初始化主题
  themeStore.initTheme(userRole.value)
  selectedTheme.value = themeStore.currentTheme
}

// 监听主题变化
selectedTheme.value = themeStore.currentTheme
</script>

<style scoped>
.settings-page {
  min-height: 100vh;
  background-color: var(--color-bg-canvas);
  padding-top: 76px;
  padding-left: 24px;
  padding-right: 24px;
  padding-bottom: 24px;
}

.settings-container {
  max-width: 960px;
  margin: 0 auto;
  display: flex;
  gap: 24px;
}

/* 左侧导航 */
.settings-nav {
  width: 200px;
  background-color: var(--color-bg-card);
  border-radius: var(--radius-md);
  padding: 8px;
  flex-shrink: 0;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 150ms ease;
  color: var(--color-text-secondary);
}

.nav-item:hover {
  background-color: var(--color-bg-hover);
}

.nav-item.active {
  background-color: var(--color-accent-subtle);
  color: var(--color-text-primary);
  border-left: 3px solid var(--color-accent);
}

.nav-icon {
  font-size: 18px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 右侧内容 */
.settings-content {
  flex: 1;
  background-color: var(--color-bg-card);
  border-radius: var(--radius-md);
  padding: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 24px;
}

/* 个人信息表单 */
.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.avatar-container {
  position: relative;
  width: 80px;
  height: 80px;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: var(--radius-full);
  object-fit: cover;
}

.avatar-upload-btn {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background-color: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-field label {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-primary);
}

.form-field input,
.form-field textarea {
  padding: 10px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background-color: var(--color-bg-elevated);
  color: var(--color-text-primary);
  font-size: 14px;
}

.readonly-input {
  background-color: var(--color-bg-elevated);
  cursor: not-allowed;
  opacity: 0.7;
}

.form-field textarea {
  resize: vertical;
  min-height: 80px;
}

.char-counter {
  font-size: 12px;
  color: var(--color-text-tertiary);
  text-align: right;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.save-btn {
  padding: 10px 24px;
  background-color: var(--color-accent);
  color: var(--color-accent-text);
  border: none;
  border-radius: var(--radius-sm);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 150ms ease;
}

.save-btn:hover {
  background-color: var(--color-accent-hover);
}

/* 主题设置 */
.theme-settings {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.theme-options {
  display: flex;
  gap: 16px;
}

.theme-option {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 150ms ease;
}

.theme-option:hover {
  border-color: var(--color-accent);
}

.theme-option.active {
  border-color: var(--color-accent);
  background-color: var(--color-accent-subtle);
}

.theme-icon {
  font-size: 24px;
}

.theme-text {
  font-size: 14px;
  color: var(--color-text-primary);
}

.theme-preview {
  display: flex;
  justify-content: center;
}

.preview-card {
  width: 200px;
  height: 120px;
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-md);
}

.preview-card.dark {
  background-color: #1e1e1e;
}

.preview-card.light {
  background-color: #ffffff;
}

.preview-card.system {
  background-color: var(--color-bg-card);
}

.preview-header {
  height: 30px;
  background-color: rgba(255, 255, 255, 0.1);
}

.preview-content {
  height: 90px;
  background-color: rgba(255, 255, 255, 0.05);
}

/* 安全设置 */
.security-settings {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.password-input {
  position: relative;
}

.password-input input {
  width: 100%;
  padding-right: 40px;
}

.toggle-password {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

.password-strength {
  margin-top: 8px;
}

.strength-bar {
  height: 4px;
  border-radius: 2px;
  transition: all 200ms ease;
}

.strength-0 {
  width: 0%;
  background-color: var(--color-text-tertiary);
}

.strength-1 {
  width: 20%;
  background-color: var(--color-danger);
}

.strength-2 {
  width: 40%;
  background-color: var(--color-danger);
}

.strength-3 {
  width: 60%;
  background-color: var(--color-warning);
}

.strength-4 {
  width: 80%;
  background-color: var(--color-success);
}

.strength-5 {
  width: 100%;
  background-color: var(--color-success);
}

.strength-text {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: 4px;
}

/* 通知设置 */
.notification-settings {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notification-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border);
}

.option-text {
  font-size: 14px;
  color: var(--color-text-primary);
}

/* 切换开关 */
.toggle-switch {
  position: relative;
  display: inline-block;
  width: 48px;
  height: 24px;
}

.toggle-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--color-bg-elevated);
  transition: .4s;
  border-radius: 24px;
  border: 1px solid var(--color-border);
}

.toggle-slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 2px;
  bottom: 2px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
  box-shadow: var(--shadow-sm);
}

input:checked + .toggle-slider {
  background-color: var(--color-accent);
  border-color: var(--color-accent);
}

input:checked + .toggle-slider:before {
  transform: translateX(24px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .settings-container {
    flex-direction: column;
  }
  
  .settings-nav {
    width: 100%;
    display: flex;
    overflow-x: auto;
  }
  
  .nav-item {
    flex-shrink: 0;
    border-left: none;
    border-bottom: 3px solid transparent;
  }
  
  .nav-item.active {
    border-left: none;
    border-bottom: 3px solid var(--color-accent);
  }
  
  .theme-options {
    flex-wrap: wrap;
  }
  
  .theme-option {
    flex: 1 1 calc(33.333% - 12px);
  }
}
</style>