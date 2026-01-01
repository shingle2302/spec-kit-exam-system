package com.exam.system.repository;

import com.exam.system.model.StudentResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResponseElasticsearchRepository extends ElasticsearchRepository<StudentResponse, Long> {
    List<StudentResponse> findByStudentId(Long studentId);
    List<StudentResponse> findByTestId(Long testId);
    List<StudentResponse> findByQuestionId(Long questionId);
    List<StudentResponse> findByStudentIdAndTestId(Long studentId, Long testId);
    List<StudentResponse> findByResponseTextContainingOrTeacherCommentsContaining(String responseText, String teacherComments);
}