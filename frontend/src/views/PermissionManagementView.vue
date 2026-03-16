<template>
  <div class="permission-management">
    <a-page-header class="page-header" title="权限管理" sub-title="管理系统权限">
      <template #extra>
        <a-button type="primary" @click="showCreateModal = true" class="add-button">
          <template #icon><PlusOutlined /></template>
          新增权限
        </a-button>
      </template>
    </a-page-header>

    <div class="content-wrapper">
      <a-card class="filter-card">
        <a-row :gutter="16">
          <a-col :xs="24" :sm="12" :md="8" :lg="6">
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
          <a-col :xs="24" :sm="12" :md="8" :lg="6">
            <a-space>
              <a-button @click="handleRefresh" class="action-button">
                <template #icon><ReloadOutlined /></template>
                刷新
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-card>

      <a-card class="table-card">
        <ModernDataTable
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
            <template v-if="column.key === 'icon'">
              <div class="permission-icon">
                <SafetyOutlined />
              </div>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-tag :color="getStatusColor(record.status)" class="status-tag">
                {{ getStatusText(record.status) }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'operationType'">
              <a-tag :color="getOperationTypeColor(record.operationType)" class="operation-tag">
                {{ getOperationTypeText(record.operationType) }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'createdAt'">
              <span class="date-text">{{ formatDate(record.createdAt) }}</span>
            </template>
            <template v-else-if="column.key === 'updatedAt'">
              <span class="date-text">{{ formatDate(record.updatedAt) }}</span>
            </template>
            <template v-else-if="column.key === 'actions'">
              <a-space>
                <a-button 
                  type="link" 
                  size="small" 
                  @click="handleEdit(record)"
                  class="action-button"
                >
                  <EditOutlined />
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
                    class="action-button"
                  >
                    <DeleteOutlined />
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </template>
        </ModernDataTable>
      </a-card>
    </div>

    <a-modal
      v-model:open="showCreateModal"
      title="新增权限"
      @ok="handleCreate"
      :confirm-loading="permissionStore.loading"
      width="600px"
      class="form-modal"
    >
      <a-form :model="createForm" layout="vertical">
        <a-form-item label="菜单ID" required>
          <a-input v-model:value="createForm.menuId" placeholder="请输入菜单ID">
            <template #prefix>
              <MenuOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="菜单名称">
          <a-input v-model:value="createForm.menuName" placeholder="请输入菜单名称">
            <template #prefix>
              <FontColorsOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="按钮名称">
          <a-input v-model:value="createForm.buttonName" placeholder="请输入按钮名称">
            <template #prefix>
              <ControlOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="操作类型" required>
          <a-select v-model:value="createForm.operationType" placeholder="请选择操作类型">
            <a-select-option value="QUERY">
              <SearchOutlined style="color: #1890ff; margin-right: 8px" />
              查询
            </a-select-option>
            <a-select-option value="CREATE">
              <PlusOutlined style="color: #52c41a; margin-right: 8px" />
              创建
            </a-select-option>
            <a-select-option value="UPDATE">
              <EditOutlined style="color: #faad14; margin-right: 8px" />
              更新
            </a-select-option>
            <a-select-option value="DELETE">
              <DeleteOutlined style="color: #ff4d4f; margin-right: 8px" />
              删除
            </a-select-option>
            <a-select-option value="UNLOCK">
              <UnlockOutlined style="color: #722ed1; margin-right: 8px" />
              解锁
            </a-select-option>
            <a-select-option value="LOCK">
              <LockOutlined style="color: #13c2c2; margin-right: 8px" />
              锁定
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="createForm.description" placeholder="请输入权限描述" :rows="3" />
        </a-form-item>
        <a-form-item label="状态" required>
          <a-select v-model:value="createForm.status" placeholder="请选择状态">
            <a-select-option value="ACTIVE">
              <CheckCircleOutlined style="color: #52c41a; margin-right: 8px" />
              启用
            </a-select-option>
            <a-select-option value="INACTIVE">
              <StopOutlined style="color: #ff4d4f; margin-right: 8px" />
              禁用
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:open="showEditModal"
      title="编辑权限"
      @ok="handleUpdate"
      :confirm-loading="permissionStore.loading"
      width="600px"
      class="form-modal"
    >
      <a-form :model="editForm" layout="vertical">
        <a-form-item label="菜单ID" required>
          <a-input v-model:value="editForm.menuId" placeholder="请输入菜单ID">
            <template #prefix>
              <MenuOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="菜单名称">
          <a-input v-model:value="editForm.menuName" placeholder="请输入菜单名称">
            <template #prefix>
              <FontColorsOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="按钮名称">
          <a-input v-model:value="editForm.buttonName" placeholder="请输入按钮名称">
            <template #prefix>
              <ControlOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="操作类型" required>
          <a-select v-model:value="editForm.operationType" placeholder="请选择操作类型">
            <a-select-option value="QUERY">
              <SearchOutlined style="color: #1890ff; margin-right: 8px" />
              查询
            </a-select-option>
            <a-select-option value="CREATE">
              <PlusOutlined style="color: #52c41a; margin-right: 8px" />
              创建
            </a-select-option>
            <a-select-option value="UPDATE">
              <EditOutlined style="color: #faad14; margin-right: 8px" />
              更新
            </a-select-option>
            <a-select-option value="DELETE">
              <DeleteOutlined style="color: #ff4d4f; margin-right: 8px" />
              删除
            </a-select-option>
            <a-select-option value="UNLOCK">
              <UnlockOutlined style="color: #722ed1; margin-right: 8px" />
              解锁
            </a-select-option>
            <a-select-option value="LOCK">
              <LockOutlined style="color: #13c2c2; margin-right: 8px" />
              锁定
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="editForm.description" placeholder="请输入权限描述" :rows="3" />
        </a-form-item>
        <a-form-item label="状态" required>
          <a-select v-model:value="editForm.status" placeholder="请选择状态">
            <a-select-option value="ACTIVE">
              <CheckCircleOutlined style="color: #52c41a; margin-right: 8px" />
              启用
            </a-select-option>
            <a-select-option value="INACTIVE">
              <StopOutlined style="color: #ff4d4f; margin-right: 8px" />
              禁用
            </a-select-option>
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
import { 
  PlusOutlined, 
  ReloadOutlined, 
  EditOutlined, 
  DeleteOutlined,
  SafetyOutlined,
  MenuOutlined,
  FontColorsOutlined,
  ControlOutlined,
  SearchOutlined,
  UnlockOutlined,
  LockOutlined,
  CheckCircleOutlined,
  StopOutlined
} from '@ant-design/icons-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'

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
  { title: '', key: 'icon', width: 50 },
  { title: '菜单ID', dataIndex: 'menuId', key: 'menuId', width: 120 },
  { title: '菜单名称', dataIndex: 'menuName', key: 'menuName', width: 150 },
  { title: '按钮名称', dataIndex: 'buttonName', key: 'buttonName', width: 150 },
  { title: '操作类型', key: 'operationType', width: 120 },
  { title: '描述', dataIndex: 'description', key: 'description', width: 200 },
  { title: '状态', key: 'status', width: 100 },
  { title: '创建时间', key: 'createdAt', width: 180 },
  { title: '更新时间', key: 'updatedAt', width: 180 },
  { title: '操作', key: 'actions', width: 150, fixed: 'right' as const }
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
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 20px 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.add-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
  transition: all 0.3s ease;
}

.add-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.content-wrapper {
  padding: 0 24px 24px;
}

.filter-card {
  margin-bottom: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
}

.table-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
}

