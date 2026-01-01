package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TestAssignmentModelTest {

    private TestAssignment testAssignment;

    @BeforeEach
    void setUp() {
        testAssignment = new TestAssignment();
    }

    @Test
    void testTestAssignmentCreation() {
        assertNotNull(testAssignment);
    }

    @Test
    void testTestIdAccessors() {
        Long testId = 1L;
        testAssignment.setTestId(testId);
        assertEquals(testId, testAssignment.getTestId());
    }

    @Test
    void testStudentIdAccessors() {
        Long studentId = 2L;
        testAssignment.setStudentId(studentId);
        assertEquals(studentId, testAssignment.getStudentId());
    }

    @Test
    void testAssignedByAccessors() {
        Long assignedBy = 3L;
        testAssignment.setAssignedBy(assignedBy);
        assertEquals(assignedBy, testAssignment.getAssignedBy());
    }

    @Test
    void testAssignedAtAccessors() {
        LocalDateTime assignedAt = LocalDateTime.now();
        testAssignment.setAssignedAt(assignedAt);
        assertEquals(assignedAt, testAssignment.getAssignedAt());
    }

    @Test
    void testDueDateAccessors() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        testAssignment.setDueDate(dueDate);
        assertEquals(dueDate, testAssignment.getDueDate());
    }

    @Test
    void testStatusAccessors() {
        String status = "ASSIGNED";
        testAssignment.setStatus(status);
        assertEquals(status, testAssignment.getStatus());
    }

    @Test
    void testTestAssignmentConstructor() {
        TestAssignment newAssignment = new TestAssignment();
        assertNotNull(newAssignment);
        assertEquals("ASSIGNED", newAssignment.getStatus());
        assertNotNull(newAssignment.getAssignedAt());
    }
}