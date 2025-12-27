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
import java.util.List;

@Document(indexName = "tests")
@TableName("tests")
public class Test {
    
    @Id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @Field(type = FieldType.Text, name = "title")
    @TableField("title")
    private String title;
    
    @Field(type = FieldType.Text, name = "description")
    @TableField("description")
    private String description;
    
    @Field(type = FieldType.Text, name = "questions")
    @TableField("questions") // Assuming this is stored as JSON string
    private List<String> questions;
    
    @Field(type = FieldType.Text, name = "assigned_to")
    @TableField("assigned_to") // Assuming this is stored as JSON string
    private List<String> assignedTo;
    
    @Field(type = FieldType.Keyword, name = "assigned_by")
    @TableField("assigned_by")
    private String assignedBy;
    
    @Field(type = FieldType.Integer, name = "time_limit_minutes")
    @TableField("time_limit_minutes")
    private Integer timeLimitMinutes;
    
    @Field(type = FieldType.Date, name = "due_date")
    @TableField("due_date")
    private LocalDateTime dueDate;
    
    @Field(type = FieldType.Date, name = "created_at")
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @Field(type = FieldType.Date, name = "updated_at")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    
    @Field(type = FieldType.Boolean, name = "is_active")
    @TableField("is_active")
    private Boolean isActive;
    
    @Field(type = FieldType.Boolean, name = "is_graded")
    @TableField("is_graded")
    private Boolean isGraded;

    public Test() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.isGraded = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(List<String> assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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

    public Boolean getIsGraded() {
        return isGraded;
    }

    public void setIsGraded(Boolean isGraded) {
        this.isGraded = isGraded;
    }
}