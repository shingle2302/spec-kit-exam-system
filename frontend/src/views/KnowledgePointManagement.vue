<template>
  <div class="knowledge-point-management">
    <a-card title="知识点管理" class="card">
      <a-row :gutter="[16, 16]">
        <a-col :span="24">
          <a-button type="primary" @click="showModal('add')" class="mb-3">
            <template #icon>
              <plus-outlined />
            </template>
            新增知识点
          </a-button>
        </a-col>
        
        <a-col :span="24">
          <a-table
            :columns="columns"
            :data-source="knowledgePoints"
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
        
        <a-col :span="24">
          <a-card title="知识点树结构" class="mt-4">
            <a-tree
              v-model:expandedKeys="expandedKeys"
              :tree-data="treeData"
              :default-expand-all="true"
              @select="onSelect"
            >
              <template #title="{ title, key }">
                <span>{{ title }}</span>
              </template>
            </a-tree>
          </a-card>
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
        <a-form-item label="知识点名称" name="name">
          <a-input v-model:value="form.name" placeholder="请输入知识点名称" />
        </a-form-item>
        <a-form-item label="知识点编码" name="code">
          <a-input v-model:value="form.code" placeholder="请输入知识点编码" />
        </a-form-item>
        <a-form-item label="父知识点" name="parentId">
          <a-select v-model:value="form.parentId" placeholder="请选择父知识点">
            <a-select-option value="">无（顶级知识点）</a-select-option>
            <a-select-option v-for="item in treeSelectData" :key="item.id" :value="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="所属学科ID" name="subjectId">
          <a-input-number v-model:value="form.subjectId" placeholder="请输入学科ID" />
        </a-form-item>
        <a-form-item label="描述" name="description">
          <a-textarea v-model:value="form.description" placeholder="请输入知识点描述" />
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
import { ref, onMounted, reactive, computed } from 'vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { knowledgePointService } from '../services/knowledgePointService';

// 状态管理
const knowledgePoints = ref<any[]>([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('');
const formRef = ref();
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
  subjectId: [{ required: true, message: '请输入学科ID', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
});

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

// 获取知识点树结构
const fetchTreeData = async () => {
  try {
    const response = await knowledgePointService.getTree();
    treeData.value = response.data;
    // 构建下拉选择数据
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
      // 处理父知识点ID
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
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '知识点名称', dataIndex: 'name', key: 'name' },
  { title: '知识点编码', dataIndex: 'code', key: 'code' },
  { title: '父知识点ID', dataIndex: 'parentId', key: 'parentId' },
  { title: '所属学科ID', dataIndex: 'subjectId', key: 'subjectId' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', slots: { customRender: 'action' } }
];

// 组件挂载时获取数据
onMounted(() => {
  fetchKnowledgePoints();
  fetchTreeData();
});
</script>

<style scoped>
.knowledge-point-management {
  padding: 20px;
}

.card {
  margin-bottom: 20px;
}
</style>