import { defineStore } from 'pinia';
import { ref } from 'vue';
import { userService } from '../services/userService';

interface User {
  id: string;
  username: string;
  email: string;
  role: string;
  phone: string;
  status: string;
}

export const useUserStore = defineStore('user', () => {
  const users = ref<User[]>([]);
  const currentUser = ref<User | null>(null);
  const loading = ref(false);

  async function fetchUsers(page: number = 1, size: number = 10) {
    loading.value = true;
    try {
      const response = await userService.list({ page, size });
      users.value = response.records || [];
    } catch (error) {
      console.error('Failed to fetch users:', error);
    } finally {
      loading.value = false;
    }
  }

  async function updateUser(userId: string, userData: Partial<User>) {
    try {
      const response = await userService.update(userId, userData);
      const index = users.value.findIndex(u => u.id === userId);
      if (index !== -1) {
        users.value[index] = { ...users.value[index], ...userData };
      }
      return true;
    } catch (error) {
      console.error('Failed to update user:', error);
      return false;
    }
  }

  async function deleteUser(userId: string) {
    try {
      await userService.remove(userId);
      users.value = users.value.filter(u => u.id !== userId);
      return true;
    } catch (error) {
      console.error('Failed to delete user:', error);
      return false;
    }
  }

  function setCurrentUser(user: User) {
    currentUser.value = user;
  }

  function clearCurrentUser() {
    currentUser.value = null;
  }

  return {
    users,
    currentUser,
    loading,
    fetchUsers,
    updateUser,
    deleteUser,
    setCurrentUser,
    clearCurrentUser
  };
});