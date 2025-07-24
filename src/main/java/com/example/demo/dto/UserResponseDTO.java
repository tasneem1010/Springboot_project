package com.example.demo.dto;

import lombok.Data;

import java.time.Instant;

@Data
// For User Response
public class UserResponseDTO {
    private int id;
    private String name;
    private String email;
    private Instant createdDate;
    private Instant updatedDate;
}