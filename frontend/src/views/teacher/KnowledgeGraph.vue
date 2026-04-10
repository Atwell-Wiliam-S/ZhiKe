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
            <h1 class="page-title">知识片段</h1>
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

      <!-- Page Specific Content -->
      <div class="knowledge-graph">
        <!-- Top Filter Bar -->
        <div class="filter-bar">
          <div class="container">
            <!-- Filters -->
            <div class="filter-controls">
              <div class="filter-group">
                <select v-model="selectedCourse" class="filter-select">
                  <option value="机器学习基础">机器学习基础</option>
                  <option value="深度学习进阶">深度学习进阶</option>
                  <option value="计算机视觉">计算机视觉</option>
                </select>
                <select v-model="selectedVideo" class="filter-select">
                  <option value="第1讲：机器学习简介">第1讲：机器学习简介</option>
                  <option value="第2讲：监督学习">第2讲：监督学习</option>
                  <option value="第3讲：无监督学习">第3讲：无监督学习</option>
                </select>
                <div class="search-box">
                  <input
                    type="text"
                    v-model="searchQuery"
                    placeholder="搜索知识片段..."
                    class="search-input"
                  />
                  <Search :size="16" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Main Content -->
        <div class="content-container">
          <div class="container">
            <!-- Left Column: Video Preview -->
            <div class="left-column">
              <div class="video-preview">
                <div class="video-wrapper">
                  <video class="video-player" controls>
                    <source src="https://example.com/video.mp4" type="video/mp4" />
                    Your browser does not support the video tag.
                  </video>
                </div>
                <div class="timeline-overlay">
                  <div
                    v-for="(segment, index) in segments"
                    :key="index"
                    class="timeline-segment"
                    :class="segment.status"
                    :style="{
                      left: segment.startPosition + '%',
                      width: (segment.endPosition - segment.startPosition) + '%'
                    }"
                    :aria-label="`${getStatusText(segment.status)}: ${segment.title}`"
                  ></div>
                </div>
              </div>
            </div>

            <!-- Right Column: Segment List -->
            <div class="right-column">
              <div class="segment-list">
                <div
                  v-for="(segment, index) in filteredSegments"
                  :key="index"
                  class="segment-card"
                >
                  <div class="segment-header">
                    <h3 class="segment-title">{{ segment.title }}</h3>
                    <div class="segment-time">
                      {{ formatTime(segment.startTime) }} - {{ formatTime(segment.endTime) }}
                    </div>
                  </div>
                  <p class="segment-content">{{ segment.content }}</p>
                  <div class="segment-tags">
                    <span
                      v-for="(tag, tagIndex) in segment.tags"
                      :key="tagIndex"
                      class="tag"
                    >
                      {{ tag }}
                    </span>
                  </div>
                  <div class="segment-actions">
                    <button class="action-button" @click="editSegment(index)" aria-label="编辑片段">
                      <Pencil :size="16" />
                    </button>
                    <button class="action-button" @click="previewSegment(index)" aria-label="预览片段">
                      <Eye :size="16" />
                    </button>
                    <button class="action-button delete" @click="deleteSegment(index)" aria-label="删除片段">
                      <Trash2 :size="16" />
                    </button>
                  </div>
                </div>
              </div>

              <!-- Bottom Action Bar -->
              <div class="action-bar">
                <div class="batch-actions">
                  <label class="batch-checkbox">
                    <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" />
                    <span class="checkbox-label">全选</span>
                  </label>
                  <button class="batch-button" @click="batchDelete" :disabled="selectedSegments.length === 0">
                    <Trash2 :size="16" />
                    批量删除
                  </button>
                  <button class="batch-button" @click="batchConfirm" :disabled="selectedSegments.length === 0">
                    <Check :size="16" />
                    批量确认
                  </button>
                </div>
                <button class="add-segment-button" @click="addSegment">
                  <Plus :size="16" />
                  添加片段
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
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
  Search,
  Pencil,
  Eye,
  Trash2,
  Check,
  Plus
} from 'lucide-vue-next';

const themeStore = useThemeStore();
const notificationStore = useNotificationStore();
const userStore = useUserStore();
const router = useRouter();

