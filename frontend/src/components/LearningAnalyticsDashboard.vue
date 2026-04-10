<template>
  <div class="learning-analytics-dashboard">
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import * as echarts from 'echarts';
import { Download, TrendingUp } from 'lucide-vue-next';
import { mockHeatmap, mockClassOverview, mockStudentProfiles, mockWordCloud } from '@/mocks/data/analytics.data';

// Props
const props = defineProps<{
  courseId?: string;
}>();

// Emits
const emit = defineEmits<{
  (e: 'courseChange', courseId: string): void;
}>();

// State
const selectedCourse = ref(props.courseId || '1');
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

// Data
const classOverview = ref(mockClassOverview);
const heatmapData = ref(mockHeatmap(Number(selectedCourse.value)));
const wordcloudData = ref(mockWordCloud(Number(selectedCourse.value)));
const topStudents = ref(mockStudentProfiles.slice(0, 10));

// Methods
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
  emit('courseChange', newCourse);
  // 更新数据
  heatmapData.value = mockHeatmap(Number(newCourse));
  wordcloudData.value = mockWordCloud(Number(newCourse));
  // 重新初始化图表
  initCharts();
});
</script>

<style scoped>
.learning-analytics-dashboard {
  width: 100%;
  background: var(--color-bg-canvas, #fdfdf8);
  color: var(--color-text-primary, #0f172a);
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