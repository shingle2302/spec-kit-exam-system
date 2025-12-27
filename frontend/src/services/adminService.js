import axios from 'axios';

const API_BASE_URL = '/api/v1';

const adminService = {
  async createStudent(studentData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/admin/students`, studentData);
      return response.data;
    } catch (error) {
      console.error('Error creating student:', error);
      throw error;
    }
  },

  async createTeacher(teacherData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/admin/teachers`, teacherData);
      return response.data;
    } catch (error) {
      console.error('Error creating teacher:', error);
      throw error;
    }
  },

  async createSubject(subjectData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/admin/subjects`, subjectData);
      return response.data;
    } catch (error) {
      console.error('Error creating subject:', error);
      throw error;
    }
  },

  async createGrade(gradeData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/admin/grades`, gradeData);
      return response.data;
    } catch (error) {
      console.error('Error creating grade:', error);
      throw error;
    }
  }
};

export default adminService;