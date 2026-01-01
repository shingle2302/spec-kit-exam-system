# Quickstart Guide: Student Exam Management System

## Prerequisites

- Java 17 or higher (for Spring Boot backend)
- Node.js 18+ and npm/yarn (for Vue3 frontend)
- Maven (for backend build)
- PostgreSQL 12+ or H2 database
- Redis (for caching)
- Elasticsearch 7+ (for search)
- Git

## Installation

### 1. Clone the repository
```bash
git clone <repository-url>
cd exam-system
```

### 2. Backend Setup
```bash
# Navigate to backend directory
cd backend

# Install dependencies
mvn clean install

# Configure environment variables
cp .env.example .env
# Edit .env with your database and service configurations
```

### 3. Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install
# or
yarn install
```

## Environment Configuration

Create `.env` files for both backend and frontend with the following variables:

### Backend (.env)
```env
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/exam_system
SPRING_DATASOURCE_USERNAME=exam_user
SPRING_DATASOURCE_PASSWORD=exam_password
SPRING_PROFILES_ACTIVE=dev

# Redis Configuration
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# Elasticsearch Configuration
ELASTICSEARCH_HOST=localhost
ELASTICSEARCH_PORT=9200

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION_MS=86400000

# Email Configuration (for notifications)
SPRING_MAIL_HOST=smtp.example.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@example.com
SPRING_MAIL_PASSWORD=your-email-password
```

### Frontend (.env)
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE="Exam Management System"
```

## Database Setup

### PostgreSQL (Production)
```sql
CREATE DATABASE exam_system;
CREATE USER exam_user WITH PASSWORD 'exam_password';
GRANT ALL PRIVILEGES ON DATABASE exam_system TO exam_user;
```

### Running Migrations
The application will automatically run migrations on startup with MyBatisPlus.

## Running the Application

### 1. Start Supporting Services
```bash
# Start Redis
redis-server

# Start PostgreSQL
sudo systemctl start postgresql

# Start Elasticsearch
elasticsearch
```

### 2. Run Backend
```bash
# From backend directory
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

### 3. Run Frontend
```bash
# From frontend directory
npm run dev
# or
yarn dev
```

## Default Admin Account

After initial setup, the system includes a default administrator account:
- **Username**: `admin`
- **Password**: `change-me`

**Important**: Change the default password immediately after first login.

## Initial Setup

### 1. Database Initialization
The system will automatically create all required tables and indexes on first run with Long primary keys and audit fields.

### 2. Sample Data
The system includes sample data for demonstration:
- 5 sample subjects
- 3 grades
- 10 questions per grade
- 2 sample teachers
- 5 sample students
- 2 sample tests

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
- `GET /api/config/language` - Get language settings for current user
- `PUT /api/config/language` - Update language preference for current user
- `GET /api/entities/{entityType}/{id}/audit` - Get audit trail for entity

### Frontend Components
- `TeacherDashboard.vue` - Teacher portal for question creation and test assignment
- `StudentDashboard.vue` - Student portal for taking tests
- `AdminDashboard.vue` - Administrative functions
- `QuestionForm.vue` - Form for creating questions
- `TestTaking.vue` - Component for taking tests
- `ErrorBook.vue` - Component for error book practice
- `LanguageSwitcher.vue` - Component for language switching

## Testing

### Backend Tests
- Run `mvn test` for unit and integration tests
- All tests must achieve 100% coverage as required by specification

### Frontend Tests
- Run `npm run test` for component and unit tests
- All tests must achieve 100% coverage as required by specification

## Internationalization (i18n)

The system supports real-time Chinese/English switching:
- All UI elements, labels, messages, and content are translatable
- User preference is persisted across sessions
- Language can be changed in user profile settings
- Language switcher component available in header/navigation

## MyBatisPlus Configuration

All entities use:
- Long (64-bit integer) primary keys
- Automatic audit field filling for creation/update tracking
- Creator/updater fields populated from login context or default to 'admin'
- Created/updated time fields automatically filled

## Code Documentation

- All source code includes Chinese comments explaining functionality
- Chinese is the primary comment language
- Documentation is available in both Chinese and English

## Security Features

- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption
- Session management
- Input validation and sanitization
- Sensitive student data stored in database with encryption

## Backup and Recovery

The system performs weekly data backups with 3-year retention:
- Automatic backup scheduling
- Configurable backup locations
- Backup verification and logging

## Development Workflow
1. Start with user authentication and JWT-based role-based access control
2. Implement question management features (creation, categorization)
3. Develop test creation and assignment functionality
4. Create student test-taking interface
5. Implement grading system (automatic with re-grading on standard explanation updates)
6. Add error book functionality with mastery tracking (3 correct answers in a row)
7. Implement administrative features
8. Add student transfer functionality with data preservation
9. Add internationalization (i18n) support for Chinese/English switching
10. Ensure all code includes Chinese comments
11. Implement comprehensive testing with 100% coverage
12. Configure MyBatisPlus for Long primary keys and audit field automatic filling