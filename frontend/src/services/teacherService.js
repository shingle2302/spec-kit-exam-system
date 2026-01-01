import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const teacherService = {
  async getTeachers(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/teachers`, {
      params: { page, size }
    });
    return response.data;
  },

  async createTeacher(teacherData) {
    const response = await axios.post(`${API_BASE_URL}/v1/teachers`, teacherData);
    return response.data;
  },

  async updateTeacher(teacherId, teacherData) {
    const response = await axios.put(`${API_BASE_URL}/v1/teachers/${teacherId}`, teacherData);
    return response.data;
  },

  async deleteTeacher(teacherId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/teachers/${teacherId}`);
    return response.data;
  },

  async getTeacherClasses(teacherId) {
    const response = await axios.get(`${API_BASE_URL}/v1/teachers/${teacherId}/classes`);
    return response.data;
  }
};

export default teacherService;