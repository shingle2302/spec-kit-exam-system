package com.exam.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exam.system.dto.QuestionDTO;
import com.exam.system.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getQuestions(
            @RequestParam(required = false) String gradeId,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) String knowledgePoint,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Check if pagination is requested
        if (page > 0 && size > 0) {
            IPage<QuestionDTO> pagedQuestions = questionService.getQuestionsPaginated(page, size, gradeId, subjectId, knowledgePoint);
            return ResponseEntity.ok(pagedQuestions);
        } else {
            // Return all questions without pagination
            List<QuestionDTO> questions = questionService.getQuestions(gradeId, subjectId, knowledgePoint);
            return ResponseEntity.ok(questions);
        }
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        return ResponseEntity.ok(createdQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable String id, @RequestBody QuestionDTO questionDTO) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
        if (updatedQuestion != null) {
            return ResponseEntity.ok(updatedQuestion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        boolean deleted = questionService.deleteQuestion(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<QuestionDTO>> searchQuestions(@RequestParam String query) {
        List<QuestionDTO> questions = questionService.searchQuestions(query);
        return ResponseEntity.ok(questions);
    }
}