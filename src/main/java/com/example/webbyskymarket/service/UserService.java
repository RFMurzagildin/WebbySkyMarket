package com.example.webbyskymarket.service;

import com.example.webbyskymarket.enams.Gender;
import com.example.webbyskymarket.enams.Role;
import com.example.webbyskymarket.enams.UserStatus;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void deleteUser(String username){
        userRepository.delete(findByUsername(username));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
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
}
