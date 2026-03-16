package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.GradeEntity;
import com.spec.kit.exam.system.service.GradeService;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 年级控制器
 */
@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController extends BaseController {

    private final GradeService gradeService;

    /**
     * 获取年级列表
     */
    @GetMapping
    @PermissionRequired(permissionCode = "GRADE_QUERY")
    public Result list(PageRequest pageRequest) {
        return successList(gradeService.list(pageRequest));
    }

    /**
     * 获取活跃年级列表
     */
    @GetMapping("/active")
    @PermissionRequired(permissionCode = "GRADE_QUERY")
    public Result listActive() {
        return successList(gradeService.listActive());
    }

    /**
     * 根据ID获取年级
     */
    @GetMapping("/{id}")
    @PermissionRequired(permissionCode = "GRADE_QUERY")
    public Result getById(@PathVariable Long id) {
        return successDetail(gradeService.getById(id));
    }

    /**
     * 创建年级
     */
    @PostMapping
    @PermissionRequired(permissionCode = "GRADE_CREATE")
    public Result create(@RequestBody GradeEntity gradeEntity) {
        return successCreate(gradeService.create(gradeEntity));
    }

    /**
     * 更新年级
     */
    @PutMapping("/{id}")
    @PermissionRequired(permissionCode = "GRADE_UPDATE")
    public Result update(@PathVariable Long id, @RequestBody GradeEntity gradeEntity) {
        gradeEntity.setId(id);
        return successUpdate(gradeService.update(gradeEntity));
    }

    /**
     * 删除年级
     */
    @DeleteMapping("/{id}")
    @PermissionRequired(permissionCode = "GRADE_DELETE")
    public Result delete(@PathVariable Long id) {
        gradeService.delete(id);
        return successDelete();
    }

}