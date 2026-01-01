import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const studentTransferService = {
  async transferStudent(transferData) {
    const response = await axios.post(`${API_BASE_URL}/v1/student-transfer`, transferData);
    return response.data;
  },

  async getTransferHistory(studentId) {
    const response = await axios.get(`${API_BASE_URL}/v1/student-transfer/history/${studentId}`);
    return response.data;
  }
};

export default studentTransferService;