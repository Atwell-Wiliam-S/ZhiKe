<script setup lang="ts">
import { useRouter } from 'vue-router'
import { GraduationCap } from 'lucide-vue-next'

const router = useRouter()
</script>

<template>
  <div class="teacher-home-container">
    <div class="placeholder-content">
      <GraduationCap :size="64" />
      <h1>教师首页</h1>
      <p>页面开发中...</p>
    </div>
  </div>
</template>

<style scoped>
.teacher-home-container {
  min-height: 100vh;
  background-color: var(--color-bg-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-content {
  text-align: center;
  color: var(--color-text-primary);
}

.placeholder-content h1 {
  margin: var(--spacing-lg) 0;
  font-size: var(--font-size-2xl);
}

.placeholder-content p {
  color: var(--color-text-secondary);
}
</style>
