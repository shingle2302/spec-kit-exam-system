import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export class KnowledgePointService extends BaseService<any, any, any> {
  protected readonly baseUrl = '/api/knowledge-points'

  async getTree(): Promise<any> {
    return this.request<any>(`${this.baseUrl}/tree`)
  }

  async getTreeBySubjectId(subjectId: number): Promise<any> {
    return this.request<any>(`${this.baseUrl}/tree/${subjectId}`)
  }

  async getSubjects(): Promise<any> {
    return this.request<any>('/api/subjects')
  }
}

export const knowledgePointService = new KnowledgePointService()