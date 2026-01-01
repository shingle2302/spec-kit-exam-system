package com.exam.system.dto;

import com.exam.system.model.Question;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDTO {
    private Long id;
    private String questionText;
    private Question.QuestionType questionType;
    private Long subjectId;
    private Long gradeId;
    private String knowledgePoint;
    private String standardExplanation;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private List<AnswerOptionDTO> answerOptions;

    // Constructors
    public QuestionDTO() {}

    public QuestionDTO(Long id, String questionText, Question.QuestionType questionType, Long subjectId, 
                       Long gradeId, String knowledgePoint, String standardExplanation, String createdBy, 
                       LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isActive, 
                       List<AnswerOptionDTO> answerOptions) {
        this.id = id;
        this.questionText = questionText;
        this.questionType = questionType;
        this.subjectId = subjectId;
        this.gradeId = gradeId;
        this.knowledgePoint = knowledgePoint;
        this.standardExplanation = standardExplanation;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.answerOptions = answerOptions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Question.QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Question.QuestionType questionType) {
        this.questionType = questionType;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public String getStandardExplanation() {
        return standardExplanation;
    }

    public void setStandardExplanation(String standardExplanation) {
        this.standardExplanation = standardExplanation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<AnswerOptionDTO> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOptionDTO> answerOptions) {
        this.answerOptions = answerOptions;
    }
}