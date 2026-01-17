<template>
  <div class="role-management">
    <a-page-header title="角色管理" sub-title="管理系统角色和权限">
      <template #extra>
        <a-button type="primary" @click="showCreateModal = true">
          <template #icon><PlusOutlined /></template>
          创建角色
        </a-button>
      </template>
    </a-page-header>

    <!-- Role Table -->
    <a-table
      :columns="columns"
      :data-source="roleStore.roles"
      :loading="roleStore.loading"
      :row-key="(record: Role) => record.id"
      :pagination="{
        current: roleStore.pagination.current,
        pageSize: roleStore.pagination.pageSize,
        total: roleStore.pagination.total,
        showSizeChanger: true,
        showQuickJumper: true,
        onChange: handlePageChange,
        onShowSizeChange: handlePageSizeChange,
        pageSizeOptions: ['10', '20', '50', '100']
      }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'isSuperAdminRole'">
          <a-tag v-if="record.isSuperAdminRole" color="gold">是</a-tag>
          <a-tag v-else>否</a-tag>
        </template>
        <template v-else-if="column.key === 'isActive'">
          <a-tag v-if="record.isActive" color="green">活跃</a-tag>
          <a-tag v-else color="red">禁用</a-tag>
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
              :disabled="record.isSuperAdminRole"
            >
              编辑
            </a-button>
            <a-popconfirm
              title="确定要删除此角色吗？"
              @confirm="handleDelete(record)"
              :disabled="record.isSuperAdminRole"
            >
              <a-button 
                type="link" 
                danger 
                size="small"
                :disabled="record.isSuperAdminRole"
              >
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- Create Role Modal -->
    <a-modal
      v-model:open="showCreateModal"
      title="创建角色"
      @ok="handleCreate"
      :confirm-loading="roleStore.loading"
      width="600px"
    >
      <a-form :model="createForm" layout="vertical">
        <a-form-item label="角色名称" required>
          <a-input v-model:value="createForm.name" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="createForm.description" placeholder="请输入角色描述" :rows="3" />
        </a-form-item>
        <a-divider>权限配置</a-divider>
        <a-form-item label="用户管理权限">
          <a-checkbox-group v-model:value="createForm.userPermissions">
            <a-checkbox value="read">查看</a-checkbox>
            <a-checkbox value="create">创建</a-checkbox>
            <a-checkbox value="update">编辑</a-checkbox>
            <a-checkbox value="delete">删除</a-checkbox>
            <a-checkbox value="unlock">解锁</a-checkbox>
          </a-checkbox-group>
        </a-form-item>
        <a-form-item label="角色管理权限">
          <a-checkbox-group v-model:value="createForm.rolePermissions">
            <a-checkbox value="read">查看</a-checkbox>
            <a-checkbox value="create">创建</a-checkbox>
            <a-checkbox value="update">编辑</a-checkbox>
            <a-checkbox value="delete">删除</a-checkbox>
          </a-checkbox-group>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- Edit Role Modal -->
    <a-modal
      v-model:open="showEditModal"
      title="编辑角色"
      @ok="handleUpdate"
      :confirm-loading="roleStore.loading"
      width="600px"
    >
      <a-form :model="editForm" layout="vertical">
        <a-form-item label="角色名称">
          <a-input v-model:value="editForm.name" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="editForm.description" placeholder="请输入角色描述" :rows="3" />
        </a-form-item>
        <a-divider>权限配置</a-divider>
        <a-form-item label="用户管理权限">
          <a-checkbox-group v-model:value="editForm.userPermissions">
            <a-checkbox value="read">查看</a-checkbox>
            <a-checkbox value="create">创建</a-checkbox>
            <a-checkbox value="update">编辑</a-checkbox>
            <a-checkbox value="delete">删除</a-checkbox>
            <a-checkbox value="unlock">解锁</a-checkbox>
          </a-checkbox-group>
        </a-form-item>
        <a-form-item label="角色管理权限">
          <a-checkbox-group v-model:value="editForm.rolePermissions">
            <a-checkbox value="read">查看</a-checkbox>
            <a-checkbox value="create">创建</a-checkbox>
            <a-checkbox value="update">编辑</a-checkbox>
            <a-checkbox value="delete">删除</a-checkbox>
          </a-checkbox-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoleStore } from '@/store'
