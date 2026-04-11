<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { GraduationCap, User, Plus, Edit, Delete, Search, Filter, Download, ArrowLeft, Check, X, Eye } from 'lucide-vue-next'

const router = useRouter()

// 用户列表
const users = ref([
  { id: 1, name: '张三', email: 'zhangsan@example.com', role: 'student', status: 'active', createdAt: '2023-10-01' },
  { id: 2, name: '李四', email: 'lisi@example.com', role: 'student', status: 'active', createdAt: '2023-10-02' },
  { id: 3, name: '王五', email: 'wangwu@example.com', role: 'teacher', status: 'active', createdAt: '2023-10-03' },
  { id: 4, name: '赵六', email: 'zhaoliu@example.com', role: 'student', status: 'inactive', createdAt: '2023-10-04' },
  { id: 5, name: '钱七', email: 'qianqi@example.com', role: 'admin', status: 'active', createdAt: '2023-10-05' }
])

// 搜索和筛选
const searchQuery = ref('')
const roleFilter = ref('all')
const statusFilter = ref('all')

// 模态框状态
const dialog = ref(false)
const dialogType = ref('add') // add, edit, view
const selectedUser = ref(null)

// 表单数据
const userForm = reactive({
  name: '',
  email: '',
  role: 'student',
  status: 'active'
})

// 验证规则
const rules = {
  required: (value: string) => !!value || '此项为必填',
  email: (value: string) => {
    const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/
    return pattern.test(value) || '请输入有效的邮箱地址'
  }
}

// 打开添加用户模态框
const openAddDialog = () => {
  dialogType.value = 'add'
  resetForm()
  dialog.value = true
}

// 打开编辑用户模态框
const openEditDialog = (user: any) => {
  dialogType.value = 'edit'
  selectedUser.value = user
  userForm.name = user.name
  userForm.email = user.email
  userForm.role = user.role
  userForm.status = user.status
  dialog.value = true
}

// 打开查看用户模态框
const openViewDialog = (user: any) => {
  dialogType.value = 'view'
  selectedUser.value = user
  userForm.name = user.name
  userForm.email = user.email
  userForm.role = user.role
  userForm.status = user.status
  dialog.value = true
}

// 关闭模态框
const closeDialog = () => {
  dialog.value = false
  resetForm()
}

// 重置表单
const resetForm = () => {
  userForm.name = ''
  userForm.email = ''
  userForm.role = 'student'
  userForm.status = 'active'
  selectedUser.value = null
}

// 保存用户
const saveUser = () => {
  if (dialogType.value === 'add') {
    // 添加新用户
    const newUser = {
      id: users.value.length + 1,
      name: userForm.name,
      email: userForm.email,
      role: userForm.role,
      status: userForm.status,
      createdAt: new Date().toISOString().split('T')[0]
    }
    users.value.push(newUser)
  } else if (dialogType.value === 'edit' && selectedUser.value) {
    // 编辑现有用户
    const index = users.value.findIndex(u => u.id === selectedUser.value.id)
    if (index !== -1) {
      users.value[index] = {
        ...users.value[index],
        name: userForm.name,
        email: userForm.email,
        role: userForm.role,
        status: userForm.status
      }
    }
  }
  closeDialog()
}

// 删除用户
const deleteUser = (userId: number) => {
  if (confirm('确定要删除这个用户吗？')) {
    users.value = users.value.filter(u => u.id !== userId)
  }
}

