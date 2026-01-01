package com.exam.system.controller;

import com.exam.system.dto.TestAssignmentDTO;
import com.exam.system.service.TestAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestAssignmentControllerTest {

    @Mock
    private TestAssignmentService testAssignmentService;

    @InjectMocks
    private TestAssignmentController testAssignmentController;

    private TestAssignmentDTO testAssignmentDTO;

    @BeforeEach
    void setUp() {
        testAssignmentDTO = new TestAssignmentDTO();
        testAssignmentDTO.setId(1L);
        testAssignmentDTO.setTestId(100L);
        testAssignmentDTO.setStudentId(200L);
        testAssignmentDTO.setAssignedBy(300L);
        testAssignmentDTO.setAssignedAt(LocalDateTime.now());
        testAssignmentDTO.setDueDate(LocalDateTime.now().plusDays(7));
        testAssignmentDTO.setStatus("ASSIGNED");
    }

    @Test
    void createTestAssignment_ShouldReturnCreatedAssignment() {
        // Given
        when(testAssignmentService.createTestAssignment(any(TestAssignmentDTO.class))).thenReturn(testAssignmentDTO);

        // When
        ResponseEntity<TestAssignmentDTO> response = testAssignmentController.createTestAssignment(testAssignmentDTO);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(testAssignmentService).createTestAssignment(any(TestAssignmentDTO.class));
    }

    @Test
    void getTestAssignmentById_WhenAssignmentExists_ShouldReturnAssignment() {
        // Given
        when(testAssignmentService.getTestAssignmentById(anyLong())).thenReturn(testAssignmentDTO);

        // When
        ResponseEntity<TestAssignmentDTO> response = testAssignmentController.getTestAssignmentById(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(testAssignmentService).getTestAssignmentById(1L);
    }

    @Test
    void getTestAssignmentById_WhenAssignmentDoesNotExist_ShouldReturnNotFound() {
        // Given
        when(testAssignmentService.getTestAssignmentById(anyLong())).thenReturn(null);

        // When
        ResponseEntity<TestAssignmentDTO> response = testAssignmentController.getTestAssignmentById(1L);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(testAssignmentService).getTestAssignmentById(1L);
    }

    @Test
    void getTestAssignmentsForStudent_ShouldReturnListOfAssignments() {
        // Given
        List<TestAssignmentDTO> assignments = Arrays.asList(testAssignmentDTO);
        when(testAssignmentService.getTestAssignmentsForStudent(anyLong())).thenReturn(assignments);

        // When
        ResponseEntity<List<TestAssignmentDTO>> response = testAssignmentController.getTestAssignmentsForStudent(200L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(testAssignmentService).getTestAssignmentsForStudent(200L);
    }

    @Test
    void getTestAssignmentsForTest_ShouldReturnListOfAssignments() {
        // Given
        List<TestAssignmentDTO> assignments = Arrays.asList(testAssignmentDTO);
        when(testAssignmentService.getTestAssignmentsForTest(anyLong())).thenReturn(assignments);

        // When
        ResponseEntity<List<TestAssignmentDTO>> response = testAssignmentController.getTestAssignmentsForTest(100L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(testAssignmentService).getTestAssignmentsForTest(100L);
    }

    @Test
    void updateTestAssignment_WhenAssignmentExists_ShouldReturnUpdatedAssignment() {
        // Given
        when(testAssignmentService.updateTestAssignment(anyLong(), any(TestAssignmentDTO.class))).thenReturn(testAssignmentDTO);

        // When
        ResponseEntity<TestAssignmentDTO> response = testAssignmentController.updateTestAssignment(1L, testAssignmentDTO);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(testAssignmentService).updateTestAssignment(1L, testAssignmentDTO);
    }

    @Test
    void updateTestAssignment_WhenAssignmentDoesNotExist_ShouldReturnNotFound() {
        // Given
        when(testAssignmentService.updateTestAssignment(anyLong(), any(TestAssignmentDTO.class))).thenReturn(null);

        // When
        ResponseEntity<TestAssignmentDTO> response = testAssignmentController.updateTestAssignment(1L, testAssignmentDTO);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(testAssignmentService).updateTestAssignment(1L, testAssignmentDTO);
    }

    @Test
    void deleteTestAssignment_WhenAssignmentExists_ShouldReturnOk() {
        // Given
        when(testAssignmentService.deleteTestAssignment(anyLong())).thenReturn(true);

        // When
        ResponseEntity<Void> response = testAssignmentController.deleteTestAssignment(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        verify(testAssignmentService).deleteTestAssignment(1L);
    }

    @Test
    void deleteTestAssignment_WhenAssignmentDoesNotExist_ShouldReturnNotFound() {
        // Given
        when(testAssignmentService.deleteTestAssignment(anyLong())).thenReturn(false);

        // When
        ResponseEntity<Void> response = testAssignmentController.deleteTestAssignment(1L);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        verify(testAssignmentService).deleteTestAssignment(1L);
    }
}