package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spec.kit.exam.system.entity.GradeEntity;
import com.spec.kit.exam.system.mapper.GradeMapper;
import com.spec.kit.exam.system.service.GradeService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 年级服务实现类
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeMapper gradeMapper;

    @Override
    public PageResponse<GradeEntity> list(PageRequest pageRequest) {
        Page<GradeEntity> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<GradeEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        Page<GradeEntity> result = gradeMapper.selectPage(page, wrapper);
        return PageResponse.of(result.getRecords(), result.getTotal(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public List<GradeEntity> listActive() {
        return gradeMapper.selectActiveGrades();
    }

    @Override
    public GradeEntity getById(Long id) {
        return gradeMapper.selectById(id);
    }

    @Override
    public GradeEntity create(GradeEntity gradeEntity) {
        gradeMapper.insert(gradeEntity);
        return gradeEntity;
    }

    @Override
    public GradeEntity update(GradeEntity gradeEntity) {
        gradeMapper.updateById(gradeEntity);
        return gradeEntity;
    }

    @Override
    public void delete(Long id) {
        gradeMapper.deleteById(id);
    }

}