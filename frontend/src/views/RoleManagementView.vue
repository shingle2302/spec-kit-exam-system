<template>
  <div class="role-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">角色管理</h1>
        <p class="page-description">管理系统角色和权限配置</p>
      </div>
      <div class="header-actions">
        <a-button @click="handleExport" :loading="exporting">
          <template #icon><ExportOutlined /></template>
          导出数据
        </a-button>
        <a-button type="primary" @click="handleAdd">
          <template #icon><PlusOutlined /></template>
          新增角色
        </a-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filter-section">
        <a-card class="filter-card" :bordered="false">
          <a-form layout="inline" :model="filters" class="filter-form">
            <a-form-item label="角色名称">
              <a-input 
                v-model:value="filters.name" 
                placeholder="请输入角色名称"
                allow-clear
                style="width: 200px"
              />
            </a-form-item>
            <a-form-item label="角色代码">
              <a-input 
                v-model:value="filters.code" 
                placeholder="请输入角色代码"
                allow-clear
                style="width: 200px"
              />
            </a-form-item>
            <a-form-item label="状态">
              <a-select 
                v-model:value="filters.status" 
                placeholder="请选择状态"
                allow-clear
                style="width: 150px"
              >
                <a-select-option value="ACTIVE">启用</a-select-option>
                <a-select-option value="INACTIVE">禁用</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item>
              <a-space>
                <a-button type="primary" @click="handleSearch">
                  <template #icon><SearchOutlined /></template>
                  搜索
                </a-button>
                <a-button @click="handleReset">
                  <template #icon><ReloadOutlined /></template>
                  重置
                </a-button>
              </a-space>
            </a-form-item>
          </a-form>
        </a-card>
      </div>

      <div class="table-section">
        <ModernDataTable
          :columns="columns"
          :data-source="roleStore.roles"
          :loading="roleStore.loading"
          :pagination="roleStore.pagination"
          row-key="id"
          @change="handleTableChange"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'icon'">
              <div class="role-icon">
                <CrownOutlined v-if="record.code === 'ADMIN'" />
                <TeamOutlined v-else-if="record.code === 'TEACHER'" />
                <UserOutlined v-else />
              </div>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-tag :color="getStatusColor(record.status)" class="status-tag">
                {{ getStatusText(record.status) }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'permissions'">
              <a-space :size="[4, 4]" wrap>
                <a-tag 
                  v-for="permission in record.permissions?.slice(0, 3)" 
                  :key="permission"
                  color="blue"
                  class="permission-tag"
                >
                  {{ permission }}
                </a-tag>
                <a-tag v-if="record.permissions?.length > 3" color="default">
                  +{{ record.permissions.length - 3 }}
                </a-tag>
              </a-space>
            </template>
            <template v-else-if="column.key === 'createdAt'">
              <span class="date-text">{{ formatDate(record.createdAt) }}</span>
            </template>
            <template v-else-if="column.key === 'action'">
              <a-space>
                <a-tooltip title="查看详情">
                  <a-button type="link" size="small" @click="handleView(record)">
                    <EyeOutlined />
                  </a-button>
                </a-tooltip>
                <a-tooltip title="编辑">
                  <a-button type="link" size="small" @click="handleEdit(record)">
                    <EditOutlined />
                  </a-button>
                </a-tooltip>
                <a-tooltip title="权限配置">
                  <a-button type="link" size="small" @click="handlePermissions(record)">
                    <SettingOutlined />
                  </a-button>
                </a-tooltip>
                <a-popconfirm 
                  v-if="record.code !== 'ADMIN'"
                  title="确定要删除该角色吗？"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="handleDelete(record)"
                >
                  <a-tooltip title="删除">
                    <a-button type="link" size="small" danger>
                      <DeleteOutlined />
                    </a-button>
                  </a-tooltip>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </ModernDataTable>
      </div>
    </div>

    <RoleFormModal
      v-model:visible="formVisible"
      :role="currentRole"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <RoleDetailDrawer
      v-model:visible="detailVisible"
      :role="currentRole"
    />

    <PermissionConfigModal
      v-model:visible="permissionVisible"
      :role="currentRole"
      @success="handlePermissionSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoleStore } from '@/store'
import { 
  PlusOutlined,
  ExportOutlined,
  SearchOutlined,
  ReloadOutlined,
  UserOutlined,
  TeamOutlined,
  CrownOutlined,
  EyeOutlined,
  EditOutlined,
  SettingOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import type { TableProps } from 'ant-design-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'
import RoleFormModal from '@/components/RoleFormModal.vue'
import RoleDetailDrawer from '@/components/RoleDetailDrawer.vue'
import PermissionConfigModal from '@/components/PermissionConfigModal.vue'
import type { Role } from '@/types'

const roleStore = useRoleStore()

const formVisible = ref(false)
const detailVisible = ref(false)
const permissionVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const currentRole = ref<Role | null>(null)
const exporting = ref(false)

const filters = reactive({
  name: '',
  code: '',
  status: undefined as string | undefined
})

const columns = [
  {
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    width: 80,
    align: 'center' as const
  },
  {
    title: '角色名称',
    dataIndex: 'name',
    key: 'name',
    width: 150,
    sorter: true
  },
  {
    title: '角色代码',
    dataIndex: 'code',
    key: 'code',
    width: 150,
    sorter: true
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description',
    width: 200,
    ellipsis: true
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    filters: [
      { text: '启用', value: 'ACTIVE' },
      { text: '禁用', value: 'INACTIVE' }
    ]
  },
  {
    title: '权限',
    dataIndex: 'permissions',
    key: 'permissions',
    width: 300
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    key: 'createdAt',
    width: 180,
    sorter: true
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    fixed: 'right' as const
  }
]

onMounted(() => {
  loadData()
})

async function loadData() {
  await roleStore.fetchRoles(filters)
}

function handleTableChange: TableProps['onChange'] = (pagination, filters, sorter) => {
  roleStore.pagination.current = pagination.current
  roleStore.pagination.pageSize = pagination.pageSize
  loadData()
}

function handleSearch() {
  roleStore.pagination.current = 1
  loadData()
}

function handleReset() {
  filters.name = ''
  filters.code = ''
  filters.status = undefined
  roleStore.pagination.current = 1
  loadData()
}

function handleAdd() {
  formMode.value = 'create'
  currentRole.value = null
  formVisible.value = true
}

function handleEdit(record: Role) {
  formMode.value = 'edit'
  currentRole.value = record
  formVisible.value = true
}

function handleView(record: Role) {
  currentRole.value = record
  detailVisible.value = true
}

function handlePermissions(record: Role) {
  currentRole.value = record
  permissionVisible.value = true
}

async function handleDelete(record: Role) {
  try {
    await roleStore.deleteRole(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

async function handleExport() {
  exporting.value = true
  try {
    await roleStore.exportRoles(filters)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}

function handleFormSuccess() {
  formVisible.value = false
  loadData()
}

function handlePermissionSuccess() {
  permissionVisible.value = false
  loadData()
}

function getStatusColor(status: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'success',
    'INACTIVE': 'default'
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

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
.role-management {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0 0 8px;
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
}

.page-description {
  margin: 0;
  color: #666;
  font-size: 15px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.page-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.filter-section {
  margin-bottom: 16px;
}

.filter-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: flex-end;
}

.table-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.role-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin: 0 auto;
}

.status-tag {
  font-weight: 500;
}

.permission-tag {
  font-size: 12px;
  margin: 2px;
}

.date-text {
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .role-management {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .page-title {
    font-size: 24px;
  }

  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-form :deep(.ant-form-item) {
    width: 100%;
  }

  .filter-form :deep(.ant-input),
  .filter-form :deep(.ant-select) {
    width: 100% !important;
  }
}

@media (max-width: 480px) {
  .role-management {
    padding: 12px;
  }

  .page-title {
    font-size: 20px;
  }

  .header-actions {
    width: 100%;
  }

  .header-actions :deep(.ant-btn) {
    flex: 1;
  }
}
</style>