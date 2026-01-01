package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StudentResponseModelTest {

    private StudentResponse studentResponse;

    @BeforeEach
    void setUp() {
        studentResponse = new StudentResponse();
    }

    @Test
    void testStudentResponseCreation() {
        assertNotNull(studentResponse);
    }

    @Test
    void testStudentIdAccessors() {
        Long studentId = 1L;
        studentResponse.setStudentId(studentId);
        assertEquals(studentId, studentResponse.getStudentId());
    }

    @Test
    void testTestIdAccessors() {
        Long testId = 2L;
        studentResponse.setTestId(testId);
        assertEquals(testId, studentResponse.getTestId());
    }

    @Test
    void testQuestionIdAccessors() {
        Long questionId = 3L;
        studentResponse.setQuestionId(questionId);
        assertEquals(questionId, studentResponse.getQuestionId());
    }

    @Test
    void testResponseTextAccessors() {
        String responseText = "My answer";
        studentResponse.setResponseText(responseText);
        assertEquals(responseText, studentResponse.getResponseText());
    }

    @Test
    void testSelectedOptionIndexAccessors() {
        Integer selectedOptionIndex = 0;
        studentResponse.setSelectedOptionIndex(selectedOptionIndex);
        assertEquals(selectedOptionIndex, studentResponse.getSelectedOptionIndex());
    }

    @Test
    void testResponseTimeAccessors() {
        LocalDateTime responseTime = LocalDateTime.now();
        studentResponse.setResponseTime(responseTime);
        assertEquals(responseTime, studentResponse.getResponseTime());
    }

    @Test
    void testIsCorrectAccessors() {
        studentResponse.setIsCorrect(true);
        assertTrue(studentResponse.getIsCorrect());
        
        studentResponse.setIsCorrect(false);
        assertFalse(studentResponse.getIsCorrect());
    }

    @Test
    void testManualGradeAccessors() {
        studentResponse.setManualGrade(true);
        assertTrue(studentResponse.getManualGrade());
    }

    @Test
    void testTeacherGradeAccessors() {
        Integer teacherGrade = 95;
        studentResponse.setTeacherGrade(teacherGrade);
        assertEquals(teacherGrade, studentResponse.getTeacherGrade());
    }

    @Test
    void testTeacherCommentsAccessors() {
        String teacherComments = "Good answer";
        studentResponse.setTeacherComments(teacherComments);
        assertEquals(teacherComments, studentResponse.getTeacherComments());
    }

    @Test
    void testSubmittedAtAccessors() {
        LocalDateTime submittedAt = LocalDateTime.now();
        studentResponse.setSubmittedAt(submittedAt);
        assertEquals(submittedAt, studentResponse.getSubmittedAt());
    }

    @Test
    void testGradedAtAccessors() {
        LocalDateTime gradedAt = LocalDateTime.now().plusMinutes(10);
        studentResponse.setGradedAt(gradedAt);
        assertEquals(gradedAt, studentResponse.getGradedAt());
    }

    @Test
    void testStudentResponseConstructor() {
        StudentResponse newResponse = new StudentResponse();
        assertNotNull(newResponse);
    }
}