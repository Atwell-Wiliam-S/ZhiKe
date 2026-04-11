<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { GraduationCap, Network, Book, Search, Filter, Download, ArrowLeft, ZoomIn, ZoomOut, RefreshCw, Settings } from 'lucide-vue-next'

const router = useRouter()

// 知识图谱数据
const knowledgeGraphData = ref({
  nodes: [
    { id: 1, label: '前端开发', category: 'topic', size: 30 },
    { id: 2, label: 'HTML', category: 'technology', size: 20 },
    { id: 3, label: 'CSS', category: 'technology', size: 20 },
    { id: 4, label: 'JavaScript', category: 'technology', size: 25 },
    { id: 5, label: 'Vue', category: 'framework', size: 22 },
    { id: 6, label: 'React', category: 'framework', size: 22 },
    { id: 7, label: 'TypeScript', category: 'language', size: 20 },
    { id: 8, label: 'CSS3', category: 'technology', size: 18 },
    { id: 9, label: 'ES6', category: 'language', size: 18 },
    { id: 10, label: 'Vue3', category: 'framework', size: 18 },
    { id: 11, label: 'React Hooks', category: 'framework', size: 18 },
    { id: 12, label: 'Responsive Design', category: 'concept', size: 16 },
    { id: 13, label: 'Component Design', category: 'concept', size: 16 },
    { id: 14, label: 'State Management', category: 'concept', size: 16 },
    { id: 15, label: 'Vuex', category: 'library', size: 14 },
    { id: 16, label: 'Redux', category: 'library', size: 14 },
    { id: 17, label: 'Axios', category: 'library', size: 14 },
    { id: 18, label: 'Fetch API', category: 'api', size: 14 },
    { id: 19, label: 'CSS Grid', category: 'technology', size: 14 },
    { id: 20, label: 'Flexbox', category: 'technology', size: 14 }
  ],
  links: [
    { source: 1, target: 2 },
    { source: 1, target: 3 },
    { source: 1, target: 4 },
    { source: 1, target: 5 },
    { source: 1, target: 6 },
    { source: 1, target: 7 },
    { source: 3, target: 8 },
    { source: 4, target: 9 },
    { source: 5, target: 10 },
    { source: 6, target: 11 },
    { source: 1, target: 12 },
    { source: 1, target: 13 },
    { source: 1, target: 14 },
    { source: 5, target: 15 },
    { source: 6, target: 16 },
    { source: 4, target: 17 },
    { source: 4, target: 18 },
    { source: 3, target: 19 },
    { source: 3, target: 20 },
    { source: 13, target: 5 },
    { source: 13, target: 6 },
    { source: 14, target: 15 },
    { source: 14, target: 16 }
  ]
})

// 筛选选项
const filters = ref({
  categories: ['topic', 'technology', 'framework', 'language', 'concept', 'library', 'api'],
  selectedCategory: 'all'
})

// 图谱控制
const zoomLevel = ref(1)

// 放大
const zoomIn = () => {
  zoomLevel.value += 0.1
}

// 缩小
const zoomOut = () => {
  if (zoomLevel.value > 0.5) {
    zoomLevel.value -= 0.1
  }
}

// 重置
const resetGraph = () => {
  zoomLevel.value = 1
}

// 应用筛选
const applyFilter = (category: string) => {
  filters.value.selectedCategory = category
}

