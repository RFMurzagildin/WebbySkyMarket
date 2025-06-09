package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.ProductService;
import com.example.webbyskymarket.service.StorageService;
import com.example.webbyskymarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final StorageService storageService;

    @GetMapping("/profile/{id}")
    public String userDetails(@PathVariable Long id, Model model, Authentication authentication){
        User user = userService.getUserById(id);
        User currentUser = userService.findByUsername(authentication.getName());
        List<Product> activeProducts = productService.getProductsByUsername(user.getUsername());
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        return "user/profile";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Product> activeProducts = productService.getProductsByUsername(currentUser.getUsername());
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentUser", currentUser);
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/profile/upload-image")
    public String uploadImage(
            @RequestParam("image") MultipartFile image,
            Authentication authentication
    ){
        userService.updateUserImage(authentication.getName(), imageUpload(image));
        return "user/edit";
    }

    public String imageUpload(MultipartFile image){
        String imagePath = null;
        if(!image.isEmpty()){
            try {
                imagePath = storageService.uploadFile(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imagePath;
    }
}
