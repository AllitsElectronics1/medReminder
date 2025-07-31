package com.example.webapp;

import retrofit2.Call;
import retrofit2.http.*;

// Data classes for requests
public class AuthRequest {
    private String email;
    private String password;
    
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    
    public RegisterRequest(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

public class AuthResponse {
    private String token;
    private String tokenType;
    private Long userId;
    private String email;
    private String name;
    private String role;
    private String message;
    
    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

// API Service Interface
public interface ApiService {
    
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);
    
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    
    @POST("api/auth/validate")
    Call<AuthResponse> validateToken(@Header("Authorization") String token);
    
    @GET("api/auth/health")
    Call<String> healthCheck();
} 