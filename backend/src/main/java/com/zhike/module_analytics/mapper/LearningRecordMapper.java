package com.zhike.module_analytics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_analytics.model.LearningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学习记录数据访问接口
 * 提供学习记录表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface LearningRecordMapper extends BaseMapper<LearningRecord> {

    /**
     * 根据视频ID查询学习记录
     * 
     * @param videoId 视频ID
     * @return 学习记录列表
     */
    @Select("SELECT * FROM learning_records WHERE video_id = #{videoId}")
    List<LearningRecord> findByVideoId(Long videoId);

    /**
     * 根据用户ID和视频ID查询学习记录
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return 学习记录对象，如果不存在则返回null
     */
    @Select("SELECT * FROM learning_records WHERE user_id = #{userId} AND video_id = #{videoId}")
    LearningRecord findByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * 根据用户ID查询学习记录
     * 
     * @param userId 用户ID
     * @return 学习记录列表
     */
    @Select("SELECT * FROM learning_records WHERE user_id = #{userId}")
    List<LearningRecord> findByUserId(Long userId);

    /**
     * 根据课程ID查询学习记录
     * 通过子查询找到该课程下所有视频的学习记录
     * 
     * @param courseId 课程ID
     * @return 学习记录列表
     */
    @Select("SELECT * FROM learning_records WHERE video_id IN (SELECT id FROM videos WHERE course_id = #{courseId})")
    List<LearningRecord> findByCourseId(Long courseId);
}
