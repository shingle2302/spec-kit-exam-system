import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const gradingService = {
  // Get student responses for grading
  async getStudentResponsesForGrading(testId, page = 1, size = 10) {
    const response = await axios.get(`${API_BASE_URL}/v1/grading/${testId}/responses`, {
      params: { page, size }
    });
    return response.data;
  },

  // Grade a student response
  async gradeStudentResponse(responseId, gradeData) {
    const response = await axios.put(`${API_BASE_URL}/v1/grading/responses/${responseId}`, gradeData);
    return response.data;
  },

  // Get grading statistics for a test
  async getGradingStats(testId) {
    const response = await axios.get(`${API_BASE_URL}/v1/grading/${testId}/stats`);
    return response.data;
  },

  // Bulk grade responses
  async bulkGradeResponses(testId, gradingData) {
    const response = await axios.post(`${API_BASE_URL}/v1/grading/${testId}/bulk-grade`, gradingData);
    return response.data;
  }
};

export default gradingService;