import { getAuthHeaders, processApiResponse } from './api'

export interface ClassItem {
  id?: number
  name: string
  gradeId: number
  educationalLevelId?: number
  capacity: number
  description?: string
  status?: string
}

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export const classService = {
  async list(params: { page?: number; size?: number; filters?: Record<string, unknown> } = {}): Promise<PageResponse<ClassItem>> {
    const response = await fetch('/api/classes/list', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params.page ?? 1,
        size: params.size ?? 10,
        filters: params.filters ?? {}
      })
    })
    return processApiResponse<PageResponse<ClassItem>>(response)
  },
  async getById(id: number): Promise<ClassItem> {
    const response = await fetch(`/api/classes/${id}`, { headers: getAuthHeaders() })
    return processApiResponse<ClassItem>(response)
  },
  async create(payload: ClassItem): Promise<ClassItem> {
    const response = await fetch('/api/classes', { method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<ClassItem>(response)
  },
  async update(id: number, payload: Partial<ClassItem>): Promise<void> {
    const response = await fetch(`/api/classes/${id}`, { method: 'PUT', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<void>(response)
  },
  async remove(id: number): Promise<void> {
    const response = await fetch(`/api/classes/${id}`, { method: 'DELETE', headers: getAuthHeaders() })
    return processApiResponse<void>(response)
  },
  async getGrades(): Promise<Array<{ id: number; name: string }>> {
    const response = await fetch('/api/classes/grades', { headers: getAuthHeaders() })
    return processApiResponse<Array<{ id: number; name: string }>>(response)
  },
  async getStatistics(): Promise<Record<string, unknown>> {
    const response = await fetch('/api/classes/statistics', { headers: getAuthHeaders() })
    return processApiResponse<Record<string, unknown>>(response)
  }
}