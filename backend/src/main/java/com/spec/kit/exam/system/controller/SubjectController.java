package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.service.SubjectService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
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
        int total = (int) result.get("total");
        
        PageResponse<Map<String, Object>> pageResponse = PageResponse.of(records, total, pageRequest.getPage(), pageRequest.getSize());
        return Result.success(pageResponse, "学科列表查询成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return Result.success(subjectService.getDetail(id), "学科详情查询成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "CREATE")
    @PostMapping
    public Result<SubjectEntity> create(@RequestBody SubjectEntity request) {
        SubjectEntity created = subjectService.create(request);
        return Result.success(created, "学科创建成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SubjectEntity request) {
        if (!subjectService.update(id, request)) {
            return Result.error(404, "学科不存在: " + id);
        }
        return Result.success(null, "学科更新成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!subjectService.delete(id)) {
            return Result.error(404, "学科不存在: " + id);
        }
        return Result.success(null, "学科删除成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/classes")
    public Result<List<Map<String, Object>>> getClasses() {
        return Result.success(subjectService.getClasses(), "班级列表查询成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/levels")
    public Result<List<Map<String, Object>>> getLevels() {
        return Result.success(subjectService.getLevels(), "教育水平列表查询成功");
    }

    @PermissionRequired(menu = "subject-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", subjectService.getTotalCount());
        statistics.put("active", subjectService.getActiveCount());
        statistics.put("inactive", subjectService.getInactiveCount());
        return Result.success(statistics, "学科统计查询成功");
    }
}