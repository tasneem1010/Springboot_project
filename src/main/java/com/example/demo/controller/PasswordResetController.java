package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class PasswordResetController {

    @PostMapping("/")
    public ResponseEntity sendUserEmail(@RequestBody Map<String, String> email) {
        try {
            ResponseEntity<ApiResponse> response = new RestTemplate().postForEntity("http://localhost:8081/", email, ApiResponse.class);
            return ApiResponse.buildResponse(response.getBody(),(HttpStatus)response.getStatusCode());
        }
        catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            return ApiResponse.buildResponse(parseError(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyOtp")
    public <T> ResponseEntity<ApiResponse<T>> verifyOtp(@RequestBody Map<String, String> otpRequest) {
        try {
            ResponseEntity<ApiResponse> response = new RestTemplate().postForEntity("http://localhost:8081/verifyOtp", otpRequest, ApiResponse.class);
            return ApiResponse.buildResponse(response.getBody(),(HttpStatus)response.getStatusCode());
        }
        catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            return ApiResponse.buildResponse(parseError(e), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody Map<String, String> resetPasswordRequest) {
        try {
            ResponseEntity<ApiResponse> response = new RestTemplate().postForEntity("http://localhost:8081/resetPassword", resetPasswordRequest, ApiResponse.class);
            return ApiResponse.buildResponse(response.getBody(),(HttpStatus)response.getStatusCode());
        }
        catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            return ApiResponse.buildResponse(parseError(e), HttpStatus.BAD_REQUEST);
        }
    }

    public <T> ApiResponse<T> parseError(HttpClientErrorException ex) {
        try {
            String body = ex.getResponseBodyAsString();
            JSONObject json = new JSONObject(body);
            ApiResponse<T> response = new ApiResponse<>();
            response.setMessage(json.optString("message", null));
            response.setSuccess(json.optBoolean("success", false));
            response.setData((T) json.optString("data", null));
            response.setTimeStamp(Instant.parse(json.optString("timeStamp", null))); // keep as string
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse error response", e);
        }
    }

}


