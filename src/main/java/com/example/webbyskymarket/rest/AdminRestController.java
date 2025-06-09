package com.example.webbyskymarket.rest;

import com.example.webbyskymarket.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminRestController {

    private final AdminService adminService;

    @PostMapping("/block/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        adminService.blockUser(userId);
        return ResponseEntity.ok("User blocked");
    }

    @PostMapping("/unblock/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        adminService.unblockUser(userId);
        return ResponseEntity.ok("User unblocked");
    }

    @PostMapping("/make-admin/{userId}")
    public ResponseEntity<String> makeAdmin(@PathVariable Long userId) {
        adminService.makeAdmin(userId);
        return ResponseEntity.ok("User promoted to admin");
    }

    @PostMapping("/remove-admin/{userId}")
    public ResponseEntity<String> removeAdmin(@PathVariable Long userId, HttpSession session) {
        adminService.removeAdmin(userId, session);
        return ResponseEntity.ok("Admin role removed");
    }
}
