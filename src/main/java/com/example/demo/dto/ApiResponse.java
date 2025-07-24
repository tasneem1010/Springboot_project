package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    private String message;
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(message, data));
    }
}
