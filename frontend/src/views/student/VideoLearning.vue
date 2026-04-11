<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 课程信息
const course = ref({
  id: 1,
  title: '前端开发基础',
  currentLesson: {
    id: 1,
    title: '1.1 前端开发简介',
    videoUrl: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
    duration: '20分钟',
    description: '本课时将介绍前端开发的基本概念、发展历程和未来趋势，帮助你对前端开发有一个全面的认识。'
  }
})

// 课程章节
const chapters = ref([
  {
    id: 1,
    title: '第一章：前端开发概述',
    lessons: [
      { id: 1, title: '1.1 前端开发简介', duration: '20分钟', current: true },
      { id: 2, title: '1.2 前端技术栈', duration: '15分钟', current: false },
      { id: 3, title: '1.3 开发环境搭建', duration: '25分钟', current: false }
    ]
  },
  {
    id: 2,
    title: '第二章：HTML基础',
    lessons: [
      { id: 4, title: '2.1 HTML简介', duration: '15分钟', current: false },
      { id: 5, title: '2.2 HTML标签', duration: '20分钟', current: false },
      { id: 6, title: '2.3 HTML语义化', duration: '15分钟', current: false }
    ]
  },
  {
    id: 3,
    title: '第三章：CSS基础',
    lessons: [
      { id: 7, title: '3.1 CSS简介', duration: '15分钟', current: false },
      { id: 8, title: '3.2 CSS选择器', duration: '20分钟', current: false },
      { id: 9, title: '3.3 CSS盒模型', duration: '20分钟', current: false }
    ]
  }
])

// 笔记
const notes = ref([
  {
    id: 1,
    timestamp: '02:30',
    content: '前端开发的定义和职责'
  },
  {
    id: 2,
    timestamp: '05:15',
    content: '前端开发的发展历程'
  }
])

const newNote = ref('')
const noteTimestamp = ref('00:00')

// 视频播放状态
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(600) // 10分钟

// 切换视频
const switchLesson = (lessonId: number) => {
  // 这里可以实现切换视频的逻辑
  console.log('切换到课程:', lessonId)
}

// 添加笔记
const addNote = () => {
  if (newNote.value.trim()) {
    notes.value.push({
      id: notes.value.length + 1,
      timestamp: noteTimestamp.value,
      content: newNote.value
    })
    newNote.value = ''
  }
}

// 格式化时间
const formatTime = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

// 处理视频时间更新
const handleTimeUpdate = (event: any) => {
  currentTime.value = event.target.currentTime
  noteTimestamp.value = formatTime(currentTime.value)
}

