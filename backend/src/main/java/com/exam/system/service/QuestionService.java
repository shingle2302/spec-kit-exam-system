package com.exam.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.system.dto.QuestionDTO;
import com.exam.system.model.Question;
import com.exam.system.repository.QuestionElasticsearchRepository;
import com.exam.system.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired(required = false)
    private QuestionElasticsearchRepository questionElasticsearchRepository;
    
    @Autowired
    private CacheService cacheService;

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        // Generate ID if not provided
        if (questionDTO.getId() == null || questionDTO.getId().isEmpty()) {
            questionDTO.setId(UUID.randomUUID().toString());
        }

        // Create and populate the entity
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setQuestionText(questionDTO.getQuestionText());
        question.setQuestionType(questionDTO.getQuestionType());
        question.setSubjectId(questionDTO.getSubjectId());
        question.setGradeId(questionDTO.getGradeId());
        question.setKnowledgePoint(questionDTO.getKnowledgePoint());
        question.setStandardExplanation(questionDTO.getStandardExplanation());
        question.setCreatedBy(questionDTO.getCreatedBy());
        question.setIsActive(questionDTO.getIsActive());

        // Insert the entity
        questionRepository.insert(question);

        // Also save to Elasticsearch if available
        if (questionElasticsearchRepository != null) {
            try {
                questionElasticsearchRepository.save(question);
            } catch (Exception e) {
                // Log the error but don't fail the operation
                System.err.println("Failed to save question to Elasticsearch: " + e.getMessage());
            }
        }

        // Convert back to DTO and return
        QuestionDTO result = convertToDTO(question);
        
        // Clear cache since we added a new question
        cacheService.deletePattern("questions_"); // Delete all question-related cache entries
        
        return result;
    }

    public QuestionDTO getQuestionById(String id) {
        String cacheKey = "question_" + id;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (QuestionDTO) cached;
        }
        
        Question question = questionRepository.selectById(id);
        QuestionDTO dto = convertToDTO(question);
        
        if (dto != null) {
            cacheService.setEntity(cacheKey, dto); // Cache for 5 minutes
        }
        
        return dto;
    }

    public List<QuestionDTO> getQuestions(String gradeId, String subjectId, String knowledgePoint) {
        String cacheKey = "questions_" + gradeId + "_" + subjectId + "_" + knowledgePoint;
        Object cached = cacheService.get(cacheKey);
        if (cached != null) {
            return (List<QuestionDTO>) cached;
        }
        
        List<Question> questions;
        
        if (gradeId != null && subjectId != null && knowledgePoint != null) {
            questions = questionRepository.findByGradeIdAndSubjectIdAndKnowledgePoint(gradeId, subjectId, knowledgePoint);
        } else if (gradeId != null) {
            questions = questionRepository.findByGradeId(gradeId);
        } else if (subjectId != null) {
            questions = questionRepository.findBySubjectId(subjectId);
        } else if (knowledgePoint != null) {
            questions = questionRepository.findByKnowledgePoint(knowledgePoint);
        } else {
            // Check if all questions are cached
            cached = cacheService.get("questions_all");
            if (cached != null) {
                return (List<QuestionDTO>) cached;
            }
            questions = questionRepository.selectList(null);
            cacheService.setList("questions_all", questions.stream().map(this::convertToDTO).collect(Collectors.toList())); // 5 min cache
            return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        
        List<QuestionDTO> result = questions.stream().map(this::convertToDTO).collect(Collectors.toList());
        cacheService.setList(cacheKey, result); // Cache for 5 minutes
        
        return result;
    }
    
    public IPage<QuestionDTO> getQuestionsPaginated(int page, int size, String gradeId, String subjectId, String knowledgePoint) {
        Page<Question> mybatisPage = new Page<>(page, size);
        
        if (gradeId != null && subjectId != null && knowledgePoint != null) {
            // For complex queries with multiple conditions, we might need to use a custom query
            // For now, we'll fetch all and filter manually
            List<Question> allQuestions = questionRepository.findByGradeIdAndSubjectIdAndKnowledgePoint(gradeId, subjectId, knowledgePoint);
            mybatisPage.setRecords(allQuestions.stream()
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList()));
            mybatisPage.setTotal(allQuestions.size());
        } else if (gradeId != null) {
            List<Question> allQuestions = questionRepository.findByGradeId(gradeId);
            mybatisPage.setRecords(allQuestions.stream()
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList()));
            mybatisPage.setTotal(allQuestions.size());
        } else if (subjectId != null) {
            List<Question> allQuestions = questionRepository.findBySubjectId(subjectId);
            mybatisPage.setRecords(allQuestions.stream()
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList()));
            mybatisPage.setTotal(allQuestions.size());
        } else if (knowledgePoint != null) {
            List<Question> allQuestions = questionRepository.findByKnowledgePoint(knowledgePoint);
            mybatisPage.setRecords(allQuestions.stream()
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList()));
            mybatisPage.setTotal(allQuestions.size());
        } else {
            // Use MyBatis Plus pagination for all questions
            mybatisPage = questionRepository.selectPage(mybatisPage, null);
        }
        
        // Convert to DTOs
        List<QuestionDTO> content = mybatisPage.getRecords().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        // Create a new Page object with DTOs
        Page<QuestionDTO> result = new Page<>();
        result.setRecords(content);
        result.setTotal(mybatisPage.getTotal());
        result.setSize(mybatisPage.getSize());
        result.setCurrent(mybatisPage.getCurrent());
        
        return result;
    }

    public List<QuestionDTO> searchQuestions(String query) {
        if (questionElasticsearchRepository != null) {
            try {
                List<Question> questions = questionElasticsearchRepository.findByQuestionTextContainingOrStandardExplanationContaining(query, query);
                return questions.stream().map(this::convertToDTO).collect(Collectors.toList());
            } catch (Exception e) {
                // Log the error but return empty list
                System.err.println("Failed to search questions in Elasticsearch: " + e.getMessage());
                return java.util.Collections.emptyList();
            }
        } else {
            // Return empty list if Elasticsearch is not available
            return java.util.Collections.emptyList();
        }
    }

    public QuestionDTO updateQuestion(String id, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.selectById(id);
        if (existingQuestion != null) {
            existingQuestion.setQuestionText(questionDTO.getQuestionText());
            existingQuestion.setQuestionType(questionDTO.getQuestionType());
            existingQuestion.setSubjectId(questionDTO.getSubjectId());
            existingQuestion.setGradeId(questionDTO.getGradeId());
            existingQuestion.setKnowledgePoint(questionDTO.getKnowledgePoint());
            existingQuestion.setStandardExplanation(questionDTO.getStandardExplanation());
            existingQuestion.setIsActive(questionDTO.getIsActive());
            
            questionRepository.updateById(existingQuestion);
            
            // Update in Elasticsearch if available
            if (questionElasticsearchRepository != null) {
                try {
                    questionElasticsearchRepository.save(existingQuestion);
                } catch (Exception e) {
                    // Log the error but don't fail the operation
                    System.err.println("Failed to update question in Elasticsearch: " + e.getMessage());
                }
            }
            
            // Clear cache
            cacheService.delete("question_" + id);
            cacheService.deletePattern("questions_"); // Delete all question-related cache entries
            
            return convertToDTO(existingQuestion);
        }
        return null;
    }

    public boolean deleteQuestion(String id) {
        int result = questionRepository.deleteById(id);
        
        if (result > 0) {
            // Delete from Elasticsearch if available
            if (questionElasticsearchRepository != null) {
                try {
                    questionElasticsearchRepository.deleteById(id);
                } catch (Exception e) {
                    // Log the error but don't fail the operation
                    System.err.println("Failed to delete question from Elasticsearch: " + e.getMessage());
                }
            }
            
            // Clear cache
            cacheService.delete("question_" + id);
            cacheService.deletePattern("questions_"); // Delete all question-related cache entries
            
            return true;
        }
        return false;
    }

    private QuestionDTO convertToDTO(Question question) {
        if (question == null) {
            return null;
        }
        
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setQuestionType(question.getQuestionType());
        dto.setSubjectId(question.getSubjectId());
        dto.setGradeId(question.getGradeId());
        dto.setKnowledgePoint(question.getKnowledgePoint());
        dto.setStandardExplanation(question.getStandardExplanation());
        dto.setCreatedBy(question.getCreatedBy());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        dto.setIsActive(question.getIsActive());
        
        return dto;
    }
}