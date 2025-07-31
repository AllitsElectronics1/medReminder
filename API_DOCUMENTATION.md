# MedReminder API Documentation

This document provides comprehensive information about the MedReminder REST API endpoints that can be used by both web and Android clients.

## Base URL
```
http://localhost:8080/api
```

## Authentication

The API uses JWT (JSON Web Token) for authentication. All protected endpoints require a valid JWT token in the Authorization header.

### Authorization Header Format
```
Authorization: Bearer <your-jwt-token>
```

## Authentication Endpoints

### 1. User Registration

**Endpoint:** `POST /api/auth/register`

**Description:** Register a new user account

**Request Body:**
```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "role": "USER"
}
```

**Response (201 Created):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john.doe@example.com",
    "name": "John Doe",
    "role": "USER",
    "message": null
}
```

**Error Response (409 Conflict):**
```json
{
    "message": "User with this email already exists"
}
```

### 2. User Login

**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and receive JWT token

**Request Body:**
```json
{
    "email": "john.doe@example.com",
    "password": "password123"
}
```

**Response (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john.doe@example.com",
    "name": "John Doe",
    "role": "USER",
    "message": null
}
```

**Error Response (401 Unauthorized):**
```json
{
    "message": "Invalid credentials"
}
```

### 3. Token Validation

**Endpoint:** `POST /api/auth/validate`

**Description:** Validate JWT token and get user information

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Response (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john.doe@example.com",
    "name": "John Doe",
    "role": "USER",
    "message": null
}
```

**Error Response (401 Unauthorized):**
```json
{
    "message": "Invalid token"
}
```

### 4. Health Check

**Endpoint:** `GET /api/auth/health`

**Description:** Check if the authentication service is running

**Response (200 OK):**
```
Authentication service is running
```

## Android Implementation Example

### Using Retrofit

```kotlin
// API Interface
interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
    
    @POST("auth/validate")
    suspend fun validateToken(@Header("Authorization") token: String): Response<AuthResponse>
}

// Data Classes
data class AuthRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "USER"
)

data class AuthResponse(
    val token: String?,
    val tokenType: String?,
    val userId: Long?,
    val email: String?,
    val name: String?,
    val role: String?,
    val message: String?
)

// Usage Example
class AuthRepository(private val api: AuthApi) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(AuthRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Using OkHttp Interceptor for Token

```kotlin
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val token = tokenManager.getToken()
        if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
        
        return chain.proceed(originalRequest)
    }
}
```

## Error Handling

The API returns appropriate HTTP status codes:

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Invalid credentials or token
- **409 Conflict**: Resource already exists
- **500 Internal Server Error**: Server error

## JWT Token Information

- **Token Type**: Bearer
- **Algorithm**: HS256
- **Expiration**: 24 hours (configurable)
- **Refresh Token Expiration**: 7 days (configurable)

## Security Considerations

1. **HTTPS**: Always use HTTPS in production
2. **Token Storage**: Store tokens securely (Android Keystore, encrypted SharedPreferences)
3. **Token Refresh**: Implement token refresh mechanism for long-running sessions
4. **Input Validation**: Validate all user inputs on the client side
5. **Error Handling**: Don't expose sensitive information in error messages

## Testing

You can test the API using:

1. **Swagger UI**: `http://localhost:8080/swagger-ui.html`
2. **Postman**: Import the endpoints and test with sample data
3. **cURL**: Use command-line tools for testing

### Example cURL Commands

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Validate Token
curl -X POST http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Support

For API support and questions, please refer to the Swagger documentation or contact the development team. 