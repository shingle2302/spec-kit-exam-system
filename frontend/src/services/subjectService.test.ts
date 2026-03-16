import { subjectService } from './subjectService'
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

describe('subjectService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  describe('list', () => {
    it('should successfully get subjects with pagination', async () => {
      const params = {
        page: 1,
        size: 10,
        name: 'Math',
        classId: 1,
        educationalLevelId: 2
      }

      const mockResponse = {
        records: [
          { id: 1, name: 'Math' },
          { id: 2, name: 'English' }
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

      const result = await subjectService.list(params)

      expect(mockFetch).toHaveBeenCalledWith('/api/subjects?page=1&size=10&name=Math&classId=1&educationalLevelId=2', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('create', () => {
    it('should successfully create subject', async () => {
      const subjectData = {
        name: 'New Subject',
        classId: 1,
        educationalLevelId: 2
      }

      const mockResponse = {
        id: 1,
        name: 'New Subject',
        classId: 1,
        educationalLevelId: 2
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockResponse, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockResponse)

      const result = await subjectService.create(subjectData)

      expect(mockFetch).toHaveBeenCalledWith('/api/subjects', {
        method: 'POST',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(subjectData)
      })

      expect(result).toEqual(mockResponse)
    })
  })

  describe('update', () => {
    it('should successfully update subject', async () => {
      const subjectData = {
        name: 'Updated Subject',
        classId: 1,
        educationalLevelId: 2
      }

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      const result = await subjectService.update(1, subjectData)

      expect(mockFetch).toHaveBeenCalledWith('/api/subjects/1', {
        method: 'PUT',
        headers: mockGetAuthHeaders(),
        body: JSON.stringify(subjectData)
      })

      expect(result).toBeUndefined()
    })
  })

  describe('remove', () => {
    it('should successfully delete subject', async () => {
      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: null, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(undefined)

      await subjectService.remove(1)

      expect(mockFetch).toHaveBeenCalledWith('/api/subjects/1', {
        method: 'DELETE',
        headers: mockGetAuthHeaders()
      })
    })
  })

  describe('classes', () => {
    it('should successfully get classes', async () => {
      const mockClasses = [
        { id: 1, name: 'Class 1' },
        { id: 2, name: 'Class 2' }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockClasses, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockClasses)

      const result = await subjectService.classes()

      expect(mockFetch).toHaveBeenCalledWith('/api/subjects/classes', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockClasses)
    })
  })

  describe('levels', () => {
    it('should successfully get levels', async () => {
      const mockLevels = [
        { id: 1, name: 'Primary' },
        { id: 2, name: 'Secondary' }
      ]

      mockFetch.mockResolvedValueOnce({
        ok: true,
        json: jest.fn().mockResolvedValueOnce({ data: mockLevels, code: '0000', msg: 'success' })
      } as Response)

      mockProcessApiResponse.mockResolvedValueOnce(mockLevels)

      const result = await subjectService.levels()

      expect(mockFetch).toHaveBeenCalledWith('/api/subjects/levels', {
        headers: mockGetAuthHeaders()
      })

      expect(result).toEqual(mockLevels)
    })
  })
})