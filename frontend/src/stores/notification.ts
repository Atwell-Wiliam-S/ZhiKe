import { defineStore } from 'pinia'
import { useNotification as useNotificationComposable } from '@/composables/useNotification'

export const useNotificationStore = defineStore('notification', () => {
  const {
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
  } = useNotificationComposable()

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
})
