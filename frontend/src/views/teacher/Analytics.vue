<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { GraduationCap, BarChart3, Users, TrendingUp, Calendar, Filter, Download, ArrowLeft } from 'lucide-vue-next'

const router = useRouter()

// 时间范围
const timeRange = ref('month')

// 课程分析数据
const courseAnalytics = ref({
  totalCourses: 12,
  totalStudents: 156,
  totalHours: 86,
  completionRate: 78
})

// 课程趋势数据
const courseTrendData = ref([
  { month: '1月', courses: 2, students: 15 },
  { month: '2月', courses: 1, students: 12 },
  { month: '3月', courses: 3, students: 28 },
  { month: '4月', courses: 2, students: 22 },
  { month: '5月', courses: 1, students: 18 },
  { month: '6月', courses: 3, students: 61 }
])

// 学生活跃度数据
const studentActivityData = ref([
  { name: '周一', value: 65 },
  { name: '周二', value: 59 },
  { name: '周三', value: 80 },
  { name: '周四', value: 81 },
  { name: '周五', value: 56 },
  { name: '周六', value: 30 },
  { name: '周日', value: 20 }
])

// 课程完成率数据
const courseCompletionData = ref([
  { name: '前端开发基础', completion: 85 },
  { name: 'JavaScript进阶', completion: 65 },
  { name: 'Vue3实战', completion: 45 },
  { name: 'React基础', completion: 70 },
  { name: 'TypeScript实战', completion: 60 }
])

