<template>
  <div class="grade-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">年级管理</h1>
        <p class="page-description">管理年级信息和教育阶段</p>
      </div>
      <div class="header-actions">
        <a-button @click="handleExport" :loading="exporting">
          <template #icon><ExportOutlined /></template>
          导出数据
        </a-button>
        <a-button type="primary" @click="handleAdd">
          <template #icon><PlusOutlined /></template>
          新增年级
        </a-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filter-section">
        <a-card class="filter-card" :bordered="false">
          <a-form layout="inline" :model="searchForm" class="filter-form">
            <a-form-item label="年级名称">
              <a-input 
                v-model:value="searchForm.name" 
                placeholder="请输入年级名称"
                allow-clear
                style="width: 200px"
              />
            </a-form-item>
            <a-form-item label="教育阶段">
              <a-select 
                v-model:value="searchForm.educationalLevelId" 
                placeholder="请选择教育阶段"
                allow-clear
                style="width: 200px"
              >
                <a-select-option v-for="level in educationalLevels" :key="level.id" :value="level.id">
                  {{ level.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="状态">
              <a-select 
                v-model:value="searchForm.status" 
                placeholder="请选择状态"
                allow-clear
                style="width: 120px"
              >
                <a-select-option value="ACTIVE">激活</a-select-option>
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
          :data-source="grades"
          :loading="loading"
          :pagination="pagination"
          row-key="id"
          @change="handleTableChange"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'icon'">
              <div class="grade-icon">
                <BookOutlined />
              </div>
            </template>
            <template v-else-if="column.key === 'educationalLevelName'">
              <a-tag color="blue" class="level-tag">
                {{ record.educationalLevelName }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-tag :color="getStatusColor(record.status)" class="status-tag">
                {{ getStatusText(record.status) }}
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
                <a-popconfirm 
                  title="确定要删除该年级吗？"
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

    <GradeFormModal
      v-model:visible="modalVisible"
      :grade="currentGrade"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <GradeDetailDrawer
      v-model:visible="detailVisible"
      :grade="currentGrade"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { 
  PlusOutlined,
  ExportOutlined,
  SearchOutlined,
  ReloadOutlined,
  EyeOutlined,
  EditOutlined,
  DeleteOutlined,
  BookOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import type { TableProps } from 'ant-design-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'
import GradeFormModal from '@/components/GradeFormModal.vue'
import GradeDetailDrawer from '@/components/GradeDetailDrawer.vue'
import { gradeService } from '@/services/gradeService'

const grades = ref<any[]>([])
const educationalLevels = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const detailVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const currentGrade = ref<any>(null)
const exporting = ref(false)

const searchForm = reactive({
  name: '',
  educationalLevelId: undefined as number | undefined,
  status: undefined as string | undefined
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  pageSizeOptions: ['10', '20', '50', '100']
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
    title: '年级名称',
    dataIndex: 'name',
    key: 'name',
    width: 150,
    ellipsis: true,
    sorter: true
  },
  {
    title: '教育阶段',
    dataIndex: 'educationalLevelName',
    key: 'educationalLevelName',
    width: 150
  },
  {
    title: '排序',
    dataIndex: 'sortOrder',
    key: 'sortOrder',
    width: 100,
    sorter: true
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    filters: [
      { text: '激活', value: 'ACTIVE' },
      { text: '禁用', value: 'INACTIVE' }
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
    width: 180,
    fixed: 'right' as const
  }
]

onMounted(async () => {
  await Promise.all([
    fetchGrades(),
    fetchEducationalLevels()
  ])
})

async function fetchGrades() {
  loading.value = true
  try {
    const response = await gradeService.list(pagination.current, pagination.pageSize)
    grades.value = response.data.list
    pagination.total = response.data.total
  } catch (error) {
    message.error('获取年级列表失败')
  } finally {
    loading.value = false
  }
}

async function fetchEducationalLevels() {
  try {
    const response = await fetch('/api/educational-levels')
    educationalLevels.value = response.data || []
  } catch (error) {
    console.error('获取教育阶段列表失败:', error)
  }
}

function handleTableChange: TableProps['onChange'] = (pag, filters, sorter) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchGrades()
}

function handleSearch() {
  pagination.current = 1
  fetchGrades()
}

function handleReset() {
  Object.assign(searchForm, {
    name: '',
    educationalLevelId: undefined,
    status: undefined
  })
  pagination.current = 1
  fetchGrades()
}

function handleAdd() {
  formMode.value = 'create'
  currentGrade.value = null
  modalVisible.value = true
}

function handleEdit(record: any) {
  formMode.value = 'edit'
  currentGrade.value = record
  modalVisible.value = true
}

function handleView(record: any) {
  currentGrade.value = record
  detailVisible.value = true
}

async function handleDelete(record: any) {
  try {
    await gradeService.delete(record.id)
    message.success('删除成功')
    fetchGrades()
  } catch (error) {
    message.error('删除失败')
  }
}

async function handleExport() {
  exporting.value = true
  try {
    await gradeService.export(searchForm)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}

function handleFormSuccess() {
  modalVisible.value = false
  fetchGrades()
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
    'ACTIVE': '激活',
    'INACTIVE': '禁用'
  }
  return texts[status] || status
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
.grade-management {
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

.grade-icon {
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

.level-tag {
  font-weight: 500;
}

.status-tag {
  font-weight: 500;
}

.date-text {
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .grade-management {
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
  .grade-management {
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