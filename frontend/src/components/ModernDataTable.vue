<template>
  <div class="modern-data-table">
    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      :pagination="pagination"
      :scroll="{ x: 'max-content' }"
      :row-selection="rowSelection"
      @change="handleTableChange"
      :row-key="rowKey"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.status)">
            {{ getStatusText(record.status) }}
          </a-tag>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="handleEdit(record)">
              编辑
            </a-button>
            <a-button type="link" size="small" danger @click="handleDelete(record)">
              删除
            </a-button>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { message } from 'ant-design-vue';

interface ColumnType {
  title: string;
  dataIndex: string;
  key: string;
  width?: number;
  align?: 'left' | 'center' | 'right';
}

interface PaginationType {
  current: number;
  pageSize: number;
  total: number;
  showSizeChanger: boolean;
  showQuickJumper: boolean;
  showTotal: (total: number) => string;
}

const props = defineProps<{
  columns: ColumnType[];
  dataSource: any[];
  loading: boolean;
  rowKey?: string;
}>();

const emit = defineEmits(['edit', 'delete', 'change']);

const pagination = ref<PaginationType>({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 条`,
});

const rowSelection = computed(() => ({
  type: 'checkbox' as const,
  selectedRowKeys: [] as any[],
  onChange: (selectedRowKeys: any[]) => {
    console.log('Selected:', selectedRowKeys);
  },
}));

const handleTableChange = (pag: any, filters: any, sorter: any) => {
  pagination.value = pag;
  emit('change', { pagination: pag, filters, sorter });
};

const handleEdit = (record: any) => {
  emit('edit', record);
};

const handleDelete = (record: any) => {
  emit('delete', record);
};

const getStatusColor = (status: string) => {
  const colorMap: Record<string, string> = {
    'ACTIVE': 'green',
    'INACTIVE': 'red',
    'PENDING': 'orange',
    'LOCKED': 'red',
  };
  return colorMap[status] || 'default';
};

const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    'ACTIVE': '活跃',
    'INACTIVE': '停用',
    'PENDING': '待定',
    'LOCKED': '锁定',
  };
  return textMap[status] || status;
};
</script>

<style scoped>
.modern-data-table {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .modern-data-table {
    padding: 8px;
    font-size: 14px;
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .modern-data-table {
    padding: 12px;
    font-size: 15px;
  }
}

@media (min-width: 1025px) {
  .modern-data-table {
    padding: 16px;
    font-size: 16px;
  }
}
</style>