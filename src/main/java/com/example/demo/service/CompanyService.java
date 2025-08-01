package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CompanyListResponseDTO;
import com.example.demo.dto.CompanyResponseDTO;
import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyListResponseDTO findAll(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        List<CompanyResponseDTO> companiesList = companies.getContent().stream().map(CompanyResponseDTO::new).toList();
        return new CompanyListResponseDTO(companiesList, companies.getTotalPages(), companies.getNumber(), (int) companies.getTotalElements());
    }

    public ResponseEntity<ApiResponse<CompanyListResponseDTO>> findCompanyByName(String name, Pageable pageable) {
        if (name == null) {
            return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", findAll(pageable));
        }
        Page<CompanyResponseDTO> companies = companyRepository.findByName(name, pageable);
        CompanyListResponseDTO liseDto = new CompanyListResponseDTO(companies.getContent(), companies.getTotalPages(), companies.getNumber(), (int) companies.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", liseDto);
    }

    public ResponseEntity<ApiResponse<CompanyResponseDTO>> createCompany(Company company) {
        if (company.getName() == null || company.getName().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Name", null);
        }
        if (nameExists(company.getName()))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "Company Name Already Exists", new CompanyResponseDTO(companyRepository.findByName(company.getName())));
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "Company Added Successfully", new CompanyResponseDTO(companyRepository.save(company)));
    }

    public boolean nameExists(String name) {
        return companyRepository.findByName(name) != null;
    }

    public ResponseEntity<ApiResponse<CompanyResponseDTO>> updateCompany(Company input) {
        // Check if company exists
        Company existingCompany = companyRepository.findById(input.getId());
        if (existingCompany == null) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Company Does Not Exist", null);
        }
        // Validate input fields
        if (input.getName() == null || input.getName().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Name", null);
        }
        // Check if name is being changed and if new name already exists for another company
        Company companyWithName = companyRepository.findByName(input.getName());
        if (companyWithName != null && companyWithName.getId() != input.getId()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Name Already Exists", null);
        }
        // Update fields
        existingCompany.setName(input.getName());
        companyRepository.save(existingCompany);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Company Updated Successfully", new CompanyResponseDTO(existingCompany));
    }

}
