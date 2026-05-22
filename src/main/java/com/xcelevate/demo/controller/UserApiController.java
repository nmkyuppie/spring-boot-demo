package com.xcelevate.demo.controller;

import com.xcelevate.demo.entity.User;
import com.xcelevate.demo.model.UserRequest;
import com.xcelevate.demo.model.UserResponse;
import com.xcelevate.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }

    // READ: Get all users (GET)
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // READ: Get user by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(convertToResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE: Create new user (POST)
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .active(userRequest.getActive() != null ? userRequest.getActive() : true)
                .build();
        
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    // UPDATE: Full update (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setRole(userDetails.getRole());
                    if (userDetails.getActive() != null) {
                        user.setActive(userDetails.getActive());
                    }
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(convertToResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PARTIAL UPDATE: Toggle status (PATCH)
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<UserResponse> toggleUserStatus(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(!user.getActive());
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(convertToResponse(updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Delete user (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
