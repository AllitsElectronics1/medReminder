# API Structure Documentation

This document outlines the clear separation between REST APIs and Web APIs in the MedReminder application.

## Overview

The application follows a clear separation of concerns with two distinct types of controllers:

1. **REST APIs** - For mobile applications, external integrations, and programmatic access
2. **Web APIs** - For web interface pages and forms

## REST APIs (`/api/*`)

### Purpose
- Serve mobile applications (Android/iOS)
- Provide programmatic access for external systems
- Return JSON/XML responses
- Handle CRUD operations for entities

### Controllers Location
All REST API controllers are located in: `src/main/java/com/medReminder/controller/`

### Available REST Controllers

#### 1. AuthRestController (`/api/auth`)
- **Purpose**: Authentication and authorization for mobile apps
- **Endpoints**:
  - `POST /api/auth/login` - User login
  - `POST /api/auth/register` - User registration
  - `POST /api/auth/refresh` - Token refresh

#### 2. MedicineController (`/api/medicines`)
- **Purpose**: Medicine management for mobile apps
- **Endpoints**:
  - `POST /api/medicines` - Create medicine
  - `GET /api/medicines/{id}` - Get medicine by ID
  - `GET /api/medicines/patient/{patientId}` - Get medicines by patient
  - `PUT /api/medicines/{id}` - Update medicine
  - `DELETE /api/medicines/{id}` - Delete medicine
  - `GET /api/medicines/check-schedule` - Check medicine schedule

#### 3. PatientController (`/api/patients`)
- **Purpose**: Patient management for mobile apps
- **Endpoints**:
  - `POST /api/patients` - Create patient
  - `POST /api/patients/create` - Create patient with request
  - `GET /api/patients/{id}` - Get patient by ID
  - `GET /api/patients` - Get all patients
  - `PUT /api/patients/{id}` - Update patient
  - `PUT /api/patients/{id}/update` - Update patient with schedules
  - `DELETE /api/patients/{id}` - Delete patient

#### 4. DeviceController (`/api/devices`)
- **Purpose**: Device management for mobile apps
- **Endpoints**:
  - `POST /api/devices` - Create device
  - `GET /api/devices/{id}` - Get device by ID
  - `GET /api/devices/patient/{patientId}` - Get device by patient
  - `PUT /api/devices/{id}` - Update device
  - `PUT /api/devices/{id}/status` - Update device status
  - `DELETE /api/devices/{id}` - Delete device
  - `POST /api/devices/update-ip` - Update device IP
  - `POST /api/devices/updateMedicineStatus` - Update medicine status
  - `POST /api/devices/send-message` - Send message to device

#### 5. MedicineScheduleController (`/api/medicine-schedules`)
- **Purpose**: Medicine schedule management
- **Endpoints**:
  - `POST /api/medicine-schedules` - Create schedule
  - `GET /api/medicine-schedules/{id}` - Get schedule by ID
  - `GET /api/medicine-schedules/patient/{patientId}` - Get schedules by patient
  - `PUT /api/medicine-schedules/{id}` - Update schedule
  - `DELETE /api/medicine-schedules/{id}` - Delete schedule

#### 6. UserController (`/api/users`)
- **Purpose**: User management
- **Endpoints**:
  - `GET /api/users/{id}` - Get user by ID
  - `PUT /api/users/{id}` - Update user
  - `DELETE /api/users/{id}` - Delete user

#### 7. BoxActivityController (`/api/box-activities`)
- **Purpose**: Box activity tracking
- **Endpoints**:
  - `POST /api/box-activities` - Create activity
  - `GET /api/box-activities/{id}` - Get activity by ID
  - `GET /api/box-activities/patient/{patientId}` - Get activities by patient

#### 8. ReportController (`/api/reports`)
- **Purpose**: Report generation
- **Endpoints**:
  - `POST /api/reports` - Create report
  - `GET /api/reports/{id}` - Get report by ID
  - `GET /api/reports/patient/{patientId}` - Get reports by patient

