# Quickstart Guide: Class and Subject Management

## Overview
This guide provides a quick introduction to implementing the class and subject management feature. This feature enables educational institutions to manage classes organized by educational level (kindergarten, elementary, middle, high school) and subjects appropriate to each level.

## Prerequisites
- Java 17
- Node.js 16+
- Maven 3.8+
- PostgreSQL or H2 database
- Redis
- Elasticsearch

## Backend Setup

### 1. Create Controller Classes

#### ClassController.java
```java
package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.service.ClassService;
import com.spec.kit.exam.system.dto.request.ClassQueryRequest;
import com.spec.kit.exam.system.dto.response.ClassResponse;
import com.spec.kit.exam.system.enums.PermissionOperationEnum;
import com.spec.kit.exam.system.annotation.PermissionRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/classes")
@Tag(name = "Class Management", description = "API for managing educational classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Operation(summary = "Create a new class")
    @PostMapping
    @PermissionRequired(menu = "class-management", button = "create-class", operation = PermissionOperationEnum.CREATE)
    public Result<?> createClass(@RequestBody ClassCreateRequest request) {
        return classService.createClass(request);
    }

    @Operation(summary = "Query classes with pagination")
    @PostMapping("/query")
    @PermissionRequired(menu = "class-management", button = "view-class", operation = PermissionOperationEnum.READ)
    public Result<PageResponse<ClassResponse>> queryClasses(@RequestBody ClassQueryRequest request) {
        return classService.queryClasses(request);
    }

    @Operation(summary = "Get class by ID")
    @GetMapping("/{id}")
    @PermissionRequired(menu = "class-management", button = "view-class", operation = PermissionOperationEnum.READ)
    public Result<ClassResponse> getClassById(@PathVariable Long id) {
        return classService.getClassById(id);
    }

    @Operation(summary = "Update class")
    @PutMapping("/{id}")
    @PermissionRequired(menu = "class-management", button = "edit-class", operation = PermissionOperationEnum.UPDATE)
    public Result<?> updateClass(@PathVariable Long id, @RequestBody ClassUpdateRequest request) {
        return classService.updateClass(id, request);
    }

    @Operation(summary = "Delete class")
    @DeleteMapping("/{id}")
    @PermissionRequired(menu = "class-management", button = "delete-class", operation = PermissionOperationEnum.DELETE)
    public Result<?> deleteClass(@PathVariable Long id) {
        return classService.deleteClass(id);
    }

    @Operation(summary = "Get available grades")
    @GetMapping("/grades")
    @PermissionRequired(menu = "class-management", button = "view-grade", operation = PermissionOperationEnum.READ)
    public Result<List<GradeResponse>> getGrades() {
        return classService.getGrades();
    }
}
```

