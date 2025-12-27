package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionRepository extends BaseMapper<Question> {
    @Select("SELECT * FROM questions WHERE grade_id = #{gradeId}")
    List<Question> findByGradeId(@Param("gradeId") String gradeId);
    
    @Select("SELECT * FROM questions WHERE subject_id = #{subjectId}")
    List<Question> findBySubjectId(@Param("subjectId") String subjectId);
    
    @Select("SELECT * FROM questions WHERE knowledge_point = #{knowledgePoint}")
    List<Question> findByKnowledgePoint(@Param("knowledgePoint") String knowledgePoint);
    
    @Select("SELECT * FROM questions WHERE grade_id = #{gradeId} AND subject_id = #{subjectId} AND knowledge_point = #{knowledgePoint}")
    List<Question> findByGradeIdAndSubjectIdAndKnowledgePoint(
        @Param("gradeId") String gradeId, 
        @Param("subjectId") String subjectId, 
        @Param("knowledgePoint") String knowledgePoint
    );
    
    @Select("SELECT * FROM questions WHERE created_by = #{createdBy}")
    List<Question> findByCreatedBy(@Param("createdBy") String createdBy);
    
    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question findById(@Param("id") String id);
}