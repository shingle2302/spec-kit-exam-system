<template>
  <div class="permission-management">
    <a-page-header title="权限管理" sub-title="管理系统权限">
      <template #extra>
        <a-button type="primary" @click="showCreateModal = true">
          <template #icon><PlusOutlined /></template>
          新增权限
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
            <a-select-option value="ACTIVE">启用</a-select-option>
            <a-select-option value="INACTIVE">禁用</a-select-option>
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

    <!-- Permission Table -->
    <a-table
      :columns="columns"
      :data-source="permissionStore.permissions"
      :loading="permissionStore.loading"
      :row-key="(record: Permission) => record.id"
      :pagination="{
        current: permissionStore.pagination.current,
        pageSize: permissionStore.pagination.pageSize,
        total: permissionStore.pagination.total,
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
        <template v-else-if="column.key === 'operationType'">
          <a-tag :color="getOperationTypeColor(record.operationType)">
            {{ getOperationTypeText(record.operationType) }}
          </a-tag>
        </template>
        <template v-else-if="column.key === 'createdAt'">
          {{ formatDate(record.createdAt) }}
        </template>
        <template v-else-if="column.key === 'updatedAt'">
          {{ formatDate(record.updatedAt) }}
        </template>
        <template v-else-if="column.key === 'actions'">
          <a-space>
            <a-button 
              type="link" 
              size="small" 
              @click="handleEdit(record)"
            >
              编辑
            </a-button>
            <a-popconfirm
              title="确定要删除此权限吗？"
              @confirm="handleDelete(record)"
            >
              <a-button 
                type="link" 
                danger 
                size="small"
              >
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- Create Permission Modal -->
    <a-modal
      v-model:open="showCreateModal"
      title="新增权限"
      @ok="handleCreate"
      :confirm-loading="permissionStore.loading"
      width="600px"
    >
      <a-form :model="createForm" layout="vertical">
        <a-form-item label="菜单ID" required>
          <a-input v-model:value="createForm.menuId" placeholder="请输入菜单ID" />
        </a-form-item>
        <a-form-item label="菜单名称">
          <a-input v-model:value="createForm.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="按钮名称">
          <a-input v-model:value="createForm.buttonName" placeholder="请输入按钮名称" />
        </a-form-item>
        <a-form-item label="操作类型" required>
          <a-select v-model:value="createForm.operationType" placeholder="请选择操作类型">
            <a-select-option value="QUERY">查询</a-select-option>
            <a-select-option value="CREATE">创建</a-select-option>
            <a-select-option value="UPDATE">更新</a-select-option>
            <a-select-option value="DELETE">删除</a-select-option>
            <a-select-option value="UNLOCK">解锁</a-select-option>
            <a-select-option value="LOCK">锁定</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="createForm.description" placeholder="请输入权限描述" :rows="3" />
        </a-form-item>
        <a-form-item label="状态" required>
          <a-select v-model:value="createForm.status" placeholder="请选择状态">
            <a-select-option value="ACTIVE">启用</a-select-option>
            <a-select-option value="INACTIVE">禁用</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- Edit Permission Modal -->
    <a-modal
      v-model:open="showEditModal"
      title="编辑权限"
      @ok="handleUpdate"
      :confirm-loading="permissionStore.loading"
      width="600px"
    >
      <a-form :model="editForm" layout="vertical">
        <a-form-item label="菜单ID" required>
          <a-input v-model:value="editForm.menuId" placeholder="请输入菜单ID" />
        </a-form-item>
        <a-form-item label="菜单名称">
          <a-input v-model:value="editForm.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="按钮名称">
          <a-input v-model:value="editForm.buttonName" placeholder="请输入按钮名称" />
        </a-form-item>
        <a-form-item label="操作类型" required>
          <a-select v-model:value="editForm.operationType" placeholder="请选择操作类型">
            <a-select-option value="QUERY">查询</a-select-option>
            <a-select-option value="CREATE">创建</a-select-option>
            <a-select-option value="UPDATE">更新</a-select-option>
            <a-select-option value="DELETE">删除</a-select-option>
            <a-select-option value="UNLOCK">解锁</a-select-option>
            <a-select-option value="LOCK">锁定</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="editForm.description" placeholder="请输入权限描述" :rows="3" />
        </a-form-item>
        <a-form-item label="状态" required>
          <a-select v-model:value="editForm.status" placeholder="请选择状态">
            <a-select-option value="ACTIVE">启用</a-select-option>
            <a-select-option value="INACTIVE">禁用</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { usePermissionStore } from '@/store'
