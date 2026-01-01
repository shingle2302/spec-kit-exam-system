package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Grade;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeRepository extends BaseMapper<Grade> {
}