# API Redesign Specification: Unified Response Format and Structure

## Overview

This specification outlines the redesign of the API response format and structure across the examination system. The primary goal is to standardize all API responses to use a consistent format using the existing `Result` utility class, which follows the `{data, code, msg}` pattern.

## Current State Analysis

The current system already has:
- An existing `Result` utility class in `backend/src/main/java/com/spec/kit/exam/system/util/Result.java`
- Basic API response patterns established in the menu-permission system
- Pagination support with `PageRequestDTO` and `PageResponseDTO` classes
- Standardized error handling with global exception handler

## Goals

1. **Standardization**: Ensure all API endpoints return responses in a consistent format using the existing `Result` class
2. **Relocation**: Move existing response-related classes from util directory to appropriate packages
3. **Documentation**: Establish clear API contract documentation for all endpoints
4. **Consistency**: Apply unified response patterns across all modules and features

## Non-Goals

1. Creating new response classes (since `Result` already exists)
2. Changing the underlying data structures or business logic
3. Modifying authentication/authorization mechanisms
4. Redesigning database schemas

## Technical Approach

### Response Format

All API endpoints will return responses using the existing `Result<T>` class with the following structure:
```json
{
  "data": {},
  "code": 0,
  "msg": "success"
}
```

Where:
- `data`: Contains the response payload (varies by endpoint)
- `code`: Numeric status code (0 for success, specific error codes for failures)
- `msg`: Human-readable message describing the result

### Class Relocation Plan

1. Move `Result.java` from `util` to `response` package
2. Move pagination DTOs (`PageRequestDTO`, `PageResponseDTO`) from `util` to `dto` package
3. Move other response-related classes from `util` to appropriate packages

## Implementation Tasks

### Phase 1: Infrastructure Setup

- [ ] T001A Relocate existing Result utility class from util directory to backend/src/main/java/com/spec/kit/exam/system/response/Result.java
- [ ] T001B Relocate existing PageRequestDTO for pagination from util directory to backend/src/main/java/com/spec/kit/exam/system/dto/PageRequestDTO.java
- [ ] T001C Relocate existing PageResponseDTO for pagination from util directory to backend/src/main/java/com/spec/kit/exam/system/dto/PageResponseDTO.java
- [ ] T001D Relocate existing ImportResultDTO for import results from util directory to backend/src/main/java/com/spec/kit/exam/system/dto/ImportResultDTO.java
- [ ] T001E Relocate existing ExportRequestDTO for export requests from util directory to backend/src/main/java/com/spec/kit/exam/system/dto/ExportRequestDTO.java

### Phase 2: API Endpoint Updates

- [ ] T002A Update all existing controllers to use Result<T> return type consistently
- [ ] T002B Update all existing service methods to return appropriate Result objects
- [ ] T002C Update all existing mapper interfaces to work with Result-wrapped responses
- [ ] T002D Update global exception handler to return Result format consistently

### Phase 3: Documentation and Testing

- [ ] T003A Update API contract documentation to reflect Result-based responses
- [ ] T003B Create comprehensive test suite for response format consistency
- [ ] T003C Update quickstart guide with Result usage examples
- [ ] T003D Add API response validation middleware/components

## Error Handling Strategy

The system will maintain the existing error code mapping:
- `0`: Success
- `1001`: Validation Error
- `1002`: Invalid Request Parameter
- `2001`: Unauthorized Access
- `2002`: Insufficient Permissions
- `2003`: Authentication Required
- `3001`: Internal Server Error
- `3002`: Service Unavailable
- `4001`: Resource Not Found
- `4002`: Resource Already Exists

## Dependencies

- Menu and Permission Management System (already implemented Result class)
- Existing authentication/authorization framework
- Database connection and ORM setup

## Success Criteria

1. All API endpoints return responses in the standardized {data, code, msg} format using Result<T>
2. All existing functionality remains unchanged except for response format
3. All tests pass with updated response format
4. API documentation accurately reflects the new response structure
5. Frontend applications can consume the standardized responses without major modifications

## Rollout Plan

1. Phase 1: Implement infrastructure changes (Result relocation)
2. Phase 2: Update existing API endpoints gradually
3. Phase 3: Update documentation and run comprehensive tests
4. Phase 4: Deploy to staging environment for validation
5. Phase 5: Deploy to production after successful validation

## Risk Mitigation

- Maintain backward compatibility during transition period
- Thorough testing of all affected endpoints
- Gradual rollout with rollback capability
- Close monitoring during deployment

## Acceptance Criteria

- [ ] All API endpoints return Result<T> wrapped responses
- [ ] Error handling follows standardized format
- [ ] Pagination endpoints use Result-wrapped responses
- [ ] Documentation reflects new response structure
- [ ] Frontend applications can consume new response format
- [ ] All automated tests pass