import { ref, watchEffect } from 'vue'

type Theme = 'student-dark' | 'student-light' | 'teacher-light'

const STORAGE_KEY = 'zhike-theme'

const currentTheme = ref<Theme>('student-dark')

export function useTheme() {
  /** 初始化主题 */
  function initTheme(roleCode: string) {
    if (roleCode === 'STUDENT') {
      const saved = localStorage.getItem(STORAGE_KEY) as Theme | null
      if (saved && (saved === 'student-dark' || saved === 'student-light')) {
        currentTheme.value = saved
      } else {
        // 跟随系统偏好
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
        currentTheme.value = prefersDark ? 'student-dark' : 'student-light'
      }
    } else {
      // 教师/管理员固定浅色
      currentTheme.value = 'teacher-light'
    }
    applyTheme()
  }

  /** 应用主题到 DOM */
  function applyTheme() {
    document.documentElement.setAttribute('data-theme', currentTheme.value)
  }

  /** 切换学生端暗色/亮色 */
  function toggleTheme() {
    if (currentTheme.value === 'student-dark') {
      currentTheme.value = 'student-light'
    } else if (currentTheme.value === 'student-light') {
      currentTheme.value = 'student-dark'
    } else {
      return // 教师端不可切换
    }
    localStorage.setItem(STORAGE_KEY, currentTheme.value)
    applyTheme()
  }

  /** 是否为暗色 */
  const isDark = ref(true)

  watchEffect(() => {
    isDark.value = currentTheme.value === 'student-dark'
  })

  // 监听系统主题变化（仅学生端未手动设置时）
  if (typeof window !== 'undefined') {
    window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', (e) => {
        if (!localStorage.getItem(STORAGE_KEY)) {
          currentTheme.value = e.matches ? 'student-dark' : 'student-light'
          applyTheme()
        }
      })
  }

  return { currentTheme, isDark, initTheme, toggleTheme }
}
