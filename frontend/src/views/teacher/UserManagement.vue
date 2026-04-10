<template>
  <div class="user-management">
    <!-- Left Sidebar -->
    <aside class="sidebar" :class="{ 'collapsed': isSidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <GraduationCap :size="24" class="logo-icon" />
          <h1 v-if="!isSidebarCollapsed">ZhiKe</h1>
          <div class="logo-dot" v-if="!isSidebarCollapsed"></div>
        </div>
        <div class="role-badge" v-if="!isSidebarCollapsed">
          管理员
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
          <li class="nav-item" :class="{ active: $route.path === '/admin/users' }">
            <router-link to="/admin/users" class="nav-link">
              <Users :size="20" />
              <span v-if="!isSidebarCollapsed">用户管理</span>
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
            <h1 class="page-title">用户管理</h1>
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

      <!-- User Management Content -->
      <section class="user-management-content">
        <!-- Top Action Bar -->
        <div class="top-action-bar">
          <h2 class="section-title">用户管理</h2>
          <button class="add-user-button" aria-label="添加用户">
            <UserPlus :size="16" />
            <span>添加用户</span>
          </button>
        </div>

        <!-- Filter Bar -->
        <div class="filter-bar">
          <div class="role-filter" role="tablist" aria-label="角色筛选">
            <button 
              v-for="role in roles" 
              :key="role.value"
              :class="['role-pill', { active: selectedRole === role.value }]"
              :aria-selected="selectedRole === role.value"
              @click="selectedRole = role.value"
            >
              {{ role.label }}
            </button>
          </div>
          <div class="search-container">
            <Search :size="16" class="search-icon" />
            <input 
              type="text" 
              class="search-input" 
              placeholder="搜索用户名或邮箱..."
              v-model="searchQuery"
            >
          </div>
          <div class="status-filter">
            <span class="status-label">启用</span>
            <label class="toggle-switch">
              <input type="checkbox" v-model="statusFilter">
              <span class="toggle-slider"></span>
            </label>
          </div>
        </div>

        <!-- User Table -->
        <div class="user-table-container">
          <table class="user-table" aria-label="用户列表">
            <thead>
              <tr>
                <th>用户名</th>
                <th>邮箱</th>
                <th>角色</th>
                <th>状态</th>
                <th>注册时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in filteredUsers" :key="user.id" class="user-row">
                <td class="user-name">{{ user.username }}</td>
                <td>{{ user.email }}</td>
                <td>
                  <span class="role-badge" :class="user.role.toLowerCase()">
                    {{ user.role }}
                  </span>
                </td>
                <td>
                  <label class="toggle-switch">
                    <input type="checkbox" v-model="user.enabled">
                    <span class="toggle-slider"></span>
                  </label>
                </td>
                <td>{{ user.registeredAt }}</td>
                <td class="user-actions">
                  <button class="action-button" aria-label="编辑用户">
                    <Pencil :size="16" />
                  </button>
                  <button class="action-button delete" @click="showDeleteConfirm(user.id)" aria-label="删除用户">
                    <Trash2 :size="16" />
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="pagination" aria-label="用户列表分页">
          <div class="pagination-info">
            显示 {{ (currentPage - 1) * pageSize + 1 }}-{{ Math.min(currentPage * pageSize, filteredUsers.length) }} 条，共 {{ filteredUsers.length }} 条
          </div>
          <div class="pagination-controls">
            <button class="pagination-button" :disabled="currentPage === 1" @click="currentPage--">
              <ChevronLeft :size="16" />
            </button>
            <button class="pagination-button active">{{ currentPage }}</button>
            <button class="pagination-button" :disabled="currentPage * pageSize >= filteredUsers.length" @click="currentPage++">
              <ChevronRight :size="16" />
            </button>
            <div class="page-size-selector">
              <span>每页</span>
              <select v-model="pageSize">
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="50">50</option>
              </select>
              <span>条</span>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="modal-overlay" role="alertdialog" aria-labelledby="delete-title" aria-describedby="delete-description">
      <div class="modal-content">
        <h3 id="delete-title">确认删除</h3>
        <p id="delete-description">确定要删除该用户吗？此操作不可撤销。</p>
        <div class="modal-actions">
          <button class="cancel-button" @click="showDeleteModal = false">取消</button>
          <button class="delete-button" @click="confirmDelete">删除</button>
        </div>
      </div>
    </div>
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
  Settings,
  ChevronLeft,
  ChevronRight,
  ChevronDown,
  Sun,
  Moon,
  Bell,
  Users,
  UserPlus,
  Search,
  Pencil,
  Trash2,
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
const pageSize = ref(10);
const selectedRole = ref('all');
const searchQuery = ref('');
const statusFilter = ref(true);
const showDeleteModal = ref(false);
const deleteUserId = ref<number | null>(null);

