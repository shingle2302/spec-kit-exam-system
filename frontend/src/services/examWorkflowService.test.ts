import { examWorkflowService } from './examWorkflowService'
import { processApiResponse, getAuthHeaders } from './api'

// Mock dependencies
jest.mock('./api', () => ({
  processApiResponse: jest.fn(),
  getAuthHeaders: jest.fn(() => ({ 'Authorization': 'Bearer test-token' }))
}))

const mockProcessApiResponse = processApiResponse as jest.MockedFunction<typeof processApiResponse>
const mockGetAuthHeaders = getAuthHeaders as jest.MockedFunction<typeof getAuthHeaders>

// Mock fetch
global.fetch = jest.fn() as jest.MockedFunction<typeof fetch>

const mockFetch = global.fetch as jest.MockedFunction<typeof fetch>

describe('examWorkflowService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('createPaper', () => {
    it('should successfully create paper', async () => {
      const paperData = {
        name: 'Math Exam',
        subjectId: 1,
        classId: 2,
        blueprint: JSON.stringify({ sections: [] })
      }

      const mockResponse = {
        id: 1,
        ...paperData,
        status: 'DRAFT'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.createPaper(paperData)

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-workflow/papers', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(paperData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('scheduleSession', () => {
    it('should successfully schedule session', async () => {
      const sessionData = {
        paperId: 1,
        classId: 2,
        subjectId: 3,
        startTime: '2024-06-01T09:00:00',
        endTime: '2024-06-01T11:00:00'
      }

      const mockResponse = {
        id: 1,
        ...sessionData,
        status: 'SCHEDULED'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.scheduleSession(sessionData)

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-workflow/sessions', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(sessionData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('startInvigilation', () => {
    it('should successfully start invigilation', async () => {
      const sessionId = 1
      const teacherId = 'teacher1'

      const mockResponse = {
        sessionId,
        teacherId,
        startTime: new Date().toISOString()
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.startInvigilation(sessionId, teacherId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/exam-workflow/sessions/${sessionId}/invigilation/start?teacherId=${teacherId}`, {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('endInvigilation', () => {
    it('should successfully end invigilation', async () => {
      const sessionId = 1
      const teacherId = 'teacher1'

      const mockResponse = {
        sessionId,
        teacherId,
        endTime: new Date().toISOString()
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.endInvigilation(sessionId, teacherId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/exam-workflow/sessions/${sessionId}/invigilation/end?teacherId=${teacherId}`, {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('submit', () => {
    it('should successfully submit exam', async () => {
      const submissionData = {
        sessionId: 1,
        studentId: 'student1',
        answers: JSON.stringify({ questions: [] })
      }

      const mockResponse = {
        id: 1,
        ...submissionData,
        status: 'SUBMITTED'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.submit(submissionData)

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-workflow/submissions', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(submissionData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('grade', () => {
    it('should successfully grade submission', async () => {
      const submissionId = 1
      const score = 85
      const gradedBy = 'teacher1'

      const mockResponse = {
        submissionId,
        score,
        gradedBy,
        gradedAt: new Date().toISOString()
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.grade(submissionId, score, gradedBy)

      expect(mockFetch).toHaveBeenCalledWith(`/api/exam-workflow/submissions/${submissionId}/grade?score=${score}&gradedBy=${gradedBy}`, {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('publish', () => {
    it('should successfully publish session results', async () => {
      const sessionId = 1

      const mockResponse = {
        sessionId,
        publishedAt: new Date().toISOString()
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.publish(sessionId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/exam-workflow/sessions/${sessionId}/publish`, {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('analysis', () => {
    it('should successfully get session analysis', async () => {
      const sessionId = 1

      const mockResponse = {
        sessionId,
        averageScore: 75,
        highestScore: 95,
        lowestScore: 50,
        passRate: 80
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.analysis(sessionId)

      expect(mockFetch).toHaveBeenCalledWith(`/api/exam-workflow/sessions/${sessionId}/analysis`, {
        method: 'POST',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('dashboard', () => {
    it('should successfully get dashboard data', async () => {
      const mockResponse = {
        totalExams: 10,
        upcomingExams: 3,
        completedExams: 7,
        averageScore: 78
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examWorkflowService.dashboard()

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-workflow/dashboard', {
        method: 'GET',
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })
})