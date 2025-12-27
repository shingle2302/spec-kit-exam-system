package com.exam.system.service;

import com.exam.system.dto.StudentDTO;
import com.exam.system.model.Student;
import com.exam.system.model.User;
import com.exam.system.repository.StudentRepository;
import com.exam.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Generate ID if not provided
        if (studentDTO.getId() == null || studentDTO.getId().isEmpty()) {
            studentDTO.setId(UUID.randomUUID().toString());
        }

        // Create and populate the user entity
        User user = new User();
        user.setId(UUID.randomUUID().toString()); // User ID is separate from student ID
        user.setUsername(studentDTO.getStudentId());
        user.setRole(User.Role.STUDENT);
        user.setIsActive(true);
        
        // Set a default password that needs to be changed
        user.setPasswordHash(passwordEncoder.encode("defaultPassword123"));
        
        // Insert the user first
        userRepository.insert(user);
        User savedUser = user;
        
        // Create and populate the student entity
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setStudentId(studentDTO.getStudentId());
        student.setGradeLevel(studentDTO.getGradeLevel());
        student.setClassId(studentDTO.getClassId());
        student.setParentId(studentDTO.getParentId());
        student.setUser(savedUser);
        
        // Insert the student
        studentRepository.insert(student);
        Student savedStudent = student;

        // Convert back to DTO and return
        return convertToDTO(savedStudent);
    }

    private StudentDTO convertToDTO(Student student) {
        if (student == null) {
            return null;
        }
        
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setGradeLevel(student.getGradeLevel());
        dto.setClassId(student.getClassId());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setParentId(student.getParentId());
        
        return dto;
    }
}