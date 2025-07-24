package com.example.demo.dto;

import lombok.Data;

@Data
// For User Update Requests
public class UserUpdateDTO {
    // Optional fields
    private String name;
    private String email;
    private String password;
} 