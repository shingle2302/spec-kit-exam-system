package com.exam.system.dto;

import lombok.Data;

@Data
public class TeacherDTO {
    private Long id;
    private String employeeId;
    private String department;
    private java.time.LocalDate hireDate;
    private String qualifications;
    private String email;
    private String firstName;
    private String lastName;
    private Long userId; // Reference to the user account
}