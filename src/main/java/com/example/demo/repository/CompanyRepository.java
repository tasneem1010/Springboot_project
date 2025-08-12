package com.example.demo.repository;

import com.example.demo.dto.CompanyDTO;
import com.example.demo.dto.CompanyWCountDTO;
import com.example.demo.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("select new com.example.demo.dto.CompanyDTO(c) from Company c")
    Page<CompanyDTO> getAll(Pageable pageable);

    @Query("select new com.example.demo.dto.CompanyDTO(c) from Company c where c.name = :name")
    Page<CompanyDTO> findByName(String name, Pageable pageable);

    Company findByName(String name);

    Company findById(int id);

    // can be replaced with dto
    // use left join in case all companies don't have users
    @Query("select new com.example.demo.dto.CompanyWCountDTO(c.id, c.name, count(u)) " +
            "from Company c " +
            "left join User u on c.id = u.company.id " +
            "group by c.id,c.name " +
            "order by count(u) desc " +
            "limit 1")
    CompanyWCountDTO findBiggestCompany();

//     @Query("SELECT new com.example.demo.dto.CompanyDTO(c) FROM Company c LEFT JOIN User u ON c.id = u.company.id WHERE u.id IS NULL") // this is closer to native sql
    @Query("select new com.example.demo.dto.CompanyDTO(c) from Company c where c.users is empty") // made simpler using hql
    List<CompanyDTO> findByNoUsers();
}
