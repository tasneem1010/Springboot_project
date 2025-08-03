package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    List<UserDTO> users;

    int currentPage;
    int totalPages;
    int totalElements;
}
