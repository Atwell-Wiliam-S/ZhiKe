-- 智能教学闭环系统数据库初始化脚本
-- 版本：v1.2
-- 包含12张表的DDL和预置数据

-- 创建角色信息表 (roles)
CREATE TABLE `roles` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '角色编号',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色代码',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 创建用户信息表 (users)
CREATE TABLE `users` (
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

-- 创建用户-角色关联表 (user_roles)
CREATE TABLE `user_roles` (
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色关联表';

-- 创建课程信息表 (courses)
CREATE TABLE `courses` (
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

-- 创建视频信息表 (videos)
CREATE TABLE `videos` (
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

-- 创建知识点片段表 (knowledge_fragments)
CREATE TABLE `knowledge_fragments` (
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

-- 创建学习记录表 (learning_records)
CREATE TABLE `learning_records` (
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

-- 创建问答日志表 (qa_logs)
CREATE TABLE `qa_logs` (
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

-- 创建笔记表 (notes)
CREATE TABLE `notes` (
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

-- 创建知识掌握度表 (knowledge_mastery)
CREATE TABLE `knowledge_mastery` (
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

-- 创建操作日志表 (operation_logs)
CREATE TABLE `operation_logs` (
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

-- 创建系统配置表 (sys_config)
CREATE TABLE `sys_config` (
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置项键名',
    `config_value` TEXT DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入预置角色数据
INSERT INTO `roles` (`role_name`, `role_code`, `description`) VALUES
('管理员', 'ADMIN', '系统管理员，拥有所有权限'),
('教师', 'TEACHER', '教师，可管理课程、视频、查看学情'),
('学生', 'STUDENT', '学生，可观看视频、提问、查看个人学情');

-- 插入预置系统配置数据
INSERT INTO `sys_config` (`config_key`, `config_value`, `description`) VALUES
('ai.model.version', 'qwen-plus', '默认大模型版本'),
('ai.max_token', '2048', '单次问答最大Token数'),
('ai.embedding.model', 'text-embedding-v3', '向量化模型版本'),
('ai.embedding.dims', '1536', '向量维度'),
('video.max_size_mb', '500', '视频上传最大文件大小(MB)'),
('video.frame_interval', '30', '关键帧提取间隔(秒)'),
('qa.rate_limit', '20', '每分钟问答次数限制'),
('qa.top_k', '5', '向量检索初筛数量'),
('qa.top_n', '3', 'Rerank精排数量'),
('heatmap.window_seconds', '30', '热力图时间窗口(秒)'),
('heatmap.threshold', '0.7', '难点判定阈值'),
('heatmap.weight_replay', '0.4', '回放权重'),
('heatmap.weight_pause', '0.3', '暂停权重'),
('heatmap.weight_question', '0.3', '提问权重');