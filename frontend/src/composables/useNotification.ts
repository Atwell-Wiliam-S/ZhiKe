import { ref, computed } from 'vue'

export interface Notification {
  id: number
  type: 'system' | 'course' | 'interaction'
  title: string
  description: string
  isRead: boolean
  link?: string
  createdAt: string
}

export function useNotification() {
  const notifications = ref<Notification[]>([])
  const activeTab = ref<'all' | 'system' | 'course' | 'interaction'>('all')
  const isDrawerOpen = ref(false)

  /** 未读总数 */
  const unreadCount = computed(() =>
    notifications.value.filter(n => !n.isRead).length
  )

  /** 按分类筛选 */
  const filteredNotifications = computed(() => {
    if (activeTab.value === 'all') return notifications.value
    return notifications.value.filter(n => n.type === activeTab.value)
  })

  function openDrawer() { isDrawerOpen.value = true }
  function closeDrawer() { isDrawerOpen.value = false }

  function markAsRead(id: number) {
    const notification = notifications.value.find(n => n.id === id)
    if (notification) notification.isRead = true
  }

  function markAllAsRead() {
    notifications.value.forEach(n => { n.isRead = true })
  }

  function pushNotification(notification: Notification) {
    notifications.value.unshift(notification)
  }

  return {
    notifications,
    activeTab,
    isDrawerOpen,
    unreadCount,
    filteredNotifications,
    openDrawer,
    closeDrawer,
    markAsRead,
    markAllAsRead,
    pushNotification,
  }
}