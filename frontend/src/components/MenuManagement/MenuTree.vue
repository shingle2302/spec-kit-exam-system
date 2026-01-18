<template>
  <div class="menu-tree">
    <a-input-search
      v-model:value="searchValue"
      placeholder="Search menus..."
      style="margin-bottom: 8px;"
      @change="onSearch"
    />
    <a-tree
      v-if="menus.length > 0"
      :tree-data="treeData"
      :expanded-keys="expandedKeys"
      :auto-expand-parent="autoExpandParent"
      :selected-keys="selectedKeys"
      @expand="onExpand"
      @select="onSelect"
      :field-names="{ key: 'id', title: 'name' }"
    >
      <template #title="{ name, id }">
        <span v-if="!searchValue || name.toLowerCase().includes(searchValue.toLowerCase())">
          {{ name }}
        </span>
        <span
          v-else
          v-html="highlightText(name, searchValue)"
        ></span>
      </template>
      <template #switcherIcon="{ expanded }">
        <DownOutlined v-if="expanded" />
        <RightOutlined v-else />
      </template>
      <template #extra="{ id }">
        <div class="menu-actions">
          <a-button
            type="link"
            size="small"
            @click.stop="editMenu(id)"
          >
            <EditOutlined />
          </a-button>
          <a-button
            type="link"
            size="small"
            danger
            @click.stop="deleteMenu(id)"
          >
            <DeleteOutlined />
          </a-button>
        </div>
      </template>
    </a-tree>
    <div v-else class="empty-state">
      <a-empty description="No menus found" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { Tree as ATree, Input as AInput, Empty as AEmpty, Button as AButton } from 'ant-design-vue';
import { DownOutlined, RightOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { menuService } from '@/services/menuService';

// Props
const props = defineProps({
  roleId: {
    type: String,
    default: null
  }
});

// Emits
const emit = defineEmits(['menu-selected', 'menu-edited', 'menu-deleted']);

// Reactive data
const searchValue = ref('');
const expandedKeys = ref([]);
const autoExpandParent = ref(true);
const selectedKeys = ref([]);
const menus = ref([]);

// Computed tree data
const treeData = computed(() => {
  return buildTree(menus.value);
});

// Methods
const buildTree = (menuList) => {
  const map = {};
  const roots = [];

  // Create a map of all nodes
  menuList.forEach(menu => {
    map[menu.id] = { ...menu, children: [] };
  });

  // Build the tree structure
  menuList.forEach(menu => {
    const node = map[menu.id];
    if (menu.parentId) {
      const parentNode = map[menu.parentId];
      if (parentNode) {
        parentNode.children.push(node);
      } else {
        roots.push(node);
      }
    } else {
      roots.push(node);
    }
  });

  return roots;
};

const highlightText = (text, search) => {
  if (!search) return text;
  const regex = new RegExp(`(${search})`, 'gi');
  return text.replace(regex, '<span class="highlight">$1</span>');
};

const onExpand = (keys) => {
  expandedKeys.value = keys;
  autoExpandParent.value = false;
};

const onSelect = (selectedKeys, info) => {
  selectedKeys.value = selectedKeys;
  if (selectedKeys.length > 0) {
    emit('menu-selected', selectedKeys[0]);
  }
};

const onSearch = () => {
  // Auto-expand all nodes when searching
  if (searchValue.value) {
    autoExpandParent.value = true;
    expandedKeys.value = getAllKeys(treeData.value);
  } else {
    autoExpandParent.value = false;
    expandedKeys.value = [];
  }
};

const getAllKeys = (nodes) => {
  const keys = [];
  nodes.forEach(node => {
    keys.push(node.id);
    if (node.children && node.children.length > 0) {
      keys.push(...getAllKeys(node.children));
    }
  });
  return keys;
};

const editMenu = (menuId) => {
  emit('menu-edited', menuId);
};

const deleteMenu = (menuId) => {
  emit('menu-deleted', menuId);
};

const loadMenuTree = async () => {
  try {
    const response = await menuService.getMenuTree(props.roleId);
    // Check if response is paginated or direct array
    if (response && typeof response === 'object' && Array.isArray(response.data)) {
      // Handle paginated response
      menus.value = response.data || [];
    } else if (Array.isArray(response)) {
      // Handle direct array response
      menus.value = response || [];
    } else {
      menus.value = [];
    }
  } catch (error) {
    console.error('Error loading menu tree:', error);
  }
};

// Lifecycle
onMounted(async () => {
  await loadMenuTree();
});
</script>

<style scoped>
.menu-tree {
  padding: 16px;
}

.highlight {
  background-color: #ffec3d;
  font-weight: bold;
  padding: 2px 4px;
  border-radius: 2px;
}

.menu-actions {
  display: flex;
  gap: 4px;
}

.empty-state {
  padding: 24px;
  text-align: center;
}
</style>