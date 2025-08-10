package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class PasswordResetController {

    @PostMapping("/")
    public ResponseEntity<ApiResponse> sendUserEmail(@RequestBody Map<String,String> email) {
        return new RestTemplate().postForEntity("http://localhost:8081/",email,ApiResponse.class);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody Map<String, String> otpRequest) {
        return new RestTemplate().postForEntity("http://localhost:8081/verifyOtp",otpRequest,ApiResponse.class);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> resetPasswordRequest) {
        return new RestTemplate().postForEntity("http://localhost:8081/resetPassword",resetPasswordRequest,ApiResponse.class);

    }
}
