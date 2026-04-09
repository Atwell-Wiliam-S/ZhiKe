package com.zhike.mapper;

import com.zhike.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("SELECT * FROM courses")
    List<Course> selectAll();
    
    @Select("SELECT * FROM courses WHERE id = #{id}")
    Course selectById(Integer id);
    
    @Select("SELECT * FROM courses WHERE teacher_id = #{teacherId}")
    List<Course> selectByTeacherId(Integer teacherId);
    
    @Select("SELECT * FROM courses WHERE is_published = 1")
    List<Course> selectPublished();
}
