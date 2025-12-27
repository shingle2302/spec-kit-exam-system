package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.ClassEnrollment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClassEnrollmentRepository extends BaseMapper<ClassEnrollment> {
    @Select("SELECT * FROM class_enrollments WHERE student_id = #{studentId} AND status = #{status}")
    ClassEnrollment findByStudentIdAndStatus(@Param("studentId") String studentId, @Param("status") ClassEnrollment.Status status);
}