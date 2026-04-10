package com.zhike.module_student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_student.model.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {

    @Select("SELECT * FROM notes WHERE user_id = #{userId} AND video_id = #{videoId} ORDER BY timestamp ASC")
    List<Note> findByUserIdAndVideoId(Long userId, Long videoId);

    @Select("SELECT * FROM notes WHERE id = #{id}")
    Note findById(Long id);
}
