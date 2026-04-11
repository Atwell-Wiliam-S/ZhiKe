<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const features = ref([
  {
    id: 1,
    title: '课程学习',
    description: '浏览并学习各类课程',
    icon: 'mdi:book-open-variant',
    path: '/student/course/1'
  },
  {
    id: 2,
    title: '问答历史',
    description: '查看与AI助教的所有对话记录',
    icon: 'mdi:message-text',
    path: '/student/qa-history'
  },
  {
    id: 3,
    title: '我的笔记',
    description: '在视频学习过程中记录的知识点',
    icon: 'mdi:file-document',
    path: '/student/notes'
  },
  {
    id: 4,
    title: '设置',
    description: '个性化设置和偏好配置',
    icon: 'mdi:cog',
    path: '/student/settings'
  }
])

const navigateTo = (path: string) => {
  router.push(path)
}
</script>

<template>
  <v-app>
    <!-- 顶部导航栏 -->
    <v-app-bar
      color="white"
      elevation="1"
      fixed
    >
      <v-container class="max-w-7xl">
        <v-app-bar-title class="d-flex items-center">
          <v-avatar
            size="40"
            class="mr-3"
            :style="{
              background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)'
            }"
          >
            <v-icon size="20" color="white">mdi:school</v-icon>
          </v-avatar>
          <span class="text-xl font-bold bg-gradient-to-r from-indigo-600 to-blue-600 bg-clip-text text-transparent">
            ZhiKe
          </span>
        </v-app-bar-title>
        
        <v-spacer></v-spacer>
        
        <v-navigation-drawer-toggle v-if="$vuetify.display.mobile"></v-navigation-drawer-toggle>
        
        <v-menu v-if="$vuetify.display.mobile" bottom right>
          <template v-slot:activator="{ props }">
            <v-btn
              variant="text"
              color="primary"
              v-bind="props"
            >
              <v-icon>mdi:menu</v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item
              v-for="item in [
                { title: '首页', path: '/student/home' },
                { title: '我的课程', path: '/student/course/1' },
                { title: '问答历史', path: '/student/qa-history' },
                { title: '我的笔记', path: '/student/notes' }
              ]"
              :key="item.path"
              :to="item.path"
              link
            >
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
        
        <v-tabs
          v-else
          :value="'/student/home'"
          class="hidden-sm-and-down"
          indicator-color="primary"
          text-color="primary"
          active-class="text-primary"
        >
          <v-tab :to="'/student/home'">首页</v-tab>
          <v-tab :to="'/student/course/1'">我的课程</v-tab>
          <v-tab :to="'/student/qa-history'">问答历史</v-tab>
          <v-tab :to="'/student/notes'">我的笔记</v-tab>
        </v-tabs>
        
        <v-spacer></v-spacer>
        
        <v-avatar
          size="40"
          :style="{
            background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)'
          }"
        >
          <span class="text-white font-semibold">Z</span>
        </v-avatar>
      </v-container>
    </v-app-bar>

    <!-- 主内容 -->
    <v-main class="pt-20">
      <v-container class="max-w-7xl py-8">
        <!-- 页面头部 -->
        <div class="mb-8">
          <h2 class="text-2xl font-bold text-gray-800">欢迎回来！</h2>
          <p class="text-gray-600">继续您的学习之旅</p>
        </div>

        <!-- 功能卡片 -->
        <v-grid
          cols="1"
          sm="2"
          md="4"
          gap="4"
          class="mb-12"
        >
          <v-card
            v-for="feature in features"
            :key="feature.id"
            class="cursor-pointer hover:shadow-lg transition-all duration-300 hover:-translate-y-1"
            @click="navigateTo(feature.path)"
            variant="outlined"
            :style="{
              borderRadius: '16px',
              borderColor: 'rgba(99, 102, 241, 0.2)'
            }"
          >
            <v-card-text class="pt-6">
              <v-avatar
                size="56"
                class="mb-4"
                :style="{
                  background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.2) 0%, rgba(59, 130, 246, 0.2) 100%)'
                }"
              >
                <v-icon size="28" color="primary">{{ feature.icon }}</v-icon>
              </v-avatar>
              <h3 class="text-lg font-semibold text-gray-800 mb-1">{{ feature.title }}</h3>
              <p class="text-gray-600 text-sm">{{ feature.description }}</p>
            </v-card-text>
          </v-card>
        </v-grid>

        <!-- 最近学习 -->
        <div class="mt-12">
          <h3 class="text-xl font-semibold text-gray-800 mb-4">最近学习</h3>
          <v-card variant="outlined" :style="{ borderRadius: '16px' }">
            <v-card-text class="text-center py-12">
              <v-avatar
                size="80"
                class="mx-auto mb-4"
                :style="{
                  background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(59, 130, 246, 0.1) 100%)'
                }"
              >
                <v-icon size="48" color="primary" opacity="0.6">mdi:book-open-variant</v-icon>
              </v-avatar>
              <p class="text-lg font-semibold text-gray-800 mb-1">暂无学习记录</p>
              <p class="text-gray-600">开始学习一门课程吧</p>
            </v-card-text>
          </v-card>
        </div>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
/* 自定义样式 */
</style>
