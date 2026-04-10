import { defineStore } from 'pinia'
import { useTheme as useThemeComposable } from '@/composables/useTheme'

export const useThemeStore = defineStore('theme', () => {
  const { currentTheme, isDark, initTheme, toggleTheme } = useThemeComposable()

  return {
    currentTheme,
    isDark,
    initTheme,
    toggleTheme,
  }
})
