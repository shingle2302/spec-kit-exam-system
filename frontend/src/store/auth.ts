import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { authService } from '../services/authService';

interface UserInfo {
  id: string;
  username: string;
  email: string;
  role: string;
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('accessToken'));
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'));
  const user = ref<UserInfo | null>(null);

  const isAuthenticated = computed(() => !!token.value);
  const isAdmin = computed(() => user.value?.role === 'ADMIN');
  const isTeacher = computed(() => user.value?.role === 'TEACHER');
  const isStudent = computed(() => user.value?.role === 'STUDENT');

  async function login(username: string, password: string) {
    try {
      const response = await authService.login(username, password);
      token.value = response.accessToken;
      refreshToken.value = response.refreshToken;
      user.value = response.user;
      
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      localStorage.setItem('user', JSON.stringify(response.user));
      
      return true;
    } catch (error) {
      console.error('Login failed:', error);
      return false;
    }
  }

  async function refreshAccessToken() {
    try {
      if (!refreshToken.value) {
        throw new Error('No refresh token available');
      }
      
      const response = await authService.refreshToken(refreshToken.value);
      token.value = response.accessToken;
      refreshToken.value = response.refreshToken;
      
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      
      return true;
    } catch (error) {
      console.error('Token refresh failed:', error);
      await logout();
      return false;
    }
  }

  async function logout() {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Logout failed:', error);
    } finally {
      token.value = null;
      refreshToken.value = null;
      user.value = null;
      
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
    }
  }

  function loadUserFromStorage() {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      user.value = JSON.parse(storedUser);
    }
  }

  return {
    token,
    refreshToken,
    user,
    isAuthenticated,
    isAdmin,
    isTeacher,
    isStudent,
    login,
    refreshAccessToken,
    logout,
    loadUserFromStorage
  };
});