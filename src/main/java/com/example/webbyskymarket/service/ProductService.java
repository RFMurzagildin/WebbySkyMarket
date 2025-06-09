package com.example.webbyskymarket.service;

import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.enams.ProductStatus;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProductsByUsername(String username) {
        return productRepository.findByUserUsername(username);
    }

    public List<Product> getProductsByName(String productName){
        return productRepository.findByNameContainingIgnoreCase(productName);
    }

    public List<Product> getProductsByCategory(String categoryName){
        ProductCategory category = ProductCategory.valueOf(categoryName.toUpperCase().replace(" ", "_"));
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByUsernameAndStatus(String username, ProductStatus productStatus){
        return productRepository.getProductsByUsernameAndStatus(username, productStatus);
    }
}