.permission-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  color: white;
  font-size: 18px;
}

.status-tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 4px 12px;
}

.operation-tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 4px 12px;
}

.date-text {
  color: #6b7280;
  font-size: 14px;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.action-button:hover {
  background: #f3f4f6;
}

.form-modal :deep(.ant-modal-header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px 8px 0 0;
  padding: 16px 24px;
}

.form-modal :deep(.ant-modal-title) {
  color: white;
  font-weight: 600;
}

.form-modal :deep(.ant-modal-close) {
  color: white;
}

.form-modal :deep(.ant-modal-close:hover) {
  color: rgba(255, 255, 255, 0.8);
}

.form-modal :deep(.ant-modal-body) {
  padding: 24px;
}

.form-modal :deep(.ant-form-item-label > label) {
  font-weight: 500;
  color: #374151;
}

.form-modal :deep(.ant-input),
.form-modal :deep(.ant-select-selector),
.form-modal :deep(.ant-input-number) {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}

.form-modal :deep(.ant-input:focus),
.form-modal :deep(.ant-select-focused .ant-select-selector),
.form-modal :deep(.ant-input-number:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 0 12px 12px;
  }

  .filter-card,
  .table-card {
    border-radius: 6px;
  }

  .permission-icon {
    width: 32px;
    height: 32px;
    font-size: 16px;
  }
}

@media (max-width: 576px) {
  .page-header {
    padding: 16px;
  }

  .add-button {
    width: 100%;
    justify-content: center;
  }
}
</style>