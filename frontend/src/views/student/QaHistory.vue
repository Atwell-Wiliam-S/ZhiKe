<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Calendar, MessageSquare, ChevronDown, GraduationCap, Clock, Book, User } from 'lucide-vue-next'

const router = useRouter()

const searchQuery = ref('')
const activeFilter = ref('all')
const sortOrder = ref('latest')

const filters = [
  { id: 'today', label: '今天' },
  { id: 'week', label: '本周' },
  { id: 'month', label: '本月' },
  { id: 'all', label: '全部' }
]

interface QaRecord {
  id: number
  question: string
  answer: string
  videoTitle: string
  timestamp: string
  courseName: string
}

const qaRecords = ref<QaRecord[]>([])

const setFilter = (filterId: string) => {
  activeFilter.value = filterId
}

// Mock数据
const mockQaRecords: QaRecord[] = [
  {
    id: 1,
    question: '什么是Vue 3的Composition API？',
    answer: 'Vue 3的Composition API是一组新的API，允许开发者使用函数而不是选项对象来组织组件逻辑。它提供了更好的代码组织、类型推断和重用性。',
    videoTitle: 'Vue 3核心概念',
    timestamp: '2024-01-15 14:30',
    courseName: '前端框架进阶'
  },
  {
    id: 2,
    question: '如何实现Vue组件的通信？',
    answer: 'Vue组件通信可以通过props、事件、provide/inject、Vuex/Pinia状态管理等方式实现。具体选择哪种方式取决于组件之间的关系和通信复杂度。',
    videoTitle: 'Vue组件通信',
    timestamp: '2024-01-14 10:20',
    courseName: '前端框架进阶'
  },
  {
    id: 3,
    question: '什么是TypeScript？',
    answer: 'TypeScript是JavaScript的超集，添加了静态类型系统。它可以帮助开发者在编译时发现错误，提高代码质量和可维护性。',
    videoTitle: 'TypeScript基础',
    timestamp: '2024-01-13 16:45',
    courseName: 'TypeScript实战'
  },
  {
    id: 4,
    question: '如何优化Vue应用的性能？',
    answer: 'Vue应用性能优化可以从以下几个方面入手：使用v-if和v-show合理切换、使用computed缓存计算结果、使用虚拟列表处理长列表、合理使用keep-alive等。',
    videoTitle: 'Vue性能优化',
    timestamp: '2024-01-12 09:15',
    courseName: '前端框架进阶'
  },
  {
    id: 5,
    question: '什么是响应式设计？',
    answer: '响应式设计是一种设计方法，使网站能够适应不同屏幕尺寸和设备。它通过媒体查询、弹性布局和流体图像等技术实现。',
    videoTitle: '响应式设计原理',
    timestamp: '2024-01-11 11:30',
    courseName: '前端基础'
  }
]

onMounted(() => {
  // 模拟数据加载
  setTimeout(() => {
    qaRecords.value = mockQaRecords
  }, 500)
})
</script>

