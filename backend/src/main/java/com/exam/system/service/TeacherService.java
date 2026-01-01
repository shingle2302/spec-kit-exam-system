package com.exam.system.service;

import com.exam.system.model.Class;
import com.exam.system.model.ClassEnrollment;
import com.exam.system.model.Student;
import com.exam.system.repository.ClassEnrollmentRepository;
import com.exam.system.repository.ClassRepository;
import com.exam.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private ClassEnrollmentRepository classEnrollmentRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getMyStudents(Long teacherId) {
        // Get the class taught by this teacher
        List<Class> classes = classRepository.findByTeacherId(teacherId);
        
        // Get all active enrollments for these classes
        return classEnrollmentRepository.findActiveStudentsByClasses(
            classes.stream().map(Class::getId).collect(Collectors.toList())
        );
    }

    public boolean addStudentToClass(Long teacherId, Long studentId, Long classId) {
        // Verify that the teacher teaches this class
        Class classEntity = classRepository.selectById(classId);
        if (classEntity == null || !classEntity.getTeacherId().equals(teacherId)) {
            return false; // Teacher doesn't teach this class
        }
        
        // Check if student already exists in the class
        ClassEnrollment existingEnrollment = classEnrollmentRepository
            .findByStudentIdAndClassIdAndStatus(studentId, classId, ClassEnrollment.Status.ACTIVE);
        if (existingEnrollment != null) {
            return false; // Student is already in this class
        }
        
        // Create new enrollment
        ClassEnrollment enrollment = new ClassEnrollment();
        enrollment.setStudentId(studentId);
        enrollment.setClassId(classId);
        enrollment.setStatus(ClassEnrollment.Status.ACTIVE);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        
        classEnrollmentRepository.insert(enrollment);
        
        return true;
    }
}