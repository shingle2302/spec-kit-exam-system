package com.exam.system.repository;

import com.exam.system.model.TestAssignment;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TestAssignmentRepository extends BaseMapper<TestAssignment> {
    
    @Select("SELECT * FROM test_assignments WHERE student_id = #{studentId}")
    List<TestAssignment> findByStudentId(@Param("studentId") Long studentId);
    
    @Select("SELECT * FROM test_assignments WHERE test_id = #{testId}")
    List<TestAssignment> findByTestId(@Param("testId") Long testId);
    
    @Select("SELECT * FROM test_assignments WHERE student_id = #{studentId} AND status = #{status}")
    List<TestAssignment> findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") String status);
    
    @Select("SELECT * FROM test_assignments WHERE assigned_by = #{teacherId}")
    List<TestAssignment> findByAssignedBy(@Param("teacherId") Long teacherId);
    
    @Select("SELECT * FROM test_assignments WHERE test_id = #{testId} AND student_id = #{studentId}")
    TestAssignment findByTestIdAndStudentId(@Param("testId") Long testId, @Param("studentId") Long studentId);
}