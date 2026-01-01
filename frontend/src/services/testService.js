import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const testService = {
  // Get all tests
  async getTests(page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/tests`, {
      params: { page, size }
    });
    return response.data;
  },

  // Get test by ID
  async getTestById(id) {
    const response = await axios.get(`${API_BASE_URL}/v1/tests/${id}`);
    return response.data;
  },

  // Create a new test
  async createTest(testData) {
    const response = await axios.post(`${API_BASE_URL}/v1/tests`, testData);
    return response.data;
  },

  // Update an existing test
  async updateTest(id, testData) {
    const response = await axios.put(`${API_BASE_URL}/v1/tests/${id}`, testData);
    return response.data;
  },

  // Delete a test
  async deleteTest(id) {
    const response = await axios.delete(`${API_BASE_URL}/v1/tests/${id}`);
    return response.data;
  },

  // Get tests assigned to a student
  async getTestsForStudent(studentId, page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/tests/student/${studentId}`, {
      params: { page, size }
    });
    return response.data;
  },

  // Assign a test to a student
  async assignTestToStudent(testId, studentId) {
    const response = await axios.post(`${API_BASE_URL}/v1/test-assignments`, {
      testId,
      studentId
    });
    return response.data;
  },

  // Submit a test
  async submitTest(testId, submissionData) {
    const response = await axios.post(`${API_BASE_URL}/v1/tests/${testId}/submit`, submissionData);
    return response.data;
  }
};

export default testService;
