package com.exam.system.controller;

import com.exam.system.dto.StudentResponseDTO;
import com.exam.system.model.Question;
import com.exam.system.model.Test;
import com.exam.system.model.TestAssignment;
import com.exam.system.repository.QuestionRepository;
import com.exam.system.repository.TestAssignmentRepository;
import com.exam.system.repository.TestQuestionRepository;
import com.exam.system.service.QuestionService;
import com.exam.system.service.TestTakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tests")
public class TestTakingController {

    @Autowired
    private TestTakingService testTakingService;
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private TestAssignmentRepository testAssignmentRepository;
    
    @Autowired
    private TestQuestionRepository testQuestionRepository;

    @GetMapping("/{testId}/take")
    public ResponseEntity<?> getTestForStudent(@PathVariable Long testId, @RequestParam Long studentId) {
        // Verify that the test exists
        Test test = testTakingService.getTestDetails(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the test is assigned to the student via TestAssignment
        TestAssignment testAssignment = testAssignmentRepository.findByTestIdAndStudentId(testId, studentId);
        if (testAssignment == null) {
            return ResponseEntity.notFound().build();
        }

        // Get the questions for this test via TestQuestion relationship
        List<Long> questionIds = testQuestionRepository.getQuestionIdsByTestId(testId);
        List<Question> questions = questionIds.stream()
            .map(questionId -> {
                // Need to get the question entity by ID
                // We'll use the repository directly since the service returns DTOs
                return questionRepository.findById(questionId);
            })
            .filter(Objects::nonNull)  // Filter out any null values
            .collect(Collectors.toList());

        // Create response with test details and questions
        Map<String, Object> response = new HashMap<>();
        response.put("test", test);
        response.put("questions", questions);
        response.put("timeLimitMinutes", test.getTimeLimitMinutes());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{testId}/submit")
    public ResponseEntity<?> submitTest(@PathVariable Long testId, 
                                       @RequestParam Long studentId,
                                       @RequestBody List<StudentResponseDTO> responses) {
        try {
            List<StudentResponseDTO> savedResponses = testTakingService.submitTest(testId, studentId, responses);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("gradedResponses", savedResponses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", Map.of("code", "SUBMISSION_ERROR", "message", e.getMessage()));
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/responses/search")
    public ResponseEntity<List<com.exam.system.model.StudentResponse>> searchStudentResponses(@RequestParam String query) {
        List<com.exam.system.model.StudentResponse> responses = testTakingService.searchStudentResponses(query);
        return ResponseEntity.ok(responses);
    }
}