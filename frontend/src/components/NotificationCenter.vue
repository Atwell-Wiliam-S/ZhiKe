<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { Info, BookOpen, MessageCircle, X } from 'lucide-vue-next'
import { useNotificationStore } from '@/stores/notification'
import type { Notification } from '@/types/api'

const notificationStore = useNotificationStore()

const { 
  notifications, 
  activeTab, 
  isDrawerOpen, 
  unreadCount, 
  filteredNotifications, 
  closeDrawer, 
  markAsRead, 
  markAllAsRead 
} = notificationStore

// Mock 数据
const mockNotifications: Notification[] = [
  {
    id: 1,
    type: 'system',
    title: '系统公告',
    description: '智能教学系统已完成升级，新增了视频笔记功能和AI助教问答系统',
    isRead: false,
    createdAt: '2024-01-15 10:30'
  },
  {
    id: 2,
    type: 'course',
    title: '课程更新',
    description: '《高等数学》课程已更新第12章内容，包含最新的微分方程解法',
    isRead: false,
    createdAt: '2024-01-14 16:45'
  },
  {
    id: 3,
    type: 'interaction',
    title: 'AI助教回复',
    description: '您的问题"如何理解微积分基本定理"已得到AI助教的详细解答',
    isRead: true,
    createdAt: '2024-01-14 09:20'
  },
  {
    id: 4,
    type: 'system',
    title: '账户安全提醒',
    description: '您的账户已成功绑定邮箱，现在可以通过邮箱重置密码',
    isRead: true,
    createdAt: '2024-01-13 14:15'
  },
  {
    id: 5,
    type: 'course',
    title: '学习进度',
    description: '您已完成《线性代数》课程的80%，继续加油！',
    isRead: false,
    createdAt: '2024-01-13 11:00'
  }
]

// 初始化 Mock 数据
onMounted(() => {
  if (notifications.length === 0) {
    mockNotifications.forEach(notification => {
      notificationStore.pushNotification(notification)
    })
  }
  document.addEventListener('keydown', handleKeyDown)
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyDown)
})

// 处理键盘事件
function handleKeyDown(event: KeyboardEvent) {
  if (event.key === 'Escape' && isDrawerOpen) {
    closeDrawer()
  }
}

// 获取通知图标
function getNotificationIcon(type: string) {
  switch (type) {
    case 'system':
      return Info
    case 'course':
      return BookOpen
    case 'interaction':
      return MessageCircle
    default:
      return Info
  }
}

// 获取未读计数
function getUnreadCountByType(type: string) {
  if (type === 'all') {
    return unreadCount
  }
  return notifications.filter((n: Notification) => n.type === type && !n.isRead).length
}

// 处理通知点击
function handleNotificationClick(notification: Notification) {
  if (!notification.isRead) {
    markAsRead(notification.id)
  }
  // 跳转到对应页面的逻辑
  if (notification.link) {
    // router.push(notification.link)
  }
}
</script>

