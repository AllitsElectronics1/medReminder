package com.medReminder.controller;

import com.medReminder.dto.AuthRequest;
import com.medReminder.dto.AuthResponse;
import com.medReminder.dto.RegisterRequest;
import com.medReminder.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            log.info("Login request received for user: {}", authRequest.getEmail());
            log.debug("Login request details - Email: {}, Password length: {}", 
                     authRequest.getEmail(), 
                     authRequest.getPassword() != null ? authRequest.getPassword().length() : 0);
            
            AuthResponse response = authService.login(authRequest);
            
            if (response.getToken() != null) {
                log.info("Login successful for user: {}", authRequest.getEmail());
                return ResponseEntity.ok(response);
            } else {
                log.warn("Login failed for user: {} - {}", authRequest.getEmail(), response.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("Unexpected error during login for user {}: {}", authRequest.getEmail(), e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login failed");
            errorResponse.put("message", "An unexpected error occurred during login");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user and return JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registration successful",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data or user already exists"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            log.info("Registration request received for user: {}", registerRequest.getEmail());
            log.debug("Registration request details - Name: {}, Email: {}, Role: {}", 
                     registerRequest.getName(), 
                     registerRequest.getEmail(), 
                     registerRequest.getRole());
            
            AuthResponse response = authService.register(registerRequest);
            
            if (response.getToken() != null) {
                log.info("Registration successful for user: {}", registerRequest.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else if (response.getMessage() != null && response.getMessage().contains("already exists")) {
                log.warn("Registration failed - user already exists: {}", registerRequest.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                log.error("Registration failed for user: {} - {}", registerRequest.getEmail(), response.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Unexpected error during registration for user {}: {}", registerRequest.getEmail(), e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            errorResponse.put("message", "An unexpected error occurred during registration");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate token", description = "Validate JWT token and return user information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Token validation failed - no valid Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("No valid authorization header"));
            }
            
            String token = authHeader.substring(7);
            log.info("Token validation request received");
            
            AuthResponse response = authService.validateToken(token);
            
            if (response.getToken() != null) {
                log.info("Token validation successful");
                return ResponseEntity.ok(response);
            } else {
                log.warn("Token validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("Unexpected error during token validation: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Token validation failed");
            errorResponse.put("message", "An unexpected error occurred during token validation");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the authentication service is running")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    public ResponseEntity<String> healthCheck() {
        log.debug("Health check request received");
        return ResponseEntity.ok("Authentication service is running");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();
        
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        
        log.error("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }
}