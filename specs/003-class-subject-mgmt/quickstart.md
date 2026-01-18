# Quickstart Guide: Grade and Subject Management

## Overview
This guide provides instructions for setting up and using the grade and subject management system. This feature allows principals and super admins to manage grade structures organized by education levels and subject configurations.

## Prerequisites
- Java 17+ (for backend)
- Node.js 16+ (for frontend)
- Maven 3.8+ (for backend builds)
- PostgreSQL or H2 database
- Redis for caching
- Elasticsearch for search

## Backend Setup

### 1. Environment Configuration
Create or update `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/exam_system  # Or use H2 for dev
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver  # Use org.h2.Driver for H2
  redis:
    host: localhost
    port: 6379
elasticsearch:
  hosts: localhost:9200
server:
  port: 8080
```

### 2. Run Backend Application
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will be available at `http://localhost:8080`.

## Frontend Setup

### 1. Install Dependencies
```bash
cd frontend
npm install
```

### 2. Run Frontend Application
```bash
npm run serve
```

The frontend will be available at `http://localhost:8080`.

## API Usage Examples

### 1. List Education Levels
```bash
curl -X GET http://localhost:8080/api/education-level/list \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### 2. Create a Grade
```bash
curl -X POST http://localhost:8080/api/grade/create \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Grade 1",
    "code": "ES_GRADE_1",
    "educationLevelId": "level_002",
    "gradeNumber": 1,
    "capacity": 30,
    "description": "Elementary Grade 1 class"
  }'
```

### 3. List Grades with Pagination
```bash
curl -X POST http://localhost:8080/api/grade/list \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "page": 1,
    "size": 10,
    "sortField": "createTime",
    "sortOrder": "desc",
    "filters": {
      "educationLevelId": "level_002",
      "status": "ACTIVE"
    }
  }'
```

### 4. Create a Subject
```bash
curl -X POST http://localhost:8080/api/subject/create \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mathematics (Science)",
    "code": "MATH_SCIENCE",
    "educationLevelId": "level_004",
    "subjectCategory": "CORE",
    "credits": 4,
    "sortOrder": 2,
    "description": "Advanced Mathematics for Science Stream"
  }'
```

## Response Format
All API endpoints return responses in the standardized format:

```json
{
  "data": {},
  "code": 0,
  "msg": "success"
}
```

## Key Features

### Education Level Management
- Four predefined education levels: Kindergarten, Elementary School, Middle School, High School
- Custom education levels can be added as needed
- Each level has specific grade/classification structures

### Grade Organization
- Kindergarten: Small, Medium, Large classes
- Elementary School: Grades 1-6
- Middle School: Grades 7-9
- High School: Grades 10-12

### Subject Management
- Kindergarten: General education subjects
- Elementary: Core subjects (Chinese, Math, English, etc.)
- Middle School: 8 subjects (Chinese, Math, English, Physics, Chemistry, Biology, History, Geography)
- High School: 8 subjects with Math split into "Mathematics (Liberal Arts)" and "Mathematics (Science)"

### Role-Based Access Control
- Principal and Super Admin roles can create, update, and delete grades and subjects
- Other roles can only view grade and subject structures
- Fine-grained permissions using annotation-based approach

### Import/Export Capabilities
- Import grade and subject data from CSV/JSON files
- Export grade and subject data to CSV/JSON files
- Batch operations for efficient data management

## Troubleshooting

### Common Issues
1. **Permission Denied**: Ensure your user account has Principal or Super Admin role for write operations
2. **Database Connection**: Verify your database credentials in application.yml
3. **Redis Connection**: Ensure Redis server is running for caching functionality
4. **Elasticsearch Connection**: Verify Elasticsearch is running for search functionality

### API Response Codes
- `0`: Success
- `1001`: Validation Error - Check request parameters
- `1002`: Invalid Request Parameter - Check parameter format
- `2001`: Unauthorized Access - Check authentication token
- `2002`: Insufficient Permissions - Check user role
- `4001`: Resource Not Found - Resource doesn't exist
- `3001`: Internal Server Error - Contact administrator

## Next Steps
1. Set up your administrative accounts with Principal or Super Admin roles
2. Configure education levels in the system
3. Create grade structures for each education level
4. Define subject offerings for each education level
5. Import existing data if migrating from another system