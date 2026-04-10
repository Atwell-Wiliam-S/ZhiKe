# 智能教学闭环系统 - 前后端联调产品需求文档

## Overview
- **Summary**: 本项目旨在完成智能教学闭环系统的前后端联调，确保前端与后端服务能够正常通信，实现完整的业务功能。
- **Purpose**: 解决前后端分离架构中的集成问题，验证所有API接口的正常工作，确保系统功能完整可用。
- **Target Users**: 系统开发人员、测试人员、最终用户（教师和学生）。

## Goals
- 确保前端能够正确调用后端API接口
- 验证所有核心功能的端到端流程
- 解决联调过程中发现的问题
- 确保系统在生产环境中的稳定性

## Non-Goals (Out of Scope)
- 不包括性能测试和压力测试
- 不包括安全渗透测试
- 不包括UI/UX优化
- 不包括新功能开发

## Background & Context
- 前端基于Vue 3 + TypeScript开发，使用Vite作为构建工具
- 后端基于Spring Boot 3.2.0 + Java 17开发，使用Maven作为构建工具
- 系统采用前后端分离架构，通过RESTful API进行通信
- 前端目前使用Mock数据模拟后端响应，需要切换到真实后端服务

## Functional Requirements
- **FR-1**: 认证功能联调
  - 用户登录/注册
  - Token管理（获取、刷新、存储）
  - 权限控制
- **FR-2**: 视频功能联调
  - 视频列表获取
  - 视频详情获取
  - 视频上传
- **FR-3**: 智能问答功能联调
  - 提问功能
  - 问答历史获取
- **FR-4**: 学情分析功能联调
  - 热力图数据获取
  - 班级概览获取
  - 学生画像获取
  - 词云数据获取
- **FR-5**: 教师功能联调
  - 课程管理（创建、更新、删除）
  - 学情分析查看
- **FR-6**: 学生功能联调
  - 课程列表获取
  - 学习进度获取
  - 笔记管理（创建、获取）
  - 个人学情分析获取
- **FR-7**: 管理员功能联调
  - 用户管理（创建、更新、删除）
  - 角色管理
  - 操作日志查看

## Non-Functional Requirements
- **NFR-1**: 性能要求
  - API响应时间不超过2秒
  - 页面加载时间不超过3秒
- **NFR-2**: 可靠性要求
  - 系统错误率不超过1%
  - 服务可用性达到99.9%
- **NFR-3**: 安全性要求
  - 所有API调用必须进行身份验证
  - 敏感数据必须加密传输
- **NFR-4**: 兼容性要求
  - 支持主流浏览器（Chrome、Firefox、Safari、Edge）
  - 响应式设计，支持不同屏幕尺寸

## Constraints
- **Technical**: 
  - 前端：Vue 3, TypeScript, Vite
  - 后端：Spring Boot 3.2.0, Java 17, Maven
  - 数据库：MySQL 8.0, Redis 7.0, Elasticsearch 8.x, RabbitMQ 3.12
- **Business**: 
  - 联调时间限制：1周
  - 资源限制：开发环境资源有限
- **Dependencies**: 
  - 后端服务必须正常运行
  - 数据库必须初始化完成
  - Docker环境必须可用

## Assumptions
- 后端服务已经开发完成，所有API接口已经实现
- 前端代码已经开发完成，所有页面和组件已经实现
- 开发环境已经搭建完成，包括Docker、Java、Node.js等
- 数据库已经初始化，包含必要的预置数据

## Acceptance Criteria

### AC-1: 认证功能联调
- **Given**: 后端认证服务正常运行
- **When**: 前端调用登录API
- **Then**: 登录成功，返回JWT token，前端能够存储并使用token
- **Verification**: `programmatic`
- **Notes**: 验证token的有效期和刷新机制

### AC-2: 视频功能联调
- **Given**: 后端视频服务正常运行
- **When**: 前端调用视频列表API
- **Then**: 成功获取视频列表，能够播放视频
- **Verification**: `programmatic`
- **Notes**: 验证视频上传功能的完整性

### AC-3: 智能问答功能联调
- **Given**: 后端问答服务正常运行
- **When**: 前端提交问题
- **Then**: 后端返回智能回答，前端正确显示
- **Verification**: `programmatic`
- **Notes**: 验证问答历史的获取和显示

### AC-4: 学情分析功能联调
- **Given**: 后端分析服务正常运行
- **When**: 前端请求学情分析数据
- **Then**: 成功获取热力图、班级概览、学生画像和词云数据
- **Verification**: `programmatic`
- **Notes**: 验证数据的准确性和可视化效果

### AC-5: 教师功能联调
- **Given**: 后端教师服务正常运行
- **When**: 教师用户登录系统
- **Then**: 能够管理课程，查看学情分析
- **Verification**: `programmatic`
- **Notes**: 验证权限控制是否正确

### AC-6: 学生功能联调
- **Given**: 后端学生服务正常运行
- **When**: 学生用户登录系统
- **Then**: 能够查看课程，获取学习进度，管理笔记
- **Verification**: `programmatic`
- **Notes**: 验证个人学情分析的准确性

### AC-7: 管理员功能联调
- **Given**: 后端管理员服务正常运行
- **When**: 管理员用户登录系统
- **Then**: 能够管理用户，查看角色和操作日志
- **Verification**: `programmatic`
- **Notes**: 验证管理员权限是否正确

### AC-8: 系统稳定性
- **Given**: 系统运行一段时间
- **When**: 进行各种操作
- **Then**: 系统保持稳定，无崩溃或异常
- **Verification**: `human-judgment`
- **Notes**: 验证系统在不同场景下的稳定性

## Open Questions
- [ ] 后端服务的具体部署方式和访问地址
- [ ] 前端Mock数据的切换方式
- [ ] 联调过程中可能遇到的网络问题解决方案
- [ ] 测试数据的准备和管理
- [ ] 联调完成后的部署计划