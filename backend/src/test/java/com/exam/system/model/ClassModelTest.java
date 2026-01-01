package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClassModelTest {

    private Class classModel;

    @BeforeEach
    void setUp() {
        classModel = new Class();
    }

    @Test
    void testClassCreation() {
        assertNotNull(classModel);
    }

    @Test
    void testNameAccessors() {
        String name = "Mathematics Class A";
        classModel.setName(name);
        assertEquals(name, classModel.getName());
    }

    @Test
    void testGradeIdAccessors() {
        Long gradeId = 1L;
        classModel.setGradeId(gradeId);
        assertEquals(gradeId, classModel.getGradeId());
    }

    @Test
    void testTeacherIdAccessors() {
        Long teacherId = 2L;
        classModel.setTeacherId(teacherId);
        assertEquals(teacherId, classModel.getTeacherId());
    }

    @Test
    void testAcademicYearAccessors() {
        String academicYear = "2023-2024";
        classModel.setAcademicYear(academicYear);
        assertEquals(academicYear, classModel.getAcademicYear());
    }

    @Test
    void testMaxStudentsAccessors() {
        Integer maxStudents = 30;
        classModel.setMaxStudents(maxStudents);
        assertEquals(maxStudents, classModel.getMaxStudents());
    }

    @Test
    void testCreatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        classModel.setCreatedAt(now);
        assertEquals(now, classModel.getCreatedAt());
    }

    @Test
    void testUpdatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        classModel.setUpdatedAt(now);
        assertEquals(now, classModel.getUpdatedAt());
    }

    @Test
    void testClassConstructor() {
        Class newClass = new Class();
        assertNotNull(newClass);
        assertNotNull(newClass.getCreatedAt());
    }
}