package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.ExamPaper;
import com.spec.kit.exam.system.service.ExamPaperService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试卷控制器
 */
@RestController
@RequestMapping("/api/exam-papers")
@RequiredArgsConstructor
public class ExamPaperController {

    private final ExamPaperService examPaperService;

    /**
     * 获取试卷列表
     */
    @GetMapping
    @PermissionRequired(permissionCode = "EXAM_PAPER_QUERY")
    public Result list(PageRequest pageRequest) {
        return Result.success(examPaperService.list(pageRequest));
    }

    /**
     * 根据年级ID获取试卷
     */
    @GetMapping("/grade/{gradeId}")
    @PermissionRequired(permissionCode = "EXAM_PAPER_QUERY")
    public Result listByGradeId(@PathVariable Long gradeId) {
        return Result.success(examPaperService.listByGradeId(gradeId));
    }

    /**
     * 根据学科和年级获取试卷
     */
    @GetMapping("/subject-grade")
    @PermissionRequired(permissionCode = "EXAM_PAPER_QUERY")
    public Result listBySubjectAndGrade(
            @RequestParam Long subjectId,
            @RequestParam Long gradeId) {
        return Result.success(examPaperService.listBySubjectAndGrade(subjectId, gradeId));
    }

    /**
     * 根据ID获取试卷
     */
    @GetMapping("/{id}")
    @PermissionRequired(permissionCode = "EXAM_PAPER_QUERY")
    public Result getById(@PathVariable Long id) {
        return Result.success(examPaperService.getById(id));
    }

    /**
     * 创建试卷
     */
    @PostMapping
    @PermissionRequired(permissionCode = "EXAM_PAPER_CREATE")
    public Result create(@RequestBody ExamPaper examPaper) {
        return Result.success(examPaperService.create(examPaper));
    }

    /**
     * 更新试卷
     */
    @PutMapping("/{id}")
    @PermissionRequired(permissionCode = "EXAM_PAPER_UPDATE")
    public Result update(@PathVariable Long id, @RequestBody ExamPaper examPaper) {
        examPaper.setId(id);
        return Result.success(examPaperService.update(examPaper));
    }

    /**
     * 删除试卷
     */
    @DeleteMapping("/{id}")
    @PermissionRequired(permissionCode = "EXAM_PAPER_DELETE")
    public Result delete(@PathVariable Long id) {
        examPaperService.delete(id);
        return Result.success();
    }

}