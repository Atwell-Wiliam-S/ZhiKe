<template>
  <header class="student-top-nav">
    <div class="nav-container">
      <div class="nav-left">
        <router-link to="/student/home" class="logo" aria-label="返回首页">
          <GraduationCap :size="24" class="logo-icon" />
          <h1>ZhiKe</h1>
        </router-link>
        <nav class="nav-links" aria-label="主导航">
          <router-link
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            class="nav-link"
            :class="{ active: isActive(item.path) }"
          >
            <component :is="item.icon" :size="18" />
            <span class="nav-link-text">{{ item.label }}</span>
          </router-link>
        </nav>
      </div>
      <div class="nav-right">
        <span class="role-badge">学生</span>
        <button
          class="nav-button"
          @click="toggleTheme"
          aria-label="切换主题"
        >
          <Sun v-if="isDarkTheme" :size="18" />
          <Moon v-else :size="18" />
        </button>
        <button
          class="nav-button notification"
          @click="toggleNotifications"
          aria-label="通知"
        >
          <Bell :size="18" />
          <span v-if="unreadNotifications > 0" class="notification-badge">{{ unreadNotifications > 9 ? '9+' : unreadNotifications }}</span>
        </button>
        <div class="user-menu" ref="userMenuRef">
          <button
            class="user-menu-button"
            @click="toggleUserMenu"
            aria-label="用户菜单"
            aria-expanded="isUserMenuOpen"
          >
            <div class="user-avatar">
              <span>{{ userNameInitial }}</span>
            </div>
            <ChevronDown :size="14" class="chevron" :class="{ rotated: isUserMenuOpen }" />
          </button>
          <Transition name="dropdown">
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
              <router-link to="/settings" class="user-menu-item" @click="isUserMenuOpen = false">
                <User :size="16" />
                <span>个人资料</span>
              </router-link>
              <div class="user-menu-divider"></div>
              <button class="user-menu-item logout" @click="logout">
                <LogOut :size="16" />
                <span>退出登录</span>
              </button>
            </div>
          </Transition>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useThemeStore } from '@/stores/theme';
import { useNotificationStore } from '@/stores/notification';
import {
  Sun, Moon, Bell, ChevronDown, User, LogOut, GraduationCap,
  Home, BookOpen, MessageSquare, FileText
} from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const themeStore = useThemeStore();
const notificationStore = useNotificationStore();

const navItems = [
  { path: '/student/home', label: '首页', icon: Home },
  { path: '/student/course', label: '我的课程', icon: BookOpen },
  { path: '/student/qa/history', label: '问答历史', icon: MessageSquare },
  { path: '/student/notes', label: '我的笔记', icon: FileText },
];

const isUserMenuOpen = ref(false);

const isDarkTheme = computed(() => themeStore.isDark);
const unreadNotifications = computed(() => notificationStore.unreadCount);
const userName = computed(() => userStore.userInfo?.username || '学生');
const userEmail = computed(() => userStore.userInfo?.email || 'student@example.com');
const userNameInitial = computed(() => userName.value.charAt(0).toUpperCase());

const isActive = (path: string) => {
  if (path === '/student/home') {
    return route.path === '/student/home';
  }
  return route.path.startsWith(path);
};

const toggleTheme = () => themeStore.toggleTheme();
const toggleNotifications = () => notificationStore.openDrawer();
const toggleUserMenu = () => { isUserMenuOpen.value = !isUserMenuOpen.value; };

const logout = () => {
  isUserMenuOpen.value = false;
  userStore.logout();
  router.push('/login');
};

const handleClickOutside = (event: MouseEvent) => {
  if (!(event.target as HTMLElement).closest('.user-menu')) {
    isUserMenuOpen.value = false;
  }
};

window.addEventListener('click', handleClickOutside);
onUnmounted(() => window.removeEventListener('click', handleClickOutside));
</script>

