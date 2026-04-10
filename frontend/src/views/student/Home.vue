<template>
  <div class="student-home">
    <!-- Top Navigation -->
    <StudentTopNav />

    <!-- Main Content -->
    <main class="main-content">
      <!-- Hero Section -->
      <section class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">Hi, {{ userName }}</h1>
          <p class="hero-subtitle">今天想学点什么？</p>
        </div>
      </section>

      <!-- Feature Cards Grid -->
      <section class="feature-section">
        <ul class="feature-grid" role="list">
          <li class="feature-card">
            <router-link to="/student/course/1" class="feature-link">
              <div class="feature-icon">
                <BookOpen :size="24" />
              </div>
              <h3 class="feature-title">我的课程</h3>
              <p class="feature-description">查看我已报名的所有课程</p>
              <div class="feature-arrow">
                <ChevronRight :size="16" />
              </div>
            </router-link>
          </li>
          <li class="feature-card">
            <router-link to="/student/learn/1" class="feature-link">
              <div class="feature-icon">
                <PlayCircle :size="24" />
              </div>
              <h3 class="feature-title">继续学习</h3>
              <p class="feature-description">从上次学习的地方继续</p>
              <div class="feature-progress">
                <div class="progress-bar">
                  <div class="progress-fill" style="width: 65%"></div>
                </div>
                <span class="progress-text">65% 完成</span>
              </div>
            </router-link>
          </li>
          <li class="feature-card">
            <router-link to="/student/learn/1" class="feature-link">
              <div class="feature-icon">
                <HelpCircle :size="24" />
              </div>
              <h3 class="feature-title">AI 助教</h3>
              <p class="feature-description">有问题随时问我</p>
              <div class="feature-status">
                <span class="status-dot"></span>
                <span class="status-text">在线</span>
              </div>
            </router-link>
          </li>
          <li class="feature-card">
            <router-link to="/student/notes" class="feature-link">
              <div class="feature-icon">
                <FileText :size="24" />
              </div>
              <h3 class="feature-title">我的笔记</h3>
              <p class="feature-description">查看我的学习笔记</p>
              <div class="feature-badge">12</div>
            </router-link>
          </li>
          <li class="feature-card">
            <a href="#" class="feature-link">
              <div class="feature-icon">
                <BarChart3 :size="24" />
              </div>
              <h3 class="feature-title">学习报告</h3>
              <p class="feature-description">查看本周学习情况</p>
              <div class="feature-badge">12.5h</div>
            </a>
          </li>
          <li class="feature-card">
            <router-link to="/settings" class="feature-link">
              <div class="feature-icon">
                <Settings :size="24" />
              </div>
              <h3 class="feature-title">设置</h3>
              <p class="feature-description">主题、个人信息</p>
              <div class="feature-arrow">
                <ChevronRight :size="16" />
              </div>
            </router-link>
          </li>
        </ul>
      </section>

      <!-- Recent Activity -->
      <section class="activity-section">
        <h2 class="section-title">最近活动</h2>
        <div class="activity-list">
          <div class="activity-item">
            <div class="activity-icon">
              <Shield :size="16" />
            </div>
            <div class="activity-content">
              <p class="activity-description">观看了视频：<strong>机器学习基础</strong></p>
              <span class="activity-time">2小时前</span>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <FileText :size="16" />
            </div>
            <div class="activity-content">
              <p class="activity-description">添加了笔记：<strong>监督学习算法</strong></p>
              <span class="activity-time">5小时前</span>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <Info :size="16" />
            </div>
            <div class="activity-content">
              <p class="activity-description">完成了测验：<strong>线性回归</strong></p>
              <span class="activity-time">昨天</span>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <BookOpen :size="16" />
            </div>
            <div class="activity-content">
              <p class="activity-description">报名了课程：<strong>深度学习进阶</strong></p>
              <span class="activity-time">2天前</span>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useUserStore } from '@/stores/user';