// Computed properties
const isDarkTheme = computed(() => themeStore.isDark);
const unreadNotifications = computed(() => notificationStore.unreadCount);
const userName = computed(() => userStore.userInfo?.username || '管理员');
const userEmail = computed(() => userStore.userInfo?.email || 'admin@example.com');
const userNameInitial = computed(() => {
  const name = userName.value;
  return name.charAt(0).toUpperCase();
});

// Roles
const roles = [
  { value: 'all', label: '全部' },
  { value: 'STUDENT', label: '学生' },
  { value: 'TEACHER', label: '教师' },
  { value: 'ADMIN', label: '管理员' }
];

// Mock data
const users = ref([
  { id: 1, username: '张三', email: 'zhangsan@example.com', role: 'STUDENT', enabled: true, registeredAt: '2024-01-15 10:30' },
  { id: 2, username: '李四', email: 'lisi@example.com', role: 'TEACHER', enabled: true, registeredAt: '2024-01-10 09:15' },
  { id: 3, username: '王五', email: 'wangwu@example.com', role: 'STUDENT', enabled: false, registeredAt: '2024-01-05 14:20' },
  { id: 4, username: '赵六', email: 'zhaoliu@example.com', role: 'ADMIN', enabled: true, registeredAt: '2024-01-01 08:00' },
  { id: 5, username: '钱七', email: 'qianqi@example.com', role: 'STUDENT', enabled: true, registeredAt: '2023-12-25 16:45' },
  { id: 6, username: '孙八', email: 'sunba@example.com', role: 'TEACHER', enabled: true, registeredAt: '2023-12-20 11:30' },
  { id: 7, username: '周九', email: 'zhoujiu@example.com', role: 'STUDENT', enabled: true, registeredAt: '2023-12-15 13:15' },
  { id: 8, username: '吴十', email: 'wushi@example.com', role: 'STUDENT', enabled: false, registeredAt: '2023-12-10 15:50' },
  { id: 9, username: '郑一', email: 'zhengyi@example.com', role: 'TEACHER', enabled: true, registeredAt: '2023-12-05 10:20' },
  { id: 10, username: '王二', email: 'wanger@example.com', role: 'STUDENT', enabled: true, registeredAt: '2023-12-01 09:45' },
  { id: 11, username: '刘三', email: 'liusan@example.com', role: 'STUDENT', enabled: true, registeredAt: '2023-11-25 14:30' },
  { id: 12, username: '陈四', email: 'chensi@example.com', role: 'STUDENT', enabled: true, registeredAt: '2023-11-20 11:15' }
]);

// Filtered users
const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const roleMatch = selectedRole.value === 'all' || user.role === selectedRole.value;
    const searchMatch = !searchQuery.value || 
      user.username.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.value.toLowerCase());
    const statusMatch = !statusFilter.value || user.enabled;
    return roleMatch && searchMatch && statusMatch;
  });
});

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

const showDeleteConfirm = (userId: number) => {
  deleteUserId.value = userId;
  showDeleteModal.value = true;
};

