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

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse<Page<User>>> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return ApiResponse.buildResponse(HttpStatus.OK,true,"Success", users);
    }
    public ResponseEntity<ApiResponse<User>> createUser(User user) {
        //TODO hash password
        if (userExists(user))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT,false,"User Already Exists",user);
        return ApiResponse.buildResponse(HttpStatus.CREATED,true,"User Added Successfully",userRepository.save(user));
    }

    public ResponseEntity<ApiResponse<User>> updateUser(User user) {
        if (userExists(user)) return null;
        //TODO implement update user
        return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST,false,"User Does Not Exist",userRepository.save(user));
    }

    public ResponseEntity<ApiResponse<Page<User>>> findUserByName(String name, Pageable pageable) {
        return ApiResponse.buildResponse(HttpStatus.OK,true,"Success",userRepository.findByName(name, pageable));
    }

    public boolean userExists(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }
}
