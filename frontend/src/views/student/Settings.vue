<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { GraduationCap, User, Lock, Mail, Phone, Calendar, Save, ArrowLeft } from 'lucide-vue-next'

const router = useRouter()

// 个人信息
const personalInfo = reactive({
  name: '张三',
  email: 'zhangsan@example.com',
  phone: '13800138000',
  birthdate: '2000-01-01',
  studentId: '20200001',
  major: '计算机科学与技术'
})

// 密码修改
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 通知设置
const notificationSettings = reactive({
  emailNotifications: true,
  smsNotifications: false,
  pushNotifications: true
})

// 验证规则
const rules = {
  required: (value: string) => !!value || '此项为必填',
  email: (value: string) => {
    const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/
    return pattern.test(value) || '请输入有效的邮箱地址'
  },
  phone: (value: string) => {
    const pattern = /^1[3-9]\d{9}$/
    return pattern.test(value) || '请输入有效的手机号码'
  },
  password: (value: string) => {
    return value.length >= 6 || '密码长度至少为6位'
  },
  confirmPassword: (value: string) => {
    return value === passwordForm.newPassword || '两次输入的密码不一致'
  }
}

// 保存个人信息
const savePersonalInfo = () => {
  // 模拟保存操作
  alert('个人信息保存成功！')
}

// 保存密码
const savePassword = () => {
  // 模拟保存操作
  alert('密码修改成功！')
  // 清空表单
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

// 保存通知设置
const saveNotificationSettings = () => {
  // 模拟保存操作
  alert('通知设置保存成功！')
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
            <router-link to="/student/home" class="text-gray-600 hover:text-blue-600 transition-colors">首页</router-link>
            <router-link to="/student/course/1" class="text-gray-600 hover:text-blue-600 transition-colors">我的课程</router-link>
            <router-link to="/student/qa-history" class="text-gray-600 hover:text-blue-600 transition-colors">问答历史</router-link>
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
        <div class="flex items-center space-x-4 mb-8">
          <v-btn
            icon
            variant="text"
            @click="goBack"
            class="text-gray-600"
          >
            <ArrowLeft />
          </v-btn>
          <h2 class="text-2xl font-bold text-gray-800">设置</h2>
        </div>

        <!-- 设置选项卡 -->
        <v-tabs
          v-model="activeTab"
          show-arrows
          class="mb-8"
        >
          <v-tab>
            <User class="mr-2" :size="16" />
            个人信息
          </v-tab>
          <v-tab>
            <Lock class="mr-2" :size="16" />
            密码修改
          </v-tab>
          <v-tab>
            <Mail class="mr-2" :size="16" />
            通知设置
          </v-tab>
        </v-tabs>

        <!-- 个人信息设置 -->
        <v-tab-item value="0">
          <v-card class="border border-gray-100">
            <v-card-item>
              <div class="flex-1">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">个人基本信息</h3>
                <v-form @submit.prevent="savePersonalInfo">
                  <v-grid cols="12" :sm="2" gap="4">
                    <v-col>
                      <v-text-field
                        v-model="personalInfo.name"
                        label="姓名"
                        variant="outlined"
                        :rules="[rules.required]"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="personalInfo.studentId"
                        label="学号"
                        variant="outlined"
                        disabled
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="personalInfo.email"
                        label="邮箱"
                        variant="outlined"
                        :rules="[rules.required, rules.email]"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="personalInfo.phone"
                        label="手机号码"
                        variant="outlined"
                        :rules="[rules.required, rules.phone]"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="personalInfo.birthdate"
                        label="出生日期"
                        variant="outlined"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="personalInfo.major"
                        label="专业"
                        variant="outlined"
                      />
                    </v-col>
                  </v-grid>
                  <div class="mt-6 flex justify-end">
                    <v-btn
                      color="blue"
                      type="submit"
                      class="flex items-center space-x-2"
                    >
                      <Save :size="16" />
                      <span>保存修改</span>
                    </v-btn>
                  </div>
                </v-form>
              </div>
            </v-card-item>
          </v-card>
        </v-tab-item>

        <!-- 密码修改设置 -->
        <v-tab-item value="1">
          <v-card class="border border-gray-100">
            <v-card-item>
              <div class="flex-1">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">修改密码</h3>
                <v-form @submit.prevent="savePassword">
                  <v-grid cols="12" gap="4">
                    <v-col>
                      <v-text-field
                        v-model="passwordForm.currentPassword"
                        label="当前密码"
                        type="password"
                        variant="outlined"
                        :rules="[rules.required]"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="passwordForm.newPassword"
                        label="新密码"
                        type="password"
                        variant="outlined"
                        :rules="[rules.required, rules.password]"
                      />
                    </v-col>
                    <v-col>
                      <v-text-field
                        v-model="passwordForm.confirmPassword"
                        label="确认新密码"
                        type="password"
                        variant="outlined"
                        :rules="[rules.required, rules.confirmPassword]"
                      />
                    </v-col>
                  </v-grid>
                  <div class="mt-6 flex justify-end">
                    <v-btn
                      color="blue"
                      type="submit"
                      class="flex items-center space-x-2"
                    >
                      <Save :size="16" />
                      <span>修改密码</span>
                    </v-btn>
                  </div>
                </v-form>
              </div>
            </v-card-item>
          </v-card>
        </v-tab-item>

        <!-- 通知设置 -->
        <v-tab-item value="2">
          <v-card class="border border-gray-100">
            <v-card-item>
              <div class="flex-1">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">通知设置</h3>
                <v-form @submit.prevent="saveNotificationSettings">
                  <v-checkbox
                    v-model="notificationSettings.emailNotifications"
                    label="邮件通知"
                    class="mb-4"
                  />
                  <v-checkbox
                    v-model="notificationSettings.smsNotifications"
                    label="短信通知"
                    class="mb-4"
                  />
                  <v-checkbox
                    v-model="notificationSettings.pushNotifications"
                    label="推送通知"
                    class="mb-6"
                  />
                  <div class="flex justify-end">
                    <v-btn
                      color="blue"
                      type="submit"
                      class="flex items-center space-x-2"
                    >
                      <Save :size="16" />
                      <span>保存设置</span>
                    </v-btn>
                  </div>
                </v-form>
              </div>
            </v-card-item>
          </v-card>
        </v-tab-item>
      </div>
    </v-main>
  </v-app>
</template>

<script lang="ts">
export default {
  data() {
    return {
      activeTab: 0
    }
  }
}
</script>

<style scoped>
.gradient-blue-purple {
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
}
</style>