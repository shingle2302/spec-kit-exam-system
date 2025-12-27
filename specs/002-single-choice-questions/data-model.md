# Data Model: Single Choice Questions Support

## Entity: SingleChoiceQuestion
**Description**: A question type with multiple answer options where exactly one option is correct

**Fields**:
- id: String (primary key)
- questionText: String (the actual question content)
- questionType: String (enum: "single_choice", "multiple_choice", "true_false", etc.)
- answerOptions: Array of AnswerOption objects
- correctAnswerIndex: Integer (index of the correct option in answerOptions array)
- gradeLevel: String (e.g., "Grade 1", "Grade 2")
- subject: String (e.g., "Mathematics", "Science")
- knowledgePoint: String (specific topic/area being tested)
- createdBy: String (teacher ID)
- createdAt: DateTime
- updatedAt: DateTime
- isActive: Boolean

**Validation Rules**:
- Must have at least 2 answer options (FR-008)
- Must have exactly one correct answer (FR-003)
- Must have between 2-6 answer options (FR-002)

## Entity: AnswerOption
**Description**: One of multiple possible answers for a single choice question

**Fields**:
- id: String (primary key)
- optionText: String (the text content of the answer option)
- optionIndex: Integer (position in the question's answer options array)
- isCorrect: Boolean (whether this is the correct answer for the question)

## Entity: StudentResponse
**Description**: Student's selected answer for a single choice question

**Fields**:
- id: String (primary key)
- studentId: String
- questionId: String (references SingleChoiceQuestion)
- selectedOptionIndex: Integer (index of the option selected by the student)
- responseTime: DateTime
- isCorrect: Boolean (computed from comparison with correct answer)
- testId: String (optional, if part of a test)

**Validation Rules**:
- selectedOptionIndex must be within the range of available options
- Only one response per student per question

## Entity: QuestionType
**Description**: Classification of question format

**Values**:
- single_choice
- multiple_choice
- true_false
- short_answer
- essay

## Relationships
- SingleChoiceQuestion has many AnswerOptions (1 to many)
- StudentResponse belongs to one SingleChoiceQuestion (many to one)
- SingleChoiceQuestion belongs to one QuestionType (many to one)

## State Transitions
- SingleChoiceQuestion: draft → active → inactive
- StudentResponse: pending → submitted → graded

## Indexes
- SingleChoiceQuestion: index on (gradeLevel, subject, knowledgePoint) for efficient searching
- StudentResponse: index on (studentId, questionId) for efficient lookup
- SingleChoiceQuestion: index on (questionType) for filtering by type