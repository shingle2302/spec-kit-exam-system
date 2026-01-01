package com.exam.system.repository;

import com.exam.system.model.Test;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestRepository extends BaseMapper<Test> {
    @Select("SELECT t.* FROM tests t JOIN test_assignments ta ON t.id = ta.test_id WHERE ta.student_id = #{studentId}")
    List<Test> findByStudentId(@Param("studentId") Long studentId);
    
    @Select("SELECT t.* FROM tests t JOIN test_assignments ta ON t.id = ta.test_id WHERE ta.student_id = #{studentId} AND t.is_active = true")
    List<Test> findByStudentIdAndIsActiveTrue(@Param("studentId") Long studentId);
    
    @Select("SELECT t.* FROM tests t JOIN test_assignments ta ON t.id = ta.test_id WHERE ta.test_id = #{testId}")
    List<Test> findByTestId(@Param("testId") Long testId);
    
    // Method for pagination with TestAssignment
    @Select("SELECT t.* FROM tests t JOIN test_assignments ta ON t.id = ta.test_id WHERE ta.student_id = #{studentId} AND t.is_active = true LIMIT #{offset}, #{limit}")
    List<Test> findTestsByStudentIdPaginated(@Param("studentId") Long studentId, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM tests t JOIN test_assignments ta ON t.id = ta.test_id WHERE ta.student_id = #{studentId} AND t.is_active = true")
    int countTestsByStudentId(@Param("studentId") Long studentId);
}