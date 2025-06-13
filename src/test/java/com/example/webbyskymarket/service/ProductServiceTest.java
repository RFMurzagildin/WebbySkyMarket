package com.example.webbyskymarket.service;

import com.example.webbyskymarket.dto.NewProductDTO;
import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.enams.ProductStatus;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;


    @Test
    void shouldCreateProduct_whenValidDataProvided() {
        NewProductDTO dto = new NewProductDTO();
        dto.setName("Laptop");
        dto.setDescription("Used laptop");
        dto.setPrice(BigDecimal.valueOf(300));
        dto.setCategory(ProductCategory.ELECTRONICS);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.getUserByUsername("testUser")).thenReturn(user);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product createdProduct = productService.createProduct(dto, "testUser");

        assertThat(createdProduct.getName()).isEqualTo("Laptop");
        assertThat(createdProduct.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        verify(productRepository, times(1)).save(createdProduct);
    }

    @Test
    void shouldThrowException_whenUpdatingProductWithWrongUser() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        User owner = new User();
        owner.setId(100L);
        existingProduct.setUser(owner);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        User wrongUser = new User();
        wrongUser.setId(200L);
        when(userService.getUserByUsername("wrongUser")).thenReturn(wrongUser);
        NewProductDTO dto = new NewProductDTO();
        dto.setName("Updated Name");
        assertThatThrownBy(() -> productService.updateProduct(1L, dto, "wrongUser"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("You can't update this product");
    }

    @Test
    void shouldReturnActiveProductsByCategory() {
        ProductCategory category = ProductCategory.FURNITURE;
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findByCategoryAndStatusEquals(category.name(), ProductStatus.ACTIVE))
                .thenReturn(products);
        List<Product> result = productService.getActiveProductsByCategory(category.name());
        assertThat(result).hasSize(2);
        verify(productRepository, times(1)).findByCategoryAndStatusEquals(category.name(), ProductStatus.ACTIVE);
    }

    @Test
    void shouldFindAllActiveProducts() {
        List<Product> mockedList = List.of(new Product(), new Product());
        when(productRepository.findRandomActiveProducts(20)).thenReturn(mockedList);
        List<Product> activeProducts = productService.getAllActiveProducts();
        assertThat(activeProducts).hasSize(2);
        verify(productRepository, times(1)).findRandomActiveProducts(20);
    }

    @Test
    void shouldFindByStatusWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        when(productRepository.findByStatus(ProductStatus.ACTIVE, pageable)).thenReturn(page);
        Page<Product> result = productService.findByStatus(ProductStatus.ACTIVE, pageable);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(productRepository, times(1)).findByStatus(ProductStatus.ACTIVE, pageable);
    }
}