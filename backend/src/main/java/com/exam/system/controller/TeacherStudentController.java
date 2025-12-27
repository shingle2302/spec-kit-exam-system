package com.exam.system.controller;

import com.exam.system.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherStudentController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/students")
    public ResponseEntity<List<String>> getMyStudents(@RequestParam String teacherId) {
        List<String> students = teacherService.getMyStudents(teacherId);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/students/add")
    public ResponseEntity<?> addStudentToClass(@RequestParam String teacherId, 
                                              @RequestParam String studentId, 
                                              @RequestParam String classId) {
        boolean success = teacherService.addStudentToClass(teacherId, studentId, classId);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}