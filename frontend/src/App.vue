<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useTheme } from './composables/useTheme'
import ThemeToggle from './components/ThemeToggle.vue'
import Button from './components/ui/Button.vue'
import Card from './components/ui/Card.vue'
import Input from './components/ui/Input.vue'

const { initTheme } = useTheme()

// 模拟登录后获取角色
onMounted(() => {
  // 这里模拟学生角色，实际应从登录接口获取
  initTheme('STUDENT')
})

const inputValue = ref('')
</script>

<template>
  <div class="app">
    <header class="app__header">
      <h1 class="app__title">智能教学闭环系统</h1>
      <div class="app__header-actions">
        <ThemeToggle />
      </div>
    </header>
    
    <main class="app__main">
      <section class="app__section">
        <h2 class="app__section-title">设计系统演示</h2>
        
        <div class="app__cards">
          <Card title="按钮示例" elevated>
            <div class="app__buttons">
              <Button>默认按钮</Button>
              <Button variant="primary">主要按钮</Button>
              <Button variant="secondary">次要按钮</Button>
              <Button variant="pill">Pill按钮</Button>
            </div>
          </Card>
          
          <Card title="输入框示例">
            <Input v-model="inputValue" placeholder="请输入内容" label="示例输入" />
            <div class="app__input-value">
              输入值: {{ inputValue }}
            </div>
          </Card>
          
          <Card title="主题切换">
            <p>点击右上角的主题切换按钮可以在暗色和亮色主题间切换</p>
            <p>主题偏好会自动保存到本地存储</p>
          </Card>
        </div>
      </section>
    </main>
    
    <footer class="app__footer">
      <p>© 2026 智能教学闭环系统</p>
    </footer>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-6) var(--space-8);
  border-bottom: 1px solid var(--color-border);
  background: var(--color-bg-elevated);
}

.app__title {
  font-size: var(--text-h2);
  font-weight: 700;
  color: var(--color-text-primary);
  margin: 0;
}

.app__header-actions {
  display: flex;
  gap: var(--space-4);
}

.app__main {
  flex: 1;
  padding: var(--space-8);
}

.app__section {
  max-width: var(--container-lg);
  margin: 0 auto;
}

.app__section-title {
  font-size: var(--text-h1);
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--space-6);
}

.app__cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--space-6);
}

.app__buttons {
  display: flex;
  gap: var(--space-4);
  flex-wrap: wrap;
}

.app__input-value {
  margin-top: var(--space-4);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.app__footer {
  padding: var(--space-6) var(--space-8);
  border-top: 1px solid var(--color-border);
  text-align: center;
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  background: var(--color-bg-elevated);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app__header {
    padding: var(--space-4) var(--space-4);
  }
  
  .app__main {
    padding: var(--space-4);
  }
  
  .app__buttons {
    flex-direction: column;
  }
  
  .app__cards {
    grid-template-columns: 1fr;
  }
}
</style>
