package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentModelTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
    }

    @Test
    void testStudentCreation() {
        assertNotNull(student);
    }

    @Test
    void testStudentIdAccessors() {
        String studentId = "S12345";
        student.setStudentId(studentId);
        assertEquals(studentId, student.getStudentId());
    }

    @Test
    void testGradeIdAccessors() {
        Long gradeId = 1L;
        student.setGradeId(gradeId);
        assertEquals(gradeId, student.getGradeId());
    }

    @Test
    void testClassIdAccessors() {
        Long classId = 2L;
        student.setClassId(classId);
        assertEquals(classId, student.getClassId());
    }

    @Test
    void testParentIdAccessors() {
        Long parentId = 3L;
        student.setParentId(parentId);
        assertEquals(parentId, student.getParentId());
    }

    @Test
    void testUserIdAccessors() {
        Long userId = 4L;
        student.setUserId(userId);
        assertEquals(userId, student.getUserId());
    }

    @Test
    void testStudentConstructor() {
        Student newStudent = new Student();
        assertNotNull(newStudent);
    }
}