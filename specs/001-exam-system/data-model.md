# Data Model: Student Exam Management System

## Entity: BaseEntity (Abstract base for all entities)
**Description**: Base entity with common fields using MyBatisPlus automatic fill
**Fields**:
- id: Long (Primary Key, auto-generated)
- creator: String (filled automatically by MyBatisPlus from login context or 'admin')
- createTime: LocalDateTime (filled automatically by MyBatisPlus)
- updater: String (filled automatically by MyBatisPlus from login context or 'admin')
- updateTime: LocalDateTime (filled automatically by MyBatisPlus)

## Entity: User
**Description**: Base entity for all system users (students, teachers, administrators)
**Extends**: BaseEntity
**Fields**:
- username: String (unique login identifier)
- email: String (unique, validated email address)
- password: String (encrypted password)
- firstName: String
- lastName: String
- role: String (enum: "STUDENT", "TEACHER", "ADMIN")
- isActive: Boolean (default: true)
- languagePreference: String (enum: "zh-CN", "en-US", default: "zh-CN")  # Supports Chinese/English switching

## Entity: Student
**Description**: Extended user profile for students with educational information
**Extends**: User
**Additional Fields**:
- studentId: String (unique student identifier)
- gradeId: Long (Foreign Key to Grade)
- classId: Long (Foreign Key to Class)
- parentId: Long (Foreign Key to User - optional for parent contact)

## Entity: Teacher
**Description**: Extended user profile for teachers with educational management capabilities
**Extends**: User
**Additional Fields**:
- employeeId: String (unique employee identifier)
- department: String
- hireDate: LocalDate
- qualifications: String (JSON array of qualifications)

## Entity: Administrator
**Description**: Extended user profile for system administrators
**Extends**: User
**Additional Fields**:
- adminLevel: String (enum: "SYSTEM", "SCHOOL", "DEPARTMENT")

## Entity: Subject
**Description**: Academic subject classification
**Extends**: BaseEntity
**Fields**:
- name: String (e.g., "Mathematics", "Science", "English")
- code: String (e.g., "MATH101", "SCI101")
- description: String
- isActive: Boolean (default: true)

## Entity: Grade
**Description**: Educational grade level
**Extends**: BaseEntity
**Fields**:
- name: String (e.g., "Grade 1", "Grade 2")
- code: String (e.g., "G1", "G2")
- description: String
- minAge: Integer
- maxAge: Integer
- isActive: Boolean (default: true)

## Entity: Class
**Description**: Grouping of students under a teacher for instruction and assessment
**Extends**: BaseEntity
**Fields**:
- name: String (e.g., "Grade 1 - Class A")
- gradeId: Long (Foreign Key to Grade - each class belongs to a single grade)
- teacherId: Long (Foreign Key to Teacher)
- maxStudents: Integer (default: 30)
- isActive: Boolean (default: true)

## Entity: Question
**Description**: Exam question with grade level, subject, and knowledge point categorization
**Extends**: BaseEntity
**Fields**:
- content: String (the actual question content)
- questionType: String (enum: "MULTIPLE_CHOICE", "TRUE_FALSE", "SHORT_ANSWER", "ESSAY", "SINGLE_CHOICE")
- gradeId: Long (Foreign Key to Grade)
- subjectId: Long (Foreign Key to Subject)
- knowledgePoint: String (specific topic/area being tested)
- standardExplanation: String (standard explanation for the correct answer)
- difficultyLevel: String (enum: "EASY", "MEDIUM", "HARD")
- options: String (JSON for multiple choice: {"A": "option A", "B": "option B", ...})
- correctAnswer: String (for validation)
- isActive: Boolean (default: true)

