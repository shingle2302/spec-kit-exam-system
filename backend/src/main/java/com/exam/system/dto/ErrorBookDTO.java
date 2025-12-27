package com.exam.system.dto;

import java.time.LocalDateTime;

public class ErrorBookDTO {
    private String id;
    private String studentId;
    private String questionId;
    private Integer errorCount;
    private Integer masteryCount;
    private Boolean mastered;
    private LocalDateTime firstIncorrectDate;
    private LocalDateTime lastPracticeDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ErrorBookDTO() {}

    public ErrorBookDTO(String id, String studentId, String questionId, Integer errorCount, 
                        Integer masteryCount, Boolean mastered, LocalDateTime firstIncorrectDate, 
                        LocalDateTime lastPracticeDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.questionId = questionId;
        this.errorCount = errorCount;
        this.masteryCount = masteryCount;
        this.mastered = mastered;
        this.firstIncorrectDate = firstIncorrectDate;
        this.lastPracticeDate = lastPracticeDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getMasteryCount() {
        return masteryCount;
    }

    public void setMasteryCount(Integer masteryCount) {
        this.masteryCount = masteryCount;
    }

    public Boolean getMastered() {
        return mastered;
    }

    public void setMastered(Boolean mastered) {
        this.mastered = mastered;
    }

    public LocalDateTime getFirstIncorrectDate() {
        return firstIncorrectDate;
    }

    public void setFirstIncorrectDate(LocalDateTime firstIncorrectDate) {
        this.firstIncorrectDate = firstIncorrectDate;
    }

    public LocalDateTime getLastPracticeDate() {
        return lastPracticeDate;
    }

    public void setLastPracticeDate(LocalDateTime lastPracticeDate) {
        this.lastPracticeDate = lastPracticeDate;
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
}