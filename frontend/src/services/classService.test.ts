import { classService } from './classService'
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

describe('classService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('list', () => {
    it('should successfully get classes with pagination', async () => {
      const params = {
        page: 1,
        size: 10,
        name: 'Class',
        gradeId: 1
      }

      const mockResponse = {
        records: [
          { id: 1, name: 'Class 1' },
          { id: 2, name: 'Class 2' }
        ],
        total: 2,
        current: 1,
        size: 10,
        pages: 1
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await classService.list(params)

      expect(mockFetch).toHaveBeenCalledWith('/api/classes?page=1&size=10&name=Class&gradeId=1', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('create', () => {
    it('should successfully create class', async () => {
      const classData = {
        name: 'New Class',
        gradeId: 1,
        educationalLevelId: 2,
        capacity: 30
      }

      const mockResponse = {
        id: 1,
        name: 'New Class',
        gradeId: 1,
        educationalLevelId: 2,
        capacity: 30
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await classService.create(classData)

      expect(mockFetch).toHaveBeenCalledWith('/api/classes', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(classData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('update', () => {
    it('should successfully update class', async () => {
      const classData = {
        name: 'Updated Class',
        gradeId: 1,
        educationalLevelId: 2,
        capacity: 35
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      const result = await classService.update(1, classData)

      expect(mockFetch).toHaveBeenCalledWith('/api/classes/1', {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(classData)
      })

      expect(result).toBeUndefined()
    })
  })

  describe('remove', () => {
    it('should successfully delete class', async () => {
      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await classService.remove(1)

      expect(mockFetch).toHaveBeenCalledWith('/api/classes/1', {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })

  describe('grades', () => {
    it('should successfully get grades', async () => {
      const mockGrades = [
        { id: 1, name: 'Grade 1' },
        { id: 2, name: 'Grade 2' }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockGrades, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockGrades)

      const result = await classService.grades()

      expect(mockFetch).toHaveBeenCalledWith('/api/classes/grades', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockGrades)
    })
  })
})