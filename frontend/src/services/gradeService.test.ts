import { gradeService } from './gradeService';
import { processApiResponse, getAuthHeaders } from './api';

// Mock the api module
jest.mock('./api', () => ({
  processApiResponse: jest.fn(),
  getAuthHeaders: jest.fn(() => ({ 'Authorization': 'Bearer test-token' }))
}));

// Mock global fetch
global.fetch = jest.fn();

describe('gradeService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('list', () => {
    it('should fetch grade list with pagination', async () => {
      const mockResponse = { data: { list: [], total: 0 } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await gradeService.list(1, 10);

      expect(global.fetch).toHaveBeenCalledWith('/api/grades?page=1&size=10', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('listActive', () => {
    it('should fetch active grade list', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await gradeService.listActive();

      expect(global.fetch).toHaveBeenCalledWith('/api/grades/active', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getById', () => {
    it('should fetch grade by id', async () => {
      const mockResponse = { data: { id: 1, name: 'Grade 1' } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await gradeService.getById(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/grades/1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('create', () => {
    it('should create a grade', async () => {
      const mockGrade = { name: 'Grade 1', educationalLevelId: 1 };
      const mockResponse = { data: { id: 1, ...mockGrade } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await gradeService.create(mockGrade);

      expect(global.fetch).toHaveBeenCalledWith('/api/grades', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockGrade)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('update', () => {
    it('should update a grade', async () => {
      const mockGrade = { name: 'Grade 1 Updated', educationalLevelId: 1 };
      const mockResponse = { data: { id: 1, ...mockGrade } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await gradeService.update(1, mockGrade);

      expect(global.fetch).toHaveBeenCalledWith('/api/grades/1', {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockGrade)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('delete', () => {
    it('should delete a grade', async () => {
      const mockResponse = { data: null };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await gradeService.delete(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/grades/1', {
        method: 'DELETE',
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });
});