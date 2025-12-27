# Quickstart: Single Choice Questions Development

## Prerequisites
- Java 11+ (for Spring Boot backend)
- Node.js 16+ (for Vue3 frontend)
- Maven (for backend build)
- npm/yarn (for frontend build)
- PostgreSQL or H2 database
- Redis (for caching)

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
4. Run `mvn spring-boot:run` to start the server

### Frontend (Vue3)
1. Navigate to the frontend directory
2. Run `npm install` to install dependencies
3. Run `npm run dev` to start the development server

## Key Endpoints for Single Choice Questions

### Backend API
- `POST /api/questions` - Create single choice question
- `PUT /api/questions/{id}` - Update single choice question
- `GET /api/questions/{id}` - Get single choice question
- `POST /api/tests/{testId}/submit` - Submit answers for grading

### Frontend Components
- `QuestionForm.vue` - Form for creating single choice questions
- `SingleChoiceQuestion.vue` - Component for displaying single choice questions
- `TestTaking.vue` - Component for taking tests with single choice questions

## Testing

### Backend Tests
- Run `mvn test` for unit and integration tests

### Frontend Tests
- Run `npm run test` for component and unit tests

## Development Workflow
1. Create new single choice question model following the data model
2. Implement backend API endpoints for CRUD operations
3. Create frontend components for question creation and display
4. Implement validation to ensure single selection for single choice questions
5. Add automatic grading functionality
6. Test integration with existing question types