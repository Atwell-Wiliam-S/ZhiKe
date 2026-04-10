<template>
  <div class="not-found" role="main">
    <div class="not-found-content">
      <div class="not-found-code">404</div>
      <h1 class="not-found-title">页面未找到</h1>
      <p class="not-found-description">抱歉，您访问的页面不存在或已被移除。</p>
      <div class="not-found-actions">
        <button class="not-found-button primary" @click="goHome" aria-label="返回首页">
          返回首页
        </button>
        <button class="not-found-button secondary" @click="goBack" aria-label="返回上页">
          返回上页
        </button>
      </div>
      <p class="not-found-countdown" aria-live="polite">
        {{ countdown }}s 后自动返回首页
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const countdown = ref(10);
let countdownInterval: number | null = null;

const goHome = () => {
  if (countdownInterval) {
    clearInterval(countdownInterval);
  }
  router.push('/login');
};

const goBack = () => {
  if (countdownInterval) {
    clearInterval(countdownInterval);
  }
  router.back();
};

onMounted(() => {
  countdownInterval = window.setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--;
    } else {
      if (countdownInterval) {
        clearInterval(countdownInterval);
      }
      goHome();
    }
  }, 1000);
});

onUnmounted(() => {
  if (countdownInterval) {
    clearInterval(countdownInterval);
  }
});
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-canvas);
  color: var(--color-text-primary);
  padding: var(--space-6);
}

.not-found-content {
  text-align: center;
  max-width: 480px;
  width: 100%;
}

.not-found-code {
  font-size: 7.5rem;
  font-weight: 800;
  color: var(--color-text-tertiary);
  opacity: 0.3;
  margin-bottom: var(--space-6);
  font-family: system-ui, -apple-system, sans-serif;
  line-height: 1;
}

.not-found-title {
  font-size: var(--text-h2);
  font-weight: 700;
  margin-bottom: var(--space-4);
  color: var(--color-text-primary);
}

.not-found-description {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-8);
  line-height: 1.5;
}

.not-found-actions {
  display: flex;
  gap: var(--space-4);
  justify-content: center;
  margin-bottom: var(--space-6);
  flex-wrap: wrap;
}

.not-found-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0 var(--space-6);
  height: 44px;
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-default);
  border: 1px solid transparent;
}

.not-found-button.primary {
  background: var(--color-accent);
  color: var(--color-accent-text);
}

.not-found-button.primary:hover {
  background: var(--color-accent-hover);
  transform: translateY(-1px);
}

.not-found-button.secondary {
  background: transparent;
  color: var(--color-text-primary);
  border-color: var(--color-border);
}

.not-found-button.secondary:hover {
  border-color: var(--color-accent);
  color: var(--color-accent);
}

.not-found-countdown {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-top: var(--space-4);
}

/* 响应式设计 */
@media (max-width: 767px) {
  .not-found-code {
    font-size: 6rem;
  }
  
  .not-found-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .not-found-button {
    width: 100%;
    max-width: 200px;
  }
}

@media (max-width: 425px) {
  .not-found-code {
    font-size: 4.5rem;
  }
  
  .not-found-title {
    font-size: var(--text-h3);
  }
}
</style>