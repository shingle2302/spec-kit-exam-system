<template>
  <div>
    <a-form
      :form="form"
      @finish="handleSubmit"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 14 }"
    >
      <a-form-item
        label="Question Text"
        name="questionText"
        :rules="[{ required: true, message: 'Please input the question text!' }]"
      >
        <a-textarea 
          v-model:value="formState.questionText" 
          placeholder="Enter the question text" 
          :rows="4" 
        />
      </a-form-item>

      <a-form-item
        label="Question Type"
        name="questionType"
        :rules="[{ required: true, message: 'Please select the question type!' }]"
      >
        <a-select 
          v-model:value="formState.questionType" 
          placeholder="Select question type"
          @change="handleQuestionTypeChange"
        >
          <a-select-option value="multiple_choice">Multiple Choice</a-select-option>
          <a-select-option value="single_choice">Single Choice</a-select-option>
          <a-select-option value="true_false">True/False</a-select-option>
          <a-select-option value="short_answer">Short Answer</a-select-option>
          <a-select-option value="essay">Essay</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item
        label="Subject"
        name="subjectId"
        :rules="[{ required: true, message: 'Please select a subject!' }]"
      >
        <a-select 
          v-model:value="formState.subjectId" 
          placeholder="Select subject"
        >
          <a-select-option v-for="subject in subjects" :key="subject.id" :value="subject.id">
            {{ subject.name }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item
        label="Grade Level"
        name="gradeId"
        :rules="[{ required: true, message: 'Please select a grade level!' }]"
      >
        <a-select 
          v-model:value="formState.gradeId" 
          placeholder="Select grade level"
        >
          <a-select-option v-for="grade in grades" :key="grade.id" :value="grade.id">
            {{ grade.name }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item
        label="Knowledge Point"
        name="knowledgePoint"
        :rules="[{ required: true, message: 'Please input the knowledge point!' }]"
      >
        <a-input 
          v-model:value="formState.knowledgePoint" 
          placeholder="Enter the knowledge point" 
        />
      </a-form-item>

      <a-form-item
        label="Standard Explanation"
        name="standardExplanation"
        :rules="[{ required: true, message: 'Please input the standard explanation!' }]"
      >
        <a-textarea 
          v-model:value="formState.standardExplanation" 
          placeholder="Enter the standard explanation" 
          :rows="4" 
        />
      </a-form-item>

      <!-- Answer Options for Multiple Choice and Single Choice -->
      <div v-if="showAnswerOptions">
        <a-form-item label="Answer Options">
          <div v-for="(option, index) in formState.answerOptions" :key="index">
            <a-row :gutter="8">
              <a-col :span="18">
                <a-input 
                  v-model:value="option.optionText" 
                  :placeholder="`Option ${index + 1}`" 
                />
              </a-col>
              <a-col :span="4">
                <a-checkbox v-model:checked="option.isCorrect">Correct</a-checkbox>
              </a-col>
              <a-col :span="2">
                <a-button 
                  type="link" 
                  @click="removeAnswerOption(index)"
                  danger
                >
                  Remove
                </a-button>
              </a-col>
            </a-row>
          </div>
          <a-button @click="addAnswerOption" style="margin-top: 10px;">
            Add Option
          </a-button>
        </a-form-item>
      </div>

      <a-form-item :wrapper-col="{ offset: 4, span: 14 }">
        <a-button type="primary" html-type="submit" :loading="submitting">
          Submit Question
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import { reactive, ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import questionService from '@/services/questionService';

export default {
  name: 'QuestionForm',
  setup() {
    const formState = reactive({
      questionText: '',
      questionType: '',
      subjectId: '',
      gradeId: '',
      knowledgePoint: '',
      standardExplanation: '',
      answerOptions: []
    });

    const subjects = ref([]);
    const grades = ref([]);
    const showAnswerOptions = ref(false);
    const submitting = ref(false);

    const form = ref();

    const addAnswerOption = () => {
      formState.answerOptions.push({
        optionText: '',
        isCorrect: false,
        optionIndex: formState.answerOptions.length
      });
    };

    const removeAnswerOption = (index) => {
      formState.answerOptions.splice(index, 1);
    };

    const handleQuestionTypeChange = (value) => {
      showAnswerOptions.value = ['multiple_choice', 'single_choice', 'true_false'].includes(value);
      if (!showAnswerOptions.value) {
        formState.answerOptions = [];
      } else if (formState.answerOptions.length === 0) {
        // Add two default options for multiple choice questions
        addAnswerOption();
        addAnswerOption();
      }
    };

    const handleSubmit = async (values) => {
      submitting.value = true;
      try {
        // Prepare the question data
        const questionData = {
          ...formState,
          answerOptions: showAnswerOptions.value ? formState.answerOptions : undefined
        };

        await questionService.createQuestion(questionData);
        message.success('Question created successfully!');
        
        // Reset form
        Object.keys(formState).forEach(key => {
          if (key !== 'answerOptions') {
            formState[key] = '';
          }
        });
        formState.answerOptions = [];
        showAnswerOptions.value = false;
      } catch (error) {
        message.error('Failed to create question: ' + error.message);
      } finally {
        submitting.value = false;
      }
    };

    onMounted(async () => {
      try {
        // Fetch subjects and grades
        // For now, using mock data - in real implementation, you would fetch from API
        subjects.value = [
          { id: '1', name: 'Mathematics' },
          { id: '2', name: 'Science' },
          { id: '3', name: 'English' }
        ];
        
        grades.value = [
          { id: '1', name: 'Grade 1' },
          { id: '2', name: 'Grade 2' },
          { id: '3', name: 'Grade 3' }
        ];
      } catch (error) {
        message.error('Failed to load subjects and grades');
      }
    });

    return {
      formState,
      subjects,
      grades,
      showAnswerOptions,
      submitting,
      form,
      addAnswerOption,
      removeAnswerOption,
      handleQuestionTypeChange,
      handleSubmit
    };
  }
};
</script>