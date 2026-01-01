<template>
  <div class="student-response-grading">
    <a-card title="Student Response Grading">
      <div class="response-header">
        <h3>Test: {{ testTitle }}</h3>
        <h4>Student: {{ studentName }}</h4>
        <p>Submitted: {{ submittedAt }}</p>
      </div>
      
      <div class="questions-list">
        <div 
          v-for="(question, index) in questions" 
          :key="question.id"
          class="question-item"
        >
          <a-divider orientation="left">
            Question {{ index + 1 }}: {{ question.questionType }}
          </a-divider>
          
          <div class="question-content">
            <p><strong>Question:</strong> {{ question.content }}</p>
            
            <!-- Options for multiple choice questions -->
            <div v-if="question.questionType === 'MULTIPLE_CHOICE' || question.questionType === 'SINGLE_CHOICE' || question.questionType === 'TRUE_FALSE'" class="options">
              <div 
                v-for="(option, optIndex) in question.options" 
                :key="optIndex"
                :class="['option', { 
                  'selected': studentAnswers[question.id] === optIndex,
                  'correct': question.correctAnswer === optIndex,
                  'incorrect': studentAnswers[question.id] === optIndex && question.correctAnswer !== optIndex
                }]"
              >
                <span class="option-letter">{{ String.fromCharCode(65 + optIndex) }}.</span>
                <span class="option-text">{{ option }}</span>
                <span 
                  v-if="studentAnswers[question.id] === optIndex" 
                  class="student-selection"
                >
                  (Student's selection)
                </span>
                <span 
                  v-if="question.correctAnswer === optIndex" 
                  class="correct-answer"
                >
                  (Correct answer)
                </span>
              </div>
            </div>
            
            <!-- Text answer for essay/short answer questions -->
            <div v-else-if="question.questionType === 'ESSAY' || question.questionType === 'SHORT_ANSWER'" class="text-answer">
              <p><strong>Student's Answer:</strong></p>
              <div class="student-text-response">
                {{ studentAnswers[question.id] || 'No answer provided' }}
              </div>
            </div>
            
            <!-- Grading controls -->
            <div class="grading-controls">
              <div class="grade-section">
                <a-form-item label="Is Correct?">
                  <a-switch 
                    v-model:checked="gradingResults[question.id].isCorrect"
                    @change="(value) => updateGradingResult(question.id, 'isCorrect', value)"
                  />
                  <span class="grade-label">{{ gradingResults[question.id].isCorrect ? 'Correct' : 'Incorrect' }}</span>
                </a-form-item>
              </div>
              
              <div class="grade-section">
                <a-form-item label="Teacher Grade (0-100)">
                  <a-input-number 
                    v-model:value="gradingResults[question.id].teacherGrade"
                    :min="0"
                    :max="100"
                    :step="1"
                    @change="(value) => updateGradingResult(question.id, 'teacherGrade', value)"
                  />
                </a-form-item>
              </div>
              
              <div class="comment-section">
                <a-form-item label="Teacher Comments">
                  <a-textarea 
                    v-model:value="gradingResults[question.id].teacherComments"
                    :rows="3"
                    placeholder="Enter feedback for the student..."
                    @change="(e) => updateGradingResult(question.id, 'teacherComments', e.target.value)"
                  />
                </a-form-item>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="grading-actions">
        <a-button type="primary" @click="submitGrading" :loading="submitting">
          Submit Grading
        </a-button>
        <a-button style="margin-left: 8px;" @click="cancelGrading">
          Cancel
        </a-button>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { gradingService } from '@/services/gradingService';

// Props
const props = defineProps({
  submissionId: {
    type: Number,
    required: true
  }
});

// State variables
const testTitle = ref('');
const studentName = ref('');
const submittedAt = ref('');
const questions = ref([]);
const studentAnswers = ref({});
const submitting = ref(false);

// Grading results
const gradingResults = reactive({});

// Initialize component
onMounted(async () => {
  await loadSubmissionDetails();
});

// Load submission details
const loadSubmissionDetails = async () => {
  try {
    const response = await gradingService.getSubmissionDetails(props.submissionId);
    
    testTitle.value = response.testTitle;
    studentName.value = response.studentName;
    submittedAt.value = response.submittedAt;
    questions.value = response.questions || [];
    
    // Initialize student answers and grading results
    questions.value.forEach(question => {
      // Get student's answer based on question type
      if (question.questionType === 'MULTIPLE_CHOICE' || 
          question.questionType === 'SINGLE_CHOICE' || 
          question.questionType === 'TRUE_FALSE') {
        studentAnswers.value[question.id] = question.studentResponse?.selectedOptionIndex;
      } else {
        studentAnswers.value[question.id] = question.studentResponse?.responseText;
      }
      
      // Initialize grading result for this question
      gradingResults[question.id] = {
        isCorrect: question.studentResponse?.isCorrect || false,
        teacherGrade: question.studentResponse?.teacherGrade || 0,
        teacherComments: question.studentResponse?.teacherComments || ''
      };
    });
  } catch (error) {
    message.error('Failed to load submission details: ' + error.message);
  }
};

// Update grading result
const updateGradingResult = (questionId, field, value) => {
  if (gradingResults[questionId]) {
    gradingResults[questionId][field] = value;
  }
};

// Submit grading
const submitGrading = async () => {
  submitting.value = true;
  try {
    const gradingData = {
      submissionId: props.submissionId,
      answers: questions.value.map(question => ({
        questionId: question.id,
        isCorrect: gradingResults[question.id].isCorrect,
        teacherGrade: gradingResults[question.id].teacherGrade,
        teacherComments: gradingResults[question.id].teacherComments
      }))
    };
    
    await gradingService.submitManualGrading(gradingData);
    message.success('Grading submitted successfully!');
    
    // Optionally emit an event to notify parent component
    emit('gradingSubmitted', props.submissionId);
  } catch (error) {
    message.error('Failed to submit grading: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Cancel grading
const cancelGrading = () => {
  // Optionally emit an event to notify parent component
  emit('gradingCanceled');
};

// Define emits
const emit = defineEmits(['gradingSubmitted', 'gradingCanceled']);
</script>

<style scoped>
.student-response-grading {
  padding: 20px;
}

.response-header {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.question-item {
  margin-bottom: 30px;
  padding: 15px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}

.question-content {
  padding-left: 15px;
}

.options {
  margin: 15px 0;
}

.option {
  padding: 8px;
  margin: 5px 0;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  display: flex;
  align-items: center;
}

.option.selected {
  border-color: #1890ff;
  background-color: #e6f7ff;
}

.option.correct {
  border-color: #52c41a;
  background-color: #f6ffed;
}

.option.incorrect {
  border-color: #ff4d4f;
  background-color: #fff2f0;
}

.option-letter {
  font-weight: bold;
  margin-right: 10px;
  min-width: 30px;
}

.student-selection {
  color: #1890ff;
  font-size: 0.9em;
  margin-left: auto;
}

.correct-answer {
  color: #52c41a;
  font-size: 0.9em;
  margin-left: 10px;
}

.text-answer {
  margin: 15px 0;
}

.student-text-response {
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
  border-left: 3px solid #1890ff;
}

.grading-controls {
  margin-top: 20px;
  padding: 15px;
  background-color: #fafafa;
  border-radius: 4px;
}

.grade-section {
  margin-bottom: 15px;
}

.grade-label {
  margin-left: 10px;
  font-weight: bold;
}

.comment-section {
  margin-top: 15px;
}

.grading-actions {
  margin-top: 30px;
  text-align: center;
}
</style>