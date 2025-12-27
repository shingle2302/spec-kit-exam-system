package com.exam.system.repository;

import com.exam.system.model.StudentResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResponseElasticsearchRepository extends ElasticsearchRepository<StudentResponse, String> {
    List<StudentResponse> findByStudentId(String studentId);
    List<StudentResponse> findByTestId(String testId);
    List<StudentResponse> findByQuestionId(String questionId);
    List<StudentResponse> findByStudentIdAndTestId(String studentId, String testId);
    List<StudentResponse> findByResponseTextContainingOrTeacherCommentsContaining(String responseText, String teacherComments);
}