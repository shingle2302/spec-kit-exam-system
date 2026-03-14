<template>
  <a-card title="班级管理">
    <div style="margin-bottom: 12px; display:flex; gap:8px;">
      <a-input v-model:value="searchName" placeholder="班级名称" style="width: 200px" />
      <a-button @click="loadData">查询</a-button>
      <a-button type="primary" :disabled="!canEdit" @click="openCreate">新增班级</a-button>
    </div>
    <a-table :data-source="records" :columns="columns" :pagination="false" row-key="id" />

    <a-modal v-model:open="open" :title="form.id ? '编辑班级' : '新增班级'" @ok="submit">
      <a-form :model="form" layout="vertical">
        <a-form-item label="名称"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="年级"><a-select v-model:value="form.gradeId" :options="gradeOptions" /></a-form-item>
        <a-form-item label="容量"><a-input-number v-model:value="form.capacity" :min="1" style="width:100%" /></a-form-item>
        <a-form-item label="描述"><a-input v-model:value="form.description" /></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { classService } from '@/services/classService'
import { useAuthStore } from '@/store'
import { hasPermission } from '@/utils/permissionChecker'

const authStore = useAuthStore()
const records = ref<any[]>([])
const gradeOptions = ref<any[]>([])
const searchName = ref('')
const open = ref(false)
const form = ref<any>({ name: '', gradeId: undefined, capacity: 30, description: '' })
const canEdit = computed(() => authStore.isAdmin || hasPermission('class-management', 'CREATE') || hasPermission('class-management', 'UPDATE'))

const columns = [
  { title: 'ID', dataIndex: 'id' },
  { title: '班级名称', dataIndex: 'name' },
  { title: '年级', dataIndex: 'gradeName' },
  { title: '学段', dataIndex: 'educationalLevelName' },
  { title: '容量', dataIndex: 'capacity' },
  {
    title: '操作',
    customRender: ({ record }: any) => h('div', [
      h('a', { style: `margin-right:8px;${!canEdit.value ? 'pointer-events:none;opacity:.4;' : ''}`, onClick: () => edit(record) }, '编辑'),
      h('a', { style: `${!canEdit.value ? 'pointer-events:none;opacity:.4;' : 'color:#ff4d4f;'}`, onClick: () => remove(record.id) }, '删除')
    ])
  }
]

const loadData = async () => {
  const data = await classService.list({ name: searchName.value })
  records.value = data.records || []
}

const loadGrades = async () => {
  const data = await classService.grades()
  gradeOptions.value = data.map((g: any) => ({ label: g.name, value: g.id }))
}

const openCreate = () => {
  form.value = { name: '', gradeId: undefined, capacity: 30, description: '' }
  open.value = true
}

const edit = (record: any) => {
  form.value = { ...record }
  open.value = true
}

const submit = async () => {
  if (!canEdit.value) return
  if (form.value.id) await classService.update(form.value.id, form.value)
  else await classService.create(form.value)
  message.success('操作成功')
  open.value = false
  await loadData()
}

const remove = async (id: number) => {
  if (!canEdit.value) return
  Modal.confirm({
    title: '确认删除?',
    async onOk() {
      await classService.remove(id)
      message.success('已删除')
      await loadData()
    }
  })
}

onMounted(async () => {
  await Promise.all([loadData(), loadGrades()])
})
</script>
