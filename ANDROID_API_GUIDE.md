# Android API Integration Guide

This guide provides all the REST APIs available for the Android app. All backend logic is already implemented - just use these endpoints!

## Base URL
```
http://localhost:8080/api
```

## Authentication
All protected endpoints require JWT token in Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 🔐 Authentication APIs

### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "CAREGIVER"
}
```

### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "john@example.com",
    "password": "password123"
}
```

### Validate Token
```http
POST /api/auth/validate
Authorization: Bearer <your-jwt-token>
```

---

## 👥 Patient Management APIs

### Create Patient
```http
POST /api/patients/create
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "name": "Patient Name",
    "age": 65,
    "gender": "MALE",
    "contactNumber": "1234567890",
    "emergencyContact": "9876543210",
    "address": "Patient Address",
    "medicalHistory": "Medical history details",
    "allergies": "Known allergies",
    "medicines": [
        {
            "medicineName": "Aspirin",
            "dosage": "100mg",
            "description": "Blood thinner",
            "schedules": [
                {
                    "dayOfWeek": "MONDAY",
                    "time": "08:00:00",
                    "isActive": true
                }
            ]
        }
    ]
}
```

### Get All Patients
```http
GET /api/patients
Authorization: Bearer <your-jwt-token>
```

### Get Patient by ID
```http
GET /api/patients/{id}
Authorization: Bearer <your-jwt-token>
```

### Update Patient
```http
PUT /api/patients/{id}
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "name": "Updated Patient Name",
    "age": 66,
    "gender": "MALE",
    "contactNumber": "1234567890",
    "emergencyContact": "9876543210",
    "address": "Updated Address",
    "medicalHistory": "Updated medical history",
    "allergies": "Updated allergies"
}
```

### Delete Patient
```http
DELETE /api/patients/{id}
Authorization: Bearer <your-jwt-token>
```

---

## 💊 Medicine Management APIs

### Create Medicine
```http
POST /api/medicines
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "medicineName": "Aspirin",
    "dosage": "100mg",
    "description": "Blood thinner medication",
    "patient": {
        "id": 1
    }
}
```

### Get Medicine by ID
```http
GET /api/medicines/{id}
Authorization: Bearer <your-jwt-token>
```

### Get Medicines by Patient
```http
GET /api/medicines/patient/{patientId}
Authorization: Bearer <your-jwt-token>
```

### Update Medicine
```http
PUT /api/medicines/{id}
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "medicineName": "Updated Aspirin",
    "dosage": "150mg",
    "description": "Updated description"
}
```

### Delete Medicine
```http
DELETE /api/medicines/{id}
Authorization: Bearer <your-jwt-token>
```

### Check Medicine Schedule (Public)
```http
GET /api/medicines/check-schedule?macAddress=AA:BB:CC:DD:EE:FF
```

---

## ⏰ Medicine Schedule APIs

### Create Schedule
```http
POST /api/medicine-schedules
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "medicine": {
        "id": 1
    },
    "dayOfWeek": "MONDAY",
    "time": "08:00:00",
    "isActive": true
}
```

### Get Schedule by ID
```http
GET /api/medicine-schedules/{id}
Authorization: Bearer <your-jwt-token>
```

### Get Schedules by Medicine
```http
GET /api/medicine-schedules/medicine/{medicineId}
Authorization: Bearer <your-jwt-token>
```

### Get Schedules by Time
```http
GET /api/medicine-schedules/time?time=08:00:00
Authorization: Bearer <your-jwt-token>
```

### Update Schedule
```http
PUT /api/medicine-schedules/{id}
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "dayOfWeek": "TUESDAY",
    "time": "09:00:00",
    "isActive": true
}
```

### Update Schedule Status
```http
PUT /api/medicine-schedules/{id}/status?isActive=false
Authorization: Bearer <your-jwt-token>
```

### Delete Schedule
```http
DELETE /api/medicine-schedules/{id}
Authorization: Bearer <your-jwt-token>
```

---

## 👤 User Management APIs

### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer <your-jwt-token>
```

### Get All Users
```http
GET /api/users
Authorization: Bearer <your-jwt-token>
```

### Update User
```http
PUT /api/users/{id}
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "name": "Updated Name",
    "email": "updated@example.com",
    "role": "ADMIN"
}
```

### Delete User
```http
DELETE /api/users/{id}
Authorization: Bearer <your-jwt-token>
```

---

## 📱 Device Communication APIs (Public)

### Update Device IP
```http
POST /api/devices/update-ip
Content-Type: application/json

{
    "macAddress": "AA:BB:CC:DD:EE:FF",
    "ipAddress": "192.168.1.100"
}
```

### Update Medicine Status
```http
POST /api/devices/updateMedicineStatus
Content-Type: application/json

{
    "macAddress": "AA:BB:CC:DD:EE:FF",
    "medicineId": 1,
    "status": "TAKEN"
}
```

### Send Device Message
```http
POST /api/devices/send-message
Content-Type: application/json

{
    "macAddress": "AA:BB:CC:DD:EE:FF",
    "message": "Time to take medicine!"
}
```

---

## 📊 Report APIs

### Get Reports
```http
GET /api/reports
Authorization: Bearer <your-jwt-token>
```

### Get Reports by Patient
```http
GET /api/reports/patient/{patientId}
Authorization: Bearer <your-jwt-token>
```

---

## 🔧 Box Activity APIs

### Get Box Activities
```http
GET /api/box-activities
Authorization: Bearer <your-jwt-token>
```

### Get Activities by Patient
```http
GET /api/box-activities/patient/{patientId}
Authorization: Bearer <your-jwt-token>
```

---

## 📋 Response Formats

### Success Response
```json
{
    "id": 1,
    "name": "Patient Name",
    "age": 65,
    "gender": "MALE",
    "contactNumber": "1234567890",
    "emergencyContact": "9876543210",
    "address": "Patient Address",
    "medicalHistory": "Medical history",
    "allergies": "Allergies",
    "medicines": [...]
}
```

### Error Response
```json
{
    "error": "Error message",
    "timestamp": "2024-01-01T12:00:00",
    "status": 400
}
```

### Authentication Response
```json
{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "user@example.com",
    "name": "User Name",
    "role": "CAREGIVER"
}
```

---

## 🚀 Android Implementation Tips

### 1. Retrofit Setup
```kotlin
interface MedReminderApi {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @GET("patients")
    suspend fun getPatients(@Header("Authorization") token: String): Response<List<Patient>>
    
    // Add other endpoints...
}
```

### 2. Token Management
```kotlin
class TokenManager {
    fun saveToken(token: String) {
        // Save to SharedPreferences or EncryptedStorage
    }
    
    fun getToken(): String? {
        // Retrieve from storage
    }
}
```

### 3. API Client
```kotlin
class ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()
    
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }
}
```

### 4. Error Handling
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
```

---

## 🔒 Security Notes

1. **Always use HTTPS** in production
2. **Store JWT tokens securely** (Android Keystore)
3. **Validate all inputs** on client side
4. **Handle token expiration** gracefully
5. **Implement proper error handling**

---

## 📚 Testing

Test APIs using:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Postman**: Import endpoints and test
- **cURL**: Command line testing

All backend logic is implemented and ready to use! 🚀 