// Layout state
const isSidebarCollapsed = ref(false);
const isNotificationsOpen = ref(false);
const isUserMenuOpen = ref(false);

// Layout computed properties
const isDarkTheme = computed(() => themeStore.isDark);
const unreadNotifications = computed(() => notificationStore.unreadCount);
const userName = computed(() => userStore.userInfo?.username || '教师');
const userEmail = computed(() => userStore.userInfo?.email || 'teacher@example.com');
const userNameInitial = computed(() => {
  const name = userName.value;
  return name.charAt(0).toUpperCase();
});

// Layout methods
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

// Page state
const selectedCourse = ref('机器学习基础');
const selectedVideo = ref('第1讲：机器学习简介');
const searchQuery = ref('');
const selectAll = ref(false);
const selectedSegments = ref<number[]>([]);

// Segments data
const segments = ref([
  {
    id: 1,
    title: '什么是机器学习',
    startTime: 60,
    endTime: 180,
    startPosition: 10,
    endPosition: 30,
    content: '机器学习是人工智能的一个分支，通过算法让计算机从数据中学习，而不需要显式编程。',
    tags: ['机器学习', '人工智能', '定义'],
    status: 'confirmed'
  },
  {
    id: 2,
    title: '监督学习与无监督学习',
    startTime: 180,
    endTime: 300,
    startPosition: 30,
    endPosition: 50,
    content: '监督学习使用标记数据进行训练，无监督学习从无标记数据中发现模式。',
    tags: ['监督学习', '无监督学习', '学习方法'],
    status: 'pending'
  },
  {
    id: 3,
    title: '线性回归',
    startTime: 300,
    endTime: 420,
    startPosition: 50,
    endPosition: 70,
    content: '线性回归是一种用于预测连续值的监督学习算法。',
    tags: ['线性回归', '监督学习', '算法'],
    status: 'rejected'
  },
  {
    id: 4,
    title: '模型评估',
    startTime: 420,
    endTime: 540,
    startPosition: 70,
    endPosition: 90,
    content: '模型评估是衡量机器学习模型性能的过程，常用指标包括准确率、精确率、召回率等。',
    tags: ['模型评估', '性能指标', '机器学习'],
    status: 'confirmed'
  }
]);

// Computed
const filteredSegments = computed(() => {
  if (!searchQuery.value) return segments.value;
  const query = searchQuery.value.toLowerCase();
  return segments.value.filter(segment =>
    segment.title.toLowerCase().includes(query) ||
    segment.content.toLowerCase().includes(query) ||
    segment.tags.some(tag => tag.toLowerCase().includes(query))
  );
});

// Methods
const formatTime = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = Math.floor(seconds % 60);
  return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
};

const getStatusText = (status: string): string => {
  switch (status) {
    case 'confirmed': return '已确认';
    case 'pending': return '待审核';
    case 'rejected': return '需修改';
    default: return '未知';
  }
};

const toggleSelectAll = () => {
  if (selectAll.value) {
    selectedSegments.value = segments.value.map((_, index) => index);
  } else {
    selectedSegments.value = [];
  }
};

const addSegment = () => {
  // 简单实现，实际项目中可使用弹窗
  segments.value.push({
    id: Date.now(),
    title: '新片段',
    startTime: 0,
    endTime: 60,
    startPosition: 0,
    endPosition: 10,
    content: '片段内容',
    tags: [],
    status: 'pending'
  });
};

const editSegment = (index: number) => {
  // 简单实现，实际项目中可使用弹窗
  console.log('编辑片段:', segments.value[index]);
};

const previewSegment = (index: number) => {
  // 简单实现，实际项目中可跳转到视频对应时间点
  console.log('预览片段:', segments.value[index]);
};

const deleteSegment = (index: number) => {
  if (confirm('确定要删除这个片段吗？')) {
    segments.value.splice(index, 1);
    selectedSegments.value = selectedSegments.value.filter(i => i !== index);
  }
};

const batchDelete = () => {
  if (selectedSegments.value.length === 0) return;
  if (confirm(`确定要删除选中的 ${selectedSegments.value.length} 个片段吗？`)) {
    // 按索引从大到小删除，避免索引偏移
    selectedSegments.value.sort((a, b) => b - a).forEach(index => {
      segments.value.splice(index, 1);
    });
    selectedSegments.value = [];
    selectAll.value = false;
  }
};

