package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.dto.RegistrationDTO;
import com.example.webbyskymarket.enams.Role;
import com.example.webbyskymarket.service.EmailService;
import com.example.webbyskymarket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @Valid RegistrationDTO registrationDTO,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the errors below.");
            return "auth/registration";
        }

        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "auth/registration";
        }

        try {
            userService.createUser(
                    registrationDTO.getName(),
                    registrationDTO.getSurname(),
                    registrationDTO.getUsername(),
                    registrationDTO.getPassword(),
                    Role.USER,
                    registrationDTO.getGender(),
                    registrationDTO.getEmail()
            );
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/registration";
        }
    }
}