const confirmDelete = () => {
  if (deleteUserId.value) {
    const index = users.value.findIndex(user => user.id === deleteUserId.value);
    if (index !== -1) {
      users.value.splice(index, 1);
    }
  }
  showDeleteModal.value = false;
  deleteUserId.value = null;
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
.user-management {
  display: flex;
  min-height: 100vh;
  background: var(--color-bg-canvas, #fdfdf8);
  color: var(--color-text-primary, #4d4f46);
}

/* Sidebar */
.sidebar {
  width: 240px;
  background: var(--color-bg-canvas, #fdfdf8);
  border-right: 1px solid var(--color-border, #bfc1b7);
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
  border-bottom: 1px solid var(--color-border, #bfc1b7);
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
  color: var(--color-accent, #F54E00);
  flex-shrink: 0;
}

.logo h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #4d4f46);
  font-family: 'IBM Plex Sans', sans-serif;
}

.logo-dot {
  width: 8px;
  height: 8px;
  background: var(--color-accent, #F54E00);
  border-radius: 50%;
  margin-left: 0.5rem;
}

.role-badge {
  background: var(--color-accent-subtle, rgba(245, 78, 0, 0.08));
  color: var(--color-accent, #F54E00);
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
  color: var(--color-text-secondary, #65675e);
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  margin: 0 8px;
  position: relative;
}

.nav-link:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
}

.nav-item.active .nav-link {
  background: var(--color-accent-subtle, rgba(245, 78, 0, 0.08));
  color: var(--color-text-primary, #4d4f46);
  border-left: 3px solid var(--color-accent, #F54E00);
  margin-left: 0;
  padding-left: 13px;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid var(--color-border, #bfc1b7);
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #65675e);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.collapse-button:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
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
  border-bottom: 1px solid var(--color-border, #bfc1b7);
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
  color: var(--color-text-primary, #4d4f46);
  font-family: 'IBM Plex Sans', sans-serif;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #65675e);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
}

.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  background: var(--color-danger, #dc2626);
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
  background: var(--color-bg-elevated, #eeefe9);
  border: 1px solid var(--color-border, #bfc1b7);
  cursor: pointer;
  transition: all 0.2s ease;
}

.nav-user-trigger:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
}

.nav-user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-accent, #F54E00);
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
  color: var(--color-text-primary, #4d4f46);
}

.nav-user-arrow {
  color: var(--color-text-tertiary, #9ea096);
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
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-lg, 8px);
  box-shadow: var(--shadow-lg, 0 25px 50px -12px rgba(0,0,0,0.25));
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
  background: var(--color-accent, #F54E00);
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
  color: var(--color-text-primary, #4d4f46);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-email {
  font-size: 0.75rem;
  color: var(--color-text-tertiary, #9ea096);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-divider {
  height: 1px;
  background: var(--color-border, #bfc1b7);
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 16px;
  border: none;
  background: none;
  color: var(--color-text-primary, #4d4f46);
  font-size: 0.875rem;
  cursor: pointer;
  transition: background 0.15s ease;
  text-decoration: none;
}

.dropdown-item:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
}

.dropdown-item-danger {
  color: var(--color-danger, #dc2626);
}

.dropdown-item-danger:hover {
  background: var(--color-danger-bg, #fef2f2);
}

/* User Management Content */
.user-management-content {
  padding: 24px;
  background: var(--color-bg-canvas, #fdfdf8);
}

/* Top Action Bar */
.top-action-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-border, #bfc1b7);
}

.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #4d4f46);
  font-family: 'IBM Plex Sans', sans-serif;
}

.add-user-button {
  background: var(--color-cta-bg, #1e1f23);
  color: var(--color-cta-text, #ffffff);
  border: none;
  border-radius: var(--radius-sm, 4px);
  padding: 8px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: 'IBM Plex Sans', sans-serif;
}

.add-user-button:hover {
  background: #333438;
  transform: translateY(-1px);
}

/* Filter Bar */
.filter-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--color-bg-elevated, #eeefe9);
  padding: 16px;
  border-radius: var(--radius-sm, 4px);
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.role-filter {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.role-pill {
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-secondary, #65675e);
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-full, 9999px);
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: 'IBM Plex Sans', sans-serif;
}

.role-pill:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
}

.role-pill.active {
  background: var(--color-accent, #F54E00);
  color: white;
  border-color: var(--color-accent, #F54E00);
}

.search-container {
  position: relative;
  flex: 1;
  min-width: 200px;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary, #9ea096);
}

.search-input {
  width: 100%;
  padding: 8px 12px 8px 36px;
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #4d4f46);
  font-size: 0.875rem;
  font-family: 'IBM Plex Sans', sans-serif;
}

.search-input:focus {
  outline: none;
  border-color: var(--color-border-focus, #4d4f46);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(245, 78, 0, 0.08));
}

.status-filter {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-label {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #65675e);
  font-family: 'IBM Plex Sans', sans-serif;
}

/* Toggle Switch */
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
  background-color: var(--color-border, #bfc1b7);
  transition: .4s;
  border-radius: 24px;
}

.toggle-slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .toggle-slider {
  background-color: var(--color-accent, #F54E00);
}

input:checked + .toggle-slider:before {
  transform: translateX(24px);
}

/* User Table */
.user-table-container {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  overflow: hidden;
  margin-bottom: 20px;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  font-family: 'IBM Plex Sans', sans-serif;
}

.user-table th,
.user-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid var(--color-border, #bfc1b7);
}

.user-table th {
  background: var(--color-bg-elevated, #eeefe9);
  font-weight: 600;
  font-size: 0.875rem;
  color: var(--color-text-secondary, #65675e);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.user-row {
  transition: all 0.2s ease;
  cursor: pointer;
}

.user-row:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
}

.user-row:hover .user-name {
  color: var(--color-accent, #F54E00);
}

.user-name {
  font-weight: 600;
  color: var(--color-text-primary, #4d4f46);
  transition: all 0.2s ease;
}

.role-badge {
  font-size: 0.75rem;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: var(--radius-full, 9999px);
  font-family: 'IBM Plex Sans', sans-serif;
}

.role-badge.student {
  background: var(--color-info-bg, #eff6ff);
  color: var(--color-info, #2563eb);
}

.role-badge.teacher {
  background: var(--color-success-bg, #f0fdf4);
  color: var(--color-success, #16a34a);
}

.role-badge.admin {
  background: var(--color-warning-bg, #fffbeb);
  color: var(--color-warning, #d97706);
}

.user-actions {
  display: flex;
  gap: 8px;
}

.action-button {
  background: none;
  border: none;
  color: var(--color-text-tertiary, #9ea096);
  cursor: pointer;
  padding: 4px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-button:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
}

.action-button.delete:hover {
  color: var(--color-danger, #dc2626);
  background: var(--color-danger-bg, #fef2f2);
}

/* Pagination */
.pagination {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  padding: 16px;
  flex-wrap: wrap;
  gap: 16px;
}

.pagination-info {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #65675e);
  font-family: 'IBM Plex Sans', sans-serif;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.pagination-button {
  background: none;
  border: 1px solid var(--color-border, #bfc1b7);
  color: var(--color-text-secondary, #65675e);
  cursor: pointer;
  padding: 6px 12px;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'IBM Plex Sans', sans-serif;
}

.pagination-button:hover:not(:disabled) {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
}

.pagination-button.active {
  background: var(--color-accent, #F54E00);
  color: white;
  border-color: var(--color-accent, #F54E00);
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  color: var(--color-text-secondary, #65675e);
  font-family: 'IBM Plex Sans', sans-serif;
}

.page-size-selector select {
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  padding: 4px 8px;
  background: var(--color-bg-card, #ffffff);
  color: var(--color-text-primary, #4d4f46);
  font-size: 0.875rem;
  font-family: 'IBM Plex Sans', sans-serif;
}

/* Modal */
.modal-overlay {
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

.modal-content {
  background: var(--color-bg-card, #ffffff);
  border-radius: var(--radius-sm, 4px);
  padding: 24px;
  max-width: 400px;
  width: 90%;
  box-shadow: var(--shadow-lg, 0 25px 50px -12px rgba(0,0,0,0.25));
  font-family: 'IBM Plex Sans', sans-serif;
}

.modal-content h3 {
  margin-top: 0;
  color: var(--color-text-primary, #4d4f46);
  font-size: 1.125rem;
  font-weight: 600;
}

.modal-content p {
  color: var(--color-text-secondary, #65675e);
  margin-bottom: 24px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-button {
  background: none;
  border: 1px solid var(--color-border, #bfc1b7);
  color: var(--color-text-secondary, #65675e);
  padding: 8px 16px;
  border-radius: var(--radius-sm, 4px);
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: 'IBM Plex Sans', sans-serif;
}

.cancel-button:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
  color: var(--color-text-primary, #4d4f46);
}

.delete-button {
  background: var(--color-danger, #dc2626);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: var(--radius-sm, 4px);
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: 'IBM Plex Sans', sans-serif;
}

.delete-button:hover {
  background: #b91c1c;
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
  
  .filter-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
  
  .role-filter {
    justify-content: center;
  }
  
  .search-container {
    width: 100%;
  }
  
  .user-table {
    font-size: 0.875rem;
  }
  
  .user-table th,
  .user-table td {
    padding: 10px 12px;
  }
  
  .top-action-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .add-user-button {
    align-self: flex-end;
  }
}

@media (max-width: 767px) {
  .user-management-content {
    padding: 16px;
  }
  
  .filter-bar {
    padding: 12px;
  }
  
  .user-table-container {
    overflow-x: auto;
  }
  
  .user-table {
    min-width: 600px;
  }
  
  .pagination {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .pagination-controls {
    align-self: flex-end;
  }
  
  .add-user-button {
    align-self: stretch;
    justify-content: center;
  }
}
</style>