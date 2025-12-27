import axios from 'axios';

const API_BASE_URL = '/api/v1';

const testService = {
  async getTestsForStudent(studentId) {
    try {
      const response = await axios.get(`${API_BASE_URL}/tests/student/${studentId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching tests for student:', error);
      throw error;
    }
  },

  async createTest(testData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/tests`, testData);
      return response.data;
    } catch (error) {
      console.error('Error creating test:', error);
      throw error;
    }
  },

  async submitTest(testId, responses) {
    try {
      const response = await axios.post(`${API_BASE_URL}/tests/${testId}/submit`, responses);
      return response.data;
    } catch (error) {
      console.error('Error submitting test:', error);
      throw error;
    }
  }
};

export default testService;