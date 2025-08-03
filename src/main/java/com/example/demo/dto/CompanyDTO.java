package com.example.demo.dto;

import com.example.demo.model.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    int id;
    String name;

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
    }
}
