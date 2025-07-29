package com.example.demo.repository;

import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("select new com.example.demo.dto.UserResponseDTO(u) from com.example.demo.model.User u where u.deleted = :deleted")
    Page<UserResponseDTO> findByDeleted(@Param("deleted") boolean deleted, Pageable pageable);

    User findById(int id);

    @Query("select new com.example.demo.dto.UserResponseDTO(u) from com.example.demo.model.User u where u.deleted = :deleted AND u.name = :name")
    Page<UserResponseDTO> findByNameAndDeleted(@Param("name") String name, @Param("deleted") Boolean deleted, Pageable pageable);

}