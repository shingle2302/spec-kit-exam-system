<template>
  <div class="user-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">用户管理</h1>
        <p class="page-description">管理系统用户账户和权限</p>
      </div>
      <div class="header-actions">
        <a-button @click="handleExport" :loading="exporting">
          <template #icon><ExportOutlined /></template>
          导出数据
        </a-button>
        <a-button type="primary" @click="handleAdd">
          <template #icon><PlusOutlined /></template>
          新增用户
        </a-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filter-section">
        <a-card class="filter-card" :bordered="false">
          <a-form layout="inline" :model="filters" class="filter-form">
            <a-form-item label="用户名">
              <a-input 
                v-model:value="filters.username" 
                placeholder="请输入用户名"
                allow-clear
                style="width: 200px"
              />
            </a-form-item>
            <a-form-item label="邮箱">
              <a-input 
                v-model:value="filters.email" 
                placeholder="请输入邮箱"
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
                <a-select-option value="ACTIVE">活跃</a-select-option>
                <a-select-option value="INACTIVE">未激活</a-select-option>
                <a-select-option value="SUSPENDED">暂停</a-select-option>
                <a-select-option value="LOCKED">锁定</a-select-option>
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
          :data-source="userStore.users"
          :loading="userStore.loading"
          :pagination="userStore.pagination"
          row-key="id"
          @change="handleTableChange"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'avatar'">
              <a-avatar :size="40" class="user-avatar">
                <template #icon><UserOutlined /></template>
              </a-avatar>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-tag :color="getStatusColor(record.status)" class="status-tag">
                {{ getStatusText(record.status) }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'role'">
              <a-tag v-if="record.role === 'ADMIN'" color="gold" class="role-tag">
                <CrownOutlined />
                管理员
              </a-tag>
              <a-tag v-else color="blue" class="role-tag">
                普通用户
              </a-tag>
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
                <a-tooltip title="重置密码">
                  <a-button type="link" size="small" @click="handleResetPassword(record)">
                    <KeyOutlined />
                  </a-button>
                </a-tooltip>
                <a-popconfirm 
                  title="确定要删除该用户吗？"
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

    <UserFormModal
      v-model:visible="formVisible"
      :user="currentUser"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <UserDetailDrawer
      v-model:visible="detailVisible"
      :user="currentUser"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/store'
import { 
  PlusOutlined,
  ExportOutlined,
  SearchOutlined,
  ReloadOutlined,
  UserOutlined,
  EyeOutlined,
  EditOutlined,
  KeyOutlined,
  DeleteOutlined,
  CrownOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import type { TableProps } from 'ant-design-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'
import UserFormModal from '@/components/UserFormModal.vue'
import UserDetailDrawer from '@/components/UserDetailDrawer.vue'
import type { User } from '@/types'

const userStore = useUserStore()

const formVisible = ref(false)
const detailVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const currentUser = ref<User | null>(null)
const exporting = ref(false)

const filters = reactive({
  username: '',
  email: '',
  status: undefined as string | undefined
})

const columns = [
  {
    title: '头像',
    dataIndex: 'avatar',
    key: 'avatar',
    width: 80,
    align: 'center' as const
  },
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
    width: 150,
    sorter: true
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    width: 200,
    sorter: true
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    key: 'phone',
    width: 150
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    filters: [
      { text: '活跃', value: 'ACTIVE' },
      { text: '未激活', value: 'INACTIVE' },
      { text: '暂停', value: 'SUSPENDED' },
      { text: '锁定', value: 'LOCKED' }
    ]
  },
  {
    title: '角色',
    dataIndex: 'role',
    key: 'role',
    width: 100,
    filters: [
      { text: '管理员', value: 'ADMIN' },
      { text: '普通用户', value: 'USER' }
    ]
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
  await userStore.fetchUsers(filters)
}

function handleTableChange: TableProps['onChange'] = (pagination, filters, sorter) => {
  userStore.pagination.current = pagination.current
  userStore.pagination.pageSize = pagination.pageSize
  loadData()
}

function handleSearch() {
  userStore.pagination.current = 1
  loadData()
}

function handleReset() {
  filters.username = ''
  filters.email = ''
  filters.status = undefined
  userStore.pagination.current = 1
  loadData()
}

function handleAdd() {
  formMode.value = 'create'
  currentUser.value = null
  formVisible.value = true
}

function handleEdit(record: User) {
  formMode.value = 'edit'
  currentUser.value = record
  formVisible.value = true
}

function handleView(record: User) {
  currentUser.value = record
  detailVisible.value = true
}

async function handleResetPassword(record: User) {
  try {
    await userStore.resetPassword(record.id)
    message.success('密码重置成功，新密码已发送到用户邮箱')
  } catch (error) {
    message.error('密码重置失败')
  }
}

async function handleDelete(record: User) {
  try {
    await userStore.deleteUser(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

async function handleExport() {
  exporting.value = true
  try {
    await userStore.exportUsers(filters)
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

function getStatusColor(status: string) {
  const colors: Record<string, string> = {
    'ACTIVE': 'success',
    'INACTIVE': 'default',
    'SUSPENDED': 'warning',
    'LOCKED': 'error'
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
</script>

<style scoped>
.user-management {
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

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.status-tag {
  font-weight: 500;
}

.role-tag {
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.date-text {
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .user-management {
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
  .user-management {
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