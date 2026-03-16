import { getAuthHeaders, processApiResponse } from './api'

export interface SubjectItem {
  id?: number
  name: string
  classId: number
  educationalLevelId: number
  description?: string
  specialization?: string
  status?: string
}

export interface PageResponse<T> {
  records: T[]
  total: number
  page: number
  size: number
}

export const subjectService = {
  async list(params: { page?: number; size?: number; filters?: Record<string, unknown> } = {}): Promise<PageResponse<SubjectItem>> {
    const response = await fetch('/api/subjects/list', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        page: params.page ?? 1,
        size: params.size ?? 10,
        filters: params.filters ?? {}
      })
    })
    return processApiResponse<PageResponse<SubjectItem>>(response)
  },
  async getById(id: number): Promise<SubjectItem> {
    const response = await fetch(`/api/subjects/${id}`, { headers: getAuthHeaders() })
    return processApiResponse<SubjectItem>(response)
  },
  async create(payload: SubjectItem): Promise<SubjectItem> {
    const response = await fetch('/api/subjects', { method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<SubjectItem>(response)
  },
  async update(id: number, payload: Partial<SubjectItem>): Promise<void> {
    const response = await fetch(`/api/subjects/${id}`, { method: 'PUT', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<void>(response)
  },
  async remove(id: number): Promise<void> {
    const response = await fetch(`/api/subjects/${id}`, { method: 'DELETE', headers: getAuthHeaders() })
    return processApiResponse<void>(response)
  },
  async getClasses(): Promise<Array<{ id: number; name: string }>> {
    const response = await fetch('/api/subjects/classes', { headers: getAuthHeaders() })
    return processApiResponse<Array<{ id: number; name: string }>>(response)
  },
  async getLevels(): Promise<Array<{ id: number; name: string }>> {
    const response = await fetch('/api/subjects/levels', { headers: getAuthHeaders() })
    return processApiResponse<Array<{ id: number; name: string }>>(response)
  },
  async getStatistics(): Promise<Record<string, unknown>> {
    const response = await fetch('/api/subjects/statistics', { headers: getAuthHeaders() })
    return processApiResponse<Record<string, unknown>>(response)
  }
}