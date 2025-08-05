package com.medReminder.config;

import com.medReminder.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    )    throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    
    System.out.println("=== JWT FILTER DEBUG ===");
    System.out.println("Request URL: " + request.getRequestURI());
    System.out.println("Auth header: " + (authHeader != null ? "EXISTS" : "NULL"));
    
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        System.out.println("No valid Authorization header - skipping JWT processing");
        filterChain.doFilter(request, response);
        return;
    }
    
    System.out.println("JWT token found, processing...");
    
    final String jwt = authHeader.substring(7);
    System.out.println("JWT token length: " + jwt.length());
    
    try {
        String userEmail = jwtService.extractEmail(jwt);
        System.out.println("Extracted email from JWT: " + userEmail);
        
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Loading user details for: " + userEmail);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set successfully for user: " + userEmail);
                log.debug("JWT authentication successful for user: {}", userEmail);
            } else {
                System.out.println("JWT token is not valid");
            }
        } else {
            System.out.println("User email is null OR authentication already exists");
            if (userEmail == null) {
                System.out.println("  - User email is null");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("  - Authentication already exists: " + SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }
    } catch (Exception e) {
        System.out.println("JWT processing error: " + e.getMessage());
        e.printStackTrace();
        log.warn("JWT authentication failed: {}", e.getMessage());
    }
    
    filterChain.doFilter(request, response);
}
} 