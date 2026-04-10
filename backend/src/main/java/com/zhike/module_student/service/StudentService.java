package com.zhike.module_student.service;

import com.zhike.module_student.dto.*;

import java.util.List;

public interface StudentService {
    // 获取课程列表
    List<CourseDTO> getCourseList(String keyword, Integer page, Integer pageSize);

    // 获取课程详情
    CourseDetailDTO getCourseDetail(Integer courseId);

    // 获取视频播放信息
    PlayInfoDTO getPlayInfo(Integer videoId);

    // 心跳上报
    void reportHeartbeat(HeartbeatRequest request);

    // 保存学习进度
    void saveProgress(ProgressRequest request);

    // 添加笔记
    Integer addNote(NoteRequest request);

    // 获取笔记列表
    List<NoteDTO> getNoteList(Integer videoId);
}
