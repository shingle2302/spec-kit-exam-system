package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.entity.SubjectEntity;
import com.spec.kit.exam.system.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;

    public SubjectEntity create(SubjectEntity subjectEntity) {
        subjectEntity.setStatus(subjectEntity.getStatus() == null ? "ACTIVE" : subjectEntity.getStatus());
        subjectEntity.setCreatedAt(LocalDateTime.now());
        subjectEntity.setUpdatedAt(LocalDateTime.now());
        subjectMapper.insert(subjectEntity);
        return subjectEntity;
    }

    public Map<String, Object> queryPage(Map<String, Object> req) {
        Integer page = (Integer) req.getOrDefault("page", 1);
        Integer size = (Integer) req.getOrDefault("size", 10);
        Map<String, Object> filters = (Map<String, Object>) req.getOrDefault("filters", new HashMap<>());
        String name = (String) filters.get("name");
        Long classId = filters.get("classId") == null ? null : Long.valueOf(filters.get("classId").toString());
        Long educationalLevelId = filters.get("educationalLevelId") == null ? null : Long.valueOf(filters.get("educationalLevelId").toString());
        String specialization = (String) filters.get("specialization");
        int offset = (page - 1) * size;
        List<Map<String, Object>> records = subjectMapper.queryPage(name, classId, educationalLevelId, specialization, size, offset);
        Long total = subjectMapper.countQuery(name, classId, educationalLevelId, specialization);

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", total);
        data.put("current", page);
        data.put("size", size);
        data.put("pages", (long) Math.ceil(total * 1.0 / size));
        return data;
    }

    public Map<String, Object> getDetail(Long id) {
        return subjectMapper.selectSubjectDetail(id);
    }

    public boolean update(Long id, SubjectEntity body) {
        body.setId(id);
        body.setUpdatedAt(LocalDateTime.now());
        return subjectMapper.updateById(body) > 0;
    }

    public boolean delete(Long id) {
        return subjectMapper.deleteById(id) > 0;
    }

    public List<Map<String, Object>> getClasses() {
        return subjectMapper.selectActiveClasses();
    }

    public List<Map<String, Object>> getLevels() {
        return subjectMapper.selectActiveLevels();
    }
}
