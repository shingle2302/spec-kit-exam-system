package com.exam.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.system.dto.TestDTO;
import com.exam.system.model.Test;
import com.exam.system.repository.TestElasticsearchRepository;
import com.exam.system.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired(required = false)
    private TestElasticsearchRepository testElasticsearchRepository;

    public TestDTO createTest(TestDTO testDTO) {
        // Generate ID if not provided
        if (testDTO.getId() == null || testDTO.getId().isEmpty()) {
            testDTO.setId(UUID.randomUUID().toString());
        }

        // Create and populate the entity
        Test test = new Test();
        test.setId(testDTO.getId());
        test.setTitle(testDTO.getTitle());
        test.setDescription(testDTO.getDescription());
        test.setQuestions(testDTO.getQuestions());
        test.setAssignedTo(testDTO.getAssignedTo());
        test.setAssignedBy(testDTO.getAssignedBy());
        test.setTimeLimitMinutes(testDTO.getTimeLimitMinutes());
        test.setDueDate(testDTO.getDueDate());
        test.setIsActive(testDTO.getIsActive());
        test.setIsGraded(testDTO.getIsGraded());

        // Insert the entity
        testRepository.insert(test);

        // Also save to Elasticsearch if available
        if (testElasticsearchRepository != null) {
            try {
                testElasticsearchRepository.save(test);
            } catch (Exception e) {
                // Log the error but don't fail the operation
                System.err.println("Failed to save test to Elasticsearch: " + e.getMessage());
            }
        }

        // Clear related cache entries
        if (testDTO.getAssignedTo() != null) {
            for (String studentId : testDTO.getAssignedTo()) {
                cacheService.delete("tests_student_" + studentId);
                cacheService.deletePattern("tests_paginated_" + studentId + "_");
            }
        }

        // Convert back to DTO and return
        return convertToDTO(test);
    }

    public TestDTO getTestById(String id) {
        String cacheKey = "test_" + id;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (TestDTO) cached;
        }
        
        Test test = testRepository.selectById(id);
        TestDTO result = convertToDTO(test);
        
        if (result != null) {
            cacheService.setEntity(cacheKey, result);
        }
        
        return result;
    }

    public List<TestDTO> getTestsForStudent(String studentId) {
        String cacheKey = "tests_student_" + studentId;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<TestDTO>) cached;
        }
        
        List<Test> tests = testRepository.findByAssignedToContainingAndIsActiveTrue(studentId);
        List<TestDTO> result = tests.stream().map(this::convertToDTO).collect(Collectors.toList());
        
        cacheService.setList(cacheKey, result);
        
        return result;
    }
    
    public IPage<TestDTO> getTestsForStudentPaginated(String studentId, int page, int size) {
        String cacheKey = "tests_paginated_" + studentId + "_" + page + "_" + size;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (IPage<TestDTO>) cached;
        }
        
        Page<Test> mybatisPage = new Page<>(page, size);
        List<Test> allTests = testRepository.findByAssignedToContainingAndIsActiveTrue(studentId);
        
        // Manually paginate the list
        int start = (int) ((page - 1) * size);
        int end = Math.min(start + size, allTests.size());
        
        List<Test> pagedTests = start < allTests.size() ? 
            allTests.subList(start, end) : List.of();
        
        mybatisPage.setRecords(pagedTests);
        mybatisPage.setTotal(allTests.size());
        
        // Convert to DTOs
        List<TestDTO> content = pagedTests.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        // Create a new Page object with DTOs
        Page<TestDTO> result = new Page<>();
        result.setRecords(content);
        result.setTotal(mybatisPage.getTotal());
        result.setSize(mybatisPage.getSize());
        result.setCurrent(mybatisPage.getCurrent());
        
        cacheService.setWithDefaultTimeout(cacheKey, result);
        
        return result;
    }

    public TestDTO updateTest(String id, TestDTO testDTO) {
        Test existingTest = testRepository.selectById(id);
        if (existingTest != null) {
            existingTest.setTitle(testDTO.getTitle());
            existingTest.setDescription(testDTO.getDescription());
            existingTest.setQuestions(testDTO.getQuestions());
            existingTest.setAssignedTo(testDTO.getAssignedTo());
            existingTest.setAssignedBy(testDTO.getAssignedBy());
            existingTest.setTimeLimitMinutes(testDTO.getTimeLimitMinutes());
            existingTest.setDueDate(testDTO.getDueDate());
            existingTest.setIsActive(testDTO.getIsActive());
            existingTest.setIsGraded(testDTO.getIsGraded());
            
            testRepository.updateById(existingTest);
            
            // Update in Elasticsearch if available
            if (testElasticsearchRepository != null) {
                try {
                    testElasticsearchRepository.save(existingTest);
                } catch (Exception e) {
                    // Log the error but don't fail the operation
                    System.err.println("Failed to update test in Elasticsearch: " + e.getMessage());
                }
            }
            
            // Clear related cache entries
            cacheService.delete("test_" + id);
            if (existingTest.getAssignedTo() != null) {
                for (String studentId : existingTest.getAssignedTo()) {
                    cacheService.delete("tests_student_" + studentId);
                    cacheService.deletePattern("tests_paginated_" + studentId + "_");
                }
            }
            
            return convertToDTO(existingTest);
        }
        return null;
    }

    public boolean deleteTest(String id) {
        Test test = testRepository.selectById(id);
        if (test != null) {
            int result = testRepository.deleteById(id);
            
            if (result > 0) {
                // Delete from Elasticsearch if available
                if (testElasticsearchRepository != null) {
                    try {
                        testElasticsearchRepository.deleteById(id);
                    } catch (Exception e) {
                        // Log the error but don't fail the operation
                        System.err.println("Failed to delete test from Elasticsearch: " + e.getMessage());
                    }
                }
                
                // Clear related cache entries
                cacheService.delete("test_" + id);
                if (test.getAssignedTo() != null) {
                    for (String studentId : test.getAssignedTo()) {
                        cacheService.delete("tests_student_" + studentId);
                        cacheService.deletePattern("tests_paginated_" + studentId + "_");
                    }
                }
                
                return true;
            }
        }
        return false;
    }

    public List<TestDTO> searchTests(String query) {
        if (testElasticsearchRepository != null) {
            try {
                List<Test> tests = testElasticsearchRepository.findByTitleContainingOrDescriptionContaining(query, query);
                return tests.stream().map(this::convertToDTO).collect(Collectors.toList());
            } catch (Exception e) {
                // Log the error but return empty list
                System.err.println("Failed to search tests in Elasticsearch: " + e.getMessage());
                return java.util.Collections.emptyList();
            }
        } else {
            // Return empty list if Elasticsearch is not available
            return java.util.Collections.emptyList();
        }
    }

    private TestDTO convertToDTO(Test test) {
        if (test == null) {
            return null;
        }
        
        TestDTO dto = new TestDTO();
        dto.setId(test.getId());
        dto.setTitle(test.getTitle());
        dto.setDescription(test.getDescription());
        dto.setQuestions(test.getQuestions());
        dto.setAssignedTo(test.getAssignedTo());
        dto.setAssignedBy(test.getAssignedBy());
        dto.setTimeLimitMinutes(test.getTimeLimitMinutes());
        dto.setDueDate(test.getDueDate());
        dto.setCreatedAt(test.getCreatedAt());
        dto.setUpdatedAt(test.getUpdatedAt());
        dto.setIsActive(test.getIsActive());
        dto.setIsGraded(test.getIsGraded());
        
        return dto;
    }
}