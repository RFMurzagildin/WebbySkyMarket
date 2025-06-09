package com.example.webbyskymarket.repository;

import com.example.webbyskymarket.models.ProfileComment;
import com.example.webbyskymarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileCommentRepository extends JpaRepository<ProfileComment, Long> {
    List<ProfileComment> findByProfileOwnerOrderByCreatedAtDesc(User profileOwner);
} 