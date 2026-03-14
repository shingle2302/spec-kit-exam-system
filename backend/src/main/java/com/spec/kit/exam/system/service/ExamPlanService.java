package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.ExamPlan;
import com.spec.kit.exam.system.mapper.ExamPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamPlanService {

    @Autowired
    private ExamPlanMapper examPlanMapper;

    public ExamPlan create(ExamPlan examPlan) {
        examPlan.setStatus(examPlan.getStatus() == null ? "DRAFT" : examPlan.getStatus());
        examPlan.setCreatedAt(LocalDateTime.now());
        examPlan.setUpdatedAt(LocalDateTime.now());
        examPlanMapper.insert(examPlan);
        return examPlan;
    }

    public Map<String, Object> queryPage(Map<String, Object> req) {
        Integer page = (Integer) req.getOrDefault("page", 1);
        Integer size = (Integer) req.getOrDefault("size", 10);
        Map<String, Object> filters = (Map<String, Object>) req.getOrDefault("filters", new HashMap<>());
        String name = (String) filters.get("name");
        String academicYear = (String) filters.get("academicYear");
        String examType = (String) filters.get("examType");
        int offset = (page - 1) * size;

        List<Map<String, Object>> records = examPlanMapper.queryPage(name, academicYear, examType, size, offset);
        Long total = examPlanMapper.countQuery(name, academicYear, examType);

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", total);
        data.put("current", page);
        data.put("size", size);
        data.put("pages", (long) Math.ceil(total * 1.0 / size));
        return data;
    }

    public ExamPlan getById(Long id) {
        return examPlanMapper.selectById(id);
    }

    public boolean update(Long id, ExamPlan body) {
        body.setId(id);
        body.setUpdatedAt(LocalDateTime.now());
        return examPlanMapper.updateById(body) > 0;
    }

    public boolean delete(Long id) {
        return examPlanMapper.deleteById(id) > 0;
    }
}
