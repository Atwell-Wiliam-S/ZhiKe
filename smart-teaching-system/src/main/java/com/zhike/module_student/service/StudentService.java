package com.zhike.module_student.service;

import com.zhike.module_student.dto.*;
import java.util.List;

public interface StudentService {
    // 课程列表
    List<CourseDTO> getCourses(String keyword, int page, int pageSize);
    
    // 课程详情
    CourseDetailDTO getCourseDetail(int courseId);
    
    // 视频播放信息
    PlayInfoDTO getVideoPlayInfo(int videoId, int userId);
    
    // 心跳上报
    void heartbeat(HeartbeatRequest request, int userId);
    
    // 保存进度
    void saveProgress(ProgressRequest request, int userId);
    
    // 添加笔记
    int addNote(NoteRequest request, int userId);
    
    // 获取笔记列表
    List<NoteDTO> getNotes(int videoId, int userId);
}