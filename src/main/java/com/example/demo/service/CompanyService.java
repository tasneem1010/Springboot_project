package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Company;
import com.example.demo.model.enums.UserStatus;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
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
    private final UserRepository userRepository;

    public CompanyListDTO findAll(Pageable pageable) {
        Page<CompanyDTO> companies = companyRepository.getAll(pageable);
        return new CompanyListDTO(companies.getContent(), companies.getTotalPages(), companies.getNumber(), (int) companies.getTotalElements());
    }

    public ResponseEntity<ApiResponse<CompanyListDTO>> findCompanyByName(String name, Pageable pageable) {
        if (name == null) {
            return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", findAll(pageable));
        }
        Page<CompanyDTO> companies = companyRepository.findByName(name, pageable);
        CompanyListDTO liseDto = new CompanyListDTO(companies.getContent(), companies.getTotalPages(), companies.getNumber(), (int) companies.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", liseDto);
    }

    public ResponseEntity<ApiResponse<CompanyDTO>> createCompany(Company company) {
        if (company.getName() == null || company.getName().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Name", null);
        }
        if (nameExists(company.getName()))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "Company Name Already Exists", new CompanyDTO(companyRepository.findByName(company.getName())));
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "Company Added Successfully", new CompanyDTO(companyRepository.save(company)));
    }

    public boolean nameExists(String name) {
        return companyRepository.findByName(name) != null;
    }

    public ResponseEntity<ApiResponse<CompanyDTO>> updateCompany(Company input) {
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
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Company Updated Successfully", new CompanyDTO(existingCompany));
    }

    public ResponseEntity<ApiResponse<UserListDTO>> getUsersByStatus(int id, UserStatus status, Pageable pageable) {
        Page<UserDTO> users;
        if (status == null) {
            users = userRepository.findByCompany(id, pageable);
        } else {
            users = userRepository.findByCompanyAndStatus(id, status, pageable);
        }
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", new UserListDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements()));
    }
    public ResponseEntity<ApiResponse<List<Object[]>>> countByCompanyAndStatus(int id){
        return ApiResponse.buildResponse(HttpStatus.OK,true,"Success",userRepository.countByCompanyAndStatus(id));
    }
}
