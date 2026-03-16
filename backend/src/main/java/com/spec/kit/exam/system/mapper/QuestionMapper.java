package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.QuestionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 试题Mapper
 */
@Mapper
public interface QuestionMapper extends BaseMapper<QuestionEntity> {

    /**
     * 根据条件查询试题
     */
    @Select("SELECT * FROM questions WHERE status = 'ACTIVE' AND (#{subjectId} IS NULL OR subject_id = #{subjectId}) AND (#{gradeId} IS NULL OR grade_id = #{gradeId}) AND (#{knowledgePointId} IS NULL OR knowledge_point_id = #{knowledgePointId}) ORDER BY id")
    List<QuestionEntity> selectByCondition(Long subjectId, Long gradeId, Long knowledgePointId);

    /**
     * 根据学科ID查询试题
     */
    @Select("SELECT * FROM questions WHERE subject_id = #{subjectId} AND status = 'ACTIVE' ORDER BY id")
    List<QuestionEntity> selectBySubjectId(Long subjectId);

    /**
     * 根据年级ID查询试题
     */
    @Select("SELECT * FROM questions WHERE grade_id = #{gradeId} AND status = 'ACTIVE' ORDER BY id")
    List<QuestionEntity> selectByGradeId(Long gradeId);

    /**
     * 根据知识点ID查询试题
     */
    @Select("SELECT * FROM questions WHERE knowledge_point_id = #{knowledgePointId} AND status = 'ACTIVE' ORDER BY id")
    List<QuestionEntity> selectByKnowledgePointId(Long knowledgePointId);

}
