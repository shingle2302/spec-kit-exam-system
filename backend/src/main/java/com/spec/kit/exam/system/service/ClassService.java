package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.ClassEntity;
import com.spec.kit.exam.system.mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassService {
    @Autowired
    private ClassMapper classMapper;

    public ClassEntity create(ClassEntity classEntity) {
        classEntity.setStatus(classEntity.getStatus() == null ? "ACTIVE" : classEntity.getStatus());
        classEntity.setCreatedAt(LocalDateTime.now());
        classEntity.setUpdatedAt(LocalDateTime.now());
        classMapper.insert(classEntity);
        return classEntity;
    }

    public Map<String, Object> queryPage(Map<String, Object> req) {
        Integer page = (Integer) req.getOrDefault("page", 1);
        Integer size = (Integer) req.getOrDefault("size", 10);
        Map<String, Object> filters = (Map<String, Object>) req.getOrDefault("filters", new HashMap<>());
        String name = (String) filters.get("name");
        Long gradeId = filters.get("gradeId") == null ? null : Long.valueOf(filters.get("gradeId").toString());
        Long educationalLevelId = filters.get("educationalLevelId") == null ? null : Long.valueOf(filters.get("educationalLevelId").toString());
        int offset = (page - 1) * size;
        List<Map<String, Object>> records = classMapper.queryPage(name, gradeId, educationalLevelId, size, offset);
        Long total = classMapper.countQuery(name, gradeId, educationalLevelId);

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", total);
        data.put("current", page);
        data.put("size", size);
        data.put("pages", (long) Math.ceil(total * 1.0 / size));
        return data;
    }

    public Map<String, Object> getDetail(Long id) {
        return classMapper.selectClassDetail(id);
    }

    public boolean update(Long id, ClassEntity body) {
        body.setId(id);
        body.setUpdatedAt(LocalDateTime.now());
        return classMapper.updateById(body) > 0;
    }

    public boolean delete(Long id) {
        return classMapper.deleteById(id) > 0;
    }

    public List<Map<String, Object>> getGrades() {
        return classMapper.selectActiveGrades();
    }
}
