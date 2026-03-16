package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.ExamPlan;
import com.spec.kit.exam.system.service.ExamPlanService;
import com.spec.kit.exam.system.util.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExamPlanControllerTest {

    @Mock
    private ExamPlanService examPlanService;

    @InjectMocks
    private ExamPlanController examPlanController;

    @Test
    void createShouldReturnCreatedExamPlan() {
        // Arrange
        ExamPlan examPlan = new ExamPlan();
        examPlan.setName("Midterm Exam");

        ExamPlan createdExamPlan = new ExamPlan();
        createdExamPlan.setId(1L);
        createdExamPlan.setName("Midterm Exam");

        when(examPlanService.create(examPlan)).thenReturn(createdExamPlan);

        // Act
        Result<ExamPlan> result = examPlanController.create(examPlan);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(createdExamPlan, result.getData());
    }

    @Test
    void queryShouldReturnPageResult() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("page", 1);
        request.put("size", 10);

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("records", java.util.List.of(Map.of("id", 1L, "name", "Midterm Exam")));
        expectedResult.put("total", 1L);
        expectedResult.put("current", 1);
        expectedResult.put("size", 10);
        expectedResult.put("pages", 1L);

        when(examPlanService.queryPage(request)).thenReturn(expectedResult);

        // Act
        Result<Map<String, Object>> result = examPlanController.query(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result.getData());
    }

    @Test
    void detailShouldReturnExamPlanWhenFound() {
        // Arrange
        ExamPlan examPlan = new ExamPlan();
        examPlan.setId(1L);
        examPlan.setName("Midterm Exam");

        when(examPlanService.getById(1L)).thenReturn(examPlan);

        // Act
        Result<ExamPlan> result = examPlanController.detail(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(examPlan, result.getData());
    }

    @Test
    void detailShouldThrowExceptionWhenExamPlanNotFound() {
        // Arrange
        when(examPlanService.getById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> examPlanController.detail(1L));
    }

    @Test
    void updateShouldReturnSuccessWhenUpdateIsSuccessful() {
        // Arrange
        ExamPlan examPlan = new ExamPlan();
        examPlan.setName("Updated Midterm Exam");

        when(examPlanService.update(1L, examPlan)).thenReturn(true);

        // Act
        Result<?> result = examPlanController.update(1L, examPlan);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void updateShouldThrowExceptionWhenExamPlanNotFound() {
        // Arrange
        ExamPlan examPlan = new ExamPlan();
        examPlan.setName("Updated Midterm Exam");

        when(examPlanService.update(1L, examPlan)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> examPlanController.update(1L, examPlan));
    }

    @Test
    void deleteShouldReturnSuccessWhenDeleteIsSuccessful() {
        // Arrange
        when(examPlanService.delete(1L)).thenReturn(true);

        // Act
        Result<?> result = examPlanController.delete(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void deleteShouldThrowExceptionWhenExamPlanNotFound() {
        // Arrange
        when(examPlanService.delete(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> examPlanController.delete(1L));
    }
}