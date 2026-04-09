<template>
  <div class="student-learn">
    <div class="container">
      <h1>视频学习</h1>
      <div class="video-container">
        <div class="video-player">
          <video controls width="100%" height="480">
            <source src="https://example.com/video/1.mp4" type="video/mp4">
            您的浏览器不支持视频播放。
          </video>
        </div>
        <div class="video-sidebar">
          <h3>知识大纲</h3>
          <ul class="knowledge-outline">
            <li>00:00 - 01:00 Vue 3 简介</li>
            <li>01:00 - 02:00 环境搭建</li>
            <li>02:00 - 03:00 基础语法</li>
            <li>03:00 - 04:00 组件系统</li>
            <li>04:00 - 05:00 Composition API</li>
          </ul>
          <h3>我的笔记</h3>
          <div class="notes-list">
            <div class="note-item">
              <p class="note-time">01:30</p>
              <p class="note-content">Vue 3 的 Composition API 提供了更好的代码组织方式</p>
            </div>
          </div>
        </div>
      </div>
      <div class="ai-chat">
        <h3>AI 助手</h3>
        <div class="chat-messages">
          <div class="message ai-message">
            <p>你好！我是你的 AI 学习助手，有什么问题可以随时问我。</p>
          </div>
        </div>
        <div class="chat-input">
          <input type="text" placeholder="输入你的问题..." />
          <button>发送</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 学习页面组件
</script>

<style scoped>
.student-learn {
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

.video-container {
  display: grid;
  grid-template-columns: 70% 30%;
  gap: 32px;
  margin-bottom: 40px;
}

.video-player {
  background: #252540;
  border: 1px solid #3a3a5c;
  border-radius: 8px;
  padding: 24px;
}

.video-sidebar {
  background: #252540;
  border: 1px solid #3a3a5c;
  border-radius: 8px;
  padding: 24px;
}

.video-sidebar h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #e2e8f0;
}

.knowledge-outline {
  list-style: none;
  margin-bottom: 32px;
}

.knowledge-outline li {
  padding: 12px;
  border-radius: 6px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.knowledge-outline li:hover {
  background: rgba(99, 102, 241, 0.1);
  border-left: 3px solid #6366f1;
}

.notes-list {
  margin-top: 16px;
}

.note-item {
  background: #2d2d4a;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 12px;
}

.note-time {
  font-size: 12px;
  color: #94a3b8;
  margin-bottom: 4px;
}

.note-content {
  font-size: 14px;
  color: #e2e8f0;
}

.ai-chat {
  background: #252540;
  border: 1px solid #3a3a5c;
  border-radius: 8px;
  padding: 24px;
}

.ai-chat h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #e2e8f0;
}

.chat-messages {
  margin-bottom: 24px;
  max-height: 300px;
  overflow-y: auto;
}

.message {
  margin-bottom: 16px;
  padding: 12px;
  border-radius: 6px;
}

.ai-message {
  background: rgba(99, 102, 241, 0.1);
  border-left: 3px solid #6366f1;
}

.chat-input {
  display: flex;
  gap: 12px;
}

.chat-input input {
  flex: 1;
  padding: 12px;
  background: #2d2d4a;
  border: 1px solid #3a3a5c;
  border-radius: 6px;
  color: #e2e8f0;
  outline: none;
}

.chat-input input::placeholder {
  color: #94a3b8;
}

.chat-input button {
  padding: 0 24px;
  background: #6366f1;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.chat-input button:hover {
  background: #818cf8;
}
</style>
