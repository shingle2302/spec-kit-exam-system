package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.mapper.SubjectMapper;
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
class SubjectServiceTest {

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void createShouldSuccessfullyCreateSubject() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Mathematics");

        // Act
        SubjectEntity result = subjectService.create(subjectEntity);

        // Assert
        assertNotNull(result);
        assertEquals("Mathematics", result.getName());
        assertEquals("ACTIVE", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(subjectMapper).insert(result);
    }

    @Test
    void createShouldSetStatusToProvidedValue() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Mathematics");
        subjectEntity.setStatus("INACTIVE");

        // Act
        SubjectEntity result = subjectService.create(subjectEntity);

        // Assert
        assertEquals("INACTIVE", result.getStatus());
        verify(subjectMapper).insert(result);
    }

    @Test
    void queryPageShouldReturnPaginationResult() {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("page", 2);
        req.put("size", 5);
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Math");
        filters.put("classId", "1");
        filters.put("educationalLevelId", "2");
        filters.put("specialization", "Advanced");
        req.put("filters", filters);

        List<Map<String, Object>> records = List.of(
            Map.of("id", 1L, "name", "Mathematics"),
            Map.of("id", 2L, "name", "Advanced Mathematics")
        );

        when(subjectMapper.queryPage("Math", 1L, 2L, "Advanced", 5, 5)).thenReturn(records);
        when(subjectMapper.countQuery("Math", 1L, 2L, "Advanced")).thenReturn(11L);

        // Act
        Map<String, Object> result = subjectService.queryPage(req);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.get("current"));
        assertEquals(5, result.get("size"));
        assertEquals(11L, result.get("total"));
        assertEquals(3L, result.get("pages"));
        assertEquals(2, ((List<?>) result.get("records")).size());
    }

    @Test
    void getDetailShouldReturnSubjectDetail() {
        // Arrange
        Map<String, Object> detail = Map.of("id", 1L, "name", "Mathematics", "className", "Class 1");
        when(subjectMapper.selectSubjectDetail(1L)).thenReturn(detail);

        // Act
        Map<String, Object> result = subjectService.getDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Mathematics", result.get("name"));
        assertEquals("Class 1", result.get("className"));
    }

    @Test
    void updateShouldSuccessfullyUpdateSubject() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Updated Mathematics");

        when(subjectMapper.updateById(any(SubjectEntity.class))).thenReturn(1);

        // Act
        boolean result = subjectService.update(1L, subjectEntity);

        // Assert
        assertTrue(result);
        assertEquals(1L, subjectEntity.getId());
        assertNotNull(subjectEntity.getUpdatedAt());
        verify(subjectMapper).updateById(subjectEntity);
    }

    @Test
    void updateShouldReturnFalseWhenUpdateFails() {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName("Updated Mathematics");

        when(subjectMapper.updateById(any(SubjectEntity.class))).thenReturn(0);

        // Act
        boolean result = subjectService.update(1L, subjectEntity);

        // Assert
        assertFalse(result);
    }

    @Test
    void deleteShouldSuccessfullyDeleteSubject() {
        // Arrange
        when(subjectMapper.deleteById(1L)).thenReturn(1);

        // Act
        boolean result = subjectService.delete(1L);

        // Assert
        assertTrue(result);
        verify(subjectMapper).deleteById(1L);
    }

    @Test
    void deleteShouldReturnFalseWhenDeleteFails() {
        // Arrange
        when(subjectMapper.deleteById(1L)).thenReturn(0);

        // Act
        boolean result = subjectService.delete(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void getClassesShouldReturnActiveClasses() {
        // Arrange
        List<Map<String, Object>> classes = List.of(
            Map.of("id", 1L, "name", "Class 1"),
            Map.of("id", 2L, "name", "Class 2")
        );
        when(subjectMapper.selectActiveClasses()).thenReturn(classes);

        // Act
        List<Map<String, Object>> result = subjectService.getClasses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Class 1", result.get(0).get("name"));
    }

    @Test
    void getLevelsShouldReturnActiveLevels() {
        // Arrange
        List<Map<String, Object>> levels = List.of(
            Map.of("id", 1L, "name", "Primary"),
            Map.of("id", 2L, "name", "Secondary")
        );
        when(subjectMapper.selectActiveLevels()).thenReturn(levels);

        // Act
        List<Map<String, Object>> result = subjectService.getLevels();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Primary", result.get(0).get("name"));
    }
}