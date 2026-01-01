import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const studentService = {
  async getStudents(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/students`, {
      params: { page, size }
    });
    return response.data;
  },

  async createStudent(studentData) {
    const response = await axios.post(`${API_BASE_URL}/v1/students`, studentData);
    return response.data;
  },

  async updateStudent(studentId, studentData) {
    const response = await axios.put(`${API_BASE_URL}/v1/students/${studentId}`, studentData);
    return response.data;
  },

  async deleteStudent(studentId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/students/${studentId}`);
    return response.data;
  }
};

export default studentService;