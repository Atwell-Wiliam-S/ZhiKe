<script setup lang="ts">
import { Sun, Moon } from 'lucide-vue-next'
import { useTheme } from '@/composables/useTheme'

const { isDark, toggleTheme, currentTheme } = useTheme()
</script>

<template>
  <button
    v-if="currentTheme !== 'teacher-light'"
    @click="toggleTheme"
    :aria-label="isDark ? '切换到亮色主题' : '切换到暗色主题'"
    class="theme-toggle"
  >
    <Sun v-if="isDark" :size="18" :stroke-width="1.5" />
    <Moon v-else :size="18" :stroke-width="1.5" />
  </button>
</template>

<style scoped>
.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: var(--radius-full);
  border: 1px solid var(--color-border);
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
}
.theme-toggle:hover {
  color: var(--color-accent);
  border-color: var(--color-accent);
}
</style>
