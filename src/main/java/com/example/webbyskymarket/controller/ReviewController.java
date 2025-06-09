package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.dto.ReviewDTO;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.Review;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.ProductService;
import com.example.webbyskymarket.service.ReviewService;
import com.example.webbyskymarket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/products/{productId}/reviews")
    public String addReview(
            @PathVariable Long productId,
            @Valid @ModelAttribute("reviewDTO") ReviewDTO reviewDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/products/" + productId + "?error=Invalid review";
        }

        Product product = productService.getProductById(productId);
        User user = userService.findByUsername(authentication.getName());

        reviewService.createReview(user, product, reviewDTO.getText());

        return "redirect:/catalog/product/" + productId;
    }

    @GetMapping("/products/{productId}/reviews")
    @ResponseBody
    public List<Review> getProductReviews(@PathVariable Long productId) {
        return reviewService.getProductReviews(productId);
    }
} 