// 返回上一页
const goBack = () => {
  router.back()
}
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
            <router-link to="/teacher/home" class="text-gray-600 hover:text-blue-600 transition-colors">首页</router-link>
            <router-link to="/teacher/analytics" class="text-gray-600 hover:text-blue-600 transition-colors">分析</router-link>
            <router-link to="/teacher/knowledge-graph" class="text-blue-600 font-medium relative">
              知识图谱
              <div class="absolute bottom-0 left-0 w-full h-0.5 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full"></div>
            </router-link>
            <router-link to="/teacher/video-upload" class="text-gray-600 hover:text-blue-600 transition-colors">视频上传</router-link>
          </nav>
          
          <!-- 用户头像 -->
          <div class="flex items-center">
            <v-avatar color="gradient-blue-purple" size="40" class="cursor-pointer">
              <span class="text-white font-semibold">T</span>
            </v-avatar>
          </div>
        </div>
      </div>
    </v-app-bar>

    <!-- 主内容区 -->
    <v-main>
      <div class="container mx-auto px-4 py-8">
        <!-- 页面标题 -->
        <div class="flex items-center space-x-4 mb-8">
          <v-btn
            icon
            variant="text"
            @click="goBack"
            class="text-gray-600"
          >
            <ArrowLeft />
          </v-btn>
          <h2 class="text-2xl font-bold text-gray-800">知识图谱</h2>
        </div>

        <!-- 控制栏 -->
        <div class="flex flex-col md:flex-row items-stretch md:items-center space-y-4 md:space-y-0 md:space-x-4 mb-8">
          <!-- 搜索框 -->
          <v-text-field
            prepend-inner-icon="mdi:magnify"
            placeholder="搜索知识点..."
            variant="outlined"
            density="compact"
            class="flex-1"
          />

          <!-- 分类筛选 -->
          <div class="flex flex-wrap gap-2">
            <v-btn
              :color="filters.selectedCategory === 'all' ? 'blue' : 'white'"
              :variant="filters.selectedCategory === 'all' ? 'flat' : 'outlined'"
              @click="applyFilter('all')"
              size="small"
              rounded="full"
              class="text-sm"
            >
              全部
            </v-btn>
            <v-btn
              v-for="category in filters.categories"
              :key="category"
              :color="filters.selectedCategory === category ? 'blue' : 'white'"
              :variant="filters.selectedCategory === category ? 'flat' : 'outlined'"
              @click="applyFilter(category)"
              size="small"
              rounded="full"
              class="text-sm"
            >
              {{ category === 'topic' ? '主题' : 
                 category === 'technology' ? '技术' : 
                 category === 'framework' ? '框架' : 
                 category === 'language' ? '语言' : 
                 category === 'concept' ? '概念' : 
                 category === 'library' ? '库' : 'API' }}
            </v-btn>
          </div>

          <!-- 操作按钮 -->
          <div class="flex space-x-2 ml-auto">
            <v-btn
              icon
              variant="outlined"
              size="small"
              @click="zoomIn"
              title="放大"
            >
              <ZoomIn :size="16" />
            </v-btn>
            <v-btn
              icon
              variant="outlined"
              size="small"
              @click="zoomOut"
              title="缩小"
            >
              <ZoomOut :size="16" />
            </v-btn>
            <v-btn
              icon
              variant="outlined"
              size="small"
              @click="resetGraph"
              title="重置"
            >
              <RefreshCw :size="16" />
            </v-btn>
            <v-btn
              icon
              variant="outlined"
              size="small"
              title="设置"
            >
              <Settings :size="16" />
            </v-btn>
            <v-btn
              color="blue"
              variant="flat"
              size="small"
              class="flex items-center space-x-2"
            >
              <Download :size="16" />
              <span>导出</span>
            </v-btn>
          </div>
        </div>

        <!-- 知识图谱展示区域 -->
        <v-card class="border border-gray-100 mb-8">
          <v-card-text class="p-0">
            <div class="relative w-full h-[600px] bg-gray-50 border border-gray-200 rounded-lg overflow-hidden">
              <!-- 知识图谱可视化区域 -->
              <div 
                class="w-full h-full" 
                :style="{ transform: `scale(${zoomLevel})`, transformOrigin: 'center center' }"
              >
                <!-- 这里可以使用D3.js或其他图谱库实现 -->
                <div class="w-full h-full flex items-center justify-center">
                  <v-skeleton-loader type="card-text" :loading="true"></v-skeleton-loader>
                </div>
              </div>

              <!-- 图谱信息 -->
              <div class="absolute top-4 left-4 bg-white p-3 rounded-lg shadow-md">
                <h3 class="text-sm font-semibold mb-2">知识图谱信息</h3>
                <div class="text-xs space-y-1">
                  <div class="flex justify-between">
                    <span>节点数量:</span>
                    <span>{{ knowledgeGraphData.nodes.length }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span>连接数量:</span>
                    <span>{{ knowledgeGraphData.links.length }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span>当前缩放:</span>
                    <span>{{ Math.round(zoomLevel * 100) }}%</span>
                  </div>
                </div>
              </div>

              <!-- 图例 -->
              <div class="absolute bottom-4 right-4 bg-white p-3 rounded-lg shadow-md">
                <h3 class="text-sm font-semibold mb-2">图例</h3>
                <div class="text-xs space-y-1">
                  <div class="flex items-center space-x-2">
                    <div class="w-3 h-3 rounded-full bg-blue-500"></div>
                    <span>主题</span>
                  </div>
                  <div class="flex items-center space-x-2">
                    <div class="w-3 h-3 rounded-full bg-green-500"></div>
                    <span>技术</span>
                  </div>
                  <div class="flex items-center space-x-2">
                    <div class="w-3 h-3 rounded-full bg-purple-500"></div>
                    <span>框架</span>
                  </div>
                  <div class="flex items-center space-x-2">
                    <div class="w-3 h-3 rounded-full bg-orange-500"></div>
                    <span>语言</span>
                  </div>
                  <div class="flex items-center space-x-2">
                    <div class="w-3 h-3 rounded-full bg-red-500"></div>
                    <span>概念</span>
                  </div>
                </div>
              </div>
            </div>
          </v-card-text>
        </v-card>

        <!-- 相关课程推荐 -->
        <v-card class="border border-gray-100">
          <v-card-title class="text-lg font-semibold">相关课程推荐</v-card-title>
          <v-card-text>
            <v-data-table
              :headers="[
                { title: '课程名称', key: 'name' },
                { title: '知识点数量', key: 'topics' },
                { title: '学生数', key: 'students' },
                { title: '操作', key: 'actions' }
              ]"
              :items="[
                { id: 1, name: '前端开发基础', topics: 15, students: 45 },
                { id: 2, name: 'Vue3实战', topics: 12, students: 28 },
                { id: 3, name: 'React进阶', topics: 10, students: 22 },
                { id: 4, name: 'TypeScript实战', topics: 8, students: 18 }
              ]"
              hide-default-footer
              class="elevation-0"
            >
              <template v-slot:item.actions="{ item }">
                <v-btn
                  color="primary"
                  variant="text"
                  size="small"
                >
                  查看
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </div>
    </v-main>
  </v-app>
</template>

<style scoped>
.gradient-blue-purple {
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
}
</style>