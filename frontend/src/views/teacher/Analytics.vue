<template>
  <div class="teacher-home">
    <!-- Left Sidebar -->
    <aside class="sidebar" :class="{ 'collapsed': isSidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <GraduationCap :size="24" class="logo-icon" />
          <h1 v-if="!isSidebarCollapsed">ZhiKe</h1>
          <div class="logo-dot" v-if="!isSidebarCollapsed"></div>
        </div>
        <div class="role-badge" v-if="!isSidebarCollapsed">
          教师
        </div>
      </div>
      <nav class="sidebar-nav" aria-label="主导航">
        <ul class="nav-list">
          <li class="nav-item">
            <a href="#" class="nav-link">
              <BarChart3 :size="20" />
              <span v-if="!isSidebarCollapsed">仪表盘</span>
            </a>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link">
              <BookOpen :size="20" />
              <span v-if="!isSidebarCollapsed">课程管理</span>
            </a>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link">
              <Video :size="20" />
              <span v-if="!isSidebarCollapsed">视频管理</span>
            </a>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link">
              <FileText :size="20" />
              <span v-if="!isSidebarCollapsed">知识片段</span>
            </a>
          </li>
          <li class="nav-item active">
            <a href="#" class="nav-link">
              <BarChart3 :size="20" />
              <span v-if="!isSidebarCollapsed">学情分析</span>
            </a>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link">
              <Settings :size="20" />
              <span v-if="!isSidebarCollapsed">系统设置</span>
            </a>
          </li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <button 
          class="collapse-button" 
          @click="toggleSidebar"
          :aria-label="isSidebarCollapsed ? '展开侧边栏' : '折叠侧边栏'"
        >
          <ChevronLeft v-if="!isSidebarCollapsed" :size="20" />
          <ChevronRight v-else :size="20" />
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
      <!-- Top Navigation -->
      <header class="top-nav">
        <div class="nav-container">
          <div class="nav-left">
            <h1 class="page-title">学情分析</h1>
          </div>
          <div class="nav-right">
            <button
              class="nav-button theme-toggle"
              @click="toggleTheme"
              aria-label="切换主题"
            >
              <Sun v-if="isDarkTheme" :size="20" />
              <Moon v-else :size="20" />
            </button>
            <button
              class="nav-button notification"
              @click="toggleNotifications"
              aria-label="通知"
            >
              <Bell :size="20" />
              <span v-if="unreadNotifications > 0" class="notification-badge" :aria-label="`${unreadNotifications} 条未读通知`">{{ unreadNotifications }}</span>
            </button>
            <div class="nav-user-menu">
              <div class="nav-user-avatar">
                <span>{{ userNameInitial }}</span>
              </div>
              <span class="nav-user-name">{{ userName }}</span>
            </div>
            <button class="nav-button logout" @click="handleLogout" aria-label="退出登录">
              <LogOut :size="20" />
            </button>
          </div>
        </div>
      </header>

      <!-- Analytics Content -->
      <section class="analytics-section">
        <!-- Top Operation Bar -->
        <div class="top-operation-bar" role="group" aria-label="分析操作">
          <div class="operation-left">
            <div class="course-selector">
              <label for="course-select" class="sr-only">选择课程</label>
              <select id="course-select" v-model="selectedCourse" class="select-input">
                <option value="">请选择课程</option>
                <option value="1">机器学习基础</option>
                <option value="2">深度学习进阶</option>
                <option value="3">计算机视觉</option>
                <option value="4">自然语言处理</option>
                <option value="5">强化学习</option>
              </select>
            </div>
            <div class="date-range">
              <label for="date-range" class="sr-only">选择日期范围</label>
              <input 
                type="text" 
                id="date-range" 
                v-model="dateRange" 
                placeholder="选择日期范围" 
                class="date-input"
              >
            </div>
          </div>
          <div class="operation-right">
            <button class="export-button" aria-label="导出分析报告">
              <Download :size="16" />
              <span>导出</span>
            </button>
          </div>
        </div>

        <!-- Chart Grid -->
        <div class="chart-grid">
          <!-- Heatmap -->
          <div class="chart-card heatmap-card" role="img" aria-label="难点热力图：共检测到 5 个难点区域，最高难度在 03:25">
            <h2 class="chart-title">视频难点热力图</h2>
            <div class="chart-container" ref="heatmapRef"></div>
          </div>

          <!-- KPI Cards -->
          <div class="chart-card kpi-card">
            <h2 class="chart-title">班级概览</h2>
            <div class="kpi-grid">
              <div class="kpi-item">
                <div class="kpi-value">{{ classOverview.avgCompletionRate * 100 }}%</div>
                <div class="kpi-label">完成率</div>
                <div class="kpi-chart" ref="completionRef"></div>
              </div>
              <div class="kpi-item">
                <div class="kpi-value">{{ classOverview.activeTodayCount }}</div>
                <div class="kpi-label">活跃学生</div>
                <div class="kpi-trend up">
                  <TrendingUp :size="16" />
                  <span>12%</span>
                </div>
              </div>
              <div class="kpi-item">
                <div class="kpi-value">{{ formatDuration(classOverview.avgWatchDuration) }}</div>
                <div class="kpi-label">平均观看时长</div>
              </div>
              <div class="kpi-item">
                <div class="kpi-value">{{ classOverview.totalQuestions }}</div>
                <div class="kpi-label">总提问数</div>
                <div class="kpi-trend up">
                  <TrendingUp :size="16" />
                  <span>8%</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Word Cloud -->
          <div class="chart-card wordcloud-card" role="img" aria-label="高频困惑词：导数(23次), 极限(18次), 积分(15次)...">
            <h2 class="chart-title">高频困惑词云</h2>
            <div class="chart-container" ref="wordcloudRef"></div>
          </div>

          <!-- Student Ranking -->
          <div class="chart-card ranking-card">
            <h2 class="chart-title">学习时长 Top 10</h2>
            <div class="chart-container" ref="rankingRef"></div>
            <div class="table-container">
              <table class="ranking-table">
                <thead>
                  <tr>
                    <th>排名</th>
                    <th>学生</th>
                    <th>学习时长</th>
                    <th>完成率</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(student, index) in topStudents" :key="student.userId">
                    <td>{{ index + 1 }}</td>
                    <td>{{ student.username }}</td>
                    <td>{{ formatDuration(student.totalWatchDuration) }}</td>
                    <td>{{ student.completionRate * 100 }}%</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useThemeStore } from '@/stores/theme';
