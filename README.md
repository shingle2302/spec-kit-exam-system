# Student Exam Management System

A comprehensive exam management system with student, teacher, and admin portals built with Spring Boot and Vue.js.

## Features

- **Question Management**: Teachers can create questions by grade/subject/knowledge point with standard explanations
- **Test Creation**: Teachers can create tests by selecting questions and assigning them to specific students
- **Student Testing**: Students can take assigned tests with configurable time limits
- **Automatic Grading**: System automatically grades multiple-choice questions
- **Error Books**: Personalized error books for students with mastery tracking (3 correct answers in a row)
- **Administrative Functions**: Admins can manage subjects, grades, teachers, and students
- **Student Transfers**: Students can be transferred between classes while preserving error books

## Technology Stack

- **Backend**: Spring Boot, Java 11+, MyBatisPlus, PostgreSQL/H2
- **Frontend**: Vue 3, TypeScript, Ant Design Vue
- **Authentication**: JWT with role-based access control
- **Caching**: Redis
- **Search**: Elasticsearch

## Setup

### Backend
1. Navigate to the `backend` directory
2. Run `mvn clean install` to install dependencies
3. Configure database in `application.properties`
4. Run `mvn spring-boot:run` to start the server

### Frontend
1. Navigate to the `frontend` directory
2. Run `npm install` to install dependencies
3. Run `npm run dev` to start the development server

## API Endpoints

- `POST /api/auth/login` - User authentication
- `GET /api/v1/questions` - Get questions with filtering
- `POST /api/v1/questions` - Create new question
- `POST /api/v1/tests` - Create new test
- `GET /api/v1/tests/student/{studentId}` - Get tests assigned to student
- `POST /api/v1/tests/{testId}/submit` - Submit test answers
- `GET /api/v1/error-book/{studentId}` - Get student's error book
- `POST /api/v1/admin/students` - Create student (admin only)
- `POST /api/v1/admin/students/{studentId}/transfer` - Transfer student between classes

## Architecture

The system follows a layered architecture with clear separation of concerns:
- Controllers handle HTTP requests
- Services contain business logic
- Repositories manage data access
- Models represent domain entities
- DTOs transfer data between layers