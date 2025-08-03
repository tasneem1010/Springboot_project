package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyListDTO {
    List<CompanyDTO> companies;
    int totalPages;
    int currentPage;
    int totalElements;

}
