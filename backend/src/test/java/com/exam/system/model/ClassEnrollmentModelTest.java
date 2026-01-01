package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClassEnrollmentModelTest {

    private ClassEnrollment classEnrollment;

    @BeforeEach
    void setUp() {
        classEnrollment = new ClassEnrollment();
    }

    @Test
    void testClassEnrollmentCreation() {
        assertNotNull(classEnrollment);
    }

    @Test
    void testStudentIdAccessors() {
        Long studentId = 1L;
        classEnrollment.setStudentId(studentId);
        assertEquals(studentId, classEnrollment.getStudentId());
    }

    @Test
    void testClassIdAccessors() {
        Long classId = 2L;
        classEnrollment.setClassId(classId);
        assertEquals(classId, classEnrollment.getClassId());
    }

    @Test
    void testEnrollmentDateAccessors() {
        LocalDateTime enrollmentDate = LocalDateTime.now();
        classEnrollment.setEnrollmentDate(enrollmentDate);
        assertEquals(enrollmentDate, classEnrollment.getEnrollmentDate());
    }

    @Test
    void testTransferDateAccessors() {
        LocalDateTime transferDate = LocalDateTime.now().plusDays(30);
        classEnrollment.setTransferDate(transferDate);
        assertEquals(transferDate, classEnrollment.getTransferDate());
    }

    @Test
    void testPreviousClassIdAccessors() {
        Long previousClassId = 3L;
        classEnrollment.setPreviousClassId(previousClassId);
        assertEquals(previousClassId, classEnrollment.getPreviousClassId());
    }

    @Test
    void testStatusAccessors() {
        ClassEnrollment.Status status = ClassEnrollment.Status.ACTIVE;
        classEnrollment.setStatus(status);
        assertEquals(status, classEnrollment.getStatus());
    }

    @Test
    void testClassEnrollmentConstructor() {
        ClassEnrollment newEnrollment = new ClassEnrollment();
        assertNotNull(newEnrollment);
        assertEquals(ClassEnrollment.Status.ACTIVE, newEnrollment.getStatus());
        assertNotNull(newEnrollment.getEnrollmentDate());
    }

    @Test
    void testStatusEnumValues() {
        ClassEnrollment.Status[] statuses = ClassEnrollment.Status.values();
        assertEquals(3, statuses.length);
        assertTrue(java.util.Arrays.asList(statuses).contains(ClassEnrollment.Status.ACTIVE));
        assertTrue(java.util.Arrays.asList(statuses).contains(ClassEnrollment.Status.TRANSFERRED));
        assertTrue(java.util.Arrays.asList(statuses).contains(ClassEnrollment.Status.DROPPED));
    }
}