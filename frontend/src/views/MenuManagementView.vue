<template>
  <div class="menu-management-view">
    <a-card title="Menu Management">
      <div class="toolbar">
        <a-button type="primary" @click="showCreateMenuModal">
          <template #icon>
            <PlusOutlined />
          </template>
          Add Menu
        </a-button>
        <a-button @click="refreshTree" style="margin-left: 8px;">
          <template #icon>
            <ReloadOutlined />
          </template>
          Refresh
        </a-button>
      </div>

      <div class="content">
        <div class="menu-tree-section">
          <h3>Menu Tree</h3>
          <MenuTree 
            :roleId="selectedRoleId"
            @menu-selected="onMenuSelected"
            @menu-edited="onMenuEdited"
            @menu-deleted="onMenuDeleted"
          />
        </div>
        
        <div class="menu-details-section" v-if="selectedMenuId">
          <h3>Menu Details</h3>
          <MenuItemEditor 
            :menuId="selectedMenuId"
            :isEditing="true"
            @save-success="onMenuSaved"
            @cancel="onEditCancel"
          />
        </div>
        
        <div class="menu-details-section" v-else-if="showCreateForm">
          <h3>Create New Menu</h3>
          <MenuItemEditor 
            @save-success="onMenuCreated"
            @cancel="onCreateCancel"
          />
        </div>
      </div>
    </a-card>

    <!-- Create Menu Modal -->
    <a-modal
      v-model:visible="createModalVisible"
      title="Create New Menu"
      @ok="handleCreateModalOk"
      @cancel="handleCreateModalCancel"
      :confirm-loading="createModalConfirmLoading"
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
import { Card as ACard, Button as AButton, Modal as AModal } from 'ant-design-vue';
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue';
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
  padding: 16px;
}

.toolbar {
  margin-bottom: 16px;
}

.content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.menu-tree-section, .menu-details-section {
  min-height: 400px;
}

@media (max-width: 768px) {
  .content {
    grid-template-columns: 1fr;
  }
}
</style>