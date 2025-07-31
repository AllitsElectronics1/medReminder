package com.medReminder.service.impl;

import com.medReminder.dto.AuthRequest;
import com.medReminder.dto.AuthResponse;
import com.medReminder.dto.RegisterRequest;
import com.medReminder.entity.User;
import com.medReminder.entity.UserRole;
import com.medReminder.service.AuthService;
import com.medReminder.service.JwtService;
import com.medReminder.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        try {
            log.info("Attempting login for user: {}", authRequest.getEmail());
            
            // Validate input
            if (authRequest.getEmail() == null || authRequest.getEmail().trim().isEmpty()) {
                log.warn("Login failed - email is null or empty");
                return new AuthResponse("Email is required");
            }
            
            if (authRequest.getPassword() == null || authRequest.getPassword().trim().isEmpty()) {
                log.warn("Login failed - password is null or empty for user: {}", authRequest.getEmail());
                return new AuthResponse("Password is required");
            }
            
            log.debug("Attempting authentication for user: {}", authRequest.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
                )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(userDetails);
                
                // Get user details for response
                User user = userService.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found after successful authentication"));
                
                log.info("Login successful for user: {}", authRequest.getEmail());
                return new AuthResponse(token, user.getUserId(), user.getEmail(), user.getName(), user.getRole());
            } else {
                log.warn("Login failed - authentication not successful for user: {}", authRequest.getEmail());
                return new AuthResponse("Invalid credentials");
            }
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            log.warn("Login failed - bad credentials for user: {}", authRequest.getEmail());
            return new AuthResponse("Invalid email or password");
        } catch (org.springframework.security.authentication.DisabledException e) {
            log.warn("Login failed - account disabled for user: {}", authRequest.getEmail());
            return new AuthResponse("Account is disabled");
        } catch (org.springframework.security.authentication.LockedException e) {
            log.warn("Login failed - account locked for user: {}", authRequest.getEmail());
            return new AuthResponse("Account is locked");
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            log.warn("Login failed - user not found: {}", authRequest.getEmail());
            return new AuthResponse("User not found");
        } catch (Exception e) {
            log.error("Login error for user {}: {}", authRequest.getEmail(), e.getMessage(), e);
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        try {
            log.info("Attempting registration for user: {}", registerRequest.getEmail());
            
            // Check if user already exists
            if (userService.findByEmail(registerRequest.getEmail()).isPresent()) {
                log.warn("Registration failed - user already exists: {}", registerRequest.getEmail());
                return new AuthResponse("User with this email already exists");
            }

            // Create user using existing service logic (password will be encoded automatically)
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword()); // Will be encoded by UserService
            user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : UserRole.CAREGIVER);

            User savedUser = userService.createUser(user);
            
            // Generate token for the new user
            UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
            String token = jwtService.generateToken(userDetails);
            
            log.info("Registration successful for user: {}", registerRequest.getEmail());
            return new AuthResponse(token, savedUser.getUserId(), savedUser.getEmail(), savedUser.getName(), savedUser.getRole());
        } catch (Exception e) {
            log.error("Registration error for user {}: {}", registerRequest.getEmail(), e.getMessage());
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse validateToken(String token) {
        try {
            log.info("Validating token");
            
            String email = jwtService.extractEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            if (jwtService.isTokenValid(token, userDetails)) {
                User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                log.info("Token validation successful for user: {}", email);
                return new AuthResponse(token, user.getUserId(), user.getEmail(), user.getName(), user.getRole());
            } else {
                log.warn("Token validation failed");
                return new AuthResponse("Invalid token");
            }
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return new AuthResponse("Invalid token");
        }
    }
} 