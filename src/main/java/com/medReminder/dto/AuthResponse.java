package com.medReminder.dto;

import com.medReminder.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private String message;
    
    public AuthResponse(String token, Long userId, String email, String name, UserRole role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }
    
    public AuthResponse(String message) {
        this.message = message;
    }
} 