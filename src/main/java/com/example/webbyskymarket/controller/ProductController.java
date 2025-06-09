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
import com.example.webbyskymarket.service.CurrencyService;
import com.example.webbyskymarket.service.ProductService;
import com.example.webbyskymarket.service.ReviewService;
import com.example.webbyskymarket.service.StorageService;
import com.example.webbyskymarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final CurrencyService currencyService;

    @GetMapping
    public String catalogPage(@CurrentSecurityContext(expression="authentication?.name") String username, Model model,
                             @CookieValue(value = "currency", defaultValue = "USD") String currency) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("currency", currency);
        model.addAttribute("usdToRub", currencyService.getUsdToRub());
        return "user/catalog";
    }

    @GetMapping("/new-product")
    public String newProductPage(Model model){
        model.addAttribute("newProductDTO", new NewProductDTO());
        return "product/new-product";
    }

    @PostMapping("/new-product")
    public String addNewProduct(
            @ModelAttribute NewProductDTO newProductDTO,
            BindingResult bindingResult,
            @CurrentSecurityContext(expression = "authentication?.name") String username,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the errors below.");
            return "product/new-product";
        }

        User currentUser = userService.findByUsername(username);

        Product product = Product.builder()
                .name(newProductDTO.getName())
                .description(newProductDTO.getDescription())
                .price(newProductDTO.getPrice())
                .imagePath1(imageUpload(newProductDTO.getImage1()))
                .imagePath2(imageUpload(newProductDTO.getImage2()))
                .imagePath3(imageUpload(newProductDTO.getImage3()))
                .category(newProductDTO.getCategory())
                .condition(newProductDTO.getCondition())
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
            Authentication authentication,
            @CookieValue(value = "currency", defaultValue = "USD") String currency
    ) {
        Product product = productService.getProductById(id);
        if (product.getStatus() != ProductStatus.ACTIVE) {
            return "redirect:/catalog?error=unavailable";
        }
        model.addAttribute("product", product);
        model.addAttribute("authentication", authentication);
        if (username != null) {
            User user = userService.findByUsername(username);
            Cart cart = cartService.getOrCreateCart(user);
            model.addAttribute("cart", cart);
        }
        model.addAttribute("currency", currency);
        model.addAttribute("usdToRub", currencyService.getUsdToRub());
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

    @PostMapping("/product/{id}/deactivate")
    public String deactivateProduct(@PathVariable Long id, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        Product product = productService.getProductById(id);
        if (product.getUser().getUsername().equals(username)) {
            product.setStatus(ProductStatus.INACTIVE);
            productService.saveProduct(product);
        }
        return "redirect:/users/profile";
    }

    @PostMapping("/product/{id}/sold")
    public String markProductAsSold(@PathVariable Long id, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        Product product = productService.getProductById(id);
        if (product.getUser().getUsername().equals(username)) {
            product.setStatus(ProductStatus.SOLD);
            productService.saveProduct(product);
        }
        return "redirect:/users/profile";
    }

    @GetMapping("/api/products")
    @ResponseBody
    public Page<Product> getActiveProductsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productService.findByStatus(ProductStatus.ACTIVE, pageable);
    }
}
