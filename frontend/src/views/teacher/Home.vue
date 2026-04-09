<template>
  <div class="teacher-home">
    <div class="container">
      <h1>教师管理中心</h1>
      <div class="kpi-grid">
        <div class="kpi-card">
          <h3>总学生数</h3>
          <p class="kpi-value">100</p>
        </div>
        <div class="kpi-card">
          <h3>活跃课程</h3>
          <p class="kpi-value">5</p>
        </div>
        <div class="kpi-card">
          <h3>平均完成率</h3>
          <p class="kpi-value">75.5%</p>
        </div>
        <div class="kpi-card">
          <h3>今日问题数</h3>
          <p class="kpi-value">20</p>
        </div>
      </div>
      <div class="course-management">
        <h2>课程管理</h2>
        <div class="course-table">
          <table>
            <thead>
              <tr>
                <th>课程名称</th>
                <th>学生数</th>
                <th>完成率</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Vue 3 从入门到精通</td>
                <td>50</td>
                <td>80%</td>
                <td>
                  <button>查看</button>
                  <button>编辑</button>
                </td>
              </tr>
              <tr>
                <td>Spring Boot 3 企业级应用开发</td>
                <td>50</td>
                <td>70%</td>
                <td>
                  <button>查看</button>
                  <button>编辑</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 教师首页组件
</script>

<style scoped>
.teacher-home {
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

.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 40px;
}

.kpi-card {
  background: white;
  border: 1px solid #bfc1b7;
  border-radius: 4px;
  padding: 24px;
  text-align: center;
}

.kpi-card h3 {
  font-size: 14px;
  font-weight: 400;
  margin-bottom: 12px;
  color: #65675e;
}

.kpi-value {
  font-size: 30px;
  font-weight: 800;
  color: #4d4f46;
}

.course-management {
  background: white;
  border: 1px solid #bfc1b7;
  border-radius: 4px;
  padding: 24px;
}

.course-management h2 {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
  color: #4d4f46;
}

.course-table table {
  width: 100%;
  border-collapse: collapse;
}

.course-table th,
.course-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #bfc1b7;
}

.course-table th {
  font-weight: 600;
  color: #4d4f46;
}

.course-table td {
  color: #65675e;
}

.course-table button {
  margin-right: 8px;
  padding: 6px 12px;
  background: #1e1f23;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.course-table button:hover {
  color: #F54E00;
}
</style>
