package com.medReminder.service.impl;

import com.medReminder.entity.User;
import com.medReminder.repository.UserRepository;
import com.medReminder.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
        log.info("Finding user by ID: {}", id);
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Updating user with ID: {}", id);
        User existingUser = getUserById(id);
        
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setRole(user.getRole());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully: {}", updatedUser.getEmail());
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User deleted successfully");
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        log.info("User found: {}", user.isPresent());
        return user;
    }
} 