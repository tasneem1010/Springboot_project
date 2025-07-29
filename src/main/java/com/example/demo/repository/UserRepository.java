package com.example.demo.repository;

import com.example.demo.dto.UserListResponseDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Page<User> findByDeleted(boolean deleted, Pageable pageable);
    User findById(int id);
    Page<User> findByNameAndDeleted(String name, Boolean deleted, Pageable pageable);

}