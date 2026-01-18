# Data Model: Classroom and Subject Management

## Overview
This document defines the data model for the classroom and subject management feature, including entities, fields, relationships, and validation rules derived from the feature requirements.

## Entity Definitions

### EducationLevel
Represents the major educational divisions (Kindergarten, Elementary School, Middle School, High School)

**Fields**:
- `id` (UUID/Long): Unique identifier for the education level
- `name` (String): Name of the education level (e.g., "Kindergarten", "Elementary School", "Middle School", "High School")
- `level_code` (String): Unique code for the education level (e.g., "K", "ES", "MS", "HS")
- `sort_order` (Integer): Order in which education levels should be displayed
- `created_at` (DateTime): Timestamp when the record was created
- `updated_at` (DateTime): Timestamp when the record was last updated
- `version` (Long): Version number for optimistic locking

**Validation Rules**:
- Name must be unique
- Name length: 2-50 characters
- Level code must be unique and 1-10 characters
- Sort order must be a positive integer

### Classroom
Represents a specific class organized by education level and grade/classification

**Fields**:
- `id` (UUID/Long): Unique identifier for the classroom
- `name` (String): Display name of the classroom (e.g., "Kindergarten Small Class", "Grade 1-A")
- `education_level_id` (UUID/Long): Foreign key reference to EducationLevel
- `grade_number` (Integer): Grade number within the education level (1-12, or null for kindergarten)
- `class_type` (String): Type of class (e.g., "small", "medium", "large", "regular")
- `capacity` (Integer): Maximum number of students that can be enrolled
- `description` (Text): Optional description of the classroom
- `is_active` (Boolean): Whether the classroom is currently active
- `created_at` (DateTime): Timestamp when the record was created
- `updated_at` (DateTime): Timestamp when the record was last updated
- `version` (Long): Version number for optimistic locking

**Validation Rules**:
- Name must be unique within the same education level and grade
- Education level ID must reference a valid EducationLevel
- Grade number must be appropriate for the education level (1-6 for elementary, 7-9 for middle school, 10-12 for high school)
- Class type must be appropriate for the education level (small/medium/large for kindergarten, regular for others)
- Capacity must be a positive integer
- At least one of grade_number or class_type must be specified

### Subject
Represents an academic subject taught at a specific education level

**Fields**:
- `id` (UUID/Long): Unique identifier for the subject
- `name` (String): Name of the subject (e.g., "Mathematics", "Mathematics (Liberal Arts)", "Mathematics (Science)", "Physics")
- `education_level_id` (UUID/Long): Foreign key reference to EducationLevel
- `subject_code` (String): Unique code for the subject (e.g., "MATH", "MATH-LA", "MATH-S", "PHYS")
- `description` (Text): Optional description of the subject
- `is_active` (Boolean): Whether the subject is currently active
- `subject_category` (String): Category for the subject (e.g., "core", "science", "humanities", "math-liberal-arts", "math-science")
- `sort_order` (Integer): Order in which subjects should be displayed
- `created_at` (DateTime): Timestamp when the record was created
- `updated_at` (DateTime): Timestamp when the record was last updated
- `version` (Long): Version number for optimistic locking

**Validation Rules**:
- Name must be unique within the same education level
- Education level ID must reference a valid EducationLevel
- Subject code must be unique within the same education level
- Subject category must be appropriate for the education level (e.g., "math-liberal-arts" and "math-science" only for high school)
- For high school level, Mathematics must be offered as two distinct subjects: "Mathematics (Liberal Arts)" and "Mathematics (Science)"
- Sort order must be a positive integer

## Relationships

### EducationLevel ↔ Classroom
- One-to-Many: One education level can have many classrooms
- Foreign Key: `classrooms.education_level_id` references `education_levels.id`
- Cascade: When an education level is deleted, classrooms should be deactivated rather than deleted (soft delete)

### EducationLevel ↔ Subject  
- One-to-Many: One education level can have many subjects
- Foreign Key: `subjects.education_level_id` references `education_levels.id`
- Cascade: When an education level is deleted, subjects should be deactivated rather than deleted (soft delete)

## Business Rules

1. **Kindergarten Class Types**: Only "small", "medium", "large" class types are allowed for kindergarten level
2. **Elementary Grade Range**: Elementary schools can have grades 1-6 only
3. **Middle School Grade Range**: Middle schools can have grades 7-9 only  
4. **High School Grade Range**: High schools can have grades 10-12 only
5. **Math Subject Split**: For high school level, Mathematics must be offered as both "Mathematics (Liberal Arts)" and "Mathematics (Science)" subjects
6. **Subject Count by Level**: 
   - Kindergarten: Only "General Education" subject
   - Elementary: Core subjects (Chinese, Math, English, etc.)
   - Middle School: 8 subjects (Chinese, Math, English, Physics, Chemistry, Biology, History, Geography)
   - High School: 8 subjects (same as middle school) with Math split into liberal arts and science streams
7. **Access Control**: Only principals and super admins can create/update/delete classroom and subject records; other roles have read-only access

## Indexes

1. `idx_classrooms_edu_level_grade` on (education_level_id, grade_number) - For efficient querying by education level and grade
2. `idx_classrooms_edu_level_type` on (education_level_id, class_type) - For efficient querying by education level and class type  
3. `idx_subjects_edu_level` on (education_level_id, subject_code) - For efficient querying by education level and subject
4. `idx_classrooms_active` on (is_active) - For filtering active classrooms
5. `idx_subjects_active` on (is_active) - For filtering active subjects

## Audit Trail

Each entity includes audit fields to track changes:
- `created_at` and `updated_at` timestamps
- Additional audit logs in separate `audit_logs` table capturing who made changes and what changed
- Version field for optimistic locking to handle concurrent modifications