import { ref, watchEffect } from 'vue'

const STORAGE_KEY = 'zhike-theme'

type ThemeValue = 'student-dark' | 'student-light' | 'teacher-light'

export function useTheme() {
  const currentTheme = ref<ThemeValue>('student-dark')

  /**
   * 初始化主题
   * @param roleCode 用户角色代码
   */
  function initTheme(roleCode: string) {
    // 1. 优先读取用户手动设置
    const saved = localStorage.getItem(STORAGE_KEY) as ThemeValue | null
    if (saved && isValidTheme(saved)) {
      currentTheme.value = saved
      applyTheme()
      return
    }

    // 2. 根据角色设置默认主题
    if (roleCode === 'STUDENT') {
      // 检测系统偏好
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      currentTheme.value = prefersDark ? 'student-dark' : 'student-light'
    } else {
      currentTheme.value = 'teacher-light'
    }
    applyTheme()
  }

  /** 验证主题值合法性 */
  function isValidTheme(value: string): value is ThemeValue {
    return ['student-dark', 'student-light', 'teacher-light'].includes(value)
  }

  /** 应用主题到 DOM */
  function applyTheme() {
    const root = document.documentElement
    root.setAttribute('data-theme', currentTheme.value)
    // 同步 color-scheme 属性，让原生控件跟随主题
    root.style.colorScheme = currentTheme.value === 'student-dark' ? 'dark' : 'light'
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

  /** 是否为暗色主题 */
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
