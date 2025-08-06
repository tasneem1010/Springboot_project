package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class PasswordResetController {

    private final OtpService otpService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> sendUserEmail(@RequestParam String email) {
        return otpService.sendUserEmail(email);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody Map<String, String> otpRequest) {
            return otpService.validateOtp(otpRequest.get("email"),otpRequest.get("otp"));
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody Map<String, String> resetPasswordRequest ) {
        return userService.resetPassword(resetPasswordRequest.get("email"),resetPasswordRequest.get("password"));
    }

}
