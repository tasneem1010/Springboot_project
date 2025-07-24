package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(@RequestParam(required = false) String name, Pageable pageable) {
        // Return all users
        if (name == null) return userService.findAll(pageable);
        // Return name match
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
    public ResponseEntity<ApiResponse<User>> DeleteUser(@RequestBody User user) {
        return userService.delete(user);
    }
}
