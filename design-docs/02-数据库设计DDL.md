# 第二部分：数据库详细设计 (DDL)

> 来源：智能教学闭环系统重构设计文档 v1.2
> 
> 本文档定义系统的完整数据库设计，包括 ER 模型、命名规范、数据字典、索引策略、JSON 字段 Schema、MySQL/ES 双写策略及数据量估算。

---

## 2.1 设计原则与命名规范

### 2.1.1 范式与约束

| 规则 | 说明 |
| --- | --- |
| 范式级别 | 第三范式 (3NF)，消除传递依赖 |
| 存储引擎 | InnoDB（支持事务、行锁、外键） |
| 字符集 | `utf8mb4`（支持 4 字节 Unicode，含 emoji） |
| 排序规则 | `utf8mb4_unicode_ci`（不区分大小写，支持多语言排序） |
| 主键策略 | `INT AUTO_INCREMENT`，单列自增 |
| 时间字段 | `DATETIME`（非 TIMESTAMP，避免 2038 问题） |
| 软删除 | 不使用逻辑删除字段，通过状态字段控制（如 `status`） |
| 外键约束 | 生产环境建议应用层校验，DDL 中保留外键作为文档约束 |

### 2.1.2 命名规范

| 对象 | 规则 | 示例 |
| --- | --- | --- |
| 表名 | 小写下划线复数 | `users`, `knowledge_fragments` |
| 字段名 | 小写下划线 | `user_id`, `created_at` |
| 主键 | `id` | 每张表的 `id INT AUTO_INCREMENT` |
| 外键 | `fk_{表缩写}_{引用表}` | `fk_users_role`, `fk_fragments_video` |
| 唯一索引 | `uk_{字段名}` | `uk_username`, `uk_vector_id` |
| 普通索引 | `idx_{字段名}` | `idx_video_id`, `idx_created_at` |
| 联合索引 | `idx_{字段1}_{字段2}` | `idx_course_order`, `idx_user_video` |

---

## 2.2 ER 关系模型

![ER实体关系图](images/er-diagram.jpg)

### 2.2.1 实体关系总览

| 实体A | 关系 | 实体B | 基数 | 关联方式 | 说明 |
| --- | --- | --- | --- | --- | --- |
| User | 1:N | LearningRecord | 一个用户多条学习记录 | `user_id` FK | 支持断点续播 |
| User | M:N | Role | 一个用户多个角色 | `user_roles` 关联表 | RBAC 多角色 |
| User | 1:N | QaLog | 一个用户多条问答日志 | `user_id` FK | AI 交互记录 |
| User | 1:N | Note | 一个用户多条笔记 | `user_id` FK | 视频时间点笔记 |
| User | 1:N | KnowledgeMastery | 一个用户多个掌握度 | `user_id` FK | 学情分析 |
| User | 1:N | OperationLog | 一个用户多条操作日志 | `user_id` FK | 审计追溯 |
| Course | 1:N | Video | 一门课程多个视频 | `course_id` FK | 课程包含视频 |
| Video | 1:N | KnowledgeFragment | 一个视频多个知识片段 | `video_id` FK | RAG 数据源 |
| KnowledgeFragment | 1:N | KnowledgeMastery | 一个片段多个掌握度 | `fragment_id` FK | 量化掌握 |

### 2.2.2 表分类

| 分类 | 表名 | 说明 |
| --- | --- | --- |
| 🔵 **用户域** | roles, users, user_roles | 认证授权 |
| 🟢 **内容域** | courses, videos, knowledge_fragments | 教学资源 |
| 🟡 **学习域** | learning_records, qa_logs, notes, knowledge_mastery | 学习行为 |
| 🟠 **系统域** | operation_logs, sys_config | 审计与配置 |

---

## 2.3 完整 DDL 语句

### 2.3.1 角色信息表 (roles)

RBAC 权限模型的核心，预置 ADMIN/TEACHER/STUDENT 三种角色。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 角色编号 |
| role_name | VARCHAR(50) | NOT NULL | 角色名称（中文） |
| role_code | VARCHAR(50) | NOT NULL, UNIQUE | 角色代码（英文大写） |
| description | VARCHAR(255) | NULLABLE | 角色描述 |

```sql
CREATE TABLE `roles` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '角色编号',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色代码',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';
```

### 2.3.2 用户信息表 (users)

