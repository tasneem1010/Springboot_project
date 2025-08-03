package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserListDTO;
import com.example.demo.dto.UserPatchDTO;
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
    public ResponseEntity<ApiResponse<UserListDTO>> getUsers(@RequestParam(required = false) String name, Pageable pageable) {
        return userService.findUserByName(name, pageable);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> patchUser(@PathVariable int id,@RequestBody UserPatchDTO user) {
        return userService.patchUser(id, user);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser( @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestParam Integer id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@RequestParam Integer id) {
        return userService.delete(id);
    }

    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<UserListDTO>> getDeletedUsers(Pageable pageable) {
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
