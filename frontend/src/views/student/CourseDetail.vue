<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 课程信息
const course = ref({
  id: 1,
  title: '前端开发基础',
  description: '本课程将介绍前端开发的基础知识，包括HTML、CSS、JavaScript等核心技术，帮助你快速入门前端开发。',
  cover: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20frontend%20development%20course%20cover%20with%20code%20and%20laptop&image_size=landscape_16_9',
  instructor: '张老师',
  duration: '10小时',
  lessons: 20,
  progress: 30,
  rating: 4.8,
  students: 1200
})

// 课程章节
const chapters = ref([
  {
    id: 1,
    title: '第一章：前端开发概述',
    lessons: [
      { id: 1, title: '1.1 前端开发简介', duration: '20分钟', completed: true },
      { id: 2, title: '1.2 前端技术栈', duration: '15分钟', completed: true },
      { id: 3, title: '1.3 开发环境搭建', duration: '25分钟', completed: false }
    ]
  },
  {
    id: 2,
    title: '第二章：HTML基础',
    lessons: [
      { id: 4, title: '2.1 HTML简介', duration: '15分钟', completed: false },
      { id: 5, title: '2.2 HTML标签', duration: '20分钟', completed: false },
      { id: 6, title: '2.3 HTML语义化', duration: '15分钟', completed: false }
    ]
  },
  {
    id: 3,
    title: '第三章：CSS基础',
    lessons: [
      { id: 7, title: '3.1 CSS简介', duration: '15分钟', completed: false },
      { id: 8, title: '3.2 CSS选择器', duration: '20分钟', completed: false },
      { id: 9, title: '3.3 CSS盒模型', duration: '20分钟', completed: false }
    ]
  }
])

// 导航到视频学习页面
const navigateToVideo = (lessonId: number) => {
  router.push(`/student/video-learning?lessonId=${lessonId}`)
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
          :value="'/student/course/1'"
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
      <!-- 课程封面 -->
      <div class="relative">
        <img
          :src="course.cover"
          alt="课程封面"
          class="w-full h-64 object-cover"
        >
        <div class="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent flex items-end">
          <v-container class="max-w-7xl py-6">
            <h1 class="text-3xl font-bold text-white">{{ course.title }}</h1>
            <div class="flex flex-wrap gap-4 mt-2">
              <span class="text-white/80 text-sm flex items-center">
                <v-icon size="16" class="mr-1">mdi:account</v-icon>
                {{ course.instructor }}
              </span>
              <span class="text-white/80 text-sm flex items-center">
                <v-icon size="16" class="mr-1">mdi:clock-outline</v-icon>
                {{ course.duration }}
              </span>
              <span class="text-white/80 text-sm flex items-center">
                <v-icon size="16" class="mr-1">mdi:book-open-variant</v-icon>
                {{ course.lessons }} 课时
              </span>
              <span class="text-white/80 text-sm flex items-center">
                <v-icon size="16" class="mr-1">mdi:star</v-icon>
                {{ course.rating }} ({{ course.students }} 人学习)
              </span>
            </div>
          </v-container>
        </div>
      </div>

      <v-container class="max-w-7xl py-8">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <!-- 课程信息和操作 -->
          <div class="lg:col-span-2">
            <!-- 课程描述 -->
            <v-card variant="outlined" class="mb-6" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-xl font-semibold">课程简介</v-card-title>
              <v-card-text>{{ course.description }}</v-card-text>
            </v-card>

            <!-- 课程章节 -->
            <v-card variant="outlined" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-xl font-semibold">课程章节</v-card-title>
              <v-card-text>
                <div v-for="chapter in chapters" :key="chapter.id" class="mb-6">
                  <div class="flex items-center justify-between mb-3">
                    <h3 class="font-semibold">{{ chapter.title }}</h3>
                    <span class="text-sm text-gray-500">{{ chapter.lessons.length }} 课时</span>
                  </div>
                  <div class="pl-4 border-l-2 border-gray-200">
                    <v-list-item
                      v-for="lesson in chapter.lessons"
                      :key="lesson.id"
                      class="cursor-pointer mb-2 rounded-lg hover:bg-gray-50"
                      @click="navigateToVideo(lesson.id)"
                    >
                      <v-list-item-avatar class="mr-3">
                        <v-icon :color="lesson.completed ? 'green' : 'primary'">
                          {{ lesson.completed ? 'mdi:check-circle' : 'mdi:play-circle' }}
                        </v-icon>
                      </v-list-item-avatar>
                      <v-list-item-title class="text-sm">{{ lesson.title }}</v-list-item-title>
                      <v-list-item-action>
                        <span class="text-xs text-gray-500">{{ lesson.duration }}</span>
                      </v-list-item-action>
                    </v-list-item>
                  </div>
                </div>
              </v-card-text>
            </v-card>
          </div>

          <!-- 侧边栏 -->
          <div class="lg:col-span-1">
            <!-- 课程进度 -->
            <v-card variant="outlined" class="mb-6" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">学习进度</v-card-title>
              <v-card-text>
                <v-progress-linear
                  :value="course.progress"
                  color="primary"
                  class="mb-2"
                  height="8"
                  rounded
                ></v-progress-linear>
                <div class="flex justify-between text-sm">
                  <span>已完成 {{ course.progress }}%</span>
                  <span>{{ Math.round(course.lessons * course.progress / 100) }} / {{ course.lessons }} 课时</span>
                </div>
              </v-card-text>
            </v-card>

            <!-- 操作按钮 -->
            <v-card variant="outlined" :style="{ borderRadius: '16px' }">
              <v-card-text class="pt-6">
                <v-btn
                  block
                  color="primary"
                  variant="flat"
                  class="mb-4"
                  :style="{
                    background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)',
                    color: 'white',
                    fontWeight: '600',
                    padding: '12px 0',
                    borderRadius: '8px'
                  }"
                  @click="navigateToVideo(1)"
                >
                  继续学习
                </v-btn>
                <v-btn
                  block
                  color="primary"
                  variant="outlined"
                  class="mb-4"
                  :style="{
                    borderRadius: '8px'
                  }"
                >
                  <v-icon left>mdi:heart-outline</v-icon>
                  收藏课程
                </v-btn>
                <v-btn
                  block
                  color="primary"
                  variant="text"
                  :style="{
                    borderRadius: '8px'
                  }"
                >
                  <v-icon left>mdi:share-variant</v-icon>
                  分享课程
                </v-btn>
              </v-card-text>
            </v-card>

            <!-- 讲师信息 -->
            <v-card variant="outlined" class="mt-6" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">讲师信息</v-card-title>
              <v-card-text>
                <div class="flex items-center mb-3">
                  <v-avatar
                    size="64"
                    class="mr-4"
                    :style="{
                      background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)'
                    }"
                  >
                    <span class="text-white font-semibold text-lg">张</span>
                  </v-avatar>
                  <div>
                    <h4 class="font-semibold">{{ course.instructor }}</h4>
                    <p class="text-sm text-gray-500">前端开发讲师</p>
                  </div>
                </div>
                <p class="text-sm text-gray-600">
                  5年前端开发经验，曾在多家互联网公司担任前端工程师，专注于前端技术教学和实践。
                </p>
              </v-card-text>
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
