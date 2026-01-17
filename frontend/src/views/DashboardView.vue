<template>
  <div class="dashboard">
    <a-page-header title="仪表盘" sub-title="欢迎回来" />

    <a-row :gutter="[24, 24]">
      <!-- Statistics Cards -->
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card>
          <a-statistic
            title="用户总数"
            :value="stats.totalUsers"
            :value-style="{ color: '#3f8600' }"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card>
          <a-statistic
            title="活跃用户"
            :value="stats.activeUsers"
            :value-style="{ color: '#1890ff' }"
          >
            <template #prefix>
              <CheckCircleOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card>
          <a-statistic
            title="锁定用户"
            :value="stats.lockedUsers"
            :value-style="{ color: '#cf1322' }"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      
      <a-col :xs="24" :sm="12" :lg="6">
        <a-card>
          <a-statistic
            title="角色数量"
            :value="stats.totalRoles"
            :value-style="{ color: '#722ed1' }"
          >
            <template #prefix>
              <TeamOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>

    <!-- User Info Card -->
    <a-row :gutter="[24, 24]" style="margin-top: 24px;">
      <a-col :xs="24" :lg="12">
        <a-card title="当前用户信息">
          <a-descriptions :column="1" bordered size="small">
            <a-descriptions-item label="用户名">
              {{ authStore.currentUser?.username }}
            </a-descriptions-item>
            <a-descriptions-item label="邮箱">
              {{ authStore.currentUser?.email }}
            </a-descriptions-item>
            <a-descriptions-item label="手机">
              {{ authStore.currentUser?.phone || '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="状态">
              <a-tag :color="getStatusColor(authStore.currentUser?.status)">
                {{ getStatusText(authStore.currentUser?.status) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="角色">
              <a-tag v-if="authStore.isAdmin" color="gold">超级管理员</a-tag>
              <a-tag v-else color="blue">普通用户</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="最后登录">
              {{ formatDate(authStore.currentUser?.lastLoginAt) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>

      <a-col :xs="24" :lg="12">
        <a-card title="快速操作">
          <a-space direction="vertical" style="width: 100%;" size="middle">
            <a-button 
              v-if="authStore.isAdmin" 
              type="primary" 
              block 
              @click="$router.push('/users')"
            >
              <template #icon><UserOutlined /></template>
              管理用户
            </a-button>
            <a-button 
              v-if="authStore.isAdmin" 
              block 
              @click="$router.push('/roles')"
            >
              <template #icon><TeamOutlined /></template>
              管理角色
            </a-button>
            <a-button block @click="$router.push('/profile')">
              <template #icon><SettingOutlined /></template>
              个人设置
            </a-button>
          </a-space>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore, useUserStore, useRoleStore } from '@/store'
import {
  UserOutlined,
  CheckCircleOutlined,
  LockOutlined,
  TeamOutlined,
  SettingOutlined
} from '@ant-design/icons-vue'

const authStore = useAuthStore()
const userStore = useUserStore()
const roleStore = useRoleStore()

const stats = ref({
  totalUsers: 0,
  activeUsers: 0,
  lockedUsers: 0,
  totalRoles: 0
})

onMounted(async () => {
  if (authStore.isAdmin) {
    await Promise.all([
      userStore.fetchUsers(),
      roleStore.fetchRoles()
    ])
    
    stats.value.totalUsers = userStore.users.length
    stats.value.activeUsers = userStore.users.filter(u => u.status === 'ACTIVE').length
    stats.value.lockedUsers = userStore.users.filter(u => u.status === 'LOCKED').length
    stats.value.totalRoles = roleStore.roles.length
  }
})

function getStatusColor(status?: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'green',
    'INACTIVE': 'default',
    'SUSPENDED': 'orange',
    'LOCKED': 'red'
  }
  return colors[status || ''] || 'default'
}

function getStatusText(status?: string) {
  const texts: Record<string, string> = {
    'ACTIVE': '活跃',
    'INACTIVE': '未激活',
    'SUSPENDED': '暂停',
    'LOCKED': '锁定'
  }
  return texts[status || ''] || status
}

function formatDate(dateStr?: string | null) {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}
</style>
