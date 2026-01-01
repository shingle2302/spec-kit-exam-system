package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuestionModelTest {

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question();
    }

    @Test
    void testQuestionCreation() {
        assertNotNull(question);
    }

    @Test
    void testQuestionTextAccessors() {
        String questionText = "What is 2+2?";
        question.setQuestionText(questionText);
        assertEquals(questionText, question.getQuestionText());
    }

    @Test
    void testQuestionTypeAccessors() {
        Question.QuestionType questionType = Question.QuestionType.multiple_choice;
        question.setQuestionType(questionType);
        assertEquals(questionType, question.getQuestionType());
    }

    @Test
    void testSubjectIdAccessors() {
        Long subjectId = 1L;
        question.setSubjectId(subjectId);
        assertEquals(subjectId, question.getSubjectId());
    }

    @Test
    void testGradeIdAccessors() {
        Long gradeId = 2L;
        question.setGradeId(gradeId);
        assertEquals(gradeId, question.getGradeId());
    }

    @Test
    void testKnowledgePointAccessors() {
        String knowledgePoint = "Basic arithmetic";
        question.setKnowledgePoint(knowledgePoint);
        assertEquals(knowledgePoint, question.getKnowledgePoint());
    }

    @Test
    void testStandardExplanationAccessors() {
        String explanation = "Explanation for the question";
        question.setStandardExplanation(explanation);
        assertEquals(explanation, question.getStandardExplanation());
    }

    @Test
    void testCreatedByAccessors() {
        String createdBy = "teacher1";
        question.setCreatedBy(createdBy);
        assertEquals(createdBy, question.getCreatedBy());
    }

    @Test
    void testCreatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        question.setCreatedAt(now);
        assertEquals(now, question.getCreatedAt());
    }

    @Test
    void testUpdatedAtAccessors() {
        LocalDateTime now = LocalDateTime.now();
        question.setUpdatedAt(now);
        assertEquals(now, question.getUpdatedAt());
    }

    @Test
    void testIsActiveAccessors() {
        question.setIsActive(true);
        assertTrue(question.getIsActive());
        
        question.setIsActive(false);
        assertFalse(question.getIsActive());
    }

    @Test
    void testQuestionConstructor() {
        Question newQuestion = new Question();
        assertNotNull(newQuestion);
        assertTrue(newQuestion.getIsActive());
        assertNotNull(newQuestion.getCreatedAt());
    }

    @Test
    void testQuestionTypeEnumValues() {
        Question.QuestionType[] types = Question.QuestionType.values();
        assertEquals(5, types.length);
        assertTrue(java.util.Arrays.asList(types).contains(Question.QuestionType.multiple_choice));
        assertTrue(java.util.Arrays.asList(types).contains(Question.QuestionType.true_false));
        assertTrue(java.util.Arrays.asList(types).contains(Question.QuestionType.short_answer));
        assertTrue(java.util.Arrays.asList(types).contains(Question.QuestionType.essay));
        assertTrue(java.util.Arrays.asList(types).contains(Question.QuestionType.single_choice));
    }
}