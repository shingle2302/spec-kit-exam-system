package com.exam.system.controller;

import com.exam.system.dto.ErrorBookDTO;
import com.exam.system.service.ErrorBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/error-book")
public class ErrorBookController {

    @Autowired
    private ErrorBookService errorBookService;

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getErrorBookForStudent(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        if (page > 0 && size > 0) {
            com.baomidou.mybatisplus.core.metadata.IPage<ErrorBookDTO> pagedErrorBook = errorBookService.getErrorBookForStudentPaginated(studentId, page, size);
            return ResponseEntity.ok(pagedErrorBook);
        } else {
            List<ErrorBookDTO> errorBook = errorBookService.getErrorBookForStudent(studentId);
            return ResponseEntity.ok(errorBook);
        }
    }

    @PostMapping("/{studentId}/practice")
    public ResponseEntity<ErrorBookDTO> practiceQuestion(@PathVariable Long studentId, @RequestBody PracticeRequest request) {
        // In a real implementation, we would evaluate the response and update the error book accordingly
        // For now, we'll just update the last practice date
        ErrorBookDTO updatedErrorBook = errorBookService.updateErrorBookOnCorrectAnswer(studentId, request.getQuestionId());
        if (updatedErrorBook != null) {
            return ResponseEntity.ok(updatedErrorBook);
        } else {
            // If the question is not in the error book, add it
            ErrorBookDTO newErrorBook = errorBookService.addQuestionToErrorBook(studentId, request.getQuestionId());
            return ResponseEntity.ok(newErrorBook);
        }
    }

    // Inner class for practice request
    public static class PracticeRequest {
        private Long questionId;
        private String response;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}