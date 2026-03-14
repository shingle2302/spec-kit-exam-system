<template>
  <a-card title="考试计划管理">
    <div style="margin-bottom: 12px; display:flex; gap:8px; flex-wrap: wrap;">
      <a-input v-model:value="search.name" placeholder="考试名称" style="width: 200px" />
      <a-input v-model:value="search.academicYear" placeholder="学年，例如 2025-2026" style="width: 200px" />
      <a-select v-model:value="search.examType" :options="examTypeOptions" placeholder="考试类型" allow-clear style="width: 160px" />
      <a-button @click="loadData">查询</a-button>
      <a-button type="primary" :disabled="!canEdit" @click="openCreate">新增考试计划</a-button>
    </div>

    <a-table :data-source="records" :columns="columns" :pagination="false" row-key="id" />

    <a-modal v-model:open="open" :title="form.id ? '编辑考试计划' : '新增考试计划'" @ok="submit">
      <a-form :model="form" layout="vertical">
        <a-form-item label="考试名称"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="学年"><a-input v-model:value="form.academicYear" placeholder="2025-2026" /></a-form-item>
        <a-form-item label="学期">
          <a-select v-model:value="form.term" :options="termOptions" />
        </a-form-item>
        <a-form-item label="考试类型">
          <a-select v-model:value="form.examType" :options="examTypeOptions" />
        </a-form-item>
        <a-form-item label="开始时间"><a-date-picker v-model:value="form.startTime" show-time value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></a-form-item>
        <a-form-item label="结束时间"><a-date-picker v-model:value="form.endTime" show-time value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="form.status" :options="statusOptions" />
        </a-form-item>
        <a-form-item label="描述"><a-input v-model:value="form.description" /></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { examPlanService } from '@/services/examPlanService'
import { useAuthStore } from '@/store'
import { hasPermission } from '@/utils/permissionChecker'

const authStore = useAuthStore()
const records = ref<any[]>([])
const open = ref(false)
const search = ref({ name: '', academicYear: '', examType: undefined as string | undefined })
const form = ref<any>({ name: '', academicYear: '', term: 'SPRING', examType: 'MIDTERM', startTime: '', endTime: '', status: 'DRAFT', description: '' })

const termOptions = [
  { label: '春季学期', value: 'SPRING' },
  { label: '秋季学期', value: 'AUTUMN' }
]

const examTypeOptions = [
  { label: '期中考试', value: 'MIDTERM' },
  { label: '期末考试', value: 'FINAL' },
  { label: '月考', value: 'MONTHLY' },
  { label: '周测', value: 'WEEKLY' }
]

const statusOptions = [
  { label: '草稿', value: 'DRAFT' },
  { label: '已发布', value: 'PUBLISHED' },
  { label: '已结束', value: 'FINISHED' }
]

const canEdit = computed(() => authStore.isAdmin || hasPermission('exam-plan-management', 'CREATE') || hasPermission('exam-plan-management', 'UPDATE'))

const columns = [
  { title: 'ID', dataIndex: 'id' },
  { title: '考试名称', dataIndex: 'name' },
  { title: '学年', dataIndex: 'academicYear' },
  { title: '学期', dataIndex: 'term' },
  { title: '考试类型', dataIndex: 'examType' },
  { title: '状态', dataIndex: 'status' },
  {
    title: '操作',
    customRender: ({ record }: any) => h('div', [
      h('a', { style: `margin-right:8px;${!canEdit.value ? 'pointer-events:none;opacity:.4;' : ''}`, onClick: () => edit(record) }, '编辑'),
      h('a', { style: `${!canEdit.value ? 'pointer-events:none;opacity:.4;' : 'color:#ff4d4f;'}`, onClick: () => remove(record.id) }, '删除')
    ])
  }
]

const loadData = async () => {
  const data = await examPlanService.list(search.value)
  records.value = data.records || []
}

const openCreate = () => {
  form.value = { name: '', academicYear: '', term: 'SPRING', examType: 'MIDTERM', startTime: '', endTime: '', status: 'DRAFT', description: '' }
  open.value = true
}

const edit = (record: any) => {
  form.value = { ...record }
  open.value = true
}

const submit = async () => {
  if (!canEdit.value) return
  if (form.value.id) await examPlanService.update(form.value.id, form.value)
  else await examPlanService.create(form.value)
  message.success('操作成功')
  open.value = false
  await loadData()
}

const remove = async (id: number) => {
  if (!canEdit.value) return
  Modal.confirm({
    title: '确认删除?',
    async onOk() {
      await examPlanService.remove(id)
      message.success('已删除')
      await loadData()
    }
  })
}

onMounted(loadData)
</script>
