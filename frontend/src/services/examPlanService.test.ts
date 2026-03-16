import { examPlanService, ExamPlanItem, buildExamPlanQueryPayload } from './examPlanService'
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

describe('examPlanService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('list', () => {
    it('should successfully get exam plans with default params', async () => {
      const mockResponse = {
        records: [
          {
            id: 1,
            name: 'Midterm Exam',
            academicYear: '2024-2025',
            term: 'Spring',
            examType: 'Midterm',
            startTime: '2024-06-01T09:00:00',
            endTime: '2024-06-10T17:00:00',
            status: 'ACTIVE'
          }
        ],
        total: 1
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examPlanService.list()

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-plans/query', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 1,
          size: 10,
          filters: {
            name: undefined,
            academicYear: undefined,
            examType: undefined
          }
        })
      })

      expect(result).toEqual(mockResponse)
    })

    it('should successfully get exam plans with custom params', async () => {
      const mockResponse = {
        records: [
          {
            id: 1,
            name: 'Final Exam',
            academicYear: '2024-2025',
            term: 'Spring',
            examType: 'Final',
            startTime: '2024-07-01T09:00:00',
            endTime: '2024-07-10T17:00:00',
            status: 'ACTIVE'
          }
        ],
        total: 1
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examPlanService.list({
        page: 2,
        size: 20,
        name: 'Final',
        academicYear: '2024-2025',
        examType: 'Final'
      })

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-plans/query', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify({
          page: 2,
          size: 20,
          filters: {
            name: 'Final',
            academicYear: '2024-2025',
            examType: 'Final'
          }
        })
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('create', () => {
    it('should successfully create exam plan', async () => {
      const examPlan: ExamPlanItem = {
        name: 'New Exam Plan',
        academicYear: '2024-2025',
        term: 'Spring',
        examType: 'Midterm',
        startTime: '2024-06-01T09:00:00',
        endTime: '2024-06-10T17:00:00',
        description: 'Spring midterm exam'
      }

      const mockResponse = {
        id: 1,
        ...examPlan,
        status: 'ACTIVE'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await examPlanService.create(examPlan)

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-plans', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(examPlan)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('update', () => {
    it('should successfully update exam plan', async () => {
      const updateData: Partial<ExamPlanItem> = {
        name: 'Updated Exam Plan',
        description: 'Updated description'
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await examPlanService.update(1, updateData)

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-plans/1', {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(updateData)
      })
    })
  })

  describe('remove', () => {
    it('should successfully remove exam plan', async () => {
      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await examPlanService.remove(1)

      expect(mockFetch).toHaveBeenCalledWith('/api/exam-plans/1', {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })
})

describe('buildExamPlanQueryPayload', () => {
  it('should build payload with default values', () => {
    const result = buildExamPlanQueryPayload()
    expect(result).toEqual({
      page: 1,
      size: 10,
      filters: {
        name: undefined,
        academicYear: undefined,
        examType: undefined
      }
    })
  })

  it('should build payload with custom values', () => {
    const result = buildExamPlanQueryPayload({
      page: 3,
      size: 50,
      name: 'Test',
      academicYear: '2023-2024',
      examType: 'Midterm'
    })
    expect(result).toEqual({
      page: 3,
      size: 50,
      filters: {
        name: 'Test',
        academicYear: '2023-2024',
        examType: 'Midterm'
      }
    })
  })
})