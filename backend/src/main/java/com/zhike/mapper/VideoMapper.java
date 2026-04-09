package com.zhike.mapper;

import com.zhike.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Select("SELECT * FROM videos")
    List<Video> selectAll();
    
    @Select("SELECT * FROM videos WHERE id = #{id}")
    Video selectById(Integer id);
    
    @Select("SELECT * FROM videos WHERE course_id = #{courseId} ORDER BY order_num ASC")
    List<Video> selectByCourseId(Integer courseId);
    
    @Select("SELECT * FROM videos WHERE parse_status = #{status}")
    List<Video> selectByParseStatus(Integer status);
}
