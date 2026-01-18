# Data Model: Class and Subject Management

## Entity: EducationalLevel
**Package**: `com.spec.kit.exam.system.entity`

### Fields:
- `id: Long` - Primary key
- `name: String` - Name of the educational level (kindergarten, elementary, middle, high school)
- `description: String` - Description of the educational level
- `ordering: Integer` - Order for display purposes
- `createTime: LocalDateTime` - Creation timestamp
- `updateTime: LocalDateTime` - Last update timestamp
- `createdBy: String` - Creator identifier
- `updatedBy: String` - Last updater identifier

### Relationships:
- One-to-many: EducationalLevel → Grade

---

## Entity: Grade
**Package**: `com.spec.kit.exam.system.entity`

### Fields:
- `id: Long` - Primary key
- `name: String` - Name of the grade (e.g., "Grade 1", "Small Class", "Middle Class", etc.)
- `levelId: Long` - Foreign key to EducationalLevel
- `description: String` - Description of the grade
- `ordering: Integer` - Order for display purposes within the level
- `createTime: LocalDateTime` - Creation timestamp
- `updateTime: LocalDateTime` - Last update timestamp
- `createdBy: String` - Creator identifier
- `updatedBy: String` - Last updater identifier

### Relationships:
- Many-to-one: Grade ← EducationalLevel
- One-to-many: Grade → Class

---

## Entity: Class
**Package**: `com.spec.kit.exam.system.entity`

### Fields:
- `id: Long` - Primary key
- `name: String` - Name of the class (e.g., "Class 1A", "Kindergarten Small Class 1")
- `gradeId: Long` - Foreign key to Grade
- `capacity: Integer` - Maximum number of students (variable based on educational level)
- `description: String` - Description of the class
- `status: String` - Active/inactive status
- `createTime: LocalDateTime` - Creation timestamp
- `updateTime: LocalDateTime` - Last update timestamp
- `createdBy: String` - Creator identifier
- `updatedBy: String` - Last updater identifier

### Relationships:
- Many-to-one: Class ← Grade
- One-to-many: Class → Subject

---

## Entity: Subject
**Package**: `com.spec.kit.exam.system.entity`

### Fields:
- `id: Long` - Primary key
- `name: String` - Name of the subject (e.g., "Mathematics", "Chinese", "English")
- `classId: Long` - Foreign key to Class (class-specific subject)
- `educationalLevelId: Long` - Foreign key to EducationalLevel
- `description: String` - Description of the subject
- `specialization: String` - Specialization if applicable (e.g., "arts", "science" for math)
- `status: String` - Active/inactive status
- `createTime: LocalDateTime` - Creation timestamp
- `updateTime: LocalDateTime` - Last update timestamp
- `createdBy: String` - Creator identifier
- `updatedBy: String` - Last updater identifier

### Relationships:
- Many-to-one: Subject ← Class
- Many-to-one: Subject ← EducationalLevel

---

## Validation Rules:

### EducationalLevel:
- Name must be unique
- Name must not be null or empty
- Ordering must be positive

### Grade:
- Name must not be null or empty
- LevelId must reference an existing EducationalLevel
- Ordering must be positive within the same level

### Class:
- Name must not be null or empty
- GradeId must reference an existing Grade
- Capacity must be positive
- Capacity must be within acceptable range based on educational level

### Subject:
- Name must not be null or empty
- ClassId must reference an existing Class
- EducationalLevelId must reference an existing EducationalLevel
- Specialization must be valid for the subject type (e.g., only applicable to Math in high school)

---

## State Transitions:

### Class:
- `DRAFT` → `ACTIVE` (when class is confirmed for the academic year)
- `ACTIVE` → `INACTIVE` (when class is completed or cancelled)
- `INACTIVE` → `ARCHIVED` (after academic year ends and retention period expires)

### Subject:
- `DRAFT` → `ACTIVE` (when subject is confirmed for the class)
- `ACTIVE` → `INACTIVE` (when subject is no longer offered)
- `INACTIVE` → `ARCHIVED` (after academic year ends and retention period expires)