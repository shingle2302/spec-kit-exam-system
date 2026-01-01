package com.exam.system.dto;

import java.time.LocalDateTime;

public class ErrorBookDTO {
    private Long id;
    private Long studentId;
    private Long questionId;
    private Integer errorCount;
    private Integer masteryCount;
    private Boolean isMastered;
    private LocalDateTime firstIncorrectDate;
    private LocalDateTime lastPracticeDate;
    private LocalDateTime masteryAchievedAt;

    // Constructors
    public ErrorBookDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
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

    public Boolean getIsMastered() {
        return isMastered;
    }

    public void setIsMastered(Boolean isMastered) {
        this.isMastered = isMastered;
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

    public LocalDateTime getMasteryAchievedAt() {
        return masteryAchievedAt;
    }

    public void setMasteryAchievedAt(LocalDateTime masteryAchievedAt) {
        this.masteryAchievedAt = masteryAchievedAt;
    }
}