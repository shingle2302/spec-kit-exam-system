package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.QuestionEntity;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;

import java.util.List;

/**
 * 试题服务接口
 */
public interface QuestionService {

    /**
     * 获取试题列表
     */
    PageResponse<QuestionEntity> list(PageRequest pageRequest);

    /**
     * 根据条件查询试题
     */
    List<QuestionEntity> listByCondition(Long subjectId, Long gradeId, Long knowledgePointId);

    /**
     * 根据ID获取试题
     */
    QuestionEntity getById(Long id);

    /**
     * 创建试题
     */
    QuestionEntity create(QuestionEntity questionEntity);

    /**
     * 更新试题
     */
    QuestionEntity update(QuestionEntity questionEntity);

    /**
     * 删除试题
     */
    void delete(Long id);

}