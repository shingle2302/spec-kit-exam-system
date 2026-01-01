import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const classService = {
  async getClasses(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/classes`, {
      params: { page, size }
    });
    return response.data;
  },

  async createClass(classData) {
    const response = await axios.post(`${API_BASE_URL}/v1/classes`, classData);
    return response.data;
  },

  async updateClass(classId, classData) {
    const response = await axios.put(`${API_BASE_URL}/v1/classes/${classId}`, classData);
    return response.data;
  },

  async deleteClass(classId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/classes/${classId}`);
    return response.data;
  },

  async getStudentsInClass(classId) {
    const response = await axios.get(`${API_BASE_URL}/v1/classes/${classId}/students`);
    return response.data;
  },

  async addStudentToClass(classId, studentId) {
    const response = await axios.post(`${API_BASE_URL}/v1/classes/${classId}/students`, { studentId });
    return response.data;
  },

  async removeStudentFromClass(classId, studentId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/classes/${classId}/students/${studentId}`);
    return response.data;
  }
};

export default classService;