// 处理视频播放/暂停
const togglePlay = () => {
  isPlaying.value = !isPlaying.value
  const video = document.getElementById('videoPlayer') as HTMLVideoElement
  if (video) {
    if (isPlaying.value) {
      video.play()
    } else {
      video.pause()
    }
  }
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
          :value="'/student/video-learning'"
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
      <v-container class="max-w-7xl py-6">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <!-- 视频播放器和课程信息 -->
          <div class="lg:col-span-2">
            <!-- 视频播放器 -->
            <v-card variant="outlined" class="mb-6" :style="{ borderRadius: '16px', overflow: 'hidden' }">
              <div class="relative">
                <video
                  id="videoPlayer"
                  :src="course.currentLesson.videoUrl"
                  class="w-full aspect-video"
                  @timeupdate="handleTimeUpdate"
                  @play="isPlaying = true"
                  @pause="isPlaying = false"
                >
                </video>
                <div class="absolute inset-0 flex items-center justify-center" @click="togglePlay">
                  <v-avatar
                    size="80"
                    :style="{
                      background: 'rgba(0, 0, 0, 0.6)',
                      cursor: 'pointer'
                    }"
                  >
                    <v-icon size="40" color="white">
                      {{ isPlaying ? 'mdi:pause' : 'mdi:play' }}
                    </v-icon>
                  </v-avatar>
                </div>
                <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-4">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-white text-sm">{{ formatTime(currentTime) }}</span>
                    <span class="text-white text-sm">{{ course.currentLesson.duration }}</span>
                  </div>
                  <input
                    type="range"
                    min="0"
                    :max="duration"
                    v-model="currentTime"
                    class="w-full h-2 bg-white/30 rounded-lg appearance-none cursor-pointer"
                    @input="(e) => {
                      const video = document.getElementById('videoPlayer') as HTMLVideoElement
                      if (video) video.currentTime = Number(e.target.value)
                    }"
                  >
                </div>
              </div>
              <v-card-title class="text-xl font-semibold">
                {{ course.currentLesson.title }}
              </v-card-title>
              <v-card-text>
                {{ course.currentLesson.description }}
              </v-card-text>
            </v-card>

            <!-- 笔记功能 -->
            <v-card variant="outlined" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">课程笔记</v-card-title>
              <v-card-text>
                <div class="mb-4">
                  <v-text-field
                    v-model="newNote"
                    label="添加笔记"
                    variant="outlined"
                    placeholder="输入笔记内容..."
                    :append-inner-icon="'mdi:clock-outline'"
                    :append-inner-text="noteTimestamp"
                    class="mb-2"
                  ></v-text-field>
                  <v-btn
                    color="primary"
                    variant="flat"
                    @click="addNote"
                    :style="{
                      background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)',
                      color: 'white',
                      borderRadius: '8px'
                    }"
                  >
                    添加笔记
                  </v-btn>
                </div>
                <div v-if="notes.length === 0" class="text-center py-4 text-gray-500">
                  暂无笔记，开始添加吧！
                </div>
                <div v-else class="space-y-3">
                  <v-card
                    v-for="note in notes"
                    :key="note.id"
                    variant="outlined"
                    class="p-3"
                    :style="{ borderRadius: '12px' }"
                  >
                    <div class="flex justify-between items-start">
                      <div>
                        <p class="text-sm font-semibold text-primary">{{ note.timestamp }}</p>
                        <p class="text-sm">{{ note.content }}</p>
                      </div>
                      <v-btn
                        icon
                        size="small"
                        color="error"
                        variant="text"
                      >
                        <v-icon size="16">mdi:delete</v-icon>
                      </v-btn>
                    </div>
                  </v-card>
                </div>
              </v-card-text>
            </v-card>
          </div>

          <!-- 章节列表 -->
          <div class="lg:col-span-1">
            <v-card variant="outlined" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">课程章节</v-card-title>
              <v-card-text>
                <div v-for="chapter in chapters" :key="chapter.id" class="mb-4">
                  <h3 class="font-semibold mb-2">{{ chapter.title }}</h3>
                  <div class="pl-4 space-y-2">
                    <v-list-item
                      v-for="lesson in chapter.lessons"
                      :key="lesson.id"
                      class="cursor-pointer rounded-lg"
                      :class="{
                        'bg-primary/10': lesson.current,
                        'hover:bg-gray-50': !lesson.current
                      }"
                      @click="switchLesson(lesson.id)"
                    >
                      <v-list-item-avatar class="mr-3">
                        <v-icon :color="lesson.current ? 'primary' : 'gray'">
                          mdi:play-circle
                        </v-icon>
                      </v-list-item-avatar>
                      <v-list-item-title class="text-sm">
                        <span :class="{
                          'font-semibold text-primary': lesson.current
                        }">
                          {{ lesson.title }}
                        </span>
                      </v-list-item-title>
                      <v-list-item-action>
                        <span class="text-xs text-gray-500">{{ lesson.duration }}</span>
                      </v-list-item-action>
                    </v-list-item>
                  </div>
                </div>
              </v-card-text>
            </v-card>

            <!-- 课程信息 -->
            <v-card variant="outlined" class="mt-6" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">课程信息</v-card-title>
              <v-card-text>
                <h3 class="font-semibold mb-2">{{ course.title }}</h3>
                <div class="space-y-2 text-sm">
                  <div class="flex justify-between">
                    <span class="text-gray-600">当前章节:</span>
                    <span>{{ course.currentLesson.title }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-gray-600">课程进度:</span>
                    <span>10%</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-gray-600">总课时:</span>
                    <span>20课时</span>
                  </div>
                </div>
                <v-btn
                  block
                  color="primary"
                  variant="outlined"
                  class="mt-4"
                  :style="{ borderRadius: '8px' }"
                  @click="router.push('/student/course/1')"
                >
                  返回课程详情
                </v-btn>
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
input[type="range"]::-webkit-slider-thumb {
  appearance: none;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: white;
  cursor: pointer;
}

input[type="range"]::-moz-range-thumb {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: white;
  cursor: pointer;
  border: none;
}
</style>
