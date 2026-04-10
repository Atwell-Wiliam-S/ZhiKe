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
          <li class="nav-item" :class="{ active: $route.path === '/teacher/upload' }">
            <router-link to="/teacher/upload" class="nav-link">
              <Video :size="20" />
              <span v-if="!isSidebarCollapsed">视频管理</span>
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
            <h1 class="page-title">仪表盘</h1>
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

      <!-- KPI Cards -->
      <section class="kpi-section">
        <div class="kpi-grid">
          <div class="kpi-card">
            <div class="kpi-content">
              <div class="kpi-value">{{ kpiData.totalStudents }}</div>
              <div class="kpi-label">总学生数</div>
            </div>
            <div class="kpi-icon">
              <Users :size="24" />
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-content">
              <div class="kpi-value">{{ kpiData.activeCourses }}</div>
              <div class="kpi-label">活跃课程</div>
            </div>
            <div class="kpi-icon">
              <BookOpen :size="24" />
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-content">
              <div class="kpi-value">{{ kpiData.averageCompletion }}%</div>
              <div class="kpi-label">平均完成率</div>
              <div class="kpi-trend" :class="{ 'up': kpiData.completionTrend > 0, 'down': kpiData.completionTrend < 0 }">
                <TrendingUp v-if="kpiData.completionTrend > 0" :size="16" />
                <TrendingDown v-else :size="16" />
                <span>{{ Math.abs(kpiData.completionTrend) }}%</span>
              </div>
            </div>
            <div class="kpi-icon">
              <BarChart3 :size="24" />
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-content">
              <div class="kpi-value">{{ kpiData.todayQuestions }}</div>
              <div class="kpi-label">今日提问</div>
            </div>
            <div class="kpi-icon">
              <Info :size="24" />
            </div>
          </div>
        </div>
      </section>

      <!-- Course Management Table -->
      <section class="course-section">
        <div class="section-header">
          <h2 class="section-title">课程管理</h2>
          <div class="section-actions">
            <button class="add-course-button">
              <Plus :size="16" />
              <span>添加课程</span>
            </button>
          </div>
        </div>
        <div class="course-table-container">
          <table class="course-table">
            <thead>
              <tr>
                <th>课程名称</th>
                <th>教师</th>
                <th>视频数</th>
                <th>学生数</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="course in courses" :key="course.id" class="course-row">
                <td class="course-name">{{ course.name }}</td>
                <td>{{ course.teacher }}</td>
                <td>{{ course.videoCount }}</td>
                <td>{{ course.studentCount }}</td>
                <td>
                  <span class="status-badge" :class="course.status">
                    {{ course.status === 'active' ? '活跃' : '已关闭' }}
                  </span>
                </td>
                <td class="course-actions">
                  <button class="action-button" aria-label="查看课程">
                    <Eye :size="16" />
                  </button>
                  <button class="action-button" aria-label="编辑课程">
                    <Pencil :size="16" />
                  </button>
                  <button class="action-button" aria-label="删除课程">
                    <Trash2 :size="16" />
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="pagination">
          <div class="pagination-info">
            显示 1-{{ courses.length }} 条，共 {{ courses.length }} 条
          </div>
          <div class="pagination-controls">
            <button class="pagination-button" :disabled="currentPage === 1">
              <ChevronLeft :size="16" />
            </button>
            <button class="pagination-button active">1</button>
            <button class="pagination-button" disabled>
              <ChevronRight :size="16" />
            </button>
          </div>
        </div>
      </section>

      <!-- Recent Activity -->
      <section class="activity-section">
        <h2 class="section-title">最近活动</h2>
        <div class="activity-list">
          <div class="activity-item" v-for="activity in recentActivities" :key="activity.id">
            <div class="activity-dot"></div>
            <div class="activity-content">
              <div class="activity-time">{{ activity.time }}</div>
              <div class="activity-description">{{ activity.description }}</div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { useThemeStore } from '@/stores/theme';
