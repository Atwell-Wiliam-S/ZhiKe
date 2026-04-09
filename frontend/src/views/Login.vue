<template>
  <div class="login-container">
    <div class="login-form">
      <h1>智能教学闭环系统</h1>
      <h2>用户登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
        <div class="login-hint">
          <p>测试账号：</p>
          <p>学生: student / 任意密码</p>
          <p>教师: teacher / 任意密码</p>
          <p>管理员: admin / 任意密码</p>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { LoginRequest } from '@/types/api'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref()

const loginForm = reactive<LoginRequest>({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    const success = await userStore.login(loginForm.username, loginForm.password)
    if (success) {
      const roleCode = userStore.roleCode
      if (roleCode === 'STUDENT') {
        router.push('/student/home')
      } else if (roleCode === 'TEACHER') {
        router.push('/teacher/home')
      } else if (roleCode === 'ADMIN') {
        router.push('/admin/home')
      }
    }
  } catch (error) {
    console.error('登录失败:', error)
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  width: 400px;
  padding: 30px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-form h1 {
  font-size: 24px;
  color: #303133;
  text-align: center;
  margin-bottom: 10px;
}

.login-form h2 {
  font-size: 18px;
  color: #606266;
  text-align: center;
  margin-bottom: 30px;
}

.login-btn {
  width: 100%;
}

.login-hint {
  margin-top: 20px;
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
}
</style>