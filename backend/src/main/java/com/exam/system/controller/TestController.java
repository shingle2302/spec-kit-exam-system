package com.exam.system.controller;

import com.exam.system.dto.TestDTO;
import com.exam.system.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getTestsForStudent(
            @PathVariable String studentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        if (page > 0 && size > 0) {
            com.baomidou.mybatisplus.core.metadata.IPage<TestDTO> pagedTests = testService.getTestsForStudentPaginated(studentId, page, size);
            return ResponseEntity.ok(pagedTests);
        } else {
            List<TestDTO> tests = testService.getTestsForStudent(studentId);
            return ResponseEntity.ok(tests);
        }
    }

    @PostMapping
    public ResponseEntity<TestDTO> createTest(@RequestBody TestDTO testDTO) {
        TestDTO createdTest = testService.createTest(testDTO);
        return ResponseEntity.ok(createdTest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestDTO> updateTest(@PathVariable String id, @RequestBody TestDTO testDTO) {
        TestDTO updatedTest = testService.updateTest(id, testDTO);
        if (updatedTest != null) {
            return ResponseEntity.ok(updatedTest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable String id) {
        boolean deleted = testService.deleteTest(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TestDTO>> searchTests(@RequestParam String query) {
        List<TestDTO> tests = testService.searchTests(query);
        return ResponseEntity.ok(tests);
    }
}