## Web APIs (No `/api` prefix)

### Purpose
- Serve web interface pages
- Handle form submissions
- Return HTML views
- Provide user-friendly web navigation

### Controllers Location
All Web API controllers are located in: `src/main/java/com/medReminder/controller/web/`

### Available Web Controllers

#### 1. HomeController (`/`)
- **Purpose**: Home page and basic navigation
- **Endpoints**:
  - `GET /` - Home page

#### 2. UserWebController (`/user/*`)
- **Purpose**: User authentication web pages
- **Endpoints**:
  - `GET /user/register` - Registration form
  - `POST /user/register` - Process registration
  - `GET /user/login` - Login form

#### 3. DashboardController (`/dashboard`)
- **Purpose**: Main dashboard for web interface
- **Endpoints**:
  - `GET /dashboard` - Dashboard page

#### 4. MedicineWebController (`/medicines`)
- **Purpose**: Medicine management web pages
- **Endpoints**:
  - `GET /medicines` - Medicine list page
  - `GET /medicines/add` - Add medicine form
  - `GET /medicines/{id}` - Medicine details page
  - `GET /medicines/{id}/edit` - Edit medicine form
  - `GET /medicines/patient/{patientId}` - Medicines for specific patient

#### 5. PatientWebController (`/patients`)
- **Purpose**: Patient management web pages
- **Endpoints**:
  - `GET /patients` - Patient list page
  - `GET /patients/add` - Add patient form
  - `GET /patients/{id}` - Patient details page
  - `GET /patients/{id}/edit` - Edit patient form
  - `GET /patients/{id}/medicines` - Patient medicines page

#### 6. DeviceWebController (`/devices`)
- **Purpose**: Device management web pages
- **Endpoints**:
  - `GET /devices` - Device list page
  - `GET /devices/add` - Add device form
  - `GET /devices/{id}` - Device details page
  - `GET /devices/{id}/edit` - Edit device form
  - `GET /devices/patient/{patientId}` - Devices for specific patient

#### 7. ReportWebController (`/reports`)
- **Purpose**: Report management web pages
- **Endpoints**:
  - `GET /reports` - Report list page
  - `GET /reports/patient/{patientId}` - Reports for specific patient
  - `GET /reports/{id}` - Report details page
  - `GET /reports/generate` - Generate report form

## Key Differences

| Aspect | REST APIs | Web APIs |
|--------|-----------|----------|
| **URL Prefix** | `/api/*` | No prefix |
| **Response Type** | JSON/XML | HTML |
| **Purpose** | Mobile apps, external systems | Web interface |
| **Authentication** | JWT tokens | Session-based |
| **Controller Type** | `@RestController` | `@Controller` |
| **Return Type** | `ResponseEntity<T>` | `String` (view name) |
| **Package** | `controller` | `controller.web` |

## Best Practices

### REST APIs
- Always use `/api` prefix
- Return `ResponseEntity<T>` with appropriate HTTP status codes
- Use `@RestController` annotation
- Implement proper error handling with `@ControllerAdvice`
- Use DTOs for request/response objects
- Follow RESTful conventions

### Web APIs
- No `/api` prefix
- Return view names as strings
- Use `@Controller` annotation
- Add data to `Model` for view rendering
- Use Thymeleaf templates
- Handle form validation with `BindingResult`

## Security Considerations

- REST APIs use JWT authentication for mobile apps
- Web APIs use session-based authentication
- Both follow Spring Security best practices
- CORS is configured for REST APIs
- CSRF protection is enabled for web forms

## Future Considerations

- Consider implementing API versioning for REST APIs
- Add comprehensive API documentation using OpenAPI/Swagger
- Implement rate limiting for REST APIs
- Add caching strategies for both API types
- Consider implementing GraphQL for complex data fetching 