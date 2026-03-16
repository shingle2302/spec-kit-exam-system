package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.KnowledgePointEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 知识点Mapper
 */
@Mapper
public interface KnowledgePointMapper extends BaseMapper<KnowledgePointEntity> {

    /**
     * 获取知识点树结构
     */
    @Select("SELECT * FROM knowledge_points WHERE status = 'ACTIVE' ORDER BY id")
    List<KnowledgePointEntity> selectAllKnowledgePoints();

    /**
     * 根据学科ID获取知识点列表
     */
    @Select("SELECT * FROM knowledge_points WHERE subject_id = #{subjectId} AND status = 'ACTIVE' ORDER BY id")
    List<KnowledgePointEntity> selectBySubjectId(Long subjectId);

    /**
     * 根据父知识点ID获取子知识点
     */
    @Select("SELECT * FROM knowledge_points WHERE parent_id = #{parentId} AND status = 'ACTIVE' ORDER BY id")
    List<KnowledgePointEntity> selectByParentId(Long parentId);

}