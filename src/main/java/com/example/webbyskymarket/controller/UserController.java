package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.dto.ProfileCommentDTO;
import com.example.webbyskymarket.enams.ProductStatus;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.ProductService;
import com.example.webbyskymarket.service.ProfileCommentService;
import com.example.webbyskymarket.service.StorageService;
import com.example.webbyskymarket.service.UserService;
import com.example.webbyskymarket.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final ProfileCommentService profileCommentService;
    private final CurrencyService currencyService;

    @GetMapping("/profile/{id}")
    public String userDetails(@PathVariable Long id, Model model, Authentication authentication,
                             @CookieValue(value = "currency", defaultValue = "USD") String currency) {
        User user = userService.getUserById(id);
        User currentUser = userService.findByUsername(authentication.getName());
        List<Product> activeProducts = productService.getProductsByUsernameAndStatus(user.getUsername(), ProductStatus.ACTIVE);
        List<Product> salesProducts = productService.getProductsByUsernameAndStatus(user.getUsername(), ProductStatus.SOLD);
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("salesProducts", salesProducts);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profileComments", profileCommentService.getCommentsForUser(user));
        model.addAttribute("newProfileComment", new ProfileCommentDTO());
        model.addAttribute("currency", currency);
        model.addAttribute("usdToRub", currencyService.getUsdToRub());
        return "user/profile";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model,
                             @CookieValue(value = "currency", defaultValue = "USD") String currency) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Product> activeProducts = productService.getProductsByUsernameAndStatus(currentUser.getUsername(), ProductStatus.ACTIVE);
        List<Product> salesProducts = productService.getProductsByUsernameAndStatus(currentUser.getUsername(), ProductStatus.SOLD);
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("salesProducts", salesProducts);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profileComments", profileCommentService.getCommentsForUser(currentUser));
        model.addAttribute("newProfileComment", new ProfileCommentDTO());
        model.addAttribute("currency", currency);
        model.addAttribute("usdToRub", currencyService.getUsdToRub());
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

    @PostMapping("/profile/{id}/comment")
    public String addProfileComment(@PathVariable Long id,
                                    @Valid @ModelAttribute("newProfileComment") ProfileCommentDTO form,
                                    BindingResult bindingResult,
                                    Authentication authentication) {
        User author = userService.findByUsername(authentication.getName());
        User profileOwner = userService.getUserById(id);
        if (bindingResult.hasErrors() || author.getId().equals(profileOwner.getId())) {
            return "redirect:/users/profile/" + id + "?error=invalid";
        }
        profileCommentService.addComment(author, profileOwner, form.getText(), form.getRating());
        return "redirect:/users/profile/" + id;
    }
}
