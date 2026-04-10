<template>
  <div class="student-home">
    <!-- Top Navigation -->
    <header class="top-nav">
      <div class="nav-container">
        <div class="nav-left">
          <div class="logo">
            <h1>ZhiKe</h1>
            <div class="logo-dot"></div>
          </div>
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
          <button
            class="nav-button notification"
            @click="toggleNotifications"
            aria-label="通知"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
              <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
            </svg>
            <span v-if="unreadNotifications > 0" class="notification-badge" :aria-label="`${unreadNotifications} 条未读通知`">{{ unreadNotifications }}</span>
          </button>
          <div class="role-badge">
            学生
          </div>
          <div class="user-menu">
            <button
              class="user-menu-button"
              @click="toggleUserMenu"
              aria-label="用户菜单"
            >
              <div class="user-avatar">
                <span>{{ userNameInitial }}</span>
              </div>
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="6 9 12 15 18 9"></polyline>
              </svg>
            </button>
            <div v-if="isUserMenuOpen" class="user-menu-dropdown">
              <div class="user-menu-header">
                <div class="user-avatar large">
                  <span>{{ userNameInitial }}</span>
                </div>
                <div class="user-info">
                  <div class="user-name">{{ userName }}</div>
                  <div class="user-email">{{ userEmail }}</div>
                </div>
              </div>
              <div class="user-menu-divider"></div>
              <button class="user-menu-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
                <span>个人资料</span>
              </button>
              <button class="user-menu-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="12" cy="12" r="3"></circle>
                  <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
                </svg>
                <span>设置</span>
              </button>
              <button class="user-menu-item" @click="logout">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
                  <polyline points="16 17 21 12 16 7"></polyline>
                  <line x1="21" y1="12" x2="9" y2="12"></line>
                </svg>
                <span>退出登录</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="main-content">
      <!-- Hero Section -->
      <section class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">Hi, {{ userName }}</h1>
          <p class="hero-subtitle">今天想学点什么？</p>
        </div>
      </section>

      <!-- Feature Cards Grid -->
      <section class="feature-section">
        <ul class="feature-grid" role="list">
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"></path>
                </svg>
              </div>
              <h3 class="feature-title">我的课程</h3>
              <p class="feature-description">查看我已报名的所有课程</p>
              <div class="feature-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="9 18 15 12 9 6"></polyline>
                </svg>
              </div>
            </a>
          </li>
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="12" cy="12" r="10"></circle>
                  <polygon points="10 8 16 12 10 16 10 8"></polygon>
                </svg>
              </div>
              <h3 class="feature-title">继续学习</h3>
              <p class="feature-description">从上次学习的地方继续</p>
              <div class="feature-progress">
                <div class="progress-bar">
                  <div class="progress-fill" style="width: 65%"></div>
                </div>
                <span class="progress-text">65% 完成</span>
              </div>
            </a>
          </li>
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20"></path>
                  <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path>
                  <path d="M12 17h.01"></path>
                </svg>
              </div>
              <h3 class="feature-title">AI 助教</h3>
              <p class="feature-description">有问题随时问我</p>
              <div class="feature-status">
                <span class="status-dot"></span>
                <span class="status-text">在线</span>
              </div>
            </a>
          </li>
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                  <polyline points="14 2 14 8 20 8"></polyline>
                  <line x1="16" y1="13" x2="8" y2="13"></line>
                  <line x1="16" y1="17" x2="8" y2="17"></line>
                  <polyline points="10 9 9 9 8 9"></polyline>
                </svg>
              </div>
              <h3 class="feature-title">我的笔记</h3>
              <p class="feature-description">查看我的学习笔记</p>
              <div class="feature-badge">12</div>
            </a>
          </li>
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M3 3v18h18"></path>
                  <path d="M19 9H5"></path>
                  <path d="M16 14H5"></path>
                  <path d="M12 19H5"></path>
                </svg>
              </div>
              <h3 class="feature-title">学习报告</h3>
              <p class="feature-description">查看本周学习情况</p>
              <div class="feature-badge">12.5h</div>
            </a>
          </li>
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="12" cy="12" r="3"></circle>
                  <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
                </svg>
              </div>
              <h3 class="feature-title">设置</h3>
              <p class="feature-description">主题、个人信息</p>
              <div class="feature-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="9 18 15 12 9 6"></polyline>
                </svg>
              </div>
            </a>
          </li>
        </ul>
      </section>

      <!-- Recent Activity -->
      <section class="activity-section">
        <h2 class="section-title">最近活动</h2>
        <div class="activity-list">
          <div class="activity-item">
            <div class="activity-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
              </svg>
            </div>
            <div class="activity-content">
              <p class="activity-description">观看了视频：<strong>机器学习基础</strong></p>
              <span class="activity-time">2小时前</span>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
              </svg>
            </div>
            <div class="activity-content">
              <p class="activity-description">添加了笔记：<strong>监督学习算法</strong></p>
              <span class="activity-time">5小时前</span>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="12" y1="8" x2="12" y2="12"></line>
                <line x1="12" y1="16" x2="12.01" y2="16"></line>
              </svg>
            </div>
            <div class="activity-content">
              <p class="activity-description">完成了测验：<strong>线性回归</strong></p>
              <span class="activity-time">昨天</span>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"></path>
              </svg>
            </div>
            <div class="activity-content">
              <p class="activity-description">报名了课程：<strong>深度学习进阶</strong></p>
              <span class="activity-time">2天前</span>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useThemeStore } from '@/stores/theme';
