package com.zhike.module_student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_student.model.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 笔记数据访问接口
 * 提供笔记表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {

    /**
     * 根据用户ID和视频ID查询笔记列表，按时间戳升序排列
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return 笔记列表
     */
    @Select("SELECT * FROM notes WHERE user_id = #{userId} AND video_id = #{videoId} ORDER BY timestamp ASC")
    List<Note> findByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * 根据笔记ID查询笔记信息
     * 
     * @param id 笔记ID
     * @return 笔记对象，如果不存在则返回null
     */
    @Select("SELECT * FROM notes WHERE id = #{id}")
    Note findById(Long id);
}
