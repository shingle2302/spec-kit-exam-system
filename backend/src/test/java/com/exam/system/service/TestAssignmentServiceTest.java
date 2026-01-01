package com.exam.system.service;

import com.exam.system.dto.TestAssignmentDTO;
import com.exam.system.model.TestAssignment;
import com.exam.system.repository.TestAssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestAssignmentServiceTest {

    @Mock
    private TestAssignmentRepository testAssignmentRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private TestAssignmentService testAssignmentService;

    private TestAssignment testAssignment;
    private TestAssignmentDTO testAssignmentDTO;

    @BeforeEach
    void setUp() {
        testAssignment = new TestAssignment();
        testAssignment.setId(1L);
        testAssignment.setTestId(100L);
        testAssignment.setStudentId(200L);
        testAssignment.setAssignedBy(300L);
        testAssignment.setAssignedAt(LocalDateTime.now());
        testAssignment.setDueDate(LocalDateTime.now().plusDays(7));
        testAssignment.setStatus("ASSIGNED");

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
    void createTestAssignment_ShouldReturnTestAssignmentDTO() {
        // Given
        when(testAssignmentRepository.insert(any(TestAssignment.class))).thenReturn(1);

        // When
        TestAssignmentDTO result = testAssignmentService.createTestAssignment(testAssignmentDTO);

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getTestId());
        assertEquals(200L, result.getStudentId());
        assertEquals("ASSIGNED", result.getStatus());
        
        verify(testAssignmentRepository).insert(any(TestAssignment.class));
    }

    @Test
    void getTestAssignmentById_WhenAssignmentExists_ShouldReturnTestAssignmentDTO() {
        // Given
        when(cacheService.get("test_assignment_1")).thenReturn(null);
        when(testAssignmentRepository.selectById(1L)).thenReturn(testAssignment);

        // When
        TestAssignmentDTO result = testAssignmentService.getTestAssignmentById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cacheService).get("test_assignment_1");
        verify(cacheService).setEntity(anyString(), any(TestAssignmentDTO.class));
    }

    @Test
    void getTestAssignmentById_WhenAssignmentDoesNotExist_ShouldReturnNull() {
        // Given
        when(cacheService.get("test_assignment_1")).thenReturn(null);
        when(testAssignmentRepository.selectById(1L)).thenReturn(null);

        // When
        TestAssignmentDTO result = testAssignmentService.getTestAssignmentById(1L);

        // Then
        assertNull(result);
        verify(cacheService).get("test_assignment_1");
    }

    @Test
    void getTestAssignmentsForStudent_ShouldReturnListOfTestAssignmentDTOs() {
        // Given
        List<TestAssignment> assignments = Arrays.asList(testAssignment);
        when(cacheService.get("test_assignments_student_200")).thenReturn(null);
        when(testAssignmentRepository.findByStudentId(200L)).thenReturn(assignments);

        // When
        List<TestAssignmentDTO> result = testAssignmentService.getTestAssignmentsForStudent(200L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(cacheService).get("test_assignments_student_200");
        verify(cacheService).setList(anyString(), any(List.class));
    }

    @Test
    void updateTestAssignment_WhenAssignmentExists_ShouldReturnUpdatedTestAssignmentDTO() {
        // Given
        TestAssignment existingAssignment = new TestAssignment();
        existingAssignment.setId(1L);
        existingAssignment.setTestId(100L);
        existingAssignment.setStudentId(200L);
        existingAssignment.setAssignedBy(300L);
        existingAssignment.setAssignedAt(LocalDateTime.now());
        existingAssignment.setDueDate(LocalDateTime.now().plusDays(7));
        existingAssignment.setStatus("ASSIGNED");

        when(testAssignmentRepository.selectById(1L)).thenReturn(existingAssignment);
        when(testAssignmentRepository.updateById(any(TestAssignment.class))).thenReturn(1);

        // When
        TestAssignmentDTO result = testAssignmentService.updateTestAssignment(1L, testAssignmentDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(testAssignmentRepository).updateById(any(TestAssignment.class));
    }

    @Test
    void updateTestAssignment_WhenAssignmentDoesNotExist_ShouldReturnNull() {
        // Given
        when(testAssignmentRepository.selectById(1L)).thenReturn(null);

        // When
        TestAssignmentDTO result = testAssignmentService.updateTestAssignment(1L, testAssignmentDTO);

        // Then
        assertNull(result);
        verify(testAssignmentRepository).selectById(1L);
        verify(testAssignmentRepository, never()).updateById(any(TestAssignment.class));
    }

    @Test
    void deleteTestAssignment_WhenAssignmentExists_ShouldReturnTrue() {
        // Given
        when(testAssignmentRepository.selectById(1L)).thenReturn(testAssignment);
        when(testAssignmentRepository.deleteById(1L)).thenReturn(1);

        // When
        boolean result = testAssignmentService.deleteTestAssignment(1L);

        // Then
        assertTrue(result);
        verify(testAssignmentRepository).deleteById(1L);
    }

    @Test
    void deleteTestAssignment_WhenAssignmentDoesNotExist_ShouldReturnFalse() {
        // Given
        when(testAssignmentRepository.selectById(1L)).thenReturn(null);

        // When
        boolean result = testAssignmentService.deleteTestAssignment(1L);

        // Then
        assertFalse(result);
        verify(testAssignmentRepository).selectById(1L);
        verify(testAssignmentRepository, never()).deleteById(1L);
    }
}