import { useNotificationStore } from '@/stores/notification';
import { useUserStore } from '@/stores/user';
import {
  BarChart3,
  BookOpen,
  Video,
  FileText,
  TrendingUp,
  TrendingDown,
  Settings,
  ChevronLeft,
  ChevronRight,
  Sun,
  Moon,
  Bell,
  Users,
  Plus,
  Eye,
  Pencil,
  Trash2,
  Info,
  LogOut,
  GraduationCap
} from 'lucide-vue-next';

const themeStore = useThemeStore();
const notificationStore = useNotificationStore();
const userStore = useUserStore();
const router = useRouter();

// State
const isSidebarCollapsed = ref(false);
const isNotificationsOpen = ref(false);
const isUserMenuOpen = ref(false);
const currentPage = ref(1);

// Computed properties
const isDarkTheme = computed(() => themeStore.isDark);
const unreadNotifications = computed(() => notificationStore.unreadCount);
const userName = computed(() => userStore.userInfo?.username || '教师');
const userEmail = computed(() => userStore.userInfo?.email || 'teacher@example.com');
const userNameInitial = computed(() => {
  const name = userName.value;
  return name.charAt(0).toUpperCase();
});

// Data
const kpiData = {
  totalStudents: 1234,
  activeCourses: 12,
  averageCompletion: 85,
  completionTrend: 5,
  todayQuestions: 42
};

const courses = [
  {
    id: 1,
    name: '机器学习基础',
    teacher: '张老师',
    videoCount: 24,
    studentCount: 345,
    status: 'active'
  },
  {
    id: 2,
    name: '深度学习进阶',
    teacher: '李老师',
    videoCount: 36,
    studentCount: 289,
    status: 'active'
  },
  {
    id: 3,
    name: '计算机视觉',
    teacher: '王老师',
    videoCount: 28,
    studentCount: 215,
    status: 'active'
  },
  {
    id: 4,
    name: '自然语言处理',
    teacher: '赵老师',
    videoCount: 32,
    studentCount: 198,
    status: 'active'
  },
  {
    id: 5,
    name: '强化学习',
    teacher: '刘老师',
    videoCount: 20,
    studentCount: 156,
    status: 'active'
  }
];

const recentActivities = [
  {
    id: 1,
    time: '10:30',
    description: '学生张三完成了「机器学习基础」课程的测验'
  },
  {
    id: 2,
    time: '09:15',
    description: '学生李四报名了「深度学习进阶」课程'
  },
  {
    id: 3,
    time: '08:45',
    description: '系统自动生成了「计算机视觉」课程的学情报告'
  },
  {
    id: 4,
    time: '昨天',
    description: '你更新了「自然语言处理」课程的视频内容'
  },
  {
    id: 5,
    time: '昨天',
    description: '学生王五在「强化学习」课程中提出了问题'
  }
];

// Methods
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

const toggleTheme = () => {
  themeStore.toggleTheme();
};

const toggleNotifications = () => {
  isNotificationsOpen.value = !isNotificationsOpen.value;
};

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
};

const userMenuRef = ref<HTMLElement | null>(null);
const closeUserMenu = (e: MouseEvent) => {
  if (userMenuRef.value && !userMenuRef.value.contains(e.target as Node)) {
    isUserMenuOpen.value = false;
  }
};
onMounted(() => document.addEventListener('click', closeUserMenu));
onBeforeUnmount(() => document.removeEventListener('click', closeUserMenu));
</script>

<style scoped>
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

/* KPI Section */
.kpi-section {
  padding: 24px;
  background: var(--color-bg-canvas, #fdfdf8);
}

.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.kpi-card {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
}

.kpi-card:hover {
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  transform: translateY(-2px);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.kpi-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(99, 102, 241, 0.1), transparent);
  transition: left 0.6s ease;
}

.kpi-card:hover::before {
  left: 100%;
}

.kpi-content {
  flex: 1;
}

