package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CompanyListResponseDTO;
import com.example.demo.dto.CompanyResponseDTO;
import com.example.demo.dto.UserListResponseDTO;
import com.example.demo.model.Company;
import com.example.demo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<ApiResponse<CompanyListResponseDTO>> getCompanies(@RequestParam(required = false) String name, Pageable pageable) {
        return companyService.findCompanyByName(name, pageable);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<ApiResponse<UserListResponseDTO>> getCompanies(@PathVariable int id, Pageable pageable) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> updateCompany(@RequestBody Company company) {
        return companyService.updateCompany(company);
    }
}
