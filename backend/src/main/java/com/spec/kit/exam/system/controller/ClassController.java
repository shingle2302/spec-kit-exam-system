package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.ClassEntity;
import com.spec.kit.exam.system.service.ClassService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {
    private final ClassService classService;

    @PermissionRequired(menu = "class-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<Map<String, Object>>> list(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;
        Map<String, Object> filters = pageRequest.getFilters() != null ? pageRequest.getFilters() : new HashMap<>();
        
        Map<String, Object> req = new HashMap<>();
        req.put("page", pageRequest.getPage());
        req.put("size", pageRequest.getSize());
        req.put("filters", filters);
        
        Map<String, Object> result = classService.queryPage(req);
        List<Map<String, Object>> records = (List<Map<String, Object>>) result.get("records");
        int total = (int) result.get("total");
        
        PageResponse<Map<String, Object>> pageResponse = PageResponse.of(records, total, pageRequest.getPage(), pageRequest.getSize());
        return Result.success(pageResponse, "班级列表查询成功");
    }

    @PermissionRequired(menu = "class-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return Result.success(classService.getDetail(id), "班级详情查询成功");
    }

    @PermissionRequired(menu = "class-management", operation = "CREATE")
    @PostMapping
    public Result<ClassEntity> create(@RequestBody ClassEntity request) {
        ClassEntity created = classService.create(request);
        return Result.success(created, "班级创建成功");
    }

    @PermissionRequired(menu = "class-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ClassEntity request) {
        if (!classService.update(id, request)) {
            return Result.error(404, "班级不存在: " + id);
        }
        return Result.success(null, "班级更新成功");
    }

    @PermissionRequired(menu = "class-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!classService.delete(id)) {
            return Result.error(404, "班级不存在: " + id);
        }
        return Result.success(null, "班级删除成功");
    }

    @PermissionRequired(menu = "class-management", operation = "READ")
    @GetMapping("/grades")
    public Result<List<Map<String, Object>>> getGrades() {
        return Result.success(classService.getGrades(), "年级列表查询成功");
    }

    @PermissionRequired(menu = "class-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", classService.getTotalCount());
        statistics.put("active", classService.getActiveCount());
        statistics.put("inactive", classService.getInactiveCount());
        return Result.success(statistics, "班级统计查询成功");
    }
}