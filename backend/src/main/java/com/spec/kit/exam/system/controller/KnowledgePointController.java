package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.KnowledgePointEntity;
import com.spec.kit.exam.system.service.KnowledgePointService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge-points")
@RequiredArgsConstructor
public class KnowledgePointController extends BaseController {

    private final KnowledgePointService knowledgePointService;

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<KnowledgePointEntity>> list(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;
        com.spec.kit.exam.system.util.PageRequest pageRequest2 = 
            new com.spec.kit.exam.system.util.PageRequest(pageRequest.getPage(), pageRequest.getSize());
        PageResponse<KnowledgePointEntity> result = knowledgePointService.list(pageRequest2);
        return successList(result);
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> getTree() {
        return successList(knowledgePointService.getTree());
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/tree/{subjectId}")
    public Result<List<Map<String, Object>>> getTreeBySubjectId(@PathVariable Long subjectId) {
        return successList(knowledgePointService.getTreeBySubjectId(subjectId));
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<KnowledgePointEntity> getById(@PathVariable Long id) {
        return successDetail(knowledgePointService.getById(id));
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "CREATE")
    @PostMapping
    public Result<KnowledgePointEntity> create(@RequestBody KnowledgePointEntity knowledgePointEntity) {
        KnowledgePointEntity created = knowledgePointService.create(knowledgePointEntity);
        return successCreate(created);
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<KnowledgePointEntity> update(@PathVariable Long id, @RequestBody KnowledgePointEntity knowledgePointEntity) {
        knowledgePointEntity.setId(id);
        KnowledgePointEntity updated = knowledgePointService.update(knowledgePointEntity);
        return successUpdate(updated);
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        knowledgePointService.delete(id);
        return successDelete();
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = buildStatistics(
            knowledgePointService::getTotalCount,
            knowledgePointService::getActiveCount,
            knowledgePointService::getInactiveCount
        );
        return successStatistics(statistics);
    }
}