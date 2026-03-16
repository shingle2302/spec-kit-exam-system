package com.spec.kit.exam.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spec.kit.exam.system.entity.KnowledgePointEntity;
import com.spec.kit.exam.system.mapper.KnowledgePointMapper;
import com.spec.kit.exam.system.service.KnowledgePointService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识点服务实现类
 */
@Service
@RequiredArgsConstructor
public class KnowledgePointServiceImpl implements KnowledgePointService {

    private final KnowledgePointMapper knowledgePointMapper;
    private static final int MAX_LEVEL = 5;

    @Override
    public PageResponse<KnowledgePointEntity> list(PageRequest pageRequest) {
        Page<KnowledgePointEntity> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<KnowledgePointEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        Page<KnowledgePointEntity> result = knowledgePointMapper.selectPage(page, wrapper);
        return PageResponse.of(result.getRecords(), result.getTotal(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public List<Map<String, Object>> getTree() {
        List<KnowledgePointEntity> allPoints = knowledgePointMapper.selectAllKnowledgePoints();
        return buildTree(allPoints, null);
    }

    @Override
    public List<Map<String, Object>> getTreeBySubjectId(Long subjectId) {
        List<KnowledgePointEntity> subjectPoints = knowledgePointMapper.selectBySubjectId(subjectId);
        return buildTree(subjectPoints, null);
    }

    @Override
    public KnowledgePointEntity getById(Long id) {
        return knowledgePointMapper.selectById(id);
    }

    @Override
    public KnowledgePointEntity create(KnowledgePointEntity knowledgePointEntity) {
        // 验证层级深度
        if (knowledgePointEntity.getParentId() != null) {
            if (!validateLevelDepth(knowledgePointEntity.getParentId(), 1)) {
                throw new RuntimeException("知识点层级深度不能超过" + MAX_LEVEL + "级");
            }
        }
        knowledgePointMapper.insert(knowledgePointEntity);
        return knowledgePointEntity;
    }

    @Override
    public KnowledgePointEntity update(KnowledgePointEntity knowledgePointEntity) {
        // 验证层级深度
        if (knowledgePointEntity.getParentId() != null) {
            if (!validateLevelDepth(knowledgePointEntity.getParentId(), 1)) {
                throw new RuntimeException("知识点层级深度不能超过" + MAX_LEVEL + "级");
            }
        }
        knowledgePointMapper.updateById(knowledgePointEntity);
        return knowledgePointEntity;
    }

    @Override
    public void delete(Long id) {
        // 检查是否有子知识点
        List<KnowledgePointEntity> children = knowledgePointMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该知识点下有子知识点，无法删除");
        }
        knowledgePointMapper.deleteById(id);
    }

    @Override
    public boolean validateLevelDepth(Long parentId, int currentLevel) {
        if (currentLevel >= MAX_LEVEL) {
            return false;
        }
        KnowledgePointEntity parent = knowledgePointMapper.selectById(parentId);
        if (parent == null || parent.getParentId() == null) {
            return true;
        }
        return validateLevelDepth(parent.getParentId(), currentLevel + 1);
    }

    /**
     * 构建树形结构
     */
    private List<Map<String, Object>> buildTree(List<KnowledgePointEntity> allPoints, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        for (KnowledgePointEntity point : allPoints) {
            if (point.getParentId() == parentId) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", point.getId());
                node.put("name", point.getName());
                node.put("code", point.getCode());
                node.put("subjectId", point.getSubjectId());
                node.put("children", buildTree(allPoints, point.getId()));
                tree.add(node);
            }
        }
        return tree;
    }

}