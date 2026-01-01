package com.exam.system.controller;

import com.exam.system.dto.TestAssignmentDTO;
import com.exam.system.service.TestAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test-assignments")
public class TestAssignmentController {

    @Autowired
    private TestAssignmentService testAssignmentService;

    @PostMapping
    public ResponseEntity<TestAssignmentDTO> createTestAssignment(@RequestBody TestAssignmentDTO testAssignmentDTO) {
        TestAssignmentDTO createdAssignment = testAssignmentService.createTestAssignment(testAssignmentDTO);
        return ResponseEntity.ok(createdAssignment);
    }

    @PostMapping("/bulk-assign")
    public ResponseEntity<List<TestAssignmentDTO>> bulkAssignTestToStudents(@RequestBody BulkTestAssignmentRequest request) {
        List<TestAssignmentDTO> assignments = request.getStudentIds().stream()
            .map(studentId -> {
                TestAssignmentDTO assignmentDTO = new TestAssignmentDTO();
                assignmentDTO.setTestId(request.getTestId());
                assignmentDTO.setStudentId(studentId);
                assignmentDTO.setAssignedBy(request.getAssignedBy());
                assignmentDTO.setDueDate(request.getDueDate());
                assignmentDTO.setStatus("ASSIGNED");
                return testAssignmentService.createTestAssignment(assignmentDTO);
            })
            .toList();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestAssignmentDTO> getTestAssignmentById(@PathVariable Long id) {
        TestAssignmentDTO assignment = testAssignmentService.getTestAssignmentById(id);
        if (assignment != null) {
            return ResponseEntity.ok(assignment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TestAssignmentDTO>> getTestAssignmentsForStudent(@PathVariable Long studentId) {
        List<TestAssignmentDTO> assignments = testAssignmentService.getTestAssignmentsForStudent(studentId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<List<TestAssignmentDTO>> getTestAssignmentsForTest(@PathVariable Long testId) {
        List<TestAssignmentDTO> assignments = testAssignmentService.getTestAssignmentsForTest(testId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<TestAssignmentDTO>> getTestAssignmentsForStudentByStatus(
            @PathVariable Long studentId, @PathVariable String status) {
        List<TestAssignmentDTO> assignments = testAssignmentService.getTestAssignmentsForStudentByStatus(studentId, status);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TestAssignmentDTO>> getTestAssignmentsByTeacher(@PathVariable Long teacherId) {
        List<TestAssignmentDTO> assignments = testAssignmentService.getTestAssignmentsByTeacher(teacherId);
        return ResponseEntity.ok(assignments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestAssignmentDTO> updateTestAssignment(
            @PathVariable Long id, @RequestBody TestAssignmentDTO testAssignmentDTO) {
        TestAssignmentDTO updatedAssignment = testAssignmentService.updateTestAssignment(id, testAssignmentDTO);
        if (updatedAssignment != null) {
            return ResponseEntity.ok(updatedAssignment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestAssignment(@PathVariable Long id) {
        boolean deleted = testAssignmentService.deleteTestAssignment(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Inner class for bulk assignment request
    public static class BulkTestAssignmentRequest {
        private Long testId;
        private List<Long> studentIds;
        private Long assignedBy; // Teacher ID
        private java.time.LocalDateTime dueDate;

        // Getters and setters
        public Long getTestId() {
            return testId;
        }

        public void setTestId(Long testId) {
            this.testId = testId;
        }

        public List<Long> getStudentIds() {
            return studentIds;
        }

        public void setStudentIds(List<Long> studentIds) {
            this.studentIds = studentIds;
        }

        public Long getAssignedBy() {
            return assignedBy;
        }

        public void setAssignedBy(Long assignedBy) {
            this.assignedBy = assignedBy;
        }

        public java.time.LocalDateTime getDueDate() {
            return dueDate;
        }

        public void setDueDate(java.time.LocalDateTime dueDate) {
            this.dueDate = dueDate;
        }
    }
}