#### SubjectController.java
```java
package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.service.SubjectService;
import com.spec.kit.exam.system.dto.request.SubjectQueryRequest;
import com.spec.kit.exam.system.dto.response.SubjectResponse;
import com.spec.kit.exam.system.enums.PermissionOperationEnum;
import com.spec.kit.exam.system.annotation.PermissionRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/subjects")
@Tag(name = "Subject Management", description = "API for managing educational subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "Create a new subject")
    @PostMapping
    @PermissionRequired(menu = "subject-management", button = "create-subject", operation = PermissionOperationEnum.CREATE)
    public Result<?> createSubject(@RequestBody SubjectCreateRequest request) {
        return subjectService.createSubject(request);
    }

    @Operation(summary = "Query subjects with pagination")
    @PostMapping("/query")
    @PermissionRequired(menu = "subject-management", button = "view-subject", operation = PermissionOperationEnum.READ)
    public Result<PageResponse<SubjectResponse>> querySubjects(@RequestBody SubjectQueryRequest request) {
        return subjectService.querySubjects(request);
    }

    @Operation(summary = "Get subject by ID")
    @GetMapping("/{id}")
    @PermissionRequired(menu = "subject-management", button = "view-subject", operation = PermissionOperationEnum.READ)
    public Result<SubjectResponse> getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @Operation(summary = "Update subject")
    @PutMapping("/{id}")
    @PermissionRequired(menu = "subject-management", button = "edit-subject", operation = PermissionOperationEnum.UPDATE)
    public Result<?> updateSubject(@PathVariable Long id, @RequestBody SubjectUpdateRequest request) {
        return subjectService.updateSubject(id, request);
    }

    @Operation(summary = "Delete subject")
    @DeleteMapping("/{id}")
    @PermissionRequired(menu = "subject-management", button = "delete-subject", operation = PermissionOperationEnum.DELETE)
    public Result<?> deleteSubject(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }

    @Operation(summary = "Get available classes")
    @GetMapping("/classes")
    @PermissionRequired(menu = "subject-management", button = "view-class", operation = PermissionOperationEnum.READ)
    public Result<List<ClassResponse>> getClasses() {
        return subjectService.getClasses();
    }

    @Operation(summary = "Get available educational levels")
    @GetMapping("/levels")
    @PermissionRequired(menu = "subject-management", button = "view-level", operation = PermissionOperationEnum.READ)
    public Result<List<EducationalLevelResponse>> getEducationalLevels() {
        return subjectService.getEducationalLevels();
    }
}
```

### 2. Create Entity Classes

#### ClassEntity.java
```java
package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_class")
public class ClassEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private Long gradeId;
    private Integer capacity;
    private String description;
    private String status = "ACTIVE";
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private String updatedBy;
}
```

#### SubjectEntity.java
```java
package com.spec.kit.exam.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_subject")
public class SubjectEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private Long classId;
    private Long educationalLevelId;
    private String description;
    private String specialization;
    private String status = "ACTIVE";
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private String updatedBy;
}
```

### 3. Create Service Interfaces and Implementations

#### ClassService.java
```java
package com.spec.kit.exam.system.service;

import com.spec.kit.exam.system.dto.request.ClassQueryRequest;
import com.spec.kit.exam.system.dto.response.ClassResponse;
import com.spec.kit.exam.system.dto.request.ClassCreateRequest;
import com.spec.kit.exam.system.dto.request.ClassUpdateRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface ClassService {
    Result<?> createClass(ClassCreateRequest request);
    Result<Page<ClassResponse>> queryClasses(ClassQueryRequest request);
    Result<ClassResponse> getClassById(Long id);
    Result<?> updateClass(Long id, ClassUpdateRequest request);
    Result<?> deleteClass(Long id);
    Result<List<GradeResponse>> getGrades();
}
```

### 4. Frontend Components

