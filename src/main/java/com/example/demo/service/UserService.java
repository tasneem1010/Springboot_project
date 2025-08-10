package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserListDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPatchDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;


    public UserListDTO findAll(Pageable pageable) {
        // Get all users that are not deleted
        Page<UserDTO> users = userRepository.findByDeleted(false, pageable);
        return new UserListDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements());
    }

    public ResponseEntity<ApiResponse<UserDTO>> createUser(User user) {
        //TODO hash password
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Enter a Valid Email", null);
        }
        if (emailExists(user))
            return ApiResponse.buildResponse(HttpStatus.CONFLICT, false, "User Already Exists", new UserDTO(userRepository.findByEmail(user.getEmail())));
        return ApiResponse.buildResponse(HttpStatus.CREATED, true, "User Added Successfully", new UserDTO(userRepository.save(user)));
    }

    public ResponseEntity<ApiResponse<UserDTO>> updateUser(int id, User input) {
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
        existingUser.setPassword(passwordEncoder.encode(input.getPassword()));
        existingUser.setName(input.getName());
        existingUser.setEmail(input.getEmail());
        userRepository.save(existingUser);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "User Updated Successfully", new UserDTO(existingUser));
    }

    public ResponseEntity<ApiResponse<UserListDTO>> findUserByName(String name, Pageable pageable) {
        if (name == null) {
            return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", findAll(pageable));
        }
        Page<UserDTO> users = userRepository.findByNameAndDeleted(name, false, pageable);
        UserListDTO liseDto = new UserListDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", liseDto);
    }

    public boolean emailExists(User user) {
        return userRepository.findByEmail(user.getEmail()) != null;
    }

    public boolean userExists(User user) {
        return userRepository.findById(user.getId()) != null;
    }

    public ResponseEntity<ApiResponse<UserDTO>> delete(int id) {
        User user = userRepository.findById(id);
        if (user != null) {
            if (user.isDeleted())
                return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Already Deleted", null);
            user.setDeleted(true);
            userRepository.save(user);
            return ApiResponse.buildResponse(HttpStatus.OK, true, "User Was -Soft- Deleted", new UserDTO(user));
        }
        return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", new UserDTO(user));
    }

    public ResponseEntity<ApiResponse<UserListDTO>> getDeletedUsers(Pageable pageable) {
        Page<UserDTO> users = userRepository.findByDeleted(true, pageable);
        UserListDTO liseDto = new UserListDTO(users.getContent(), users.getTotalPages(), users.getNumber(), (int) users.getTotalElements());
        return ApiResponse.buildResponse(HttpStatus.OK, true, "Success", liseDto);
    }

    public ResponseEntity<ApiResponse<Map<String, String>>> getCurrentUserّInfo() {
        Map<String,String> map = new HashMap<>();
        UserDTO userDetails = getCurrentUser();
        map.put("id", String.valueOf(userDetails.getId()));
        map.put("email",userDetails.getEmail());
        return ApiResponse.buildResponse(HttpStatus.OK,true,"User Info",map);
    }
    public UserDTO getCurrentUser(){
        return (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ResponseEntity<ApiResponse<String>> getCompanyName() {
        return ApiResponse.buildResponse(HttpStatus.OK,true,"not yet implemented",null);
    }

    public ResponseEntity<ApiResponse<UserDTO>> patchUser(int id, UserPatchDTO patchDTO) {
        // fetch existing user
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "User Does Not Exist", null);
        }
        // validate if email is changing and already exists for another user
        if (patchDTO.getEmail() != null) {
            User userWithEmail = userRepository.findByEmail(patchDTO.getEmail());
            if (userWithEmail != null && userWithEmail.getId() != id) {
                return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "Email Already Exists", null);
            }
            existingUser.setEmail(patchDTO.getEmail());
        }
        if (patchDTO.getName() != null) {
            existingUser.setName(patchDTO.getName());
        }
        if (patchDTO.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(patchDTO.getPassword()));
        }
        if (patchDTO.getCreatedDate() != null) {
            existingUser.setCreatedDate(patchDTO.getCreatedDate());
        }
        if (patchDTO.getUpdatedDate() != null) {
            existingUser.setUpdatedDate(patchDTO.getUpdatedDate());
        }
        if (patchDTO.getDeleted() != null) {
            existingUser.setDeleted(patchDTO.getDeleted());
        }
        if(patchDTO.getStatus() != null) {
            existingUser.setStatus(patchDTO.getStatus());
            System.out.println(existingUser.getStatus());
        }
        userRepository.save(existingUser);
        return ApiResponse.buildResponse(HttpStatus.OK, true, "User Updated Successfully", new UserDTO(existingUser));
    }

}
