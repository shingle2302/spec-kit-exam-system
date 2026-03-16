import { processApiResponse, getAuthHeaders } from './api';

const API_BASE_URL = '/api/exam-papers';

export const examPaperService = {
  /**
   * 获取试卷列表
   */
  async list(page: number = 1, size: number = 10) {
    const response = await fetch(`${API_BASE_URL}?page=${page}&size=${size}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据年级ID获取试卷
   */
  async listByGradeId(gradeId: number) {
    const response = await fetch(`${API_BASE_URL}/grade/${gradeId}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据科目和年级获取试卷
   */
  async listBySubjectAndGrade(subjectId: number, gradeId: number) {
    const response = await fetch(`${API_BASE_URL}/subject-grade?subjectId=${subjectId}&gradeId=${gradeId}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据ID获取试卷
   */
  async getById(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 创建试卷
   */
  async create(examPaper: any) {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(examPaper),
    });
    return processApiResponse(response);
  },

  /**
   * 更新试卷
   */
  async update(id: number, examPaper: any) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(examPaper),
    });
    return processApiResponse(response);
  },

  /**
   * 删除试卷
   */
  async delete(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },
};
