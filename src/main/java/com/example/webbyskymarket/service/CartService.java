package com.example.webbyskymarket.service;

import com.example.webbyskymarket.models.Cart;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public boolean addToCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        Product product = productService.getProductById(productId);

        if (cart.getProducts().contains(product)) {
            return false;
        }
        
        cart.getProducts().add(product);
        cartRepository.save(cart);
        return true;
    }

    @Transactional
    public boolean removeFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        Product product = productService.getProductById(productId);
        
        boolean removed = cart.getProducts().remove(product);
        if (removed) {
            cartRepository.save(cart);
        }
        return removed;
    }

    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .user(User.builder().id(userId).build())
                            .build();
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getProducts().clear();
        cartRepository.save(cart);
    }
} 