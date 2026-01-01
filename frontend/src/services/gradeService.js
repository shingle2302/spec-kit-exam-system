import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const gradeService = {
  async getGrades(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/grades`, {
      params: { page, size }
    });
    return response.data;
  },

  async createGrade(gradeData) {
    const response = await axios.post(`${API_BASE_URL}/v1/grades`, gradeData);
    return response.data;
  },

  async updateGrade(gradeId, gradeData) {
    const response = await axios.put(`${API_BASE_URL}/v1/grades/${gradeId}`, gradeData);
    return response.data;
  },

  async deleteGrade(gradeId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/grades/${gradeId}`);
    return response.data;
  }
};

export default gradeService;