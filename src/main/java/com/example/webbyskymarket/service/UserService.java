package com.example.webbyskymarket.service;

import com.example.webbyskymarket.dto.ProfileEditDTO;
import com.example.webbyskymarket.enams.Gender;
import com.example.webbyskymarket.enams.Role;
import com.example.webbyskymarket.enams.UserStatus;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(
            String name,
            String surname,
            String username,
            String password,
            Role role,
            Gender gender,
            String email
    ) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with this username already exists...");
        }

        User user = User.builder()
                .name(name)
                .surname(surname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .gender(gender)
                .status(UserStatus.ACTIVE)
                .email(email)
                .build();
        userRepository.save(user);
        return user;
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with this id doesn't exist"));
    }

    public List<User> getAllUsersSortedByIdAsc() {
        return userRepository.findAllByOrderByIdAsc();
    }


    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with this username doesn't exist"));
    }

    public void updateUserImage(String username, String imagePath){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with this username doesn't exist"));
        user.setImage(imagePath);
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void updateUserProfile(String username, ProfileEditDTO profileEditDTO) {
        User user = findByUsername(username);

        if (!user.getEmail().equals(profileEditDTO.getEmail()) && 
            userRepository.findByEmail(profileEditDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken");
        }

        if (profileEditDTO.getGender() == null) {
            throw new RuntimeException("Gender cannot be null");
        }

        user.setName(profileEditDTO.getName());
        user.setSurname(profileEditDTO.getSurname());
        user.setEmail(profileEditDTO.getEmail());
        user.setPhoneNumber(profileEditDTO.getPhoneNumber());
        user.setGender(profileEditDTO.getGender());

        userRepository.save(user);
    }

    public List<User> searchUsersByNameOrSurname(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        return userRepository.findUsersByNameOrSurnameContainingIgnoreCase(searchTerm.trim());
    }

    public List<User> findActiveUsersInIdRange(Long startId, Long endId) {
        if (startId == null || endId == null || startId > endId) {
            return List.of();
        }
        return userRepository.findActiveUsersByIdRange(startId, endId);
    }
}
