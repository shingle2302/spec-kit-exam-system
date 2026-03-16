import { processApiResponse, getAuthHeaders } from './api';

const API_BASE_URL = '/api/questions';

export const questionService = {
  /**
   * 获取试题列表
   */
  async list(page: number = 1, size: number = 10) {
    const response = await fetch(`${API_BASE_URL}?page=${page}&size=${size}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据条件查询试题
   */
  async listByCondition(subjectId?: number, gradeId?: number, knowledgePointId?: number) {
    let url = `${API_BASE_URL}/condition`;
    const params = new URLSearchParams();
    if (subjectId) params.append('subjectId', subjectId.toString());
    if (gradeId) params.append('gradeId', gradeId.toString());
    if (knowledgePointId) params.append('knowledgePointId', knowledgePointId.toString());
    if (params.toString()) url += `?${params.toString()}`;

    const response = await fetch(url, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据ID获取试题
   */
  async getById(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 创建试题
   */
  async create(question: any) {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(question),
    });
    return processApiResponse(response);
  },

  /**
   * 更新试题
   */
  async update(id: number, question: any) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(question),
    });
    return processApiResponse(response);
  },

  /**
   * 删除试题
   */
  async delete(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },
};