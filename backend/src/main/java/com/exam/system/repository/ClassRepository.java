package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Class;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassRepository extends BaseMapper<Class> {
    @Select("SELECT * FROM classes WHERE grade_id = #{gradeId}")
    List<Class> findByGradeId(@Param("gradeId") String gradeId);
    
    @Select("SELECT * FROM classes WHERE teacher_id = #{teacherId}")
    List<Class> findByTeacherId(@Param("teacherId") String teacherId);
    
    @Select("SELECT * FROM classes WHERE name = #{name}")
    Class findByName(@Param("name") String name);
}