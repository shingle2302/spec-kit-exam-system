package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestQuestionModelTest {

    private TestQuestion testQuestion;

    @BeforeEach
    void setUp() {
        testQuestion = new TestQuestion();
    }

    @Test
    void testTestQuestionCreation() {
        assertNotNull(testQuestion);
    }

    @Test
    void testTestIdAccessors() {
        Long testId = 1L;
        testQuestion.setTestId(testId);
        assertEquals(testId, testQuestion.getTestId());
    }

    @Test
    void testQuestionIdAccessors() {
        Long questionId = 2L;
        testQuestion.setQuestionId(questionId);
        assertEquals(questionId, testQuestion.getQuestionId());
    }

    @Test
    void testOrderAccessors() {
        Integer order = 1;
        testQuestion.setOrder(order);
        assertEquals(order, testQuestion.getOrder());
    }

    @Test
    void testPointsAccessors() {
        Integer points = 10;
        testQuestion.setPoints(points);
        assertEquals(points, testQuestion.getPoints());
    }

    @Test
    void testTestQuestionConstructor() {
        TestQuestion newTestQuestion = new TestQuestion();
        assertNotNull(newTestQuestion);
    }
}