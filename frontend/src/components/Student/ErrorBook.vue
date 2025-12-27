<template>
  <div>
    <a-card title="Error Book">
      <p>Your personalized collection of questions you answered incorrectly</p>
      
      <a-table 
        :columns="columns" 
        :data-source="errorBook" 
        :loading="loading"
        row-key="id"
        :pagination="{ pageSize: 10 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'mastered'">
            <a-tag :color="record.mastered ? 'green' : 'orange'">
              {{ record.mastered ? 'Mastered' : 'Practicing' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'masteryProgress'">
            <a-progress 
              :percent="calculateMasteryProgress(record)" 
              :status="record.mastered ? 'success' : 'active'"
            />
            <div>Mastery: {{ record.masteryCount }}/3</div>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <a-button type="primary" @click="practiceQuestion(record)">Practice</a-button>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import errorBookService from '@/services/errorBookService';

export default {
  name: 'ErrorBook',
  setup() {
    const errorBook = ref([]);
    const loading = ref(false);

    const columns = [
      {
        title: 'Question',
        dataIndex: 'questionText',
        key: 'questionText',
        ellipsis: true
      },
      {
        title: 'Subject',
        dataIndex: 'subjectName',
        key: 'subjectName'
      },
      {
        title: 'Grade',
        dataIndex: 'gradeName',
        key: 'gradeName'
      },
      {
        title: 'Knowledge Point',
        dataIndex: 'knowledgePoint',
        key: 'knowledgePoint'
      },
      {
        title: 'Error Count',
        dataIndex: 'errorCount',
        key: 'errorCount'
      },
      {
        title: 'Mastery',
        dataIndex: 'mastered',
        key: 'mastered',
        slots: { customRender: 'mastered' }
      },
      {
        title: 'Mastery Progress',
        dataIndex: 'masteryProgress',
        key: 'masteryProgress',
        slots: { customRender: 'masteryProgress' }
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const calculateMasteryProgress = (record) => {
      return Math.min(100, (record.masteryCount / 3) * 100);
    };

    const practiceQuestion = (record) => {
      // Navigate to practice interface for this question
      console.log('Practicing question:', record);
    };

    const fetchErrorBook = async () => {
      loading.value = true;
      try {
        const response = await errorBookService.getErrorBookForStudent('current-student-id');
        errorBook.value = response;
      } catch (error) {
        message.error('Failed to fetch error book: ' + error.message);
      } finally {
        loading.value = false;
      }
    };

    onMounted(() => {
      fetchErrorBook();
    });

    return {
      errorBook,
      loading,
      columns,
      calculateMasteryProgress,
      practiceQuestion
    };
  }
};
</script>