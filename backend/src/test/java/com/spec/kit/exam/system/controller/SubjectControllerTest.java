package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.service.SubjectService;
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
        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPage(1);
        pageRequest.setSize(10);
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Math");
        filters.put("gradeId", 1L);
        filters.put("educationalLevelId", 2L);
        filters.put("level", "Advanced");
        pageRequest.setFilters(filters);
        
        Result<PageResponse<Map<String, Object>>> result = subjectController.list(pageRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        PageResponse<Map<String, Object>> pageResponse = result.getData();
        assertEquals(1, pageResponse.getTotal());
        assertEquals(1, pageResponse.getPage());
        assertEquals(10, pageResponse.getSize());
        assertEquals(1, pageResponse.getData().size());
        assertEquals("Mathematics", pageResponse.getData().get(0).get("name"));
    }

    @Test
    void detailShouldReturnSubjectDetail() {
        // Arrange
        Map<String, Object> detail = Map.of("id", 1L, "name", "Mathematics", "className", "Class 1");
        when(subjectService.getDetail(1L)).thenReturn(detail);

        // Act
        Result<Map<String, Object>> result = subjectController.getById(1L);

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

        // Act
        Result<?> result = subjectController.update(1L, subjectEntity);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("学科不存在"));
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

        // Act
        Result<?> result = subjectController.delete(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMsg().contains("学科不存在"));
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
        Result<List<Map<String, Object>>> result = subjectController.getClasses();

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
        Result<List<Map<String, Object>>> result = subjectController.getLevels();

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(levels, result.getData());
    }
}