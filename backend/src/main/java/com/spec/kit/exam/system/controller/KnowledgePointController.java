package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.KnowledgePointEntity;
import com.spec.kit.exam.system.service.KnowledgePointService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识点控制器
 */
@RestController
@RequestMapping("/api/knowledge-points")
@RequiredArgsConstructor
public class KnowledgePointController {

    private final KnowledgePointService knowledgePointService;

    /**
     * 获取知识点列表
     */
    @GetMapping
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_QUERY")
    public Result list(PageRequest pageRequest) {
        return Result.success(knowledgePointService.list(pageRequest));
    }

    /**
     * 获取知识点树结构
     */
    @GetMapping("/tree")
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_QUERY")
    public Result getTree() {
        return Result.success(knowledgePointService.getTree());
    }

    /**
     * 根据学科ID获取知识点树
     */
    @GetMapping("/tree/{subjectId}")
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_QUERY")
    public Result getTreeBySubjectId(@PathVariable Long subjectId) {
        return Result.success(knowledgePointService.getTreeBySubjectId(subjectId));
    }

    /**
     * 根据ID获取知识点
     */
    @GetMapping("/{id}")
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_QUERY")
    public Result getById(@PathVariable Long id) {
        return Result.success(knowledgePointService.getById(id));
    }

    /**
     * 创建知识点
     */
    @PostMapping
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_CREATE")
    public Result create(@RequestBody KnowledgePointEntity knowledgePointEntity) {
        return Result.success(knowledgePointService.create(knowledgePointEntity));
    }

    /**
     * 更新知识点
     */
    @PutMapping("/{id}")
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_UPDATE")
    public Result update(@PathVariable Long id, @RequestBody KnowledgePointEntity knowledgePointEntity) {
        knowledgePointEntity.setId(id);
        return Result.success(knowledgePointService.update(knowledgePointEntity));
    }

    /**
     * 删除知识点
     */
    @DeleteMapping("/{id}")
    @PermissionRequired(permissionCode = "KNOWLEDGE_POINT_DELETE")
    public Result delete(@PathVariable Long id) {
        knowledgePointService.delete(id);
        return Result.success();
    }

}