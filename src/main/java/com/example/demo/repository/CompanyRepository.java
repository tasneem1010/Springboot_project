package com.example.demo.repository;

import com.example.demo.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Page<Company> findByName(String name, Pageable pageable);

    Company findByName(String name);
    Company findById(int id);
}
