package com.example.webbyskymarket.service;

import com.example.webbyskymarket.enams.Role;
import com.example.webbyskymarket.enams.UserStatus;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    public void blockUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getStatus() != UserStatus.BLOCKED) {
            user.setStatus(UserStatus.BLOCKED);
            userRepository.save(user);

            try {
                emailService.sendBlockedAccountEmail(user.getEmail(), user.getUsername());
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email notification", e);
            }
        }
    }

    public void unblockUser(Long userId) {
        User user = userService.getUserById(userId);
        if(user.getStatus() != UserStatus.ACTIVE){
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

            try {
                emailService.sendUnblockedAccountEmail(user.getEmail(), user.getUsername());
            }catch (MessagingException e) {
                throw new RuntimeException("Failed to send email notification", e);
            }
        }

    }

    public void makeAdmin(Long userId) {
        User user = userService.getUserById(userId);
        user.setRole(Role.ADMIN);
        userRepository.save(user);

    }

    public void removeAdmin(Long userId, HttpSession session) {
        User user = userService.getUserById(userId);
        user.setRole(Role.USER);

        userRepository.save(user);

    }
}
