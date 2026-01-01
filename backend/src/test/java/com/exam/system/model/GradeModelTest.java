package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeModelTest {

    private Grade grade;

    @BeforeEach
    void setUp() {
        grade = new Grade();
    }

    @Test
    void testGradeCreation() {
        assertNotNull(grade);
    }

    @Test
    void testIdAccessors() {
        Long id = 1L;
        grade.setId(id);
        assertEquals(id, grade.getId());
    }

    @Test
    void testNameAccessors() {
        String name = "Grade 10";
        grade.setName(name);
        assertEquals(name, grade.getName());
    }

    @Test
    void testCodeAccessors() {
        String code = "G10";
        grade.setCode(code);
        assertEquals(code, grade.getCode());
    }

    @Test
    void testDescriptionAccessors() {
        String description = "Tenth Grade";
        grade.setDescription(description);
        assertEquals(description, grade.getDescription());
    }

    @Test
    void testMinAgeAccessors() {
        Integer minAge = 15;
        grade.setMinAge(minAge);
        assertEquals(minAge, grade.getMinAge());
    }

    @Test
    void testMaxAgeAccessors() {
        Integer maxAge = 16;
        grade.setMaxAge(maxAge);
        assertEquals(maxAge, grade.getMaxAge());
    }

    @Test
    void testIsActiveAccessors() {
        grade.setIsActive(true);
        assertTrue(grade.getIsActive());
        
        grade.setIsActive(false);
        assertFalse(grade.getIsActive());
    }

    @Test
    void testGradeConstructor() {
        Grade newGrade = new Grade();
        assertNotNull(newGrade);
        assertTrue(newGrade.getIsActive());
    }
}