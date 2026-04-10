package com.zhike.module_student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_student.model.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 课程数据访问接口
 * 提供课程表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据关键词分页查询已发布的课程，按创建时间倒序排列
     * 
     * @param keyword 搜索关键词
     * @param pageSize 每页数量
     * @param offset 偏移量
     * @return 课程列表
     */
    @Select("SELECT * FROM courses WHERE is_published = 1 AND title LIKE CONCAT('%', #{keyword}, '%') ORDER BY created_at DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<Course> findByKeyword(String keyword, Integer pageSize, Integer offset);

    /**
     * 统计符合关键词的已发布课程总数
     * 
     * @param keyword 搜索关键词
     * @return 课程总数
     */
    @Select("SELECT COUNT(*) FROM courses WHERE is_published = 1 AND title LIKE CONCAT('%', #{keyword}, '%')")
    Integer countByKeyword(String keyword);

    /**
     * 根据课程ID查询课程信息
     * 
     * @param id 课程ID
     * @return 课程对象，如果不存在则返回null
     */
    @Select("SELECT * FROM courses WHERE id = #{id}")
    Course findById(Long id);
}
