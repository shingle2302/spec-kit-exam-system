package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spec.kit.exam.system.entity.QuestionEntity;
import com.spec.kit.exam.system.mapper.QuestionMapper;
import com.spec.kit.exam.system.service.QuestionService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试题服务实现类
 */
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    @Override
    public PageResponse<QuestionEntity> list(PageRequest pageRequest) {
        Page<QuestionEntity> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<QuestionEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        Page<QuestionEntity> result = questionMapper.selectPage(page, wrapper);
        return PageResponse.of(result.getRecords(), result.getTotal(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public List<QuestionEntity> listByCondition(Long subjectId, Long gradeId, Long knowledgePointId) {
        return questionMapper.selectByCondition(subjectId, gradeId, knowledgePointId);
    }

    @Override
    public QuestionEntity getById(Long id) {
        return questionMapper.selectById(id);
    }

    @Override
    public QuestionEntity create(QuestionEntity questionEntity) {
        questionMapper.insert(questionEntity);
        return questionEntity;
    }

    @Override
    public QuestionEntity update(QuestionEntity questionEntity) {
        questionMapper.updateById(questionEntity);
        return questionEntity;
    }

    @Override
    public void delete(Long id) {
        questionMapper.deleteById(id);
    }

}