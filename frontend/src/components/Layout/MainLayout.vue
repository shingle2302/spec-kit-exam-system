<template>
  <a-layout style="min-height: 100vh;">
    <a-layout-header class="header">
      <div class="logo">{{ $t('nav.dashboard') }}</div>
      <div class="header-right">
        <LanguageSwitcher />
        <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
          @click="handleMenuClick"
        >
          <template v-if="!user">
            <a-menu-item key="login">{{ $t('nav.login') }}</a-menu-item>
            <a-menu-item key="register">{{ $t('nav.register') }}</a-menu-item>
          </template>
          <template v-else>
            <template v-if="user.role === 'STUDENT'">
              <a-menu-item key="student-dashboard">{{ $t('nav.dashboard') }}</a-menu-item>
              <a-menu-item key="student-error-book">{{ $t('nav.errorBook') }}</a-menu-item>
            </template>
            <template v-else-if="user.role === 'TEACHER'">
              <a-menu-item key="teacher-dashboard">{{ $t('nav.dashboard') }}</a-menu-item>
              <a-menu-item key="teacher-questions">{{ $t('questions.title') }}</a-menu-item>
            </template>
            <template v-else-if="user.role === 'ADMIN'">
              <a-menu-item key="admin-dashboard">{{ $t('nav.dashboard') }}</a-menu-item>
              <a-menu-item key="admin-users">{{ $t('students.title') }}</a-menu-item>
            </template>
            <a-menu-item key="logout" style="float: right;">
              <span>Hi, {{ user.firstName || user.username }}</span> ({{ $t('nav.logout') }})
            </a-menu-item>
          </template>
        </a-menu>
      </div>
    </a-layout-header>
    <a-layout-content style="padding: 0 50px; margin-top: 24px;">
      <div :style="{ padding: '24px', background: '#fff', minHeight: '380px' }">
        <router-view />
      </div>
    </a-layout-content>
    <a-layout-footer style="text-align: center;">
      {{ $t('nav.dashboard') }} ©{{ new Date().getFullYear() }}
    </a-layout-footer>
  </a-layout>
</template>

<script>
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import LanguageSwitcher from '@/components/LanguageSwitcher.vue';

export default {
  name: 'MainLayout',
  components: {
    LanguageSwitcher
  },
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
        // Remove axios auth header if it exists
        if (window.axios) {
          delete window.axios.defaults.headers.common['Authorization'];
        }
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
.header {
  position: relative;
  z-index: 1;
  width: 100%;
  background: #001529;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  color: white;
  font-size: 20px;
  font-weight: bold;
  padding-left: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.header-right .ant-menu {
  background: transparent;
}
</style>