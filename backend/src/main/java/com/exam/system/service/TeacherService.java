package com.exam.system.service;

import com.exam.system.model.ClassEnrollment;
import com.exam.system.repository.ClassEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private ClassEnrollmentRepository classEnrollmentRepository;

    public List<String> getMyStudents(String teacherId) {
        // In a real implementation, this would query based on teacher's classes
        // For now, returning an empty list
        return List.of();
    }

    public boolean addStudentToClass(String teacherId, String studentId, String classId) {
        // In a real implementation, this would validate teacher permissions and add the student
        // For now, returning true
        return true;
    }
}