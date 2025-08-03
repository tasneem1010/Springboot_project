package com.example.demo.repository;

import com.example.demo.dto.CompanyDTO;
import com.example.demo.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("select new com.example.demo.dto.CompanyDTO(c) from com.example.demo.model.Company c")
    Page<CompanyDTO> getAll(Pageable pageable);

    @Query("select new com.example.demo.dto.CompanyDTO(c) from com.example.demo.model.Company c where c.name = :name")
    Page<CompanyDTO> findByName(String name, Pageable pageable);

    Company findByName(String name);

    Company findById(int id);
}
