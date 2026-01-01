<template>
  <div class="student-transfer">
    <a-card title="Student Transfer">
      <a-form
        :model="formState"
        name="transferForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
        @finish="onFinish"
      >
        <a-form-item
          label="Student"
          name="studentId"
          :rules="[{ required: true, message: 'Please select a student!' }]"
        >
          <a-select
            v-model:value="formState.studentId"
            placeholder="Select student to transfer"
            @change="onStudentChange"
          >
            <a-select-option v-for="student in students" :key="student.id" :value="student.id">
              {{ student.firstName }} {{ student.lastName }} ({{ student.studentId }})
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          label="Current Class"
          name="currentClassId"
        >
          <a-input 
            v-model:value="currentClassName" 
            disabled 
            placeholder="Select a student first"
          />
        </a-form-item>

        <a-form-item
          label="Destination Class"
          name="newClassId"
          :rules="[{ required: true, message: 'Please select a destination class!' }]"
        >
          <a-select
            v-model:value="formState.newClassId"
            placeholder="Select destination class"
          >
            <a-select-option v-for="classItem in classes" :key="classItem.id" :value="classItem.id">
              {{ classItem.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 6, span: 16 }">
          <a-button type="primary" html-type="submit" :loading="submitting">
            Transfer Student
          </a-button>
          <a-button style="margin-left: 8px;" @click="resetForm">
            Reset
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- Transfer Confirmation Modal -->
    <a-modal
      v-model:visible="showConfirmationModal"
      title="Confirm Student Transfer"
      @ok="confirmTransfer"
      @cancel="cancelTransfer"
    >
      <p><strong>Student:</strong> {{ selectedStudent?.firstName }} {{ selectedStudent?.lastName }}</p>
      <p><strong>Current Class:</strong> {{ currentClassName }}</p>
      <p><strong>Destination Class:</strong> {{ destinationClassName }}</p>
      <p><strong>Important:</strong> This will preserve the student's error book and learning history.</p>
      <p>Are you sure you want to proceed with the transfer?</p>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { studentTransferService } from '@/services/studentTransferService';
import { studentService } from '@/services/studentService';
import { classService } from '@/services/classService';

// State variables
const students = ref([]);
const classes = ref([]);
const submitting = ref(false);
const showConfirmationModal = ref(false);
const selectedStudent = ref(null);

// Form state
const formState = reactive({
  studentId: null,
  newClassId: null
});

// Computed properties
const currentClassName = computed(() => {
  if (selectedStudent.value && selectedStudent.value.classId) {
    const classItem = classes.value.find(c => c.id === selectedStudent.value.classId);
    return classItem ? classItem.name : 'Unknown';
  }
  return '';
});

const destinationClassName = computed(() => {
  if (formState.newClassId) {
    const classItem = classes.value.find(c => c.id === formState.newClassId);
    return classItem ? classItem.name : 'Unknown';
  }
  return '';
});

// Initialize component
onMounted(async () => {
  await loadStudents();
  await loadClasses();
});

// Load students
const loadStudents = async () => {
  try {
    students.value = await studentService.getStudents();
  } catch (error) {
    message.error('Failed to load students: ' + error.message);
  }
};

// Load classes
const loadClasses = async () => {
  try {
    classes.value = await classService.getClasses();
  } catch (error) {
    message.error('Failed to load classes: ' + error.message);
  }
};

// Handle student selection change
const onStudentChange = async (studentId) => {
  // Find the selected student
  selectedStudent.value = students.value.find(s => s.id === studentId);
};

// Form submission
const onFinish = (values) => {
  // Show confirmation modal before proceeding
  showConfirmationModal.value = true;
};

// Confirm transfer
const confirmTransfer = async () => {
  submitting.value = true;
  try {
    await studentTransferService.transferStudent(formState.studentId, formState.newClassId);
    message.success('Student transferred successfully!');
    
    // Reset form and close modal
    resetForm();
    showConfirmationModal.value = false;
  } catch (error) {
    message.error('Failed to transfer student: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Cancel transfer
const cancelTransfer = () => {
  showConfirmationModal.value = false;
};

// Reset form
const resetForm = () => {
  formState.studentId = null;
  formState.newClassId = null;
  selectedStudent.value = null;
};
</script>

<style scoped>
.student-transfer {
  padding: 20px;
}
</style>