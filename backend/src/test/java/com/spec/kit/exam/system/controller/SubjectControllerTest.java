package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.service.SubjectService;
import com.spec.kit.exam.system.util.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @Test
    void createShouldReturnCreatedSubject() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Mathematics");

        SubjectEntity createdSubject = new SubjectEntity();
        createdSubject.setId(1L);
        createdSubject.setName("Mathematics");

        when(subjectService.create(subjectEntity)).thenReturn(createdSubject);

        // Act
        Result<SubjectEntity> result = subjectController.create(subjectEntity);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(createdSubject, result.getData());
    }

    @Test
    void queryShouldReturnPageResult() {
        // Arrange
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("records", List.of(Map.of("id", 1L, "name", "Mathematics")));
        expectedResult.put("total", 1L);
        expectedResult.put("current", 1);
        expectedResult.put("size", 10);
        expectedResult.put("pages", 1L);

        when(subjectService.queryPage(anyMap())).thenReturn(expectedResult);

        // Act
        Result<Map<String, Object>> result = subjectController.query(1, 10, "Math", 1L, 2L, "Advanced");

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result.getData());
    }

    @Test
    void detailShouldReturnSubjectDetail() {
        // Arrange
        Map<String, Object> detail = Map.of("id", 1L, "name", "Mathematics", "className", "Class 1");
        when(subjectService.getDetail(1L)).thenReturn(detail);

        // Act
        Result<Map<String, Object>> result = subjectController.detail(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(detail, result.getData());
    }

    @Test
    void updateShouldReturnSuccessWhenUpdateIsSuccessful() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Updated Mathematics");

        when(subjectService.update(1L, subjectEntity)).thenReturn(true);

        // Act
        Result<?> result = subjectController.update(1L, subjectEntity);

        // Assert
        assertNotNull(result);
        assertEquals("0000", result.getCode());
    }

    @Test
    void updateShouldThrowExceptionWhenSubjectNotFound() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Updated Mathematics");

        when(subjectService.update(1L, subjectEntity)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> subjectController.update(1L, subjectEntity));
    }

    @Test
    void deleteShouldReturnSuccessWhenDeleteIsSuccessful() {
        // Arrange
        when(subjectService.delete(1L)).thenReturn(true);

        // Act
        Result<?> result = subjectController.delete(1L);

        // Assert
        assertNotNull(result);
        assertEquals("0000", result.getCode());
    }

    @Test
    void deleteShouldThrowExceptionWhenSubjectNotFound() {
        // Arrange
        when(subjectService.delete(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> subjectController.delete(1L));
    }

    @Test
    void classesShouldReturnClassList() {
        // Arrange
        List<Map<String, Object>> classes = List.of(
            Map.of("id", 1L, "name", "Class 1"),
            Map.of("id", 2L, "name", "Class 2")
        );
        when(subjectService.getClasses()).thenReturn(classes);

        // Act
        Result<Object> result = subjectController.classes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(classes, result.getData());
    }

    @Test
    void levelsShouldReturnLevelList() {
        // Arrange
        List<Map<String, Object>> levels = List.of(
            Map.of("id", 1L, "name", "Primary"),
            Map.of("id", 2L, "name", "Secondary")
        );
        when(subjectService.getLevels()).thenReturn(levels);

        // Act
        Result<Object> result = subjectController.levels();

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(levels, result.getData());
    }
}