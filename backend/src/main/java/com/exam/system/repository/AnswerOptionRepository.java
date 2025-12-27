package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.AnswerOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerOptionRepository extends BaseMapper<AnswerOption> {
    @Select("SELECT * FROM answer_options WHERE question_id = #{questionId}")
    List<AnswerOption> findByQuestionId(@Param("questionId") String questionId);
}