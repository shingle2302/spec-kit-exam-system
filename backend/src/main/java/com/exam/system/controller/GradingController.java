package com.exam.system.controller;

import com.exam.system.dto.StudentResponseDTO;
import com.exam.system.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grading")
public class GradingController {

    @Autowired
    private GradingService gradingService;

    @GetMapping("/tests/{testId}/students/{studentId}")
    public ResponseEntity<List<StudentResponseDTO>> getSubmissionsForGrading(
            @PathVariable Long testId, 
            @PathVariable Long studentId) {
        // In a real implementation, this would return submissions for grading
        // For now, returning an empty list
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/tests/{testId}/students/{studentId}/grade")
    public ResponseEntity<List<StudentResponseDTO>> gradeTest(
            @PathVariable Long testId, 
            @PathVariable Long studentId) {
        List<StudentResponseDTO> gradedResponses = gradingService.gradeTestAutomatically(testId, studentId);
        return ResponseEntity.ok(gradedResponses);
    }
}