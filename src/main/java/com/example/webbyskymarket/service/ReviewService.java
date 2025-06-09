package com.example.webbyskymarket.service;

import com.example.webbyskymarket.models.Review;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.models.Product;
import com.example.webbyskymarket.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review createReview(User user, Product product, String text) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setText(text);
        return reviewRepository.save(review);
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }
}