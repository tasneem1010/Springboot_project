package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ApiResponse<Map<String, String>>> login(Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(email);

            String token = tokenManager.generateJwtToken(userDetails, (long) user.getId());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid credentials", null));
        }
    }

    public ResponseEntity<ApiResponse<String>> register(User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "User already exists", null));
            }

            // encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", "User ID: " + savedUser.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Registration failed", null));
        }
    }
}
