package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.SubjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SubjectMapper extends BaseMapper<SubjectEntity> {
    @Select("<script>" +
            "SELECT s.id, s.name, s.description, s.specialization, s.created_at AS createTime, " +
            "c.name AS className, el.name AS educationalLevelName " +
            "FROM subjects s " +
            "LEFT JOIN classes c ON s.class_id = c.id " +
            "LEFT JOIN educational_levels el ON s.educational_level_id = el.id " +
            "WHERE 1 = 1 " +
            "<if test='name != null and name != \"\"'>AND s.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='classId != null'>AND s.class_id = #{classId}</if> " +
            "<if test='educationalLevelId != null'>AND s.educational_level_id = #{educationalLevelId}</if> " +
            "<if test='specialization != null and specialization != \"\"'>AND s.specialization = #{specialization}</if> " +
            "ORDER BY s.created_at DESC LIMIT #{size} OFFSET #{offset}" +
            "</script>")
    List<Map<String, Object>> queryPage(@Param("name") String name,
                                        @Param("classId") Long classId,
                                        @Param("educationalLevelId") Long educationalLevelId,
                                        @Param("specialization") String specialization,
                                        @Param("size") Integer size,
                                        @Param("offset") Integer offset);

    @Select("<script>" +
            "SELECT COUNT(*) FROM subjects s WHERE 1 = 1 " +
            "<if test='name != null and name != \"\"'>AND s.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='classId != null'>AND s.class_id = #{classId}</if> " +
            "<if test='educationalLevelId != null'>AND s.educational_level_id = #{educationalLevelId}</if> " +
            "<if test='specialization != null and specialization != \"\"'>AND s.specialization = #{specialization}</if> " +
            "</script>")
    Long countQuery(@Param("name") String name,
                    @Param("classId") Long classId,
                    @Param("educationalLevelId") Long educationalLevelId,
                    @Param("specialization") String specialization);

    @Select("SELECT id, name FROM classes WHERE status = 'ACTIVE' ORDER BY id")
    List<Map<String, Object>> selectActiveClasses();

    @Select("SELECT id, name FROM educational_levels WHERE status = 'ACTIVE' ORDER BY id")
    List<Map<String, Object>> selectActiveLevels();

    @Select("SELECT s.id, s.name, s.class_id AS classId, s.educational_level_id AS educationalLevelId, s.description, s.specialization, s.status, s.created_at AS createTime, s.updated_at AS updateTime, c.name AS className, el.name AS educationalLevelName FROM subjects s LEFT JOIN classes c ON s.class_id=c.id LEFT JOIN educational_levels el ON s.educational_level_id=el.id WHERE s.id=#{id}")
    Map<String, Object> selectSubjectDetail(@Param("id") Long id);
}
