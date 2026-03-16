package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.*;
import com.spec.kit.exam.system.mapper.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExamWorkflowServiceTest {

    @Mock
    private ExamPaperMapper examPaperMapper;

    @Mock
    private ExamSessionMapper examSessionMapper;

    @Mock
    private ExamInvigilationMapper examInvigilationMapper;

    @Mock
    private ExamSubmissionMapper examSubmissionMapper;

    @Mock
    private ExamAnalysisMapper examAnalysisMapper;

    @InjectMocks
    private ExamWorkflowService examWorkflowService;

    @Test
    void createPaperShouldSuccessfullyCreatePaper() {
        // Arrange
        ExamPaper paper = new ExamPaper();
        paper.setName("Math Exam");

        // Act
        ExamPaper result = examWorkflowService.createPaper(paper);

        // Assert
        assertNotNull(result);
        assertEquals("Math Exam", result.getName());
        assertEquals("DRAFT", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(examPaperMapper).insert(result);
    }

    @Test
    void scheduleExamShouldSuccessfullyScheduleExam() {
        // Arrange
        ExamSession session = new ExamSession();
        session.setPaperId(1L);
        session.setStartTime(LocalDateTime.now());

        // Act
        ExamSession result = examWorkflowService.scheduleExam(session);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getPaperId());
        assertEquals("SCHEDULED", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(examSessionMapper).insert(result);
    }

    @Test
    void startInvigilationShouldSuccessfullyStartInvigilation() {
        // Arrange
        ExamSession session = new ExamSession();
        session.setId(1L);

        when(examSessionMapper.selectById(1L)).thenReturn(session);

        // Act
        ExamInvigilation result = examWorkflowService.startInvigilation(1L, "teacher1");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getExamSessionId());
        assertEquals("teacher1", result.getTeacherId());
        assertEquals("IN_PROGRESS", result.getStatus());
        assertNotNull(result.getStartedAt());
        verify(examInvigilationMapper).insert(result);
        verify(examSessionMapper).updateById(session);
        assertEquals("IN_PROGRESS", session.getStatus());
    }

    @Test
    void endInvigilationShouldSuccessfullyEndInvigilation() {
        // Arrange
        ExamInvigilation invigilation = new ExamInvigilation();
        invigilation.setId(1L);
        invigilation.setExamSessionId(1L);
        invigilation.setTeacherId("teacher1");

        ExamSession session = new ExamSession();
        session.setId(1L);

        when(examInvigilationMapper.selectOne(any())).thenReturn(invigilation);
        when(examSessionMapper.selectById(1L)).thenReturn(session);

        // Act
        ExamInvigilation result = examWorkflowService.endInvigilation(1L, "teacher1");

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertNotNull(result.getEndedAt());
        verify(examInvigilationMapper).updateById(result);
        verify(examSessionMapper).updateById(session);
        assertEquals("WAITING_GRADE", session.getStatus());
    }

    @Test
    void submitAnswerShouldSuccessfullySubmitAnswer() {
        // Arrange
        ExamSubmission submission = new ExamSubmission();
        submission.setExamSessionId(1L);
        submission.setStudentId("student1");

        // Act
        ExamSubmission result = examWorkflowService.submitAnswer(submission);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getExamSessionId());
        assertEquals("student1", result.getStudentId());
        assertEquals("SUBMITTED", result.getStatus());
        assertNotNull(result.getSubmittedAt());
        verify(examSubmissionMapper).insert(result);
    }

    @Test
    void gradeSubmissionShouldSuccessfullyGradeSubmission() {
        // Arrange
        ExamSubmission submission = new ExamSubmission();
        submission.setId(1L);

        when(examSubmissionMapper.selectById(1L)).thenReturn(submission);

        // Act
        ExamSubmission result = examWorkflowService.gradeSubmission(1L, BigDecimal.valueOf(85), "teacher1");

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(85), result.getScore());
        assertEquals("GRADED", result.getStatus());
        assertEquals("teacher1", result.getGradedBy());
        assertNotNull(result.getGradedAt());
        verify(examSubmissionMapper).updateById(result);
    }

    @Test
    void publishScoreShouldSuccessfullyPublishScore() {
        // Arrange
        ExamSession session = new ExamSession();
        session.setId(1L);

        when(examSessionMapper.selectById(1L)).thenReturn(session);

        // Act
        ExamSession result = examWorkflowService.publishScore(1L);

        // Assert
        assertNotNull(result);
        assertEquals("PUBLISHED", result.getStatus());
        assertNotNull(result.getPublishedAt());
        verify(examSessionMapper).updateById(result);
    }

    @Test
    void generateAnalysisShouldSuccessfullyGenerateAnalysis() {
        // Arrange
        ExamSubmission submission1 = new ExamSubmission();
        submission1.setId(1L);
        submission1.setScore(BigDecimal.valueOf(85));

        ExamSubmission submission2 = new ExamSubmission();
        submission2.setId(2L);
        submission2.setScore(BigDecimal.valueOf(75));

        List<ExamSubmission> submissions = List.of(submission1, submission2);

        when(examSubmissionMapper.selectList(any())).thenReturn(submissions);

        // Act
        ExamAnalysis result = examWorkflowService.generateAnalysis(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getExamSessionId());
        assertNotNull(result.getAvgScore());
        assertNotNull(result.getPassRate());
        assertNotNull(result.getExcellentRate());
        assertNotNull(result.getRecommendation());
        assertNotNull(result.getGeneratedAt());
        verify(examAnalysisMapper).insert(result);
    }

    @Test
    void dashboardShouldReturnDashboardData() {
        // Arrange
        when(examPaperMapper.selectCount(null)).thenReturn(5L);
        when(examSessionMapper.selectCount(null)).thenReturn(10L);
        when(examSubmissionMapper.selectCount(null)).thenReturn(50L);
        when(examAnalysisMapper.selectCount(null)).thenReturn(5L);

        // Act
        Map<String, Object> result = examWorkflowService.dashboard();

        // Assert
        assertNotNull(result);
        assertEquals(5L, result.get("papers"));
        assertEquals(10L, result.get("sessions"));
        assertEquals(50L, result.get("submissions"));
        assertEquals(5L, result.get("analyses"));
    }
}