const batchConfirm = () => {
  if (selectedSegments.value.length === 0) return;
  selectedSegments.value.forEach(index => {
    segments.value[index].status = 'confirmed';
  });
  selectedSegments.value = [];
  selectAll.value = false;
};
</script>

<style scoped>
/* Layout - Teacher Home */
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

/* Knowledge Graph Page Styles */
.knowledge-graph {
  min-height: calc(100vh - 64px);
  background: var(--color-bg-canvas, #fdfdf8);
  color: var(--color-text-primary, #0f172a);
}

/* Filter Bar */
.filter-bar {
  background: var(--color-bg-elevated, #eeefe9);
  padding: 16px 0;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
}

.container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 24px;
}

/* Filter Controls */
.filter-controls {
  margin-bottom: 16px;
}

.filter-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #0f172a);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-select:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 8px 12px 8px 32px;
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #0f172a);
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: var(--color-border-focus, #6366f1);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
}

.search-box svg {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary, #94a3b8);
}

/* Main Content */
.content-container {
  padding: 24px 0;
}

.content-container .container {
  display: grid;
  grid-template-columns: 40% 60%;
  gap: 24px;
}

/* Left Column */
.left-column {
  position: sticky;
  top: 80px;
  align-self: flex-start;
}

.video-preview {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  overflow: hidden;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.video-wrapper {
  position: relative;
  padding-bottom: 56.25%; /* 16:9 */
}

.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
}

.timeline-overlay {
  position: relative;
  height: 8px;
  background: var(--color-bg-input, #f1f5f9);
  border-radius: var(--radius-full, 9999px);
  margin: 16px;
}

.timeline-segment {
  position: absolute;
  height: 100%;
  border-radius: var(--radius-full, 9999px);
  transition: all 0.2s ease;
  cursor: pointer;
}

.timeline-segment.confirmed {
  background: #008b00;
}

.timeline-segment.pending {
  background: #F7A501;
}

.timeline-segment.rejected {
  background: #fb565b;
}

.timeline-segment:hover {
  opacity: 0.8;
  transform: scaleY(1.2);
}

/* Right Column */
.right-column {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.segment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.segment-card {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  padding: 16px;
  transition: all 0.2s ease;
  cursor: pointer;
}

.segment-card:hover {
  background: var(--color-bg-hover, #f1f5f9);
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  transform: translateY(-1px);
}

.segment-card:hover .segment-title {
  color: #F54E00;
}

.segment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.segment-title {
  font-size: 1.333rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
  font-family: 'IBM Plex Sans', sans-serif;
  transition: color 0.2s ease;
}

.segment-time {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  font-family: 'Source Code Pro', monospace;
  white-space: nowrap;
}

.segment-content {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  line-height: 1.5;
  margin: 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.segment-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin: 12px 0;
}

.tag {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  padding: 4px 8px;
  border-radius: var(--radius-full, 9999px);
  font-size: 0.75rem;
  font-weight: 600;
}

.segment-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  justify-content: flex-end;
}

.action-button {
  background: none;
  border: none;
  color: #1e1f23;
  cursor: pointer;
  padding: 6px;
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

.action-button.delete:hover {
  color: var(--color-danger, #ef4444);
}

/* Action Bar */
.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.batch-checkbox {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 0.875rem;
  color: var(--color-text-primary, #0f172a);
}

.batch-checkbox input[type="checkbox"] {
  accent-color: var(--color-accent, #6366f1);
}

.batch-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: var(--radius-sm, 4px);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.batch-button:hover:not(:disabled) {
  background: #333;
  transform: translateY(-1px);
}

.batch-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.add-segment-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: var(--radius-sm, 4px);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.add-segment-button:hover {
  background: #333;
  transform: translateY(-1px);
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

  .content-container .container {
    grid-template-columns: 35% 65%;
  }
}

@media (max-width: 767px) {
  .content-container .container {
    grid-template-columns: 1fr;
  }

  .left-column {
    position: static;
  }

  .filter-group {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    max-width: none;
  }

  .action-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .batch-actions {
    justify-content: center;
  }

  .add-segment-button {
    justify-content: center;
  }
}
</style>
