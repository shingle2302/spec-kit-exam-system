<template>
  <div class="grading-dashboard">
    <a-card title="Grading Dashboard">
      <a-tabs v-model:activeKey="activeTab">
        <a-tab-pane key="pending" tab="Pending Grading">
          <div class="grading-list">
            <a-list
              :data-source="pendingSubmissions"
              :pagination="pagination"
              :loading="loading"
              item-layout="vertical"
            >
              <template #renderItem="{ item }">
                <a-list-item>
                  <template #actions>
                    <a-button type="primary" @click="startGrading(item)">
                      Grade Test
                    </a-button>
                  </template>
                  <a-list-item-meta
                    :title="item.testTitle"
                    :description="`Student: ${item.studentName}, Submitted: ${item.submittedAt}`"
                  />
                </a-list-item>
              </template>
            </a-list>
          </div>
        </a-tab-pane>
        
        <a-tab-pane key="graded" tab="Graded Tests">
          <div class="graded-list">
            <a-list
              :data-source="gradedSubmissions"
              :pagination="pagination"
              :loading="loading"
            >
              <template #renderItem="{ item }">
                <a-list-item>
                  <a-list-item-meta
                    :title="item.testTitle"
                    :description="`Student: ${item.studentName}, Grade: ${item.grade}, Graded: ${item.gradedAt}`"
                  />
                </a-list-item>
              </template>
            </a-list>
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-card>

    <!-- Grading Modal -->
    <a-modal
      v-model:visible="gradingModalVisible"
      title="Grade Test Submission"
      :footer="null"
      width="80%"
      :destroy-on-close="true"
    >
      <div v-if="currentSubmission" class="grading-content">
        <h3>Test: {{ currentSubmission.testTitle }}</h3>
        <h4>Student: {{ currentSubmission.studentName }}</h4>
        
        <div class="question-list">
          <div 
            v-for="(question, index) in currentSubmission.questions" 
            :key="question.id"
            class="question-item"
          >
            <a-divider>{{ `Question ${index + 1}` }}</a-divider>
            <div class="question-content">
              <p><strong>Question:</strong> {{ question.content }}</p>
              <p v-if="question.questionType !== 'ESSAY' && question.questionType !== 'SHORT_ANSWER'">
                <strong>Options:</strong>
                <ul>
                  <li v-for="(option, optIndex) in question.options" :key="optIndex">
                    {{ String.fromCharCode(65 + optIndex) }}. {{ option }}
                  </li>
                </ul>
              </p>
              <p><strong>Student Answer:</strong> {{ formatStudentAnswer(question) }}</p>
              
              <div class="grading-controls">
                <a-form-item label="Grade as Correct">
                  <a-switch 
                    v-model:checked="gradedAnswers[question.id].isCorrect"
                    @change="(value) => handleAnswerGradeChange(question.id, value)"
                  />
                </a-form-item>
                
                <a-form-item label="Teacher Comments">
                  <a-textarea 
                    v-model:value="gradedAnswers[question.id].comments"
                    :rows="2"
                  />
                </a-form-item>
              </div>
            </div>
          </div>
        </div>
        
        <div class="grading-actions">
          <a-button type="primary" @click="submitGrading">
            Submit Grades
          </a-button>
          <a-button style="margin-left: 8px;" @click="cancelGrading">
            Cancel
          </a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { gradingService } from '@/services/gradingService';

// State variables
const activeTab = ref('pending');
const loading = ref(false);
const gradingModalVisible = ref(false);
const currentSubmission = ref(null);
const pendingSubmissions = ref([]);
const gradedSubmissions = ref([]);
const gradedAnswers = ref({});

// Pagination
const pagination = ref({
  pageSize: 10,
  current: 1,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
});

// Initialize component
onMounted(() => {
  loadSubmissions();
});

// Load submissions
const loadSubmissions = async () => {
  loading.value = true;
  try {
    // Load pending submissions
    pendingSubmissions.value = await gradingService.getPendingSubmissions();
    
    // Load graded submissions
    gradedSubmissions.value = await gradingService.getGradedSubmissions();
  } catch (error) {
    message.error('Failed to load submissions: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Start grading a submission
const startGrading = async (submission) => {
  try {
    // Get detailed submission data with questions and student responses
    const submissionDetails = await gradingService.getSubmissionDetails(submission.id);
    currentSubmission.value = submissionDetails;
    
    // Initialize grading answers object
    gradedAnswers.value = {};
    for (const question of submissionDetails.questions) {
      gradedAnswers.value[question.id] = {
        isCorrect: question.studentResponse?.isCorrect || false,
        comments: question.studentResponse?.teacherComments || '',
        grade: question.studentResponse?.teacherGrade || 0
      };
    }
    
    gradingModalVisible.value = true;
  } catch (error) {
    message.error('Failed to load submission details: ' + error.message);
  }
};

// Format student answer based on question type
const formatStudentAnswer = (question) => {
  if (!question.studentResponse) {
    return 'No answer provided';
  }
  
  if (question.questionType === 'MULTIPLE_CHOICE' || question.questionType === 'SINGLE_CHOICE' || 
      question.questionType === 'TRUE_FALSE') {
    if (question.studentResponse.selectedOptionIndex !== undefined) {
      const optionIndex = question.studentResponse.selectedOptionIndex;
      const optionLetter = String.fromCharCode(65 + optionIndex);
      return `${optionLetter}. ${question.options[optionIndex]}`;
    }
    return 'No option selected';
  } else {
    return question.studentResponse.responseText || 'No text response';
  }
};

// Handle answer grade change
const handleAnswerGradeChange = (questionId, isCorrect) => {
  gradedAnswers.value[questionId].isCorrect = isCorrect;
};

// Submit grading
const submitGrading = async () => {
  try {
    const gradingData = {
      submissionId: currentSubmission.value.id,
      answers: Object.keys(gradedAnswers.value).map(questionId => ({
        questionId: parseInt(questionId),
        isCorrect: gradedAnswers.value[questionId].isCorrect,
        teacherGrade: gradedAnswers.value[questionId].grade,
        teacherComments: gradedAnswers.value[questionId].comments
      }))
    };
    
    await gradingService.submitGrading(gradingData);
    message.success('Grading submitted successfully!');
    
    // Close modal and refresh list
    gradingModalVisible.value = false;
    currentSubmission.value = null;
    loadSubmissions();
  } catch (error) {
    message.error('Failed to submit grading: ' + error.message);
  }
};

// Cancel grading
const cancelGrading = () => {
  gradingModalVisible.value = false;
  currentSubmission.value = null;
};
</script>

<style scoped>
.grading-dashboard {
  padding: 20px;
}

.grading-list, .graded-list {
  padding: 20px 0;
}

.question-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}

.question-content {
  padding-left: 15px;
}

.question-content ul {
  list-style-type: none;
  padding-left: 0;
}

.question-content li {
  margin-bottom: 5px;
}

.grading-controls {
  margin-top: 15px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.grading-actions {
  margin-top: 20px;
  text-align: center;
}
</style>