<style scoped>
.student-top-nav {
  height: 60px;
  flex-shrink: 0;
  background: var(--color-bg-card, #252540);
  border-bottom: 1px solid var(--color-border, #3a3a5c);
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

.nav-container {
  max-width: 1400px;
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
  gap: 36px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: var(--color-text-primary, #e2e8f0);
  flex-shrink: 0;
}

.logo h1 {
  font-size: 1.2rem;
  font-weight: 700;
  margin: 0;
  letter-spacing: -0.02em;
}

.logo-icon {
  color: var(--color-accent, #6366f1);
}

/* Navigation Links */
.nav-links {
  display: flex;
  align-items: center;
  gap: 2px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  border-radius: var(--radius-md, 6px);
  color: var(--color-text-secondary, #94a3b8);
  text-decoration: none;
  font-size: var(--text-sm, 0.875rem);
  font-weight: 500;
  transition: all 0.2s ease;
  white-space: nowrap;
  position: relative;
}

.nav-link:hover {
  background: var(--color-bg-hover, rgba(255,255,255,0.05));
  color: var(--color-text-primary, #e2e8f0);
}

.nav-link.active {
  color: var(--color-accent, #6366f1);
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: -14px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background: var(--color-accent, #6366f1);
  border-radius: 1px;
}

/* Right Section */
.nav-right {
  display: flex;
  align-items: center;
  gap: 6px;
}

.role-badge {
  background: var(--color-accent-subtle, rgba(99,102,241,0.15));
  color: var(--color-accent, #6366f1);
  font-size: 0.7rem;
  font-weight: 600;
  padding: 3px 10px;
  border-radius: var(--radius-full, 9999px);
  letter-spacing: 0.02em;
}

.nav-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #94a3b8);
  cursor: pointer;
  padding: 8px;
  border-radius: var(--radius-md, 6px);
  transition: all 0.15s ease;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button:hover {
  background: var(--color-bg-hover, rgba(255,255,255,0.05));
  color: var(--color-text-primary, #e2e8f0);
}

.notification-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  background: var(--color-danger, #ef4444);
  color: white;
  font-size: 0.6rem;
  font-weight: 700;
  min-width: 16px;
  height: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
  line-height: 1;
}

/* User Menu */
.user-menu {
  position: relative;
}

.user-menu-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  border-radius: var(--radius-md, 6px);
  display: flex;
  align-items: center;
  gap: 2px;
  transition: all 0.15s ease;
}

.user-menu-button:hover {
  background: var(--color-bg-hover, rgba(255,255,255,0.05));
}

.chevron {
  transition: transform 0.2s ease;
  color: var(--color-text-tertiary, #64748b);
}

.chevron.rotated {
  transform: rotate(180deg);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-accent, #6366f1), var(--color-accent-hover, #818cf8));
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.8rem;
}

.user-avatar.large {
  width: 40px;
  height: 40px;
  font-size: 1rem;
}

/* Dropdown with animation */
.user-menu-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: var(--color-bg-card, #252540);
  border: 1px solid var(--color-border, #3a3a5c);
  border-radius: var(--radius-lg, 8px);
  box-shadow: var(--shadow-lg, 0 12px 40px rgba(0,0,0,0.5));
  width: 220px;
  z-index: 1000;
  overflow: hidden;
}

.dropdown-enter-active {
  transition: all 0.2s ease-out;
}

.dropdown-leave-active {
  transition: all 0.15s ease-in;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-8px) scale(0.96);
}

.user-menu-header {
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #3a3a5c);
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-weight: 600;
  font-size: var(--text-sm, 0.875rem);
  color: var(--color-text-primary, #e2e8f0);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-email {
  font-size: var(--text-xs, 0.75rem);
  color: var(--color-text-secondary, #94a3b8);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-menu-divider {
  height: 1px;
  background: var(--color-border, #3a3a5c);
}

.user-menu-item {
  width: 100%;
  background: none;
  border: none;
  padding: 10px 16px;
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--color-text-primary, #e2e8f0);
  cursor: pointer;
  transition: all 0.15s ease;
  text-align: left;
  text-decoration: none;
  font-size: var(--text-sm, 0.875rem);
}

.user-menu-item:hover {
  background: var(--color-bg-hover, rgba(255,255,255,0.05));
}

.user-menu-item.logout {
  color: var(--color-danger, #ef4444);
}

.user-menu-item.logout:hover {
  background: var(--color-danger-bg, rgba(239,68,68,0.12));
}

/* Responsive */
@media (max-width: 1023px) {
  .nav-link-text {
    display: none;
  }
  .nav-link {
    padding: 8px;
  }
  .nav-links {
    gap: 2px;
  }
  .nav-link.active::after {
    bottom: -10px;
    width: 16px;
  }
  .role-badge {
    display: none;
  }
}

@media (max-width: 767px) {
  .student-top-nav {
    height: 52px;
  }
  .nav-container {
    padding: 0 16px;
  }
  .nav-left {
    gap: 12px;
  }
  .logo h1 {
    font-size: 1rem;
  }
  .logo-icon {
    width: 22px;
    height: 22px;
  }
}
</style>