存储用户身份信息与 BCrypt 加密密码。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 用户编号 |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 登录用户名 |
| password | VARCHAR(100) | NOT NULL | BCrypt 加密密码（strength=10, 60字符） |
| email | VARCHAR(100) | UNIQUE, NULLABLE | 邮箱（可选） |
| avatar_url | VARCHAR(255) | NULLABLE | 头像 OSS 路径 |
| role_id | INT | NOT NULL, FK→roles.id | 主角色编号 |
| status | TINYINT | NOT NULL, DEFAULT 1 | 0-禁用, 1-启用 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 创建时间 |
| updated_at | DATETIME | NOT NULL, ON UPDATE NOW | 更新时间 |

```sql
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
    PRIMARY KEY (`id`), UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`), KEY `idx_role_id` (`role_id`),
    CONSTRAINT `fk_users_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';
```

### 2.3.3 用户-角色关联表 (user_roles)

支持多角色的 M:N 关系，联合主键防止重复关联。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| user_id | INT | PK, FK→users.id | 用户编号 |
| role_id | INT | PK, FK→roles.id | 角色编号 |

```sql
CREATE TABLE `user_roles` (
    `user_id` INT NOT NULL, `role_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色关联表';
```

### 2.3.4 课程信息表 (courses)

教学资源顶层容器，is_published 控制发布状态。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 课程编号 |
| title | VARCHAR(100) | NOT NULL | 课程标题 |
| teacher_id | INT | NOT NULL, FK→users.id | 授课教师编号 |
| description | TEXT | NULLABLE | 课程描述 |
| cover_image | VARCHAR(255) | NULLABLE | 封面图 OSS 路径 |
| category | VARCHAR(50) | NULLABLE | 课程分类 |
| is_published | TINYINT | NOT NULL, DEFAULT 0 | 0-未发布, 1-已发布 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 创建时间 |

```sql
CREATE TABLE `courses` (
    `id` INT NOT NULL AUTO_INCREMENT, `title` VARCHAR(100) NOT NULL,
    `teacher_id` INT NOT NULL, `description` TEXT DEFAULT NULL,
    `cover_image` VARCHAR(255) DEFAULT NULL, `category` VARCHAR(50) DEFAULT NULL,
    `is_published` TINYINT NOT NULL DEFAULT 0 COMMENT '0-未发布,1-已发布',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), KEY `idx_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_courses_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程信息表';
```

### 2.3.5 视频信息表 (videos)

视频元数据及解析状态机。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 视频编号 |
| course_id | INT | NOT NULL, FK→courses.id | 所属课程编号 |
| title | VARCHAR(100) | NOT NULL | 视频标题 |
| oss_url | VARCHAR(255) | NOT NULL | 视频 OSS 存储路径 |
| duration | INT | NOT NULL | 时长（秒） |
| parse_status | TINYINT | NOT NULL, DEFAULT 0 | 状态机：0-待解析, 1-进行中, 2-完成, 3-失败 |
| thumbnail_url | VARCHAR(255) | NULLABLE | 缩略图 OSS 路径 |
| order_num | INT | NOT NULL, DEFAULT 0 | 课程内排序序号 |

**状态机流转：**
```
[0 待解析] --触发解析--> [1 进行中] --成功--> [2 完成]
                            |                  ↑
                            |--失败(重试3次)--> [3 失败] --手动重试--> [1]
```

```sql
CREATE TABLE `videos` (
    `id` INT NOT NULL AUTO_INCREMENT, `course_id` INT NOT NULL,
    `title` VARCHAR(100) NOT NULL, `oss_url` VARCHAR(255) NOT NULL,
    `duration` INT NOT NULL COMMENT '时长(秒)',
    `parse_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待解析,1-进行中,2-完成,3-失败',
    `thumbnail_url` VARCHAR(255) DEFAULT NULL, `order_num` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`), KEY `idx_course_id` (`course_id`),
    KEY `idx_course_order` (`course_id`, `order_num`),
    CONSTRAINT `fk_videos_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频信息表';
