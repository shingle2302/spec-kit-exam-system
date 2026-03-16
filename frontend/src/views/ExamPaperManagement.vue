<template>
  <div class="exam-paper-management">
    <a-card title="试卷管理" class="card">
      <a-row :gutter="[16, 16]">
        <a-col :span="24">
          <a-button type="primary" @click="showModal('add')" class="mb-3">
            <template #icon>
              <plus-outlined />
            </template>
            新增试卷
          </a-button>
        </a-col>
        
        <a-col :span="24">
          <a-table
            :columns="columns"
            :data-source="examPapers"
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
        <a-form-item label="试卷名称" name="name">
          <a-input v-model:value="form.name" placeholder="请输入试卷名称" />
        </a-form-item>
        <a-form-item label="学科ID" name="subjectId">
          <a-input-number v-model:value="form.subjectId" placeholder="请输入学科ID" />
        </a-form-item>
        <a-form-item label="班级ID" name="classId">
          <a-input-number v-model:value="form.classId" placeholder="请输入班级ID" />
        </a-form-item>
        <a-form-item label="年级ID" name="gradeId">
          <a-input-number v-model:value="form.gradeId" placeholder="请输入年级ID" />
        </a-form-item>
        <a-form-item label="蓝图" name="blueprint">
          <a-textarea v-model:value="form.blueprint" placeholder="请输入试卷蓝图（JSON格式）" :rows="4" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="form.status">
            <a-select-option value="DRAFT">草稿</a-select-option>
            <a-select-option value="PUBLISHED">已发布</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { examPaperService } from '../services/examPaperService';

// 状态管理
const examPapers = ref<any[]>([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('');
const formRef = ref();
const form = reactive({
  id: undefined,
  name: '',
  subjectId: undefined,
  classId: undefined,
  gradeId: undefined,
  blueprint: '{}',
  status: 'DRAFT'
});

// 分页
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.current = page;
    fetchExamPapers();
  }
});

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入试卷名称', trigger: 'blur' }],
  subjectId: [{ required: true, message: '请输入学科ID', trigger: 'blur' }],
  classId: [{ required: true, message: '请输入班级ID', trigger: 'blur' }],
  gradeId: [{ required: true, message: '请输入年级ID', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

// 获取试卷列表
const fetchExamPapers = async () => {
  loading.value = true;
  try {
    const response = await examPaperService.list(pagination.current, pagination.pageSize);
    examPapers.value = response.data.list;
    pagination.total = response.data.total;
  } catch (error) {
    console.error('获取试卷列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 显示弹窗
const showModal = (type: string, record?: any) => {
  if (type === 'add') {
    modalTitle.value = '新增试卷';
    Object.assign(form, {
      id: undefined,
      name: '',
      subjectId: undefined,
      classId: undefined,
      gradeId: undefined,
      blueprint: '{}',
      status: 'DRAFT'
    });
  } else if (type === 'edit' && record) {
    modalTitle.value = '编辑试卷';
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
        await examPaperService.update(form.id, form);
      } else {
        await examPaperService.create(form);
      }
      modalVisible.value = false;
      fetchExamPapers();
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
    await examPaperService.delete(id);
    fetchExamPapers();
  } catch (error) {
    console.error('删除失败:', error);
  }
};

// 表格列定义
const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '试卷名称', dataIndex: 'name', key: 'name' },
  { title: '学科ID', dataIndex: 'subjectId', key: 'subjectId' },
  { title: '班级ID', dataIndex: 'classId', key: 'classId' },
  { title: '年级ID', dataIndex: 'gradeId', key: 'gradeId' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', slots: { customRender: 'action' } }
];

// 组件挂载时获取数据
onMounted(() => {
  fetchExamPapers();
});
</script>

<style scoped>
.exam-paper-management {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}
</style>