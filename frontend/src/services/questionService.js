import axios from 'axios';

const API_BASE_URL = '/api/v1';

const questionService = {
  async getQuestions(params = {}) {
    try {
      const response = await axios.get(`${API_BASE_URL}/questions`, { params });
      return response.data;
    } catch (error) {
      console.error('Error fetching questions:', error);
      throw error;
    }
  },

  async createQuestion(questionData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/questions`, questionData);
      return response.data;
    } catch (error) {
      console.error('Error creating question:', error);
      throw error;
    }
  },

  async updateQuestion(id, questionData) {
    try {
      const response = await axios.put(`${API_BASE_URL}/questions/${id}`, questionData);
      return response.data;
    } catch (error) {
      console.error('Error updating question:', error);
      throw error;
    }
  },

  async deleteQuestion(id) {
    try {
      const response = await axios.delete(`${API_BASE_URL}/questions/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error deleting question:', error);
      throw error;
    }
  },

  async getQuestionById(id) {
    try {
      const response = await axios.get(`${API_BASE_URL}/questions/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching question:', error);
      throw error;
    }
  }
};

export default questionService;