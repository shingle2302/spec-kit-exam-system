package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.ExamPlan;
import com.spec.kit.exam.system.mapper.ExamPlanMapper;
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

@ExtendWith(MockitoExtension.class)
class ExamPlanServiceTest {

    @Mock
    private ExamPlanMapper examPlanMapper;

    @InjectMocks
    private ExamPlanService examPlanService;

    @Test
    void createShouldSetDefaultStatusAndTimestamps() {
        ExamPlan examPlan = new ExamPlan();
        examPlan.setName("期中考试");

        when(examPlanMapper.insert(any(ExamPlan.class))).thenReturn(1);

        ExamPlan saved = examPlanService.create(examPlan);

        assertEquals("DRAFT", saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void queryPageShouldReturnPaginationResult() {
        Map<String, Object> request = new HashMap<>();
        request.put("page", 2);
        request.put("size", 5);
        request.put("filters", Map.of("name", "期中", "academicYear", "2025-2026", "examType", "MIDTERM"));

        when(examPlanMapper.queryPage(anyString(), anyString(), anyString(), eq(5), eq(5)))
                .thenReturn(List.of(Map.of("id", 1L, "name", "2026春季期中考试")));
        when(examPlanMapper.countQuery(anyString(), anyString(), anyString())).thenReturn(11L);

        Map<String, Object> result = examPlanService.queryPage(request);

        assertEquals(2, result.get("current"));
        assertEquals(5, result.get("size"));
        assertEquals(11L, result.get("total"));
        assertEquals(3L, result.get("pages"));
        assertEquals(1, ((List<?>) result.get("records")).size());
    }
}
