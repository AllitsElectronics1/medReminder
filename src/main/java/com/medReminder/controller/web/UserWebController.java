package com.medReminder.controller.web;

import com.medReminder.entity.User;
import com.medReminder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserWebController {
    
    private final UserService userService;
    
    @GetMapping("/user/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }
    
    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                             BindingResult result) {
        if (result.hasErrors()) {
            return "user/register";
        }
        
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/user/login")
    public String showLoginForm() {
        return "user/login";
    }
} 