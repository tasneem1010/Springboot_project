package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    private String message;
    private T data;
    boolean success;
    Instant timeStamp;
    public ApiResponse( boolean success, String message, T data) {
        this.message = message;
        this.data = data;
        this.success = success;
        this.timeStamp = Instant.now();
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, boolean success,String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(success,message, data));
    }
}
