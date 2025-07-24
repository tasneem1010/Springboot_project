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
        if (userExistsByEmail(user))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "User Already Exists", userRepository.findByEmail(user.getEmail()));
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "User Added Successfully", userRepository.save(user));
    }

    public ResponseEntity<ApiResponse<User>> updateUser(User user) {
        if (!userExists(user)) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", userRepository.save(user));
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Email", null);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Password", null);
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Name", null);
        }
        User res = userRepository.findById(user.getId());
        if (userExistsByEmail(user)) return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST,false,"Email Already Exists",null); //return user?
        res.setPassword(user.getPassword());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        userRepository.save(res);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "User Updated Successfully", res);

    }

    public ResponseEntity<ApiResponse<Page<User>>> findUserByName(String name, Pageable pageable) {
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", userRepository.findByName(name, pageable));
    }

    public boolean userExistsByEmail(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }

    public boolean userExists(User user) {
        return userRepository.findById(user.getId()) != null;
    }

    public ResponseEntity<ApiResponse<User>> delete(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Email", null);
        }
        if (userExistsByEmail(user)) {
            userRepository.delete(user);
            return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", user);
        }
        return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", user);
    }
}
