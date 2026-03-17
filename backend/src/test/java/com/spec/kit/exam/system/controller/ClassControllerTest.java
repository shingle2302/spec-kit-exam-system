package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.ClassEntity;
import com.spec.kit.exam.system.service.ClassService;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
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
class ClassControllerTest {

    @Mock
    private ClassService classService;

    @InjectMocks
    private ClassController classController;

    @Test
    void createShouldReturnCreatedClass() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Class 1");

        ClassEntity createdClass = new ClassEntity();
        createdClass.setId(1L);
        createdClass.setName("Class 1");

        when(classService.create(classEntity)).thenReturn(createdClass);

        // Act
        Result<ClassEntity> result = classController.create(classEntity);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(createdClass, result.getData());
    }

    @Test
    void queryShouldReturnPageResult() {
        // Arrange
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("records", List.of(Map.of("id", 1L, "name", "Class 1")));
        expectedResult.put("total", 1L);
        expectedResult.put("current", 1);
        expectedResult.put("size", 10);
        expectedResult.put("pages", 1L);

        when(classService.queryPage(anyMap())).thenReturn(expectedResult);

        // Act
        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPage(1);
        pageRequest.setSize(10);
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Class");
        filters.put("gradeId", 1L);
        filters.put("educationalLevelId", 2L);
        pageRequest.setFilters(filters);
        
        Result<PageResponse<Map<String, Object>>> result = classController.list(pageRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        PageResponse<Map<String, Object>> pageResponse = result.getData();
        assertEquals(1, pageResponse.getTotal());
        assertEquals(1, pageResponse.getPage());
        assertEquals(10, pageResponse.getSize());
        assertEquals(1, pageResponse.getData().size());
        assertEquals("Class 1", pageResponse.getData().get(0).get("name"));
    }

    @Test
    void detailShouldReturnClassDetail() {
        // Arrange
        Map<String, Object> detail = Map.of("id", 1L, "name", "Class 1", "gradeName", "Grade 1");
        when(classService.getDetail(1L)).thenReturn(detail);

        // Act
        Result<Map<String, Object>> result = classController.getById(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(detail, result.getData());
    }

    @Test
    void updateShouldReturnSuccessWhenUpdateIsSuccessful() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Updated Class");

        when(classService.update(1L, classEntity)).thenReturn(true);

        // Act
        Result<?> result = classController.update(1L, classEntity);

        // Assert
        assertNotNull(result);
        assertEquals("0000", result.getCode());
    }

    @Test
    void updateShouldThrowExceptionWhenClassNotFound() {
        // Arrange
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName("Updated Class");

        when(classService.update(1L, classEntity)).thenReturn(false);

        // Act
        Result<?> result = classController.update(1L, classEntity);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("班级不存在"));
    }

    @Test
    void deleteShouldReturnSuccessWhenDeleteIsSuccessful() {
        // Arrange
        when(classService.delete(1L)).thenReturn(true);

        // Act
        Result<?> result = classController.delete(1L);

        // Assert
        assertNotNull(result);
        assertEquals("0000", result.getCode());
    }

    @Test
    void deleteShouldThrowExceptionWhenClassNotFound() {
        // Arrange
        when(classService.delete(1L)).thenReturn(false);

        // Act
        Result<?> result = classController.delete(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("班级不存在"));
    }

    @Test
    void gradesShouldReturnGradeList() {
        // Arrange
        List<Map<String, Object>> grades = List.of(
            Map.of("id", 1L, "name", "Grade 1"),
            Map.of("id", 2L, "name", "Grade 2")
        );
        when(classService.getGrades()).thenReturn(grades);

        // Act
        Result<List<Map<String, Object>>> result = classController.getGrades();

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(grades, result.getData());
    }
}