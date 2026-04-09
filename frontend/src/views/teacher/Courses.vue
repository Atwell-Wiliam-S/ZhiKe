<template>
  <div class="teacher-courses">
    <div class="container">
      <h1>课程管理</h1>
      <div class="actions">
        <button class="add-course-btn">添加课程</button>
      </div>
      <div class="course-list">
        <div class="course-item">
          <div class="course-info">
            <h3>Vue 3 从入门到精通</h3>
            <p>李老师</p>
            <p>前端开发</p>
            <p>50 名学生</p>
          </div>
          <div class="course-actions">
            <button>编辑</button>
            <button>删除</button>
            <button>查看视频</button>
          </div>
        </div>
        <div class="course-item">
          <div class="course-info">
            <h3>Spring Boot 3 企业级应用开发</h3>
            <p>李老师</p>
            <p>后端开发</p>
            <p>50 名学生</p>
          </div>
          <div class="course-actions">
            <button>编辑</button>
            <button>删除</button>
            <button>查看视频</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 教师课程管理组件
</script>

<style scoped>
.teacher-courses {
  min-height: 100vh;
  background: #fdfdf8;
  color: #4d4f46;
  padding: 40px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.container h1 {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 40px;
  color: #4d4f46;
}

.actions {
  margin-bottom: 24px;
}

.add-course-btn {
  padding: 10px 24px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
}

.add-course-btn:hover {
  color: #F54E00;
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.course-item {
  background: white;
  border: 1px solid #bfc1b7;
  border-radius: 4px;
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.course-info h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #4d4f46;
}

.course-info p {
  font-size: 14px;
  color: #65675e;
  margin-bottom: 4px;
}

.course-actions button {
  margin-left: 8px;
  padding: 6px 12px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.course-actions button:hover {
  color: #F54E00;
}
</style>
