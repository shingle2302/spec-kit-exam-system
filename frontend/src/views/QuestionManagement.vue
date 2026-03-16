<template>
  <div class="question-management">
    <a-card title="试题管理" class="card">
      <a-row :gutter="[16, 16]">
        <a-col :span="24">
          <a-button type="primary" @click="showModal('add')" class="mb-3">
            <template #icon>
              <plus-outlined />
            </template>
            新增试题
          </a-button>
        </a-col>
        
        <!-- 筛选条件 -->
        <a-col :span="24">
          <a-form :model="searchForm" layout="inline">
            <a-form-item label="学科ID">
              <a-input-number v-model:value="searchForm.subjectId" placeholder="请输入学科ID" />
            </a-form-item>
            <a-form-item label="年级ID">
              <a-input-number v-model:value="searchForm.gradeId" placeholder="请输入年级ID" />
            </a-form-item>
            <a-form-item label="知识点ID">
              <a-input-number v-model:value="searchForm.knowledgePointId" placeholder="请输入知识点ID" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="searchQuestions">
                搜索
              </a-button>
              <a-button @click="resetSearch">
                重置
              </a-button>
            </a-form-item>
          </a-form>
        </a-col>
        
        <a-col :span="24">
          <a-table
            :columns="columns"
            :data-source="questions"
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
      :width="800"
    >
      <a-form :model="form" :rules="rules" ref="formRef">
        <a-form-item label="试题内容" name="content">
          <a-textarea v-model:value="form.content" placeholder="请输入试题内容" :rows="4" />
        </a-form-item>
        <a-form-item label="试题类型" name="type">
          <a-select v-model:value="form.type">
            <a-select-option value="选择题">选择题</a-select-option>
            <a-select-option value="填空题">填空题</a-select-option>
            <a-select-option value="简答题">简答题</a-select-option>
            <a-select-option value="判断题">判断题</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="难度级别" name="difficulty">
          <a-select v-model:value="form.difficulty">
            <a-select-option value="EASY">简单</a-select-option>
            <a-select-option value="MEDIUM">中等</a-select-option>
            <a-select-option value="HARD">困难</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="分值" name="score">
          <a-input-number v-model:value="form.score" placeholder="请输入分值" />
        </a-form-item>
        <a-form-item label="答案" name="answer">
          <a-textarea v-model:value="form.answer" placeholder="请输入答案" :rows="2" />
        </a-form-item>
        <a-form-item label="解析" name="analysis">
          <a-textarea v-model:value="form.analysis" placeholder="请输入解析" :rows="3" />
        </a-form-item>
        <a-form-item label="学科ID" name="subjectId">
          <a-input-number v-model:value="form.subjectId" placeholder="请输入学科ID" />
        </a-form-item>
        <a-form-item label="年级ID" name="gradeId">
          <a-input-number v-model:value="form.gradeId" placeholder="请输入年级ID" />
        </a-form-item>
        <a-form-item label="知识点ID" name="knowledgePointId">
          <a-input-number v-model:value="form.knowledgePointId" placeholder="请输入知识点ID" />
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
import { questionService } from '../services/questionService';

// 状态管理
const questions = ref<any[]>([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('');
const formRef = ref();
const form = reactive({
  id: undefined,
  content: '',
  type: '',
  difficulty: '',
  score: undefined,
  answer: '',
  analysis: '',
  subjectId: undefined,
  gradeId: undefined,
  knowledgePointId: undefined,
  status: 'ACTIVE'
});

// 搜索表单
const searchForm = reactive({
  subjectId: undefined,
  gradeId: undefined,
  knowledgePointId: undefined
});

// 分页
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.current = page;
    fetchQuestions();
  }
});

// 表单验证规则
const rules = reactive({
  content: [{ required: true, message: '请输入试题内容', trigger: 'blur' }],
  type: [{ required: true, message: '请选择试题类型', trigger: 'change' }],
  difficulty: [{ required: true, message: '请选择难度级别', trigger: 'change' }],
  score: [{ required: true, message: '请输入分值', trigger: 'blur' }],
  answer: [{ required: true, message: '请输入答案', trigger: 'blur' }],
  subjectId: [{ required: true, message: '请输入学科ID', trigger: 'blur' }],
  gradeId: [{ required: true, message: '请输入年级ID', trigger: 'blur' }],
  knowledgePointId: [{ required: true, message: '请输入知识点ID', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

// 获取试题列表
const fetchQuestions = async () => {
  loading.value = true;
  try {
    const response = await questionService.list(pagination.current, pagination.pageSize);
    questions.value = response.data.list;
    pagination.total = response.data.total;
  } catch (error) {
    console.error('获取试题列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索试题
const searchQuestions = async () => {
  loading.value = true;
  try {
    const response = await questionService.listByCondition(
      searchForm.subjectId,
      searchForm.gradeId,
      searchForm.knowledgePointId
    );
    questions.value = response.data;
    pagination.total = response.data.length;
  } catch (error) {
    console.error('搜索试题失败:', error);
  } finally {
    loading.value = false;
  }
};

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    subjectId: undefined,
    gradeId: undefined,
    knowledgePointId: undefined
  });
  fetchQuestions();
};

// 显示弹窗
const showModal = (type: string, record?: any) => {
  if (type === 'add') {
    modalTitle.value = '新增试题';
    Object.assign(form, {
      id: undefined,
      content: '',
      type: '',
      difficulty: '',
      score: undefined,
      answer: '',
      analysis: '',
      subjectId: undefined,
      gradeId: undefined,
      knowledgePointId: undefined,
      status: 'ACTIVE'
    });
  } else if (type === 'edit' && record) {
    modalTitle.value = '编辑试题';
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
        await questionService.update(form.id, form);
      } else {
        await questionService.create(form);
      }
      modalVisible.value = false;
      fetchQuestions();
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
    await questionService.delete(id);
    fetchQuestions();
  } catch (error) {
    console.error('删除失败:', error);
  }
};

// 表格列定义
const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '试题内容', dataIndex: 'content', key: 'content', ellipsis: true },
  { title: '类型', dataIndex: 'type', key: 'type' },
  { title: '难度', dataIndex: 'difficulty', key: 'difficulty' },
  { title: '分值', dataIndex: 'score', key: 'score' },
  { title: '学科ID', dataIndex: 'subjectId', key: 'subjectId' },
  { title: '年级ID', dataIndex: 'gradeId', key: 'gradeId' },
  { title: '知识点ID', dataIndex: 'knowledgePointId', key: 'knowledgePointId' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', slots: { customRender: 'action' } }
];

// 组件挂载时获取数据
onMounted(() => {
  fetchQuestions();
});
</script>

<style scoped>
.question-management {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}
</style>