package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CompanyListDTO;
import com.example.demo.dto.CompanyDTO;
import com.example.demo.model.Company;
import com.example.demo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<ApiResponse<CompanyListDTO>> getCompanies(@RequestParam(required = false) String name, Pageable pageable) {
        return companyService.findCompanyByName(name, pageable);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyDTO>> createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CompanyDTO>> updateCompany(@RequestBody Company company) {
        return companyService.updateCompany(company);
    }
}
