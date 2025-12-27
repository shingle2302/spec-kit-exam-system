package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository extends BaseMapper<Student> {
    @Select("SELECT * FROM students WHERE student_id = #{studentId}")
    Student findByStudentId(@Param("studentId") String studentId);
    
    @Select("SELECT * FROM students WHERE user_id = #{userId}")
    Student findByUserId(@Param("userId") String userId);
    
    @Select("SELECT * FROM students WHERE id = #{id}")
    Student findById(@Param("id") String id);
}