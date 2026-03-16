import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

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

export class ExamPlanService extends BaseService<ExamPlanItem, ExamPlanItem, Partial<ExamPlanItem>> {
  protected readonly baseUrl = '/api/exam-plans'

  async query(params: { page?: number; size?: number; name?: string; academicYear?: string; examType?: string } = {}) {
    return this.request<any>(`${this.baseUrl}/query`, {
      method: 'POST',
      body: JSON.stringify(buildExamPlanQueryPayload(params))
    })
  }
}

export const examPlanService = new ExamPlanService()