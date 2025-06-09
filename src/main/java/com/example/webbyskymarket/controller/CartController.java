package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.models.Cart;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.CartService;
import com.example.webbyskymarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String viewCart(@CurrentSecurityContext(expression = "authentication?.name") String username, Model model) {
        User user = userService.findByUsername(username);
        Cart cart = cartService.getOrCreateCart(user);
        cart.setProducts(cart.getProducts().stream()
            .filter(product -> product.getStatus() == com.example.webbyskymarket.enams.ProductStatus.ACTIVE)
            .toList());
        model.addAttribute("cart", cart);
        return "cart/cart";
    }

    @PostMapping("/add/{productId}")
    @ResponseBody
    public Map<String, Object> addToCart(@PathVariable Long productId, 
                          @CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        boolean success = cartService.addToCart(user.getId(), productId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Product added to cart" : "Product is already in cart");
        return response;
    }

    @PostMapping("/remove/{productId}")
    @ResponseBody
    public Map<String, Object> removeFromCart(@PathVariable Long productId,
                               @CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        boolean success = cartService.removeFromCart(user.getId(), productId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Product removed from cart" : "Product was not in cart");
        return response;
    }

    @PostMapping("/clear")
    @ResponseBody
    public String clearCart(@CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        cartService.clearCart(user.getId());
        return "Cart cleared";
    }
} 