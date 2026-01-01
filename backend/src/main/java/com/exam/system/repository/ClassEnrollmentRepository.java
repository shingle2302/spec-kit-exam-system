package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.ClassEnrollment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClassEnrollmentRepository extends BaseMapper<ClassEnrollment> {
    @Select("SELECT * FROM class_enrollments WHERE student_id = #{studentId} AND status = #{status}")
    ClassEnrollment findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") ClassEnrollment.Status status);
    
    @Select("SELECT * FROM class_enrollments WHERE class_id IN " +
            "<foreach item='classId' collection='classList' open='(' separator=',' close=')'>#{classId}</foreach> " +
            "AND status = 'ACTIVE'")
    java.util.List<com.exam.system.model.Student> findActiveStudentsByClasses(@Param("classList") java.util.List<Long> classList);
    
    @Select("SELECT * FROM class_enrollments WHERE student_id = #{studentId} AND class_id = #{classId} AND status = #{status}")
    ClassEnrollment findByStudentIdAndClassIdAndStatus(@Param("studentId") Long studentId, 
                                                      @Param("classId") Long classId, 
                                                      @Param("status") ClassEnrollment.Status status);
}