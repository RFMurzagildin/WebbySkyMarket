package com.example.webbyskymarket.repository;

import com.example.webbyskymarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByIdAsc();

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.surname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findUsersByNameOrSurnameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    @Query("SELECT u FROM User u WHERE u.id BETWEEN :startId AND :endId " +
           "AND u.status = 'ACTIVE' " +
           "ORDER BY u.id DESC")
    List<User> findActiveUsersByIdRange(@Param("startId") Long startId, @Param("endId") Long endId);
}
