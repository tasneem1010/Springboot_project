package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
// For Paginated User Responses
public class UserListResponseDTO {
    private List<UserResponseDTO> users;
    private int totalPages;
    private long totalElements;

} 