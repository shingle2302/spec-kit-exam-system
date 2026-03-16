package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.ClassEntity;
import com.spec.kit.exam.system.mapper.ClassMapper;
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
class ClassServiceTest {

    @Mock
    private ClassMapper classMapper;

    @InjectMocks
    private ClassService classService;

    @Test
    void createShouldSuccessfullyCreateClass() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Class 1");

        // Act
        ClassEntity result = classService.create(classEntity);

        // Assert
        assertNotNull(result);
        assertEquals("Class 1", result.getName());
        assertEquals("ACTIVE", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(classMapper).insert(result);
    }

    @Test
    void createShouldSetStatusToProvidedValue() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Class 1");
        classEntity.setStatus("INACTIVE");

        // Act
        ClassEntity result = classService.create(classEntity);

        // Assert
        assertEquals("INACTIVE", result.getStatus());
        verify(classMapper).insert(result);
    }

    @Test
    void queryPageShouldReturnPaginationResult() {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("page", 2);
        req.put("size", 5);
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Class");
        filters.put("gradeId", "1");
        filters.put("educationalLevelId", "2");
        req.put("filters", filters);

        List<Map<String, Object>> records = List.of(
            Map.of("id", 1L, "name", "Class 1"),
            Map.of("id", 2L, "name", "Class 2")
        );

        when(classMapper.queryPage("Class", 1L, 2L, 5, 5)).thenReturn(records);
        when(classMapper.countQuery("Class", 1L, 2L)).thenReturn(11L);

        // Act
        Map<String, Object> result = classService.queryPage(req);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.get("current"));
        assertEquals(5, result.get("size"));
        assertEquals(11L, result.get("total"));
        assertEquals(3L, result.get("pages"));
        assertEquals(2, ((List<?>) result.get("records")).size());
    }

    @Test
    void getDetailShouldReturnClassDetail() {
        // Arrange
        Map<String, Object> detail = Map.of("id", 1L, "name", "Class 1", "gradeName", "Grade 1");
        when(classMapper.selectClassDetail(1L)).thenReturn(detail);

        // Act
        Map<String, Object> result = classService.getDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Class 1", result.get("name"));
        assertEquals("Grade 1", result.get("gradeName"));
    }

    @Test
    void updateShouldSuccessfullyUpdateClass() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Updated Class");

        when(classMapper.updateById(any(ClassEntity.class))).thenReturn(1);

        // Act
        boolean result = classService.update(1L, classEntity);

        // Assert
        assertTrue(result);
        assertEquals(1L, classEntity.getId());
        assertNotNull(classEntity.getUpdatedAt());
        verify(classMapper).updateById(classEntity);
    }

    @Test
    void updateShouldReturnFalseWhenUpdateFails() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Updated Class");

        when(classMapper.updateById(any(ClassEntity.class))).thenReturn(0);

        // Act
        boolean result = classService.update(1L, classEntity);

        // Assert
        assertFalse(result);
    }

    @Test
    void deleteShouldSuccessfullyDeleteClass() {
        // Arrange
        when(classMapper.deleteById(1L)).thenReturn(1);

        // Act
        boolean result = classService.delete(1L);

        // Assert
        assertTrue(result);
        verify(classMapper).deleteById(1L);
    }

    @Test
    void deleteShouldReturnFalseWhenDeleteFails() {
        // Arrange
        when(classMapper.deleteById(1L)).thenReturn(0);

        // Act
        boolean result = classService.delete(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void getGradesShouldReturnActiveGrades() {
        // Arrange
        List<Map<String, Object>> grades = List.of(
            Map.of("id", 1L, "name", "Grade 1"),
            Map.of("id", 2L, "name", "Grade 2")
        );
        when(classMapper.selectActiveGrades()).thenReturn(grades);

        // Act
        List<Map<String, Object>> result = classService.getGrades();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Grade 1", result.get(0).get("name"));
    }
}