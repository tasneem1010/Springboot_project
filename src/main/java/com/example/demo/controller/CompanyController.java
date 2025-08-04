package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CompanyListDTO;
import com.example.demo.dto.CompanyDTO;
import com.example.demo.dto.UserListDTO;
import com.example.demo.model.Company;
import com.example.demo.model.enums.UserStatus;
import com.example.demo.service.CompanyService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> updateCompany(@RequestParam int id, @RequestBody Company company) {
        return companyService.updateCompany(id,company);
    }
    @GetMapping("/{id}/users")
    public ResponseEntity<ApiResponse<UserListDTO>> getUsersByStatus(@PathVariable int id, @RequestParam(required = false) UserStatus status, Pageable pageable) {
        return companyService.getUsersByStatus(id,status,pageable);
    }
    @GetMapping("{id}/statusCounts")
    public ResponseEntity<ApiResponse<List<Object[]>>> countByCompanyAndStatus(@PathVariable int id) {
        return companyService.countByCompanyAndStatus(id);
    }
}
