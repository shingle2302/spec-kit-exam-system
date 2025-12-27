package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.ErrorBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ErrorBookRepository extends BaseMapper<ErrorBook> {
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId}")
    List<ErrorBook> findByStudentId(@Param("studentId") String studentId);
    
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId} AND mastered = #{mastered}")
    List<ErrorBook> findByStudentIdAndMastered(@Param("studentId") String studentId, @Param("mastered") Boolean mastered);
    
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId} AND question_id = #{questionId}")
    ErrorBook findByStudentIdAndQuestionId(@Param("studentId") String studentId, @Param("questionId") String questionId);
    
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId} AND mastered = false")
    List<ErrorBook> findByStudentIdAndMasteredFalse(@Param("studentId") String studentId);
}