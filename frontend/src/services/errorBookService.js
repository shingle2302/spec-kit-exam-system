import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const errorBookService = {
  // Get error book for a student
  async getErrorBook(studentId, page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/error-book/${studentId}`, {
      params: { page, size }
    });
    return response.data;
  },

  // Get error book questions for practice
  async getErrorBookQuestions(studentId) {
    const response = await axios.get(`${API_BASE_URL}/v1/error-book/${studentId}/practice`);
    return response.data;
  },

  // Submit practice answer
  async submitPracticeAnswer(answerData) {
    const response = await axios.post(`${API_BASE_URL}/v1/error-book/practice`, answerData);
    return response.data;
  },

  // Get mastery statistics for a student
  async getMasteryStats(studentId) {
    const response = await axios.get(`${API_BASE_URL}/v1/error-book/${studentId}/stats`);
    return response.data;
  }
};

export default errorBookService;
