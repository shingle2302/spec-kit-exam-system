import { knowledgePointService } from './knowledgePointService';
import { processApiResponse, getAuthHeaders } from './api';

// Mock the api module
jest.mock('./api', () => ({
  processApiResponse: jest.fn(),
  getAuthHeaders: jest.fn(() => ({ 'Authorization': 'Bearer test-token' }))
}));

// Mock global fetch
global.fetch = jest.fn();

describe('knowledgePointService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('list', () => {
    it('should fetch knowledge point list with pagination', async () => {
      const mockResponse = { data: { list: [], total: 0 } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.list(1, 10);

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points?page=1&size=10', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getTree', () => {
    it('should fetch knowledge point tree', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.getTree();

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points/tree', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getTreeBySubjectId', () => {
    it('should fetch knowledge point tree by subject id', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.getTreeBySubjectId(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points/tree/1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getById', () => {
    it('should fetch knowledge point by id', async () => {
      const mockResponse = { data: { id: 1, name: 'Math Basics' } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.getById(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points/1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('create', () => {
    it('should create a knowledge point', async () => {
      const mockKnowledgePoint = { name: 'Math Basics', code: 'MATH_BASIC', parentId: null, subjectId: 1 };
      const mockResponse = { data: { id: 1, ...mockKnowledgePoint } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.create(mockKnowledgePoint);

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockKnowledgePoint)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('update', () => {
    it('should update a knowledge point', async () => {
      const mockKnowledgePoint = { name: 'Math Basics Updated', code: 'MATH_BASIC', parentId: null, subjectId: 1 };
      const mockResponse = { data: { id: 1, ...mockKnowledgePoint } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.update(1, mockKnowledgePoint);

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points/1', {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockKnowledgePoint)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('delete', () => {
    it('should delete a knowledge point', async () => {
      const mockResponse = { data: null };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await knowledgePointService.delete(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/knowledge-points/1', {
        method: 'DELETE',
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });
});