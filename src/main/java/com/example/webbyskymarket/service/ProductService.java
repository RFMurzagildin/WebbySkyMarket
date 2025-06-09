package com.example.webbyskymarket.service;

import com.example.webbyskymarket.dto.NewProductDTO;
import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.enams.ProductStatus;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;

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

    public Product createProduct(NewProductDTO productDTO, String username) {
        User user = userService.getUserByUsername(username);
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setCondition(productDTO.getCondition());
        product.setUser(user);
        product.setStatus(ProductStatus.ACTIVE);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, NewProductDTO productDTO, String username) {
        Product product = getProductById(id);
        User user = userService.getUserByUsername(username);
        
        if (!product.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can't update this product");
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setCondition(productDTO.getCondition());
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id, String username) {
        Product product = getProductById(id);
        User user = userService.getUserByUsername(username);
        
        if (!product.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can't delete this product");
        }

        productRepository.delete(product);
    }

    public List<Product> searchProductsByName(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndStatusEquals(query, ProductStatus.ACTIVE);
    }

    public List<Product> getActiveProductsByCategory(String category) {
        return productRepository.findByCategoryAndStatusEquals(category, ProductStatus.ACTIVE);
    }

    public List<Product> getActiveProductsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return productRepository.findByUserAndStatusEquals(user, ProductStatus.ACTIVE);
    }

    public Page<Product> findByStatus(ProductStatus status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable);
    }
}