import { ChevronRight, BookOpen, PlayCircle, HelpCircle, FileText, BarChart3, Shield, Info, Settings } from 'lucide-vue-next';
import StudentTopNav from '@/components/ui/StudentTopNav.vue';

const userStore = useUserStore();

// Computed properties
const userName = computed(() => userStore.userInfo?.username || '学生');
</script>

<style scoped>
.student-home {
  min-height: 100vh;
  background: var(--color-bg-canvas, #f8fafc);
  color: var(--color-text-primary, #0f172a);
  display: flex;
  flex-direction: column;
}

/* Main Content */
.main-content {
  max-width: var(--container-2xl);
  margin: 0 auto;
  padding: 0 24px;
  width: 100%;
}

/* Hero Section */
.hero-section {
  padding: 48px 0 32px;
}

.hero-content {
  max-width: 600px;
}

.hero-title {
  font-size: 2.25rem;
  font-weight: 400;
  margin: 0 0 0.5rem;
  color: var(--color-text-primary, #0f172a);
}

.hero-subtitle {
  font-size: 1.125rem;
  color: var(--color-text-secondary, #64748b);
  margin: 0;
}

/* Feature Section */
.feature-section {
  margin-bottom: 48px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-card {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  padding: 24px;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.feature-card:hover {
  border-color: var(--color-accent, #6366f1);
  box-shadow: var(--shadow-glow, 0 0 20px rgba(99, 102, 241, 0.2));
  transform: translateY(-2px);
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(99, 102, 241, 0.1), transparent);
  transition: left 0.6s ease;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-link {
  display: block;
  text-decoration: none;
  color: var(--color-text-primary, #0f172a);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md, 8px);
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
}

.feature-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 0 0 0.5rem;
}

.feature-description {
  font-size: 0.875rem;
  color: var(--color-text-secondary, #64748b);
  margin: 0 0 1rem;
  flex: 1;
}

.feature-progress {
  margin-top: auto;
}

.progress-bar {
  height: 4px;
  background: var(--color-bg-input, #f1f5f9);
  border-radius: var(--radius-full, 9999px);
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background: var(--color-accent, #6366f1);
  border-radius: var(--radius-full, 9999px);
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
}

.feature-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: auto;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-success, #10b981);
}

.status-text {
  font-size: 0.75rem;
  color: var(--color-text-secondary, #64748b);
}

.feature-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  background: var(--color-accent, #6366f1);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full, 9999px);
}

.feature-arrow {
  margin-top: auto;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  color: var(--color-text-secondary, #64748b);
}

/* Activity Section */
.activity-section {
  margin-bottom: 48px;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 1rem;
  color: var(--color-text-primary, #0f172a);
}

.activity-list {
  background: var(--color-bg-card, #ffffff);
  border: 1px solid var(--color-border, #e2e8f0);
  border-radius: var(--radius-lg, 12px);
  overflow: hidden;
}

.activity-item {
  padding: 16px;
  border-bottom: 1px solid var(--color-border, #e2e8f0);
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  transition: all 0.2s ease;
}

.activity-item:hover {
  background: var(--color-bg-hover, #f1f5f9);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-md, 8px);
  background: var(--color-accent-subtle, rgba(99, 102, 241, 0.1));
  color: var(--color-accent, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 2px;
}

.activity-content {
  flex: 1;
}

.activity-description {
  margin: 0 0 0.25rem;
  color: var(--color-text-primary, #0f172a);
}

.activity-time {
  font-size: 0.75rem;
  color: var(--color-text-tertiary, #94a3b8);
}

/* Responsive Design */
@media (max-width: 1023px) {
  .feature-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 767px) {
  .hero-section {
    padding: 24px 0 16px;
  }
  
  .hero-title {
    font-size: 1.75rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
  }
  
  .feature-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .feature-card {
    padding: 16px;
  }
  
  .main-content {
    padding: 0 16px;
  }
}
</style>