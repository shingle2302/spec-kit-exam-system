package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.KnowledgePointEntity;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * 知识点服务接口
 */
public interface KnowledgePointService {

    /**
     * 获取知识点列表
     */
    PageResponse<KnowledgePointEntity> list(PageRequest pageRequest);

    /**
     * 获取知识点树结构
     */
    List<Map<String, Object>> getTree();

    /**
     * 根据学科ID获取知识点树
     */
    List<Map<String, Object>> getTreeBySubjectId(Long subjectId);

    /**
     * 根据ID获取知识点
     */
    KnowledgePointEntity getById(Long id);

    /**
     * 创建知识点
     */
    KnowledgePointEntity create(KnowledgePointEntity knowledgePointEntity);

    /**
     * 更新知识点
     */
    KnowledgePointEntity update(KnowledgePointEntity knowledgePointEntity);

    /**
     * 删除知识点
     */
    void delete(Long id);

    /**
     * 验证知识点层级深度
     */
    boolean validateLevelDepth(Long parentId, int currentLevel);

}