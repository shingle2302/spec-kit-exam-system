package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.ClassEntity;
import com.spec.kit.exam.system.service.ClassService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    @Autowired
    private ClassService classService;

    @PermissionRequired(menu = "class-management", button = "create-class", operation = "CREATE")
    @PostMapping
    public Result<ClassEntity> create(@RequestBody ClassEntity request) {
        return Result.success(classService.create(request));
    }

    @PermissionRequired(menu = "class-management", button = "view-class", operation = "READ")
    @GetMapping
    public Result<Map<String, Object>> query(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Long gradeId,
                                             @RequestParam(required = false) Long educationalLevelId) {
        Map<String, Object> req = new HashMap<>();
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("gradeId", gradeId);
        filters.put("educationalLevelId", educationalLevelId);
        req.put("page", page);
        req.put("size", size);
        req.put("filters", filters);
        return Result.success(classService.queryPage(req));
    }

    @PermissionRequired(menu = "class-management", button = "view-class", operation = "READ")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.success(classService.getDetail(id));
    }

    @PermissionRequired(menu = "class-management", button = "edit-class", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ClassEntity request) {
        if (!classService.update(id, request)) {
            throw new IllegalArgumentException("Class not found: " + id);
        }
        return Result.success();
    }

    @PermissionRequired(menu = "class-management", button = "delete-class", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!classService.delete(id)) {
            throw new IllegalArgumentException("Class not found: " + id);
        }
        return Result.success();
    }

    @PermissionRequired(menu = "class-management", button = "view-grade", operation = "READ")
    @GetMapping("/grades")
    public Result<Object> grades() {
        return Result.success(classService.getGrades());
    }
}
