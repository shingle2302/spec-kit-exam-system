<template>
  <div class="knowledge-point-management">
    <a-card class="card">
      <template #title>
        <div class="card-title">
          <PartitionOutlined class="title-icon" />
          <span>知识点管理</span>
        </div>
      </template>
      
      <div class="content-wrapper">
        <div class="filter-section">
          <a-row :gutter="16">
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
              <a-input
                v-model:value="filters.name"
                placeholder="搜索知识点名称"
                allow-clear
                @pressEnter="handleSearch"
              >
                <template #prefix>
                  <SearchOutlined />
                </template>
              </a-input>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
              <a-select
                v-model:value="filters.status"
                placeholder="选择状态"
                allow-clear
                style="width: 100%"
              >
                <a-select-option value="ACTIVE">激活</a-select-option>
                <a-select-option value="INACTIVE">禁用</a-select-option>
              </a-select>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
              <a-select
                v-model:value="filters.subjectId"
                placeholder="选择学科"
                allow-clear
                style="width: 100%"
              >
                <a-select-option v-for="subject in subjects" :key="subject.id" :value="subject.id">
                  {{ subject.name }}
                </a-select-option>
              </a-select>
            </a-col>
            <a-col :xs="24" :sm="12" :md="8" :lg="6">
              <a-space>
                <a-button type="primary" @click="handleSearch">
                  <template #icon>
                    <SearchOutlined />
                  </template>
                  搜索
                </a-button>
                <a-button @click="handleReset">
                  <template #icon>
                    <ReloadOutlined />
                  </template>
                  重置
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </div>

        <div class="action-section">
          <a-button type="primary" @click="showModal('add')" class="add-button">
            <template #icon>
              <PlusOutlined />
            </template>
            新增知识点
          </a-button>
        </div>

        <div class="table-section">
          <ModernDataTable
            :columns="columns"
            :data-source="knowledgePoints"
            :loading="loading"
            :pagination="pagination"
            row-key="id"
            @change="handleTableChange"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'icon'">
                <div class="kp-icon">
                  <PartitionOutlined />
                </div>
              </template>
              <template v-else-if="column.key === 'name'">
                <div class="kp-name">
                  <span class="kp-text">{{ record.name }}</span>
                  <a-tag v-if="record.parentId" color="blue" size="small" class="parent-tag">
                    子知识点
                  </a-tag>
                </div>
              </template>
              <template v-else-if="column.key === 'code'">
                <a-tag color="cyan" class="code-tag">
                  {{ record.code }}
                </a-tag>
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
                <a-space size="small">
                  <a-button type="link" @click="showModal('edit', record)" class="action-button">
                    <EditOutlined />
                    编辑
                  </a-button>
                  <a-button type="link" danger @click="confirmDelete(record.id)" class="action-button">
                    <DeleteOutlined />
                    删除
                  </a-button>
                </a-space>
              </template>
            </template>
          </ModernDataTable>
        </div>

        <div class="tree-section">
          <a-card title="知识点树结构" class="tree-card">
            <template #title>
              <div class="tree-title">
                <ApartmentOutlined class="tree-icon" />
                <span>知识点树结构</span>
              </div>
            </template>
            <a-tree
              v-model:expandedKeys="expandedKeys"
              :tree-data="treeData"
              :default-expand-all="true"
              @select="onSelect"
              class="knowledge-tree"
            >
              <template #title="{ title, key }">
                <span class="tree-node">{{ title }}</span>
              </template>
            </a-tree>
          </a-card>
        </div>
      </div>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      @ok="handleOk"
      @cancel="handleCancel"
      width="600px"
      class="form-modal"
    >
      <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="知识点名称" name="name">
          <a-input v-model:value="form.name" placeholder="请输入知识点名称">
            <template #prefix>
              <FontColorsOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="知识点编码" name="code">
          <a-input v-model:value="form.code" placeholder="请输入知识点编码">
            <template #prefix>
              <CodeOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="父知识点" name="parentId">
          <a-select v-model:value="form.parentId" placeholder="请选择父知识点" allow-clear>
            <a-select-option value="">无（顶级知识点）</a-select-option>
            <a-select-option v-for="item in treeSelectData" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="所属学科" name="subjectId">
          <a-select v-model:value="form.subjectId" placeholder="请选择学科" allow-clear>
            <a-select-option v-for="subject in subjects" :key="subject.id" :value="subject.id">
              {{ subject.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="描述" name="description">
          <a-textarea v-model:value="form.description" placeholder="请输入知识点描述" :rows="4" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="form.status">
            <a-select-option value="ACTIVE">
              <CheckCircleOutlined style="color: #52c41a; margin-right: 8px" />
              激活
            </a-select-option>
            <a-select-option value="INACTIVE">
              <StopOutlined style="color: #ff4d4f; margin-right: 8px" />
              禁用
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue';
import { 
  PlusOutlined, 
  SearchOutlined, 
  ReloadOutlined, 
  EditOutlined, 
  DeleteOutlined,
  PartitionOutlined,
  ApartmentOutlined,
  FontColorsOutlined,
  CodeOutlined,
  CheckCircleOutlined,
  StopOutlined
} from '@ant-design/icons-vue';
import { knowledgePointService } from '../services/knowledgePointService';
import ModernDataTable from '../components/ModernDataTable.vue';

// 状态管理
const knowledgePoints = ref<any[]>([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('');
const formRef = ref();
const subjects = ref<any[]>([]);

// 过滤器
const filters = reactive({
  name: '',
  status: undefined,
  subjectId: undefined
});

// 表单
const form = reactive({
  id: undefined,
  name: '',
  code: '',
  parentId: '',
  subjectId: undefined,
  description: '',
  status: 'ACTIVE'
});

// 树形结构
const treeData = ref<any[]>([]);
const expandedKeys = ref<string[]>([]);
const treeSelectData = ref<any[]>([]);

// 分页
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  onChange: (page: number) => {
    pagination.current = page;
    fetchKnowledgePoints();
  }
});

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入知识点名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入知识点编码', trigger: 'blur' }],
  subjectId: [{ required: true, message: '请选择学科', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

// 获取状态颜色
const getStatusColor = (status: string) => {
  return status === 'ACTIVE' ? 'success' : 'error';
};

// 获取状态文本
const getStatusText = (status: string) => {
  return status === 'ACTIVE' ? '激活' : '禁用';
};

// 格式化日期
const formatDate = (date: string) => {
  if (!date) return '-';
  const d = new Date(date);
  return d.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchKnowledgePoints();
};

// 重置
const handleReset = () => {
  filters.name = '';
  filters.status = undefined;
  filters.subjectId = undefined;
  pagination.current = 1;
  fetchKnowledgePoints();
};

// 表格变化
const handleTableChange = (pag: any) => {
  pagination.current = pag.current;
  pagination.pageSize = pag.pageSize;
  fetchKnowledgePoints();
};

// 获取知识点列表
const fetchKnowledgePoints = async () => {
  loading.value = true;
  try {
    const response = await knowledgePointService.list(pagination.current, pagination.pageSize);
    knowledgePoints.value = response.data.list;
    pagination.total = response.data.total;
  } catch (error) {
    console.error('获取知识点列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 获取学科列表
const fetchSubjects = async () => {
  try {
    const response = await knowledgePointService.getSubjects();
    subjects.value = response.data;
  } catch (error) {
    console.error('获取学科列表失败:', error);
  }
};

// 获取知识点树结构
const fetchTreeData = async () => {
  try {
    const response = await knowledgePointService.getTree();
    treeData.value = response.data;
    buildTreeSelectData(response.data);
  } catch (error) {
    console.error('获取知识点树结构失败:', error);
  }
};

// 构建树形选择数据
const buildTreeSelectData = (data: any[], prefix: string = '') => {
  treeSelectData.value = [];
  const build = (items: any[], currentPrefix: string) => {
    items.forEach(item => {
      treeSelectData.value.push({
        id: item.id,
        name: currentPrefix + item.name
      });
      if (item.children && item.children.length > 0) {
        build(item.children, currentPrefix + item.name + ' > ');
      }
    });
  };
  build(data, prefix);
};

// 显示弹窗
const showModal = (type: string, record?: any) => {
  if (type === 'add') {
    modalTitle.value = '新增知识点';
    Object.assign(form, {
      id: undefined,
      name: '',
      code: '',
      parentId: '',
      subjectId: undefined,
      description: '',
      status: 'ACTIVE'
    });
  } else if (type === 'edit' && record) {
    modalTitle.value = '编辑知识点';
    Object.assign(form, record);
  }
  modalVisible.value = true;
};

// 处理确定
const handleOk = async () => {
  if (formRef.value) {
    await formRef.value.validate();
    try {
      const submitForm = { ...form };
      if (submitForm.parentId === '') {
        submitForm.parentId = null;
      }
      if (submitForm.id) {
        await knowledgePointService.update(submitForm.id, submitForm);
      } else {
        await knowledgePointService.create(submitForm);
      }
      modalVisible.value = false;
      fetchKnowledgePoints();
      fetchTreeData();
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
    await knowledgePointService.delete(id);
    fetchKnowledgePoints();
    fetchTreeData();
  } catch (error) {
    console.error('删除失败:', error);
  }
};

// 树形选择
const onSelect = (selectedKeys: string[], info: any) => {
  console.log('Selected keys:', selectedKeys);
  console.log('Selected info:', info);
};

// 表格列定义
const columns = [
  { title: '', key: 'icon', width: 50 },
  { title: '知识点名称', dataIndex: 'name', key: 'name', width: 200 },
  { title: '知识点编码', dataIndex: 'code', key: 'code', width: 150 },
  { title: '父知识点ID', dataIndex: 'parentId', key: 'parentId', width: 120 },
  { title: '所属学科', dataIndex: 'subjectId', key: 'subjectId', width: 120 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 100 },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt', width: 180 },
  { title: '操作', key: 'action', width: 150, fixed: 'right' as const }
];

// 组件挂载时获取数据
onMounted(() => {
  fetchKnowledgePoints();
  fetchTreeData();
  fetchSubjects();
});
</script>

<style scoped>
.knowledge-point-management {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.card {
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

.content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.filter-section {
  padding: 16px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.action-section {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
}

.add-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
  transition: all 0.3s ease;
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

.kp-icon {
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

.kp-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.kp-text {
  font-weight: 500;
  color: #1f2937;
}

.parent-tag {
  font-size: 12px;
  border-radius: 4px;
}

.code-tag {
  font-family: 'Courier New', monospace;
  font-weight: 500;
  border-radius: 4px;
}

.status-tag {
  font-weight: 500;
  border-radius: 4px;
  padding: 4px 12px;
}

.date-text {
  color: #6b7280;
  font-size: 14px;
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

.tree-section {
  margin-top: 24px;
}

.tree-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.tree-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.tree-icon {
  font-size: 18px;
  color: #10b981;
}

.knowledge-tree {
  padding: 16px;
  background: #f9fafb;
  border-radius: 6px;
  border: 1px solid #e5e7eb;
}

.tree-node {
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
  cursor: pointer;
}

.tree-node:hover {
  background: #e5e7eb;
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

.form-modal :deep(.ant-form-item-label > label) {
  font-weight: 500;
  color: #374151;
}

.form-modal :deep(.ant-input),
.form-modal :deep(.ant-select-selector),
.form-modal :deep(.ant-input-number) {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}

.form-modal :deep(.ant-input:focus),
.form-modal :deep(.ant-select-focused .ant-select-selector),
.form-modal :deep(.ant-input-number:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

@media (max-width: 768px) {
  .knowledge-point-management {
    padding: 12px;
  }

  .filter-section {
    padding: 12px;
  }

  .action-section {
    margin-bottom: 12px;
  }

  .kp-icon {
    width: 32px;
    height: 32px;
    font-size: 16px;
  }

  .tree-section {
    margin-top: 16px;
  }
}

@media (max-width: 576px) {
  .card-title {
    font-size: 16px;
  }

  .title-icon {
    font-size: 18px;
  }

  .add-button {
    width: 100%;
    justify-content: center;
  }
}
</style>