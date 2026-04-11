<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  rememberMe: false
})

const showPassword = ref(false)
const isLoading = ref(false)

const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const handleLogin = async () => {
  if (!form.username || !form.password) {
    return
  }
  
  isLoading.value = true
  
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (form.username === 'student') {
      router.push('/student/home')
    } else if (form.username === 'teacher') {
      router.push('/teacher/home')
    } else if (form.username === 'admin') {
      router.push('/admin/users')
    } else {
      router.push('/student/home')
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <v-container class="min-h-screen d-flex align-center justify-center bg-gradient-to-br from-indigo-50 to-blue-50">
    <v-card
      class="w-full max-w-md elevation-8"
      :style="{
        borderRadius: '16px',
        overflow: 'hidden',
        backdropFilter: 'blur(10px)'
      }"
    >
      <!-- 头部 -->
      <v-card-title class="text-center py-6">
        <div class="flex flex-col items-center">
          <v-avatar
            size="80"
            class="mb-4"
            :style="{
              background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)',
              boxShadow: '0 4px 12px rgba(99, 102, 241, 0.3)'
            }"
          >
            <v-icon size="40" color="white">mdi:school</v-icon>
          </v-avatar>
          <h1 class="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-blue-600 bg-clip-text text-transparent">
            ZhiKe
          </h1>
          <p class="text-gray-500 mt-1">智能教学系统</p>
        </div>
      </v-card-title>
      
      <!-- 表单 -->
      <v-card-text class="px-8">
        <v-form @submit.prevent="handleLogin">
          <v-text-field
            v-model="form.username"
            label="用户名"
            variant="outlined"
            density="comfortable"
            placeholder="请输入用户名"
            autocomplete="username"
            :rules="[(v) => !!v || '请输入用户名']"
            class="mb-4"
          >
            <template v-slot:prepend-inner>
              <v-icon color="primary">mdi:account</v-icon>
            </template>
          </v-text-field>
          
          <v-text-field
            v-model="form.password"
            label="密码"
            variant="outlined"
            density="comfortable"
            placeholder="请输入密码"
            :type="showPassword ? 'text' : 'password'"
            autocomplete="current-password"
            :rules="[(v) => !!v || '请输入密码']"
            class="mb-4"
          >
            <template v-slot:prepend-inner>
              <v-icon color="primary">mdi:lock</v-icon>
            </template>
            <template v-slot:append-inner>
              <v-btn
                icon
                @click="togglePassword"
                variant="text"
                color="primary"
              >
                <v-icon>{{ showPassword ? 'mdi:eye-off' : 'mdi:eye' }}</v-icon>
              </v-btn>
            </template>
          </v-text-field>
          
          <v-checkbox
            v-model="form.rememberMe"
            label="记住我"
            color="primary"
            class="mb-6"
          />
          
          <v-btn
            type="submit"
            color="primary"
            variant="flat"
            block
            :loading="isLoading"
            :disabled="isLoading"
            :style="{
              background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)',
              color: 'white',
              fontWeight: '600',
              padding: '10px 0',
              borderRadius: '8px',
              transition: 'all 0.3s ease'
            }"
            @mouseenter="$event.target.style.transform = 'translateY(-2px)'; $event.target.style.boxShadow = '0 4px 12px rgba(99, 102, 241, 0.3)'"
            @mouseleave="$event.target.style.transform = 'translateY(0)'; $event.target.style.boxShadow = 'none'"
          >
            登 录
          </v-btn>
        </v-form>
      </v-card-text>
      
      <!-- 底部 -->
      <v-card-actions class="justify-center border-t border-gray-200 py-4">
        <div class="text-center">
          <p class="text-xs text-gray-500 mb-2">演示账号：</p>
          <div class="flex flex-wrap gap-2 justify-center">
            <v-chip variant="outlined" size="small" color="primary">
              学生: student
            </v-chip>
            <v-chip variant="outlined" size="small" color="primary">
              教师: teacher
            </v-chip>
            <v-chip variant="outlined" size="small" color="primary">
              管理员: admin
            </v-chip>
          </div>
        </div>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<style scoped>
/* 自定义样式 */
</style>
