package com.example.webbyskymarket.repository;

import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserUsername(String username);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(ProductCategory category);
}
