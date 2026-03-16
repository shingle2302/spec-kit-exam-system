import { processApiResponse, getAuthHeaders } from './api';

const API_BASE_URL = '/api/knowledge-points';

export const knowledgePointService = {
  /**
   * 获取知识点列表
   */
  async list(page: number = 1, size: number = 10) {
    const response = await fetch(`${API_BASE_URL}?page=${page}&size=${size}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 获取知识点树结构
   */
  async getTree() {
    const response = await fetch(`${API_BASE_URL}/tree`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据学科ID获取知识点树
   */
  async getTreeBySubjectId(subjectId: number) {
    const response = await fetch(`${API_BASE_URL}/tree/${subjectId}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 根据ID获取知识点
   */
  async getById(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },

  /**
   * 创建知识点
   */
  async create(knowledgePoint: any) {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(knowledgePoint),
    });
    return processApiResponse(response);
  },

  /**
   * 更新知识点
   */
  async update(id: number, knowledgePoint: any) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(knowledgePoint),
    });
    return processApiResponse(response);
  },

  /**
   * 删除知识点
   */
  async delete(id: number) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return processApiResponse(response);
  },
};