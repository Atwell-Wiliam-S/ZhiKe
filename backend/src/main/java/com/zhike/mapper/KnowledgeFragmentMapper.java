package com.zhike.mapper;

import com.zhike.entity.KnowledgeFragment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KnowledgeFragmentMapper {
    @Select("SELECT * FROM knowledge_fragments")
    List<KnowledgeFragment> selectAll();
    
    @Select("SELECT * FROM knowledge_fragments WHERE id = #{id}")
    KnowledgeFragment selectById(Integer id);
    
    @Select("SELECT * FROM knowledge_fragments WHERE video_id = #{videoId}")
    List<KnowledgeFragment> selectByVideoId(Integer videoId);
    
    @Select("SELECT * FROM knowledge_fragments WHERE vector_id = #{vectorId}")
    KnowledgeFragment selectByVectorId(String vectorId);
}
