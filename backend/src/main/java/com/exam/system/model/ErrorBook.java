package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("error_books")
public class ErrorBook {
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @TableField("student_id")
    private String studentId;
    
    @TableField("question_id")
    private String questionId;
    
    @TableField("error_count")
    private Integer errorCount;
    
    @TableField("mastery_count")
    private Integer masteryCount;
    
    @TableField("mastered")
    private Boolean mastered;
    
    @TableField("first_incorrect_date")
    private LocalDateTime firstIncorrectDate;
    
    @TableField("last_practice_date")
    private LocalDateTime lastPracticeDate;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public ErrorBook() {
        this.errorCount = 0;
        this.masteryCount = 0;
        this.mastered = false;
        this.createdAt = LocalDateTime.now();
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