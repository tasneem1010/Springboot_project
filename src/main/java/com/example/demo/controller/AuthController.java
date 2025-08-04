package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;

import com.example.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody Map<String, String> loginRequest) {
       return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(HttpServletRequest request, @RequestBody User user) {
       try{
           return authService.register(request, user);
       } catch (Exception e) {
           System.out.println(e.getMessage());
           return ApiResponse.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,false ,e.getMessage(), null);
       }
    }
}
