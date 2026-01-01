package com.exam.system.service;

import com.exam.system.model.TestAssignment;
import com.exam.system.repository.TestAssignmentRepository;
import com.exam.system.dto.TestAssignmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestAssignmentService {

    @Autowired
    private TestAssignmentRepository testAssignmentRepository;

    @Autowired
    private CacheService cacheService;

    public TestAssignmentDTO createTestAssignment(TestAssignmentDTO testAssignmentDTO) {
        // Create and populate the model
        TestAssignment testAssignment = new TestAssignment();
        testAssignment.setTestId(testAssignmentDTO.getTestId());
        testAssignment.setStudentId(testAssignmentDTO.getStudentId());
        testAssignment.setAssignedBy(testAssignmentDTO.getAssignedBy());
        testAssignment.setAssignedAt(testAssignmentDTO.getAssignedAt() != null ? 
            testAssignmentDTO.getAssignedAt() : LocalDateTime.now());
        testAssignment.setDueDate(testAssignmentDTO.getDueDate());
        testAssignment.setStatus(testAssignmentDTO.getStatus() != null ? 
            testAssignmentDTO.getStatus() : "ASSIGNED");

        // Insert the model
        testAssignmentRepository.insert(testAssignment);

        // Clear related cache entries
        String studentCacheKey = "test_assignments_student_" + testAssignment.getStudentId();
        String testCacheKey = "test_assignments_test_" + testAssignment.getTestId();
        cacheService.delete(studentCacheKey);
        cacheService.delete(testCacheKey);

        // Convert back to DTO and return
        return convertToDTO(testAssignment);
    }

    public TestAssignmentDTO getTestAssignmentById(Long id) {
        String cacheKey = "test_assignment_" + id;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (TestAssignmentDTO) cached;
        }
        
        TestAssignment testAssignment = testAssignmentRepository.selectById(id);
        TestAssignmentDTO result = convertToDTO(testAssignment);
        
        if (result != null) {
            cacheService.setEntity(cacheKey, result);
        }
        
        return result;
    }

    public List<TestAssignmentDTO> getTestAssignmentsForStudent(Long studentId) {
        String cacheKey = "test_assignments_student_" + studentId;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<TestAssignmentDTO>) cached;
        }
        
        List<TestAssignment> assignments = testAssignmentRepository.findByStudentId(studentId);
        List<TestAssignmentDTO> result = assignments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        cacheService.setList(cacheKey, result);
        
        return result;
    }

    public List<TestAssignmentDTO> getTestAssignmentsForTest(Long testId) {
        String cacheKey = "test_assignments_test_" + testId;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<TestAssignmentDTO>) cached;
        }
        
        List<TestAssignment> assignments = testAssignmentRepository.findByTestId(testId);
        List<TestAssignmentDTO> result = assignments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        cacheService.setList(cacheKey, result);
        
        return result;
    }

    public List<TestAssignmentDTO> getTestAssignmentsForStudentByStatus(Long studentId, String status) {
        String cacheKey = "test_assignments_student_" + studentId + "_status_" + status;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<TestAssignmentDTO>) cached;
        }
        
        List<TestAssignment> assignments = testAssignmentRepository.findByStudentIdAndStatus(studentId, status);
        List<TestAssignmentDTO> result = assignments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        cacheService.setList(cacheKey, result);
        
        return result;
    }

    public List<TestAssignmentDTO> getTestAssignmentsByTeacher(Long teacherId) {
        String cacheKey = "test_assignments_teacher_" + teacherId;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<TestAssignmentDTO>) cached;
        }
        
        List<TestAssignment> assignments = testAssignmentRepository.findByAssignedBy(teacherId);
        List<TestAssignmentDTO> result = assignments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        cacheService.setList(cacheKey, result);
        
        return result;
    }

    public TestAssignmentDTO updateTestAssignment(Long id, TestAssignmentDTO testAssignmentDTO) {
        TestAssignment existingAssignment = testAssignmentRepository.selectById(id);
        if (existingAssignment != null) {
            existingAssignment.setTestId(testAssignmentDTO.getTestId());
            existingAssignment.setStudentId(testAssignmentDTO.getStudentId());
            existingAssignment.setAssignedBy(testAssignmentDTO.getAssignedBy());
            existingAssignment.setAssignedAt(testAssignmentDTO.getAssignedAt());
            existingAssignment.setDueDate(testAssignmentDTO.getDueDate());
            existingAssignment.setStatus(testAssignmentDTO.getStatus());
            
            testAssignmentRepository.updateById(existingAssignment);
            
            // Clear related cache entries
            String studentCacheKey = "test_assignments_student_" + existingAssignment.getStudentId();
            String testCacheKey = "test_assignments_test_" + existingAssignment.getTestId();
            cacheService.delete("test_assignment_" + id);
            cacheService.delete(studentCacheKey);
            cacheService.delete(testCacheKey);
            
            return convertToDTO(existingAssignment);
        }
        return null;
    }

    public boolean deleteTestAssignment(Long id) {
        TestAssignment assignment = testAssignmentRepository.selectById(id);
        if (assignment != null) {
            int result = testAssignmentRepository.deleteById(id);
            
            if (result > 0) {
                // Clear related cache entries
                String studentCacheKey = "test_assignments_student_" + assignment.getStudentId();
                String testCacheKey = "test_assignments_test_" + assignment.getTestId();
                cacheService.delete("test_assignment_" + id);
                cacheService.delete(studentCacheKey);
                cacheService.delete(testCacheKey);
                
                return true;
            }
        }
        return false;
    }

    private TestAssignmentDTO convertToDTO(TestAssignment assignment) {
        if (assignment == null) {
            return null;
        }
        
        TestAssignmentDTO dto = new TestAssignmentDTO();
        dto.setId(assignment.getId());
        dto.setTestId(assignment.getTestId());
        dto.setStudentId(assignment.getStudentId());
        dto.setAssignedBy(assignment.getAssignedBy());
        dto.setAssignedAt(assignment.getAssignedAt());
        dto.setDueDate(assignment.getDueDate());
        dto.setStatus(assignment.getStatus());
        
        return dto;
    }
}