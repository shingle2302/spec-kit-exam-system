import { questionService } from './questionService';
import { processApiResponse, getAuthHeaders } from './api';

// Mock the api module
jest.mock('./api', () => ({
  processApiResponse: jest.fn(),
  getAuthHeaders: jest.fn(() => ({ 'Authorization': 'Bearer test-token' }))
}));

// Mock global fetch
global.fetch = jest.fn();

describe('questionService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('list', () => {
    it('should fetch question list with pagination', async () => {
      const mockResponse = { data: { list: [], total: 0 } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.list(1, 10);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions?page=1&size=10', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('listByCondition', () => {
    it('should fetch questions by condition', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.listByCondition(1, 2, 3);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions/condition?subjectId=1&gradeId=2&knowledgePointId=3', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });

    it('should fetch questions with partial conditions', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.listByCondition(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions/condition?subjectId=1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getById', () => {
    it('should fetch question by id', async () => {
      const mockResponse = { data: { id: 1, content: '1+1=?' } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.getById(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions/1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('create', () => {
    it('should create a question', async () => {
      const mockQuestion = {
        content: '1+1=?',
        type: '选择题',
        difficulty: 'EASY',
        score: 5,
        answer: '2',
        analysis: '1+1=2',
        subjectId: 1,
        gradeId: 1,
        knowledgePointId: 1
      };
      const mockResponse = { data: { id: 1, ...mockQuestion } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.create(mockQuestion);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockQuestion)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('update', () => {
    it('should update a question', async () => {
      const mockQuestion = {
        content: '1+1=?',
        type: '选择题',
        difficulty: 'EASY',
        score: 5,
        answer: '2',
        analysis: '1+1=2',
        subjectId: 1,
        gradeId: 1,
        knowledgePointId: 1
      };
      const mockResponse = { data: { id: 1, ...mockQuestion } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.update(1, mockQuestion);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions/1', {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockQuestion)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('delete', () => {
    it('should delete a question', async () => {
      const mockResponse = { data: null };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await questionService.delete(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/questions/1', {
        method: 'DELETE',
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });
});