## Entity: AnswerOption
**Description**: One of multiple possible answers for multiple choice or single choice questions
**Extends**: BaseEntity
**Fields**:
- questionId: Long (Foreign Key to Question)
- optionText: String (the text content of the answer option)
- optionIndex: Integer (position in the question's answer options array)
- isCorrect: Boolean (whether this is the correct answer for the question)

## Entity: Test
**Description**: Collection of questions assigned to specific students with configurable time limits
**Extends**: BaseEntity
**Fields**:
- title: String
- description: String
- gradeId: Long (Foreign Key to Grade)
- subjectId: Long (Foreign Key to Subject)
- timeLimitMinutes: Integer (configurable by teacher, 0 for no limit)
- isActive: Boolean (default: true)
- isPublished: Boolean (default: false)
- publishDate: LocalDateTime (when test becomes available to students)

## Entity: TestQuestion
**Description**: Junction table to link tests with questions
**Extends**: BaseEntity
**Fields**:
- testId: Long (Foreign Key to Test)
- questionId: Long (Foreign Key to Question)
- order: Integer (order of question in test)
- points: Integer (points for this question)

## Entity: TestAssignment
**Description**: Assignment of tests to students
**Extends**: BaseEntity
**Fields**:
- testId: Long (Foreign Key to Test)
- studentId: Long (Foreign Key to Student)
- assignedBy: Long (Foreign Key to Teacher)
- assignedAt: LocalDateTime
- dueDate: LocalDateTime (optional)
- status: String (enum: "ASSIGNED", "IN_PROGRESS", "SUBMITTED", "GRADED")

## Entity: TestSubmission
**Description**: Student's submission of a test
**Extends**: BaseEntity
**Fields**:
- testId: Long (Foreign Key to Test)
- studentId: Long (Foreign Key to Student)
- submissionDate: LocalDateTime
- answers: String (JSON structured answers: {"questionId": "studentAnswer", ...})
- status: String (enum: "SUBMITTED", "GRADED")
- grade: BigDecimal (0-100, calculated grade)
- gradedBy: Long (Foreign Key to Teacher, nullable)
- gradedAt: LocalDateTime (nullable)
- feedback: String (from teacher)

## Entity: ErrorBook
**Description**: Personalized collection of questions a student answered incorrectly
**Extends**: BaseEntity
**Fields**:
- studentId: Long (Foreign Key to Student)
- questionId: Long (Foreign Key to Question)
- errorCount: Integer (how many times student got this wrong)
- masteryCount: Integer (how many times student answered correctly in a row)
- isMastered: Boolean (true when student answers correctly 3 times in a row)
- masteryAchievedAt: LocalDateTime (nullable)

## Entity: ErrorBookAttempt
**Description**: Record of a student's attempt at an error book question
**Extends**: BaseEntity
**Fields**:
- errorBookId: Long (Foreign Key to ErrorBook)
- attemptDate: LocalDateTime
- studentAnswer: String
- isCorrect: Boolean

## Entity: Notification
**Description**: System notifications for assignments, grades, and system messages
**Extends**: BaseEntity
**Fields**:
- userId: Long (user to receive notification)
- type: String (enum: "ASSIGNMENT", "GRADE_UPDATE", "SYSTEM_MESSAGE")
- title: String (notification title)
- content: String (notification content)
- isRead: Boolean (default: false)
- readAt: LocalDateTime (nullable)

## Entity: SystemConfig
**Description**: System configuration including default admin account and language settings
**Extends**: BaseEntity
**Fields**:
- configKey: String (unique key for configuration item)
- configValue: String (value for configuration)
- description: String (description of the configuration)

## Relationships:

1. **Grade** 1-to-many **Class**
2. **Grade** 1-to-many **Subject** 
3. **Grade** 1-to-many **Question**
4. **Subject** 1-to-many **Question**
5. **Teacher** 1-to-many **Class** (teacher teaches class)
6. **Teacher** 1-to-many **Question** (teacher creates questions)
7. **Teacher** 1-to-many **Test** (teacher creates tests)
8. **Teacher** 1-to-many **TestAssignment** (teacher assigns tests)
9. **Student** 1-to-1 **Grade** (student belongs to grade)
10. **Student** 1-to-1 **Class** (student belongs to class)
11. **Student** 1-to-many **TestAssignment** (student receives assignments)
12. **Student** 1-to-many **TestSubmission** (student submits tests)
13. **Student** 1-to-many **ErrorBook** (student has error book entries)
14. **Test** 1-to-many **TestQuestion** (test contains questions)
15. **Test** 1-to-many **TestAssignment** (test assigned to students)
16. **Test** 1-to-many **TestSubmission** (test receives submissions)
17. **Question** 1-to-many **TestQuestion** (question used in tests)
18. **Question** 1-to-many **ErrorBook** (question in error books)
19. **ErrorBook** 1-to-many **ErrorBookAttempt** (error book has multiple attempts)

## Validation Rules:

1. **User**: Username and email must be unique across all user types
2. **Question**: Must have grade, subject, and knowledge point specified
3. **Test**: Questions must match the test's grade and subject
4. **TestAssignment**: A student can only have one assignment per test at a time
5. **ErrorBook**: Tracks mastery progress (student must answer correctly 3 times in a row to achieve mastery)
6. **Class**: Cannot exceed maxStudents limit
7. **TestSubmission**: Must match the test structure and contain answers for all questions

## MyBatisPlus Configuration:

All entities extend BaseEntity which includes:
- @TableId(type = IdType.AUTO) for Long primary keys
- @TableField(fill = FieldFill.INSERT) for creator and createTime
- @TableField(fill = FieldFill.INSERT_UPDATE) for updater and updateTime
- Custom MetaObjectHandler implementation to populate fields from login context or default to 'admin'