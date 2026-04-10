<script setup lang="ts">
interface Props {
  title?: string
  elevated?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  elevated: false
})
</script>

<template>
  <div class="card" :class="{ 'card--elevated': elevated }">
    <div v-if="title" class="card__header">
      <h3 class="card__title">{{ title }}</h3>
    </div>
    <div class="card__body">
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.card {
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: all var(--duration-normal) var(--ease-default);
}

.card:hover {
  border-color: var(--color-accent);
}

.card--elevated {
  box-shadow: var(--shadow-md);
}

.card__header {
  padding: var(--space-5) var(--space-6) var(--space-4);
  border-bottom: 1px solid var(--color-border);
}

.card__title {
  font-size: var(--text-h3);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.card__body {
  padding: var(--space-6);
}
</style>
