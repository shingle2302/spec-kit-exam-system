# Data Model: Grade and Subject Management

## Entity: EducationLevel
Represents the major educational divisions

**Fields:**
- id: String (Primary Key, auto-generated)
- name: String (e.g., "Kindergarten", "Elementary School", "Middle School", "High School")
- code: String (unique identifier, e.g., "KINDERGARTEN", "ELEMENTARY", "MIDDLE", "HIGH")
- sortOrder: Integer (for ordering in UI)
- description: String (optional description)
- status: String ("ACTIVE", "INACTIVE")
- createTime: LocalDateTime (audit field)
- updateTime: LocalDateTime (audit field)
- createdBy: String (audit field)
- updatedBy: String (audit field)

**Validation Rules:**
- name and code must be unique
- name length: 2-50 characters
- code length: 2-20 characters, uppercase alphanumeric with underscores

## Entity: Grade
Represents a specific class organized by education level and grade/classification

**Fields:**
- id: String (Primary Key, auto-generated)
- name: String (e.g., "Small Class", "Grade 1", "Grade 7", "Grade 10")
- code: String (unique identifier, e.g., "KINDER_SMALL", "ES_GRADE_1", "MS_GRADE_7", "HS_GRADE_10")
- educationLevelId: String (Foreign Key to EducationLevel)
- gradeNumber: Integer (for numeric grades: 1-12, null for kindergarten classes)
- classType: String ("SMALL_CLASS", "MEDIUM_CLASS", "LARGE_CLASS" for kindergarten, null for other levels)
- capacity: Integer (maximum number of students)
- currentEnrollment: Integer (current number of enrolled students, default 0)
- status: String ("ACTIVE", "INACTIVE", "FULL")
- description: String (optional description)
- createTime: LocalDateTime (audit field)
- updateTime: LocalDateTime (audit field)
- createdBy: String (audit field)
- updatedBy: String (audit field)

**Relationships:**
- Many-to-One: EducationLevel (one education level has many grades)
- One-to-Many: Student (one grade has many students - assumed from context)

**Validation Rules:**
- name and code must be unique within education level
- gradeNumber required for elementary/middle/high school, null for kindergarten
- classType required for kindergarten, null for other levels
- capacity must be positive
- currentEnrollment cannot exceed capacity

## Entity: Subject
Represents an academic subject taught at a specific education level

**Fields:**
- id: String (Primary Key, auto-generated)
- name: String (e.g., "Mathematics", "Mathematics (Liberal Arts)", "Chinese", "General Education")
- code: String (unique identifier, e.g., "MATH", "MATH_LIBERAL_ARTS", "CHINESE", "GEN_ED")
- educationLevelId: String (Foreign Key to EducationLevel)
- subjectCategory: String ("CORE", "SCIENCE", "HUMANITIES", "GENERAL", etc.)
- description: String (optional description)
- credits: Integer (academic credits, optional)
- status: String ("ACTIVE", "INACTIVE")
- sortOrder: Integer (for ordering in UI)
- createTime: LocalDateTime (audit field)
- updateTime: LocalDateTime (audit field)
- createdBy: String (audit field)
- updatedBy: String (audit field)

**Relationships:**
- Many-to-One: EducationLevel (one education level has many subjects)

**Validation Rules:**
- name and code must be unique within education level
- For high school math, two specific entries required: "Mathematics (Liberal Arts)" and "Mathematics (Science)"

## Entity: UserRole (referenced from feature spec)
Represents the permission levels that determine access rights

**Fields:**
- id: String (Primary Key, auto-generated)
- roleName: String (e.g., "Principal", "Super Admin", "Teacher", "Student")
- roleCode: String (unique identifier, e.g., "PRINCIPAL", "SUPER_ADMIN")
- description: String (optional description)
- permissions: List<String> (list of permission codes)
- status: String ("ACTIVE", "INACTIVE")
- createTime: LocalDateTime (audit field)
- updateTime: LocalDateTime (audit field)
- createdBy: String (audit field)
- updatedBy: String (audit field)

**Validation Rules:**
- roleName and roleCode must be unique
- Certain roleCodes are reserved for special permissions (PRINCIPAL, SUPER_ADMIN)

## State Transitions

### Grade Status Transitions
- ACTIVE ↔ INACTIVE (administrative actions)
- ACTIVE → FULL (when enrollment reaches capacity)
- FULL → ACTIVE (when enrollment drops below capacity)

### Subject Status Transitions
- ACTIVE ↔ INACTIVE (administrative actions)

### EducationLevel Status Transitions
- ACTIVE ↔ INACTIVE (administrative actions)