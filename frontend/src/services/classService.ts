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

export const classService = {
  async list(params: { page?: number; size?: number; name?: string; gradeId?: number } = {}) {
    const query = new URLSearchParams()
    Object.entries(params).forEach(([k, v]) => {
      if (v !== undefined && v !== null && v !== '') query.append(k, String(v))
    })
    const response = await fetch(`/api/classes?${query.toString()}`, { headers: getAuthHeaders() })
    return processApiResponse<any>(response)
  },
  async create(payload: ClassItem) {
    const response = await fetch('/api/classes', { method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<any>(response)
  },
  async update(id: number, payload: Partial<ClassItem>) {
    const response = await fetch(`/api/classes/${id}`, { method: 'PUT', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<void>(response)
  },
  async remove(id: number) {
    const response = await fetch(`/api/classes/${id}`, { method: 'DELETE', headers: getAuthHeaders() })
    return processApiResponse<void>(response)
  },
  async grades() {
    const response = await fetch('/api/classes/grades', { headers: getAuthHeaders() })
    return processApiResponse<Array<{ id: number; name: string }>>(response)
  }
}
