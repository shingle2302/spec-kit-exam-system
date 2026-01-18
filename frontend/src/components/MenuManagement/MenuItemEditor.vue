<template>
  <div class="menu-item-editor">
    <a-form
      :model="formState"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      @finish="onFinish"
      @finishFailed="onFinishFailed"
    >
      <a-form-item label="Menu Name" name="name" :rules="[{ required: true, message: 'Please input menu name!' }]">
        <a-input v-model:value="formState.name" placeholder="Enter menu name" />
      </a-form-item>
      
      <a-form-item label="Menu Code" name="code" :rules="[{ required: true, message: 'Please input menu code!' }]">
        <a-input v-model:value="formState.code" placeholder="Enter menu code (e.g., user-management)" />
      </a-form-item>
      
      <a-form-item label="Parent Menu" name="parentId">
        <a-select
          v-model:value="formState.parentId"
          placeholder="Select parent menu (optional)"
          allow-clear
        >
          <a-select-option v-for="menu in menuOptions" :key="menu.id" :value="menu.id">
            {{ menu.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item label="Route Path" name="path" :rules="[{ required: true, message: 'Please input route path!' }]">
        <a-input v-model:value="formState.path" placeholder="Enter route path" />
      </a-form-item>
      
      <a-form-item label="Component" name="component" :rules="[{ required: true, message: 'Please input component name!' }]">
        <a-input v-model:value="formState.component" placeholder="Enter component name" />
      </a-form-item>
      
      <a-form-item label="Icon" name="icon">
        <a-input v-model:value="formState.icon" placeholder="Enter icon identifier" />
      </a-form-item>
      
      <a-form-item label="Display Order" name="orderNum">
        <a-input-number 
          v-model:value="formState.orderNum" 
          :min="1" 
          :max="999" 
          placeholder="Enter display order"
          style="width: 100%;"
        />
      </a-form-item>
      
      <a-form-item label="Status" name="status" :rules="[{ required: true, message: 'Please select status!' }]">
        <a-select v-model:value="formState.status" placeholder="Select status">
          <a-select-option value="ACTIVE">Active</a-select-option>
          <a-select-option value="INACTIVE">Inactive</a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item :wrapper-col="{ offset: 6, span: 16 }">
        <a-button type="primary" html-type="submit" :loading="saving">
          {{ isEditing ? 'Update Menu' : 'Create Menu' }}
        </a-button>
        <a-button style="margin-left: 8px;" @click="onCancel">
          Cancel
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue';
import { Form as AForm, Input as AInput, Select as ASelect, Button as AButton, InputNumber as AInputNumber } from 'ant-design-vue';
import { menuService } from '@/services/menuService';

// Props
const props = defineProps({
  menuId: {
    type: String,
    default: null
  },
  isEditing: {
    type: Boolean,
    default: false
  }
});

// Emits
const emit = defineEmits(['save-success', 'cancel']);

// Reactive data
const saving = ref(false);
const menuOptions = ref([]);

// Form state
const formState = reactive({
  name: '',
  code: '',
  parentId: undefined,
  path: '',
  component: '',
  icon: '',
  orderNum: 1,
  status: 'ACTIVE'
});

// Validation rules
const rules = {
  name: [
    { required: true, message: 'Please input menu name!' },
    { min: 2, max: 100, message: 'Name must be between 2 and 100 characters', trigger: 'blur' }
  ],
  code: [
    { required: true, message: 'Please input menu code!' },
    { min: 2, max: 50, message: 'Code must be between 2 and 50 characters', trigger: 'blur' },
    { pattern: /^[a-z0-9\-]+$/, message: 'Code can only contain lowercase letters, numbers, and hyphens', trigger: 'blur' }
  ],
  path: [
    { required: true, message: 'Please input route path!' },
    { pattern: /^\/[\w\-/]*$/, message: 'Path must start with / and contain only letters, numbers, hyphens, and slashes' }
  ],
  component: [
    { required: true, message: 'Please input component name!' },
    { min: 2, max: 100, message: 'Component name must be between 2 and 100 characters', trigger: 'blur' }
  ],
  orderNum: [
    { type: 'number', min: 1, max: 999, message: 'Order must be between 1 and 999', trigger: 'blur' }
  ]
};

// Methods
const loadMenuDetails = async () => {
  if (props.menuId && props.isEditing) {
    try {
      const response = await menuService.getMenuById(props.menuId);
      Object.assign(formState, response.data);
    } catch (error) {
      console.error('Error loading menu details:', error);
    }
  }
};

const loadMenuOptions = async () => {
  try {
    const response = await menuService.getAllMenus();
    menuOptions.value = response.data || [];
  } catch (error) {
    console.error('Error loading menu options:', error);
  }
};

const onFinish = async (values) => {
  saving.value = true;
  try {
    if (props.isEditing && props.menuId) {
      // Update existing menu
      await menuService.updateMenu({ id: props.menuId, ...values });
    } else {
      // Create new menu
      await menuService.createMenu(values);
    }
    
    emit('save-success');
  } catch (error) {
    console.error('Error saving menu:', error);
  } finally {
    saving.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('Form validation failed:', errorInfo);
};

const onCancel = () => {
  emit('cancel');
  // Reset form
  Object.keys(formState).forEach(key => {
    if (key === 'orderNum') {
      formState[key] = 1;
    } else if (key === 'status') {
      formState[key] = 'ACTIVE';
    } else if (key === 'code') {
      formState[key] = ''; // Initialize code field as empty
    } else {
      formState[key] = '';
    }
  });
};

// Watch for changes in menuId to reload details when editing
watch(
  () => props.menuId,
  async (newMenuId) => {
    if (newMenuId && props.isEditing) {
      await loadMenuDetails();
    } else if (!props.isEditing) {
      // Reset form when not editing
      Object.keys(formState).forEach(key => {
        if (key === 'orderNum') {
          formState[key] = 1;
        } else if (key === 'status') {
          formState[key] = 'ACTIVE';
        } else if (key === 'code') {
          formState[key] = ''; // Initialize code field as empty
        } else {
          formState[key] = '';
        }
      });
    }
  }
);

// Lifecycle
loadMenuOptions();
</script>

<style scoped>
.menu-item-editor {
  padding: 16px;
}
</style>