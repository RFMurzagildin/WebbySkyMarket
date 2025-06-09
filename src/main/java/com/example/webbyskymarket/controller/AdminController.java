package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    public final UserService userService;

    @GetMapping
    public String adminPanel(Model model){
        List<User> users = userService.getAllUsersSortedByIdAsc();
        model.addAttribute("users", users);
        return "admin/admin-panel";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam String query, Model model) {
        User user = userService.findByUsername(query);
        model.addAttribute("users", user);
        return "admin/admin-panel";
    }
}
