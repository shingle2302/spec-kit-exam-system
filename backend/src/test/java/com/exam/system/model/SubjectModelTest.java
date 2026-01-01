package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubjectModelTest {

    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject();
    }

    @Test
    void testSubjectCreation() {
        assertNotNull(subject);
    }

    @Test
    void testNameAccessors() {
        String name = "Mathematics";
        subject.setName(name);
        assertEquals(name, subject.getName());
    }

    @Test
    void testCodeAccessors() {
        String code = "MATH";
        subject.setCode(code);
        assertEquals(code, subject.getCode());
    }

    @Test
    void testDescriptionAccessors() {
        String description = "Mathematics subject";
        subject.setDescription(description);
        assertEquals(description, subject.getDescription());
    }

    @Test
    void testIsActiveAccessors() {
        subject.setIsActive(true);
        assertTrue(subject.getIsActive());
        
        subject.setIsActive(false);
        assertFalse(subject.getIsActive());
    }

    @Test
    void testSubjectConstructor() {
        Subject newSubject = new Subject();
        assertNotNull(newSubject);
        assertTrue(newSubject.getIsActive());
    }
}