.kpi-value {
  font-size: 2.25rem;
  font-weight: 700;
  color: var(--color-text-primary, #0f172a);
  margin-bottom: 4px;
  font-family: 'IBM Plex Sans', sans-serif;
}

.kpi-label {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  margin-bottom: 8px;
}

.kpi-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.75rem;
  font-weight: 600;
}

.kpi-trend.up {
  color: var(--color-success, #10b981);
}

.kpi-trend.down {
  color: var(--color-danger, #ef4444);
}

.kpi-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md, 8px);
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

/* Course Section */
.course-section {
  padding: 0 24px 24px;
  background: var(--color-bg-canvas, #fdfdf8);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-top: 24px;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.section-actions {
  display: flex;
  gap: 8px;
}

.add-course-button {
  background: var(--color-accent, #6366f1);
  color: white;
  border: none;
  border-radius: var(--radius-md, 8px);
  padding: 8px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.add-course-button:hover {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

.course-table-container {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.course-table {
  width: 100%;
  border-collapse: collapse;
}

.course-table th,
.course-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.course-table th {
  background: var(--color-bg-elevated, #f8fafc);
  font-weight: 600;
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
}

.course-row {
  transition: all 0.2s ease;
  cursor: pointer;
}

.course-row:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.course-row:hover .course-name {
  color: #F54E00;
}

.course-name {
  font-weight: 600;
  color: var(--color-text-primary, #0f172a);
  transition: all 0.2s ease;
}

.status-badge {
  font-size: 0.75rem;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: var(--radius-full, 9999px);
}

.status-badge.active {
  background: var(--color-success-subtle, rgba(16, 185, 129, 0.1));
  color: var(--color-success, #10b981);
}

.status-badge.inactive {
  background: var(--color-danger-subtle, rgba(239, 68, 68, 0.1));
  color: var(--color-danger, #ef4444);
}

.course-actions {
  display: flex;
  gap: 8px;
}

.action-button {
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

.action-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16px;
  padding: 16px;
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
}

.pagination-info {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
}

.pagination-controls {
  display: flex;
  gap: 8px;
  align-items: center;
}

.pagination-button {
  background: none;
  border: 1px solid var(--color-border, #e2e8f0);
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 6px 12px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-button:hover:not(:disabled) {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.pagination-button.active {
  background: var(--color-accent, #6366f1);
  color: white;
  border-color: var(--color-accent, #6366f1);
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Activity Section */
.activity-section {
  padding: 0 24px 24px;
  background: var(--color-bg-canvas, #fdfdf8);
}

.activity-list {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  padding: 16px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.activity-item {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  position: relative;
}

.activity-item::before {
  content: '';
  position: absolute;
  left: 7px;
  top: 28px;
  bottom: -12px;
  width: 2px;
  background: var(--color-border, #e2e8f0);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-item:last-child::before {
  display: none;
}

.activity-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: var(--color-accent, #6366f1);
  flex-shrink: 0;
  margin-top: 4px;
  position: relative;
  z-index: 1;
}

.activity-content {
  flex: 1;
}

.activity-time {
  font-size: 0.75rem;
  color: var(--color-text-tertiary, #94a3b8);
  margin-bottom: 4px;
}

.activity-description {
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
  line-height: 1.4;
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
  
  .kpi-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .course-table {
    font-size: 0.875rem;
  }
  
  .course-table th,
  .course-table td {
    padding: 8px 12px;
  }
}

@media (max-width: 767px) {
  .kpi-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .pagination {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .pagination-controls {
    align-self: flex-end;
  }
  
  .course-table {
    font-size: 0.75rem;
  }
  
  .course-table th,
  .course-table td {
    padding: 6px 8px;
  }
  
  .course-actions {
    gap: 4px;
  }
  
  .action-button {
    padding: 2px;
  }
  
  .action-button svg {
    width: 14px;
    height: 14px;
  }
}
</style>