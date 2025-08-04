package com.example.demo.repository;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("select new com.example.demo.dto.UserDTO(u) from User u where u.deleted = :deleted")
    Page<UserDTO> findByDeleted(@Param("deleted") boolean deleted, Pageable pageable);

    User findById(int id);

    @Query("select new com.example.demo.dto.UserDTO(u) from User u where u.deleted = :deleted AND u.name = :name")
    Page<UserDTO> findByNameAndDeleted(@Param("name") String name, @Param("deleted") Boolean deleted, Pageable pageable);

    @Query("select new com.example.demo.dto.UserDTO(u) from User u where u.company.id = :id AND u.status = :status")
    Page<UserDTO> findByCompanyAndStatus(@Param("id") int id, @Param("status") UserStatus status, Pageable pageable);
    @Query("select new com.example.demo.dto.UserDTO(u) from User u where u.company.id = :id")
    Page<UserDTO> findByCompany(int id, Pageable pageable);
    @Query("select count(u),u.status from User u where u.company.id = :id group by u.status")
    List<Object[]> countByCompanyAndStatus(@Param("id") int id);
}