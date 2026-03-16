package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.GradeEntity;
import com.spec.kit.exam.system.mapper.GradeMapper;
import com.spec.kit.exam.system.service.impl.GradeServiceImpl;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GradeServiceTest {

    @Mock
    private GradeMapper gradeMapper;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testList() {
        // 准备测试数据
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);
        List<GradeEntity> gradeList = new ArrayList<>();
        GradeEntity grade1 = new GradeEntity();
        grade1.setId(1L);
        grade1.setName("Grade 1");
        gradeList.add(grade1);

        // 模拟方法调用
        when(gradeMapper.selectPage(any(), any())).thenReturn(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<GradeEntity>() {
            {
                setRecords(gradeList);
                setTotal(1L);
            }
        });

        // 执行测试
        PageResponse<GradeEntity> result = gradeService.list(pageRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("Grade 1", result.getData().get(0).getName());
    }

    @Test
    void testListActive() {
        // 准备测试数据
        List<GradeEntity> gradeList = new ArrayList<>();
        GradeEntity grade1 = new GradeEntity();
        grade1.setId(1L);
        grade1.setName("Grade 1");
        gradeList.add(grade1);

        // 模拟方法调用
        when(gradeMapper.selectActiveGrades()).thenReturn(gradeList);

        // 执行测试
        List<GradeEntity> result = gradeService.listActive();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Grade 1", result.get(0).getName());
    }

    @Test
    void testGetById() {
        // 准备测试数据
        GradeEntity grade = new GradeEntity();
        grade.setId(1L);
        grade.setName("Grade 1");

        // 模拟方法调用
        when(gradeMapper.selectById(1L)).thenReturn(grade);

        // 执行测试
        GradeEntity result = gradeService.getById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("Grade 1", result.getName());
    }

    @Test
    void testCreate() {
        // 准备测试数据
        GradeEntity grade = new GradeEntity();
        grade.setName("Grade 1");

        // 模拟方法调用
        when(gradeMapper.insert(grade)).thenReturn(1);

        // 执行测试
        GradeEntity result = gradeService.create(grade);

        // 验证结果
        assertNotNull(result);
        assertEquals("Grade 1", result.getName());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        GradeEntity grade = new GradeEntity();
        grade.setId(1L);
        grade.setName("Grade 1 Updated");

        // 模拟方法调用
        when(gradeMapper.updateById(grade)).thenReturn(1);

        // 执行测试
        GradeEntity result = gradeService.update(grade);

        // 验证结果
        assertNotNull(result);
        assertEquals("Grade 1 Updated", result.getName());
    }

    @Test
    void testDelete() {
        // 模拟方法调用
        when(gradeMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        gradeService.delete(1L);

        // 验证方法被调用
        verify(gradeMapper, times(1)).deleteById(1L);
    }
}