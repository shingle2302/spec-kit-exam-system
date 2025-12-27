package com.exam.system.controller;

import com.exam.system.dto.StudentDTO;
import com.exam.system.model.User;
import com.exam.system.service.AdminService;
import com.exam.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserService userService;
    


    @PostMapping("/students")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = adminService.createStudent(studentDTO);
        if (createdStudent != null) {
            return ResponseEntity.ok(createdStudent);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
}

