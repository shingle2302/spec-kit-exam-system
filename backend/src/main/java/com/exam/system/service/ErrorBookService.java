package com.exam.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.system.dto.ErrorBookDTO;
import com.exam.system.model.ErrorBook;
import com.exam.system.repository.ErrorBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ErrorBookService {

    @Autowired
    private ErrorBookRepository errorBookRepository;
    
    @Autowired
    private CacheService cacheService;

    public List<ErrorBookDTO> getErrorBookForStudent(String studentId) {
        String cacheKey = "errorbook_student_" + studentId;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<ErrorBookDTO>) cached;
        }
        
        List<ErrorBook> errorBooks = errorBookRepository.findByStudentId(studentId);
        List<ErrorBookDTO> result = errorBooks.stream().map(this::convertToDTO).collect(Collectors.toList());
        
        cacheService.setList(cacheKey, result);
        
        return result;
    }
    
    public IPage<ErrorBookDTO> getErrorBookForStudentPaginated(String studentId, int page, int size) {
        String cacheKey = "errorbook_paginated_" + studentId + "_" + page + "_" + size;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (IPage<ErrorBookDTO>) cached;
        }
        
        Page<ErrorBook> mybatisPage = new Page<>(page, size);
        List<ErrorBook> allErrorBooks = errorBookRepository.findByStudentIdAndMasteredFalse(studentId);
        
        // Manually paginate the list
        int start = (int) ((page - 1) * size);
        int end = Math.min(start + size, allErrorBooks.size());
        
        List<ErrorBook> pagedErrorBooks = start < allErrorBooks.size() ? 
            allErrorBooks.subList(start, end) : List.of();
        
        mybatisPage.setRecords(pagedErrorBooks);
        mybatisPage.setTotal(allErrorBooks.size());
        
        // Convert to DTOs
        List<ErrorBookDTO> content = pagedErrorBooks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        // Create a new Page object with DTOs
        Page<ErrorBookDTO> result = new Page<>();
        result.setRecords(content);
        result.setTotal(mybatisPage.getTotal());
        result.setSize(mybatisPage.getSize());
        result.setCurrent(mybatisPage.getCurrent());
        
        cacheService.setWithDefaultTimeout(cacheKey, result);
        
        return result;
    }

    public ErrorBookDTO addQuestionToErrorBook(String studentId, String questionId) {
        // Check if the question is already in the error book
        ErrorBook existingErrorBook = errorBookRepository.findByStudentIdAndQuestionId(studentId, questionId);
        
        ErrorBook errorBook;
        if (existingErrorBook != null) {
            // Update existing entry
            errorBook = existingErrorBook;
            errorBook.setErrorCount(errorBook.getErrorCount() + 1);
        } else {
            // Create new entry
            errorBook = new ErrorBook();
            errorBook.setId(UUID.randomUUID().toString());
            errorBook.setStudentId(studentId);
            errorBook.setQuestionId(questionId);
            errorBook.setErrorCount(1);
            errorBook.setFirstIncorrectDate(LocalDateTime.now());
        }
        
        errorBookRepository.insert(errorBook);
        
        // Clear related cache entries
        cacheService.delete("errorbook_student_" + studentId);
        cacheService.delete("errorbook_paginated_" + studentId + "*");
        
        return convertToDTO(errorBook);
    }

    public ErrorBookDTO updateErrorBookOnCorrectAnswer(String studentId, String questionId) {
        ErrorBook errorBook = errorBookRepository.findByStudentIdAndQuestionId(studentId, questionId);
        if (errorBook != null) {
            // Increment mastery count
            errorBook.setMasteryCount(errorBook.getMasteryCount() + 1);
            errorBook.setLastPracticeDate(LocalDateTime.now());
            
            // Check if the student has mastered the question (3 correct answers in a row)
            if (errorBook.getMasteryCount() >= 3) {
                errorBook.setMastered(true);
            }
            
            errorBookRepository.updateById(errorBook);
            
            // Clear related cache entries
            cacheService.delete("errorbook_student_" + studentId);
            cacheService.delete("errorbook_paginated_" + studentId + "*");
            
            return convertToDTO(errorBook);
        }
        return null;
    }

    public ErrorBookDTO resetErrorBookOnIncorrectAnswer(String studentId, String questionId) {
        ErrorBook errorBook = errorBookRepository.findByStudentIdAndQuestionId(studentId, questionId);
        if (errorBook != null) {
            // Reset mastery count but keep the error count
            errorBook.setMasteryCount(0);
            errorBook.setMastered(false);
            errorBook.setLastPracticeDate(LocalDateTime.now());
            
            errorBookRepository.updateById(errorBook);
            
            // Clear related cache entries
            cacheService.delete("errorbook_student_" + studentId);
            cacheService.delete("errorbook_paginated_" + studentId + "*");
            
            return convertToDTO(errorBook);
        }
        return null;
    }

    private ErrorBookDTO convertToDTO(ErrorBook errorBook) {
        if (errorBook == null) {
            return null;
        }
        
        ErrorBookDTO dto = new ErrorBookDTO();
        dto.setId(errorBook.getId());
        dto.setStudentId(errorBook.getStudentId());
        dto.setQuestionId(errorBook.getQuestionId());
        dto.setErrorCount(errorBook.getErrorCount());
        dto.setMasteryCount(errorBook.getMasteryCount());
        dto.setMastered(errorBook.getMastered());
        dto.setFirstIncorrectDate(errorBook.getFirstIncorrectDate());
        dto.setLastPracticeDate(errorBook.getLastPracticeDate());
        dto.setCreatedAt(errorBook.getCreatedAt());
        dto.setUpdatedAt(errorBook.getUpdatedAt());
        
        return dto;
    }
}