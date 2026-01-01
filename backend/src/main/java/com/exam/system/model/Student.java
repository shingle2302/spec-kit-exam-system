package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("students")
public class Student extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("student_id")
    private String studentId;
    
    @TableField("grade_id")
    private Long gradeId;
    
    @TableField("class_id")
    private Long classId;
    
    @TableField("parent_id")
    private Long parentId;
    
    @TableField("user_id")
    private Long userId; // Reference to the user account

    public Student() {
        // Default constructor
    }



    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}