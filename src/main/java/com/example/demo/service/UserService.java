package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserListResponseDTO;
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

    public ResponseEntity<ApiResponse<UserListResponseDTO>> findAll(Pageable pageable) {
        Page<User> users = userRepository.findByDeleted(false, pageable);
        UserListResponseDTO listDto = new UserListResponseDTO();
        listDto.setUsers(users.map(this::toUserResponseDTO).getContent());
        listDto.setTotalPages(users.getTotalPages());
        listDto.setTotalElements(users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", listDto);
    }

    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(UserRequestDTO userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null)
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "User Already Exists", null);
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setDeleted(false);
        User saved = userRepository.save(user);
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "User Added Successfully", toUserResponseDTO(saved));
    }

    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(int id, UserRequestDTO input) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", null);
        }
        if (input.getEmail() != null && !input.getEmail().isEmpty()) {
            User userWithEmail = userRepository.findByEmail(input.getEmail());
            if (userWithEmail != null && userWithEmail.getId() != id) {
                return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Email Already Exists", null);
            }
            existingUser.setEmail(input.getEmail());
        }
        if (input.getPassword() != null && !input.getPassword().isEmpty()) {
            existingUser.setPassword(input.getPassword());
        }
        if (input.getName() != null && !input.getName().isEmpty()) {
            existingUser.setName(input.getName());
        }
        userRepository.save(existingUser);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "User Updated Successfully", toUserResponseDTO(existingUser));
    }

    public ResponseEntity<ApiResponse<UserListResponseDTO>> findUserByName(String name, Pageable pageable) {
        Page<User> users = userRepository.findByNameAndDeleted(name, false, pageable);
        UserListResponseDTO listDto = new UserListResponseDTO();
        listDto.setUsers(users.map(this::toUserResponseDTO).getContent());
        listDto.setTotalPages(users.getTotalPages());
        listDto.setTotalElements(users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", listDto);
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
            if (user.isDeleted()) return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Already Deleted", toUserResponseDTO(user));
            user.setDeleted(true);
            userRepository.save(user);
            return ApiResponse.buildResponse(HttpStatus.OK, true, "User Was -Soft- Deleted", toUserResponseDTO(user));
        }
        return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", null);
    }

    public ResponseEntity<ApiResponse<UserListResponseDTO>> getDeletedUsers(Pageable pageable) {
        Page<User> users = userRepository.findByDeleted(true, pageable);
        UserListResponseDTO listDto = new UserListResponseDTO();
        listDto.setUsers(users.map(this::toUserResponseDTO).getContent());
        listDto.setTotalPages(users.getTotalPages());
        listDto.setTotalElements(users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", listDto);
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setUpdatedDate(user.getUpdatedDate());
        return dto;
    }
}
