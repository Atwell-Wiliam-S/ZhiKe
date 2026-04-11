<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const searchQuery = ref('')
const selectedCourse = ref('all')
const selectedVideo = ref('all')

interface Note {
  id: number
  content: string
  videoTitle: string
  timestamp: number
  createdAt: string
  courseName: string
}

const notes = ref<Note[]>([
  {
    id: 1,
    content: '前端开发的定义：前端开发是创建Web页面或app等前端界面呈现给用户的过程，通过HTML、CSS及JavaScript以及衍生出来的各种技术、框架、解决方案，来实现互联网产品的用户界面交互。',
    videoTitle: '1.1 前端开发简介',
    timestamp: 150,
    createdAt: '2023-10-15 14:30',
    courseName: '前端开发基础'
  },
  {
    id: 2,
    content: '前端开发的发展历程：从静态页面到动态交互，从jQuery到现代框架（React、Vue、Angular），前端开发技术不断演进。',
    videoTitle: '1.1 前端开发简介',
    timestamp: 315,
    createdAt: '2023-10-15 14:35',
    courseName: '前端开发基础'
  },
  {
    id: 3,
    content: 'HTML的基本结构：DOCTYPE、html、head、body等标签的作用和使用方法。',
    videoTitle: '2.1 HTML简介',
    timestamp: 90,
    createdAt: '2023-10-16 10:20',
    courseName: '前端开发基础'
  },
  {
    id: 4,
    content: 'CSS选择器的优先级：内联样式 > ID选择器 > 类选择器 > 标签选择器。',
    videoTitle: '3.2 CSS选择器',
    timestamp: 240,
    createdAt: '2023-10-17 09:45',
    courseName: '前端开发基础'
  }
])

const goToVideoPage = () => {
  router.push('/student/video-learning')
}

// 格式化时间戳
const formatTimestamp = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
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
          :value="'/student/notes'"
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
        <div class="flex flex-wrap justify-between items-start mb-8 gap-4">
          <div>
            <h2 class="text-2xl font-bold text-gray-800">我的笔记</h2>
            <p class="text-gray-600">在视频学习过程中记录的知识点</p>
          </div>
          <v-chip
            color="primary"
            variant="outlined"
            :style="{
              background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(59, 130, 246, 0.15) 100%)',
              borderColor: 'rgba(99, 102, 241, 0.3)'
            }"
          >
            <v-icon left color="primary">mdi:file-document</v-icon>
            共 {{ notes.length }} 条
          </v-chip>
        </div>

        <!-- 过滤栏 -->
        <div class="flex flex-wrap gap-4 mb-8">
          <div class="flex gap-2">
            <v-select
              v-model="selectedCourse"
              :items="['全部课程', '前端开发基础', 'JavaScript进阶', 'Vue3实战']"
              label="课程"
              variant="outlined"
              density="comfortable"
              class="min-w-[140px]"
            ></v-select>
            <v-select
              v-model="selectedVideo"
              :items="['全部视频', '1.1 前端开发简介', '2.1 HTML简介', '3.2 CSS选择器']"
              label="视频"
              variant="outlined"
              density="comfortable"
              class="min-w-[140px]"
            ></v-select>
          </div>
          <v-spacer></v-spacer>
          <v-text-field
            v-model="searchQuery"
            label="搜索笔记内容..."
            variant="outlined"
            density="comfortable"
            prepend-inner-icon="mdi:magnify"
            class="min-w-[240px]"
          ></v-text-field>
        </div>

        <!-- 笔记内容 -->
        <div class="notes-content">
          <div v-if="notes.length === 0" class="text-center py-12">
            <v-avatar
              size="80"
              class="mx-auto mb-4"
              :style="{
                background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(59, 130, 246, 0.1) 100%)'
              }"
            >
              <v-icon size="40" color="primary">mdi:file-document</v-icon>
            </v-avatar>
            <h3 class="text-lg font-semibold text-gray-800 mb-2">暂无笔记</h3>
            <p class="text-gray-600 mb-6">在视频学习页面按快捷键添加笔记</p>
            <v-btn
              color="primary"
              variant="flat"
              @click="goToVideoPage"
              :style="{
                background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)',
                color: 'white',
                borderRadius: '8px'
              }"
            >
              <v-icon left>mdi:play-circle</v-icon>
              去学习页
            </v-btn>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <v-card
              v-for="note in notes"
              :key="note.id"
              variant="outlined"
              class="hover:shadow-md transition-shadow duration-300"
              :style="{ borderRadius: '16px' }"
            >
              <v-card-title class="text-sm font-semibold text-gray-500">
                {{ note.courseName }} - {{ note.videoTitle }}
              </v-card-title>
              <v-card-text>
                <p class="text-sm mb-4">{{ note.content }}</p>
                <div class="flex justify-between items-center text-xs text-gray-500">
                  <span class="flex items-center">
                    <v-icon size="14" class="mr-1">mdi:clock-outline</v-icon>
                    {{ formatTimestamp(note.timestamp) }}
                  </span>
                  <span>{{ note.createdAt }}</span>
                </div>
              </v-card-text>
              <v-card-actions class="justify-end">
                <v-btn
                  color="primary"
                  variant="text"
                  size="small"
                  @click="goToVideoPage"
                >
                  查看视频
                </v-btn>
              </v-card-actions>
            </v-card>
          </div>
        </div>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
/* 自定义样式 */
</style>
