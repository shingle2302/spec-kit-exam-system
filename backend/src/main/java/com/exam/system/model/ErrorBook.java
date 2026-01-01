package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("error_books")
public class ErrorBook extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("question_id")
    private Long questionId;
    
    @TableField("error_count")
    private Integer errorCount;
    
    @TableField("mastery_count")
    private Integer masteryCount;
    
    @TableField("is_mastered")
    private Boolean isMastered;
    
    @TableField("first_incorrect_date")
    private LocalDateTime firstIncorrectDate;
    
    @TableField("last_practice_date")
    private LocalDateTime lastPracticeDate;
    
    @TableField("mastery_achieved_at")
    private LocalDateTime masteryAchievedAt;

    public ErrorBook() {
        this.errorCount = 0;
        this.masteryCount = 0;
        this.isMastered = false;
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