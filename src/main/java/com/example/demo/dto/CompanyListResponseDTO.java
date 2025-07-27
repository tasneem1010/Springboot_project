package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyListResponseDTO {
    List<CompanyResponseDTO> companies;
    int totalPages;
    int currentPage;
    int totalElements;

}
