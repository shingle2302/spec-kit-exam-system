package com.exam.system.dto;

import java.time.LocalDateTime;

public class AnswerOptionDTO {
    private String id;
    private String questionId;
    private String optionText;
    private Integer optionIndex;
    private Boolean isCorrect;
    private LocalDateTime createdAt;

    // Constructors
    public AnswerOptionDTO() {}

    public AnswerOptionDTO(String id, String questionId, String optionText, Integer optionIndex, 
                           Boolean isCorrect, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.optionText = optionText;
        this.optionIndex = optionIndex;
        this.isCorrect = isCorrect;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Integer getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(Integer optionIndex) {
        this.optionIndex = optionIndex;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}