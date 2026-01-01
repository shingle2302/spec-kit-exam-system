package com.exam.system.service;

import com.exam.system.model.ClassEnrollment;
import com.exam.system.repository.ClassEnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentTransferServiceTest {

    @Mock
    private ClassEnrollmentRepository classEnrollmentRepository;

    @InjectMocks
    private StudentTransferService studentTransferService;

    private ClassEnrollment currentEnrollment;

    @BeforeEach
    void setUp() {
        currentEnrollment = new ClassEnrollment();
        currentEnrollment.setId(1L);
        currentEnrollment.setStudentId(1L);
        currentEnrollment.setClassId(2L);
        currentEnrollment.setStatus(ClassEnrollment.Status.ACTIVE);
        currentEnrollment.setEnrollmentDate(LocalDateTime.now());
    }

    @Test
    void transferStudent_WhenStudentExists_ShouldReturnTrue() {
        // Given
        when(classEnrollmentRepository.findByStudentIdAndStatus(1L, ClassEnrollment.Status.ACTIVE))
            .thenReturn(currentEnrollment);
        when(classEnrollmentRepository.updateById(any(ClassEnrollment.class))).thenReturn(1);
        when(classEnrollmentRepository.insert(any(ClassEnrollment.class))).thenReturn(1);

        // When
        boolean result = studentTransferService.transferStudent(1L, 3L);

        // Then
        assertTrue(result);
        verify(classEnrollmentRepository).updateById(any(ClassEnrollment.class));
        verify(classEnrollmentRepository).insert(any(ClassEnrollment.class));
    }

    @Test
    void transferStudent_WhenStudentDoesNotExist_ShouldReturnFalse() {
        // Given
        when(classEnrollmentRepository.findByStudentIdAndStatus(1L, ClassEnrollment.Status.ACTIVE))
            .thenReturn(null);

        // When
        boolean result = studentTransferService.transferStudent(1L, 3L);

        // Then
        assertFalse(result);
        verify(classEnrollmentRepository, never()).updateById(any(ClassEnrollment.class));
        verify(classEnrollmentRepository, never()).insert(any(ClassEnrollment.class));
    }
}