<template>
  <div class="exam-paper-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">试卷管理</h1>
        <p class="page-description">管理考试试卷和试卷蓝图</p>
      </div>
      <div class="header-actions">
        <a-button @click="handleExport" :loading="exporting">
          <template #icon><ExportOutlined /></template>
          导出数据
        </a-button>
        <a-button type="primary" @click="handleAdd">
          <template #icon><PlusOutlined /></template>
          新增试卷
        </a-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filter-section">
        <a-card class="filter-card" :bordered="false">
          <a-form layout="inline" :model="searchForm" class="filter-form">
            <a-form-item label="试卷名称">
              <a-input 
                v-model:value="searchForm.name" 
                placeholder="请输入试卷名称"
                allow-clear
                style="width: 200px"
              />
            </a-form-item>
            <a-form-item label="学科">
              <a-select 
                v-model:value="searchForm.subjectId" 
                placeholder="请选择学科"
                allow-clear
                style="width: 150px"
              >
                <a-select-option v-for="subject in subjects" :key="subject.id" :value="subject.id">
                  {{ subject.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="班级">
              <a-select 
                v-model:value="searchForm.classId" 
                placeholder="请选择班级"
                allow-clear
                style="width: 150px"
              >
                <a-select-option v-for="cls in classes" :key="cls.id" :value="cls.id">
                  {{ cls.name }}
                </a-select-option>
              </a-select>
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
            <a-form-item label="状态">
              <a-select 
                v-model:value="searchForm.status" 
                placeholder="请选择状态"
                allow-clear
                style="width: 120px"
              >
                <a-select-option value="DRAFT">草稿</a-select-option>
                <a-select-option value="PUBLISHED">已发布</a-select-option>
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
          :data-source="examPapers"
          :loading="loading"
          :pagination="pagination"
          row-key="id"
          @change="handleTableChange"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'icon'">
              <div class="paper-icon">
                <FileTextOutlined />
              </div>
            </template>
            <template v-else-if="column.key === 'status'">
              <a-tag :color="getStatusColor(record.status)" class="status-tag">
                {{ getStatusText(record.status) }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'questionCount'">
              <span class="count-text">{{ record.questionCount || 0 }}题</span>
            </template>
            <template v-else-if="column.key === 'totalScore'">
              <span class="score-text">{{ record.totalScore || 0 }}分</span>
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
                <a-tooltip title="配置试题">
                  <a-button type="link" size="small" @click="handleConfigure(record)">
                    <SettingOutlined />
                  </a-button>
                </a-tooltip>
                <a-tooltip title="预览">
                  <a-button type="link" size="small" @click="handlePreview(record)">
                    <FileSearchOutlined />
                  </a-button>
                </a-tooltip>
                <a-popconfirm 
                  title="确定要删除该试卷吗？"
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

    <ExamPaperFormModal
      v-model:visible="modalVisible"
      :exam-paper="currentExamPaper"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <ExamPaperDetailDrawer
      v-model:visible="detailVisible"
      :exam-paper="currentExamPaper"
    />

    <QuestionConfigModal
      v-model:visible="configVisible"
      :exam-paper="currentExamPaper"
      @success="handleConfigSuccess"
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
  SettingOutlined,
  FileSearchOutlined,
  DeleteOutlined,
  FileTextOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import type { TableProps } from 'ant-design-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'
import ExamPaperFormModal from '@/components/ExamPaperFormModal.vue'
import ExamPaperDetailDrawer from '@/components/ExamPaperDetailDrawer.vue'
import QuestionConfigModal from '@/components/QuestionConfigModal.vue'
import { examPaperService } from '@/services/examPaperService'

const examPapers = ref<any[]>([])
const subjects = ref<any[]>([])
const classes = ref<any[]>([])
const grades = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const detailVisible = ref(false)
const configVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const currentExamPaper = ref<any>(null)
const exporting = ref(false)

const searchForm = reactive({
  name: '',
  subjectId: undefined as number | undefined,
  classId: undefined as number | undefined,
  gradeId: undefined as number | undefined,
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
    title: '试卷名称',
    dataIndex: 'name',
    key: 'name',
    width: 200,
    ellipsis: true,
    sorter: true
  },
  {
    title: '学科',
    dataIndex: 'subjectName',
    key: 'subjectName',
    width: 120
  },
  {
    title: '班级',
    dataIndex: 'className',
    key: 'className',
    width: 120
  },
  {
    title: '年级',
    dataIndex: 'gradeName',
    key: 'gradeName',
    width: 100
  },
  {
    title: '试题数量',
    dataIndex: 'questionCount',
    key: 'questionCount',
    width: 120,
    sorter: true
  },
  {
    title: '总分',
    dataIndex: 'totalScore',
    key: 'totalScore',
    width: 100,
    sorter: true
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    filters: [
      { text: '草稿', value: 'DRAFT' },
      { text: '已发布', value: 'PUBLISHED' }
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
    width: 250,
    fixed: 'right' as const
  }
]

onMounted(async () => {
  await Promise.all([
    fetchExamPapers(),
    fetchSubjects(),
    fetchClasses(),
    fetchGrades()
  ])
})

async function fetchExamPapers() {
  loading.value = true
  try {
    const response = await examPaperService.list(pagination.current, pagination.pageSize)
    examPapers.value = response.data.list
    pagination.total = response.data.total
  } catch (error) {
    message.error('获取试卷列表失败')
  } finally {
    loading.value = false
  }
}

async function fetchSubjects() {
  try {
    const response = await fetch('/api/subjects')
    subjects.value = response.data || []
  } catch (error) {
    console.error('获取学科列表失败:', error)
  }
}

async function fetchClasses() {
  try {
    const response = await fetch('/api/classes')
    classes.value = response.data || []
  } catch (error) {
    console.error('获取班级列表失败:', error)
  }
}

async function fetchGrades() {
  try {
    const response = await fetch('/api/grades')
    grades.value = response.data || []
  } catch (error) {
    console.error('获取年级列表失败:', error)
  }
}

function handleTableChange: TableProps['onChange'] = (pag, filters, sorter) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchExamPapers()
}

function handleSearch() {
  pagination.current = 1
  fetchExamPapers()
}

function handleReset() {
  Object.assign(searchForm, {
    name: '',
    subjectId: undefined,
    classId: undefined,
    gradeId: undefined,
    status: undefined
  })
  pagination.current = 1
  fetchExamPapers()
}

function handleAdd() {
  formMode.value = 'create'
  currentExamPaper.value = null
  modalVisible.value = true
}

function handleEdit(record: any) {
  formMode.value = 'edit'
  currentExamPaper.value = record
  modalVisible.value = true
}

function handleView(record: any) {
  currentExamPaper.value = record
  detailVisible.value = true
}

function handleConfigure(record: any) {
  currentExamPaper.value = record
  configVisible.value = true
}

function handlePreview(record: any) {
  message.info('预览功能开发中')
}

async function handleDelete(record: any) {
  try {
    await examPaperService.delete(record.id)
    message.success('删除成功')
    fetchExamPapers()
  } catch (error) {
    message.error('删除失败')
  }
}

async function handleExport() {
  exporting.value = true
  try {
    await examPaperService.export(searchForm)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}

function handleFormSuccess() {
  modalVisible.value = false
  fetchExamPapers()
}

function handleConfigSuccess() {
  configVisible.value = false
  fetchExamPapers()
}

function getStatusColor(status: string) {
  const colors: Record<string, string> = {
    'DRAFT': 'default',
    'PUBLISHED': 'success'
  }
  return colors[status] || 'default'
}

function getStatusText(status: string) {
  const texts: Record<string, string> = {
    'DRAFT': '草稿',
    'PUBLISHED': '已发布'
  }
  return texts[status] || status
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN')
}
</script>

<style scoped>
.exam-paper-management {
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

.paper-icon {
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

.count-text {
  font-weight: 600;
  color: #667eea;
}

.score-text {
  font-weight: 600;
  color: #52c41a;
}

.date-text {
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .exam-paper-management {
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
  .exam-paper-management {
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