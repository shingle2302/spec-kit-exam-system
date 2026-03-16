<template>
  <div class="class-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">班级管理</h1>
        <p class="page-description">管理班级信息和班级容量</p>
      </div>
      <div class="header-actions">
        <a-button @click="handleExport" :loading="exporting">
          <template #icon><ExportOutlined /></template>
          导出数据
        </a-button>
        <a-button type="primary" @click="handleAdd" :disabled="!canEdit">
          <template #icon><PlusOutlined /></template>
          新增班级
        </a-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filter-section">
        <a-card class="filter-card" :bordered="false">
          <a-form layout="inline" :model="searchForm" class="filter-form">
            <a-form-item label="班级名称">
              <a-input 
                v-model:value="searchForm.name" 
                placeholder="请输入班级名称"
                allow-clear
                style="width: 200px"
              />
            </a-form-item>
            <a-form-item label="年级">
              <a-select 
                v-model:value="searchForm.gradeId" 
                placeholder="请选择年级"
                allow-clear
                style="width: 150px"
              >
                <a-select-option v-for="grade in grades" :key="grade.id" :value="grade.id">
                  {{ grade.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="学段">
              <a-select 
                v-model:value="searchForm.educationalLevelId" 
                placeholder="请选择学段"
                allow-clear
                style="width: 150px"
              >
                <a-select-option v-for="level in educationalLevels" :key="level.id" :value="level.id">
                  {{ level.name }}
                </a-select-option>
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
          :data-source="records"
          :loading="loading"
          :pagination="false"
          row-key="id"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'icon'">
              <div class="class-icon">
                <TeamOutlined />
              </div>
            </template>
            <template v-else-if="column.key === 'capacity'">
              <div class="capacity-info">
                <a-progress 
                  :percent="getCapacityPercent(record)" 
                  :stroke-color="getCapacityColor(record)"
                  :show-info="false"
                  size="small"
                />
                <span class="capacity-text">{{ record.currentCount || 0 }}/{{ record.capacity }}</span>
              </div>
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
                  <a-button 
                    type="link" 
                    size="small" 
                    @click="handleEdit(record)"
                    :disabled="!canEdit"
                  >
                    <EditOutlined />
                  </a-button>
                </a-tooltip>
                <a-popconfirm 
                  title="确定要删除该班级吗？"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="handleDelete(record)"
                >
                  <a-tooltip title="删除">
                    <a-button 
                      type="link" 
                      size="small" 
                      danger
                      :disabled="!canEdit"
                    >
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

    <ClassFormModal
      v-model:visible="modalVisible"
      :class-data="currentClass"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <ClassDetailDrawer
      v-model:visible="detailVisible"
      :class-data="currentClass"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { 
  PlusOutlined,
  ExportOutlined,
  SearchOutlined,
  ReloadOutlined,
  EyeOutlined,
  EditOutlined,
  DeleteOutlined,
  TeamOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'
import ClassFormModal from '@/components/ClassFormModal.vue'
import ClassDetailDrawer from '@/components/ClassDetailDrawer.vue'
import { classService } from '@/services/classService'
import { useAuthStore } from '@/store'
import { hasPermission } from '@/utils/permissionChecker'

const authStore = useAuthStore()
const records = ref<any[]>([])
const grades = ref<any[]>([])
const educationalLevels = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const detailVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const currentClass = ref<any>(null)
const exporting = ref(false)

const canEdit = computed(() => 
  authStore.isAdmin || 
  hasPermission('class-management', 'CREATE') || 
  hasPermission('class-management', 'UPDATE')
)

const searchForm = reactive({
  name: '',
  gradeId: undefined as number | undefined,
  educationalLevelId: undefined as number | undefined
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
    title: '班级名称',
    dataIndex: 'name',
    key: 'name',
    width: 200,
    ellipsis: true,
    sorter: true
  },
  {
    title: '年级',
    dataIndex: 'gradeName',
    key: 'gradeName',
    width: 120
  },
  {
    title: '学段',
    dataIndex: 'educationalLevelName',
    key: 'educationalLevelName',
    width: 120
  },
  {
    title: '容量',
    dataIndex: 'capacity',
    key: 'capacity',
    width: 200
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description',
    width: 250,
    ellipsis: true
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
    loadData(),
    loadGrades(),
    loadEducationalLevels()
  ])
})

async function loadData() {
  loading.value = true
  try {
    const data = await classService.list({ name: searchForm.name })
    records.value = data.records || []
  } catch (error) {
    message.error('获取班级列表失败')
  } finally {
    loading.value = false
  }
}

async function loadGrades() {
  try {
    const data = await classService.grades()
    grades.value = data.map((g: any) => ({ id: g.id, name: g.name }))
  } catch (error) {
    console.error('获取年级列表失败:', error)
  }
}

async function loadEducationalLevels() {
  try {
    const response = await fetch('/api/educational-levels')
    educationalLevels.value = response.data || []
  } catch (error) {
    console.error('获取学段列表失败:', error)
  }
}

function handleSearch() {
  loadData()
}

function handleReset() {
  Object.assign(searchForm, {
    name: '',
    gradeId: undefined,
    educationalLevelId: undefined
  })
  loadData()
}

function handleAdd() {
  if (!canEdit.value) return
  formMode.value = 'create'
  currentClass.value = null
  modalVisible.value = true
}

function handleEdit(record: any) {
  if (!canEdit.value) return
  formMode.value = 'edit'
  currentClass.value = record
  modalVisible.value = true
}

function handleView(record: any) {
  currentClass.value = record
  detailVisible.value = true
}

async function handleDelete(record: any) {
  if (!canEdit.value) return
  try {
    await classService.remove(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

async function handleExport() {
  exporting.value = true
  try {
    await classService.export(searchForm)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}

function handleFormSuccess() {
  modalVisible.value = false
  loadData()
}

function getCapacityPercent(record: any) {
  if (!record.capacity || record.capacity === 0) return 0
  const percent = ((record.currentCount || 0) / record.capacity) * 100
  return Math.min(Math.round(percent), 100)
}

function getCapacityColor(record: any) {
  const percent = getCapacityPercent(record)
  if (percent >= 90) return '#ff4d4f'
  if (percent >= 70) return '#faad14'
  return '#52c41a'
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
.class-management {
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

.class-icon {
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

.capacity-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.capacity-text {
  font-size: 12px;
  color: #666;
  text-align: center;
}

.date-text {
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .class-management {
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
  .class-management {
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