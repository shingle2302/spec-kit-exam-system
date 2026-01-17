/**
 * API utility functions
 */

// Define the backend Result format
interface BackendResult<T> {
  data: T;
  code: string;
  msg: string;
}

/**
 * Process the backend API response according to Result format {data, code, msg}
 * @param response - The fetch response
 * @returns The actual data from the response
 */
export async function processApiResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  const result: BackendResult<T> = await response.json();
  
  // Check if the backend returned an error code
  if (result.code !== '0000') {
    throw new Error(result.msg || `Backend error with code: ${result.code}`);
  }
  
  return result.data;
}

/**
 * Get authorization header with token
 */
export function getAuthHeaders(): Record<string, string> {
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}` || ''
  };
}