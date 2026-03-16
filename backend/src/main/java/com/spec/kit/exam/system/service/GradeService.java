package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.GradeEntity;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;

import java.util.List;

/**
 * 年级服务接口
 */
public interface GradeService {

    /**
     * 获取年级列表
     */
    PageResponse<GradeEntity> list(PageRequest pageRequest);

    /**
     * 获取所有活跃年级
     */
    List<GradeEntity> listActive();

    /**
     * 根据ID获取年级
     */
    GradeEntity getById(Long id);

    /**
     * 创建年级
     */
    GradeEntity create(GradeEntity gradeEntity);

    /**
     * 更新年级
     */
    GradeEntity update(GradeEntity gradeEntity);

    /**
     * 删除年级
     */
    void delete(Long id);

}