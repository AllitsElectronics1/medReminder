package com.medReminder.service;

import com.medReminder.dto.AuthRequest;
import com.medReminder.dto.AuthResponse;
import com.medReminder.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse validateToken(String token);
} 