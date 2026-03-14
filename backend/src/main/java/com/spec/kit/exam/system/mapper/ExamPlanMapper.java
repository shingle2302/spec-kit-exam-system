package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.ExamPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamPlanMapper extends BaseMapper<ExamPlan> {

    @Select("<script>" +
            "SELECT id, name, academic_year AS academicYear, term, exam_type AS examType, start_time AS startTime, end_time AS endTime, status, description, created_at AS createdAt " +
            "FROM exam_plans WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='academicYear != null and academicYear != \"\"'>AND academic_year = #{academicYear}</if> " +
            "<if test='examType != null and examType != \"\"'>AND exam_type = #{examType}</if> " +
            "ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}" +
            "</script>")
    List<Map<String, Object>> queryPage(@Param("name") String name,
                                        @Param("academicYear") String academicYear,
                                        @Param("examType") String examType,
                                        @Param("size") Integer size,
                                        @Param("offset") Integer offset);

    @Select("<script>" +
            "SELECT COUNT(*) FROM exam_plans WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='academicYear != null and academicYear != \"\"'>AND academic_year = #{academicYear}</if> " +
            "<if test='examType != null and examType != \"\"'>AND exam_type = #{examType}</if> " +
            "</script>")
    Long countQuery(@Param("name") String name,
                    @Param("academicYear") String academicYear,
                    @Param("examType") String examType);
}
