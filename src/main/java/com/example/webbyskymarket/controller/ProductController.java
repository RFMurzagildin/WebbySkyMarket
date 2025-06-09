package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.dto.NewProductDTO;
import com.example.webbyskymarket.dto.ReviewDTO;
import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.enams.ProductCondition;
import com.example.webbyskymarket.enams.ProductStatus;
import com.example.webbyskymarket.models.Cart;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.service.CartService;
import com.example.webbyskymarket.service.ProductService;
import com.example.webbyskymarket.service.ReviewService;
import com.example.webbyskymarket.service.StorageService;
import com.example.webbyskymarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class ProductController {

    private final UserService userService;
    private final ProductService productService;
    private final StorageService storageService;
    private final CartService cartService;
    private final ReviewService reviewService;

    @GetMapping
    public String catalogPage(@CurrentSecurityContext(expression="authentication?.name") String username, Model model){
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        return "user/catalog";
    }

    @GetMapping("/new-product")
    public String newProductPage(Model model){
        model.addAttribute("newProductDto", new NewProductDTO());
        return "product/new-product";
    }

    @PostMapping("/new-product")
    public String addNewProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam("image1") MultipartFile image1,
            @RequestParam("image2") MultipartFile image2,
            @RequestParam("image3") MultipartFile image3,
            @RequestParam String category,
            @RequestParam String condition,
            @CurrentSecurityContext(expression = "authentication?.name") String username
    ) {
        User currentUser = userService.findByUsername(username);

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .imagePath1(imageUpload(image1))
                .imagePath2(imageUpload(image2))
                .imagePath3(imageUpload(image3))
                .category(ProductCategory.valueOf(category))
                .condition(ProductCondition.valueOf(condition))
                .user(currentUser)
                .status(ProductStatus.ACTIVE)
                .build();

        productService.saveProduct(product);

        return "redirect:/catalog?success=true";
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

    @GetMapping("/product/{id}")
    public String productDetails(
            @PathVariable Long id, 
            Model model, 
            @CurrentSecurityContext(expression="authentication?.name") String username,
            Authentication authentication
    ) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("authentication", authentication);
        
        if (username != null) {
            User user = userService.findByUsername(username);
            Cart cart = cartService.getOrCreateCart(user);
            model.addAttribute("cart", cart);
        }
        
        model.addAttribute("reviews", reviewService.getProductReviews(id));
        model.addAttribute("reviewDTO", new ReviewDTO());
        return "product/product-card";
    }

    @GetMapping("/search_by_product_name")
    public String searchProductsByName(@RequestParam String query, Model model, @CurrentSecurityContext(expression="authentication?.name") String username) {
        List<Product> products = productService.getProductsByName(query);
        model.addAttribute("products", products);
        User user = userService.findByUsername(username);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        return "product/search";
    }

    @GetMapping("/search_by_category")
    public String searchProductByCategory(@RequestParam String category, Model model, @CurrentSecurityContext(expression="authentication?.name") String username){
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        User user = userService.findByUsername(username);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        return "product/search";
    }
}
