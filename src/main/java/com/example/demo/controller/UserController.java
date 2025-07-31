package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CompanyResponseDTO;
import com.example.demo.dto.UserListResponseDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserListResponseDTO>> getUsers(@RequestParam(required = false) String name, Pageable pageable) {
        return userService.findUserByName(name, pageable);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@RequestParam Integer id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> deleteUser(@RequestParam Integer id) {
        return userService.delete(id);
    }

    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<UserListResponseDTO>> getDeletedUsers(Pageable pageable) {
        return userService.getDeletedUsers(pageable);
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String,String>>> getCurrentUser() {
        return userService.getCurrentUserّInfo();
    }
    @GetMapping("/me/company")
    public ResponseEntity<ApiResponse<String>> getCurrentUserCompany() {
        return userService.getCompanyName();
    }
}
