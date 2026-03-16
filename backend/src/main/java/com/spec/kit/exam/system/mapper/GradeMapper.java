package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.GradeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 年级Mapper
 */
@Mapper
public interface GradeMapper extends BaseMapper<GradeEntity> {

    /**
     * 获取活跃的年级列表
     */
    @Select("SELECT * FROM grades WHERE status = 'ACTIVE' ORDER BY id")
    List<GradeEntity> selectActiveGrades();

}