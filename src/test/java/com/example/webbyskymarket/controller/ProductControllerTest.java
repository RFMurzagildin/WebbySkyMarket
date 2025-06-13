package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.enams.*;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.ProductRepository;
import com.example.webbyskymarket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setUsername("testuser7458");
        testUser.setPassword("Password123+");
        testUser.setEmail("test@example.com");
        testUser.setName("John");
        testUser.setSurname("Doe");
        testUser.setGender(Gender.MALE);
        testUser.setRole(Role.USER);
        testUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(testUser);

        Product product = new Product();
        product.setName("Test Laptop");
        product.setDescription("A great laptop");
        product.setPrice(BigDecimal.valueOf(999));
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStatus(ProductStatus.ACTIVE);
        product.setCondition(ProductCondition.NEW);
        product.setUser(testUser);

        product.setImagePath1("/images/laptop.jpg");
        productRepository.save(product);
    }

    @Test
    @WithMockUser(username = "testuser7458")
    void shouldReturnCatalogPageWithProducts() throws Exception {
        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/catalog"))
                .andExpect(model().attributeExists("products", "user", "cart", "currency"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Laptop")));
    }
}