// 学生进步数据
const studentProgressData = ref([
  { name: '张三', progress: 90 },
  { name: '李四', progress: 75 },
  { name: '王五', progress: 85 },
  { name: '赵六', progress: 65 },
  { name: '钱七', progress: 80 }
])

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
            <router-link to="/teacher/analytics" class="text-blue-600 font-medium relative">
              分析
              <div class="absolute bottom-0 left-0 w-full h-0.5 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full"></div>
            </router-link>
            <router-link to="/teacher/knowledge-graph" class="text-gray-600 hover:text-blue-600 transition-colors">知识图谱</router-link>
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
          <h2 class="text-2xl font-bold text-gray-800">数据分析</h2>
        </div>

        <!-- 筛选栏 -->
        <div class="flex flex-col md:flex-row items-stretch md:items-center space-y-4 md:space-y-0 md:space-x-4 mb-8">
          <div class="flex flex-wrap gap-2">
            <v-btn
              v-for="range in ['week', 'month', 'quarter', 'year']"
              :key="range"
              :color="timeRange === range ? 'blue' : 'white'"
              :variant="timeRange === range ? 'flat' : 'outlined'"
              @click="timeRange = range"
              size="small"
              rounded="full"
              class="text-sm"
            >
              {{ range === 'week' ? '本周' : range === 'month' ? '本月' : range === 'quarter' ? '本季度' : '本年' }}
            </v-btn>
          </div>
          <div class="flex space-x-2 ml-auto">
            <v-btn
              variant="outlined"
              size="small"
              class="flex items-center space-x-2"
            >
              <Filter :size="16" />
              <span>筛选</span>
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

        <!-- 统计卡片 -->
        <v-grid cols="12" sm="2" md="4" gap="4" class="mb-8">
          <v-card class="border border-gray-100 hover:shadow-md transition-shadow">
            <v-card-item>
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <div>
                    <p class="text-sm text-gray-500">课程数量</p>
                    <h3 class="text-2xl font-bold mt-1">{{ courseAnalytics.totalCourses }}</h3>
                  </div>
                  <v-avatar
                    size="56"
                    class="bg-blue-100 text-blue-600"
                  >
                    <BarChart3 :size="28" />
                  </v-avatar>
                </div>
              </div>
            </v-card-item>
          </v-card>
          <v-card class="border border-gray-100 hover:shadow-md transition-shadow">
            <v-card-item>
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <div>
                    <p class="text-sm text-gray-500">学生数量</p>
                    <h3 class="text-2xl font-bold mt-1">{{ courseAnalytics.totalStudents }}</h3>
                  </div>
                  <v-avatar
                    size="56"
                    class="bg-purple-100 text-purple-600"
                  >
                    <Users :size="28" />
                  </v-avatar>
                </div>
              </div>
            </v-card-item>
          </v-card>
          <v-card class="border border-gray-100 hover:shadow-md transition-shadow">
            <v-card-item>
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <div>
                    <p class="text-sm text-gray-500">总课时</p>
                    <h3 class="text-2xl font-bold mt-1">{{ courseAnalytics.totalHours }}小时</h3>
                  </div>
                  <v-avatar
                    size="56"
                    class="bg-green-100 text-green-600"
                  >
                    <Calendar :size="28" />
                  </v-avatar>
                </div>
              </div>
            </v-card-item>
          </v-card>
          <v-card class="border border-gray-100 hover:shadow-md transition-shadow">
            <v-card-item>
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <div>
                    <p class="text-sm text-gray-500">完成率</p>
                    <h3 class="text-2xl font-bold mt-1">{{ courseAnalytics.completionRate }}%</h3>
                  </div>
                  <v-avatar
                    size="56"
                    class="bg-orange-100 text-orange-600"
                  >
                    <TrendingUp :size="28" />
                  </v-avatar>
                </div>
              </div>
            </v-card-item>
          </v-card>
        </v-grid>

        <!-- 图表区域 -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          <!-- 课程趋势图表 -->
          <v-card class="border border-gray-100">
            <v-card-title class="text-lg font-semibold">课程趋势</v-card-title>
            <v-card-text>
              <div class="h-64">
                <!-- 这里可以使用Chart.js或其他图表库 -->
                <div class="w-full h-full flex items-center justify-center">
                  <v-skeleton-loader type="card-text" :loading="true"></v-skeleton-loader>
                </div>
              </div>
            </v-card-text>
          </v-card>

          <!-- 学生活跃度图表 -->
          <v-card class="border border-gray-100">
            <v-card-title class="text-lg font-semibold">学生活跃度</v-card-title>
            <v-card-text>
              <div class="h-64">
                <!-- 这里可以使用Chart.js或其他图表库 -->
                <div class="w-full h-full flex items-center justify-center">
                  <v-skeleton-loader type="card-text" :loading="true"></v-skeleton-loader>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </div>

        <!-- 表格区域 -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <!-- 课程完成率表格 -->
          <v-card class="border border-gray-100">
            <v-card-title class="text-lg font-semibold">课程完成率</v-card-title>
            <v-card-text>
              <v-data-table
                :headers="[
                  { title: '课程名称', key: 'name' },
                  { title: '完成率', key: 'completion' },
                  { title: '状态', key: 'status' }
                ]"
                :items="courseCompletionData"
                hide-default-footer
                class="elevation-0"
              >
                <template v-slot:item.completion="{ item }">
                  <v-progress-linear
                    :value="item.completion"
                    color="primary"
                    height="8"
                    rounded
                    class="mb-1"
                  ></v-progress-linear>
                  <span class="text-xs text-gray-500">{{ item.completion }}%</span>
                </template>
                <template v-slot:item.status="{ item }">
                  <v-chip
                    :color="item.completion > 80 ? 'success' : item.completion > 60 ? 'warning' : 'error'"
                    size="small"
                  >
                    {{ item.completion > 80 ? '优秀' : item.completion > 60 ? '良好' : '需改进' }}
                  </v-chip>
                </template>
              </v-data-table>
            </v-card-text>
          </v-card>

          <!-- 学生进步表格 -->
          <v-card class="border border-gray-100">
            <v-card-title class="text-lg font-semibold">学生进步</v-card-title>
            <v-card-text>
              <v-data-table
                :headers="[
                  { title: '学生姓名', key: 'name' },
                  { title: '进步率', key: 'progress' },
                  { title: '状态', key: 'status' }
                ]"
                :items="studentProgressData"
                hide-default-footer
                class="elevation-0"
              >
                <template v-slot:item.progress="{ item }">
                  <v-progress-linear
                    :value="item.progress"
                    color="primary"
                    height="8"
                    rounded
                    class="mb-1"
                  ></v-progress-linear>
                  <span class="text-xs text-gray-500">{{ item.progress }}%</span>
                </template>
                <template v-slot:item.status="{ item }">
                  <v-chip
                    :color="item.progress > 85 ? 'success' : item.progress > 70 ? 'warning' : 'error'"
                    size="small"
                  >
                    {{ item.progress > 85 ? '优秀' : item.progress > 70 ? '良好' : '需努力' }}
                  </v-chip>
                </template>
              </v-data-table>
            </v-card-text>
          </v-card>
        </div>
      </div>
    </v-main>
  </v-app>
</template>

<style scoped>
.gradient-blue-purple {
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
}
</style>