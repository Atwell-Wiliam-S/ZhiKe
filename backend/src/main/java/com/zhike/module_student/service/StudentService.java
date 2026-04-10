package com.zhike.module_student.service;

import com.zhike.common.util.PageResult;
import com.zhike.module_student.model.Course;
import com.zhike.module_student.model.Note;
import com.zhike.module_video.model.Video;
import com.zhike.module_video.model.KnowledgeFragment;

import java.util.Map;

public interface StudentService {

    PageResult<Course> getCourses(String keyword, Integer page, Integer pageSize);

    Map<String, Object> getCourseDetail(Long courseId);

    Map<String, Object> getPlayInfo(Long videoId, Long userId);

    void heartbeat(Long userId, Long videoId, Double currentTime, String status);

    void saveProgress(Long userId, Long videoId, Double currentTime, Integer duration);

    Long addNote(Long userId, Long videoId, Double timestamp, String content);

    Map<String, Object> getNotes(Long userId, Long videoId);
}
