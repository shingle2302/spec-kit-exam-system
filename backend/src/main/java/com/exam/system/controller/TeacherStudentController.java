package com.exam.system.controller;

import com.exam.system.model.Student;
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
    public ResponseEntity<List<Student>> getMyStudents(@RequestParam Long teacherId) {
        List<Student> students = teacherService.getMyStudents(teacherId);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/students/add")
    public ResponseEntity<?> addStudentToClass(@RequestParam Long teacherId, 
                                              @RequestParam Long studentId, 
                                              @RequestParam Long classId) {
        boolean success = teacherService.addStudentToClass(teacherId, studentId, classId);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}