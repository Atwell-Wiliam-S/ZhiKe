-- 创建数据库
CREATE DATABASE IF NOT EXISTS intelligent_teaching_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE intelligent_teaching_system;

-- 角色信息表
CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色代码',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 用户信息表
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
  `password` VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像OSS路径',
  `role_id` INT NOT NULL COMMENT '角色编号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用,1-启用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_users_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色关联表';

-- 课程信息表
CREATE TABLE IF NOT EXISTS `courses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `teacher_id` INT NOT NULL,
  `description` TEXT DEFAULT NULL,
  `cover_image` VARCHAR(255) DEFAULT NULL,
  `category` VARCHAR(50) DEFAULT NULL,
  `is_published` TINYINT NOT NULL DEFAULT 0 COMMENT '0-未发布,1-已发布',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_teacher_id` (`teacher_id`),
  CONSTRAINT `fk_courses_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程信息表';

-- 视频信息表
CREATE TABLE IF NOT EXISTS `videos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `course_id` INT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `oss_url` VARCHAR(255) NOT NULL,
  `duration` INT NOT NULL COMMENT '时长(秒)',
  `parse_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待解析,1-进行中,2-完成,3-失败',
  `thumbnail_url` VARCHAR(255) DEFAULT NULL,
  `order_num` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_course_order` (`course_id`, `order_num`),
  CONSTRAINT `fk_videos_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频信息表';

-- 知识点片段表
CREATE TABLE IF NOT EXISTS `knowledge_fragments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `video_id` INT NOT NULL,
  `start_time` DECIMAL(10,2) NOT NULL,
  `end_time` DECIMAL(10,2) NOT NULL,
  `content_text` TEXT NOT NULL COMMENT '视-听-文融合文本',
  `keyframe_url` VARCHAR(255) DEFAULT NULL COMMENT '关键帧OSS路径',
  `asr_text` TEXT DEFAULT NULL COMMENT '原始语音转写文本',
  `visual_tags` JSON DEFAULT NULL COMMENT '视觉标签（如：公式、图表、人脸）',
  `summary` VARCHAR(500) DEFAULT NULL,
  `keywords` JSON DEFAULT NULL COMMENT '关键词列表',
  `vector_id` VARCHAR(64) DEFAULT NULL COMMENT 'ES向量文档ID',
  PRIMARY KEY (`id`),
  KEY `idx_video_id` (`video_id`),
  UNIQUE KEY `uk_vector_id` (`vector_id`),
  CONSTRAINT `fk_fragments_video` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点片段表';

-- 学习记录表
CREATE TABLE IF NOT EXISTS `learning_records` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `video_id` INT NOT NULL,
  `progress` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '观看进度(0-100)',
  `last_watch_time` DECIMAL(10,2) DEFAULT NULL,
  `total_duration` INT NOT NULL DEFAULT 0,
  `is_completed` TINYINT NOT NULL DEFAULT 0,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_video` (`user_id`, `video_id`),
  KEY `idx_video_completed` (`video_id`, `is_completed`),
  CONSTRAINT `fk_lr_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_lr_video` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习记录表';

-- 问答日志表
CREATE TABLE IF NOT EXISTS `qa_logs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `question` TEXT NOT NULL,
  `answer` TEXT NOT NULL,
  `context_fragments` JSON DEFAULT NULL COMMENT '参考片段列表',
  `model_version` VARCHAR(50) DEFAULT NULL,
  `token_usage` INT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_qa_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问答日志表';

-- 笔记表
CREATE TABLE IF NOT EXISTS `notes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `video_id` INT NOT NULL,
  `timestamp` DECIMAL(10,2) NOT NULL,
  `content` TEXT NOT NULL,
  `is_public` TINYINT NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_video` (`user_id`, `video_id`),
  CONSTRAINT `fk_notes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_notes_video` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='笔记表';

-- 知识掌握度表
CREATE TABLE IF NOT EXISTS `knowledge_mastery` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `fragment_id` INT NOT NULL,
  `mastery_score` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '掌握度(0.00-1.00)',
  `last_review_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_fragment` (`user_id`, `fragment_id`),
  KEY `idx_fragment_id` (`fragment_id`),
  CONSTRAINT `fk_km_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_km_fragment` FOREIGN KEY (`fragment_id`) REFERENCES `knowledge_fragments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点掌握度表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS `operation_logs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT DEFAULT NULL,
  `operation_type` VARCHAR(50) NOT NULL,
  `request_uri` VARCHAR(255) NOT NULL,
  `ip_address` VARCHAR(50) DEFAULT NULL,
  `result_status` TINYINT NOT NULL,
  `error_msg` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置项键名',
  `config_value` TEXT DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 初始化角色数据
INSERT INTO `roles` (`role_name`, `role_code`, `description`) VALUES
('管理员', 'ADMIN', '系统管理员，拥有所有权限'),
('教师', 'TEACHER', '教师，可管理课程、视频、查看学情'),
('学生', 'STUDENT', '学生，可观看视频、提问、查看个人学情')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name), description = VALUES(description);

-- 初始化系统配置
INSERT INTO `sys_config` (`config_key`, `config_value`, `description`) VALUES
('ai.model.version', 'qwen-plus', '默认大模型版本'),
('ai.max_token', '2048', '单次问答最大Token数'),
('video.max_size_mb', '500', '视频上传最大文件大小(MB)'),
('qa.rate_limit', '20', '每分钟问答次数限制'),
('heatmap.window_seconds', '30', '热力图时间窗口(秒)'),
('heatmap.threshold', '0.7', '难点判定阈值')
ON DUPLICATE KEY UPDATE config_value = VALUES(config_value), description = VALUES(description);

-- 初始化测试用户
INSERT INTO `users` (`username`, `password`, `email`, `role_id`, `status`) VALUES
('admin', '$2a$10$eG79QvHNZ0r8Fg5J7v5GZeZ8h0C1X8JQ5X8JQ5X8JQ5X8JQ5X8JQ', 'admin@edu.cn', 1, 1),
('teacher', '$2a$10$eG79QvHNZ0r8Fg5J7v5GZeZ8h0C1X8JQ5X8JQ5X8JQ5X8JQ5X8JQ', 'teacher@edu.cn', 2, 1),
('student', '$2a$10$eG79QvHNZ0r8Fg5J7v5GZeZ8h0C1X8JQ5X8JQ5X8JQ5X8JQ5X8JQ', 'student@edu.cn', 3, 1)
ON DUPLICATE KEY UPDATE password = VALUES(password), email = VALUES(email), role_id = VALUES(role_id), status = VALUES(status);
