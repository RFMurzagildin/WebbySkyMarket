package com.example.webbyskymarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDTO {
    @NotBlank(message = "Review text cannot be empty")
    @Size(min = 1, max = 1000, message = "Review must be between 1 and 1000 characters")
    private String text;
}