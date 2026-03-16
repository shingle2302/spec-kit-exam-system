package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.ExamPaper;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;

import java.util.List;

/**
 * 试卷服务接口
 */
public interface ExamPaperService {

    /**
     * 获取试卷列表
     */
    PageResponse<ExamPaper> list(PageRequest pageRequest);

    /**
     * 根据年级ID获取试卷
     */
    List<ExamPaper> listByGradeId(Long gradeId);

    /**
     * 根据学科和年级获取试卷
     */
    List<ExamPaper> listBySubjectAndGrade(Long subjectId, Long gradeId);

    /**
     * 根据ID获取试卷
     */
    ExamPaper getById(Long id);

    /**
     * 创建试卷
     */
    ExamPaper create(ExamPaper examPaper);

    /**
     * 更新试卷
     */
    ExamPaper update(ExamPaper examPaper);

    /**
     * 删除试卷
     */
    void delete(Long id);

}