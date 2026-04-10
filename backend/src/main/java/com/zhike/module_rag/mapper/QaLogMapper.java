package com.zhike.module_rag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_rag.model.QaLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 问答日志数据访问接口
 * 提供问答日志表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface QaLogMapper extends BaseMapper<QaLog> {

    /**
     * 根据用户ID分页查询问答历史记录，按创建时间倒序排列
     * 
     * @param userId 用户ID
     * @param pageSize 每页数量
     * @param offset 偏移量
     * @return 问答日志列表
     */
    @Select("SELECT * FROM qa_logs WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<QaLog> findByUserId(Long userId, Integer pageSize, Integer offset);

    /**
     * 统计用户的问答记录总数
     * 
     * @param userId 用户ID
     * @return 问答记录总数
     */
    @Select("SELECT COUNT(*) FROM qa_logs WHERE user_id = #{userId}")
    Integer countByUserId(Long userId);
}
