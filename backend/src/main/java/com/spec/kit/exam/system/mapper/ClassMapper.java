package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.ClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClassMapper extends BaseMapper<ClassEntity> {
    @Select("<script>" +
            "SELECT c.id, c.name, c.capacity, c.description, c.created_at AS createTime, " +
            "g.name AS gradeName, el.name AS educationalLevelName " +
            "FROM classes c " +
            "LEFT JOIN grades g ON c.grade_id = g.id " +
            "LEFT JOIN educational_levels el ON c.educational_level_id = el.id " +
            "WHERE 1 = 1 " +
            "<if test='name != null and name != \"\"'>AND c.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='gradeId != null'>AND c.grade_id = #{gradeId}</if> " +
            "<if test='educationalLevelId != null'>AND c.educational_level_id = #{educationalLevelId}</if> " +
            "ORDER BY c.created_at DESC LIMIT #{size} OFFSET #{offset}" +
            "</script>")
    List<Map<String, Object>> queryPage(@Param("name") String name,
                                        @Param("gradeId") Long gradeId,
                                        @Param("educationalLevelId") Long educationalLevelId,
                                        @Param("size") Integer size,
                                        @Param("offset") Integer offset);

    @Select("<script>" +
            "SELECT COUNT(*) FROM classes c WHERE 1 = 1 " +
            "<if test='name != null and name != \"\"'>AND c.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='gradeId != null'>AND c.grade_id = #{gradeId}</if> " +
            "<if test='educationalLevelId != null'>AND c.educational_level_id = #{educationalLevelId}</if> " +
            "</script>")
    Long countQuery(@Param("name") String name,
                    @Param("gradeId") Long gradeId,
                    @Param("educationalLevelId") Long educationalLevelId);

    @Select("SELECT id, name FROM grades WHERE status = 'ACTIVE' ORDER BY id")
    List<Map<String, Object>> selectActiveGrades();

    @Select("SELECT c.id, c.name, c.grade_id AS gradeId, c.educational_level_id AS educationalLevelId, c.capacity, c.description, c.status, c.created_at AS createTime, c.updated_at AS updateTime, g.name AS gradeName, el.name AS educationalLevelName FROM classes c LEFT JOIN grades g ON c.grade_id=g.id LEFT JOIN educational_levels el ON c.educational_level_id=el.id WHERE c.id=#{id}")
    Map<String, Object> selectClassDetail(@Param("id") Long id);
}
