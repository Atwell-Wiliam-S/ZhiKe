<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { GraduationCap, Upload, FileVideo, Book, Save, ArrowLeft, CheckCircle, Clock, Info, AlertCircle } from 'lucide-vue-next'

const router = useRouter()

// 上传状态
const uploadStatus = ref('idle') // idle, uploading, success, error
const uploadProgress = ref(0)

// 视频信息
const videoForm = reactive({
  title: '',
  description: '',
  course: '',
  chapter: '',
  duration: '',
  coverImage: null as any,
  videoFile: null as any
})

// 课程选项
const courses = ref([
  { id: 1, name: '前端开发基础' },
  { id: 2, name: 'JavaScript进阶' },
  { id: 3, name: 'Vue3实战' },
  { id: 4, name: 'React基础' },
  { id: 5, name: 'TypeScript实战' }
])

// 章节选项
const chapters = ref([
  { id: 1, name: '第一章：前端开发概述' },
  { id: 2, name: '第二章：HTML基础' },
  { id: 3, name: '第三章：CSS基础' },
  { id: 4, name: '第四章：JavaScript基础' },
  { id: 5, name: '第五章：前端框架' }
])

// 验证规则
const rules = {
  required: (value: string) => !!value || '此项为必填',
  file: (value: any) => !!value || '请选择视频文件',
  image: (value: any) => !!value || '请选择封面图片'
}

// 选择视频文件
const selectVideoFile = (event: any) => {
  const file = event.target.files[0]
  if (file) {
    videoForm.videoFile = file
    // 模拟获取视频时长
    const video = document.createElement('video')
    video.onloadedmetadata = () => {
      const duration = Math.floor(video.duration)
      const minutes = Math.floor(duration / 60)
      const seconds = duration % 60
      videoForm.duration = `${minutes}:${seconds.toString().padStart(2, '0')}`
    }
    video.src = URL.createObjectURL(file)
  }
}

// 选择封面图片
const selectCoverImage = (event: any) => {
  const file = event.target.files[0]
  if (file) {
    videoForm.coverImage = file
  }
}

// 开始上传
const startUpload = () => {
  uploadStatus.value = 'uploading'
  uploadProgress.value = 0
  
  // 模拟上传进度
  const interval = setInterval(() => {
    uploadProgress.value += 5
    if (uploadProgress.value >= 100) {
      clearInterval(interval)
      uploadStatus.value = 'success'
      // 模拟上传成功后的延迟
      setTimeout(() => {
        resetForm()
      }, 2000)
    }
  }, 200)
}

