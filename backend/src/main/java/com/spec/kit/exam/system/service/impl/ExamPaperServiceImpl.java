package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spec.kit.exam.system.entity.ExamPaper;
import com.spec.kit.exam.system.mapper.ExamPaperMapper;
import com.spec.kit.exam.system.service.ExamPaperService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试卷服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExamPaperServiceImpl implements ExamPaperService {

    private final ExamPaperMapper examPaperMapper;

    @Override
    public PageResponse<ExamPaper> list(PageRequest pageRequest) {
        Page<ExamPaper> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<ExamPaper> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        Page<ExamPaper> result = examPaperMapper.selectPage(page, wrapper);
        return PageResponse.of(result.getRecords(), result.getTotal(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public List<ExamPaper> listByGradeId(Long gradeId) {
        return examPaperMapper.selectByGradeId(gradeId);
    }

    @Override
    public List<ExamPaper> listBySubjectAndGrade(Long subjectId, Long gradeId) {
        return examPaperMapper.selectBySubjectAndGrade(subjectId, gradeId);
    }

    @Override
    public ExamPaper getById(Long id) {
        return examPaperMapper.selectById(id);
    }

    @Override
    public ExamPaper create(ExamPaper examPaper) {
        examPaperMapper.insert(examPaper);
        return examPaper;
    }

    @Override
    public ExamPaper update(ExamPaper examPaper) {
        examPaperMapper.updateById(examPaper);
        return examPaper;
    }

    @Override
    public void delete(Long id) {
        examPaperMapper.deleteById(id);
    }

}