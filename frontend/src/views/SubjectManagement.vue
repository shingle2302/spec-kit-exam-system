<template>
  <a-card title="学科管理">
    <div style="margin-bottom: 12px; display:flex; gap:8px;">
      <a-input v-model:value="searchName" placeholder="学科名称" style="width: 200px" />
      <a-button @click="loadData">查询</a-button>
      <a-button type="primary" :disabled="!canEdit" @click="openCreate">新增学科</a-button>
    </div>
    <a-table :data-source="records" :columns="columns" :pagination="false" row-key="id" />

    <a-modal v-model:open="open" :title="form.id ? '编辑学科' : '新增学科'" @ok="submit">
      <a-form :model="form" layout="vertical">
        <a-form-item label="名称"><a-input v-model:value="form.name" /></a-form-item>
        <a-form-item label="班级"><a-select v-model:value="form.classId" :options="classOptions" /></a-form-item>
        <a-form-item label="学段"><a-select v-model:value="form.educationalLevelId" :options="levelOptions" /></a-form-item>
        <a-form-item label="专业方向"><a-input v-model:value="form.specialization" /></a-form-item>
        <a-form-item label="描述"><a-input v-model:value="form.description" /></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { subjectService } from '@/services/subjectService'
import { useAuthStore } from '@/store'
import { hasPermission } from '@/utils/permissionChecker'

const authStore = useAuthStore()
const records = ref<any[]>([])
const classOptions = ref<any[]>([])
const levelOptions = ref<any[]>([])
const searchName = ref('')
const open = ref(false)
const form = ref<any>({ name: '', classId: undefined, educationalLevelId: undefined, specialization: '', description: '' })
const canEdit = computed(() => authStore.isAdmin || hasPermission('subject-management', 'CREATE') || hasPermission('subject-management', 'UPDATE'))

const columns = [
  { title: 'ID', dataIndex: 'id' },
  { title: '学科名称', dataIndex: 'name' },
  { title: '班级', dataIndex: 'className' },
  { title: '学段', dataIndex: 'educationalLevelName' },
  { title: '方向', dataIndex: 'specialization' },
  {
    title: '操作',
    customRender: ({ record }: any) => h('div', [
      h('a', { style: `margin-right:8px;${!canEdit.value ? 'pointer-events:none;opacity:.4;' : ''}`, onClick: () => edit(record) }, '编辑'),
      h('a', { style: `${!canEdit.value ? 'pointer-events:none;opacity:.4;' : 'color:#ff4d4f;'}`, onClick: () => remove(record.id) }, '删除')
    ])
  }
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
