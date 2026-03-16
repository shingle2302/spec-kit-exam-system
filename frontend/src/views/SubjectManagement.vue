<template>
  <div class="subject-management">
    <a-card class="main-card">
      <template #title>
        <div class="card-title">
          <BookOutlined class="title-icon" />
          <span>学科管理</span>
        </div>
      </template>
      
      <div class="filter-section">
        <a-space class="filter-space">
          <a-input 
            v-model:value="searchName" 
            placeholder="学科名称" 
            class="search-input"
          >
            <template #prefix>
              <SearchOutlined />
            </template>
          </a-input>
          <a-button @click="loadData" class="search-button">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button 
            type="primary" 
            :disabled="!canEdit" 
            @click="openCreate" 
            class="add-button"
          >
            <template #icon>
              <PlusOutlined />
            </template>
            新增学科
          </a-button>
        </a-space>
      </div>

      <div class="table-section">
        <ModernDataTable 
          :data-source="records" 
          :columns="columns" 
          :pagination="false" 
          row-key="id" 
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'icon'">
              <div class="subject-icon">
                <BookOutlined />
              </div>
            </template>
            <template v-else-if="column.key === 'name'">
              <div class="subject-name">
                {{ record.name }}
              </div>
            </template>
            <template v-else-if="column.key === 'className'">
              <a-tag color="blue" class="tag">
                {{ record.className }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'educationalLevelName'">
              <a-tag color="green" class="tag">
                {{ record.educationalLevelName }}
              </a-tag>
            </template>
            <template v-else-if="column.key === 'actions'">
              <a-space>
                <a-button 
                  type="link" 
                  size="small" 
                  @click="edit(record)"
                  class="action-button"
                  :style="{ pointerEvents: canEdit.value ? 'auto' : 'none', opacity: canEdit.value ? 1 : 0.4 }"
                >
                  <EditOutlined />
                  编辑
                </a-button>
                <a-button 
                  type="link" 
                  danger 
                  size="small"
                  @click="remove(record.id)"
                  class="action-button"
                  :style="{ pointerEvents: canEdit.value ? 'auto' : 'none', opacity: canEdit.value ? 1 : 0.4 }"
                >
                  <DeleteOutlined />
                  删除
                </a-button>
              </a-space>
            </template>
          </template>
        </ModernDataTable>
      </div>
    </a-card>

    <a-modal 
      v-model:open="open" 
      :title="form.id ? '编辑学科' : '新增学科'" 
      @ok="submit"
      class="form-modal"
      width="600px"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item label="名称">
          <a-input v-model:value="form.name" class="form-input">
            <template #prefix>
              <FontColorsOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="班级">
          <a-select v-model:value="form.classId" :options="classOptions" class="form-input" />
        </a-form-item>
        <a-form-item label="学段">
          <a-select v-model:value="form.educationalLevelId" :options="levelOptions" class="form-input" />
        </a-form-item>
        <a-form-item label="专业方向">
          <a-input v-model:value="form.specialization" class="form-input">
            <template #prefix>
              <AimOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="form.description" class="form-input" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { subjectService } from '@/services/subjectService'
import { useAuthStore } from '@/store'
import { hasPermission } from '@/utils/permissionChecker'
import { 
  BookOutlined, 
  SearchOutlined, 
  PlusOutlined, 
  EditOutlined, 
  DeleteOutlined,
  FontColorsOutlined,
  AimOutlined
} from '@ant-design/icons-vue'
import ModernDataTable from '@/components/ModernDataTable.vue'

const authStore = useAuthStore()
const records = ref<any[]>([])
const classOptions = ref<any[]>([])
const levelOptions = ref<any[]>([])
const searchName = ref('')
const open = ref(false)
const form = ref<any>({ name: '', classId: undefined, educationalLevelId: undefined, specialization: '', description: '' })
const canEdit = computed(() => authStore.isAdmin || hasPermission('subject-management', 'CREATE') || hasPermission('subject-management', 'UPDATE'))

const columns = [
  { title: '', key: 'icon', width: 50 },
  { title: '学科名称', dataIndex: 'name', key: 'name', width: 200 },
  { title: '班级', dataIndex: 'className', key: 'className', width: 150 },
  { title: '学段', dataIndex: 'educationalLevelName', key: 'educationalLevelName', width: 150 },
  { title: '方向', dataIndex: 'specialization', key: 'specialization', width: 200 },
  { title: '操作', key: 'actions', width: 150, fixed: 'right' as const }
]

const loadData = async () => {
  const data = await subjectService.list({ name: searchName.value })
  records.value = data.records || []
}

const loadRefs = async () => {
  const [classes, levels] = await Promise.all([subjectService.classes(), subjectService.levels()])
  classOptions.value = classes.map((x: any) => ({ label: x.name, value: x.id }))
  levelOptions.value = levels.map((x: any) => ({ label: x.name, value: x.id }))
}

const openCreate = () => {
  form.value = { name: '', classId: undefined, educationalLevelId: undefined, specialization: '', description: '' }
  open.value = true
}

const edit = (record: any) => {
  form.value = { ...record }
  open.value = true
}

const submit = async () => {
  if (!canEdit.value) return
  if (form.value.id) await subjectService.update(form.value.id, form.value)
  else await subjectService.create(form.value)
  message.success('操作成功')
  open.value = false
  await loadData()
}

const remove = async (id: number) => {
  if (!canEdit.value) return
  Modal.confirm({
    title: '确认删除?',
    async onOk() {
      await subjectService.remove(id)
      message.success('已删除')
      await loadData()
    }
  })
}

onMounted(async () => {
  await Promise.all([loadData(), loadRefs()])
})
</script>

<style scoped>
.subject-management {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.main-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.title-icon {
  font-size: 20px;
  color: #1890ff;
}

.filter-section {
  margin-bottom: 20px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.filter-space {
  width: 100%;
  flex-wrap: wrap;
  gap: 12px;
}

.search-input {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}

.search-input:hover {
  border-color: #667eea;
}

.search-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.search-button,
.add-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.add-button {
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
}

.add-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.table-section {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.subject-icon {
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

.subject-name {
  font-weight: 500;
  color: #1f2937;
}

.tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 4px 12px;
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

.form-input {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}

.form-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

@media (max-width: 768px) {
  .subject-management {
    padding: 12px;
  }

  .filter-section {
    padding: 12px;
  }

  .filter-space {
    flex-direction: column;
    align-items: stretch;
  }

  .search-input,
  .search-button,
  .add-button {
    width: 100%;
  }
}

@media (max-width: 576px) {
  .card-title {
    font-size: 16px;
  }

  .title-icon {
    font-size: 18px;
  }
}
</style>