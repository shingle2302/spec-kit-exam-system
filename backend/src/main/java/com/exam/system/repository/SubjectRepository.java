package com.exam.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.system.model.Subject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectRepository extends BaseMapper<Subject> {
}