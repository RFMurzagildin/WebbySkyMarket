package com.example.webbyskymarket.service;

import com.example.webbyskymarket.models.ProfileComment;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.ProfileCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileCommentService {
    private final ProfileCommentRepository profileCommentRepository;

    public List<ProfileComment> getCommentsForUser(User profileOwner) {
        return profileCommentRepository.findByProfileOwnerOrderByCreatedAtDesc(profileOwner);
    }

    public ProfileComment addComment(User author, User profileOwner, String text, int rating) {
        ProfileComment comment = ProfileComment.builder()
                .author(author)
                .profileOwner(profileOwner)
                .text(text)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .build();
        return profileCommentRepository.save(comment);
    }
} 