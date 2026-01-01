package com.exam.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;

@TableName("teachers")
public class Teacher extends BaseModel {
    
    // id field is inherited from BaseModel
    
    @TableField("employee_id")
    private String employeeId;
    
    @TableField("department")
    private String department;
    
    @TableField("hire_date")
    private java.time.LocalDate hireDate;
    
    @TableField("qualifications") // Assuming this is stored as JSON string
    private List<String> qualifications;
    
    @TableField("user_id")
    private Long userId; // Reference to the user account

    public Teacher() {
        // Default constructor
    }



    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public java.time.LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(java.time.LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public List<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}