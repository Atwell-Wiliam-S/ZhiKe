<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  modelValue: string
  placeholder?: string
  error?: string
  disabled?: boolean
  label?: string
}

const props = withDefaults(defineProps<Props>(), {
  placeholder: '',
  error: '',
  disabled: false,
  label: ''
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const inputClasses = computed(() => {
  return [
    'input',
    { 'input--error': props.error },
    { 'input--disabled': props.disabled }
  ]
})
</script>

<template>
  <div class="input-wrapper">
    <label v-if="label" class="input__label">{{ label }}</label>
    <input
      :class="inputClasses"
      type="text"
      :value="modelValue"
      :placeholder="placeholder"
      :disabled="disabled"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
    <div v-if="error" class="input__error">{{ error }}</div>
  </div>
</template>

<style scoped>
.input-wrapper {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  width: 100%;
}

.input__label {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-text-secondary);
}

.input {
  width: 100%;
  height: 40px;
  padding: 0 var(--space-4);
  background: var(--color-bg-input);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  color: var(--color-text-primary);
  font-size: var(--text-base);
  transition: all var(--duration-normal) var(--ease-default);
}

.input:focus {
  outline: none;
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-accent-subtle);
}

.input::placeholder {
  color: var(--color-text-tertiary);
}

.input--error {
  border-color: var(--color-danger);
}

.input--error:focus {
  box-shadow: 0 0 0 3px var(--color-danger-bg);
}

.input--disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.input__error {
  font-size: var(--text-xs);
  color: var(--color-danger);
  margin-top: var(--space-1);
}
</style>
