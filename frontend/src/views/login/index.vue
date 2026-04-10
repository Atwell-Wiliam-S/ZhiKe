<template>
  <div class="login-container">
    <!-- Left Brand Section -->
    <div class="brand-section">
      <div class="decorations">
        <div class="decoration decoration-1"></div>
        <div class="decoration decoration-2"></div>
        <div class="decoration decoration-3"></div>
      </div>
      <div class="brand-content">
        <div class="logo">
          <h1>ZhiKe</h1>
          <div class="logo-dot"></div>
        </div>
        <p class="slogan">AI 驱动的智能教学闭环系统</p>
        <div class="features">
          <div class="feature-item">
            <span class="feature-icon"><GraduationCap :size="24" /></span>
            <span class="feature-text">多模态视频解析</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon"><Bot :size="24" /></span>
            <span class="feature-text">AI 智能问答助手</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon"><BarChart3 :size="24" /></span>
            <span class="feature-text">学情热力图分析</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Right Form Section -->
    <div class="form-section">
      <div class="form-container">
        <div class="mobile-logo">
          <h1>ZhiKe</h1>
          <div class="logo-dot"></div>
        </div>
        <h2>{{ isLogin ? '欢迎回来' : '创建账号' }}</h2>
        <p class="subtitle">{{ isLogin ? '登录以继续学习' : '开始你的学习之旅' }}</p>

        <form @submit.prevent="handleSubmit" class="login-form">
          <!-- Username -->
          <div class="form-group">
            <label for="username" class="sr-only">用户名</label>
            <div class="input-wrapper">
              <span class="input-icon">
                <User :size="20" />
              </span>
              <input
                type="text"
                id="username"
                v-model="form.username"
                placeholder="用户名"
                class="form-input"
                :class="{ 'error': errors.username }"
                aria-describedby="username-error"
              />
            </div>
            <p v-if="errors.username" id="username-error" class="error-message">{{ errors.username }}</p>
          </div>

          <!-- Password -->
          <div class="form-group">
            <label for="password" class="sr-only">密码</label>
            <div class="input-wrapper">
              <span class="input-icon">
                <Lock :size="20" />
              </span>
              <input
                type="password"
                id="password"
                v-model="form.password"
                placeholder="密码"
                class="form-input"
                :class="{ 'error': errors.password }"
                aria-describedby="password-error"
              />
              <button
                type="button"
                class="password-toggle"
                @click="showPassword = !showPassword"
                :aria-label="showPassword ? '隐藏密码' : '显示密码'"
              >
                <Eye v-if="!showPassword" :size="20" />
                <EyeOff v-else :size="20" />
              </button>
            </div>
            <p v-if="errors.password" id="password-error" class="error-message">{{ errors.password }}</p>
          </div>

          <!-- Email (only for register) -->
          <div v-if="!isLogin" class="form-group">
            <label for="email" class="sr-only">邮箱</label>
            <div class="input-wrapper">
              <span class="input-icon">
                <Mail :size="20" />
              </span>
              <input
                type="email"
                id="email"
                v-model="form.email"
                placeholder="邮箱"
                class="form-input"
                :class="{ 'error': errors.email }"
                aria-describedby="email-error"
              />
            </div>
            <p v-if="errors.email" id="email-error" class="error-message">{{ errors.email }}</p>
          </div>

          <!-- Role Selection (only for register) -->
          <div v-if="!isLogin" class="form-group">
            <label class="role-label">角色</label>
            <div class="role-group" role="radiogroup" aria-labelledby="role-label">
              <label class="role-option">
                <input
                  type="radio"
                  v-model="form.role"
                  value="STUDENT"
                  class="role-input"
                />
                <span class="role-text">学生</span>
              </label>
              <label class="role-option">
                <input
                  type="radio"
                  v-model="form.role"
                  value="TEACHER"
                  class="role-input"
                />
                <span class="role-text">教师</span>
              </label>
            </div>
            <p v-if="errors.role" class="error-message">{{ errors.role }}</p>
          </div>

          <!-- Remember Me & Forgot Password (only for login) -->
          <div v-if="isLogin" class="form-options">
            <label class="remember-me">
              <input
                type="checkbox"
                v-model="form.rememberMe"
                class="checkbox"
              />
              <span class="checkbox-label">记住我</span>
            </label>
            <a href="#" class="forgot-password">忘记密码？</a>
          </div>

          <!-- Submit Button -->
          <button
            type="submit"
            class="submit-button"
            :disabled="isLoading"
          >
            <span v-if="isLoading" class="spinner"></span>
            {{ isLoading ? '登录中...' : (isLogin ? '登录' : '注册') }}
          </button>

          <!-- Divider -->
          <div class="divider">
            <span class="divider-text">或</span>
          </div>

          <!-- Third Party Login (placeholder) -->
          <div class="third-party-login">
            <button type="button" class="third-party-button">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
              </svg>
              <span>Google</span>
            </button>
          </div>

          <!-- Switch Between Login/Register -->
          <div class="switch-mode">
            {{ isLogin ? '没有账号？' : '已有账号？' }}
            <button
              type="button"
              class="switch-button"
              @click="isLogin = !isLogin"
            >
              {{ isLogin ? '立即注册' : '立即登录' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { GraduationCap, Bot, BarChart3, User, Lock, Eye, EyeOff, Mail } from 'lucide-vue-next';

const router = useRouter();
const userStore = useUserStore();

// State
const isLogin = ref(true);
const showPassword = ref(false);
const isLoading = ref(false);

// Form data
const form = reactive({
  username: '',
  password: '',
  email: '',
  role: 'STUDENT',
  rememberMe: false
});

// Form errors
const errors = reactive({
  username: '',
  password: '',
  email: '',
  role: ''
});

// Form validation
const validateForm = () => {
  let isValid = true;

  // Reset errors
  Object.keys(errors).forEach(key => {
    errors[key as keyof typeof errors] = '';
  });

  // Validate username
  if (!form.username.trim()) {
    errors.username = '请输入用户名';
    isValid = false;
  }

  // Validate password
  if (!form.password) {
    errors.password = '请输入密码';
    isValid = false;
  } else if (form.password.length < 6) {
    errors.password = '密码长度至少为6位';
    isValid = false;
  }

  // Validate email (only for register)
  if (!isLogin.value && !form.email.trim()) {
    errors.email = '请输入邮箱';
    isValid = false;
  } else if (!isLogin.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = '请输入有效的邮箱地址';
    isValid = false;
  }

  // Validate role (only for register)
  if (!isLogin.value && !form.role) {
    errors.role = '请选择角色';
    isValid = false;
  }

  return isValid;
};

// Handle form submission
const handleSubmit = async () => {
  if (!validateForm()) {
    return;
  }

  isLoading.value = true;

  try {
    if (isLogin.value) {
      // Login logic
      await userStore.login(form.username, form.password);
    } else {
      // Register logic (mock)
      // In real implementation, this would call an API endpoint
      console.log('Registering user:', form);
      // Simulate successful registration
      setTimeout(() => {
        isLoading.value = false;
        isLogin.value = true;
      }, 1000);
      return;
    }

    // Redirect based on role
    const role = userStore.userInfo?.roleCode || 'STUDENT';
    if (role === 'STUDENT') {
      router.push('/student/home');
    } else if (role === 'TEACHER') {
      router.push('/teacher/home');
    }
  } catch (error: any) {
    // Handle error
    if (error.message) {
      // Show error message
      console.error('Error:', error.message);
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* Brand Section */
.brand-section {
  flex: 0 0 50%;
  background: linear-gradient(135deg, #1a1a2e 0%, #6366f1 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  padding: 2rem;
}

.decorations {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.decoration {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
  animation: float 6s ease-in-out infinite;
}

.decoration-1 {
  width: 200px;
  height: 200px;
  background: rgba(255, 255, 255, 0.3);
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.decoration-2 {
  width: 150px;
  height: 150px;
  background: rgba(255, 255, 255, 0.2);
  bottom: 20%;
  right: 15%;
  animation-delay: 2s;
}

.decoration-3 {
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.25);
  top: 50%;
  right: 25%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

.brand-content {
  max-width: 400px;
  z-index: 1;
}

.logo {
  display: flex;
  align-items: center;
  margin-bottom: 2rem;
}

.logo h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0;
}

.logo-dot {
  width: 8px;
  height: 8px;
  background: #6366f1;
  border-radius: 50%;
  margin-left: 0.5rem;
}

.slogan {
  font-size: 1.25rem;
  margin-bottom: 3rem;
  opacity: 0.9;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.feature-icon {
  font-size: 1.5rem;
}

.feature-text {
  font-size: 1rem;
  opacity: 0.9;
}

/* Form Section */
.form-section {
  flex: 0 0 50%;
  background: var(--color-bg-canvas, #ffffff);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.form-container {
  max-width: 400px;
  width: 100%;
}

.mobile-logo {
  display: none;
  flex-align: center;
  margin-bottom: 2rem;
}

.mobile-logo h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.mobile-logo .logo-dot {
  background: var(--color-accent, #6366f1);
}

.form-container h2 {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  color: var(--color-text-primary, #0f172a);
}

.subtitle {
  color: var(--color-text-secondary, #64748b);
  margin-bottom: 2rem;
}

/* Form Styles */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 1rem;
  color: var(--color-text-tertiary, #94a3b8);
}

.form-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 3rem;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  background: var(--color-bg-input, #f8fafc);
  color: var(--color-text-primary, #0f172a);
  font-size: 1rem;
  transition: all 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.form-input.error {
  border-color: var(--color-danger, #ef4444);
}

.password-toggle {
  position: absolute;
  right: 1rem;
  background: none;
  border: none;
  color: var(--color-text-tertiary, #94a3b8);
  cursor: pointer;
  padding: 0.25rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
}

.password-toggle:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.role-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
}

.role-group {
  display: flex;
  gap: 1rem;
}

.role-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.role-input {
  accent-color: var(--color-accent, #6366f1);
}

.role-text {
  color: var(--color-text-primary, #0f172a);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.checkbox {
  accent-color: var(--color-accent, #6366f1);
}

.checkbox-label {
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
}

.forgot-password {
  font-size: 0.875rem;
  color: var(--color-accent, #6366f1);
  text-decoration: none;
  transition: all 0.2s ease;
}

.forgot-password:hover {
  text-decoration: underline;
}

.submit-button {
  width: 100%;
  padding: 0.75rem;
  background: var(--color-accent, #6366f1);
  color: var(--color-accent-text, #ffffff);
  border: none;
  border-radius: var(--radius-md, 8px);
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.submit-button:hover {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

.submit-button:active {
  transform: scale(0.98);
}

.submit-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
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

.error-message {
  font-size: 0.75rem;
  color: var(--color-danger, #ef4444);
  margin: 0;
}

.divider {
  position: relative;
  height: 1px;
  background: var(--color-border, #e2e8f0);
  margin: 1.5rem 0;
}

.divider-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: var(--color-bg-canvas, #ffffff);
  padding: 0 1rem;
  font-size: 0.875rem;
  color: var(--color-text-tertiary, #94a3b8);
}

.third-party-login {
  margin-bottom: 1.5rem;
}

.third-party-button {
  width: 100%;
  padding: 0.75rem;
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #0f172a);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.third-party-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.switch-mode {
  text-align: center;
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
}

.switch-button {
  background: none;
  border: none;
  color: var(--color-accent, #6366f1);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  padding: 0;
  margin-left: 0.25rem;
  transition: all 0.2s ease;
}

.switch-button:hover {
  text-decoration: underline;
}

.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

/* Responsive Design */
@media (max-width: 1023px) {
  .brand-section {
    flex: 0 0 40%;
  }
  
  .form-section {
    flex: 0 0 60%;
  }
}

@media (max-width: 767px) {
  .login-container {
    flex-direction: column;
  }
  
  .brand-section {
    flex: 0 0 120px;
    padding: 1rem;
  }
  
  .brand-content {
    max-width: 100%;
    text-align: center;
  }
  
  .logo h1 {
    font-size: 1.5rem;
  }
  
  .slogan {
    font-size: 0.875rem;
    margin-bottom: 1rem;
  }
  
  .features {
    flex-direction: row;
    justify-content: center;
    gap: 1rem;
  }
  
  .feature-item {
    flex-direction: column;
    gap: 0.25rem;
  }
  
  .feature-icon {
    font-size: 1.25rem;
  }
  
  .feature-text {
    font-size: 0.75rem;
  }
  
  .form-section {
    flex: 1;
    padding: 1.5rem;
  }
  
  .mobile-logo {
    display: flex;
  }
  
  .form-container h2 {
    font-size: 1.25rem;
  }
  
  .subtitle {
    font-size: 0.875rem;
  }
}
</style>