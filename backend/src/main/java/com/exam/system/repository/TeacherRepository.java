package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherRepository extends BaseMapper<Teacher> {
}