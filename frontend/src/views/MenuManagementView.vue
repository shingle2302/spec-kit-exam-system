<template>
  <div class="menu-management-view">
    <a-card class="main-card">
      <template #title>
        <div class="card-title">
          <MenuOutlined class="title-icon" />
          <span>菜单管理</span>
        </div>
      </template>
      
      <div class="toolbar">
        <a-space>
          <a-button type="primary" @click="showCreateMenuModal" class="action-button">
            <template #icon>
              <PlusOutlined />
            </template>
            新增菜单
          </a-button>
          <a-button @click="refreshTree" class="action-button">
            <template #icon>
              <ReloadOutlined />
            </template>
            刷新
          </a-button>
        </a-space>
      </div>

      <div class="content">
        <div class="menu-tree-section">
          <div class="section-header">
            <ApartmentOutlined class="section-icon" />
            <h3>菜单树</h3>
          </div>
          <MenuTree 
            :roleId="selectedRoleId"
            @menu-selected="onMenuSelected"
            @menu-edited="onMenuEdited"
            @menu-deleted="onMenuDeleted"
          />
        </div>
        
        <div class="menu-details-section" v-if="selectedMenuId">
          <div class="section-header">
            <EditOutlined class="section-icon" />
            <h3>菜单详情</h3>
          </div>
          <MenuItemEditor 
            :menuId="selectedMenuId"
            :isEditing="true"
            @save-success="onMenuSaved"
            @cancel="onEditCancel"
          />
        </div>
        
        <div class="menu-details-section" v-else-if="showCreateForm">
          <div class="section-header">
            <PlusCircleOutlined class="section-icon" />
            <h3>创建新菜单</h3>
          </div>
          <MenuItemEditor 
            @save-success="onMenuCreated"
            @cancel="onCreateCancel"
          />
        </div>
      </div>
    </a-card>

    <a-modal
      v-model:visible="createModalVisible"
      title="创建新菜单"
      @ok="handleCreateModalOk"
      @cancel="handleCreateModalCancel"
      :confirm-loading="createModalConfirmLoading"
      class="form-modal"
      width="600px"
    >
      <MenuItemEditor 
        @save-success="onMenuCreatedFromModal"
        @cancel="onCreateModalCancel"
      />
    </a-modal>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Card as ACard, Button as AButton, Modal as AModal, Space as ASpace } from 'ant-design-vue';
import { 
  PlusOutlined, 
  ReloadOutlined, 
  MenuOutlined, 
  ApartmentOutlined, 
  EditOutlined, 
  PlusCircleOutlined 
} from '@ant-design/icons-vue';
import MenuTree from '@/components/MenuManagement/MenuTree.vue';
import MenuItemEditor from '@/components/MenuManagement/MenuItemEditor.vue';

// Reactive data
const selectedMenuId = ref(null);
const selectedRoleId = ref(null); // Could come from props or user context
const showCreateForm = ref(false);
const createModalVisible = ref(false);
const createModalConfirmLoading = ref(false);

// Methods
const onMenuSelected = (menuId) => {
  selectedMenuId.value = menuId;
  showCreateForm.value = false;
};

const onMenuEdited = (menuId) => {
  selectedMenuId.value = menuId;
  showCreateForm.value = false;
};

const onMenuDeleted = (menuId) => {
  if (selectedMenuId.value === menuId) {
    selectedMenuId.value = null;
  }
  // Refresh the tree after deletion
  refreshTree();
};

const onMenuSaved = () => {
  selectedMenuId.value = null;
  refreshTree();
};

const onMenuCreated = () => {
  showCreateForm.value = false;
  refreshTree();
};

const onEditCancel = () => {
  selectedMenuId.value = null;
};

const onCreateCancel = () => {
  showCreateForm.value = false;
};

const showCreateMenuModal = () => {
  createModalVisible.value = true;
};

const refreshTree = () => {
  // This would ideally trigger a refresh in the MenuTree component
  // For now, we'll just log it
  console.log('Refreshing menu tree...');
};

const handleCreateModalOk = () => {
  // Handled by the MenuItemEditor component
};

const handleCreateModalCancel = () => {
  createModalVisible.value = false;
};

const onMenuCreatedFromModal = () => {
  createModalVisible.value = false;
  refreshTree();
};

const onCreateModalCancel = () => {
  createModalVisible.value = false;
};
</script>

<style scoped>
.menu-management-view {
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

.toolbar {
  margin-bottom: 20px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
  transition: all 0.3s ease;
}

.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.menu-tree-section,
.menu-details-section {
  min-height: 500px;
  background: white;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e5e7eb;
}

.section-icon {
  font-size: 18px;
  color: #10b981;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
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

@media (max-width: 768px) {
  .menu-management-view {
    padding: 12px;
  }

  .content {
    grid-template-columns: 1fr;
  }

  .menu-tree-section,
  .menu-details-section {
    min-height: 400px;
  }

  .toolbar {
    padding: 12px;
  }
}

@media (max-width: 576px) {
  .card-title {
    font-size: 16px;
  }

  .title-icon {
    font-size: 18px;
  }

  .action-button {
    width: 100%;
    justify-content: center;
  }
}
</style>