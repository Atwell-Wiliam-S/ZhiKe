<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterView } from 'vue-router'
import { useTheme } from './composables/useTheme'
import NotificationCenter from './components/NotificationCenter.vue'

const { initTheme } = useTheme()

// 模拟登录后获取角色
onMounted(() => {
  // 这里模拟学生角色，实际应从登录接口获取
  initTheme('STUDENT')
})
</script>

<template>
  <RouterView />
  <NotificationCenter />
</template>
