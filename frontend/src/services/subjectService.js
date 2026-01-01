import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const subjectService = {
  async getSubjects(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/subjects`, {
      params: { page, size }
    });
    return response.data;
  },

  async createSubject(subjectData) {
    const response = await axios.post(`${API_BASE_URL}/v1/subjects`, subjectData);
    return response.data;
  },

  async updateSubject(subjectId, subjectData) {
    const response = await axios.put(`${API_BASE_URL}/v1/subjects/${subjectId}`, subjectData);
    return response.data;
  },

  async deleteSubject(subjectId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/subjects/${subjectId}`);
    return response.data;
  }
};

export default subjectService;