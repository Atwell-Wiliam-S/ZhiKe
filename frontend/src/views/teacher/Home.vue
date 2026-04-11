<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 统计数据
const stats = ref([
  {
    title: '课程数量',
    value: 12,
    icon: 'mdi:book-open-variant',
    color: 'primary'
  },
  {
    title: '学生数量',
    value: 156,
    icon: 'mdi:account-group',
    color: 'secondary'
  },
  {
    title: '总课时',
    value: 86,
    icon: 'mdi:clock-outline',
    color: 'success'
  },
  {
    title: '完成率',
    value: '78%',
    icon: 'mdi:chart-line',
    color: 'warning'
  }
])

// 最近课程
const recentCourses = ref([
  {
    id: 1,
    title: '前端开发基础',
    students: 45,
    progress: 65,
    lastUpdated: '2023-10-15'
  },
  {
    id: 2,
    title: 'JavaScript进阶',
    students: 32,
    progress: 42,
    lastUpdated: '2023-10-10'
  },
  {
    id: 3,
    title: 'Vue3实战',
    students: 28,
    progress: 28,
    lastUpdated: '2023-10-05'
  }
])

// 最近活动
const recentActivities = ref([
  {
    id: 1,
    type: 'student',
    message: '张三完成了课程作业',
    time: '10分钟前'
  },
  {
    id: 2,
    type: 'course',
    message: '李四报名了前端开发基础课程',
    time: '30分钟前'
  },
  {
    id: 3,
    type: 'question',
    message: '王五提出了一个问题',
    time: '1小时前'
  },
  {
    id: 4,
    type: 'student',
    message: '赵六完成了课程学习',
    time: '2小时前'
  }
])

