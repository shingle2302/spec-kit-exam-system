<template>
  <div>
    <a-card title="Test Taking">
      <div v-if="currentTest">
        <h3>{{ currentTest.title }}</h3>
        <p>{{ currentTest.description }}</p>
        
        <!-- Timer if time limit is set -->
        <div v-if="timeLimit > 0" class="timer">
          Time remaining: <span :class="{'time-warning': timeRemaining < 300000}">{{ formatTime(timeRemaining) }}</span>
        </div>
        
        <!-- Questions -->
        <div v-for="(question, index) in questions" :key="question.id" class="question">
          <h4>{{ index + 1 }}. {{ question.questionText }}</h4>
          
          <!-- Multiple choice/single choice -->
          <div v-if="['multiple_choice', 'single_choice', 'true_false'].includes(question.questionType)">
            <a-radio-group 
              v-if="question.questionType === 'single_choice' || question.questionType === 'true_false'"
              v-model:value="responses[question.id]"
            >
              <a-radio 
                v-for="option in question.answerOptions" 
                :key="option.id" 
                :value="option.optionIndex"
              >
                {{ option.optionText }}
              </a-radio>
            </a-radio-group>
            
            <a-checkbox-group 
              v-else
              v-model:value="responses[question.id]"
            >
              <a-row>
                <a-col 
                  v-for="option in question.answerOptions" 
                  :key="option.id" 
                  :span="24"
                >
                  <a-checkbox :value="option.optionIndex">
                    {{ option.optionText }}
                  </a-checkbox>
                </a-col>
              </a-row>
            </a-checkbox-group>
          </div>
          
          <!-- Short answer/essay -->
          <a-textarea 
            v-else-if="['short_answer', 'essay'].includes(question.questionType)"
            v-model:value="responses[question.id]"
            :rows="question.questionType === 'essay' ? 6 : 3"
            placeholder="Enter your answer here..."
          />
        </div>
        
        <div style="margin-top: 20px;">
          <a-button type="primary" @click="submitTest" :disabled="isSubmitting">
            {{ isSubmitting ? 'Submitting...' : 'Submit Test' }}
          </a-button>
        </div>
      </div>
      <div v-else>
        <p>Loading test...</p>
      </div>
    </a-card>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import testService from '@/services/testTakingService';

export default {
  name: 'TestTaking',
  setup() {
    const route = useRoute();
    const currentTest = ref(null);
    const questions = ref([]);
    const responses = ref({});
    const timeRemaining = ref(0);
    const timeLimit = ref(0);
    const isSubmitting = ref(false);
    const timerInterval = ref(null);
    
    const testId = route.params.testId;

    const formatTime = (milliseconds) => {
      const totalSeconds = Math.floor(milliseconds / 1000);
      const minutes = Math.floor(totalSeconds / 60);
      const seconds = totalSeconds % 60;
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    };

    const submitTest = async () => {
      isSubmitting.value = true;
      try {
        const response = await testService.submitTest(testId, responses.value);
        message.success('Test submitted successfully!');
        // Redirect to results or dashboard
      } catch (error) {
        message.error('Failed to submit test: ' + error.message);
      } finally {
        isSubmitting.value = false;
      }
    };

    onMounted(async () => {
      try {
        const testData = await testService.getTestForStudent(testId);
        currentTest.value = testData.test;
        questions.value = testData.questions;
        timeLimit.value = testData.timeLimitMinutes * 60 * 1000; // Convert to milliseconds
        timeRemaining.value = timeLimit.value;
        
        // Initialize responses object
        questions.value.forEach(question => {
          if (['multiple_choice', 'single_choice', 'true_false'].includes(question.questionType)) {
            responses.value[question.id] = question.questionType === 'multiple_choice' ? [] : null;
          } else {
            responses.value[question.id] = '';
          }
        });

        // Start timer if time limit exists
        if (timeLimit.value > 0) {
          timerInterval.value = setInterval(() => {
            timeRemaining.value -= 1000;
            if (timeRemaining.value <= 0) {
              timeRemaining.value = 0;
              clearInterval(timerInterval.value);
              // Auto-submit when time runs out
              submitTest();
            }
          }, 1000);
        }
      } catch (error) {
        message.error('Failed to load test: ' + error.message);
      }
    });

    onUnmounted(() => {
      if (timerInterval.value) {
        clearInterval(timerInterval.value);
      }
    });

    return {
      currentTest,
      questions,
      responses,
      timeRemaining,
      timeLimit,
      isSubmitting,
      formatTime,
      submitTest
    };
  }
};
</script>

<style scoped>
.timer {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: bold;
}

.time-warning {
  color: red;
}

.question {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}
</style>