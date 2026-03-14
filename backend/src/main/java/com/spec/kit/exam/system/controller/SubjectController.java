package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.service.SubjectService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PermissionRequired(menu = "subject-management", button = "create-subject", operation = "CREATE")
    @PostMapping
    public Result<SubjectEntity> create(@RequestBody SubjectEntity request) {
        return Result.success(subjectService.create(request));
    }

    @PermissionRequired(menu = "subject-management", button = "view-subject", operation = "READ")
    @GetMapping
    public Result<Map<String, Object>> query(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Long classId,
                                             @RequestParam(required = false) Long educationalLevelId,
                                             @RequestParam(required = false) String specialization) {
        Map<String, Object> req = new HashMap<>();
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("classId", classId);
        filters.put("educationalLevelId", educationalLevelId);
        filters.put("specialization", specialization);
        req.put("page", page);
        req.put("size", size);
        req.put("filters", filters);
        return Result.success(subjectService.queryPage(req));
    }

    @PermissionRequired(menu = "subject-management", button = "view-subject", operation = "READ")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.success(subjectService.getDetail(id));
    }

    @PermissionRequired(menu = "subject-management", button = "edit-subject", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SubjectEntity request) {
        if (!subjectService.update(id, request)) {
            throw new IllegalArgumentException("Subject not found: " + id);
        }
        return Result.success();
    }

    @PermissionRequired(menu = "subject-management", button = "delete-subject", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!subjectService.delete(id)) {
            throw new IllegalArgumentException("Subject not found: " + id);
        }
        return Result.success();
    }

    @PermissionRequired(menu = "subject-management", button = "view-class", operation = "READ")
    @GetMapping("/classes")
    public Result<Object> classes() {
        return Result.success(subjectService.getClasses());
    }

    @PermissionRequired(menu = "subject-management", button = "view-level", operation = "READ")
    @GetMapping("/levels")
    public Result<Object> levels() {
        return Result.success(subjectService.getLevels());
    }
}
