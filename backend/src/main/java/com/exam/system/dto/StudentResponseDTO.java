package com.exam.system.dto;

import java.time.LocalDateTime;

public class StudentResponseDTO {
    private String id;
    private String studentId;
    private String testId;
    private String questionId;
    private String responseText;
    private Integer selectedOptionIndex;
    private LocalDateTime responseTime;
    private Boolean isCorrect;
    private Boolean manualGrade;
    private Integer teacherGrade;
    private String teacherComments;
    private LocalDateTime submittedAt;

    // Constructors
    public StudentResponseDTO() {}

    public StudentResponseDTO(String id, String studentId, String testId, String questionId, 
                              String responseText, Integer selectedOptionIndex, LocalDateTime responseTime, 
                              Boolean isCorrect, Boolean manualGrade, Integer teacherGrade, 
                              String teacherComments, LocalDateTime submittedAt) {
        this.id = id;
        this.studentId = studentId;
        this.testId = testId;
        this.questionId = questionId;
        this.responseText = responseText;
        this.selectedOptionIndex = selectedOptionIndex;
        this.responseTime = responseTime;
        this.isCorrect = isCorrect;
        this.manualGrade = manualGrade;
        this.teacherGrade = teacherGrade;
        this.teacherComments = teacherComments;
        this.submittedAt = submittedAt;
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

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
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
}