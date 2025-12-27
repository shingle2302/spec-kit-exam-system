package com.exam.system.service;

import com.exam.system.dto.StudentResponseDTO;
import com.exam.system.model.AnswerOption;
import com.exam.system.model.Question;
import com.exam.system.model.StudentResponse;
import com.exam.system.repository.AnswerOptionRepository;
import com.exam.system.repository.QuestionRepository;
import com.exam.system.repository.StudentResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradingService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerOptionRepository answerOptionRepository;
    
    @Autowired
    private StudentResponseRepository studentResponseRepository;
    
    @Autowired
    private ErrorBookService errorBookService;

    public List<StudentResponseDTO> gradeTestAutomatically(String testId, String studentId) {
        // Get all student responses for this test
        List<StudentResponse> responses = studentResponseRepository.findByStudentIdAndTestId(studentId, testId);
        
        for (StudentResponse response : responses) {
            // Get the question to check the correct answer
            Question question = questionRepository.findById(response.getQuestionId());
            
            if (question != null) {
                boolean isCorrect = false;
                
                // Grade based on question type
                if (question.getQuestionType() == Question.QuestionType.single_choice || 
                    question.getQuestionType() == Question.QuestionType.multiple_choice ||
                    question.getQuestionType() == Question.QuestionType.true_false) {
                    // For multiple choice questions, check if the selected option is correct
                    if (response.getSelectedOptionIndex() != null) {
                        // Find the answer option with the matching index
                        List<AnswerOption> options = answerOptionRepository.findByQuestionId(question.getId());
                        for (AnswerOption option : options) {
                            if (option.getOptionIndex().equals(response.getSelectedOptionIndex()) && option.getIsCorrect()) {
                                isCorrect = true;
                                break;
                            }
                        }
                    }
                } else {
                    // For text-based questions, automatic grading would require more complex logic
                    // For now, we'll leave them ungraded
                    response.setManualGrade(true);
                    response.setIsCorrect(null); // null indicates ungraded
                }
                
                // Set the correctness and save
                response.setIsCorrect(isCorrect);
                
                // Update error book based on the result
                if (isCorrect) {
                    errorBookService.updateErrorBookOnCorrectAnswer(studentId, question.getId());
                } else {
                    errorBookService.addQuestionToErrorBook(studentId, question.getId());
                }
                
                studentResponseRepository.updateById(response);
            }
        }
        
        // Return the graded responses
        return responses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private StudentResponseDTO convertToDTO(StudentResponse response) {
        if (response == null) {
            return null;
        }
        
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(response.getId());
        dto.setStudentId(response.getStudentId());
        dto.setTestId(response.getTestId());
        dto.setQuestionId(response.getQuestionId());
        dto.setResponseText(response.getResponseText());
        dto.setSelectedOptionIndex(response.getSelectedOptionIndex());
        dto.setResponseTime(response.getResponseTime());
        dto.setIsCorrect(response.getIsCorrect());
        dto.setManualGrade(response.getManualGrade());
        dto.setTeacherGrade(response.getTeacherGrade());
        dto.setTeacherComments(response.getTeacherComments());
        dto.setSubmittedAt(response.getSubmittedAt());
        
        return dto;
    }
}