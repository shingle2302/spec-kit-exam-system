package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Document(indexName = "test_assignments")
@TableName("test_assignments")
public class TestAssignment extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @Field(type = FieldType.Keyword, name = "test_id")
    @TableField("test_id")
    private Long testId;
    
    @Field(type = FieldType.Keyword, name = "student_id")
    @TableField("student_id")
    private Long studentId;
    
    @Field(type = FieldType.Keyword, name = "assigned_by")
    @TableField("assigned_by") // Teacher ID
    private Long assignedBy;
    
    @Field(type = FieldType.Date, name = "assigned_at")
    @TableField("assigned_at")
    private LocalDateTime assignedAt;
    
    @Field(type = FieldType.Date, name = "due_date")
    @TableField("due_date")
    private LocalDateTime dueDate; // Optional
    
    @Field(type = FieldType.Keyword, name = "status")
    @TableField("status") // ASSIGNED, IN_PROGRESS, SUBMITTED, GRADED
    private String status;

    public TestAssignment() {
        this.assignedAt = LocalDateTime.now();
        this.status = "ASSIGNED";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}