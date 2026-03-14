package com.spec.kit.exam.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spec.kit.exam.system.entity.*;
import com.spec.kit.exam.system.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamWorkflowService {
    @Autowired private ExamPaperMapper examPaperMapper;
    @Autowired private ExamSessionMapper examSessionMapper;
    @Autowired private ExamInvigilationMapper examInvigilationMapper;
    @Autowired private ExamSubmissionMapper examSubmissionMapper;
    @Autowired private ExamAnalysisMapper examAnalysisMapper;

    public ExamPaper createPaper(ExamPaper paper) {
        paper.setStatus("DRAFT");
        paper.setCreatedAt(LocalDateTime.now());
        paper.setUpdatedAt(LocalDateTime.now());
        examPaperMapper.insert(paper);
        return paper;
    }

    public ExamSession scheduleExam(ExamSession session) {
        session.setStatus("SCHEDULED");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        examSessionMapper.insert(session);
        return session;
    }

    public ExamInvigilation startInvigilation(Long examSessionId, String teacherId) {
        ExamInvigilation inv = new ExamInvigilation();
        inv.setExamSessionId(examSessionId);
        inv.setTeacherId(teacherId);
        inv.setStatus("IN_PROGRESS");
        inv.setStartedAt(LocalDateTime.now());
        inv.setCreatedAt(LocalDateTime.now());
        inv.setUpdatedAt(LocalDateTime.now());
        examInvigilationMapper.insert(inv);

        ExamSession session = examSessionMapper.selectById(examSessionId);
        if (session != null) {
            session.setStatus("IN_PROGRESS");
            session.setUpdatedAt(LocalDateTime.now());
            examSessionMapper.updateById(session);
        }
        return inv;
    }

    public ExamInvigilation endInvigilation(Long examSessionId, String teacherId) {
        ExamInvigilation inv = examInvigilationMapper.selectOne(new LambdaQueryWrapper<ExamInvigilation>()
                .eq(ExamInvigilation::getExamSessionId, examSessionId)
                .eq(ExamInvigilation::getTeacherId, teacherId)
                .orderByDesc(ExamInvigilation::getId)
                .last("LIMIT 1"));
        if (inv == null) {
            throw new IllegalArgumentException("No invigilation found for examSessionId=" + examSessionId);
        }
        inv.setStatus("COMPLETED");
        inv.setEndedAt(LocalDateTime.now());
        inv.setUpdatedAt(LocalDateTime.now());
        examInvigilationMapper.updateById(inv);

        ExamSession session = examSessionMapper.selectById(examSessionId);
        if (session != null) {
            session.setStatus("WAITING_GRADE");
            session.setUpdatedAt(LocalDateTime.now());
            examSessionMapper.updateById(session);
        }
        return inv;
    }

    public ExamSubmission submitAnswer(ExamSubmission submission) {
        submission.setStatus("SUBMITTED");
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        examSubmissionMapper.insert(submission);
        return submission;
    }

    public ExamSubmission gradeSubmission(Long submissionId, BigDecimal score, String gradedBy) {
        ExamSubmission submission = examSubmissionMapper.selectById(submissionId);
        if (submission == null) {
            throw new IllegalArgumentException("Submission not found: " + submissionId);
        }
        submission.setScore(score);
        submission.setStatus("GRADED");
        submission.setGradedBy(gradedBy);
        submission.setGradedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());
        examSubmissionMapper.updateById(submission);
        return submission;
    }

    public ExamSession publishScore(Long examSessionId) {
        ExamSession session = examSessionMapper.selectById(examSessionId);
        if (session == null) {
            throw new IllegalArgumentException("Exam session not found: " + examSessionId);
        }
        session.setStatus("PUBLISHED");
        session.setPublishedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        examSessionMapper.updateById(session);
        return session;
    }

    public ExamAnalysis generateAnalysis(Long examSessionId) {
        List<ExamSubmission> submissions = examSubmissionMapper.selectList(new LambdaQueryWrapper<ExamSubmission>()
                .eq(ExamSubmission::getExamSessionId, examSessionId)
                .eq(ExamSubmission::getStatus, "GRADED"));

        if (submissions.isEmpty()) {
            throw new IllegalArgumentException("No graded submissions for exam session: " + examSessionId);
        }

        double avg = submissions.stream().map(ExamSubmission::getScore).mapToDouble(BigDecimal::doubleValue).average().orElse(0);
        long passCount = submissions.stream().filter(s -> s.getScore().compareTo(BigDecimal.valueOf(60)) >= 0).count();
        long excellentCount = submissions.stream().filter(s -> s.getScore().compareTo(BigDecimal.valueOf(90)) >= 0).count();
        long warningCount = submissions.stream().filter(s -> s.getScore().compareTo(BigDecimal.valueOf(60)) < 0).count();

        ExamAnalysis analysis = new ExamAnalysis();
        analysis.setExamSessionId(examSessionId);
        analysis.setAvgScore(BigDecimal.valueOf(avg).setScale(2, java.math.RoundingMode.HALF_UP));
        analysis.setPassRate(BigDecimal.valueOf(passCount * 100.0 / submissions.size()).setScale(2, java.math.RoundingMode.HALF_UP));
        analysis.setExcellentRate(BigDecimal.valueOf(excellentCount * 100.0 / submissions.size()).setScale(2, java.math.RoundingMode.HALF_UP));
        analysis.setWarningCount((int) warningCount);
        analysis.setRecommendation(warningCount > 0 ? "建议针对低分群体开展分层辅导并优化命题难度结构" : "整体表现稳定，可增加高阶能力题比重");
        analysis.setGeneratedAt(LocalDateTime.now());
        examAnalysisMapper.insert(analysis);
        return analysis;
    }

    public Map<String, Object> dashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("papers", examPaperMapper.selectCount(null));
        data.put("sessions", examSessionMapper.selectCount(null));
        data.put("submissions", examSubmissionMapper.selectCount(null));
        data.put("analyses", examAnalysisMapper.selectCount(null));
        return data;
    }
}
