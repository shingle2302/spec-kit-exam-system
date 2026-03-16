import { examPaperService } from './examPaperService';
import { processApiResponse, getAuthHeaders } from './api';

// Mock the api module
jest.mock('./api', () => ({
  processApiResponse: jest.fn(),
  getAuthHeaders: jest.fn(() => ({ 'Authorization': 'Bearer test-token' }))
}));

// Mock global fetch
global.fetch = jest.fn();

describe('examPaperService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('list', () => {
    it('should fetch exam paper list with pagination', async () => {
      const mockResponse = { data: { list: [], total: 0 } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.list(1, 10);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers?page=1&size=10', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('listByGradeId', () => {
    it('should fetch exam papers by grade id', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.listByGradeId(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers/grade/1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('listBySubjectAndGrade', () => {
    it('should fetch exam papers by subject and grade', async () => {
      const mockResponse = { data: [] };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.listBySubjectAndGrade(1, 2);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers/subject-grade?subjectId=1&gradeId=2', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getById', () => {
    it('should fetch exam paper by id', async () => {
      const mockResponse = { data: { id: 1, name: 'Math Exam' } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.getById(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers/1', {
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('create', () => {
    it('should create an exam paper', async () => {
      const mockExamPaper = {
        name: 'Math Exam',
        subjectId: 1,
        classId: 1,
        gradeId: 1,
        blueprint: '{}',
        status: 'DRAFT'
      };
      const mockResponse = { data: { id: 1, ...mockExamPaper } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.create(mockExamPaper);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockExamPaper)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('update', () => {
    it('should update an exam paper', async () => {
      const mockExamPaper = {
        name: 'Math Exam Updated',
        subjectId: 1,
        classId: 1,
        gradeId: 1,
        blueprint: '{}',
        status: 'DRAFT'
      };
      const mockResponse = { data: { id: 1, ...mockExamPaper } };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.update(1, mockExamPaper);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers/1', {
        method: 'PUT',
        headers: {
          'Authorization': 'Bearer test-token',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mockExamPaper)
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });

  describe('delete', () => {
    it('should delete an exam paper', async () => {
      const mockResponse = { data: null };
      (processApiResponse as jest.Mock).mockResolvedValue(mockResponse);
      (global.fetch as jest.Mock).mockResolvedValue({});

      const result = await examPaperService.delete(1);

      expect(global.fetch).toHaveBeenCalledWith('/api/exam-papers/1', {
        method: 'DELETE',
        headers: { 'Authorization': 'Bearer test-token' }
      });
      expect(processApiResponse).toHaveBeenCalledWith({});
      expect(result).toEqual(mockResponse);
    });
  });
});