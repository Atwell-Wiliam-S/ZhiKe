package com.zhike.module_video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_video.model.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Select("SELECT * FROM videos WHERE course_id = #{courseId} ORDER BY order_num ASC")
    List<Video> findByCourseId(Long courseId);

    @Select("SELECT * FROM videos WHERE id = #{id}")
    Video findById(Long id);

    @Update("UPDATE videos SET parse_status = #{parseStatus} WHERE id = #{id}")
    void updateParseStatus(Long id, Integer parseStatus);
}
