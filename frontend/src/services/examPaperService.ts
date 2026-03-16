import { BaseService } from './baseService'
import type { PageResponse } from '@/types/common'

export class ExamPaperService extends BaseService<any, any, any> {
  protected readonly baseUrl = '/api/exam-papers'

  async listByGradeId(gradeId: number) {
    return this.request<any>(`${this.baseUrl}/grade/${gradeId}`)
  }

  async listBySubjectAndGrade(subjectId: number, gradeId: number) {
    return this.request<any>(`${this.baseUrl}/subject-grade?subjectId=${subjectId}&gradeId=${gradeId}`)
  }
}

export const examPaperService = new ExamPaperService()