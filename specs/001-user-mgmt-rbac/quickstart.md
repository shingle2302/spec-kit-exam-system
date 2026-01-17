# Quickstart Guide: User Management Module with RBAC

## Prerequisites

- Java 17+
- Node.js 18+ with npm/yarn
- PostgreSQL 12+ (or H2 for development)
- Redis server
- Elasticsearch 7+

## Setup Instructions

### 1. Clone and Navigate to Project

```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Backend Setup

#### Environment Configuration
```bash
# Copy environment template
cp backend/.env.example backend/.env

# Edit the environment variables
vim backend/.env
```

Key environment variables:
```env
# Database Configuration
SPRING_PROFILES_ACTIVE=dev  # or prod
DB_URL=jdbc:postgresql://localhost:5432/user_management_db
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# Elasticsearch Configuration
ELASTICSEARCH_HOST=localhost
ELASTICSEARCH_PORT=9200

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION_MS=1800000  # 30 minutes
REFRESH_TOKEN_EXPIRATION_MS=604800000  # 7 days
```

#### Install Dependencies and Run Backend
```bash
# Navigate to backend directory
cd backend

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The backend server will start on `http://localhost:8080`

### 3. Frontend Setup

#### Environment Configuration
```bash
# Navigate to frontend directory
cd frontend

# Copy environment template
cp .env.example .env

# Edit the environment variables
vim .env
```

Environment variables:
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE="User Management System"
```

#### Install Dependencies and Run Frontend
```bash
# Install dependencies
npm install

# Run the development server
npm run dev
```

The frontend will be available at `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout

### User Management
- `GET /api/users` - Get all users (admin only)
- `POST /api/users` - Create a new user (admin only)
- `GET /api/users/:id` - Get user by ID
- `PUT /api/users/:id` - Update user (admin only)
- `DELETE /api/users/:id` - Delete user (admin only)

### Role Management
- `GET /api/roles` - Get all roles
- `POST /api/roles` - Create a new role (admin only)
- `GET /api/roles/:id` - Get role by ID
- `PUT /api/roles/:id` - Update role (admin only)
- `DELETE /api/roles/:id` - Delete role (admin only)

## Initial Admin User

On first startup, the system creates a default admin user:
- Username: `admin`
- Password: `admin`

**Important**: Change the default password immediately after first login.

## Database Migrations

The application automatically runs database migrations on startup. To manually run migrations:

```bash
# Backend directory
cd backend

# Run migrations
./mvnw flyway:migrate
```

## Running Tests

### Backend Tests
```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw verify
```

### Frontend Tests
```bash
# Unit tests
npm run test:unit

# E2E tests
npm run test:e2e
```

## Docker Deployment (Optional)

To run the entire stack with Docker:

```bash
# At project root
docker-compose up -d
```

This will start:
- Application server
- PostgreSQL database
- Redis cache
- Elasticsearch