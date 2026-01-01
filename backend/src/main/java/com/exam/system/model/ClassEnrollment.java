package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("class_enrollments")
public class ClassEnrollment extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("class_id")
    private Long classId;
    
    @TableField("enrollment_date")
    private LocalDateTime enrollmentDate;
    
    @TableField("transfer_date")
    private LocalDateTime transferDate;
    
    @TableField("previous_class_id")
    private Long previousClassId;
    
    @TableField("status")
    private Status status;

    public enum Status {
        ACTIVE, TRANSFERRED, DROPPED
    }

    public ClassEnrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }



    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
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

    public Long getPreviousClassId() {
        return previousClassId;
    }

    public void setPreviousClassId(Long previousClassId) {
        this.previousClassId = previousClassId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}