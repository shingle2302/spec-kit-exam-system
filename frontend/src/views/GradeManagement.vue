<template>
  <div class="grade-management">
    <a-card title="年级管理" class="card">
      <a-row :gutter="[16, 16]">
        <a-col :span="24">
          <a-button type="primary" @click="showModal('add')" class="mb-3">
            <template #icon>
              <plus-outlined />
            </template>
            新增年级
          </a-button>
        </a-col>
        
        <a-col :span="24">
          <a-table
            :columns="columns"
            :data-source="grades"
            :pagination="pagination"
            :loading="loading"
            row-key="id"
          >
            <template #action="{ record }">
              <a-space size="small">
                <a-button type="link" @click="showModal('edit', record)">
                  编辑
                </a-button>
                <a-button type="link" danger @click="confirmDelete(record.id)">
                  删除
                </a-button>
              </a-space>
            </template>
          </a-table>
        </a-col>
      </a-row>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form :model="form" :rules="rules" ref="formRef">
        <a-form-item label="年级名称" name="name">
          <a-input v-model:value="form.name" placeholder="请输入年级名称" />
        </a-form-item>
        <a-form-item label="教育阶段ID" name="educationalLevelId">
          <a-input-number v-model:value="form.educationalLevelId" placeholder="请输入教育阶段ID" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="form.status">
            <a-select-option value="ACTIVE">激活</a-select-option>
            <a-select-option value="INACTIVE">禁用</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { gradeService } from '../services/gradeService';

// 状态管理
const grades = ref<any[]>([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('');
const formRef = ref();
const form = reactive({
  id: undefined,
  name: '',
  educationalLevelId: undefined,
  status: 'ACTIVE'
});

// 分页
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.current = page;
    fetchGrades();
  }
});

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入年级名称', trigger: 'blur' }],
  educationalLevelId: [{ required: true, message: '请输入教育阶段ID', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

// 获取年级列表
const fetchGrades = async () => {
  loading.value = true;
  try {
    const response = await gradeService.list(pagination.current, pagination.pageSize);
    grades.value = response.data.list;
    pagination.total = response.data.total;
  } catch (error) {
    console.error('获取年级列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 显示弹窗
const showModal = (type: string, record?: any) => {
  if (type === 'add') {
    modalTitle.value = '新增年级';
    Object.assign(form, {
      id: undefined,
      name: '',
      educationalLevelId: undefined,
      status: 'ACTIVE'
    });
  } else if (type === 'edit' && record) {
    modalTitle.value = '编辑年级';
    Object.assign(form, record);
  }
  modalVisible.value = true;
};

// 处理确定
const handleOk = async () => {
  if (formRef.value) {
    await formRef.value.validate();
    try {
      if (form.id) {
        await gradeService.update(form.id, form);
      } else {
        await gradeService.create(form);
      }
      modalVisible.value = false;
      fetchGrades();
    } catch (error) {
      console.error('操作失败:', error);
    }
  }
};

// 处理取消
const handleCancel = () => {
  modalVisible.value = false;
};

// 确认删除
const confirmDelete = async (id: number) => {
  try {
    await gradeService.delete(id);
    fetchGrades();
  } catch (error) {
    console.error('删除失败:', error);
  }
};

// 表格列定义
const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '年级名称', dataIndex: 'name', key: 'name' },
  { title: '教育阶段ID', dataIndex: 'educationalLevelId', key: 'educationalLevelId' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', slots: { customRender: 'action' } }
];

// 组件挂载时获取数据
onMounted(() => {
  fetchGrades();
});
</script>

<style scoped>
.grade-management {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}
</style>