// 重置表单
const resetForm = () => {
  videoForm.title = ''
  videoForm.description = ''
  videoForm.course = ''
  videoForm.chapter = ''
  videoForm.duration = ''
  videoForm.coverImage = null
  videoForm.videoFile = null
  uploadStatus.value = 'idle'
  uploadProgress.value = 0
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
            <router-link to="/teacher/knowledge-graph" class="text-gray-600 hover:text-blue-600 transition-colors">知识图谱</router-link>
            <router-link to="/teacher/video-upload" class="text-blue-600 font-medium relative">
              视频上传
              <div class="absolute bottom-0 left-0 w-full h-0.5 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full"></div>
            </router-link>
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
          <h2 class="text-2xl font-bold text-gray-800">视频上传</h2>
        </div>

        <!-- 上传状态 -->
        <v-card class="border border-gray-100 mb-8" v-if="uploadStatus === 'uploading' || uploadStatus === 'success'">
          <v-card-item>
            <div class="flex-1">
              <div v-if="uploadStatus === 'uploading'">
                <div class="flex items-center justify-between mb-4">
                  <h3 class="text-lg font-semibold">正在上传视频</h3>
                  <span class="text-sm text-gray-500">{{ uploadProgress }}%</span>
                </div>
                <v-progress-linear
                  :value="uploadProgress"
                  color="primary"
                  height="10"
                  rounded
                  class="mb-4"
                ></v-progress-linear>
                <p class="text-sm text-gray-500">请不要关闭浏览器窗口，上传正在进行中...</p>
              </div>
              <div v-else-if="uploadStatus === 'success'" class="text-center py-8">
                <v-avatar
                  size="80"
                  class="bg-green-100 text-green-600 mx-auto mb-4"
                >
                  <CheckCircle :size="40" />
                </v-avatar>
                <h3 class="text-lg font-semibold mb-2">视频上传成功！</h3>
                <p class="text-gray-600 mb-4">视频已成功上传并处理完成</p>
                <v-btn
                  color="blue"
                  variant="flat"
                  @click="resetForm"
                >
                  上传新视频
                </v-btn>
              </div>
            </div>
          </v-card-item>
        </v-card>

        <!-- 上传表单 -->
        <v-card class="border border-gray-100" v-if="uploadStatus === 'idle' || uploadStatus === 'error'">
          <v-card-item>
            <div class="flex-1">
              <v-form @submit.prevent="startUpload">
                <!-- 视频文件上传 -->
                <div class="mb-6">
                  <v-file-input
                    label="视频文件"
                    accept="video/*"
                    variant="outlined"
                    :rules="[rules.file]"
                    @change="selectVideoFile"
                    class="mb-4"
                  />
                  <div class="flex items-center space-x-4 mb-4">
                    <v-avatar
                      size="80"
                      class="bg-gray-100 flex items-center justify-center"
                    >
                      <FileVideo :size="40" class="text-gray-400" />
                    </v-avatar>
                    <div v-if="videoForm.videoFile">
                      <p class="font-medium">{{ videoForm.videoFile.name }}</p>
                      <p class="text-sm text-gray-500">{{ (videoForm.videoFile.size / (1024 * 1024)).toFixed(2) }} MB</p>
                      <p class="text-sm text-gray-500" v-if="videoForm.duration">时长: {{ videoForm.duration }}</p>
                    </div>
                  </div>
                </div>

                <!-- 封面图片上传 -->
                <div class="mb-6">
                  <v-file-input
                    label="封面图片"
                    accept="image/*"
                    variant="outlined"
                    :rules="[rules.image]"
                    @change="selectCoverImage"
                    class="mb-4"
                  />
                  <div class="flex items-center space-x-4">
                    <v-avatar
                      size="80"
                      class="bg-gray-100 flex items-center justify-center"
                    >
                      <img v-if="videoForm.coverImage" :src="URL.createObjectURL(videoForm.coverImage)" class="w-full h-full object-cover rounded-full" />
                      <Info v-else :size="40" class="text-gray-400" />
                    </v-avatar>
                    <div v-if="videoForm.coverImage">
                      <p class="font-medium">{{ videoForm.coverImage.name }}</p>
                      <p class="text-sm text-gray-500">{{ (videoForm.coverImage.size / (1024 * 1024)).toFixed(2) }} MB</p>
                    </div>
                  </div>
                </div>

                <!-- 视频信息 -->
                <v-grid cols="12" sm="2" gap="4" class="mb-6">
                  <v-col>
                    <v-text-field
                      v-model="videoForm.title"
                      label="视频标题"
                      variant="outlined"
                      :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col>
                    <v-select
                      v-model="videoForm.course"
                      :items="courses"
                      item-title="name"
                      item-value="id"
                      label="所属课程"
                      variant="outlined"
                      :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col>
                    <v-select
                      v-model="videoForm.chapter"
                      :items="chapters"
                      item-title="name"
                      item-value="id"
                      label="所属章节"
                      variant="outlined"
                      :rules="[rules.required]"
                    />
                  </v-col>
                  <v-col>
                    <v-text-field
                      v-model="videoForm.duration"
                      label="视频时长"
                      variant="outlined"
                      disabled
                    />
                  </v-col>
                </v-grid>

                <div class="mb-6">
                  <v-textarea
                    v-model="videoForm.description"
                    label="视频描述"
                    variant="outlined"
                    rows="3"
                    :rules="[rules.required]"
                  />
                </div>

                <!-- 上传提示 -->
                <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                  <div class="flex items-start">
                    <Info class="text-blue-500 mr-3 mt-1" />
                    <div>
                      <h4 class="font-medium text-blue-800 mb-2">上传提示</h4>
                      <ul class="text-sm text-blue-700 space-y-1">
                        <li>• 支持的视频格式：MP4、WebM、MOV</li>
                        <li>• 视频大小不超过500MB</li>
                        <li>• 封面图片建议尺寸：1280×720像素</li>
                        <li>• 上传过程中请勿关闭浏览器窗口</li>
                      </ul>
                    </div>
                  </div>
                </div>

                <!-- 操作按钮 -->
                <div class="flex justify-end space-x-4">
                  <v-btn
                    variant="outlined"
                    @click="resetForm"
                  >
                    重置
                  </v-btn>
                  <v-btn
                    color="blue"
                    type="submit"
                    class="flex items-center space-x-2"
                  >
                    <Upload :size="16" />
                    <span>开始上传</span>
                  </v-btn>
                </div>
              </v-form>
            </div>
          </v-card-item>
        </v-card>

        <!-- 最近上传的视频 -->
        <v-card class="border border-gray-100 mt-8">
          <v-card-title class="text-lg font-semibold">最近上传的视频</v-card-title>
          <v-card-text>
            <v-data-table
              :headers="[
                { title: '视频标题', key: 'title' },
                { title: '所属课程', key: 'course' },
                { title: '上传时间', key: 'uploadTime' },
                { title: '状态', key: 'status' },
                { title: '操作', key: 'actions' }
              ]"
              :items="[
                { id: 1, title: '1.1 前端开发简介', course: '前端开发基础', uploadTime: '2023-10-15 14:30', status: '已发布' },
                { id: 2, title: '1.2 前端技术栈', course: '前端开发基础', uploadTime: '2023-10-14 10:20', status: '已发布' },
                { id: 3, title: '1.3 开发环境搭建', course: '前端开发基础', uploadTime: '2023-10-13 09:15', status: '审核中' }
              ]"
              hide-default-footer
              class="elevation-0"
            >
              <template v-slot:item.status="{ item }">
                <v-chip
                  :color="item.status === '已发布' ? 'success' : 'warning'"
                  size="small"
                >
                  {{ item.status }}
                </v-chip>
              </template>
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