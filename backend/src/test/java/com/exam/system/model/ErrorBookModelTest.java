package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorBookModelTest {

    private ErrorBook errorBook;

    @BeforeEach
    void setUp() {
        errorBook = new ErrorBook();
    }

    @Test
    void testErrorBookCreation() {
        assertNotNull(errorBook);
    }

    @Test
    void testStudentIdAccessors() {
        Long studentId = 1L;
        errorBook.setStudentId(studentId);
        assertEquals(studentId, errorBook.getStudentId());
    }

    @Test
    void testQuestionIdAccessors() {
        Long questionId = 2L;
        errorBook.setQuestionId(questionId);
        assertEquals(questionId, errorBook.getQuestionId());
    }

    @Test
    void testErrorCountAccessors() {
        Integer errorCount = 3;
        errorBook.setErrorCount(errorCount);
        assertEquals(errorCount, errorBook.getErrorCount());
    }

    @Test
    void testMasteryCountAccessors() {
        Integer masteryCount = 0;
        errorBook.setMasteryCount(masteryCount);
        assertEquals(masteryCount, errorBook.getMasteryCount());
    }

    @Test
    void testIsMasteredAccessors() {
        errorBook.setIsMastered(true);
        assertTrue(errorBook.getIsMastered());
        
        errorBook.setIsMastered(false);
        assertFalse(errorBook.getIsMastered());
    }

    @Test
    void testFirstIncorrectDateAccessors() {
        LocalDateTime firstIncorrectDate = LocalDateTime.now();
        errorBook.setFirstIncorrectDate(firstIncorrectDate);
        assertEquals(firstIncorrectDate, errorBook.getFirstIncorrectDate());
    }

    @Test
    void testLastPracticeDateAccessors() {
        LocalDateTime lastPracticeDate = LocalDateTime.now().plusDays(1);
        errorBook.setLastPracticeDate(lastPracticeDate);
        assertEquals(lastPracticeDate, errorBook.getLastPracticeDate());
    }

    @Test
    void testMasteryAchievedAtAccessors() {
        LocalDateTime masteryAchievedAt = LocalDateTime.now().plusDays(5);
        errorBook.setMasteryAchievedAt(masteryAchievedAt);
        assertEquals(masteryAchievedAt, errorBook.getMasteryAchievedAt());
    }

    @Test
    void testErrorBookConstructor() {
        ErrorBook newErrorBook = new ErrorBook();
        assertNotNull(newErrorBook);
        assertFalse(newErrorBook.getIsMastered());
    }
}