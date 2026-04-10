package com.zhike.module_video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_video.model.KnowledgeFragment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 知识片段数据访问接口
 * 提供知识片段表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface KnowledgeFragmentMapper extends BaseMapper<KnowledgeFragment> {

    /**
     * 根据视频ID查询知识片段列表，按开始时间升序排列
     * 
     * @param videoId 视频ID
     * @return 知识片段列表
     */
    @Select("SELECT * FROM knowledge_fragments WHERE video_id = #{videoId} ORDER BY start_time ASC")
    List<KnowledgeFragment> findByVideoId(Long videoId);

    /**
     * 根据知识片段ID查询知识片段信息
     * 
     * @param id 知识片段ID
     * @return 知识片段对象，如果不存在则返回null
     */
    @Select("SELECT * FROM knowledge_fragments WHERE id = #{id}")
    KnowledgeFragment findById(Long id);
}
