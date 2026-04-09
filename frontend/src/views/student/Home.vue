<template>
  <div class="student-home">
    <div class="header">
      <h1>欢迎回来，{{ userInfo?.username }}</h1>
      <p>开始你的学习之旅</p>
    </div>
    <div class="feature-grid">
      <div class="feature-card">
        <h3>我的课程</h3>
        <p>查看已报名的课程</p>
      </div>
      <div class="feature-card">
        <h3>继续学习</h3>
        <p>从上次的位置继续</p>
      </div>
      <div class="feature-card">
        <h3>AI 助手</h3>
        <p>随时解答你的问题</p>
      </div>
      <div class="feature-card">
        <h3>我的笔记</h3>
        <p>查看和管理笔记</p>
      </div>
      <div class="feature-card">
        <h3>学习报告</h3>
        <p>查看学习进度</p>
      </div>
      <div class="feature-card">
        <h3>设置</h3>
        <p>个性化设置</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userInfo = userStore.userInfo
</script>

<style scoped>
.student-home {
  min-height: 100vh;
  background: #1a1a2e;
  color: #e2e8f0;
  padding: 40px;
}

.header {
  text-align: center;
  margin-bottom: 60px;
}

.header h1 {
  font-size: 36px;
  font-weight: 400;
  margin-bottom: 10px;
}

.header p {
  font-size: 18px;
  color: #94a3b8;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background: #252540;
  border: 1px solid #3a3a5c;
  border-radius: 8px;
  padding: 32px;
  text-align: center;
  transition: all 0.3s ease;
}

.feature-card:hover {
  border-color: #6366f1;
  box-shadow: 0 0 20px rgba(99, 102, 241, 0.3);
  transform: translateY(-5px);
}

.feature-card h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #e2e8f0;
}

.feature-card p {
  font-size: 14px;
  color: #94a3b8;
}
</style>
