/**
 * Service for menu-related API calls
 */

import { processApiResponse, getAuthHeaders } from './api';

// Base API URL
const BASE_URL = '/api';

/**
 * Get menu tree structure for a specific role
 * @param {string} roleId - The ID of the role
 * @returns {Promise} Promise object representing the response
 */
export const getMenuTree = async (roleId = null) => {
  let url = `${BASE_URL}/menu/tree`;
  if (roleId) {
    url += `?roleId=${encodeURIComponent(roleId)}`;
  }
  const response = await fetch(url, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Create a new menu
 * @param {Object} menu - The menu object to create
 * @returns {Promise} Promise object representing the response
 */
export const createMenu = async (menu) => {
  const response = await fetch(`${BASE_URL}/menu/create`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(menu)
  });
  
  return processApiResponse(response);
};

/**
 * Update an existing menu
 * @param {Object} menu - The menu object to update
 * @returns {Promise} Promise object representing the response
 */
export const updateMenu = async (menu) => {
  const response = await fetch(`${BASE_URL}/menu/update`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(menu)
  });
  
  return processApiResponse(response);
};

/**
 * Delete a menu by ID
 * @param {string} menuId - The ID of the menu to delete
 * @returns {Promise} Promise object representing the response
 */
export const deleteMenu = async (menuId) => {
  const response = await fetch(`${BASE_URL}/menu/delete/${encodeURIComponent(menuId)}`, {
    method: 'DELETE',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Get all menus
 * @returns {Promise} Promise object representing the response
 */
export const getAllMenus = async () => {
  const response = await fetch(`${BASE_URL}/menu/list`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};

/**
 * Get a menu by ID
 * @param {string} menuId - The ID of the menu to get
 * @returns {Promise} Promise object representing the response
 */
export const getMenuById = async (menuId) => {
  const response = await fetch(`${BASE_URL}/menu/${encodeURIComponent(menuId)}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });
  
  return processApiResponse(response);
};