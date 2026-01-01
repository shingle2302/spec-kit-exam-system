import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const adminService = {
  // Student management
  async getStudents(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/admin/students`, {
      params: { page, size }
    });
    return response.data;
  },

  async createStudent(studentData) {
    const response = await axios.post(`${API_BASE_URL}/v1/admin/students`, studentData);
    return response.data;
  },

  async updateStudent(studentId, studentData) {
    const response = await axios.put(`${API_BASE_URL}/v1/admin/students/${studentId}`, studentData);
    return response.data;
  },

  async deleteStudent(studentId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/admin/students/${studentId}`);
    return response.data;
  },

  // Teacher management
  async getTeachers(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/admin/teachers`, {
      params: { page, size }
    });
    return response.data;
  },

  async createTeacher(teacherData) {
    const response = await axios.post(`${API_BASE_URL}/v1/admin/teachers`, teacherData);
    return response.data;
  },

  async updateTeacher(teacherId, teacherData) {
    const response = await axios.put(`${API_BASE_URL}/v1/admin/teachers/${teacherId}`, teacherData);
    return response.data;
  },

  async deleteTeacher(teacherId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/admin/teachers/${teacherId}`);
    return response.data;
  },

  // Subject management
  async getSubjects(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/admin/subjects`, {
      params: { page, size }
    });
    return response.data;
  },

  async createSubject(subjectData) {
    const response = await axios.post(`${API_BASE_URL}/v1/admin/subjects`, subjectData);
    return response.data;
  },

  async updateSubject(subjectId, subjectData) {
    const response = await axios.put(`${API_BASE_URL}/v1/admin/subjects/${subjectId}`, subjectData);
    return response.data;
  },

  async deleteSubject(subjectId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/admin/subjects/${subjectId}`);
    return response.data;
  },

  // Grade management
  async getGrades(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/admin/grades`, {
      params: { page, size }
    });
    return response.data;
  },

  async createGrade(gradeData) {
    const response = await axios.post(`${API_BASE_URL}/v1/admin/grades`, gradeData);
    return response.data;
  },

  async updateGrade(gradeId, gradeData) {
    const response = await axios.put(`${API_BASE_URL}/v1/admin/grades/${gradeId}`, gradeData);
    return response.data;
  },

  async deleteGrade(gradeId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/admin/grades/${gradeId}`);
    return response.data;
  }
};

export default adminService;
