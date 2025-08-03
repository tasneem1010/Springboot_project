package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public ResponseEntity<ApiResponse<UserDTO>> login(Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

           UserDTO user = new UserDTO(userRepository.findByEmail(userDetails.getUsername()));
           String token = tokenManager.generateJwtToken(user);

            return ApiResponse.buildResponse(HttpStatus.OK,true, "Login successful with token: "+ token,user);
        } catch (BadCredentialsException ex) {
            return ApiResponse.buildResponse(HttpStatus.UNAUTHORIZED,false, "Invalid credentials", null);
        } catch (Exception ex) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST,false, ex.getMessage(), null);
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
