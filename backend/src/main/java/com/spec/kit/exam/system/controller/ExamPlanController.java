package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.ExamPlan;
import com.spec.kit.exam.system.service.ExamPlanService;
import com.spec.kit.exam.system.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/exam-plans")
public class ExamPlanController {

    @Autowired
    private ExamPlanService examPlanService;

    @PermissionRequired(menu = "exam-plan-management", button = "create-exam-plan", operation = "CREATE")
    @PostMapping
    public Result<ExamPlan> create(@RequestBody ExamPlan request) {
        return Result.success(examPlanService.create(request));
    }

    @PermissionRequired(menu = "exam-plan-management", button = "view-exam-plan", operation = "READ")
    @PostMapping("/query")
    public Result<Map<String, Object>> query(@RequestBody Map<String, Object> request) {
        return Result.success(examPlanService.queryPage(request));
    }

    @PermissionRequired(menu = "exam-plan-management", button = "view-exam-plan", operation = "READ")
    @GetMapping("/{id}")
    public Result<ExamPlan> detail(@PathVariable Long id) {
        ExamPlan examPlan = examPlanService.getById(id);
        if (examPlan == null) {
            throw new IllegalArgumentException("Exam plan not found: " + id);
        }
        return Result.success(examPlan);
    }

    @PermissionRequired(menu = "exam-plan-management", button = "edit-exam-plan", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ExamPlan request) {
        if (!examPlanService.update(id, request)) {
            throw new IllegalArgumentException("Exam plan not found: " + id);
        }
        return Result.success();
    }

    @PermissionRequired(menu = "exam-plan-management", button = "delete-exam-plan", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!examPlanService.delete(id)) {
            throw new IllegalArgumentException("Exam plan not found: " + id);
        }
        return Result.success();
    }
}
