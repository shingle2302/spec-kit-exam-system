package com.exam.system.service;

import com.exam.system.dto.TestDTO;
import com.exam.system.model.Test;
import com.exam.system.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private com.exam.system.service.TestService testService;

    private com.exam.system.model.Test test;
    private TestDTO testDTO;

    @BeforeEach
    void setUp() {
        test = new com.exam.system.model.Test();
        test.setId(1L);
        test.setTitle("Math Test 1");
        test.setDescription("First math test");
        test.setGradeId(1L);
        test.setSubjectId(2L);
        test.setTimeLimitMinutes(60);
        test.setIsActive(true);
        test.setIsPublished(true);
        test.setPublishDate(LocalDateTime.now());

        testDTO = new TestDTO();
        testDTO.setId(1L);
        testDTO.setTitle("Math Test 1");
        testDTO.setDescription("First math test");
        testDTO.setGradeId(1L);
        testDTO.setSubjectId(2L);
        testDTO.setTimeLimitMinutes(60);
        testDTO.setIsActive(true);
        testDTO.setIsPublished(true);
        testDTO.setPublishDate(LocalDateTime.now());
    }

    @org.junit.jupiter.api.Test
    void createTest_ShouldReturnTestDTO() {
        // Given
        when(testRepository.insert(any(com.exam.system.model.Test.class))).thenReturn(1);

        // When
        TestDTO result = testService.createTest(testDTO);

        // Then
        assertNotNull(result);
        assertEquals("Math Test 1", result.getTitle());
        assertEquals(1L, result.getGradeId());
        verify(testRepository).insert(any(com.exam.system.model.Test.class));
    }

    @org.junit.jupiter.api.Test
    void getTestById_WhenTestExists_ShouldReturnTestDTO() {
        // Given
        when(cacheService.get("test_1")).thenReturn(null);
        when(testRepository.selectById(1L)).thenReturn(test);

        // When
        TestDTO result = testService.getTestById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cacheService).get("test_1");
        verify(cacheService).setEntity(anyString(), any(TestDTO.class));
    }

    @org.junit.jupiter.api.Test
    void getTestById_WhenTestDoesNotExist_ShouldReturnNull() {
        // Given
        when(cacheService.get("test_1")).thenReturn(null);
        when(testRepository.selectById(1L)).thenReturn(null);

        // When
        TestDTO result = testService.getTestById(1L);

        // Then
        assertNull(result);
        verify(cacheService).get("test_1");
    }

    @org.junit.jupiter.api.Test
    void getAllTests_ShouldReturnListOfTestDTOs() {
        // Given
        List<com.exam.system.model.Test> tests = Arrays.asList(test);
        when(cacheService.get("tests_all")).thenReturn(null);
        when(testRepository.selectList(null)).thenReturn(tests);

        // When
        List<TestDTO> result = testService.getAllTests();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(cacheService).get("tests_all");
        verify(cacheService).setList(anyString(), any(List.class));
    }

    @org.junit.jupiter.api.Test
    void updateTest_WhenTestExists_ShouldReturnUpdatedTestDTO() {
        // Given
        com.exam.system.model.Test existingTest = new com.exam.system.model.Test();
        existingTest.setId(1L);
        existingTest.setTitle("Math Test 1");
        existingTest.setDescription("First math test");
        existingTest.setGradeId(1L);
        existingTest.setSubjectId(2L);
        existingTest.setTimeLimitMinutes(60);
        existingTest.setIsActive(true);
        existingTest.setIsPublished(true);
        existingTest.setPublishDate(LocalDateTime.now());

        when(testRepository.selectById(1L)).thenReturn(existingTest);
        when(testRepository.updateById(any(com.exam.system.model.Test.class))).thenReturn(1);

        // When
        TestDTO result = testService.updateTest(1L, testDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(testRepository).updateById(any(com.exam.system.model.Test.class));
    }

    @org.junit.jupiter.api.Test
    void updateTest_WhenTestDoesNotExist_ShouldReturnNull() {
        // Given
        when(testRepository.selectById(1L)).thenReturn(null);

        // When
        TestDTO result = testService.updateTest(1L, testDTO);

        // Then
        assertNull(result);
        verify(testRepository).selectById(1L);
        verify(testRepository, never()).updateById(any(com.exam.system.model.Test.class));
    }

    @org.junit.jupiter.api.Test
    void deleteTest_WhenTestExists_ShouldReturnTrue() {
        // Given
        when(testRepository.selectById(1L)).thenReturn(test);
        when(testRepository.deleteById(1L)).thenReturn(1);

        // When
        boolean result = testService.deleteTest(1L);

        // Then
        assertTrue(result);
        verify(testRepository).deleteById(1L);
    }

    @org.junit.jupiter.api.Test
    void deleteTest_WhenTestDoesNotExist_ShouldReturnFalse() {
        // Given
        when(testRepository.selectById(1L)).thenReturn(null);

        // When
        boolean result = testService.deleteTest(1L);

        // Then
        assertFalse(result);
        verify(testRepository).selectById(1L);
        verify(testRepository, never()).deleteById(1L);
    }
}