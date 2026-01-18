<template>
  <div class="user-management">
    <a-page-header title="用户管理" sub-title="管理系统用户">
      <template #extra>
        <a-button type="primary" @click="showCreateModal = true">
          <template #icon><PlusOutlined /></template>
          创建用户
        </a-button>
      </template>
    </a-page-header>

    <!-- Filters -->
    <a-card style="margin-bottom: 16px;">
      <a-row :gutter="16">
        <a-col :span="6">
          <a-select
            v-model:value="filterStatus"
            placeholder="筛选状态"
            style="width: 100%"
            allowClear
            @change="handleFilter"
          >
            <a-select-option value="ACTIVE">活跃</a-select-option>
            <a-select-option value="INACTIVE">未激活</a-select-option>
            <a-select-option value="SUSPENDED">暂停</a-select-option>
            <a-select-option value="LOCKED">锁定</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="6">
          <a-button @click="handleRefresh">
            <template #icon><ReloadOutlined /></template>
            刷新
          </a-button>
        </a-col>
      </a-row>
    </a-card>

    <!-- User Table -->
    <a-table
      :columns="columns"
      :data-source="userStore.users"
      :loading="userStore.loading"
      :row-key="(record: User) => record.id"
      :pagination="{
        current: userStore.pagination.current,
        pageSize: userStore.pagination.pageSize,
        total: userStore.pagination.total,
        showSizeChanger: true,
        showQuickJumper: true,
        onChange: handlePageChange,
        onShowSizeChange: handlePageSizeChange,
        pageSizeOptions: ['10', '20', '50', '100']
      }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.status)">
            {{ getStatusText(record.status) }}
          </a-tag>
        </template>
        <template v-else-if="column.key === 'isSuperAdmin'">
          <a-tag v-if="record.isSuperAdmin" color="gold">超级管理员</a-tag>
          <a-tag v-else>普通用户</a-tag>
        </template>
        <template v-else-if="column.key === 'createdAt'">
          {{ formatDate(record.createdAt) }}
        </template>
        <template v-else-if="column.key === 'actions'">
          <a-space>
            <a-button 
              type="link" 
              size="small" 
              @click="handleEdit(record)"
              :disabled="record.isSuperAdmin"
            >
              编辑
            </a-button>
            <a-button 
              v-if="record.status === 'LOCKED'" 
              type="link" 
              size="small" 
              @click="handleUnlock(record)"
            >
              解锁
            </a-button>
            <a-popconfirm
              title="确定要删除此用户吗？"
              @confirm="handleDelete(record)"
              :disabled="record.isSuperAdmin"
            >
              <a-button 
                type="link" 
                danger 
                size="small"
                :disabled="record.isSuperAdmin"
              >
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- Create User Modal -->
    <a-modal
      v-model:open="showCreateModal"
      title="创建用户"
      @ok="handleCreate"
      :confirm-loading="userStore.loading"
    >
      <a-form :model="createForm" layout="vertical">
        <a-form-item label="用户名" required>
          <a-input v-model:value="createForm.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="邮箱" required>
          <a-input v-model:value="createForm.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item label="手机号">
          <a-input v-model:value="createForm.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item label="密码" required>
          <a-input-password v-model:value="createForm.password" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="createForm.status">
            <a-select-option value="ACTIVE">活跃</a-select-option>
            <a-select-option value="INACTIVE">未激活</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- Edit User Modal -->
    <a-modal
      v-model:open="showEditModal"
      title="编辑用户"
      @ok="handleUpdate"
      :confirm-loading="userStore.loading"
    >
      <a-form :model="editForm" layout="vertical">
        <a-form-item label="用户名">
          <a-input v-model:value="editForm.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="邮箱">
          <a-input v-model:value="editForm.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item label="手机号">
          <a-input v-model:value="editForm.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="editForm.status">
            <a-select-option value="ACTIVE">活跃</a-select-option>
            <a-select-option value="INACTIVE">未激活</a-select-option>
            <a-select-option value="SUSPENDED">暂停</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store'
import type { User } from '@/types'
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue'

const userStore = useUserStore()

const filterStatus = ref<string>('')
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingUserId = ref<string>('')

const createForm = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  status: 'ACTIVE'
})

const editForm = reactive({
  username: '',
  email: '',
  phone: '',
  status: ''
})

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '邮箱', dataIndex: 'email', key: 'email' },
  { title: '手机', dataIndex: 'phone', key: 'phone' },
  { title: '状态', key: 'status' },
  { title: '角色', key: 'isSuperAdmin' },
  { title: '创建时间', key: 'createdAt' },
  { title: '操作', key: 'actions', width: 200 }
]

onMounted(() => {
  userStore.fetchUsers()
})

function getStatusColor(status: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'green',
    'INACTIVE': 'default',
    'SUSPENDED': 'orange',
    'LOCKED': 'red'
  }
  return colors[status] || 'default'
}

function getStatusText(status: string) {
  const texts: Record<string, string> = {
    'ACTIVE': '活跃',
    'INACTIVE': '未激活',
    'SUSPENDED': '暂停',
    'LOCKED': '锁定'
  }
  return texts[status] || status
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}

async function handleFilter() {
  await userStore.fetchUsers({ page: 1, limit: userStore.pagination.pageSize, status: filterStatus.value || undefined })
}

async function handleRefresh() {
  filterStatus.value = ''
  await userStore.fetchUsers({ page: userStore.pagination.current, limit: userStore.pagination.pageSize })
}

async function handlePageChange(page: number, pageSize: number) {
  await userStore.fetchUsers({ page, limit: pageSize, status: filterStatus.value || undefined })
}

async function handlePageSizeChange(current: number, size: number) {
  await userStore.fetchUsers({ page: 1, limit: size, status: filterStatus.value || undefined })
}

function handleEdit(user: User) {
  editingUserId.value = user.id
  editForm.username = user.username
  editForm.email = user.email
  editForm.phone = user.phone || ''
  editForm.status = user.status
  showEditModal.value = true
}

async function handleCreate() {
  const result = await userStore.createUser({
    username: createForm.username,
    email: createForm.email,
    phone: createForm.phone,
    password: createForm.password,
    status: createForm.status
  })
  if (result.success) {
    showCreateModal.value = false
    Object.assign(createForm, { username: '', email: '', phone: '', password: '', status: 'ACTIVE' })
  }
}

async function handleUpdate() {
  const result = await userStore.updateUser(editingUserId.value, {
    username: editForm.username,
    email: editForm.email,
    phone: editForm.phone,
    status: editForm.status
  })
  if (result.success) {
    showEditModal.value = false
  }
}

async function handleDelete(user: User) {
  await userStore.deleteUser(user.id)
}

async function handleUnlock(user: User) {
  await userStore.unlockUser(user.id)
}
</script>

<style scoped>
.user-management {
  padding: 0;
}
</style>