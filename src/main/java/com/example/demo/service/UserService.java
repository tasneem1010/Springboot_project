package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse<Page<User>>> findAll(Pageable pageable) {
        // Get all users that are not deleted
        Page<User> users = userRepository.findByDeleted(false,pageable);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", users);
    }

    public ResponseEntity<ApiResponse<User>> createUser(User user) {
        //TODO hash password
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Email", null);
        }
        if (emailExists(user))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "User Already Exists", userRepository.findByEmail(user.getEmail()));
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "User Added Successfully", userRepository.save(user));
    }
    public ResponseEntity<ApiResponse<User>> updateUser(User input) {
        // Check if user exists
        User existingUser = userRepository.findById(input.getId());
        if (existingUser == null) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", null);
        }
        // Validate input fields
        if (input.getEmail() == null || input.getEmail().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Email", null);
        }
        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Password", null);
        }
        if (input.getName() == null || input.getName().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Name", null);
        }
        // Check if email is being changed and if new email already exists for another user
        User userWithEmail = userRepository.findByEmail(input.getEmail());
        if (userWithEmail != null && userWithEmail.getId() != input.getId()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Email Already Exists", null);
        }
        // Update fields
        existingUser.setPassword(input.getPassword()); // TODO: hash password if needed
        existingUser.setName(input.getName());
        existingUser.setEmail(input.getEmail());
        userRepository.save(existingUser);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "User Updated Successfully", existingUser);
    }

    public ResponseEntity<ApiResponse<Page<User>>> findUserByName(String name, Pageable pageable) {
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", userRepository.findByNameAndDeleted(name, false, pageable));
    }

    public boolean emailExists(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }

    public boolean userExists(User user) {
        return userRepository.findById(user.getId()) != null;
    }

    public ResponseEntity<ApiResponse<User>> delete(int id) {
        User user = userRepository.findById(id);
        if (user != null) {
            if (user.isDeleted()) return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Already Deleted", user);
            user.setDeleted(true);
            userRepository.save(user);
            return ApiResponse.buildResponse(HttpStatus.OK, true, "User Was -Soft- Deleted", user);
        }
        return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", user);
    }
    public ResponseEntity<ApiResponse<Page<User>>> getDeletedUsers(Pageable pageable) {
        return ApiResponse.buildResponse(HttpStatus.OK,true,"Success",userRepository.findByDeleted(true, pageable));

    }
}