import type { Permission } from '@/types'
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue'

const permissionStore = usePermissionStore()

const filterStatus = ref<string>('')
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingPermissionId = ref<string>('')

const createForm = reactive({
  menuId: '',
  menuName: '',
  buttonName: '',
  operationType: 'QUERY' as any,
  description: '',
  status: 'ACTIVE' as any
})

const editForm = reactive({
  menuId: '',
  menuName: '',
  buttonName: '',
  operationType: 'QUERY' as any,
  description: '',
  status: 'ACTIVE' as any
})

const columns = [
  { title: '菜单ID', dataIndex: 'menuId', key: 'menuId' },
  { title: '菜单名称', dataIndex: 'menuName', key: 'menuName' },
  { title: '按钮名称', dataIndex: 'buttonName', key: 'buttonName' },
  { title: '操作类型', key: 'operationType' },
  { title: '描述', dataIndex: 'description', key: 'description' },
  { title: '状态', key: 'status' },
  { title: '创建时间', key: 'createdAt' },
  { title: '更新时间', key: 'updatedAt' },
  { title: '操作', key: 'actions', width: 150 }
]

onMounted(() => {
  permissionStore.fetchPermissions()
})

function getStatusColor(status: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'green',
    'INACTIVE': 'red'
  }
  return colors[status] || 'default'
}

function getStatusText(status: string) {
  const texts: Record<string, string> = {
    'ACTIVE': '启用',
    'INACTIVE': '禁用'
  }
  return texts[status] || status
}

function getOperationTypeColor(operationType: string) {
  const colors: Record<string, string> = {
    'QUERY': 'blue',
    'CREATE': 'green',
    'UPDATE': 'orange',
    'DELETE': 'red',
    'UNLOCK': 'purple',
    'LOCK': 'cyan'
  }
  return colors[operationType] || 'default'
}

function getOperationTypeText(operationType: string) {
  const texts: Record<string, string> = {
    'QUERY': '查询',
    'CREATE': '创建',
    'UPDATE': '更新',
    'DELETE': '删除',
    'UNLOCK': '解锁',
    'LOCK': '锁定'
  }
  return texts[operationType] || operationType
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}

async function handleFilter() {
  await permissionStore.fetchPermissions({ page: 1, limit: permissionStore.pagination.pageSize, status: filterStatus.value || undefined })
}

async function handleRefresh() {
  filterStatus.value = ''
  await permissionStore.fetchPermissions({ page: permissionStore.pagination.current, limit: permissionStore.pagination.pageSize })
}

async function handlePageChange(page: number, pageSize: number) {
  await permissionStore.fetchPermissions({ page, limit: pageSize, status: filterStatus.value || undefined })
}

async function handlePageSizeChange(current: number, size: number) {
  await permissionStore.fetchPermissions({ page: 1, limit: size, status: filterStatus.value || undefined })
}

function handleEdit(permission: Permission) {
  editingPermissionId.value = permission.id
  editForm.menuId = permission.menuId
  editForm.menuName = permission.menuName
  editForm.buttonName = permission.buttonName
  editForm.operationType = permission.operationType
  editForm.description = permission.description
  editForm.status = permission.status
  showEditModal.value = true
}

async function handleCreate() {
  const result = await permissionStore.createPermission({
    menuId: createForm.menuId,
    menuName: createForm.menuName,
    buttonName: createForm.buttonName,
    operationType: createForm.operationType,
    description: createForm.description,
    status: createForm.status
  })
  if (result.success) {
    showCreateModal.value = false
    Object.assign(createForm, { 
      menuId: '', 
      menuName: '', 
      buttonName: '', 
      operationType: 'QUERY', 
      description: '', 
      status: 'ACTIVE' 
    })
  }
}

async function handleUpdate() {
  const result = await permissionStore.updatePermission(editingPermissionId.value, {
    menuId: editForm.menuId,
    menuName: editForm.menuName,
    buttonName: editForm.buttonName,
    operationType: editForm.operationType,
    description: editForm.description,
    status: editForm.status
  })
  if (result.success) {
    showEditModal.value = false
  }
}

async function handleDelete(permission: Permission) {
  await permissionStore.deletePermission(permission.id)
}
</script>

<style scoped>
.permission-management {
  padding: 0;
}
</style>