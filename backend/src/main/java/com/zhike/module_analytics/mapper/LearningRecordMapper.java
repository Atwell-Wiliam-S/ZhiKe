package com.zhike.module_analytics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_analytics.model.LearningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LearningRecordMapper extends BaseMapper<LearningRecord> {

    @Select("SELECT * FROM learning_records WHERE video_id = #{videoId}")
    List<LearningRecord> findByVideoId(Long videoId);

    @Select("SELECT * FROM learning_records WHERE user_id = #{userId} AND video_id = #{videoId}")
    LearningRecord findByUserIdAndVideoId(Long userId, Long videoId);

    @Select("SELECT * FROM learning_records WHERE user_id = #{userId}")
    List<LearningRecord> findByUserId(Long userId);

    @Select("SELECT * FROM learning_records WHERE video_id IN (SELECT id FROM videos WHERE course_id = #{courseId})")
    List<LearningRecord> findByCourseId(Long courseId);
}
