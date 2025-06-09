package com.example.webbyskymarket.repository;

import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.enams.ProductStatus;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserUsername(String username);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(ProductCategory category);
    @Query("SELECT p FROM Product p WHERE p.user.username = :username AND p.status = :status")
    List<Product> getProductsByUsernameAndStatus(@Param("username") String username, @Param("status") ProductStatus status);
    List<Product> findByNameContainingIgnoreCaseAndStatusEquals(String name, ProductStatus status);
    List<Product> findByCategoryAndStatusEquals(String category, ProductStatus status);
    List<Product> findByUserAndStatusEquals(User user, ProductStatus status);
}
