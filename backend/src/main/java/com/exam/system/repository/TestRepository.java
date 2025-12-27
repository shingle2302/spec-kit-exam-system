package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestRepository extends BaseMapper<Test> {
    @Select("SELECT * FROM tests WHERE assigned_by = #{assignedBy}")
    List<Test> findByAssignedBy(@Param("assignedBy") String assignedBy);
    
    @Select("SELECT * FROM tests WHERE JSON_CONTAINS(assigned_to, #{studentId})")
    List<Test> findByAssignedToContaining(@Param("studentId") String studentId);
    
    @Select("SELECT * FROM tests WHERE JSON_CONTAINS(assigned_to, #{studentId}) AND is_active = true")
    List<Test> findByAssignedToContainingAndIsActiveTrue(@Param("studentId") String studentId);
}