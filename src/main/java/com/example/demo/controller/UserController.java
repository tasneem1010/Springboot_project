package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserListResponseDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(@RequestParam(required = false) String name, Pageable pageable) {
        return userService.findUserByName(name, pageable);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<User>> deleteUser(@RequestParam Integer id) {
        return userService.delete(id);
    }
    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<Page<User>>> getDeletedUsers(Pageable pageable) {
        return userService.getDeletedUsers(pageable);
    }
}
