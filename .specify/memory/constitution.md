<!-- 
Sync Impact Report:
- Version change: 1.1.0 → 1.2.0
- Modified principles: Architecture and Technology Standards (added package structure requirements)
- Added sections: Package Structure Standards
- Removed sections: N/A
- Templates requiring updates: ⚠ pending - .specify/templates/plan-template.md, .specify/templates/spec-template.md, .specify/templates/tasks-template.md, .specify/templates/commands/*.md
- Follow-up TODOs: None
-->
# Spec-Kit Exam System Constitution

## Core Principles

### Code Quality Standards
All code must adhere to established style guides including the Alibaba Java Coding Guidelines and pass static analysis checks (SonarQube) before merging. Code reviews are mandatory for all changes, with emphasis on maintainability, readability, and adherence to architectural patterns. Code complexity must comply with SonarQube rules and thresholds. Complexity must be justified and documented, with preference given to simple, clear solutions over clever implementations.

### Testing Standards (NON-NEGOTIABLE)
Comprehensive test coverage is mandatory: unit tests for all business logic, integration tests for service interactions, and end-to-end tests for critical user flows. Test-driven development is encouraged for new features. All tests must pass before code can be merged, with minimum 80% coverage thresholds for new code.

### User Experience Consistency
User interfaces must follow consistent design patterns and component libraries across all applications. All user-facing features must maintain visual and behavioral consistency with established design systems. Accessibility standards (WCAG 2.1 AA) must be met for all new features.

### Performance Requirements
All system responses must meet defined SLAs: API endpoints under 500ms, UI interactions under 100ms, and page load times under 3 seconds. Performance benchmarks must be established and monitored for all critical paths. Resource utilization must remain within defined thresholds under expected load conditions.

### Architecture and Technology Standards
Frontend applications must use Vue3 with Ant Design Vue components for consistent UI patterns. Backend services must use Spring Boot with MyBatisPlus for database operations. Package structure must follow com.spec.kit.exam.system convention. Support dual database configuration (H2 for development, PostgreSQL for production) with seamless switching capability. All data must be simultaneously stored in Elasticsearch for fast search queries. Redis caching must be implemented for frequently accessed data to improve response times.

### API Response Standards
All API responses must follow the standardized format {data, code, msg} where:
- data: Contains the actual response payload or null if no data
- code: Numeric status code (200 for success, specific error codes for failures)
- msg: Human-readable message describing the result or error

### Request Method Standards
Pagination queries and all complex requests must use POST method with parameters passed in the request body rather than URL query parameters. This ensures consistency and allows handling of complex filter objects.

### Package Structure Standards
All backend code must follow the package structure convention: com.spec.kit.exam.system. DTO (Data Transfer Objects) must be used for frontend-backend interaction objects. Entity classes must represent database entities. Existing project structure must be preserved and not modified unnecessarily.

## Technology Stack Requirements

The project must use the following technology stack:
- Frontend: Vue3 with TypeScript, Ant Design Vue and Element Plus for UI components
- Backend: Spring Boot with Java, MyBatisPlus for ORM
- Databases: Support both H2 (development) and PostgreSQL (production)
- Search: Elasticsearch for advanced querying and indexing
- Caching: Redis for performance optimization
- Testing: JUnit for backend, Vitest/Jest for frontend, Cypress for E2E tests
- Build tools: Maven for backend, Vite for frontend
- Code Quality: SonarQube for static analysis and complexity monitoring

## Development Workflow

All development must follow a structured workflow including:
- Feature branches created from main
- Pull requests with mandatory code reviews
- Automated testing and quality checks
- Pre-commit hooks enforcing code standards
- Continuous integration with build verification
- Documentation updates for new features
- Security scanning for dependencies
- Static analysis compliance with SonarQube rules

## Governance

This constitution serves as the authoritative guide for all technical decisions within the project. All code changes must comply with these principles. Amendments to this constitution require approval from the technical steering committee and must include a migration plan for existing code. All team members are responsible for ensuring compliance with these standards during code reviews and development activities.

**Version**: 1.2.0 | **Ratified**: 2025-12-26 | **Last Amended**: 2026-01-18