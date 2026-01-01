<template>
  <div class="error-book-practice">
    <a-card title="Error Book Practice">
      <div class="practice-header">
        <h3>Practice Error Book Questions</h3>
        <p>Improve your understanding by practicing questions you previously got wrong</p>
      </div>
      
      <div v-if="currentQuestion" class="question-practice">
        <div class="question-info">
          <span class="question-count">Question {{ currentQuestionIndex + 1 }} of {{ questions.length }}</span>
          <span class="mastery-info">Mastery: {{ currentQuestion.masteryCount }}/3</span>
        </div>
        
        <a-divider />
        
        <div class="question-content">
          <h4>{{ currentQuestion.content }}</h4>
          
          <!-- Options for multiple choice questions -->
          <div v-if="currentQuestion.questionType === 'MULTIPLE_CHOICE' || 
                     currentQuestion.questionType === 'SINGLE_CHOICE' || 
                     currentQuestion.questionType === 'TRUE_FALSE'" 
               class="options">
            <a-radio-group v-model:value="selectedAnswer" @change="onAnswerSelect">
              <a-radio 
                v-for="(option, index) in currentQuestion.options" 
                :key="index" 
                :value="index"
              >
                {{ String.fromCharCode(65 + index) }}. {{ option }}
              </a-radio>
            </a-radio-group>
          </div>
          
          <!-- Text area for essay/short answer questions -->
          <div v-else-if="currentQuestion.questionType === 'ESSAY' || currentQuestion.questionType === 'SHORT_ANSWER'" class="text-answer">
            <a-textarea
              v-model:value="selectedAnswer"
              :rows="4"
              placeholder="Enter your answer here..."
              @change="onAnswerTextChange"
            />
          </div>
          
          <div class="question-actions">
            <a-button 
              type="primary" 
              @click="submitAnswer"
              :disabled="!isAnswerSelected"
            >
              Submit Answer
            </a-button>
            <a-button style="margin-left: 8px;" @click="skipQuestion">
              Skip
            </a-button>
          </div>
        </div>
      </div>
      
      <div v-else-if="questions.length === 0" class="no-questions">
        <a-empty description="No questions in your error book. Keep up the good work!" />
      </div>
      
      <div v-else class="loading">
        <a-spin size="large" />
      </div>
      
      <!-- Progress and mastery tracking -->
      <div v-if="questions.length > 0" class="progress-section">
        <a-progress 
          :percent="progressPercent" 
          status="active"
        />
        <p>{{ completedQuestions }} of {{ questions.length }} questions completed</p>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { message } from 'ant-design-vue';
import { errorBookService } from '@/services/errorBookService';

// State variables
const questions = ref([]);
const currentQuestionIndex = ref(0);
const selectedAnswer = ref(null);
const loading = ref(true);

// Computed properties
const currentQuestion = computed(() => {
  if (questions.value.length > 0 && currentQuestionIndex.value < questions.value.length) {
    return questions.value[currentQuestionIndex.value];
  }
  return null;
});

const isAnswerSelected = computed(() => {
  if (!currentQuestion.value) return false;
  
  if (currentQuestion.value.questionType === 'MULTIPLE_CHOICE' || 
      currentQuestion.value.questionType === 'SINGLE_CHOICE' || 
      currentQuestion.value.questionType === 'TRUE_FALSE') {
    return selectedAnswer.value !== null;
  } else {
    return selectedAnswer.value && selectedAnswer.value.trim() !== '';
  }
});

const completedQuestions = computed(() => {
  return questions.value.filter(q => q.completed).length;
});

const progressPercent = computed(() => {
  if (questions.value.length === 0) return 0;
  return Math.round((completedQuestions.value / questions.value.length) * 100);
});

// Initialize component
onMounted(async () => {
  await loadErrorBookQuestions();
});

// Load error book questions
const loadErrorBookQuestions = async () => {
  try {
    loading.value = true;
    questions.value = await errorBookService.getErrorBookQuestions();
    
    // Initialize question properties
    questions.value.forEach(q => {
      q.completed = false;
      q.masteryCount = q.masteryCount || 0;
    });
  } catch (error) {
    message.error('Failed to load error book questions: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Handle answer selection for multiple choice
const onAnswerSelect = (e) => {
  selectedAnswer.value = e.target.value;
};

// Handle text answer change
const onAnswerTextChange = (e) => {
  selectedAnswer.value = e.target.value;
};

// Submit answer
const submitAnswer = async () => {
  if (!currentQuestion.value || !isAnswerSelected.value) {
    message.warning('Please select or enter an answer first');
    return;
  }
  
  try {
    // Prepare submission data
    const submissionData = {
      questionId: currentQuestion.value.id,
      answer: selectedAnswer.value,
      questionType: currentQuestion.value.questionType
    };
    
    // Submit the answer and get feedback
    const result = await errorBookService.submitPracticeAnswer(submissionData);
    
    // Update the question's completion status
    questions.value[currentQuestionIndex.value].completed = true;
    
    // Show feedback message
    if (result.isCorrect) {
      message.success('Correct! Great job!');
      
      // Update mastery count
      questions.value[currentQuestionIndex.value].masteryCount = result.masteryCount;
      
      if (result.masteryAchieved) {
        message.info('Mastery achieved! You have correctly answered this question 3 times in a row.');
      }
    } else {
      message.warning('Incorrect. Keep practicing!');
    }
    
    // Move to next question or finish if last
    if (currentQuestionIndex.value < questions.value.length - 1) {
      currentQuestionIndex.value++;
      selectedAnswer.value = null;
    } else {
      message.success('You have completed all error book questions for now!');
      // Optionally reload to get updated questions
      await loadErrorBookQuestions();
    }
  } catch (error) {
    message.error('Failed to submit answer: ' + error.message);
  }
};

// Skip current question
const skipQuestion = () => {
  if (currentQuestionIndex.value < questions.value.length - 1) {
    currentQuestionIndex.value++;
    selectedAnswer.value = null;
  } else {
    message.info('You have reached the end of the practice session.');
  }
};
</script>

<style scoped>
.error-book-practice {
  padding: 20px;
}

.practice-header {
  text-align: center;
  margin-bottom: 30px;
}

.question-practice {
  max-width: 800px;
  margin: 0 auto;
}

.question-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.question-count, .mastery-info {
  font-weight: bold;
  color: #555;
}

.question-content {
  padding: 20px 0;
}

.options {
  margin: 20px 0;
}

.text-answer {
  margin: 20px 0;
}

.question-actions {
  margin-top: 20px;
  text-align: center;
}

.progress-section {
  margin-top: 30px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.no-questions {
  text-align: center;
  padding: 40px 0;
}

.loading {
  text-align: center;
  padding: 40px 0;
}
</style>