// 导航到课程详情
const navigateToCourse = (courseId: number) => {
  // 这里可以实现导航到课程详情的逻辑
  console.log('导航到课程:', courseId)
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
                { title: '首页', path: '/teacher/home' },
                { title: '分析', path: '/teacher/analytics' },
                { title: '知识图谱', path: '/teacher/knowledge-graph' },
                { title: '视频上传', path: '/teacher/video-upload' }
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
          :value="'/teacher/home'"
          class="hidden-sm-and-down"
          indicator-color="primary"
          text-color="primary"
          active-class="text-primary"
        >
          <v-tab :to="'/teacher/home'">首页</v-tab>
          <v-tab :to="'/teacher/analytics'">分析</v-tab>
          <v-tab :to="'/teacher/knowledge-graph'">知识图谱</v-tab>
          <v-tab :to="'/teacher/video-upload'">视频上传</v-tab>
        </v-tabs>
        
        <v-spacer></v-spacer>
        
        <v-avatar
          size="40"
          :style="{
            background: 'linear-gradient(135deg, #6366F1 0%, #3B82F6 100%)'
          }"
        >
          <span class="text-white font-semibold">T</span>
        </v-avatar>
      </v-container>
    </v-app-bar>

    <!-- 主内容 -->
    <v-main class="pt-20">
      <v-container class="max-w-7xl py-8">
        <!-- 页面头部 -->
        <div class="mb-8">
          <h2 class="text-2xl font-bold text-gray-800">欢迎回来，张老师！</h2>
          <p class="text-gray-600">这是您的教师工作台</p>
        </div>

        <!-- 统计卡片 -->
        <v-grid
          cols="1"
          sm="2"
          md="4"
          gap="4"
          class="mb-8"
        >
          <v-card
            v-for="stat in stats"
            :key="stat.title"
            variant="outlined"
            :style="{ borderRadius: '16px' }"
          >
            <v-card-text class="pt-6">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-sm text-gray-500">{{ stat.title }}</p>
                  <h3 class="text-2xl font-bold mt-1">{{ stat.value }}</h3>
                </div>
                <v-avatar
                  size="56"
                  :style="{
                    background: `var(--v-${stat.color}-lighten2)`,
                    color: `var(--v-${stat.color}-base)`
                  }"
                >
                  <v-icon size="28">{{ stat.icon }}</v-icon>
                </v-avatar>
              </div>
            </v-card-text>
          </v-card>
        </v-grid>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <!-- 最近课程 -->
          <div class="lg:col-span-2">
            <v-card variant="outlined" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">最近课程</v-card-title>
              <v-card-text>
                <v-data-table
                  :headers="[
                    { title: '课程名称', key: 'title' },
                    { title: '学生数', key: 'students' },
                    { title: '进度', key: 'progress' },
                    { title: '最后更新', key: 'lastUpdated' },
                    { title: '操作', key: 'actions' }
                  ]"
                  :items="recentCourses"
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
                  <template v-slot:item.actions="{ item }">
                    <v-btn
                      color="primary"
                      variant="text"
                      size="small"
                      @click="navigateToCourse(item.id)"
                    >
                      查看
                    </v-btn>
                  </template>
                </v-data-table>
              </v-card-text>
            </v-card>
          </div>

          <!-- 最近活动 -->
          <div class="lg:col-span-1">
            <v-card variant="outlined" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">最近活动</v-card-title>
              <v-card-text>
                <div v-if="recentActivities.length === 0" class="text-center py-4 text-gray-500">
                  暂无活动
                </div>
                <div v-else class="space-y-4">
                  <v-card
                    v-for="activity in recentActivities"
                    :key="activity.id"
                    variant="outlined"
                    class="p-3"
                    :style="{ borderRadius: '12px' }"
                  >
                    <div class="flex items-start">
                      <v-avatar
                        size="40"
                        class="mr-3"
                        :style="{
                          background: activity.type === 'student' ? '#6366F120' : 
                                    activity.type === 'course' ? '#10B98120' : '#F59E0B20',
                          color: activity.type === 'student' ? '#6366F1' : 
                                  activity.type === 'course' ? '#10B981' : '#F59E0B'
                        }"
                      >
                        <v-icon size="20">
                          {{ activity.type === 'student' ? 'mdi:account' : 
                             activity.type === 'course' ? 'mdi:book-open-variant' : 'mdi:message-text' }}
                        </v-icon>
                      </v-avatar>
                      <div>
                        <p class="text-sm">{{ activity.message }}</p>
                        <p class="text-xs text-gray-500 mt-1">{{ activity.time }}</p>
                      </div>
                    </div>
                  </v-card>
                </div>
              </v-card-text>
            </v-card>

            <!-- 快捷操作 -->
            <v-card variant="outlined" class="mt-6" :style="{ borderRadius: '16px' }">
              <v-card-title class="text-lg font-semibold">快捷操作</v-card-title>
              <v-card-text>
                <v-list density="compact">
                  <v-list-item
                    link
                    :to="'/teacher/video-upload'"
                    class="mb-2 rounded-lg hover:bg-gray-50"
                  >
                    <v-list-item-avatar class="mr-3">
                      <v-icon color="primary">mdi:upload</v-icon>
                    </v-list-item-avatar>
                    <v-list-item-title>上传视频</v-list-item-title>
                  </v-list-item>
                  <v-list-item
                    link
                    :to="'/teacher/analytics'"
                    class="mb-2 rounded-lg hover:bg-gray-50"
                  >
                    <v-list-item-avatar class="mr-3">
                      <v-icon color="primary">mdi:chart-line</v-icon>
                    </v-list-item-avatar>
                    <v-list-item-title>查看分析</v-list-item-title>
                  </v-list-item>
                  <v-list-item
                    link
                    :to="'/teacher/knowledge-graph'"
                    class="rounded-lg hover:bg-gray-50"
                  >
                    <v-list-item-avatar class="mr-3">
                      <v-icon color="primary">mdi:graph</v-icon>
                    </v-list-item-avatar>
                    <v-list-item-title>知识图谱</v-list-item-title>
                  </v-list-item>
                </v-list>
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
