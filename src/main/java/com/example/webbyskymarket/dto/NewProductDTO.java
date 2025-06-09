package com.example.webbyskymarket.dto;

import com.example.webbyskymarket.enams.ProductCategory;
import com.example.webbyskymarket.enams.ProductCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class NewProductDTO {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "First image is required")
    private MultipartFile image1;

    private MultipartFile image2;

    private MultipartFile image3;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @NotNull(message = "Condition is required")
    private ProductCondition condition;
}
