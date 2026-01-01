package com.exam.system.repository;

import com.exam.system.model.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionElasticsearchRepository extends ElasticsearchRepository<Question, Long> {
    List<Question> findByQuestionTextContainingOrStandardExplanationContaining(String questionText, String standardExplanation);
    List<Question> findByGradeId(Long gradeId);
    List<Question> findBySubjectId(Long subjectId);
    List<Question> findByKnowledgePoint(String knowledgePoint);
}