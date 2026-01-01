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
    List<ErrorBook> findByStudentId(@Param("studentId") Long studentId);
    
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId} AND is_mastered = #{mastered}")
    List<ErrorBook> findByStudentIdAndMastered(@Param("studentId") Long studentId, @Param("mastered") Boolean mastered);
    
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId} AND question_id = #{questionId}")
    ErrorBook findByStudentIdAndQuestionId(@Param("studentId") Long studentId, @Param("questionId") Long questionId);
    
    @Select("SELECT * FROM error_books WHERE student_id = #{studentId} AND is_mastered = false")
    List<ErrorBook> findByStudentIdAndIsMasteredFalse(@Param("studentId") Long studentId);
}