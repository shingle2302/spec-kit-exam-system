package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.*;
import com.spec.kit.exam.system.service.ExamWorkflowService;
import com.spec.kit.exam.system.util.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExamWorkflowControllerTest {

    @Mock
    private ExamWorkflowService examWorkflowService;

    @InjectMocks
    private ExamWorkflowController examWorkflowController;

    @Test
    void createPaperShouldReturnCreatedExamPaper() {
        // Arrange
        ExamPaper examPaper = new ExamPaper();
        examPaper.setName("Math Exam");

        ExamPaper createdPaper = new ExamPaper();
        createdPaper.setId(1L);
        createdPaper.setName("Math Exam");

        when(examWorkflowService.createPaper(examPaper)).thenReturn(createdPaper);

        // Act
        Result<ExamPaper> result = examWorkflowController.createPaper(examPaper);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(createdPaper, result.getData());
    }

    @Test
    void scheduleShouldReturnScheduledExamSession() {
        // Arrange
        ExamSession examSession = new ExamSession();
        examSession.setPaperId(1L);

        ExamSession scheduledSession = new ExamSession();
        scheduledSession.setId(1L);
        scheduledSession.setPaperId(1L);

        when(examWorkflowService.scheduleExam(examSession)).thenReturn(scheduledSession);

        // Act
        Result<ExamSession> result = examWorkflowController.schedule(examSession);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(scheduledSession, result.getData());
    }

    @Test
    void startInvigilationShouldReturnStartedInvigilation() {
        // Arrange
        ExamInvigilation invigilation = new ExamInvigilation();
        invigilation.setId(1L);
        invigilation.setExamSessionId(1L);
        invigilation.setTeacherId("teacher1");

        when(examWorkflowService.startInvigilation(1L, "teacher1")).thenReturn(invigilation);

        // Act
        Result<ExamInvigilation> result = examWorkflowController.startInvigilation(1L, "teacher1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(invigilation, result.getData());
    }

    @Test
    void endInvigilationShouldReturnEndedInvigilation() {
        // Arrange
        ExamInvigilation invigilation = new ExamInvigilation();
        invigilation.setId(1L);
        invigilation.setExamSessionId(1L);
        invigilation.setTeacherId("teacher1");

        when(examWorkflowService.endInvigilation(1L, "teacher1")).thenReturn(invigilation);

        // Act
        Result<ExamInvigilation> result = examWorkflowController.endInvigilation(1L, "teacher1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(invigilation, result.getData());
    }

    @Test
    void submitShouldReturnSubmittedExamSubmission() {
        // Arrange
        ExamSubmission submission = new ExamSubmission();
        submission.setExamSessionId(1L);
        submission.setStudentId("student1");

        ExamSubmission submittedSubmission = new ExamSubmission();
        submittedSubmission.setId(1L);
        submittedSubmission.setExamSessionId(1L);
        submittedSubmission.setStudentId("student1");

        when(examWorkflowService.submitAnswer(submission)).thenReturn(submittedSubmission);

        // Act
        Result<ExamSubmission> result = examWorkflowController.submit(submission);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(submittedSubmission, result.getData());
    }

    @Test
    void gradeShouldReturnGradedExamSubmission() {
        // Arrange
        ExamSubmission submission = new ExamSubmission();
        submission.setId(1L);
        submission.setScore(BigDecimal.valueOf(85));

        when(examWorkflowService.gradeSubmission(1L, BigDecimal.valueOf(85), "teacher1")).thenReturn(submission);

        // Act
        Result<ExamSubmission> result = examWorkflowController.grade(1L, BigDecimal.valueOf(85), "teacher1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(submission, result.getData());
    }

    @Test
    void publishShouldReturnPublishedExamSession() {
        // Arrange
        ExamSession session = new ExamSession();
        session.setId(1L);
        session.setStatus("PUBLISHED");

        when(examWorkflowService.publishScore(1L)).thenReturn(session);

        // Act
        Result<ExamSession> result = examWorkflowController.publish(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(session, result.getData());
    }

    @Test
    void analysisShouldReturnGeneratedExamAnalysis() {
        // Arrange
        ExamAnalysis analysis = new ExamAnalysis();
        analysis.setId(1L);
        analysis.setExamSessionId(1L);

        when(examWorkflowService.generateAnalysis(1L)).thenReturn(analysis);

        // Act
        Result<ExamAnalysis> result = examWorkflowController.analysis(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(analysis, result.getData());
    }

    @Test
    void dashboardShouldReturnDashboardData() {
        // Arrange
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("papers", 5L);
        dashboardData.put("sessions", 10L);
        dashboardData.put("submissions", 50L);
        dashboardData.put("analyses", 5L);

        when(examWorkflowService.dashboard()).thenReturn(dashboardData);

        // Act
        Result<Map<String, Object>> result = examWorkflowController.dashboard();

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(dashboardData, result.getData());
    }
}