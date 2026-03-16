<template>
  <div class="question-management">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">试题管理</h1>
        <p class="page-description">管理考试题库和试题内容</p>
      </div>
      <div class="header-actions">
        <a-button @click="handleExport" :loading="exporting">
          <template #icon><ExportOutlined /></template>
          导出数据
        </a-button>
        <a-button type="primary" @click="handleAdd">
          <template #icon><PlusOutlined /></template>
          新增试题
        </a-button>
      </div>
    </div>

    <div class="page-content">
      <div class="filter-section">
        <a-card class="filter-card" :bordered="false">
          <a-form layout="inline" :model="searchForm" class="filter-form">
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
            <a-form-item label="知识点">
              <a-select 
                v-model:value="searchForm.knowledgePointId" 
                placeholder="请选择知识点"
                allow-clear
                style="width: 200px"
              >
                <a-select-option v-for="kp in knowledgePoints" :key="kp.id" :value="kp.id">
                  {{ kp.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="试题类型">
              <a-select 
                v-model:value="searchForm.type" 
                placeholder="请选择类型"
                allow-clear
                style="width: 150px"
              >
                <a-select-option value="选择题">选择题</a-select-option>
                <a-select-option value="填空题">填空题</a-select-option>
                <a-select-option value="简答题">简答题</a-select-option>
                <a-select-option value="判断题">判断题</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="难度">
              <a-select 
                v-model:value="searchForm.difficulty" 
                placeholder="请选择难度"
                allow-clear
                style="width: 120px"
              >
                <a-select-option value="EASY">简单</a-select-option>
                <a-select-option value="MEDIUM">中等</a-select-option>
                <a-select-option value="HARD">困难</a-select-option>
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
          :data-source="questions"
          :loading="loading"
          :pagination="pagination"
          row-key="id"
          @change="handleTableChange"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'type'">
              <a-tag :color="getTypeColor(record.type)" class="type-tag">
                <component :is="getTypeIcon(record.type)" class="type-icon" />
                {{ record.type }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'difficulty'">
              <a-tag :color="getDifficultyColor(record.difficulty)" class="difficulty-tag">
                {{ getDifficultyText(record.difficulty) }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'score'">
              <span class="score-text">{{ record.score }}分</span>
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
                <a-tooltip title="复制试题">
                  <a-button type="link" size="small" @click="handleCopy(record)">
                    <CopyOutlined />
                  </a-button>
                </a-tooltip>
                <a-popconfirm 
                  title="确定要删除该试题吗？"
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

    <QuestionFormModal
      v-model:visible="modalVisible"
      :question="currentQuestion"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <QuestionDetailDrawer
      v-model:visible="detailVisible"
      :question="currentQuestion"
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
  CopyOutlined,
  DeleteOutlined,
  CheckCircleOutlined,
  FileTextOutlined,
  QuestionCircleOutlined,
  CloseCircleOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import type { TableProps } from 'ant-design-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'
import QuestionFormModal from '@/components/QuestionFormModal.vue'
import QuestionDetailDrawer from '@/components/QuestionDetailDrawer.vue'
import { questionService } from '@/services/questionService'

const questions = ref<any[]>([])
const subjects = ref<any[]>([])
const grades = ref<any[]>([])
const knowledgePoints = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const detailVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const currentQuestion = ref<any>(null)
const exporting = ref(false)

const searchForm = reactive({
  subjectId: undefined as number | undefined,
  gradeId: undefined as number | undefined,
  knowledgePointId: undefined as number | undefined,
  type: undefined as string | undefined,
  difficulty: undefined as string | undefined
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
    title: '试题内容',
    dataIndex: 'content',
    key: 'content',
    width: 300,
    ellipsis: true
  },
  {
    title: '类型',
    dataIndex: 'type',
    key: 'type',
    width: 120,
    filters: [
      { text: '选择题', value: '选择题' },
      { text: '填空题', value: '填空题' },
      { text: '简答题', value: '简答题' },
      { text: '判断题', value: '判断题' }
    ]
  },
  {
    title: '难度',
    dataIndex: 'difficulty',
    key: 'difficulty',
    width: 100,
    filters: [
      { text: '简单', value: 'EASY' },
      { text: '中等', value: 'MEDIUM' },
      { text: '困难', value: 'HARD' }
    ]
  },
  {
    title: '分值',
    dataIndex: 'score',
    key: 'score',
    width: 100,
    sorter: true
  },
  {
    title: '学科',
    dataIndex: 'subjectName',
    key: 'subjectName',
    width: 120
  },
  {
    title: '年级',
    dataIndex: 'gradeName',
    key: 'gradeName',
    width: 100
  },
  {
    title: '知识点',
    dataIndex: 'knowledgePointName',
    key: 'knowledgePointName',
    width: 150,
    ellipsis: true
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
    width: 200,
    fixed: 'right' as const
  }
]

onMounted(async () => {
  await Promise.all([
    fetchQuestions(),
    fetchSubjects(),
    fetchGrades(),
    fetchKnowledgePoints()
  ])
})

async function fetchQuestions() {
  loading.value = true
  try {
    const response = await questionService.list(pagination.current, pagination.pageSize)
    questions.value = response.data.list
    pagination.total = response.data.total
  } catch (error) {
    message.error('获取试题列表失败')
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

async function fetchGrades() {
  try {
    const response = await fetch('/api/grades')
    grades.value = response.data || []
  } catch (error) {
    console.error('获取年级列表失败:', error)
  }
}

async function fetchKnowledgePoints() {
  try {
    const response = await fetch('/api/knowledge-points')
    knowledgePoints.value = response.data || []
  } catch (error) {
    console.error('获取知识点列表失败:', error)
  }
}

function handleTableChange: TableProps['onChange'] = (pag, filters, sorter) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  fetchQuestions()
}

function handleSearch() {
  pagination.current = 1
  fetchQuestions()
}

function handleReset() {
  Object.assign(searchForm, {
    subjectId: undefined,
    gradeId: undefined,
    knowledgePointId: undefined,
    type: undefined,
    difficulty: undefined
  })
  pagination.current = 1
  fetchQuestions()
}

function handleAdd() {
  formMode.value = 'create'
  currentQuestion.value = null
  modalVisible.value = true
}

function handleEdit(record: any) {
  formMode.value = 'edit'
  currentQuestion.value = record
  modalVisible.value = true
}

function handleView(record: any) {
  currentQuestion.value = record
  detailVisible.value = true
}

function handleCopy(record: any) {
  formMode.value = 'create'
  currentQuestion.value = { ...record, id: undefined }
  modalVisible.value = true
  message.success('试题已复制，请编辑后保存')
}

async function handleDelete(record: any) {
  try {
    await questionService.delete(record.id)
    message.success('删除成功')
    fetchQuestions()
  } catch (error) {
    message.error('删除失败')
  }
}

async function handleExport() {
  exporting.value = true
  try {
    await questionService.export(searchForm)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
  } finally {
    exporting.value = false
  }
}

function handleFormSuccess() {
  modalVisible.value = false
  fetchQuestions()
}

function getTypeColor(type: string) {
  const colors: Record<string, string> = {
    '选择题': 'blue',
    '填空题': 'green',
    '简答题': 'orange',
    '判断题': 'purple'
  }
  return colors[type] || 'default'
}

function getTypeIcon(type: string) {
  const icons: Record<string, any> = {
    '选择题': CheckCircleOutlined,
    '填空题': FileTextOutlined,
    '简答题': QuestionCircleOutlined,
    '判断题': CloseCircleOutlined
  }
  return icons[type] || QuestionCircleOutlined
}

function getDifficultyColor(difficulty: string) {
  const colors: Record<string, string> = {
    'EASY': 'success',
    'MEDIUM': 'warning',
    'HARD': 'error'
  }
  return colors[difficulty] || 'default'
}

function getDifficultyText(difficulty: string) {
  const texts: Record<string, string> = {
    'EASY': '简单',
    'MEDIUM': '中等',
    'HARD': '困难'
  }
  return texts[difficulty] || difficulty
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
.question-management {
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

.type-tag {
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.type-icon {
  font-size: 14px;
}

.difficulty-tag {
  font-weight: 500;
}

.score-text {
  font-weight: 600;
  color: #667eea;
}

.status-tag {
  font-weight: 500;
}

.date-text {
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .question-management {
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
  .question-management {
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