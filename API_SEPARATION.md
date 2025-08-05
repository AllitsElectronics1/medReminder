# API Separation Documentation

This document shows the current separation between REST APIs and Web APIs in your MedReminder application.

## Current Structure

### REST APIs (for mobile apps, external systems)
**Location**: `src/main/java/com/medReminder/controller/`
**URL Prefix**: `/api/*`
**Purpose**: Serve mobile applications and external integrations

#### Controllers:
- `AuthRestController.java` - `/api/auth/*`
- `MedicineController.java` - `/api/medicines/*`
- `PatientController.java` - `/api/patients/*`
- `DeviceController.java` - `/api/devices/*`
- `MedicineScheduleController.java` - `/api/medicine-schedules/*`
- `UserController.java` - `/api/users/*`
- `BoxActivityController.java` - `/api/box-activities/*`
- `ReportController.java` - `/api/reports/*`

### Web APIs (for web interface)
**Location**: `src/main/java/com/medReminder/controller/web/`
**URL Prefix**: No prefix
**Purpose**: Serve web interface pages

#### Controllers:
- `HomeController.java` - `/`
- `UserWebController.java` - `/user/*`
- `DashboardController.java` - `/dashboard`
- `MedicineWebController.java` - `/medicines/*`
- `PatientWebController.java` - `/patients/*`
- `DeviceWebController.java` - `/devices/*`
- `ReportWebController.java` - `/reports/*`

## Separation Summary

✅ **REST APIs**: All in `controller/` package with `/api` prefix  
✅ **Web APIs**: All in `controller/web/` package with no prefix  
✅ **No API changes**: All existing functionality preserved  
✅ **Clear organization**: Easy to distinguish between API types  

## Benefits

1. **Clear Purpose**: REST APIs for mobile/external, Web APIs for web interface
2. **Better Organization**: Related functionality grouped together
3. **Easy Maintenance**: Changes to web interface don't affect REST APIs
4. **Scalability**: Can scale REST APIs and web APIs independently

## URL Examples

### REST APIs (Mobile/External)
- `POST /api/auth/login` - Mobile app login
- `GET /api/medicines/patient/123` - Get medicines for patient
- `POST /api/devices/update-ip` - Update device IP

### Web APIs (Web Interface)
- `GET /` - Home page
- `GET /dashboard` - Dashboard page
- `GET /medicines` - Medicine list page
- `GET /patients/add` - Add patient form

The separation is already in place and working correctly! 