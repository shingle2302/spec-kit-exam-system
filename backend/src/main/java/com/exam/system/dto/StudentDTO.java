package com.exam.system.dto;

import java.time.LocalDateTime;

public class StudentDTO {
    private String id;
    private String studentId;
    private String gradeLevel;
    private String classId;
    private LocalDateTime enrollmentDate;
    private String parentId;

    // Constructors
    public StudentDTO() {}

    public StudentDTO(String id, String studentId, String gradeLevel, String classId, 
                      LocalDateTime enrollmentDate, String parentId) {
        this.id = id;
        this.studentId = studentId;
        this.gradeLevel = gradeLevel;
        this.classId = classId;
        this.enrollmentDate = enrollmentDate;
        this.parentId = parentId;
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

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}