```

### 2.3.6 知识点片段表 (knowledge_fragments) ⭐核心表

RAG 核心数据源，vector_id 与 ES 向量文档一一对应。v1.2 新增多模态冗余存储字段。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 片段编号 |
| video_id | INT | NOT NULL, FK→videos.id | 所属视频编号 |
| start_time | DECIMAL(10,2) | NOT NULL | 片段起始时间（秒） |
| end_time | DECIMAL(10,2) | NOT NULL | 片段结束时间（秒） |
| content_text | TEXT | NOT NULL | 视-听-文融合文本（RAG检索用） |
| keyframe_url | VARCHAR(255) | NULLABLE | 🆕 关键帧图片 OSS 路径 |
| asr_text | TEXT | NULLABLE | 🆕 原始语音转写文本（溯源用） |
| visual_tags | JSON | NULLABLE | 🆕 视觉标签（公式/图表/人脸等） |
| summary | VARCHAR(500) | NULLABLE | 片段摘要 |
| keywords | JSON | NULLABLE | 关键词列表 |
| vector_id | VARCHAR(64) | UNIQUE, NULLABLE | ES 向量文档 ID |

```sql
CREATE TABLE `knowledge_fragments` (
    `id` INT NOT NULL AUTO_INCREMENT, `video_id` INT NOT NULL,
    `start_time` DECIMAL(10,2) NOT NULL, `end_time` DECIMAL(10,2) NOT NULL,
    `content_text` TEXT NOT NULL COMMENT '视-听-文融合文本',
    `keyframe_url` VARCHAR(255) DEFAULT NULL COMMENT '关键帧OSS路径',
    `asr_text` TEXT DEFAULT NULL COMMENT '原始语音转写文本',
    `visual_tags` JSON DEFAULT NULL COMMENT '视觉标签（如：公式、图表、人脸）',
    `summary` VARCHAR(500) DEFAULT NULL,
    `keywords` JSON DEFAULT NULL COMMENT '关键词列表',
    `vector_id` VARCHAR(64) DEFAULT NULL COMMENT 'ES向量文档ID',
    PRIMARY KEY (`id`), KEY `idx_video_id` (`video_id`),
    UNIQUE KEY `uk_vector_id` (`vector_id`),
    CONSTRAINT `fk_fragments_video` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点片段表';
```

### 2.3.7 学习记录表 (learning_records)

学习进度与行为轨迹，支持断点续播。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 记录编号 |
| user_id | INT | NOT NULL, FK→users.id | 用户编号 |
| video_id | INT | NOT NULL, FK→videos.id | 视频编号 |
| progress | DECIMAL(5,2) | NOT NULL, DEFAULT 0.00 | 观看进度 (0-100) |
| last_watch_time | DECIMAL(10,2) | NULLABLE | 最后观看时间点（秒） |
| total_duration | INT | NOT NULL, DEFAULT 0 | 累计观看时长（秒） |
| is_completed | TINYINT | NOT NULL, DEFAULT 0 | 0-未完成, 1-已完成 |
| updated_at | DATETIME | NOT NULL, ON UPDATE NOW | 更新时间 |

```sql
CREATE TABLE `learning_records` (
    `id` INT NOT NULL AUTO_INCREMENT, `user_id` INT NOT NULL, `video_id` INT NOT NULL,
    `progress` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '观看进度(0-100)',
    `last_watch_time` DECIMAL(10,2) DEFAULT NULL,
    `total_duration` INT NOT NULL DEFAULT 0,
    `is_completed` TINYINT NOT NULL DEFAULT 0,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_user_video` (`user_id`, `video_id`),
    KEY `idx_video_completed` (`video_id`, `is_completed`),
    CONSTRAINT `fk_lr_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_lr_video` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习记录表';
```

### 2.3.8 问答日志表 (qa_logs)

记录每次 AI 交互，context_fragments 为 JSON 支撑答案溯源。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 日志编号 |
| user_id | INT | NOT NULL, FK→users.id | 用户编号 |
| question | TEXT | NOT NULL | 学生提问内容 |
| answer | TEXT | NOT NULL | AI 回答内容 |
| context_fragments | JSON | NULLABLE | 参考的知识片段列表 |
| model_version | VARCHAR(50) | NULLABLE | 使用的模型版本 |
| token_usage | INT | NULLABLE | Token 消耗量 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 创建时间 |

```sql
CREATE TABLE `qa_logs` (
    `id` INT NOT NULL AUTO_INCREMENT, `user_id` INT NOT NULL,
    `question` TEXT NOT NULL, `answer` TEXT NOT NULL,
    `context_fragments` JSON DEFAULT NULL COMMENT '参考片段列表',
    `model_version` VARCHAR(50) DEFAULT NULL,
    `token_usage` INT DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_qa_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问答日志表';
```

### 2.3.9 笔记表 (notes)

视频时间点关联的学习笔记，支持公开/私有。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 笔记编号 |
| user_id | INT | NOT NULL, FK→users.id | 用户编号 |
| video_id | INT | NOT NULL, FK→videos.id | 视频编号 |
| timestamp | DECIMAL(10,2) | NOT NULL | 关联的视频时间点（秒） |
| content | TEXT | NOT NULL | 笔记内容 |
| is_public | TINYINT | NOT NULL, DEFAULT 0 | 0-私有, 1-公开 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 创建时间 |

```sql
CREATE TABLE `notes` (
    `id` INT NOT NULL AUTO_INCREMENT, `user_id` INT NOT NULL, `video_id` INT NOT NULL,
    `timestamp` DECIMAL(10,2) NOT NULL, `content` TEXT NOT NULL,
    `is_public` TINYINT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), KEY `idx_user_video` (`user_id`, `video_id`),
    CONSTRAINT `fk_notes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_notes_video` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='笔记表';
```

### 2.3.10 知识掌握度表 (knowledge_mastery)

量化掌握程度 (0.00-1.00)，参与热力图算法。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 编号 |
| user_id | INT | NOT NULL, FK→users.id | 用户编号 |
| fragment_id | INT | NOT NULL, FK→knowledge_fragments.id | 知识片段编号 |
| mastery_score | DECIMAL(5,2) | NOT NULL, DEFAULT 0.00 | 掌握度 (0.00-1.00) |
| last_review_at | DATETIME | NULLABLE | 最后复习时间 |

```sql
CREATE TABLE `knowledge_mastery` (
    `id` INT NOT NULL AUTO_INCREMENT, `user_id` INT NOT NULL, `fragment_id` INT NOT NULL,
    `mastery_score` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '掌握度(0.00-1.00)',
    `last_review_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_user_fragment` (`user_id`, `fragment_id`),
    KEY `idx_fragment_id` (`fragment_id`),
    CONSTRAINT `fk_km_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_km_fragment` FOREIGN KEY (`fragment_id`) REFERENCES `knowledge_fragments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点掌握度表';
```

### 2.3.11 操作日志表 (operation_logs)

审计关键操作行为，支撑安全审计与故障追溯。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| id | INT | PK, AUTO_INCREMENT | 日志编号 |
| user_id | INT | NULLABLE, FK→users.id | 操作用户编号（系统操作可为NULL） |
| operation_type | VARCHAR(50) | NOT NULL | 操作类型（LOGIN/UPLOAD/PARSE/DELETE等） |
| request_uri | VARCHAR(255) | NOT NULL | 请求URI |
| ip_address | VARCHAR(50) | NULLABLE | 客户端IP |
| result_status | TINYINT | NOT NULL | 操作结果状态码 |
| error_msg | TEXT | NULLABLE | 错误信息 |
| created_at | DATETIME | NOT NULL, DEFAULT NOW | 创建时间 |

```sql
CREATE TABLE `operation_logs` (
    `id` INT NOT NULL AUTO_INCREMENT, `user_id` INT DEFAULT NULL,
    `operation_type` VARCHAR(50) NOT NULL, `request_uri` VARCHAR(255) NOT NULL,
    `ip_address` VARCHAR(50) DEFAULT NULL, `result_status` TINYINT NOT NULL,
    `error_msg` TEXT DEFAULT NULL, `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), KEY `idx_user_id` (`user_id`), KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';
```

### 2.3.12 系统配置表 (sys_config)

动态参数配置，避免硬编码。主键为 config_key 字符串。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| config_key | VARCHAR(100) | PK | 配置项键名（如 `ai.model.version`） |
| config_value | TEXT | NULLABLE | 配置值 |
| description | VARCHAR(255) | NULLABLE | 配置说明 |
| updated_at | DATETIME | NOT NULL, ON UPDATE NOW | 最后更新时间 |

```sql
CREATE TABLE `sys_config` (
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置项键名',
    `config_value` TEXT DEFAULT NULL, `description` VARCHAR(255) DEFAULT NULL,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';
```

---

## 2.4 JSON 字段 Schema 定义

MySQL 8.0 原生支持 JSON 类型，以下定义各 JSON 字段的结构约束，用于应用层校验。

### 2.4.1 knowledge_fragments.visual_tags

```json
{
  "$schema": "视觉标签",
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "tag": { "type": "string", "enum": ["公式", "图表", "人脸", "文字", "代码", "动画", "实验"] },
      "confidence": { "type": "number", "minimum": 0, "maximum": 1 },
      "bbox": { "type": "array", "items": { "type": "number" }, "minItems": 4, "maxItems": 4 }
    },
    "required": ["tag", "confidence"]
  }
}
```

**示例值：**
```json
[
  { "tag": "公式", "confidence": 0.95, "bbox": [120, 80, 400, 200] },
  { "tag": "图表", "confidence": 0.87, "bbox": [50, 300, 600, 500] }
]
```

### 2.4.2 knowledge_fragments.keywords

```json
{
  "type": "array",
  "items": { "type": "string", "maxLength": 50 },
  "maxItems": 20
}
```

**示例值：**
```json
["傅里叶变换", "频域分析", "信号处理", "DFT"]
```

### 2.4.3 qa_logs.context_fragments

```json
{
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "fragmentId": { "type": "integer" },
      "startTime": { "type": "number" },
      "endTime": { "type": "number" },
      "relevance": { "type": "number", "minimum": 0, "maximum": 1 }
    },
    "required": ["fragmentId", "startTime", "endTime", "relevance"]
  }
}
```

**示例值：**
```json
[
  { "fragmentId": 1, "startTime": 120.5, "endTime": 185.0, "relevance": 0.95 },
  { "fragmentId": 3, "startTime": 240.0, "endTime": 310.5, "relevance": 0.82 }
]
```

---

## 2.5 索引设计策略

### 2.5.1 索引清单

| 表 | 索引名 | 类型 | 字段 | 用途 |
| --- | --- | --- | --- | --- |
| roles | PRIMARY | 主键 | id | 主键查找 |
| roles | uk_role_code | 唯一 | role_code | 按角色代码查询 |
| users | PRIMARY | 主键 | id | 主键查找 |
| users | uk_username | 唯一 | username | 登录查询 |
| users | uk_email | 唯一 | email | 邮箱唯一性 |
| users | idx_role_id | 普通 | role_id | 按角色筛选用户 |
| user_roles | PRIMARY | 联合主键 | (user_id, role_id) | 防重复 + 查用户角色 |
| courses | PRIMARY | 主键 | id | 主键查找 |
| courses | idx_teacher_id | 普通 | teacher_id | 查教师的课程 |
| videos | PRIMARY | 主键 | id | 主键查找 |
| videos | idx_course_id | 普通 | course_id | 查课程下的视频 |
| videos | idx_course_order | 联合 | (course_id, order_num) | 按序号排序视频 |
| knowledge_fragments | PRIMARY | 主键 | id | 主键查找 |
| knowledge_fragments | idx_video_id | 普通 | video_id | 查视频的知识片段 |
| knowledge_fragments | uk_vector_id | 唯一 | vector_id | ES 向量文档一一对应 |
| learning_records | PRIMARY | 主键 | id | 主键查找 |
| learning_records | uk_user_video | 唯一 | (user_id, video_id) | 一个用户一个视频一条记录 |
| learning_records | idx_video_completed | 联合 | (video_id, is_completed) | 统计完成人数 |
| qa_logs | PRIMARY | 主键 | id | 主键查找 |
| qa_logs | idx_user_id | 普通 | user_id | 查用户的问答历史 |
| qa_logs | idx_created_at | 普通 | created_at | 按时间范围查询 |
| notes | PRIMARY | 主键 | id | 主键查找 |
| notes | idx_user_video | 联合 | (user_id, video_id) | 查用户在某个视频的笔记 |
| knowledge_mastery | PRIMARY | 主键 | id | 主键查找 |
| knowledge_mastery | uk_user_fragment | 唯一 | (user_id, fragment_id) | 一个用户一个片段一条记录 |
| knowledge_mastery | idx_fragment_id | 普通 | fragment_id | 查某片段的掌握情况 |
| operation_logs | PRIMARY | 主键 | id | 主键查找 |
| operation_logs | idx_user_id | 普通 | user_id | 查用户的操作记录 |
| operation_logs | idx_created_at | 普通 | created_at | 按时间范围审计 |
| sys_config | PRIMARY | 主键 | config_key | 按键名查询配置 |

### 2.5.2 索引设计原则

| 原则 | 说明 |
| --- | --- |
| **联合索引最左前缀** | `idx_course_order(course_id, order_num)` 可用于单独查 course_id，也可用于联合查询 |
| **唯一索引防重复** | 用户-视频、用户-片段等关系使用 UNIQUE 保证数据一致性 |
| **外键即索引** | 所有 FK 字段均建立索引，避免全表扫描 |
| **时间字段建索引** | `created_at` 用于日志按时间范围查询和定期清理 |
| **避免过度索引** | 12 张表共 27 个索引（含主键），平均每表 2.25 个，保持写入性能 |

---

## 2.6 MySQL + Elasticsearch 双写策略

knowledge_fragments 是唯一需要同步到 ES 的表，采用**应用层双写 + 最终一致性**方案。

### 2.6.1 写入流程

```
应用层写入 knowledge_fragments
    ↓ MySQL INSERT (同步)
    ↓ ES Index (同步，同一事务内)
    ↓ 两者都成功 → 提交
    ↓ ES 失败 → 记录到 retry_queue → 定时任务补偿重试
```

### 2.6.2 ES 索引 Mapping

```json
{
  "mappings": {
    "properties": {
      "fragment_id":       { "type": "integer" },
      "video_id":          { "type": "integer" },
      "start_time":        { "type": "float" },
      "end_time":          { "type": "float" },
      "content_text":      { "type": "text", "analyzer": "ik_max_word" },
      "content_vector":    { "type": "dense_vector", "dims": 1536, "index": true, "similarity": "cosine" },
      "keywords":          { "type": "keyword" },
      "summary":           { "type": "text", "analyzer": "ik_max_word" },
      "course_id":         { "type": "integer" },
      "created_at":        { "type": "date" }
    }
  },
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 1,
    "analysis": {
      "analyzer": {
        "ik_max_word": { "type": "custom", "tokenizer": "ik_max_word" }
      }
    }
  }
}
```

### 2.6.3 向量检索参数

| 参数 | 值 | 说明 |
| --- | --- | --- |
| 向量维度 | 1536 | DashScope text-embedding-v3 输出维度 |
| 相似度算法 | cosine | 余弦相似度 |
| 检索方式 | kNN | ES 8.x 原生 kNN 向量搜索 |
| TopK | 5 | 初筛返回 5 个候选片段 |
| Rerank TopN | 3 | 精排后取前 3 个 |

---

## 2.7 数据量估算与容量规划

### 2.7.1 数据量估算

假设系统运行 1 年，500 名学生，20 名教师，50 门课程。

| 表 | 单条大小 | 预估行数 | 总大小 | 增长趋势 |
| --- | --- | --- | --- | --- |
| roles | ~200B | 3 | < 1KB | 静态 |
| users | ~500B | 520 | ~260KB | 低 |
| user_roles | ~16B | 540 | ~9KB | 低 |
| courses | ~1KB | 50 | ~50KB | 低 |
| videos | ~1KB | 500 | ~500KB | 中 |
| **knowledge_fragments** | ~5KB | **50,000** | **~250MB** | **高** |
| learning_records | ~200B | 260,000 | ~52MB | 高 |
| qa_logs | ~5KB | 100,000 | ~500MB | 高 |
| notes | ~2KB | 50,000 | ~100MB | 中 |
| knowledge_mastery | ~100B | 2,500,000 | ~250MB | 高 |
| operation_logs | ~500B | 500,000 | ~250MB | 高 |
| sys_config | ~500B | 20 | ~10KB | 静态 |
| **合计** | | | **~1.4GB** | |

### 2.7.2 容量规划建议

| 阶段 | 用户规模 | MySQL 存储 | ES 存储 | Redis 内存 | 建议 |
| --- | --- | --- | --- | --- | --- |
| MVP (0-6月) | < 100 | < 500MB | < 200MB | < 256MB | 单实例即可 |
| 成长期 (6-18月) | 100-1000 | 1-5GB | 1-3GB | 1-2GB | MySQL主从 + ES单节点 |
| 成熟期 (18月+) | 1000+ | 5-20GB | 5-10GB | 2-4GB | MySQL主从 + ES集群 + Redis哨兵 |

---

## 2.8 初始化数据脚本

```sql
-- 角色初始化
INSERT INTO `roles` (`role_name`, `role_code`, `description`) VALUES
('管理员', 'ADMIN', '系统管理员，拥有所有权限'),
('教师', 'TEACHER', '教师，可管理课程、视频、查看学情'),
('学生', 'STUDENT', '学生，可观看视频、提问、查看个人学情');

-- 系统配置初始化
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
```
