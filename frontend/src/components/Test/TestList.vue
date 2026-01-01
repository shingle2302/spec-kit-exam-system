<template>
  <div class="test-list">
    <h2>My Assigned Tests</h2>
    
    <div class="test-filters">
      <a-space>
        <a-select
          v-model:value="statusFilter"
          placeholder="Filter by status"
          allow-clear
          style="width: 200px"
          @change="fetchTests"
        >
          <a-select-option value="ASSIGNED">Assigned</a-select-option>
          <a-select-option value="IN_PROGRESS">In Progress</a-select-option>
          <a-select-option value="SUBMITTED">Submitted</a-select-option>
          <a-select-option value="GRADED">Graded</a-select-option>
        </a-select>
        
        <a-input
          v-model:value="searchQuery"
          placeholder="Search tests..."
          style="width: 200px"
          @pressEnter="fetchTests"
          allow-clear
        />
        
        <a-button type="primary" @click="fetchTests">Search</a-button>
      </a-space>
    </div>

    <div class="tests-container">
      <a-list
        :data-source="tests"
        :loading="loading"
        item-layout="vertical"
      >
        <template # renderItem="{ item }">
          <a-list-item>
            <template # actions>
              <span v-if="item.status === 'ASSIGNED' || item.status === 'IN_PROGRESS'">
                <a-button 
                  type="primary" 
                  @click="startTest(item)"
                  :disabled="isTestExpired(item)"
                >
                  {{ item.status === 'IN_PROGRESS' ? 'Continue Test' : 'Start Test' }}
                </a-button>
              </span>
              <span v-else-if="item.status === 'SUBMITTED' || item.status === 'GRADED'">
                <a-button @click="viewResults(item)">View Results</a-button>
              </span>
              <span v-if="item.dueDate">
                <a-tag :color="getDueDateColor(item)">
                  Due: {{ formatDate(item.dueDate) }}
                </a-tag>
              </span>
            </template>
            
            <a-list-item-meta>
              <template # title>
                <a href="#" @click.prevent="viewTestDetails(item)">
                  {{ item.title }}
                </a>
                <a-tag :color="getStatusColor(item.status)" style="margin-left: 8px;">
                  {{ item.status }}
                </a-tag>
              </template>
              <template # description>
                {{ item.description }}
              </template>
            </a-list-item-meta>
            
            <div class="test-info">
              <p><strong>Assigned:</strong> {{ formatDate(item.assignedAt) }}</p>
              <p v-if="item.timeLimitMinutes">
                <strong>Time Limit:</strong> {{ item.timeLimitMinutes }} minutes
              </p>
              <p v-if="item.gradeId">
                <strong>Grade:</strong> {{ item.gradeId }}
              </p>
              <p v-if="item.subjectId">
                <strong>Subject:</strong> {{ item.subjectId }}
              </p>
            </div>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { testService } from '@/services/testService';
import { testAssignmentService } from '@/services/testAssignmentService';
import { message } from 'ant-design-vue';

interface TestAssignment {
  id: number;
  testId: number;
  studentId: number;
  assignedBy: number;
  assignedAt: string;
  dueDate?: string;
  status: string;
}

interface Test {
  id: number;
  title: string;
  description: string;
  timeLimitMinutes?: number;
  gradeId?: number;
  subjectId?: number;
  assignedAt: string;
  status: string;
}

const router = useRouter();

const tests = ref<Test[]>([]);
const loading = ref(false);
const statusFilter = ref<string | null>(null);
const searchQuery = ref('');

const fetchTests = async () => {
  loading.value = true;
  
  try {
    // Get current student ID (this should come from user context)
    const currentStudentId = 1; // Replace with actual current user ID
    
    // Get test assignments for this student
    let assignments;
    if (statusFilter.value) {
      assignments = await testAssignmentService.getTestAssignmentsForStudentByStatus(
        currentStudentId, 
        statusFilter.value
      );
    } else {
      assignments = await testAssignmentService.getTestAssignmentsForStudent(currentStudentId);
    }
    
    // For each assignment, get the test details
    const testPromises = assignments.map(async (assignment: TestAssignment) => {
      // Since we don't have an endpoint to get a test by ID, we'll create a partial test object
      // In a real implementation, we'd fetch the test details from the backend
      return {
        id: assignment.testId,
        title: `Test ${assignment.testId}`, // Placeholder - should come from test details
        description: 'Test description', // Placeholder - should come from test details
        timeLimitMinutes: undefined, // Should come from test details
        gradeId: undefined, // Should come from test details
        subjectId: undefined, // Should come from test details
        assignedAt: assignment.assignedAt,
        status: assignment.status,
        dueDate: assignment.dueDate
      };
    });
    
    tests.value = await Promise.all(testPromises);
  } catch (error) {
    message.error('Failed to load tests');
    console.error('Error fetching tests:', error);
  } finally {
    loading.value = false;
  }
};

const startTest = (test: Test) => {
  router.push(`/test-taking/${test.id}`);
};

const viewResults = (test: Test) => {
  router.push(`/test-results/${test.id}`);
};

const viewTestDetails = (test: Test) => {
  router.push(`/test-details/${test.id}`);
};

const isTestExpired = (test: Test): boolean => {
  if (!test.dueDate) return false;
  
  const dueDate = new Date(test.dueDate);
  const now = new Date();
  return now > dueDate;
};

const getDueDateColor = (test: Test): string => {
  if (!test.dueDate) return 'default';
  
  const dueDate = new Date(test.dueDate);
  const now = new Date();
  const timeDiff = dueDate.getTime() - now.getTime();
  const daysDiff = timeDiff / (1000 * 3600 * 24);
  
  if (daysDiff < 0) return 'red';
  if (daysDiff < 1) return 'orange';
  if (daysDiff < 7) return 'gold';
  return 'green';
};

const getStatusColor = (status: string): string => {
  switch (status) {
    case 'ASSIGNED': return 'blue';
    case 'IN_PROGRESS': return 'orange';
    case 'SUBMITTED': return 'gold';
    case 'GRADED': return 'green';
    default: return 'default';
  }
};

const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  return date.toLocaleString();
};

onMounted(() => {
  fetchTests();
});
</script>

<style scoped>
.test-list {
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.test-filters {
  margin-bottom: 24px;
}

.tests-container {
  margin-top: 16px;
}

.test-info {
  margin-top: 8px;
}

.test-info p {
  margin: 4px 0;
  color: #666;
}
</style>