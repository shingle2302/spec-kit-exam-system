package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Document(indexName = "student_responses")
@TableName("student_responses")
public class StudentResponse {
    
    @Id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @Field(type = FieldType.Keyword, name = "student_id")
    @TableField("student_id")
    private String studentId;
    
    @Field(type = FieldType.Keyword, name = "test_id")
    @TableField("test_id")
    private String testId;
    
    @Field(type = FieldType.Keyword, name = "question_id")
    @TableField("question_id")
    private String questionId;
    
    @Field(type = FieldType.Text, name = "response_text")
    @TableField("response_text")
    private String responseText;
    
    @Field(type = FieldType.Integer, name = "selected_option_index")
    @TableField("selected_option_index")
    private Integer selectedOptionIndex;
    
    @Field(type = FieldType.Date, name = "response_time")
    @TableField("response_time")
    private LocalDateTime responseTime;
    
    @Field(type = FieldType.Boolean, name = "is_correct")
    @TableField("is_correct")
    private Boolean isCorrect;
    
    @Field(type = FieldType.Boolean, name = "manual_grade")
    @TableField("manual_grade")
    private Boolean manualGrade;
    
    @Field(type = FieldType.Integer, name = "teacher_grade")
    @TableField("teacher_grade")
    private Integer teacherGrade;
    
    @Field(type = FieldType.Text, name = "teacher_comments")
    @TableField("teacher_comments")
    private String teacherComments;
    
    @Field(type = FieldType.Date, name = "submitted_at")
    @TableField("submitted_at")
    private LocalDateTime submittedAt;

    public StudentResponse() {
        this.responseTime = LocalDateTime.now();
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