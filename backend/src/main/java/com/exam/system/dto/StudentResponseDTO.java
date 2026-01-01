package com.exam.system.dto;

import java.time.LocalDateTime;

public class StudentResponseDTO {
    private Long id;
    private Long studentId;
    private Long testId;
    private Long questionId;
    private String responseText;
    private Integer selectedOptionIndex;
    private LocalDateTime responseTime;
    private Boolean isCorrect;
    private Boolean manualGrade;
    private Integer teacherGrade;
    private String teacherComments;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;

    // Constructors
    public StudentResponseDTO() {}

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

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public Integer getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public void setSelectedOptionIndex(Integer selectedOptionIndex) {
        this.selectedOptionIndex = selectedOptionIndex;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Boolean getManualGrade() {
        return manualGrade;
    }

    public void setManualGrade(Boolean manualGrade) {
        this.manualGrade = manualGrade;
    }

    public Integer getTeacherGrade() {
        return teacherGrade;
    }

    public void setTeacherGrade(Integer teacherGrade) {
        this.teacherGrade = teacherGrade;
    }

    public String getTeacherComments() {
        return teacherComments;
    }

    public void setTeacherComments(String teacherComments) {
        this.teacherComments = teacherComments;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }
}