import { useNotificationStore } from '@/stores/notification';

const router = useRouter();
const userStore = useUserStore();
const themeStore = useThemeStore();
const notificationStore = useNotificationStore();

// State
const isUserMenuOpen = ref(false);

// Computed properties
const isDarkTheme = computed(() => themeStore.isDark);
const unreadNotifications = computed(() => notificationStore.unreadCount);
const userName = computed(() => userStore.userInfo?.username || '学生');
const userEmail = computed(() => userStore.userInfo?.email || 'student@example.com');
const userNameInitial = computed(() => {
  const name = userName.value;
  return name.charAt(0).toUpperCase();
});

// Methods
const toggleTheme = () => {
  themeStore.toggleTheme();
};

const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value;
};

const toggleNotifications = () => {
  notificationStore.openDrawer();
};

const logout = () => {
  userStore.logout();
  router.push('/login');
};

// Close dropdowns when clicking outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (!target.closest('.user-menu')) {
    isUserMenuOpen.value = false;
  }
};

// Add event listener
window.addEventListener('click', handleClickOutside);

// Cleanup
const cleanup = () => {
  window.removeEventListener('click', handleClickOutside);
};

// Register cleanup function
import { onUnmounted } from 'vue';
onUnmounted(cleanup);
</script>

<style scoped>
.student-home {
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
  max-width: var(--container-2xl, 1280px);
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

.logo {
  display: flex;
  align-items: center;
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

.role-badge {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full, 9999px);
}

.user-menu {
  position: relative;
}

.user-menu-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: var(--radius-md, 8px);
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
}

.user-menu-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.user-avatar {
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

.user-avatar.large {
  width: 48px;
  height: 48px;
  font-size: 1.25rem;
}

.user-menu-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 0.5rem;
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-md, 8px);
  box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
  width: 240px;
  z-index: 1000;
  overflow: hidden;
}

.user-menu-header {
  padding: 1rem;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
  margin-bottom: 0.25rem;
}

.user-email {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
}

