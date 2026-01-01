package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AnswerOptionModelTest {

    private AnswerOption answerOption;

    @BeforeEach
    void setUp() {
        answerOption = new AnswerOption();
    }

    @Test
    void testAnswerOptionCreation() {
        assertNotNull(answerOption);
    }

    @Test
    void testQuestionIdAccessors() {
        Long questionId = 1L;
        answerOption.setQuestionId(questionId);
        assertEquals(questionId, answerOption.getQuestionId());
    }

    @Test
    void testOptionTextAccessors() {
        String optionText = "Option A";
        answerOption.setOptionText(optionText);
        assertEquals(optionText, answerOption.getOptionText());
    }

    @Test
    void testOptionIndexAccessors() {
        Integer optionIndex = 0;
        answerOption.setOptionIndex(optionIndex);
        assertEquals(optionIndex, answerOption.getOptionIndex());
    }

    @Test
    void testIsCorrectAccessors() {
        answerOption.setIsCorrect(true);
        assertTrue(answerOption.getIsCorrect());
        
        answerOption.setIsCorrect(false);
        assertFalse(answerOption.getIsCorrect());
    }

    @Test
    void testCreatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        answerOption.setCreatedAt(now);
        assertEquals(now, answerOption.getCreatedAt());
    }

    @Test
    void testAnswerOptionConstructor() {
        AnswerOption newOption = new AnswerOption();
        assertNotNull(newOption);
        assertNotNull(newOption.getCreatedAt());
    }
}