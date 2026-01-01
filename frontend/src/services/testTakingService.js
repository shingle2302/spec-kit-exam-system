import axios from 'axios';

const API_BASE_URL = '/api/v1';

const testTakingService = {
  async getTestForStudent(testId, studentId) {
    try {
      const response = await axios.get(`${API_BASE_URL}/tests/${testId}/take`, {
        params: { studentId }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching test for student:', error);
      throw error;
    }
  },

  async submitTest(testId, studentId, responses) {
    try {
      const response = await axios.post(`${API_BASE_URL}/tests/${testId}/submit`, responses, {
        params: { studentId }
      });
      return response.data;
    } catch (error) {
      console.error('Error submitting test:', error);
      throw error;
    }
  }
};

export default testTakingService;