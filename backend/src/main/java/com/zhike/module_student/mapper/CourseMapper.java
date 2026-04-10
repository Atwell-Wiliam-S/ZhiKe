package com.zhike.module_student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_student.model.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Select("SELECT * FROM courses WHERE is_published = 1 AND title LIKE CONCAT('%', #{keyword}, '%') ORDER BY created_at DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<Course> findByKeyword(String keyword, Integer pageSize, Integer offset);

    @Select("SELECT COUNT(*) FROM courses WHERE is_published = 1 AND title LIKE CONCAT('%', #{keyword}, '%')")
    Integer countByKeyword(String keyword);

    @Select("SELECT * FROM courses WHERE id = #{id}")
    Course findById(Long id);
}
