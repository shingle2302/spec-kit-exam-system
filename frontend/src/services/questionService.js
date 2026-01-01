import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const questionService = {
  // Get questions with optional filters
  async getQuestions(filters = {}, page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/questions`, {
      params: { ...filters, page, size }
    });
    return response.data;
  },

  // Get question by ID
  async getQuestionById(questionId) {
    const response = await axios.get(`${API_BASE_URL}/v1/questions/${questionId}`);
    return response.data;
  },

  // Create a new question
  async createQuestion(questionData) {
    const response = await axios.post(`${API_BASE_URL}/v1/questions`, questionData);
    return response.data;
  },

  // Update an existing question
  async updateQuestion(questionId, questionData) {
    const response = await axios.put(`${API_BASE_URL}/v1/questions/${questionId}`, questionData);
    return response.data;
  },

  // Delete a question
  async deleteQuestion(questionId) {
    const response = await axios.delete(`${API_BASE_URL}/v1/questions/${questionId}`);
    return response.data;
  },

  // Search questions
  async searchQuestions(query, page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/questions/search`, {
      params: { query, page, size }
    });
    return response.data;
  }
};

export default questionService;
