package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.QuestionEntity;
import com.spec.kit.exam.system.service.QuestionService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试题控制器
 */
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController extends BaseController {

    private final QuestionService questionService;

    /**
     * 获取试题列表
     */
    @GetMapping
    @PermissionRequired(permissionCode = "QUESTION_QUERY")
    public Result list(PageRequest pageRequest) {
        return successList(questionService.list(pageRequest));
    }

    /**
     * 根据条件查询试题
     */
    @GetMapping("/condition")
    @PermissionRequired(permissionCode = "QUESTION_QUERY")
    public Result listByCondition(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long gradeId,
            @RequestParam(required = false) Long knowledgePointId) {
        return successList(questionService.listByCondition(subjectId, gradeId, knowledgePointId));
    }

    /**
     * 根据ID获取试题
     */
    @GetMapping("/{id}")
    @PermissionRequired(permissionCode = "QUESTION_QUERY")
    public Result getById(@PathVariable Long id) {
        return successDetail(questionService.getById(id));
    }

    /**
     * 创建试题
     */
    @PostMapping
    @PermissionRequired(permissionCode = "QUESTION_CREATE")
    public Result create(@RequestBody QuestionEntity questionEntity) {
        return successCreate(questionService.create(questionEntity));
    }

    /**
     * 更新试题
     */
    @PutMapping("/{id}")
    @PermissionRequired(permissionCode = "QUESTION_UPDATE")
    public Result update(@PathVariable Long id, @RequestBody QuestionEntity questionEntity) {
        questionEntity.setId(id);
        return successUpdate(questionService.update(questionEntity));
    }

    /**
     * 删除试题
     */
    @DeleteMapping("/{id}")
    @PermissionRequired(permissionCode = "QUESTION_DELETE")
    public Result delete(@PathVariable Long id) {
        questionService.delete(id);
        return successDelete();
    }

}