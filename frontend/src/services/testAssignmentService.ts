import axios from 'axios';

const API_BASE_URL = '/api/v1';

export interface TestAssignment {
  id?: number;
  testId: number;
  studentId: number;
  assignedBy: number;
  assignedAt?: string;
  dueDate?: string;
  status: string;
}

export interface BulkTestAssignmentRequest {
  testId: number;
  studentIds: number[];
  assignedBy: number;
  dueDate?: string;
}

const testAssignmentService = {
  async createTestAssignment(assignment: TestAssignment) {
    try {
      const response = await axios.post(`${API_BASE_URL}/test-assignments`, assignment);
      return response.data;
    } catch (error) {
      console.error('Error creating test assignment:', error);
      throw error;
    }
  },

  async bulkAssignTest(requestData: BulkTestAssignmentRequest) {
    try {
      const response = await axios.post(`${API_BASE_URL}/test-assignments/bulk-assign`, requestData);
      return response.data;
    } catch (error) {
      console.error('Error bulk assigning test:', error);
      throw error;
    }
  },

  async getTestAssignmentById(id: number) {
    try {
      const response = await axios.get(`${API_BASE_URL}/test-assignments/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching test assignment:', error);
      throw error;
    }
  },

  async getTestAssignmentsForStudent(studentId: number) {
    try {
      const response = await axios.get(`${API_BASE_URL}/test-assignments/student/${studentId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching test assignments for student:', error);
      throw error;
    }
  },

  async getTestAssignmentsForTest(testId: number) {
    try {
      const response = await axios.get(`${API_BASE_URL}/test-assignments/test/${testId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching test assignments for test:', error);
      throw error;
    }
  },

  async getTestAssignmentsForStudentByStatus(studentId: number, status: string) {
    try {
      const response = await axios.get(`${API_BASE_URL}/test-assignments/student/${studentId}/status/${status}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching test assignments by status:', error);
      throw error;
    }
  },

  async getTestAssignmentsByTeacher(teacherId: number) {
    try {
      const response = await axios.get(`${API_BASE_URL}/test-assignments/teacher/${teacherId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching test assignments by teacher:', error);
      throw error;
    }
  },

  async updateTestAssignment(id: number, assignment: TestAssignment) {
    try {
      const response = await axios.put(`${API_BASE_URL}/test-assignments/${id}`, assignment);
      return response.data;
    } catch (error) {
      console.error('Error updating test assignment:', error);
      throw error;
    }
  },

  async deleteTestAssignment(id: number) {
    try {
      const response = await axios.delete(`${API_BASE_URL}/test-assignments/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error deleting test assignment:', error);
      throw error;
    }
  }
};

export default testAssignmentService;