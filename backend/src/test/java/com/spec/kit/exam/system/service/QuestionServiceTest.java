package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.QuestionEntity;
import com.spec.kit.exam.system.mapper.QuestionMapper;
import com.spec.kit.exam.system.service.impl.QuestionServiceImpl;
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

class QuestionServiceTest {

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionServiceImpl questionService;

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
        List<QuestionEntity> questionList = new ArrayList<>();
        QuestionEntity question1 = new QuestionEntity();
        question1.setId(1L);
        question1.setContent("1+1=?");
        questionList.add(question1);

        // 模拟方法调用
        when(questionMapper.selectPage(any(), any())).thenReturn(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<QuestionEntity>() {
            {
                setRecords(questionList);
                setTotal(1L);
            }
        });

        // 执行测试
        PageResponse<QuestionEntity> result = questionService.list(pageRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("1+1=?", result.getData().get(0).getContent());
    }

    @Test
    void testListByCondition() {
        // 准备测试数据
        List<QuestionEntity> questionList = new ArrayList<>();
        QuestionEntity question1 = new QuestionEntity();
        question1.setId(1L);
        question1.setContent("1+1=?");
        questionList.add(question1);

        // 模拟方法调用
        when(questionMapper.selectByCondition(1L, 2L, 3L)).thenReturn(questionList);

        // 执行测试
        List<QuestionEntity> result = questionService.listByCondition(1L, 2L, 3L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1+1=?", result.get(0).getContent());
    }

    @Test
    void testGetById() {
        // 准备测试数据
        QuestionEntity question = new QuestionEntity();
        question.setId(1L);
        question.setContent("1+1=?");

        // 模拟方法调用
        when(questionMapper.selectById(1L)).thenReturn(question);

        // 执行测试
        QuestionEntity result = questionService.getById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("1+1=?", result.getContent());
    }

    @Test
    void testCreate() {
        // 准备测试数据
        QuestionEntity question = new QuestionEntity();
        question.setContent("1+1=?");
        question.setType("选择题");
        question.setDifficulty("EASY");
        question.setScore(5);
        question.setAnswer("2");
        question.setSubjectId(1L);
        question.setGradeId(1L);
        question.setKnowledgePointId(1L);

        // 模拟方法调用
        when(questionMapper.insert(question)).thenReturn(1);

        // 执行测试
        QuestionEntity result = questionService.create(question);

        // 验证结果
        assertNotNull(result);
        assertEquals("1+1=?", result.getContent());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        QuestionEntity question = new QuestionEntity();
        question.setId(1L);
        question.setContent("1+1=?");
        question.setType("选择题");
        question.setDifficulty("EASY");
        question.setScore(5);
        question.setAnswer("2");
        question.setSubjectId(1L);
        question.setGradeId(1L);
        question.setKnowledgePointId(1L);

        // 模拟方法调用
        when(questionMapper.updateById(question)).thenReturn(1);

        // 执行测试
        QuestionEntity result = questionService.update(question);

        // 验证结果
        assertNotNull(result);
        assertEquals("1+1=?", result.getContent());
    }

    @Test
    void testDelete() {
        // 模拟方法调用
        when(questionMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        questionService.delete(1L);

        // 验证方法被调用
        verify(questionMapper, times(1)).deleteById(1L);
    }
}