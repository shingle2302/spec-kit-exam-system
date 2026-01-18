# Quickstart Guide: Classroom and Subject Management

## Overview
This guide provides instructions for setting up and working with the classroom and subject management feature in the exam system.

## Prerequisites
- Java 17+ (for backend development)
- Node.js 18+ (for frontend development)
- Maven 3.8+ (for backend builds)
- npm/yarn (for frontend builds)
- PostgreSQL 12+ or H2 database
- Redis (for caching)
- Elasticsearch (for search functionality)

## Environment Setup

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Configure database connection in `application.properties`:
   ```properties
   # For development (H2)
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=

   # For production (PostgreSQL)
   # spring.datasource.url=jdbc:postgresql://localhost:5432/exam_system
   # spring.datasource.username=exam_user
   # spring.datasource.password=exam_password
   ```

3. Start the backend server:
   ```bash
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Configure API endpoint in `.env`:
   ```env
   VITE_API_BASE_URL=http://localhost:8080/api/v1
   ```

4. Start the development server:
   ```bash
   npm run dev
   ```
   The frontend will be available at `http://localhost:3000`

## Key Endpoints

### Classroom Management
- GET `/api/v1/classrooms` - Get all classrooms
- POST `/api/v1/classrooms` - Create a new classroom
- GET `/api/v1/classrooms/{id}` - Get classroom by ID
- PUT `/api/v1/classrooms/{id}` - Update classroom
- DELETE `/api/v1/classrooms/{id}` - Delete classroom

### Subject Management
- GET `/api/v1/subjects` - Get all subjects
- POST `/api/v1/subjects` - Create a new subject
- GET `/api/v1/subjects/{id}` - Get subject by ID
- PUT `/api/v1/subjects/{id}` - Update subject
- DELETE `/api/v1/subjects/{id}` - Delete subject

### Data Import/Export
- POST `/api/v1/data/import` - Import classroom/subject data
- POST `/api/v1/data/export` - Export classroom/subject data

## Key Components

### Backend Components
1. **Controllers**:
   - `ClassroomController` - Handles classroom management endpoints
   - `SubjectController` - Handles subject management endpoints

2. **Entities**:
   - `Classroom` - Represents classroom data
   - `Subject` - Represents subject data
   - `EducationLevel` - Represents education level data

3. **Services**:
   - `ClassroomService` - Business logic for classroom operations
   - `SubjectService` - Business logic for subject operations

4. **Mappers**:
   - `ClassroomMapper` - Database mapping for classrooms
   - `SubjectMapper` - Database mapping for subjects

### Frontend Components
1. **Views**:
   - `ClassroomManagementView` - Main view for classroom management
   - `SubjectManagementView` - Main view for subject management

2. **Components**:
   - `ClassroomTree` - Hierarchical display of classrooms
   - `ClassroomForm` - Form for creating/updating classrooms
   - `SubjectForm` - Form for creating/updating subjects
   - `DataImport/DataExport` - Components for data import/export

## Role-Based Access Control
- **Principal/Super Admin**: Full CRUD access to classroom and subject management
- **Other Roles**: Read-only access to classroom and subject data

Permissions are enforced at both the API layer (Spring Security) and UI layer (conditional rendering).

## Testing

### Backend Tests
```bash
mvn test  # Run all backend tests
mvn test -Dtest=ClassroomServiceTest  # Run specific test class
```

### Frontend Tests
```bash
npm run test  # Run unit tests
npm run test:e2e  # Run end-to-end tests
```

## Database Migrations
Database schema changes should be managed through Flyway migrations located in `src/main/resources/db/migration/`.

## Caching
Frequently accessed classroom and subject data is cached in Redis with appropriate TTL settings. Cache is invalidated when data is modified.

## Logging and Monitoring
- All classroom and subject management operations are logged
- Audit trails are maintained for compliance
- Performance metrics are collected for monitoring