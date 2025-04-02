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
        // TODO: Implement repository logic
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        // TODO: Implement repository logic
        return null;
    }

    @Override
    public User updateUser(Long id, User user) {
        // TODO: Implement repository logic
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        // TODO: Implement repository logic
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        log.info("User found: {}", user.isPresent());
        return user;
    }
} 