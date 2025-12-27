# Quickstart: Student Exam Management System Development

## Prerequisites
- Java 11+ (for Spring Boot backend)
- Node.js 16+ (for Vue3 frontend)
- Maven (for backend build)
- npm/yarn (for frontend build)
- PostgreSQL or H2 database
- Redis (for caching)
- Elasticsearch (for search)

## Setup

### Backend (Spring Boot)
1. Navigate to the backend directory
2. Run `mvn clean install` to install dependencies
3. Configure database in `application.properties`:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/exam_system
   # OR for H2 development:
   # spring.datasource.url=jdbc:h2:mem:testdb
   ```
4. Configure Redis and Elasticsearch connections
5. Run `mvn spring-boot:run` to start the server

### Frontend (Vue3)
1. Navigate to the frontend directory
2. Run `npm install` to install dependencies
3. Run `npm run dev` to start the development server

## Key Endpoints

### Backend API
- `POST /api/auth/login` - User authentication with JWT
- `GET /api/questions` - Get questions with filtering by grade/subject/knowledge point
- `POST /api/questions` - Create new question
- `POST /api/tests` - Create new test
- `GET /api/tests/student/{studentId}` - Get tests assigned to student
- `POST /api/tests/{testId}/submit` - Submit test answers (allows multiple submissions, latest is final)
- `GET /api/error-book/{studentId}` - Get student's error book
- `POST /api/admin/students` - Create student (admin only)
- `POST /api/admin/teachers` - Create teacher (admin only)
- `POST /api/admin/students/{studentId}/transfer` - Transfer student between classes (preserves error books)

### Frontend Components
- `TeacherDashboard.vue` - Teacher portal for question creation and test assignment
- `StudentDashboard.vue` - Student portal for taking tests
- `AdminDashboard.vue` - Administrative functions
- `QuestionForm.vue` - Form for creating questions
- `TestTaking.vue` - Component for taking tests
- `ErrorBook.vue` - Component for error book practice

## Testing

### Backend Tests
- Run `mvn test` for unit and integration tests

### Frontend Tests
- Run `npm run test` for component and unit tests

## Development Workflow
1. Start with user authentication and JWT-based role-based access control
2. Implement question management features (creation, categorization)
3. Develop test creation and assignment functionality
4. Create student test-taking interface
5. Implement grading system (automatic with re-grading on standard explanation updates)
6. Add error book functionality with mastery tracking (3 correct answers in a row)
7. Implement administrative features
8. Add student transfer functionality with data preservation