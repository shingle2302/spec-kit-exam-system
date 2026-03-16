import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export class QuestionService extends BaseService<any, any, any> {
  protected readonly baseUrl = '/api/questions'

  async listByCondition(subjectId?: number, gradeId?: number, knowledgePointId?: number) {
    let url = `${this.baseUrl}/condition`
    const params = new URLSearchParams()
    if (subjectId) params.append('subjectId', subjectId.toString())
    if (gradeId) params.append('gradeId', gradeId.toString())
    if (knowledgePointId) params.append('knowledgePointId', knowledgePointId.toString())
    if (params.toString()) url += `?${params.toString()}`

    return this.request<any>(url)
  }
}

export const questionService = new QuestionService()