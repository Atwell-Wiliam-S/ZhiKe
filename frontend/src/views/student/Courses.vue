<template>
  <div class="student-courses">
    <div class="container">
      <h1>我的课程</h1>
      <div class="search-bar">
        <input type="text" placeholder="搜索课程..." />
        <button>搜索</button>
      </div>
      <div class="courses-grid">
        <div class="course-card">
          <img src="/covers/vue3-course.png" alt="Vue 3 从入门到精通" class="course-cover" />
          <div class="course-info">
            <h3>Vue 3 从入门到精通</h3>
            <p class="course-teacher">李老师</p>
            <p class="course-description">全面学习 Vue 3 的核心概念和实战技巧</p>
            <div class="course-progress">
              <div class="progress-bar">
                <div class="progress-fill" style="width: 30%;"></div>
              </div>
              <span class="progress-text">30% 完成</span>
            </div>
            <button class="course-btn">继续学习</button>
          </div>
        </div>
        <div class="course-card">
          <img src="/covers/spring-boot3-course.png" alt="Spring Boot 3 企业级应用开发" class="course-cover" />
          <div class="course-info">
            <h3>Spring Boot 3 企业级应用开发</h3>
            <p class="course-teacher">李老师</p>
            <p class="course-description">学习 Spring Boot 3 的企业级应用开发</p>
            <div class="course-progress">
              <div class="progress-bar">
                <div class="progress-fill" style="width: 10%;"></div>
              </div>
              <span class="progress-text">10% 完成</span>
            </div>
            <button class="course-btn">继续学习</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 课程列表页面组件
</script>

<style scoped>
.student-courses {
  min-height: 100vh;
  background: #1a1a2e;
  color: #e2e8f0;
  padding: 40px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.container h1 {
  font-size: 36px;
  font-weight: 400;
  margin-bottom: 40px;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 40px;
}

.search-bar input {
  flex: 1;
  padding: 12px;
  background: #252540;
  border: 1px solid #3a3a5c;
  border-radius: 6px;
  color: #e2e8f0;
  outline: none;
}

.search-bar input::placeholder {
  color: #94a3b8;
}

.search-bar button {
  padding: 0 24px;
  background: #6366f1;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-bar button:hover {
  background: #818cf8;
}

.courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 32px;
}

.course-card {
  background: #252540;
  border: 1px solid #3a3a5c;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.course-card:hover {
  border-color: #6366f1;
  box-shadow: 0 0 20px rgba(99, 102, 241, 0.3);
  transform: translateY(-5px);
}

.course-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.course-info {
  padding: 24px;
}

.course-info h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #e2e8f0;
}

.course-teacher {
  font-size: 14px;
  color: #94a3b8;
  margin-bottom: 12px;
}

.course-description {
  font-size: 14px;
  color: #94a3b8;
  margin-bottom: 20px;
  line-height: 1.5;
}

.course-progress {
  margin-bottom: 20px;
}

.progress-bar {
  width: 100%;
  height: 6px;
  background: #3a3a5c;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background: #6366f1;
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 12px;
  color: #94a3b8;
}

.course-btn {
  width: 100%;
  padding: 12px;
  background: #6366f1;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.course-btn:hover {
  background: #818cf8;
}
</style>
