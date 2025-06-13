package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.dto.ProfileCommentDTO;
import com.example.webbyskymarket.dto.ProfileEditDTO;
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
import java.util.ArrayList;
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
        ProfileEditDTO profileEditDTO = new ProfileEditDTO();
        profileEditDTO.setName(user.getName());
        profileEditDTO.setSurname(user.getSurname());
        profileEditDTO.setEmail(user.getEmail());
        profileEditDTO.setPhoneNumber(user.getPhoneNumber());
        profileEditDTO.setGender(user.getGender());
        
        model.addAttribute("user", user);
        model.addAttribute("profileEditDTO", profileEditDTO);
        return "user/edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @Valid @ModelAttribute("profileEditDTO") ProfileEditDTO profileEditDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("user", user);
            return "user/edit";
        }

        try {
            userService.updateUserProfile(authentication.getName(), profileEditDTO);
            return "redirect:/users/profile?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("user", user);
            return "user/edit";
        }
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long startId,
            @RequestParam(required = false) Long endId,
            Model model
    ) {
        List<User> searchResults = new ArrayList<>();
        
        if (query != null && !query.trim().isEmpty()) {
            searchResults.addAll(userService.searchUsersByNameOrSurname(query));
        }
        
        if (startId != null && endId != null) {
            searchResults.addAll(userService.findActiveUsersInIdRange(startId, endId));
        }
        
        model.addAttribute("users", searchResults);
        model.addAttribute("searchQuery", query);
        model.addAttribute("startId", startId);
        model.addAttribute("endId", endId);
        
        return "user/search-results";
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