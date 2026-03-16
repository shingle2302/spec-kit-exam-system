package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.KnowledgePointEntity;
import com.spec.kit.exam.system.mapper.KnowledgePointMapper;
import com.spec.kit.exam.system.service.impl.KnowledgePointServiceImpl;
import com.spec.kit.exam.system.util.PageRequest;
import com.spec.kit.exam.system.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class KnowledgePointServiceTest {

    @Mock
    private KnowledgePointMapper knowledgePointMapper;

    @InjectMocks
    private KnowledgePointServiceImpl knowledgePointService;

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
        List<KnowledgePointEntity> knowledgePointList = new ArrayList<>();
        KnowledgePointEntity knowledgePoint1 = new KnowledgePointEntity();
        knowledgePoint1.setId(1L);
        knowledgePoint1.setName("Math Basics");
        knowledgePointList.add(knowledgePoint1);

        // 模拟方法调用
        when(knowledgePointMapper.selectPage(any(), any())).thenReturn(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<KnowledgePointEntity>() {
            {
                setRecords(knowledgePointList);
                setTotal(1L);
            }
        });

        // 执行测试
        PageResponse<KnowledgePointEntity> result = knowledgePointService.list(pageRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("Math Basics", result.getData().get(0).getName());
    }

    @Test
    void testGetTree() {
        // 准备测试数据
        List<KnowledgePointEntity> knowledgePointList = new ArrayList<>();
        KnowledgePointEntity parent = new KnowledgePointEntity();
        parent.setId(1L);
        parent.setName("Math");
        parent.setParentId(null);
        knowledgePointList.add(parent);

        KnowledgePointEntity child = new KnowledgePointEntity();
        child.setId(2L);
        child.setName("Addition");
        child.setParentId(1L);
        knowledgePointList.add(child);

        // 模拟方法调用
        when(knowledgePointMapper.selectAllKnowledgePoints()).thenReturn(knowledgePointList);

        // 执行测试
        List<Map<String, Object>> result = knowledgePointService.getTree();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Math", result.get(0).get("name"));
        List<Map<String, Object>> children = (List<Map<String, Object>>) result.get(0).get("children");
        assertEquals(1, children.size());
        assertEquals("Addition", children.get(0).get("name"));
    }

    @Test
    void testGetById() {
        // 准备测试数据
        KnowledgePointEntity knowledgePoint = new KnowledgePointEntity();
        knowledgePoint.setId(1L);
        knowledgePoint.setName("Math Basics");

        // 模拟方法调用
        when(knowledgePointMapper.selectById(1L)).thenReturn(knowledgePoint);

        // 执行测试
        KnowledgePointEntity result = knowledgePointService.getById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Basics", result.getName());
    }

    @Test
    void testCreate() {
        // 准备测试数据
        KnowledgePointEntity knowledgePoint = new KnowledgePointEntity();
        knowledgePoint.setName("Math Basics");
        knowledgePoint.setParentId(null);

        // 模拟方法调用
        when(knowledgePointMapper.insert(knowledgePoint)).thenReturn(1);

        // 执行测试
        KnowledgePointEntity result = knowledgePointService.create(knowledgePoint);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Basics", result.getName());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        KnowledgePointEntity knowledgePoint = new KnowledgePointEntity();
        knowledgePoint.setId(1L);
        knowledgePoint.setName("Math Basics Updated");
        knowledgePoint.setParentId(null);

        // 模拟方法调用
        when(knowledgePointMapper.updateById(knowledgePoint)).thenReturn(1);

        // 执行测试
        KnowledgePointEntity result = knowledgePointService.update(knowledgePoint);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Basics Updated", result.getName());
    }

    @Test
    void testDelete() {
        // 准备测试数据
        List<KnowledgePointEntity> emptyList = new ArrayList<>();

        // 模拟方法调用
        when(knowledgePointMapper.selectByParentId(1L)).thenReturn(emptyList);
        when(knowledgePointMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        knowledgePointService.delete(1L);

        // 验证方法被调用
        verify(knowledgePointMapper, times(1)).selectByParentId(1L);
        verify(knowledgePointMapper, times(1)).deleteById(1L);
    }

    @Test
    void testValidateLevelDepth() {
        // 测试顶级节点
        boolean result1 = knowledgePointService.validateLevelDepth(null, 1);
        assertTrue(result1);

        // 测试层级深度
        KnowledgePointEntity parent1 = new KnowledgePointEntity();
        parent1.setId(1L);
        parent1.setParentId(null);

        KnowledgePointEntity parent2 = new KnowledgePointEntity();
        parent2.setId(2L);
        parent2.setParentId(1L);

        when(knowledgePointMapper.selectById(1L)).thenReturn(parent1);
        when(knowledgePointMapper.selectById(2L)).thenReturn(parent2);

        boolean result2 = knowledgePointService.validateLevelDepth(1L, 1);
        assertTrue(result2);
    }
}