// 过滤用户列表
const filteredUsers = () => {
  return users.value.filter(user => {
    const matchesSearch = user.name.toLowerCase().includes(searchQuery.value.toLowerCase()) || 
                         user.email.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesRole = roleFilter.value === 'all' || user.role === roleFilter.value
    const matchesStatus = statusFilter.value === 'all' || user.status === statusFilter.value
    return matchesSearch && matchesRole && matchesStatus
  })
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
            <router-link to="/admin/dashboard" class="text-gray-600 hover:text-blue-600 transition-colors">仪表盘</router-link>
            <router-link to="/admin/user-management" class="text-blue-600 font-medium relative">
              用户管理
              <div class="absolute bottom-0 left-0 w-full h-0.5 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full"></div>
            </router-link>
            <router-link to="/admin/course-management" class="text-gray-600 hover:text-blue-600 transition-colors">课程管理</router-link>
            <router-link to="/admin/settings" class="text-gray-600 hover:text-blue-600 transition-colors">系统设置</router-link>
          </nav>
          
          <!-- 用户头像 -->
          <div class="flex items-center">
            <v-avatar color="gradient-blue-purple" size="40" class="cursor-pointer">
              <span class="text-white font-semibold">A</span>
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
          <h2 class="text-2xl font-bold text-gray-800">用户管理</h2>
        </div>

        <!-- 控制栏 -->
        <div class="flex flex-col md:flex-row items-stretch md:items-center space-y-4 md:space-y-0 md:space-x-4 mb-8">
          <!-- 搜索框 -->
          <v-text-field
            v-model="searchQuery"
            prepend-inner-icon="mdi:magnify"
            placeholder="搜索用户..."
            variant="outlined"
            density="compact"
            class="flex-1"
          />

          <!-- 角色筛选 -->
          <v-select
            v-model="roleFilter"
            :items="[
              { value: 'all', label: '所有角色' },
              { value: 'student', label: '学生' },
              { value: 'teacher', label: '教师' },
              { value: 'admin', label: '管理员' }
            ]"
            variant="outlined"
            density="compact"
            class="w-48"
          />

          <!-- 状态筛选 -->
          <v-select
            v-model="statusFilter"
            :items="[
              { value: 'all', label: '所有状态' },
              { value: 'active', label: '活跃' },
              { value: 'inactive', label: '未活跃' }
            ]"
            variant="outlined"
            density="compact"
            class="w-48"
          />

          <!-- 操作按钮 -->
          <div class="flex space-x-2 ml-auto">
            <v-btn
              variant="outlined"
              size="small"
              class="flex items-center space-x-2"
            >
              <Download :size="16" />
              <span>导出</span>
            </v-btn>
            <v-btn
              color="blue"
              variant="flat"
              size="small"
              class="flex items-center space-x-2"
              @click="openAddDialog"
            >
              <Plus :size="16" />
              <span>添加用户</span>
            </v-btn>
          </div>
        </div>

        <!-- 用户列表 -->
        <v-card class="border border-gray-100 mb-8">
          <v-card-text class="p-0">
            <v-data-table
              :headers="[
                { title: 'ID', key: 'id' },
                { title: '姓名', key: 'name' },
                { title: '邮箱', key: 'email' },
                { title: '角色', key: 'role' },
                { title: '状态', key: 'status' },
                { title: '创建时间', key: 'createdAt' },
                { title: '操作', key: 'actions' }
              ]"
              :items="filteredUsers()"
              hide-default-footer
              class="elevation-0"
            >
              <template v-slot:item.role="{ item }">
                <v-chip
                  :color="item.role === 'admin' ? 'purple' : item.role === 'teacher' ? 'blue' : 'green'"
                  size="small"
                >
                  {{ item.role === 'admin' ? '管理员' : item.role === 'teacher' ? '教师' : '学生' }}
                </v-chip>
              </template>
              <template v-slot:item.status="{ item }">
                <v-chip
                  :color="item.status === 'active' ? 'success' : 'error'"
                  size="small"
                >
                  {{ item.status === 'active' ? '活跃' : '未活跃' }}
                </v-chip>
              </template>
              <template v-slot:item.actions="{ item }">
                <div class="flex space-x-2">
                  <v-btn
                    icon
                    size="small"
                    color="primary"
                    @click="openViewDialog(item)"
                    title="查看"
                  >
                    <Eye :size="16" />
                  </v-btn>
                  <v-btn
                    icon
                    size="small"
                    color="warning"
                    @click="openEditDialog(item)"
                    title="编辑"
                  >
                    <Edit :size="16" />
                  </v-btn>
                  <v-btn
                    icon
                    size="small"
                    color="error"
                    @click="deleteUser(item.id)"
                    title="删除"
                  >
                    <Delete :size="16" />
                  </v-btn>
                </div>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>

        <!-- 分页 -->
        <div class="flex justify-center">
          <v-pagination
            v-model="page"
            :length="Math.ceil(users.value.length / itemsPerPage)"
            :items-per-page="itemsPerPage"
            class="mt-4"
          ></v-pagination>
        </div>
      </div>
    </v-main>

    <!-- 用户模态框 -->
    <v-dialog
      v-model="dialog"
      max-width="500px"
    >
      <v-card>
        <v-card-title class="text-lg font-semibold">
          {{ dialogType === 'add' ? '添加用户' : dialogType === 'edit' ? '编辑用户' : '查看用户' }}
        </v-card-title>
        <v-card-text>
          <v-form @submit.prevent="saveUser">
            <v-text-field
              v-model="userForm.name"
              label="姓名"
              variant="outlined"
              :rules="[rules.required]"
              :disabled="dialogType === 'view'"
              class="mb-4"
            />
            <v-text-field
              v-model="userForm.email"
              label="邮箱"
              variant="outlined"
              :rules="[rules.required, rules.email]"
              :disabled="dialogType === 'view'"
              class="mb-4"
            />
            <v-select
              v-model="userForm.role"
              :items="[
                { value: 'student', label: '学生' },
                { value: 'teacher', label: '教师' },
                { value: 'admin', label: '管理员' }
              ]"
              label="角色"
              variant="outlined"
              :disabled="dialogType === 'view'"
              class="mb-4"
            />
            <v-select
              v-model="userForm.status"
              :items="[
                { value: 'active', label: '活跃' },
                { value: 'inactive', label: '未活跃' }
              ]"
              label="状态"
              variant="outlined"
              :disabled="dialogType === 'view'"
              class="mb-4"
            />
          </v-form>
        </v-card-text>
        <v-card-actions class="justify-end space-x-2">
          <v-btn
            variant="outlined"
            @click="closeDialog"
          >
            取消
          </v-btn>
          <v-btn
            v-if="dialogType !== 'view'"
            color="blue"
            @click="saveUser"
          >
            保存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script lang="ts">
export default {
  data() {
    return {
      page: 1,
      itemsPerPage: 10
    }
  }
}
</script>

<style scoped>
.gradient-blue-purple {
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
}
</style>