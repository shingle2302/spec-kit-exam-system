package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.StudentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentResponseRepository extends BaseMapper<StudentResponse> {
    @Select("SELECT * FROM student_responses WHERE student_id = #{studentId}")
    List<StudentResponse> findByStudentId(@Param("studentId") Long studentId);
    
    @Select("SELECT * FROM student_responses WHERE test_id = #{testId}")
    List<StudentResponse> findByTestId(@Param("testId") Long testId);
    
    @Select("SELECT * FROM student_responses WHERE student_id = #{studentId} AND test_id = #{testId}")
    List<StudentResponse> findByStudentIdAndTestId(@Param("studentId") Long studentId, @Param("testId") Long testId);
    
    @Select("SELECT * FROM student_responses WHERE question_id = #{questionId}")
    List<StudentResponse> findByQuestionId(@Param("questionId") Long questionId);
    
    @Select("SELECT * FROM student_responses WHERE id = #{id}")
    StudentResponse findById(@Param("id") Long id);
    
    @Select("SELECT * FROM student_responses WHERE student_id = #{studentId} AND test_id = #{testId} AND question_id = #{questionId}")
    StudentResponse findByStudentIdAndTestIdAndQuestionId(@Param("studentId") Long studentId, 
                                                         @Param("testId") Long testId, 
                                                         @Param("questionId") Long questionId);
}