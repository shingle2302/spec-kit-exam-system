package com.spec.kit.exam.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spec.kit.exam.system.entity.ExamPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {

    /**
     * 根据年级ID查询试卷
     */
    @Select("SELECT * FROM exam_papers WHERE grade_id = #{gradeId} AND status != 'DELETED' ORDER BY id")
    List<ExamPaper> selectByGradeId(Long gradeId);

    /**
     * 根据学科和年级查询试卷
     */
    @Select("SELECT * FROM exam_papers WHERE subject_id = #{subjectId} AND grade_id = #{gradeId} AND status != 'DELETED' ORDER BY id")
    List<ExamPaper> selectBySubjectAndGrade(Long subjectId, Long gradeId);

}