#### ClassManagement.vue
```vue
<template>
  <div class="class-management">
    <a-card title="Class Management">
      <div class="toolbar">
        <a-button type="primary" @click="handleCreate">New Class</a-button>
      </div>
      
      <a-table 
        :columns="columns" 
        :data-source="classList" 
        :pagination="pagination"
        :loading="loading"
        row-key="id"
        @change="handleTableChange"
      >
        <template #action="{ record }">
          <a-space>
            <a-button size="small" @click="handleEdit(record)">Edit</a-button>
            <a-popconfirm
              title="Are you sure to delete this class?"
              @confirm="handleDelete(record.id)"
            >
              <a-button danger size="small">Delete</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>
    
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      @ok="handleSubmit"
      @cancel="handleCancel"
    >
      <a-form :model="formState" layout="vertical">
        <a-form-item label="Class Name" name="name">
          <a-input v-model:value="formState.name" />
        </a-form-item>
        
        <a-form-item label="Grade" name="gradeId">
          <a-select v-model:value="formState.gradeId">
            <a-select-option v-for="grade in grades" :key="grade.id" :value="grade.id">
              {{ grade.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="Capacity" name="capacity">
          <a-input-number v-model:value="formState.capacity" style="width: 100%" />
        </a-form-item>
        
        <a-form-item label="Description" name="description">
          <a-textarea v-model:value="formState.description" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useClassApi } from '@/api/classApi';

const { getClassList, createClass, updateClass, deleteClass, getGrades } = useClassApi();

const classList = ref([]);
const grades = ref([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('');
const isEdit = ref(false);
const recordId = ref(null);

const formState = reactive({
  name: '',
  gradeId: undefined,
  capacity: undefined,
  description: ''
});

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
});

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id'
  },
  {
    title: 'Class Name',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: 'Grade',
    dataIndex: 'gradeName',
    key: 'gradeName'
  },
  {
    title: 'Level',
    dataIndex: 'educationalLevelName',
    key: 'educationalLevelName'
  },
  {
    title: 'Capacity',
    dataIndex: 'capacity',
    key: 'capacity'
  },
  {
    title: 'Action',
    key: 'action',
    slots: { customRender: 'action' }
  }
];

onMounted(() => {
  fetchClassList();
  fetchGrades();
});

const fetchClassList = async () => {
  loading.value = true;
  try {
    const params = {
      page: pagination.value.current,
      size: pagination.value.pageSize
    };
    const response = await getClassList(params);
    classList.value = response.data.records;
    pagination.value.total = response.data.total;
  } catch (error) {
    console.error('Failed to fetch class list:', error);
  } finally {
    loading.value = false;
  }
};

const fetchGrades = async () => {
  try {
    const response = await getGrades();
    grades.value = response.data;
  } catch (error) {
    console.error('Failed to fetch grades:', error);
  }
};

const handleTableChange = (paginationInfo) => {
  pagination.value = {
    ...pagination.value,
    current: paginationInfo.current,
    pageSize: paginationInfo.pageSize
  };
  fetchClassList();
};

const handleCreate = () => {
  modalTitle.value = 'Create New Class';
  isEdit.value = false;
  recordId.value = null;
  Object.keys(formState).forEach(key => {
    formState[key] = '';
  });
  modalVisible.value = true;
};

const handleEdit = (record) => {
  modalTitle.value = 'Edit Class';
  isEdit.value = true;
  recordId.value = record.id;
  Object.keys(formState).forEach(key => {
    formState[key] = record[key];
  });
  modalVisible.value = true;
};

const handleDelete = async (id) => {
  try {
    await deleteClass(id);
    fetchClassList();
  } catch (error) {
    console.error('Failed to delete class:', error);
  }
};

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateClass(recordId.value, formState);
    } else {
      await createClass(formState);
    }
    modalVisible.value = false;
    fetchClassList();
  } catch (error) {
    console.error('Failed to submit class:', error);
  }
};

const handleCancel = () => {
  modalVisible.value = false;
};
</script>
```

## Database Schema

### Class Table
```sql
CREATE TABLE t_class (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    grade_id BIGINT NOT NULL,
    capacity INT DEFAULT 0,
    description TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);
```

### Subject Table
```sql
CREATE TABLE t_subject (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    class_id BIGINT NOT NULL,
    educational_level_id BIGINT NOT NULL,
    description TEXT,
    specialization VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);
```

## Key Features

1. **Educational Level Classification**: Classes organized by kindergarten, elementary, middle, and high school levels
2. **Grade Management**: Specific grade classifications within each level
3. **Variable Capacity**: Different capacity limits based on educational level
4. **Role-Based Access**: Editing restricted to principals and super administrators
5. **Class-Specific Subjects**: Each subject is tied to a specific class
6. **Academic Cycle Retention**: Data retention follows academic year cycles
7. **Clear Permission Handling**: Explicit rejection messages for unauthorized actions

## Testing

1. Unit tests for service layer operations
2. Integration tests for API endpoints
3. End-to-end tests for UI workflows
4. Permission validation tests