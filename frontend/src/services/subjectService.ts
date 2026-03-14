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

export const subjectService = {
  async list(params: { page?: number; size?: number; name?: string; classId?: number; educationalLevelId?: number } = {}) {
    const query = new URLSearchParams()
    Object.entries(params).forEach(([k, v]) => {
      if (v !== undefined && v !== null && v !== '') query.append(k, String(v))
    })
    const response = await fetch(`/api/subjects?${query.toString()}`, { headers: getAuthHeaders() })
    return processApiResponse<any>(response)
  },
  async create(payload: SubjectItem) {
    const response = await fetch('/api/subjects', { method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<any>(response)
  },
  async update(id: number, payload: Partial<SubjectItem>) {
    const response = await fetch(`/api/subjects/${id}`, { method: 'PUT', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<void>(response)
  },
  async remove(id: number) {
    const response = await fetch(`/api/subjects/${id}`, { method: 'DELETE', headers: getAuthHeaders() })
    return processApiResponse<void>(response)
  },
  async classes() {
    const response = await fetch('/api/subjects/classes', { headers: getAuthHeaders() })
    return processApiResponse<Array<{ id: number; name: string }>>(response)
  },
  async levels() {
    const response = await fetch('/api/subjects/levels', { headers: getAuthHeaders() })
    return processApiResponse<Array<{ id: number; name: string }>>(response)
  }
}
