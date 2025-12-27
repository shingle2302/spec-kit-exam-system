package com.exam.system.service;

import com.exam.system.dto.StudentResponseDTO;
import com.exam.system.model.Question;
import com.exam.system.model.StudentResponse;
import com.exam.system.model.Test;
import com.exam.system.repository.QuestionRepository;
import com.exam.system.repository.StudentResponseElasticsearchRepository;
import com.exam.system.repository.StudentResponseRepository;
import com.exam.system.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestTakingService {

    @Autowired
    private StudentResponseRepository studentResponseRepository;
    
    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired(required = false)
    private StudentResponseElasticsearchRepository studentResponseElasticsearchRepository;

    public List<StudentResponseDTO> submitTest(String testId, String studentId, List<StudentResponseDTO> responses) {
        // Verify that the test exists and is assigned to the student
        Test test = testRepository.selectById(testId);
        if (test == null || !test.getAssignedTo().contains(studentId)) {
            throw new IllegalArgumentException("Test not found or not assigned to student");
        }

        // Process each response
        for (StudentResponseDTO responseDTO : responses) {
            // Generate ID if not provided
            if (responseDTO.getId() == null || responseDTO.getId().isEmpty()) {
                responseDTO.setId(UUID.randomUUID().toString());
            }

            // Create and populate the entity
            StudentResponse response = new StudentResponse();
            response.setId(responseDTO.getId());
            response.setTestId(testId);
            response.setStudentId(studentId);
            response.setQuestionId(responseDTO.getQuestionId());
            response.setResponseText(responseDTO.getResponseText());
            response.setSelectedOptionIndex(responseDTO.getSelectedOptionIndex());
            response.setSubmittedAt(LocalDateTime.now());

            // Insert the response
            studentResponseRepository.insert(response);

            // Also save to Elasticsearch if available
            if (studentResponseElasticsearchRepository != null) {
                try {
                    studentResponseElasticsearchRepository.save(response);
                } catch (Exception e) {
                    // Log the error but don't fail the operation
                    System.err.println("Failed to save student response to Elasticsearch: " + e.getMessage());
                }
            }
        }

        return responses;
    }

    public List<StudentResponse> getTestForStudent(String testId, String studentId) {
        return studentResponseRepository.findByStudentIdAndTestId(studentId, testId);
    }
    
    public Test getTestDetails(String testId) {
        return testRepository.selectById(testId);
    }
    
    public List<StudentResponse> searchStudentResponses(String query) {
        // Search in Elasticsearch if available - searching in response text and teacher comments
        if (studentResponseElasticsearchRepository != null) {
            try {
                return studentResponseElasticsearchRepository.findByResponseTextContainingOrTeacherCommentsContaining(query, query);
            } catch (Exception e) {
                // Log the error but return empty list
                System.err.println("Failed to search student responses in Elasticsearch: " + e.getMessage());
                return java.util.Collections.emptyList();
            }
        } else {
            // Return empty list if Elasticsearch is not available
            return java.util.Collections.emptyList();
        }
    }
}