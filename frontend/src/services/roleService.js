/**
 * Service for role-related API calls
 */

import { processApiResponse, getAuthHeaders } from './api';

// Base API URL
const BASE_URL = '/api';

/**
 * Get all roles
 * @returns {Promise} Promise object representing the response
 */
export const getAllRoles = async () => {
  const response = await fetch(`${BASE_URL}/role/list`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Create a new role
 * @param {Object} role - The role object to create
 * @returns {Promise} Promise object representing the response
 */
export const createRole = async (role) => {
  const response = await fetch(`${BASE_URL}/role/create`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(role)
  });
  
  return processApiResponse(response);
};

/**
 * Update an existing role
 * @param {Object} role - The role object to update
 * @returns {Promise} Promise object representing the response
 */
export const updateRole = async (role) => {
  const response = await fetch(`${BASE_URL}/role/update`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(role)
  });
  
  return processApiResponse(response);
};

/**
 * Delete a role by ID
 * @param {string} roleId - The ID of the role to delete
 * @returns {Promise} Promise object representing the response
 */
export const deleteRole = async (roleId) => {
  const response = await fetch(`${BASE_URL}/role/delete/${encodeURIComponent(roleId)}`, {
    method: 'DELETE',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Get a role by ID
 * @param {string} roleId - The ID of the role to get
 * @returns {Promise} Promise object representing the response
 */
export const getRoleById = async (roleId) => {
  const response = await fetch(`${BASE_URL}/role/${encodeURIComponent(roleId)}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Get a role by role code
 * @param {string} roleCode - The role code to search for
 * @returns {Promise} Promise object representing the response
 */
export const getRoleByRoleCode = async (roleCode) => {
  const response = await fetch(`${BASE_URL}/role/code/${encodeURIComponent(roleCode)}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};