.user-menu-divider {
  height: 1px;
  background: var(--color-border, #e2e8f0);
}

.user-menu-item {
  width: 100%;
  background: none;
  border: none;
  padding: 0.75rem 1rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: var(--color-text-primary, #0f172a);
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: left;
}

.user-menu-item:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

/* Main Content */
.main-content {
  max-width: var(--container-2xl, 1280px);
  margin: 0 auto;
  padding: 0 24px;
}

/* Hero Section */
.hero-section {
  padding: 48px 0 32px;
}

.hero-content {
  max-width: 600px;
}

.hero-title {
  font-size: 2.25rem;
  font-weight: 400;
  margin: 0 0 0.5rem;
  color: var(--color-text-primary, #0f172a);
}

.hero-subtitle {
  font-size: 1.125rem;
  color: var(--color-text-secondary, #64748b);
  margin: 0;
}

/* Feature Section */
.feature-section {
  margin-bottom: 48px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-card {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  padding: 24px;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.feature-card:hover {
  border-color: var(--color-accent, #6366f1);
  box-shadow: var(--shadow-glow, 0 0 20px rgba(99, 102, 241, 0.2));
  transform: translateY(-2px);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(99, 102, 241, 0.1), transparent);
  transition: left 0.6s ease;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-link {
  display: block;
  text-decoration: none;
  color: var(--color-text-primary, #0f172a);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md, 8px);
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
}

.feature-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 0 0 0.5rem;
}

.feature-description {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  margin: 0 0 1rem;
  flex: 1;
}

.feature-progress {
  margin-top: auto;
}

.progress-bar {
  height: 4px;
  background: var(--color-bg-input, #f1f5f9);
  border-radius: var(--radius-full, 9999px);
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background: var(--color-accent, #6366f1);
  border-radius: var(--radius-full, 9999px);
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
}

.feature-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: auto;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-success, #10b981);
}

.status-text {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
}

.feature-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  background: var(--color-accent, #6366f1);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full, 9999px);
}

.feature-arrow {
  margin-top: auto;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  color: var(--color-text-secondary, #64748b);
}

/* Activity Section */
.activity-section {
  margin-bottom: 48px;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 1rem;
  color: var(--color-text-primary, #0f172a);
}

.activity-list {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
}

.activity-item {
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  transition: all 0.2s ease;
}

.activity-item:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-md, 8px);
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 2px;
}

.activity-content {
  flex: 1;
}

.activity-description {
  margin: 0 0 0.25rem;
  color: var(--color-text-primary, #0f172a);
}

.activity-time {
  font-size: 0.75rem;
  color: var(--color-text-tertiary, #94a3b8);
}

/* Responsive Design */
@media (max-width: 1023px) {
  .feature-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 767px) {
  .top-nav {
    height: 56px;
  }
  
  .nav-container {
    padding: 0 16px;
  }
  
  .logo h1 {
    font-size: 1.25rem;
  }
  
  .hero-section {
    padding: 24px 0 16px;
  }
  
  .hero-title {
    font-size: 1.75rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
  }
  
  .feature-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .feature-card {
    padding: 16px;
  }
  
  .main-content {
    padding: 0 16px;
  }
}

/* Dark Theme Support */
@media (prefers-color-scheme: dark) {
  :root {
    --color-bg-canvas: #0f172a;
    --color-bg-card: #1e293b;
    --color-bg-hover: #334155;
    --color-bg-input: #334155;
    --color-text-primary: #f8fafc;
    --color-text-secondary: #cbd5e1;
    --color-text-tertiary: #94a3b8;
    --color-border: #334155;
    --color-accent: #6366f1;
    --color-accent-subtle: rgba(99, 102, 241, 0.1);
    --color-danger: #ef4444;
    --color-success: #10b981;
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.3);
    --shadow-glow: 0 0 20px rgba(99, 102, 241, 0.3);
    --container-2xl: 1280px;
  }
}

/* Light Theme */
@media (prefers-color-scheme: light) {
  :root {
    --color-bg-canvas: #f8fafc;
    --color-bg-card: #ffffff;
    --color-bg-hover: #f1f5f9;
    --color-bg-input: #f1f5f9;
    --color-text-primary: #0f172a;
    --color-text-secondary: #64748b;
    --color-text-tertiary: #94a3b8;
    --color-border: #e2e8f0;
    --color-accent: #0075de;
    --color-accent-subtle: rgba(0, 117, 222, 0.15);
    --color-danger: #ef4444;
    --color-success: #10b981;
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
    --shadow-glow: 0 0 20px rgba(0, 117, 222, 0.2);
    --container-2xl: 1280px;
  }
}
</style>