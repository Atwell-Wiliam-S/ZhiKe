package com.zhike.module_student.service;

import com.zhike.common.util.PageResult;
import com.zhike.module_student.model.Course;
import com.zhike.module_student.model.Note;
import com.zhike.module_video.model.Video;
import com.zhike.module_video.model.KnowledgeFragment;

import java.util.Map;

/**
 * 学生服务接口
 * 提供学生端的课程学习、视频播放、笔记管理等功能
 */
public interface StudentService {

    /**
     * 分页查询课程列表
     * 支持按关键词搜索课程
     * 
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页结果，包含课程列表
     */
    PageResult<Course> getCourses(String keyword, Integer page, Integer pageSize);

    /**
     * 获取课程详情
     * 包括课程信息和该课程下的视频列表
     * 
     * @param courseId 课程ID
     * @return 包含课程详情和视频列表的Map
     */
    Map<String, Object> getCourseDetail(Long courseId);

    /**
     * 获取视频播放信息
     * 包括视频信息、播放签名URL、知识片段、上次观看位置等
     * 
     * @param videoId 视频ID
     * @param userId 用户ID
     * @return 包含播放信息的Map
     */
    Map<String, Object> getPlayInfo(Long videoId, Long userId);

    /**
     * 视频观看心跳上报
     * 用于记录用户的观看行为
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param currentTime 当前观看时间点（秒）
     * @param status 观看状态
     */
    void heartbeat(Long userId, Long videoId, Double currentTime, String status);

    /**
     * 保存视频观看进度
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param currentTime 当前观看时间点（秒）
     * @param duration 视频总时长（秒）
     */
    void saveProgress(Long userId, Long videoId, Double currentTime, Integer duration);

    /**
     * 添加学习笔记
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param timestamp 笔记对应的时间点（秒）
     * @param content 笔记内容
     * @return 创建的笔记ID
     */
    Long addNote(Long userId, Long videoId, Double timestamp, String content);

    /**
     * 获取视频的笔记列表
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return 包含笔记列表的Map
     */
    Map<String, Object> getNotes(Long userId, Long videoId);
}