<template>
  <v-app>
    <!-- 顶部导航栏 -->
    <v-app-bar app color="white" elevation="1">
      <div class="container mx-auto px-4">
        <div class="flex items-center justify-between w-full">
          <!-- Logo -->
          <div class="flex items-center space-x-2">
            <GraduationCap class="text-blue-600" :size="32" />
            <h1 class="text-xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">ZhiKe</h1>
          </div>
          
          <!-- 导航菜单 -->
          <nav class="hidden md:flex items-center space-x-6">
            <router-link to="/student/home" class="text-gray-600 hover:text-blue-600 transition-colors">首页</router-link>
            <router-link to="/student/course/1" class="text-gray-600 hover:text-blue-600 transition-colors">我的课程</router-link>
            <router-link to="/student/qa-history" class="text-blue-600 font-medium relative">
              问答历史
              <div class="absolute bottom-0 left-0 w-full h-0.5 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full"></div>
            </router-link>
            <router-link to="/student/notes" class="text-gray-600 hover:text-blue-600 transition-colors">我的笔记</router-link>
          </nav>
          
          <!-- 用户头像 -->
          <div class="flex items-center">
            <v-avatar color="gradient-blue-purple" size="40" class="cursor-pointer">
              <span class="text-white font-semibold">Z</span>
            </v-avatar>
          </div>
        </div>
      </div>
    </v-app-bar>

    <!-- 主内容区 -->
    <v-main>
      <div class="container mx-auto px-4 py-8">
        <!-- 页面标题 -->
        <div class="flex flex-col md:flex-row md:items-center md:justify-between mb-8 space-y-4 md:space-y-0">
          <div>
            <h2 class="text-2xl font-bold text-gray-800">问答历史</h2>
            <p class="text-gray-500 mt-1">查看你与AI助教的所有对话记录</p>
          </div>
          <v-chip
            color="blue-lighten-5"
            text-color="blue"
            class="flex items-center space-x-2"
          >
            <MessageSquare :size="16" />
            <span>共 {{ qaRecords.length }} 条</span>
          </v-chip>
        </div>

        <!-- 筛选栏 -->
        <div class="flex flex-col md:flex-row items-stretch md:items-center space-y-4 md:space-y-0 md:space-x-4 mb-8">
          <!-- 搜索框 -->
          <v-text-field
            v-model="searchQuery"
            prepend-inner-icon="mdi-magnify"
            placeholder="搜索问题..."
            variant="outlined"
            density="compact"
            class="flex-1"
          />

          <!-- 时间筛选 -->
          <div class="flex flex-wrap gap-2">
            <v-btn
              v-for="filter in filters"
              :key="filter.id"
              :color="activeFilter === filter.id ? 'blue' : 'white'"
              :variant="activeFilter === filter.id ? 'flat' : 'outlined'"
              @click="setFilter(filter.id)"
              size="small"
              rounded="full"
            >
              {{ filter.label }}
            </v-btn>
          </div>

          <!-- 排序下拉框 -->
          <v-menu
            transition="scale-transition"
            offset-y
            max-width="200px"
          >
            <template v-slot:activator="{ props }">
              <v-btn
                v-bind="props"
                variant="outlined"
                size="small"
                class="flex items-center space-x-2"
              >
                <Calendar :size="16" />
                <span>最新优先</span>
                <ChevronDown :size="16" />
              </v-btn>
            </template>
            <v-list>
              <v-list-item>
                <v-list-item-title>最新优先</v-list-item-title>
              </v-list-item>
              <v-list-item>
                <v-list-item-title>最早优先</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>

        <!-- 问答列表 -->
        <div class="qa-list">
          <v-skeleton-loader
            v-if="qaRecords.length === 0"
            type="card-text"
            class="mx-auto"
            :loading="true"
          >
            <div class="text-center py-12">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
              <p class="text-gray-500">加载中...</p>
            </div>
          </v-skeleton-loader>

          <div v-else class="grid gap-6">
            <v-card
              v-for="record in qaRecords"
              :key="record.id"
              class="border border-gray-100 hover:shadow-md transition-shadow"
            >
              <v-card-item>
                <div class="flex-1">
                  <div class="flex items-center space-x-2 mb-2">
                    <Book size="16" class="text-blue-500" />
                    <span class="text-sm text-gray-500">{{ record.courseName }}</span>
                  </div>
                  <h3 class="text-lg font-semibold text-gray-800 mb-2">{{ record.question }}</h3>
                  <p class="text-gray-600 text-sm line-clamp-2 mb-4">{{ record.answer }}</p>
                  <div class="flex items-center justify-between text-xs text-gray-400">
                    <div class="flex items-center space-x-2">
                      <Clock size="14" />
                      <span>{{ record.timestamp }}</span>
                    </div>
                    <div class="flex items-center space-x-2">
                      <User size="14" />
                      <span>AI助教</span>
                    </div>
                  </div>
                </div>
              </v-card-item>
              <v-card-actions class="border-t border-gray-100">
                <v-btn
                  text
                  color="blue"
                  size="small"
                >
                  查看详情
                </v-btn>
                <v-btn
                  text
                  color="gray"
                  size="small"
                  class="ml-auto"
                >
                  相关视频: {{ record.videoTitle }}
                </v-btn>
              </v-card-actions>
            </v-card>
          </div>
        </div>
      </div>
    </v-main>
  </v-app>
</template>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.gradient-blue-purple {
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
}
</style>