<template>
  <div v-if="isDrawerOpen" class="notification-drawer-overlay" @click="closeDrawer">
    <div class="notification-drawer" @click.stop>
      <!-- 面板头部 -->
      <div class="drawer-header">
        <h2 class="drawer-title">通知</h2>
        <div class="drawer-actions">
          <button 
            v-if="unreadCount > 0" 
            class="mark-all-read-btn"
            @click="markAllAsRead"
            aria-label="全部已读"
          >
            全部已读
          </button>
          <button 
            class="close-btn"
            @click="closeDrawer"
            aria-label="关闭通知中心"
          >
            <X :size="20" :stroke-width="1.5" />
          </button>
        </div>
      </div>

      <!-- 通知分类 Tab -->
      <div class="notification-tabs" role="tablist" aria-label="通知分类">
        <button
          v-for="tab in ['all', 'system', 'course', 'interaction']"
          :key="tab"
          class="tab-btn"
          :class="{ active: activeTab === tab }"
          :aria-selected="activeTab === tab"
          role="tab"
          @click="activeTab = tab as any"
        >
          {{ tab === 'all' ? '全部' : tab === 'system' ? '系统' : tab === 'course' ? '课程' : '互动' }}
          <span v-if="getUnreadCountByType(tab) > 0" class="unread-badge">
            {{ getUnreadCountByType(tab) }}
          </span>
        </button>
      </div>

      <!-- 通知列表 -->
      <div 
        class="notification-list" 
        role="list" 
        :aria-label="activeTab === 'all' ? '所有通知列表' : activeTab === 'system' ? '系统通知列表' : activeTab === 'course' ? '课程通知列表' : '互动通知列表'"
      >
        <div v-if="filteredNotifications.length === 0" class="empty-state">
          <div class="empty-icon">
            <MessageCircle :size="48" />
          </div>
          <p class="empty-text">暂无通知</p>
        </div>
        
        <div 
          v-for="notification in filteredNotifications" 
          :key="notification.id"
          class="notification-item"
          :class="{ 'unread': !notification.isRead }"
          role="listitem"
          :aria-unread="!notification.isRead"
          @click="handleNotificationClick(notification)"
        >
          <div class="notification-icon">
            <component 
              :is="getNotificationIcon(notification.type)" 
              :size="24" 
              :stroke-width="1.5" 
            />
          </div>
          <div class="notification-content">
            <div class="notification-header">
              <h3 class="notification-title">{{ notification.title }}</h3>
              <span class="notification-time">{{ notification.createdAt }}</span>
            </div>
            <p class="notification-description">{{ notification.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.notification-drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  justify-content: flex-end;
  animation: fadeIn var(--duration-normal) var(--ease-out);
}

.notification-drawer {
  width: 400px;
  max-width: 100%;
  height: 100vh;
  background: var(--color-bg-card);
  box-shadow: var(--shadow-lg);
  display: flex;
  flex-direction: column;
  animation: slideIn var(--duration-normal) var(--ease-out);
}

@media (max-width: 768px) {
  .notification-drawer {
    width: 100%;
  }
}

.drawer-header {
  padding: var(--space-6);
  border-bottom: 1px solid var(--color-border);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.drawer-title {
  font-size: var(--text-h3);
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
}

.drawer-actions {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.mark-all-read-btn {
  background: transparent;
  border: none;
  color: var(--color-accent);
  font-size: var(--text-sm);
  font-weight: 500;
  cursor: pointer;
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-md);
  transition: all var(--duration-fast) var(--ease-default);
}

.mark-all-read-btn:hover {
  background: var(--color-accent-subtle);
}

.close-btn {
  background: transparent;
  border: none;
  color: var(--color-text-secondary);
  cursor: pointer;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-full);
  transition: all var(--duration-fast) var(--ease-default);
}

.close-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.notification-tabs {
  display: flex;
  padding: var(--space-3);
  gap: var(--space-2);
  border-bottom: 1px solid var(--color-border);
}

.tab-btn {
  background: transparent;
  border: none;
  color: var(--color-text-secondary);
  font-size: var(--text-sm);
  font-weight: 500;
  padding: var(--space-2) var(--space-4);
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--duration-fast) var(--ease-default);
  position: relative;
}

.tab-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.tab-btn.active {
  background: var(--color-accent-subtle);
  color: var(--color-accent);
}

.unread-badge {
  position: absolute;
  top: -2px;
  right: -2px;
  background: var(--color-danger);
  color: white;
  font-size: 10px;
  font-weight: 600;
  padding: 2px 6px;
  border-radius: var(--radius-full);
  min-width: 16px;
  text-align: center;
}

.notification-list {
  flex: 1;
  overflow-y: auto;
  padding: var(--space-3);
}

.notification-item {
  display: flex;
  gap: var(--space-4);
  padding: var(--space-4);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--duration-fast) var(--ease-default);
  border-left: 3px solid transparent;
}

.notification-item:hover {
  background: var(--color-bg-hover);
}

.notification-item.unread {
  border-left-color: var(--color-accent);
  background: var(--color-accent-subtle);
}

.notification-icon {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  background: var(--color-bg-elevated);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: var(--color-accent);
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-1);
}

.notification-title {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
  line-height: 1.4;
}

.notification-time {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-left: var(--space-2);
  white-space: nowrap;
}

.notification-description {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  margin: 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-16) var(--space-6);
  text-align: center;
}

.empty-icon {
  margin-bottom: var(--space-4);
  color: var(--color-text-tertiary);
}

.empty-text {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  margin: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
</style>