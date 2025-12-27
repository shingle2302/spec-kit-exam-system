import axios from 'axios';

const API_BASE_URL = '/api/v1';

const errorBookService = {
  async getErrorBookForStudent(studentId) {
    try {
      const response = await axios.get(`${API_BASE_URL}/error-book/${studentId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching error book:', error);
      throw error;
    }
  },

  async practiceQuestion(studentId, questionId, response) {
    try {
      const response = await axios.post(`${API_BASE_URL}/error-book/${studentId}/practice`, {
        questionId,
        response
      });
      return response.data;
    } catch (error) {
      console.error('Error practicing question:', error);
      throw error;
    }
  }
};

export default errorBookService;