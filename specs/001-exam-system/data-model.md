# Data Model: Student Exam Management System

## Entity: User
**Description**: Base entity for all system users (students, teachers, administrators)

**Fields**:
- id: String (primary key)
- username: String (unique login identifier)
- email: String (unique, validated email address)
- passwordHash: String (encrypted password)
- firstName: String
- lastName: String
- role: String (enum: "STUDENT", "TEACHER", "ADMIN")
- createdAt: DateTime
- updatedAt: DateTime
- isActive: Boolean

## Entity: Student
**Description**: Extended user profile for students with educational information

**Fields**:
- id: String (primary key, references User)
- studentId: String (unique student identifier)
- gradeLevel: String (e.g., "Grade 1", "Grade 2", references Grade)
- classId: String (references Class)
- enrollmentDate: DateTime
- parentId: String (optional, for parent contact)

## Entity: Teacher
**Description**: Extended user profile for teachers with educational management capabilities

**Fields**:
- id: String (primary key, references User)
- employeeId: String (unique employee identifier)
- department: String
- hireDate: DateTime
- qualifications: Array of String

## Entity: Administrator
**Description**: Extended user profile for system administrators

**Fields**:
- id: String (primary key, references User)
- adminLevel: String (enum: "SYSTEM", "SCHOOL", "DEPARTMENT")

## Entity: Subject
**Description**: Academic subject classification

**Fields**:
- id: String (primary key)
- name: String (e.g., "Mathematics", "Science", "English")
- code: String (e.g., "MATH101", "SCI101")
- description: String
- createdAt: DateTime
- updatedAt: DateTime

## Entity: Grade
**Description**: Educational grade level

**Fields**:
- id: String (primary key)
- name: String (e.g., "Grade 1", "Grade 2")
- code: String (e.g., "G1", "G2")
- description: String
- minAge: Integer
- maxAge: Integer

## Entity: Class
**Description**: Grouping of students under a teacher for instruction and assessment

**Fields**:
- id: String (primary key)
- name: String (e.g., "Grade 1 - Class A")
- gradeId: String (references Grade - each class belongs to a single grade)
- teacherId: String (references Teacher)
- academicYear: String
- maxStudents: Integer
- createdAt: DateTime
- updatedAt: DateTime

## Entity: Question
**Description**: Exam question with grade level, subject, and knowledge point categorization

**Fields**:
- id: String (primary key)
- questionText: String (the actual question content)
- questionType: String (enum: "multiple_choice", "true_false", "short_answer", "essay", "single_choice")
- subjectId: String (references Subject)
- gradeId: String (references Grade)
- knowledgePoint: String (specific topic/area being tested)
- standardExplanation: String (standard explanation for the correct answer)
- createdBy: String (teacher ID)
- createdAt: DateTime
- updatedAt: DateTime
- isActive: Boolean

## Entity: AnswerOption
**Description**: One of multiple possible answers for multiple choice or single choice questions

**Fields**:
- id: String (primary key)
- questionId: String (references Question)
- optionText: String (the text content of the answer option)
- optionIndex: Integer (position in the question's answer options array)
- isCorrect: Boolean (whether this is the correct answer for the question)
- createdAt: DateTime

## Entity: Test
**Description**: Collection of questions assigned to specific students with configurable time limits

**Fields**:
- id: String (primary key)
- title: String
- description: String
- questions: Array of Question IDs
- assignedTo: Array of Student IDs
- assignedBy: String (teacher ID)
- timeLimitMinutes: Integer (configurable by teacher, 0 for no limit)
- dueDate: DateTime
- createdAt: DateTime
- updatedAt: DateTime
- isActive: Boolean
- isGraded: Boolean

## Entity: StudentResponse
**Description**: Student's response to a question in a test

**Fields**:
- id: String (primary key)
- studentId: String
- testId: String (references Test)
- questionId: String (references Question)
- responseText: String (for text-based answers)
- selectedOptionIndex: Integer (for multiple choice answers)
- responseTime: DateTime
- isCorrect: Boolean (computed from comparison with correct answer)
- manualGrade: Boolean (true if manually graded by teacher)
- teacherGrade: Integer (0-100 scale for manual grading)
- teacherComments: String
- submittedAt: DateTime

## Entity: ErrorBook
**Description**: Personalized collection of questions a student answered incorrectly

**Fields**:
- id: String (primary key)
- studentId: String (references Student)
- questionId: String (references Question)
- errorCount: Integer (how many times student got this wrong)
- masteryCount: Integer (how many times student answered correctly in a row)
- mastered: Boolean (true when student answers correctly 3 times in a row)
- firstIncorrectDate: DateTime
- lastPracticeDate: DateTime
- createdAt: DateTime
- updatedAt: DateTime

## Entity: ClassEnrollment
**Description**: Junction table for student-class relationships (needed for transfer tracking)

**Fields**:
- id: String (primary key)
- studentId: String (references Student)
- classId: String (references Class)
- enrollmentDate: DateTime
- transferDate: DateTime (null if not transferred)
- previousClassId: String (references Class, for tracking transfers)
- status: String (enum: "ACTIVE", "TRANSFERRED", "DROPPED")

## Relationships
- User has one Student/Teacher/Administrator (one-to-one)
- Grade has many Classes (one-to-many) - A grade can have multiple classes
- Class belongs to one Grade (many-to-one) - A class belongs to a single grade
- Class has many Students (one-to-many)
- Teacher has many Classes (one-to-many)
- Subject has many Questions (one-to-many)
- Grade has many Questions (one-to-many)
- Question has many AnswerOptions (one-to-many)
- Test has many StudentResponses (one-to-many)
- Student has many ErrorBooks (one-to-many)
- Student has many StudentResponses (one-to-many)
- Student has many ClassEnrollments (one-to-many)
- Class has many ClassEnrollments (one-to-many)

## State Transitions
- Test: created → assigned → active → due → graded
- StudentResponse: pending → submitted → auto-graded → manually-graded
- ErrorBook: created → practicing → mastered (when masteryCount reaches 3)

## Indexes
- User: index on (username, email, role)
- Question: index on (subjectId, gradeId, knowledgePoint, questionType)
- Test: index on (assignedBy, dueDate, isActive)
- StudentResponse: index on (studentId, testId, questionId)
- ErrorBook: index on (studentId, questionId, mastered)
- ClassEnrollment: index on (studentId, classId, status)