<template>
  <a-layout style="min-height: 100vh;">
    <a-layout-header class="header">
      <div class="logo">Exam Management System</div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
        @click="handleMenuClick"
      >
        <template v-if="!user">
          <a-menu-item key="login">Login</a-menu-item>
          <a-menu-item key="register">Register</a-menu-item>
        </template>
        <template v-else>
          <template v-if="user.role === 'STUDENT'">
            <a-menu-item key="student-dashboard">Dashboard</a-menu-item>
            <a-menu-item key="student-error-book">Error Book</a-menu-item>
          </template>
          <template v-else-if="user.role === 'TEACHER'">
            <a-menu-item key="teacher-dashboard">Dashboard</a-menu-item>
            <a-menu-item key="teacher-questions">Questions</a-menu-item>
          </template>
          <template v-else-if="user.role === 'ADMIN'">
            <a-menu-item key="admin-dashboard">Dashboard</a-menu-item>
            <a-menu-item key="admin-users">Users</a-menu-item>
          </template>
          <a-menu-item key="logout" style="float: right;">
            <span>Hi, {{ user.firstName || user.username }}</span> (Logout)
          </a-menu-item>
        </template>
      </a-menu>
    </a-layout-header>
    <a-layout-content style="padding: 0 50px; margin-top: 24px;">
      <div :style="{ padding: '24px', background: '#fff', minHeight: '380px' }">
        <router-view />
      </div>
    </a-layout-content>
    <a-layout-footer style="text-align: center;">
      Exam Management System ©{{ new Date().getFullYear() }}
    </a-layout-footer>
  </a-layout>
</template>

<script>
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  name: 'MainLayout',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const selectedKeys = ref(['home']);
    const user = ref(null);
    
    // Load user from localStorage
    const loadUser = () => {
      const userData = localStorage.getItem('user');
      if (userData) {
        user.value = JSON.parse(userData);
      } else {
        user.value = null;
      }
    };
    
    // Initialize user
    loadUser();
    
    // Update selected menu based on current route
    const updateSelectedKey = () => {
      const path = route.path;
      if (path === '/') selectedKeys.value = ['home'];
      else if (path === '/login') selectedKeys.value = ['login'];
      else if (path === '/register') selectedKeys.value = ['register'];
      else if (path.includes('/student')) selectedKeys.value = ['student-dashboard'];
      else if (path.includes('/teacher')) selectedKeys.value = ['teacher-dashboard'];
      else if (path.includes('/admin')) selectedKeys.value = ['admin-dashboard'];
    };
    
    // Watch for route changes
    watch(route, () => {
      updateSelectedKey();
    }, { immediate: true });
    
    const handleMenuClick = ({ key }) => {
      if (key === 'logout') {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        delete axios.defaults.headers.common['Authorization'];
        router.push('/login');
      } else if (key === 'login') {
        router.push('/login');
      } else if (key === 'register') {
        router.push('/register');
      } else if (key === 'student-dashboard') {
        router.push('/student/dashboard');
      } else if (key === 'student-error-book') {
        router.push('/student/error-book');
      } else if (key === 'teacher-dashboard') {
        router.push('/teacher/dashboard');
      } else if (key === 'teacher-questions') {
        router.push('/teacher/questions');
      } else if (key === 'admin-dashboard') {
        router.push('/admin/dashboard');
      } else if (key === 'admin-users') {
        router.push('/admin/users');
      }
    };
    
    return {
      selectedKeys,
      user,
      handleMenuClick
    };
  }
};
</script>

<style scoped>
#components-layout-demo-top .logo {
  float: left;
  width: 120px;
  line-height: 31px;
  padding-left: 20px;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.header {
  position: relative;
  z-index: 1;
  width: 100%;
  background: #001529;
}

.logo {
  float: left;
  color: white;
  font-size: 20px;
  line-height: 64px;
  padding-left: 20px;
  font-weight: bold;
}
</style>