import { useNotificationStore } from '@/stores/notification';
import { useUserStore } from '@/stores/user';
import {
  BarChart3,
  BookOpen,
  Video,
  FileText,
  TrendingUp,
  TrendingDown,
  Settings,
  ChevronLeft,
  ChevronRight,
  Sun,
  Moon,
  Bell,
  Download,
  Info,
  LogOut,
  GraduationCap
} from 'lucide-vue-next';
import * as echarts from 'echarts';
import { mockHeatmap, mockClassOverview, mockStudentProfiles, mockWordCloud } from '@/mocks/data/analytics.data';

const themeStore = useThemeStore();
const notificationStore = useNotificationStore();
const userStore = useUserStore();
const router = useRouter();

// State
const isSidebarCollapsed = ref(false);
const isNotificationsOpen = ref(false);
const selectedCourse = ref('1');
const dateRange = ref('2024-01-01 至 2024-01-31');

// Refs
const heatmapRef = ref<HTMLElement | null>(null);
const wordcloudRef = ref<HTMLElement | null>(null);
const rankingRef = ref<HTMLElement | null>(null);
const completionRef = ref<HTMLElement | null>(null);

// Charts
let heatmapChart: echarts.ECharts | null = null;
let wordcloudChart: echarts.ECharts | null = null;
let rankingChart: echarts.ECharts | null = null;
let completionChart: echarts.ECharts | null = null;

// Computed properties
const isDarkTheme = computed(() => themeStore.isDark);
const unreadNotifications = computed(() => notificationStore.unreadCount);
const userName = computed(() => userStore.userInfo?.username || '教师');
const userEmail = computed(() => userStore.userInfo?.email || 'teacher@example.com');
const userNameInitial = computed(() => {
  const name = userName.value;
  return name.charAt(0).toUpperCase();
});

// Data
const classOverview = ref(mockClassOverview);
const heatmapData = ref(mockHeatmap(1));
const wordcloudData = ref(mockWordCloud(1));
const topStudents = ref(mockStudentProfiles.slice(0, 10));

// Methods
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

const toggleTheme = () => {
  themeStore.toggleTheme();
};

