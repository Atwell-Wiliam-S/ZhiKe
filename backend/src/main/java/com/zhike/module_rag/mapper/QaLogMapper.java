package com.zhike.module_rag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_rag.model.QaLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QaLogMapper extends BaseMapper<QaLog> {

    @Select("SELECT * FROM qa_logs WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<QaLog> findByUserId(Long userId, Integer pageSize, Integer offset);

    @Select("SELECT COUNT(*) FROM qa_logs WHERE user_id = #{userId}")
    Integer countByUserId(Long userId);
}
