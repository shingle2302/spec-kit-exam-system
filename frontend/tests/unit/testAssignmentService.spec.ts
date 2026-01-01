import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import testAssignmentService, { TestAssignment } from '../../src/services/testAssignmentService';

// Mock axios
vi.mock('axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
  },
}));

describe('testAssignmentService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('createTestAssignment', () => {
    it('should create a test assignment successfully', async () => {
      // Arrange
      const mockAssignment: TestAssignment = {
        testId: 1,
        studentId: 1,
        assignedBy: 1,
        status: 'ASSIGNED',
      };
      const mockResponse = { id: 1, ...mockAssignment };
      (axios.post as vi.MockedFunction<typeof axios.post>).mockResolvedValue({ data: mockResponse });

      // Act
      const result = await testAssignmentService.createTestAssignment(mockAssignment);

      // Assert
      expect(axios.post).toHaveBeenCalledWith('/api/v1/test-assignments', mockAssignment);
      expect(result).toEqual(mockResponse);
    });

    it('should throw an error when creating a test assignment fails', async () => {
      // Arrange
      const mockAssignment: TestAssignment = {
        testId: 1,
        studentId: 1,
        assignedBy: 1,
        status: 'ASSIGNED',
      };
      const mockError = new Error('Network error');
      (axios.post as vi.MockedFunction<typeof axios.post>).mockRejectedValue(mockError);

      // Act & Assert
      await expect(testAssignmentService.createTestAssignment(mockAssignment)).rejects.toThrow('Network error');
    });
  });

  describe('getTestAssignmentById', () => {
    it('should fetch a test assignment by ID successfully', async () => {
      // Arrange
      const assignmentId = 1;
      const mockResponse = { id: assignmentId, testId: 1, studentId: 1, status: 'ASSIGNED' };
      (axios.get as vi.MockedFunction<typeof axios.get>).mockResolvedValue({ data: mockResponse });

      // Act
      const result = await testAssignmentService.getTestAssignmentById(assignmentId);

      // Assert
      expect(axios.get).toHaveBeenCalledWith(`/api/v1/test-assignments/${assignmentId}`);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getTestAssignmentsForStudent', () => {
    it('should fetch test assignments for a student successfully', async () => {
      // Arrange
      const studentId = 1;
      const mockResponse = [{ id: 1, testId: 1, studentId, status: 'ASSIGNED' }];
      (axios.get as vi.MockedFunction<typeof axios.get>).mockResolvedValue({ data: mockResponse });

      // Act
      const result = await testAssignmentService.getTestAssignmentsForStudent(studentId);

      // Assert
      expect(axios.get).toHaveBeenCalledWith(`/api/v1/test-assignments/student/${studentId}`);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('updateTestAssignment', () => {
    it('should update a test assignment successfully', async () => {
      // Arrange
      const assignmentId = 1;
      const updatedAssignment: TestAssignment = {
        id: assignmentId,
        testId: 1,
        studentId: 1,
        assignedBy: 1,
        status: 'COMPLETED',
      };
      const mockResponse = { ...updatedAssignment };
      (axios.put as vi.MockedFunction<typeof axios.put>).mockResolvedValue({ data: mockResponse });

      // Act
      const result = await testAssignmentService.updateTestAssignment(assignmentId, updatedAssignment);

      // Assert
      expect(axios.put).toHaveBeenCalledWith(`/api/v1/test-assignments/${assignmentId}`, updatedAssignment);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('deleteTestAssignment', () => {
    it('should delete a test assignment successfully', async () => {
      // Arrange
      const assignmentId = 1;
      const mockResponse = { message: 'Assignment deleted' };
      (axios.delete as vi.MockedFunction<typeof axios.delete>).mockResolvedValue({ data: mockResponse });

      // Act
      const result = await testAssignmentService.deleteTestAssignment(assignmentId);

      // Assert
      expect(axios.delete).toHaveBeenCalledWith(`/api/v1/test-assignments/${assignmentId}`);
      expect(result).toEqual(mockResponse);
    });
  });
});