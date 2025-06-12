package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.models.Cart;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.CartService;
import com.example.webbyskymarket.service.CurrencyService;
import com.example.webbyskymarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;
    private final CurrencyService currencyService;
    private final UserService userService;

    @GetMapping("/cart")
    public String showCart(@CurrentSecurityContext(expression = "authentication?.name") String username, 
                          Model model,
                          @CookieValue(value = "currency", defaultValue = "USD") String currency) {
        User user = userService.findByUsername(username);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("usdToRub", currencyService.getUsdToRub());
        model.addAttribute("currency", currency);
        return "cart/cart";
    }

    @PostMapping("/cart/remove/{id}")
    @ResponseBody
    public String removeFromCart(@PathVariable Long id, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        cartService.removeFromCart(user.getId(), id);
        return "Item removed from cart";
    }

    @PostMapping("/cart/add/{id}")
    @ResponseBody
    public String addToCart(@PathVariable Long id, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        cartService.addToCart(user.getId(), id);
        return "Item added to cart";
    }

    @PostMapping("/cart/clear")
    @ResponseBody
    public String clearCart(@CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        cartService.clearCart(user.getId());
        return "Cart cleared";
    }

    @GetMapping("/cart/summary")
    @ResponseBody
    public Map<String, BigDecimal> getCartSummary(@CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userService.findByUsername(username);
        Cart cart = cartService.getOrCreateCart(user);
        BigDecimal subtotal = cart != null && cart.getProducts() != null ? 
            cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : 
            BigDecimal.ZERO;
            
        BigDecimal subtotalRub = subtotal.multiply(currencyService.getUsdToRub());
        
        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("subtotal", subtotal);
        summary.put("subtotalRub", subtotalRub);
        return summary;
    }
} 