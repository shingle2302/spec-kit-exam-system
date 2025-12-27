package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("class_enrollments")
public class ClassEnrollment {
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    
    @TableField("student_id")
    private String studentId;
    
    @TableField("class_id")
    private String classId;
    
    @TableField("enrollment_date")
    private LocalDateTime enrollmentDate;
    
    @TableField("transfer_date")
    private LocalDateTime transferDate;
    
    @TableField("previous_class_id")
    private String previousClassId;
    
    @TableField("status")
    private Status status;

    public enum Status {
        ACTIVE, TRANSFERRED, DROPPED
    }

    public ClassEnrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.status = Status.ACTIVE;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

    public String getPreviousClassId() {
        return previousClassId;
    }

    public void setPreviousClassId(String previousClassId) {
        this.previousClassId = previousClassId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}