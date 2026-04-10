package com.zhike.module_video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_video.model.KnowledgeFragment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KnowledgeFragmentMapper extends BaseMapper<KnowledgeFragment> {

    @Select("SELECT * FROM knowledge_fragments WHERE video_id = #{videoId} ORDER BY start_time ASC")
    List<KnowledgeFragment> findByVideoId(Long videoId);

    @Select("SELECT * FROM knowledge_fragments WHERE id = #{id}")
    KnowledgeFragment findById(Long id);
}
