<template>
  <a-layout class="main-layout">
    <!-- Sidebar -->
    <a-layout-sider 
      v-model:collapsed="collapsed" 
      collapsible 
      :trigger="null"
      class="sidebar"
    >
      <div class="logo">
        <span v-if="!collapsed">用户管理系统</span>
        <span v-else>U</span>
      </div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="inline"
      >
        <a-menu-item key="dashboard" @click="navigateTo('/dashboard')">
          <template #icon><DashboardOutlined /></template>
          <span>仪表盘</span>
        </a-menu-item>
        
        <a-menu-item 
          v-if="authStore.isAdmin" 
          key="users" 
          @click="navigateTo('/users')"
        >
          <template #icon><UserOutlined /></template>
          <span>用户管理</span>
        </a-menu-item>
        
        <a-menu-item 
          v-if="authStore.isAdmin" 
          key="roles" 
          @click="navigateTo('/roles')"
        >
          <template #icon><TeamOutlined /></template>
          <span>角色管理</span>
        </a-menu-item>
        
        <a-menu-item 
          v-if="authStore.isAdmin" 
          key="menus" 
          @click="navigateTo('/menus')"
        >
          <template #icon><AppstoreOutlined /></template>
          <span>菜单管理</span>
        </a-menu-item>
        
        <a-menu-item 
          v-if="authStore.isAdmin" 
          key="permissions" 
          @click="navigateTo('/permissions')"
        >
          <template #icon><LockOutlined /></template>
          <span>权限管理</span>
        </a-menu-item>
        
        <a-menu-item key="profile" @click="navigateTo('/profile')">
          <template #icon><SettingOutlined /></template>
          <span>个人中心</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <a-layout>
      <!-- Header -->
      <a-layout-header class="header">
        <div class="header-left">
          <MenuFoldOutlined
            v-if="!collapsed"
            class="trigger"
            @click="collapsed = true"
          />
          <MenuUnfoldOutlined
            v-else
            class="trigger"
            @click="collapsed = false"
          />
        </div>
        
        <div class="header-right">
          <a-dropdown>
            <a class="user-dropdown" @click.prevent>
              <a-avatar :size="32">
                <template #icon><UserOutlined /></template>
              </a-avatar>
              <span class="username">{{ authStore.currentUser?.username }}</span>
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item key="profile" @click="navigateTo('/profile')">
                  <UserOutlined />
                  <span style="margin-left: 8px;">个人中心</span>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" @click="handleLogout">
                  <LogoutOutlined />
                  <span style="margin-left: 8px;">退出登录</span>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>

      <!-- Content -->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>

      <!-- Footer -->
      <a-layout-footer class="footer">
        用户管理系统 ©2026 Created by Spec Kit
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store'
import {
  DashboardOutlined,
  UserOutlined,
  TeamOutlined,
  SettingOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  LogoutOutlined,
  AppstoreOutlined,
  LockOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const collapsed = ref(false)
const selectedKeys = ref<string[]>([])

// Watch route changes and update selected menu
watch(
  () => route.path,
  (path) => {
    if (path.includes('/dashboard')) {
      selectedKeys.value = ['dashboard']
    } else if (path.includes('/users')) {
      selectedKeys.value = ['users']
    } else if (path.includes('/roles')) {
      selectedKeys.value = ['roles']
    } else if (path.includes('/menus')) {
      selectedKeys.value = ['menus']
    } else if (path.includes('/permissions')) {
      selectedKeys.value = ['permissions']
    } else if (path.includes('/profile')) {
      selectedKeys.value = ['profile']
    }
  },
  { immediate: true }
)

function navigateTo(path: string) {
  router.push(path)
}

async function handleLogout() {
  await authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
}

.sidebar {
  background: #001529;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: bold;
  background: rgba(255, 255, 255, 0.1);
}

.header {
  background: #fff;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.trigger {
  font-size: 18px;
  cursor: pointer;
  transition: color 0.3s;
}

.trigger:hover {
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
  color: rgba(0, 0, 0, 0.65);
}

.content {
  margin: 24px;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  min-height: calc(100vh - 64px - 70px - 48px);
}

.footer {
  text-align: center;
  color: rgba(0, 0, 0, 0.45);
}
</style>