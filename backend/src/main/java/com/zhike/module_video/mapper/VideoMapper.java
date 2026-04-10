package com.zhike.module_video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_video.model.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 视频数据访问接口
 * 提供视频表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    /**
     * 根据课程ID查询视频列表，按排序号升序排列
     * 
     * @param courseId 课程ID
     * @return 视频列表
     */
    @Select("SELECT * FROM videos WHERE course_id = #{courseId} ORDER BY order_num ASC")
    List<Video> findByCourseId(Long courseId);

    /**
     * 根据视频ID查询视频信息
     * 
     * @param id 视频ID
     * @return 视频对象，如果不存在则返回null
     */
    @Select("SELECT * FROM videos WHERE id = #{id}")
    Video findById(Long id);

    /**
     * 更新视频的解析状态
     * 
     * @param id 视频ID
     * @param parseStatus 解析状态（0:未解析，1:解析中，2:解析完成，3:解析失败）
     */
    @Update("UPDATE videos SET parse_status = #{parseStatus} WHERE id = #{id}")
    void updateParseStatus(Long id, Integer parseStatus);
}
