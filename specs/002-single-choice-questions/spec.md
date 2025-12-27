# Feature Specification: Single Choice Questions Support

**Feature Branch**: `002-single-choice-questions`  
**Created**: 2025-12-26  
**Status**: Draft  
**Input**: User description: "题目也要支持单选"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Single Choice Question Creation (Priority: P1)

Teachers can create single choice questions with one correct answer among multiple options. The system allows teachers to define question text, multiple answer choices, and specify which option is correct.

**Why this priority**: This is the core functionality needed to support single choice questions, which is the primary requirement.

**Independent Test**: Teachers can create a single choice question with multiple options and mark one as correct, and the question is saved with the correct answer information.

**Acceptance Scenarios**:

1. **Given** a teacher is creating a question, **When** the teacher selects the single choice question type and enters question text with multiple options, **Then** the teacher can mark one option as the correct answer
2. **Given** a single choice question exists, **When** the question is displayed to a student, **Then** the question shows the question text with multiple answer options for selection

---

### User Story 2 - Single Choice Question Answering (Priority: P1)

Students can answer single choice questions by selecting one option from the available choices. The system captures the student's selection and validates it against the correct answer.

**Why this priority**: This enables students to interact with single choice questions, which is essential for the feature.

**Independent Test**: Students can view single choice questions and select one answer option, with their selection being recorded.

**Acceptance Scenarios**:

1. **Given** a student is taking a test with single choice questions, **When** the student selects an answer option, **Then** the selection is recorded as the student's response
2. **Given** a student has selected an answer for a single choice question, **When** the student moves to the next question, **Then** the selected answer remains saved

---

### User Story 3 - Single Choice Question Grading (Priority: P2)

The system can automatically grade single choice questions by comparing student selections with the correct answers. Teachers can review the automatic grading results.

**Why this priority**: This enables automatic assessment of single choice questions, which is important for efficiency.

**Independent Test**: Single choice questions can be automatically graded with accurate results based on correct answer comparison.

**Acceptance Scenarios**:

1. **Given** a student has completed single choice questions, **When** the system grades the answers automatically, **Then** correct answers are marked as correct and incorrect answers as incorrect
2. **Given** automatic grading is completed, **When** a teacher reviews the results, **Then** the teacher can see which answers were marked correctly or incorrectly

---

### User Story 4 - Single Choice Question Integration (Priority: P2)

Single choice questions can be integrated into existing tests and question banks alongside other question types. The system maintains compatibility with existing functionality.

**Why this priority**: This ensures single choice questions work seamlessly with the existing exam system.

**Independent Test**: Single choice questions can be added to existing tests and managed alongside other question types.

**Acceptance Scenarios**:

1. **Given** various question types exist in the system, **When** a teacher creates a test with mixed question types, **Then** single choice questions appear alongside other question types in the test
2. **Given** a test contains single choice questions, **When** a student takes the test, **Then** the student can answer single choice questions in the same interface as other question types

---

### Edge Cases

- What happens when a student tries to select multiple options for a single choice question?
- How does the system handle a single choice question with no correct answer specified?
- What happens when a single choice question has fewer than 2 options?
- How does the system handle grading when the correct answer is changed after students have already answered?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST support single choice question type with multiple answer options
- **FR-002**: System MUST allow teachers to create single choice questions with 2-6 answer options
- **FR-003**: System MUST allow teachers to designate exactly one correct answer for each single choice question
- **FR-004**: System MUST allow students to select exactly one answer option for single choice questions
- **FR-005**: System MUST prevent students from selecting multiple options for single choice questions
- **FR-006**: System MUST automatically grade single choice questions by comparing student selection to correct answer
- **FR-007**: System MUST allow single choice questions to be included in tests alongside other question types
- **FR-008**: System MUST validate that single choice questions have at least 2 answer options
- **FR-009**: System MUST provide visual indicators for selected answer options in single choice questions
- **FR-010**: System MUST maintain existing question management functionality for single choice questions

### Key Entities *(include if feature involves data)*

- **SingleChoiceQuestion**: A question type with multiple answer options where exactly one option is correct, including question text, answer options, and correct answer index
- **AnswerOption**: One of multiple possible answers for a single choice question with text content and correctness status
- **StudentResponse**: Student's selected answer for a single choice question with reference to the selected option
- **QuestionType**: Classification of question format (single choice, multiple choice, true/false, etc.)

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Teachers can create 10 single choice questions within 15 minutes
- **SC-002**: Students can answer 20 single choice questions within 30 minutes without interface issues
- **SC-003**: System automatically grades single choice questions with 100% accuracy
- **SC-004**: 95% of students can successfully select answers for single choice questions without technical issues
- **SC-005**: Single choice questions integrate seamlessly with existing test functionality (no additional load time)
- **SC-006**: Teachers can identify single choice questions in question management interface within 5 seconds