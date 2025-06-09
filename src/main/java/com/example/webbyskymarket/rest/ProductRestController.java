package com.example.webbyskymarket.rest;

import com.example.webbyskymarket.dto.NewProductDTO;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.service.ProductService;
import com.example.webbyskymarket.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "API для работы с продуктами")
public class ProductRestController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить все активные продукты")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продукт по ID")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID продукта") @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый продукт")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Данные нового продукта") @RequestBody NewProductDTO productDTO,
            @CurrentSecurityContext(expression = "authentication?.name") String username) {
        Product product = productService.createProduct(productDTO, username);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий продукт")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID продукта") @PathVariable Long id,
            @Parameter(description = "Обновленные данные продукта") @RequestBody NewProductDTO productDTO,
            @CurrentSecurityContext(expression = "authentication?.name") String username) {
        Product updatedProduct = productService.updateProduct(id, productDTO, username);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить продукт")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID продукта") @PathVariable Long id,
            @CurrentSecurityContext(expression = "authentication?.name") String username) {
        productService.deleteProduct(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск продуктов по названию")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Поисковый запрос") @RequestParam String query) {
        return ResponseEntity.ok(productService.searchProductsByName(query));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Получить продукты по категории")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Категория продукта") @PathVariable String category) {
        return ResponseEntity.ok(productService.getActiveProductsByCategory(category));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Получить продукты пользователя")
    public ResponseEntity<List<Product>> getProductsByUser(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        return ResponseEntity.ok(productService.getActiveProductsByUsername(username));
    }
} 