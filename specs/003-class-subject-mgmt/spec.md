# Education Management System Specification

## Overview
This specification outlines the requirements for implementing class and subject management functionality in the examination system. The feature will allow administrators to manage educational structures including class organization by educational level and subject assignments appropriate to each level.

## Clarifications

### Session 2026-01-18

- Q: For the role-based permissions system, what specific permissions should be available for different user types beyond principals and super administrators? → A: View only
- Q: What should be the maximum capacity limits for different class types? → A: Variable capacity by educational level
- Q: Should subjects be reusable across multiple classes or tied to specific classes? → A: Class-specific subjects
- Q: How long should class and subject data be retained after they are no longer active? → A: Academic cycle
- Q: How should the system respond when a user attempts an unauthorized action? → A: Clear rejection

## Business Context
The system needs to support educational institutions with hierarchical organization of classes and subjects appropriate to different educational levels. This includes managing kindergarten classes (small, medium, large), elementary school grades (6 levels), middle school grades (3 levels), and high school grades (3 levels), along with corresponding subject offerings.

## User Scenarios & Testing

### Scenario 1: Class Management
- As a principal or super administrator, I want to create, modify, and delete classes organized by educational level so that I can maintain accurate records of the educational structure.
- As a teacher with appropriate permissions, I want to view classes I'm authorized to see so that I can perform my duties effectively.
- Test: An administrator can create a new class for 3rd grade elementary school and assign it to the correct educational level.

### Scenario 2: Subject Management
- As a principal or super administrator, I want to manage subjects appropriate to each educational level so that curriculum can be properly organized.
- As an authorized user, I want to view subjects relevant to my role so that I can perform assigned tasks.
- Test: An administrator can create a mathematics subject for high school with differentiation between arts and science tracks.

### Scenario 3: Role-Based Access
- As a system user, I want the system to enforce appropriate access controls based on my role so that I can only perform actions I'm authorized to do.
- Test: A regular teacher cannot edit class information if their role doesn't grant that permission.

## Functional Requirements

### FR-CLASS-001: Educational Level Classification
The system shall categorize classes into four educational levels:
- Kindergarten (with small, medium, and large class designations)
- Elementary school (6 grade levels)
- Middle school (3 grade levels)
- High school (3 grade levels)

### FR-CLASS-002: Class Creation and Management
The system shall provide interfaces for creating, reading, updating, and deleting class entities within each educational level.

### FR-SUBJECT-001: Subject Organization by Level
The system shall organize subjects according to educational appropriateness:
- Kindergarten: General education subjects
- Elementary: Core subjects including Chinese, Mathematics, English, and others
- Middle School: 8 subjects including Chinese, Mathematics, English, Physics, Chemistry, Biology, History, and Geography
- High School: Same 8 subjects with Mathematics differentiated for arts and science tracks

### FR-SUBJECT-002: Subject Management
The system shall provide interfaces for creating, reading, updating, and deleting subject entities appropriate to each educational level.

### FR-ACCESS-001: Role-Based Permissions
The system shall restrict class and subject editing capabilities to principals and super administrators only.

### FR-ACCESS-002: Permission Hierarchy
The system shall allow users with appropriate role permissions to view only the classes and subjects based on their assigned role hierarchy, with editing rights restricted to principals and super administrators only.

### FR-ACCESS-003: Permission Enforcement
The system shall enforce permission checks before allowing any operations on class or subject data.

### FR-ACCESS-004: Permission Violation Handling
When a user attempts an unauthorized action, the system shall provide a clear rejection message indicating the specific permission required to perform the action.

## Non-Functional Requirements

### NFR-SECURITY-001: Data Protection
All class and subject data shall be protected with appropriate access controls preventing unauthorized access or modification.

### NFR-DATA-001: Data Retention
Inactive class and subject data shall be retained until the end of the academic year, after which they shall be managed separately in archived form.

### NFR-PERFORMANCE-001: Response Time
Class and subject data retrieval operations shall complete within 3 seconds for authorized users.

### NFR-USABILITY-001: Interface Clarity
The user interface for class and subject management shall clearly indicate educational level associations and role-based permissions.

## Success Criteria

### Functionality Metrics
- 100% of educational levels (kindergarten, elementary, middle, high school) are supported in the class management system
- All required grade classifications are available: 3 for kindergarten, 6 for elementary, 3 for middle school, and 3 for high school
- All subject categories are properly implemented according to educational level requirements
- Math differentiation for arts/science tracks is available in high school

### Access Control Metrics
- Principals and super administrators can successfully perform all edit operations on classes and subjects
- Regular users can only access classes and subjects according to their role permissions
- Unauthorized access attempts are properly rejected

### User Experience Metrics
- Administrators can create and manage classes and subjects within 2 minutes per entity
- Users can view their authorized classes and subjects within 3 seconds of request
- User task completion rate for class/subject management reaches 95%

### Reliability Metrics
- System maintains 99% uptime during business hours
- Data integrity is maintained during all CRUD operations
- Permission changes take effect immediately across the system

## Key Entities

### EducationalLevel
Represents the major divisions: kindergarten, elementary school, middle school, high school
Properties: name, description, ordering

### Grade
Represents specific grade levels within educational levels
For kindergarten: small class, medium class, large class
For elementary: grades 1-6
For middle/high school: grades 7-9 (middle) and 10-12 (high)

### Class
Represents individual class groupings
Associated with an educational level and specific grade
Additional properties: class name, capacity (variable based on educational level), etc.

### Subject
Represents academic subjects taught at specific classes (not shared across classes)
Properties: name, description, educational level, specializations (e.g., math arts vs. science track), associated class

### Role
Defines user permissions and access rights
Includes principal, super administrator, and other roles with specific permissions

### UserRoleAssignment
Links users to their assigned roles
Determines what actions a user can perform

### Permission
Fine-grained access controls for specific operations on classes and subjects

## Assumptions
- The existing system has a role-based access control mechanism that can be extended
- Educational institutions follow standard grade structures as described
- The system already has basic user authentication functionality
- Subject availability follows standard educational curricula