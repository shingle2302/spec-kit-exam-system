/**
 * Service for permission-related API calls
 */

import { processApiResponse, getAuthHeaders } from './api';

// Base API URL
const BASE_URL = '/api';

/**
 * Get permissions for a specific role
 * @param {string} roleId - The ID of the role
 * @returns {Promise} Promise object representing the response
 */
export const getPermissionsByRole = async (roleId) => {
  const response = await fetch(`${BASE_URL}/permission/role/${encodeURIComponent(roleId)}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Assign permissions to a role
 * @param {string} roleId - The ID of the role
 * @param {Array<string>} permissionIds - List of permission IDs to assign
 * @returns {Promise} Promise object representing the response
 */
export const assignPermissionsToRole = async (roleId, permissionIds) => {
  const response = await fetch(`${BASE_URL}/permission/assign`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({
      roleId: roleId,
      permissionIds: permissionIds
    })
  });
  
  return processApiResponse(response);
};

/**
 * Remove permissions from a role
 * @param {string} roleId - The ID of the role
 * @param {Array<string>} permissionIds - List of permission IDs to remove
 * @returns {Promise} Promise object representing the response
 */
export const removePermissionsFromRole = async (roleId, permissionIds) => {
  const response = await fetch(`${BASE_URL}/permission/remove`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({
      roleId: roleId,
      permissionIds: permissionIds
    })
  });
  
  return processApiResponse(response);
};

/**
 * Get all permissions
 * @returns {Promise} Promise object representing the response
 */
export const getAllPermissions = async () => {
  const response = await fetch(`${BASE_URL}/permission/list`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Create a new permission
 * @param {Object} permission - The permission object to create
 * @returns {Promise} Promise object representing the response
 */
export const createPermission = async (permission) => {
  const response = await fetch(`${BASE_URL}/permission/create`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(permission)
  });
  
  return processApiResponse(response);
};

/**
 * Update an existing permission
 * @param {Object} permission - The permission object to update
 * @returns {Promise} Promise object representing the response
 */
export const updatePermission = async (permission) => {
  const response = await fetch(`${BASE_URL}/permission/update`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(permission)
  });
  
  return processApiResponse(response);
};

/**
 * Delete a permission by ID
 * @param {string} permissionId - The ID of the permission to delete
 * @returns {Promise} Promise object representing the response
 */
export const deletePermission = async (permissionId) => {
  const response = await fetch(`${BASE_URL}/permission/delete/${encodeURIComponent(permissionId)}`, {
    method: 'DELETE',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};