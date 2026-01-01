package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeacherModelTest {

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
    }

    @Test
    void testTeacherCreation() {
        assertNotNull(teacher);
    }

    @Test
    void testEmployeeIdAccessors() {
        String employeeId = "T12345";
        teacher.setEmployeeId(employeeId);
        assertEquals(employeeId, teacher.getEmployeeId());
    }

    @Test
    void testDepartmentAccessors() {
        String department = "Mathematics";
        teacher.setDepartment(department);
        assertEquals(department, teacher.getDepartment());
    }

    @Test
    void testHireDateAccessors() {
        LocalDate hireDate = LocalDate.now();
        teacher.setHireDate(hireDate);
        assertEquals(hireDate, teacher.getHireDate());
    }

    @Test
    void testUserIdAccessors() {
        Long userId = 1L;
        teacher.setUserId(userId);
        assertEquals(userId, teacher.getUserId());
    }

    @Test
    void testTeacherConstructor() {
        Teacher newTeacher = new Teacher();
        assertNotNull(newTeacher);
    }
}