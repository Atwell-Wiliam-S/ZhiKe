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
<template>
  <div<template>
  <div class="student-home">
    <el<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon"><template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon">🚪</div>
        <h3><template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon">🚪</div>
        <h3>退出登录</h3>
        <p<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon">🚪</div>
        <h3>退出登录</h3>
        <p>安全退出系统</p>
      </el-card>
    </div>
<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon">🚪</div>
        <h3>退出登录</h3>
        <p>安全退出系统</p>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { use<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon">🚪</div>
        <h3>退出登录</h3>
        <p>安全退出系统</p>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
<template>
  <div class="student-home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎回来，{{ userStore.userInfo?.username }}</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>智能教学闭环系统</h2>
        <p>这里是你的学习中心，你可以：</p>
        <ul>
          <li>浏览课程列表</li>
          <li>观看视频课程</li>
          <li>向AI助教提问</li>
          <li>查看学习进度</li>
        </ul>
      </div>
    </el-card>
    
    <div class="feature-grid">
      <el-card class="feature-card" @click="navigateTo('/student/courses')">
        <div class="feature-icon">📚</div>
        <h3>我的课程</h3>
        <p>浏览和管理你的课程</p>
      </el-card>
      
      <el-card class="feature-card" @click="navigateTo('/student/learn/1')">
        <div class="feature-icon">🎥</div>
        <h3>继续学习</h3>
        <p>从上次的地方继续学习</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">🤖</div>
        <h3>AI助教</h3>
        <p>向AI助教提问，获取学习帮助</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📝</div>
        <h3>我的笔记</h3>
        <p>查看和管理你的学习笔记</p>
      </el-card>
      
      <el-card class="feature-card">
        <div class="feature-icon">📊</div>
        <h3>学习报告</h3>
        <p>查看你的学习进度和成绩</p>
      </el-card>
      
      <el-card class="feature-card" @click="handleLogout">
        <div class="feature-icon">🚪</div>
        <h3>退出登录</h3>
        <p>安全退出系统</p>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
