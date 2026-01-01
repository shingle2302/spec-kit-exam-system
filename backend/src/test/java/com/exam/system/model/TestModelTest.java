package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TestModelTest {

    private com.exam.system.model.Test test;

    @BeforeEach
    void setUp() {
        test = new com.exam.system.model.Test();
    }

    @Test
    void testTestCreation() {
        assertNotNull(test);
    }

    @Test
    void testTitleAccessors() {
        String title = "Math Test 1";
        test.setTitle(title);
        assertEquals(title, test.getTitle());
    }

    @Test
    void testDescriptionAccessors() {
        String description = "First math test";
        test.setDescription(description);
        assertEquals(description, test.getDescription());
    }

    @Test
    void testGradeIdAccessors() {
        Long gradeId = 1L;
        test.setGradeId(gradeId);
        assertEquals(gradeId, test.getGradeId());
    }

    @Test
    void testSubjectIdAccessors() {
        Long subjectId = 2L;
        test.setSubjectId(subjectId);
        assertEquals(subjectId, test.getSubjectId());
    }

    @Test
    void testTimeLimitMinutesAccessors() {
        Integer timeLimit = 60;
        test.setTimeLimitMinutes(timeLimit);
        assertEquals(timeLimit, test.getTimeLimitMinutes());
    }

    @Test
    void testIsActiveAccessors() {
        test.setIsActive(true);
        assertTrue(test.getIsActive());
        
        test.setIsActive(false);
        assertFalse(test.getIsActive());
    }

    @Test
    void testIsPublishedAccessors() {
        test.setIsPublished(true);
        assertTrue(test.getIsPublished());
        
        test.setIsPublished(false);
        assertFalse(test.getIsPublished());
    }

    @Test
    void testPublishDateAccessors() {
        LocalDateTime publishDate = LocalDateTime.now();
        test.setPublishDate(publishDate);
        assertEquals(publishDate, test.getPublishDate());
    }

    @Test
    void testTestConstructor() {
        com.exam.system.model.Test newTest = new com.exam.system.model.Test();
        assertNotNull(newTest);
        assertTrue(newTest.getIsActive());
    }
}