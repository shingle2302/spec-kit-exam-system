package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.TestQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestQuestionRepository extends BaseMapper<TestQuestion> {
    
    @Select("SELECT question_id FROM t_test_question WHERE test_id = #{testId} ORDER BY question_order")
    List<Long> getQuestionIdsByTestId(@Param("testId") Long testId);
}