const toggleNotifications = () => {
  isNotificationsOpen.value = !isNotificationsOpen.value;
};

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
};

const formatDuration = (seconds: number): string => {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  if (hours > 0) {
    return `${hours}h ${minutes}m`;
  }
  return `${minutes}m`;
};

const initHeatmap = () => {
  if (!heatmapRef.value) return;
  
  heatmapChart = echarts.init(heatmapRef.value);
  
  const data = heatmapData.value.data.map((point, index) => [
    index,
    0,
    point.score
  ]);
  
  const option = {
    tooltip: {
      position: 'top',
      formatter: function(params: any) {
        const point = heatmapData.value.data[params.data[0]];
        const startTime = new Date(point.startTime * 1000).toISOString().substr(14, 5);
        return `时间: ${startTime}<br/>难度分数: ${point.score.toFixed(2)}<br/>困惑学生: ${Math.round(point.score * 100)}%`;
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: heatmapData.value.data.map((point, index) => {
        if (index % 10 === 0) {
          return new Date(point.startTime * 1000).toISOString().substr(14, 5);
        }
        return '';
      }),
      splitArea: {
        show: true
      }
    },
    yAxis: {
      type: 'category',
      data: ['学生密度'],
      splitArea: {
        show: true
      }
    },
    visualMap: {
      min: 0,
      max: 1,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '5%',
      inRange: {
        color: ['#3b82f6', '#eab308', '#ef4444']
      }
    },
    series: [
      {
        name: '难度',
        type: 'heatmap',
        data: data,
        label: {
          show: false
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  
  heatmapChart.setOption(option);
};

const initWordcloud = () => {
  if (!wordcloudRef.value) return;
  
  wordcloudChart = echarts.init(wordcloudRef.value);
  
  const data = wordcloudData.value.words.map(word => ({
    name: word.text,
    value: word.count
  }));
  
  const option = {
    tooltip: {
      formatter: function(params: any) {
        return `词频: ${params.value} 次`;
      }
    },
    series: [
      {
        type: 'wordCloud',
        shape: 'circle',
        left: 'center',
        top: 'center',
        width: '90%',
        height: '80%',
        right: null,
        bottom: null,
        sizeRange: [14, 64],
        rotationRange: [-45, 45],
        rotationStep: 45,
        gridSize: 8,
        drawOutOfBound: false,
        textStyle: {
          fontFamily: 'IBM Plex Sans, sans-serif',
          fontWeight: 'bold',
          color: function() {
            return 'rgb(' + [
              Math.round(Math.random() * 100 + 155),
              Math.round(Math.random() * 100 + 155),
              Math.round(Math.random() * 100 + 155)
            ].join(',') + ')';
          }
        },
        emphasis: {
          focus: 'self',
          textStyle: {
            shadowBlur: 10,
            shadowColor: '#333'
          }
        },
        data: data
      }
    ]
  };
  
  wordcloudChart.setOption(option);
};

const initRanking = () => {
  if (!rankingRef.value) return;
  
  rankingChart = echarts.init(rankingRef.value);
  
  const data = topStudents.value.map(student => ({
    name: student.username,
    value: student.totalWatchDuration
  }));
  
  const option = {
    tooltip: {
      formatter: function(params: any) {
        const student = topStudents.value.find(s => s.username === params.name);
        if (student) {
          return `${student.username}: ${formatDuration(student.totalWatchDuration)}, 完成率 ${student.completionRate * 100}%`;
        }
        return params.name;
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      axisLabel: {
        formatter: function(value: number) {
          return formatDuration(value);
        }
      }
    },
    yAxis: {
      type: 'category',
      data: data.map(item => item.name),
      axisLabel: {
        interval: 0
      }
    },
    series: [
      {
        data: data.map(item => item.value),
        type: 'bar',
        itemStyle: {
          color: '#4d4f46',
          borderRadius: [0, 4, 4, 0]
        }
      }
    ]
  };
  
  rankingChart.setOption(option);
};

const initCompletionChart = () => {
  if (!completionRef.value) return;
  
  completionChart = echarts.init(completionRef.value);
  
  const option = {
    series: [
      {
        type: 'gauge',
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 8,
        axisLine: {
          lineStyle: {
            width: 6,
            color: [
              [0.6, '#3b82f6'],
              [0.8, '#eab308'],
              [1, '#ef4444']
            ]
          }
        },
        pointer: {
          icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
          length: '12%',
          width: 20,
          offsetCenter: [0, '-60%'],
          itemStyle: {
            color: 'auto'
          }
        },
        axisTick: {
          length: 12,
          lineStyle: {
            color: 'auto',
            width: 2
          }
        },
        splitLine: {
          length: 20,
          lineStyle: {
            color: 'auto',
            width: 5
          }
        },
        axisLabel: {
          color: '#464646',
          fontSize: 12,
          distance: -60,
          formatter: function (value: number) {
            if (value === 0 || value === 100) {
              return value;
            }
            return '';
          }
        },
        title: {
          offsetCenter: [0, '-10%'],
          fontSize: 14
        },
        detail: {
          fontSize: 20,
          offsetCenter: [0, '10%'],
          valueAnimation: true,
          formatter: function (value: number) {
            return Math.round(value) + '%';
          },
          color: 'auto'
        },
        data: [
          {
            value: classOverview.value.avgCompletionRate * 100,
            name: '完成率'
          }
        ]
      }
    ]
  };
  
  completionChart.setOption(option);
};

const initCharts = () => {
  initHeatmap();
  initWordcloud();
  initRanking();
  initCompletionChart();
};

const handleResize = () => {
  heatmapChart?.resize();
  wordcloudChart?.resize();
  rankingChart?.resize();
  completionChart?.resize();
};

// Lifecycle
onMounted(() => {
  initCharts();
  window.addEventListener('resize', handleResize);
});

// Watch
watch(selectedCourse, (newCourse) => {
  // 更新数据
  heatmapData.value = mockHeatmap(Number(newCourse));
  wordcloudData.value = mockWordCloud(Number(newCourse));
  // 重新初始化图表
  initCharts();
});
</script>

<style scoped>
.teacher-home {
  display: flex;
  min-height: 100vh;
  background: var(--color-bg-canvas, #fdfdf8);
  color: var(--color-text-primary, #0f172a);
}

/* Sidebar */
.sidebar {
  width: 240px;
  background: var(--color-bg-canvas, #fdfdf8);
  border-right: 1px solid var(--color-border, #e2e8f0);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  z-index: 100;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  color: var(--color-accent, #6366f1);
  flex-shrink: 0;
}

.logo h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.logo-dot {
  width: 8px;
  height: 8px;
  background: var(--color-accent, #6366f1);
  border-radius: 50%;
  margin-left: 0.5rem;
}

.role-badge {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full, 9999px);
}

.sidebar-nav {
  flex: 1;
  padding: 16px 0;
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  margin-bottom: 4px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  text-decoration: none;
  color: var(--color-text-secondary, #64748b);
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  margin: 0 8px;
  position: relative;
}

.nav-link:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.nav-item.active .nav-link {
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-text-primary, #0f172a);
  border-left: 3px solid var(--color-accent, #6366f1);
  margin-left: 0;
  padding-left: 13px;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-sm, 4px);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.collapse-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

/* Main Content */
.main-content {
  flex: 1;
  margin-left: 240px;
  transition: margin-left 0.3s ease;
  min-height: 100vh;
}

.main-content.sidebar-collapsed {
  margin-left: 64px;
}

/* Top Navigation */
.top-nav {
  height: 64px;
  background: var(--color-bg-card, #ffffff);
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  position: sticky;
  top: 0;
  z-index: 90;
}

.nav-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-left {
  display: flex;
  align-items: center;
}

.page-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  color: var(--color-text-primary, #0f172a);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-button {
  background: none;
  border: none;
  color: var(--color-text-secondary, #64748b);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: var(--radius-md, 8px);
  transition: all 0.2s ease;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button:hover {
  background: var(--color-bg-hover, #f1f5f9);
  color: var(--color-text-primary, #0f172a);
}

.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  background: var(--color-danger, #ef4444);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-user-menu {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px 4px 4px;
  border-radius: var(--radius-full, 9999px);
  background: var(--color-bg-elevated, #f8fafc);
  cursor: default;
}

.nav-user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-accent, #6366f1);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.875rem;
}

.nav-user-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--color-text-primary, #0f172a);
}

.nav-button.logout:hover {
  color: var(--color-danger, #ef4444);
  background: var(--color-danger-bg, rgba(239, 68, 68, 0.1));
}

/* Analytics Section */
.analytics-section {
  padding: 24px;
  background: var(--color-bg-canvas, #fdfdf8);
}

/* Top Operation Bar */
.top-operation-bar {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-sm, 4px);
  padding: 16px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.operation-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.course-selector, .date-range {
  display: flex;
  align-items: center;
}

.select-input, .date-input {
  background: var(--color-bg-input, #eeefe9);
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  padding: 8px 12px;
  font-size: 0.875rem;
  color: var(--color-text-primary, #4d4f46);
  transition: all 0.2s ease;
}

.select-input:focus, .date-input:focus {
  outline: none;
  border-color: var(--color-border-focus, #4d4f46);
  box-shadow: 0 0 0 3px var(--color-accent-subtle, rgba(245, 78, 0, 0.08));
}

.export-button {
  background: var(--color-cta-bg, #1e1f23);
  color: var(--color-cta-text, #ffffff);
  border: none;
  border-radius: var(--radius-sm, 4px);
  padding: 8px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.export-button:hover {
  background: #333;
  transform: translateY(-1px);
}

/* Chart Grid */
.chart-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  height: calc(100vh - 200px);
}

.chart-card {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: var(--color-text-primary, #4d4f46);
  font-family: 'IBM Plex Sans', sans-serif;
}

.chart-container {
  flex: 1;
  min-height: 0;
}

/* KPI Grid */
.kpi-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 12px;
  flex: 1;
}

.kpi-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--color-border, #bfc1b7);
  border-radius: var(--radius-sm, 4px);
  padding: 16px;
}

.kpi-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text-primary, #4d4f46);
  font-family: 'IBM Plex Sans', sans-serif;
  margin-bottom: 4px;
}

.kpi-label {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #65675e);
  margin-bottom: 8px;
}

.kpi-chart {
  width: 80px;
  height: 40px;
}

.kpi-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.75rem;
  font-weight: 600;
}

.kpi-trend.up {
  color: var(--color-success, #16a34a);
}

.kpi-trend.down {
  color: var(--color-danger, #dc2626);
}

/* Word Cloud */
.wordcloud-card {
  grid-column: 1 / 2;
  grid-row: 2 / 3;
}

/* Ranking */
.ranking-card {
  grid-column: 2 / 3;
  grid-row: 2 / 3;
}

.table-container {
  margin-top: 16px;
  overflow-x: auto;
}

.ranking-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.875rem;
}

.ranking-table th, .ranking-table td {
  padding: 8px 12px;
  text-align: left;
  border-bottom: 1px solid var(--color-border, #bfc1b7);
}

.ranking-table th {
  background: var(--color-bg-elevated, #eeefe9);
  font-weight: 600;
  color: var(--color-text-primary, #4d4f46);
}

.ranking-table tr:hover {
  background: var(--color-bg-hover, rgba(0,0,0,0.03));
}

/* Responsive Design */
@media (max-width: 1279px) {
  .chart-grid {
    grid-template-columns: 2fr 1fr;
    grid-template-rows: 1fr 1fr;
  }
}

@media (max-width: 1023px) {
  .sidebar {
    width: 64px;
  }
  
  .sidebar.collapsed {
    width: 0;
  }
  
  .main-content {
    margin-left: 64px;
  }
  
  .main-content.sidebar-collapsed {
    margin-left: 0;
  }
  
  .chart-grid {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto auto;
    height: auto;
  }
  
  .heatmap-card,
  .kpi-card,
  .wordcloud-card,
  .ranking-card {
    grid-column: 1 / 2;
  }
  
  .heatmap-card {
    grid-row: 1 / 2;
  }
  
  .kpi-card {
    grid-row: 2 / 3;
  }
  
  .wordcloud-card {
    grid-row: 3 / 4;
  }
  
  .ranking-card {
    grid-row: 4 / 5;
  }
  
  .kpi-grid {
    grid-template-columns: repeat(4, 1fr);
    grid-template-rows: 1fr;
  }
}

@media (max-width: 767px) {
  .top-operation-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .operation-left {
    flex-direction: column;
    align-items: stretch;
  }
  
  .select-input, .date-input {
    width: 100%;
  }
  
  .kpi-grid {
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: repeat(2, 1fr);
  }
}

/* Accessibility */
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}
</style>