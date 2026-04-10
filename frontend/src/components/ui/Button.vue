<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  variant?: 'ghost' | 'primary' | 'secondary' | 'pill'
  size?: 'sm' | 'md' | 'lg'
  disabled?: boolean
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'ghost',
  size: 'md',
  disabled: false,
  loading: false
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
}>()

const buttonClasses = computed(() => {
  return [
    'button',
    `button--${props.variant}`,
    `button--${props.size}`,
    { 'button--disabled': props.disabled },
    { 'button--loading': props.loading }
  ]
})
</script>

<template>
  <button
    :class="buttonClasses"
    :disabled="disabled || loading"
    @click="emit('click', $event)"
  >
    <slot></slot>
  </button>
</template>

<style scoped>
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-weight: 500;
  transition: all var(--duration-normal) var(--ease-default);
  cursor: pointer;
}

.button:hover:not(:disabled) {
  border-color: var(--color-accent);
}

.button:active:not(:disabled) {
  transform: scale(0.98);
  transition: transform var(--duration-fast) var(--ease-default);
}

.button:focus-visible {
  box-shadow: 0 0 0 2px var(--color-bg-canvas), 0 0 0 4px var(--color-accent);
}

.button--disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Variants */
.button--ghost {
  background: transparent;
  color: var(--color-text-primary);
}

.button--primary {
  background: var(--color-accent);
  color: var(--color-accent-text);
  border-color: var(--color-accent);
}

.button--secondary {
  background: var(--color-bg-elevated);
  color: var(--color-text-primary);
}

.button--pill {
  background: var(--color-accent-subtle);
  color: var(--color-accent);
  border-radius: var(--radius-full);
}

/* Sizes */
.button--sm {
  height: 32px;
  padding: 0 var(--space-3);
  font-size: var(--text-sm);
}

.button--md {
  height: 36px;
  padding: 0 var(--space-4);
  font-size: var(--text-sm);
}

.button--lg {
  height: 40px;
  padding: 0 var(--space-5);
  font-size: var(--text-base);
}
</style>
