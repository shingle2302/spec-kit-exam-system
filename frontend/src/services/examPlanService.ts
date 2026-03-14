import { getAuthHeaders, processApiResponse } from './api'

export interface ExamPlanItem {
  id?: number
  name: string
  academicYear: string
  term: string
  examType: string
  startTime: string
  endTime: string
  status?: string
  description?: string
}

export function buildExamPlanQueryPayload(params: { page?: number; size?: number; name?: string; academicYear?: string; examType?: string } = {}) {
  return {
    page: params.page ?? 1,
    size: params.size ?? 10,
    filters: {
      name: params.name,
      academicYear: params.academicYear,
      examType: params.examType
    }
  }
}

export const examPlanService = {
  async list(params: { page?: number; size?: number; name?: string; academicYear?: string; examType?: string } = {}) {
    const response = await fetch('/api/exam-plans/query', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(buildExamPlanQueryPayload(params))
    })
    return processApiResponse<any>(response)
  },
  async create(payload: ExamPlanItem) {
    const response = await fetch('/api/exam-plans', { method: 'POST', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<any>(response)
  },
  async update(id: number, payload: Partial<ExamPlanItem>) {
    const response = await fetch(`/api/exam-plans/${id}`, { method: 'PUT', headers: getAuthHeaders(), body: JSON.stringify(payload) })
    return processApiResponse<void>(response)
  },
  async remove(id: number) {
    const response = await fetch(`/api/exam-plans/${id}`, { method: 'DELETE', headers: getAuthHeaders() })
    return processApiResponse<void>(response)
  }
}
