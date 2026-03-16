import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export class GradeService extends BaseService<any, any, any> {
  protected readonly baseUrl = '/api/grades'

  async listActive() {
    return this.request<any>(`${this.baseUrl}/active`)
  }
}

export const gradeService = new GradeService()