import type { Role, Permissions } from '@/types'
import { PlusOutlined } from '@ant-design/icons-vue'

const roleStore = useRoleStore()

const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingRoleId = ref<string>('')

const createForm = reactive({
  name: '',
  description: '',
  userPermissions: [] as string[],
  rolePermissions: [] as string[]
})

const editForm = reactive({
  name: '',
  description: '',
  userPermissions: [] as string[],
  rolePermissions: [] as string[]
})

const columns = [
  { title: '角色名称', dataIndex: 'name', key: 'name' },
  { title: '描述', dataIndex: 'description', key: 'description' },
  { title: '超级管理员角色', key: 'isSuperAdminRole' },
  { title: '状态', key: 'isActive' },
  { title: '创建时间', key: 'createdAt' },
  { title: '操作', key: 'actions', width: 150 }
]

onMounted(() => {
  roleStore.fetchRoles()
})

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}

function handlePageChange(page: number, pageSize: number) {
  roleStore.fetchRoles({ page, limit: pageSize })
}

function handlePageSizeChange(current: number, size: number) {
  roleStore.fetchRoles({ page: 1, limit: size })
}

function buildPermissions(userPerms: string[], rolePerms: string[]): Permissions {
  return {
    users: {
      read: userPerms.includes('read'),
      create: userPerms.includes('create'),
      update: userPerms.includes('update'),
      delete: userPerms.includes('delete'),
      unlock: userPerms.includes('unlock')
    },
    roles: {
      read: rolePerms.includes('read'),
      create: rolePerms.includes('create'),
      update: rolePerms.includes('update'),
      delete: rolePerms.includes('delete')
    },
    system: {
      admin: false,
      super_admin: false,
      audit: false
    }
  }
}

function extractUserPermissions(permissions: Permissions): string[] {
  const perms: string[] = []
  if (permissions.users.read) perms.push('read')
  if (permissions.users.create) perms.push('create')
  if (permissions.users.update) perms.push('update')
  if (permissions.users.delete) perms.push('delete')
  if (permissions.users.unlock) perms.push('unlock')
  return perms
}

function extractRolePermissions(permissions: Permissions): string[] {
  const perms: string[] = []
  if (permissions.roles.read) perms.push('read')
  if (permissions.roles.create) perms.push('create')
  if (permissions.roles.update) perms.push('update')
  if (permissions.roles.delete) perms.push('delete')
  return perms
}

function handleEdit(role: Role) {
  editingRoleId.value = role.id
  editForm.name = role.name
  editForm.description = role.description
  editForm.userPermissions = extractUserPermissions(role.permissions)
  editForm.rolePermissions = extractRolePermissions(role.permissions)
  showEditModal.value = true
}

async function handleCreate() {
  const result = await roleStore.createRole({
    name: createForm.name,
    description: createForm.description,
    permissions: buildPermissions(createForm.userPermissions, createForm.rolePermissions)
  })
  if (result.success) {
    showCreateModal.value = false
    Object.assign(createForm, { name: '', description: '', userPermissions: [], rolePermissions: [] })
  }
}

async function handleUpdate() {
  const result = await roleStore.updateRole(editingRoleId.value, {
    name: editForm.name,
    description: editForm.description,
    permissions: buildPermissions(editForm.userPermissions, editForm.rolePermissions)
  })
  if (result.success) {
    showEditModal.value = false
  }
}

async function handleDelete(role: Role) {
  await roleStore.deleteRole(role.id)
}
</script>

<style scoped>
.role-management {
  padding: 0;
}
</style>