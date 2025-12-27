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

@Document(indexName = "questions")
@TableName("questions")
public class Question {
    
    @Id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @Field(type = FieldType.Text, name = "question_text")
    @TableField("question_text")
    private String questionText;
    
    @Field(type = FieldType.Keyword, name = "question_type")
    @TableField("question_type")
    private QuestionType questionType;
    
    @Field(type = FieldType.Keyword, name = "subject_id")
    @TableField("subject_id")
    private String subjectId;
    
    @Field(type = FieldType.Keyword, name = "grade_id")
    @TableField("grade_id")
    private String gradeId;
    
    @Field(type = FieldType.Text, name = "knowledge_point")
    @TableField("knowledge_point")
    private String knowledgePoint;
    
    @Field(type = FieldType.Text, name = "standard_explanation")
    @TableField("standard_explanation")
    private String standardExplanation;
    
    @Field(type = FieldType.Keyword, name = "created_by")
    @TableField("created_by")
    private String createdBy;
    
    @Field(type = FieldType.Date, name = "created_at")
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @Field(type = FieldType.Date, name = "updated_at")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    
    @Field(type = FieldType.Boolean, name = "is_active")
    @TableField("is_active")
    private Boolean isActive;

    public enum QuestionType {
        multiple_choice, true_false, short_answer, essay, single_choice
    }

    public Question() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
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
}