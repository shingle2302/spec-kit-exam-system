package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.ExamPaper;
import com.spec.kit.exam.system.mapper.ExamPaperMapper;
import com.spec.kit.exam.system.service.impl.ExamPaperServiceImpl;
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

class ExamPaperServiceTest {

    @Mock
    private ExamPaperMapper examPaperMapper;

    @InjectMocks
    private ExamPaperServiceImpl examPaperService;

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
        List<ExamPaper> examPaperList = new ArrayList<>();
        ExamPaper examPaper1 = new ExamPaper();
        examPaper1.setId(1L);
        examPaper1.setName("Math Exam");
        examPaperList.add(examPaper1);

        // 模拟方法调用
        when(examPaperMapper.selectPage(any(), any())).thenReturn(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<ExamPaper>() {
            {
                setRecords(examPaperList);
                setTotal(1L);
            }
        });

        // 执行测试
        PageResponse<ExamPaper> result = examPaperService.list(pageRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("Math Exam", result.getData().get(0).getName());
    }

    @Test
    void testListByGradeId() {
        // 准备测试数据
        List<ExamPaper> examPaperList = new ArrayList<>();
        ExamPaper examPaper1 = new ExamPaper();
        examPaper1.setId(1L);
        examPaper1.setName("Math Exam");
        examPaperList.add(examPaper1);

        // 模拟方法调用
        when(examPaperMapper.selectByGradeId(1L)).thenReturn(examPaperList);

        // 执行测试
        List<ExamPaper> result = examPaperService.listByGradeId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Math Exam", result.get(0).getName());
    }

    @Test
    void testListBySubjectAndGrade() {
        // 准备测试数据
        List<ExamPaper> examPaperList = new ArrayList<>();
        ExamPaper examPaper1 = new ExamPaper();
        examPaper1.setId(1L);
        examPaper1.setName("Math Exam");
        examPaperList.add(examPaper1);

        // 模拟方法调用
        when(examPaperMapper.selectBySubjectAndGrade(1L, 2L)).thenReturn(examPaperList);

        // 执行测试
        List<ExamPaper> result = examPaperService.listBySubjectAndGrade(1L, 2L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Math Exam", result.get(0).getName());
    }

    @Test
    void testGetById() {
        // 准备测试数据
        ExamPaper examPaper = new ExamPaper();
        examPaper.setId(1L);
        examPaper.setName("Math Exam");

        // 模拟方法调用
        when(examPaperMapper.selectById(1L)).thenReturn(examPaper);

        // 执行测试
        ExamPaper result = examPaperService.getById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Exam", result.getName());
    }

    @Test
    void testCreate() {
        // 准备测试数据
        ExamPaper examPaper = new ExamPaper();
        examPaper.setName("Math Exam");
        examPaper.setSubjectId(1L);
        examPaper.setClassId(1L);
        examPaper.setGradeId(1L);

        // 模拟方法调用
        when(examPaperMapper.insert(examPaper)).thenReturn(1);

        // 执行测试
        ExamPaper result = examPaperService.create(examPaper);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Exam", result.getName());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        ExamPaper examPaper = new ExamPaper();
        examPaper.setId(1L);
        examPaper.setName("Math Exam Updated");
        examPaper.setSubjectId(1L);
        examPaper.setClassId(1L);
        examPaper.setGradeId(1L);

        // 模拟方法调用
        when(examPaperMapper.updateById(examPaper)).thenReturn(1);

        // 执行测试
        ExamPaper result = examPaperService.update(examPaper);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Exam Updated", result.getName());
    }

    @Test
    void testDelete() {
        // 模拟方法调用
        when(examPaperMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        examPaperService.delete(1L);

        // 验证方法被调用
        verify(examPaperMapper, times(1)).deleteById(1L);
    }
}