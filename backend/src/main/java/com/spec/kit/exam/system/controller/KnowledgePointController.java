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
public class KnowledgePointController {

    private final KnowledgePointService knowledgePointService;

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<KnowledgePointEntity>> list(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;
        Map<String, Object> result = knowledgePointService.list(pageRequest);
        List<KnowledgePointEntity> records = (List<KnowledgePointEntity>) result.get("records");
        int total = (int) result.get("total");
        
        PageResponse<KnowledgePointEntity> pageResponse = PageResponse.of(records, total, pageRequest.getPage(), pageRequest.getSize());
        return Result.success(pageResponse, "知识点列表查询成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> getTree() {
        return Result.success(knowledgePointService.getTree(), "知识点树结构查询成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/tree/{subjectId}")
    public Result<List<Map<String, Object>>> getTreeBySubjectId(@PathVariable Long subjectId) {
        return Result.success(knowledgePointService.getTreeBySubjectId(subjectId), "学科知识点树结构查询成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<KnowledgePointEntity> getById(@PathVariable Long id) {
        return Result.success(knowledgePointService.getById(id), "知识点详情查询成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "CREATE")
    @PostMapping
    public Result<KnowledgePointEntity> create(@RequestBody KnowledgePointEntity knowledgePointEntity) {
        KnowledgePointEntity created = knowledgePointService.create(knowledgePointEntity);
        return Result.success(created, "知识点创建成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<KnowledgePointEntity> update(@PathVariable Long id, @RequestBody KnowledgePointEntity knowledgePointEntity) {
        knowledgePointEntity.setId(id);
        KnowledgePointEntity updated = knowledgePointService.update(knowledgePointEntity);
        return Result.success(updated, "知识点更新成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        knowledgePointService.delete(id);
        return Result.success(null, "知识点删除成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/subjects")
    public Result<List<Map<String, Object>>> getSubjects() {
        return Result.success(knowledgePointService.getSubjects(), "学科列表查询成功");
    }

    @PermissionRequired(menu = "knowledge-point-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", knowledgePointService.getTotalCount());
        statistics.put("active", knowledgePointService.getActiveCount());
        statistics.put("inactive", knowledgePointService.getInactiveCount());
        return Result.success(statistics, "知识点统计查询成功");
    }
}