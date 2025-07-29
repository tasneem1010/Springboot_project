package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserListResponseDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserListResponseDTO findAll(Pageable pageable) {
        // Get all users that are not deleted
        Page<UserResponseDTO> users = userRepository.findByDeleted(false, pageable);
        return new UserListResponseDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements());
    }

    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(User user) {
        //TODO hash password
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Email", null);
        }
        if (emailExists(user))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "User Already Exists", new UserResponseDTO(userRepository.findByEmail(user.getEmail())));
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "User Added Successfully", new UserResponseDTO(userRepository.save(user)));
    }

    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(int id, User input) {
        // Check if user exists
        User existingUser = userRepository.findById(id);
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
        if (userWithEmail != null && userWithEmail.getId() != id) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Email Already Exists", null);
        }
        // Update fields
        existingUser.setPassword(input.getPassword()); // TODO: hash password if needed
        existingUser.setName(input.getName());
        existingUser.setEmail(input.getEmail());
        userRepository.save(existingUser);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "User Updated Successfully", new UserResponseDTO(existingUser));
    }

    public ResponseEntity<ApiResponse<UserListResponseDTO>> findUserByName(String name, Pageable pageable) {
        if (name == null) {
            return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", findAll(pageable));
        }
        Page<UserResponseDTO> users = userRepository.findByNameAndDeleted(name, false, pageable);
        UserListResponseDTO liseDto = new UserListResponseDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", liseDto);
    }

    public boolean emailExists(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }

    public boolean userExists(User user) {
        return userRepository.findById(user.getId()) != null;
    }

    public ResponseEntity<ApiResponse<UserResponseDTO>> delete(int id) {
        User user = userRepository.findById(id);
        if (user != null) {
            if (user.isDeleted())
                return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Already Deleted", null);
            user.setDeleted(true);
            userRepository.save(user);
            return ApiResponse.buildResponse(HttpStatus.OK, true, "User Was -Soft- Deleted", new UserResponseDTO(user));
        }
        return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", new UserResponseDTO(user));
    }

    public ResponseEntity<ApiResponse<UserListResponseDTO>> getDeletedUsers(Pageable pageable) {
        Page<UserResponseDTO> users = userRepository.findByDeleted(true, pageable);
        UserListResponseDTO liseDto = new UserListResponseDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", liseDto);
    }
}
