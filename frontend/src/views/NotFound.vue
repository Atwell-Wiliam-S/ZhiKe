<template>
  <div class="not-found">
    <div class="not-found-content">
      <div class="not-found-code">404</div>
      <h1 class="not-found-title">页面未找到</h1>
      <p class="not-found-description">抱歉，您访问的页面不存在或已被删除。</p>
      <button class="not-found-button" @click="goHome">返回首页</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/login');
};
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-canvas, #f8fafc);
  color: var(--color-text-primary, #0f172a);
}

.not-found-content {
  text-align: center;
  max-width: 400px;
  padding: 2rem;
  background: var(--color-bg-card, #ffffff);
  border-radius: var(--radius-lg, 12px);
  box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
}

.not-found-code {
  font-size: 6rem;
  font-weight: 700;
  color: var(--color-accent, #6366f1);
  margin-bottom: 1rem;
  font-family: 'IBM Plex Sans', sans-serif;
}

.not-found-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: var(--color-text-primary, #0f172a);
}

.not-found-description {
  font-size: 1rem;
  color: var(--color-text-secondary, #64748b);
  margin-bottom: 2rem;
  line-height: 1.5;
}

.not-found-button {
  background: var(--color-accent, #6366f1);
  color: white;
  border: none;
  border-radius: var(--radius-md, 8px);
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.not-found-button:hover {
  background: var(--color-accent-hover, #4f46e5);
  transform: translateY(-1px);
}

/* Dark Theme Support */
@media (prefers-color-scheme: dark) {
  :root {
    --color-bg-canvas: #0f172a;
    --color-bg-card: #1e293b;
    --color-text-primary: #f8fafc;
    --color-text-secondary: #cbd5e1;
    --color-accent: #6366f1;
    --color-accent-hover: #4f46e5;
    --radius-md: 8px;
    --radius-lg: 12px;
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.3);
  }
}

/* Light Theme */
@media (prefers-color-scheme: light) {
  :root {
    --color-bg-canvas: #f8fafc;
    --color-bg-card: #ffffff;
    --color-text-primary: #0f172a;
    --color-text-secondary: #64748b;
    --color-accent: #0075de;
    --color-accent-hover: #0056b3;
    --radius-md: 8px;
    --radius-lg: 12px;
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  }
}
</style>