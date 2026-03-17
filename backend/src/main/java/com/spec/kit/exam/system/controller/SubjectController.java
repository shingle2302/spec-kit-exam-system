package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.constants.ApiConstants;
import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.service.SubjectService;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import com.spec.kit.exam.system.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController extends BaseController {
    private final SubjectService subjectService;

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<Map<String, Object>>> list(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;
        Map<String, Object> filters = pageRequest.getFilters() != null ? pageRequest.getFilters() : new HashMap<>();
        
        Map<String, Object> req = new HashMap<>();
        req.put("page", pageRequest.getPage());
        req.put("size", pageRequest.getSize());
        req.put("filters", filters);
        
        Map<String, Object> result = subjectService.queryPage(req);
        List<Map<String, Object>> records = (List<Map<String, Object>>) result.get("records");
        Object totalObj = result.get("total");
        int total = totalObj instanceof Number ? ((Number) totalObj).intValue() : 0;
        
        PageResponse<Map<String, Object>> pageResponse = PageResponse.of(records, total, pageRequest.getPage(), pageRequest.getSize());
        return successList(pageResponse);
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return successDetail(subjectService.getDetail(id));
    }

    @PermissionRequired(menu = "subject-management", operation = "CREATE")
    @PostMapping
    public Result<SubjectEntity> create(@RequestBody SubjectEntity request) {
        SubjectEntity created = subjectService.create(request);
        return successCreate(created);
    }

    @PermissionRequired(menu = "subject-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SubjectEntity request) {
        if (!subjectService.update(id, request)) {
            return Result.error("404", "学科不存在: " + id);
        }
        return successUpdate(null);
    }

    @PermissionRequired(menu = "subject-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!subjectService.delete(id)) {
            return Result.error("404", "学科不存在: " + id);
        }
        return successDelete();
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/classes")
    public Result<List<Map<String, Object>>> getClasses() {
        return successList(subjectService.getClasses());
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/levels")
    public Result<List<Map<String, Object>>> getLevels() {
        return successList(subjectService.getLevels());
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = buildStatistics(
            subjectService::getTotalCount,
            subjectService::getActiveCount,
            subjectService::getInactiveCount
        );
        return successStatistics(statistics);
    }
}