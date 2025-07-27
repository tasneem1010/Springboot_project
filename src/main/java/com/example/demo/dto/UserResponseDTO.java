package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    int id;
    String name;
    String email;
    Instant created_date;
    Instant updated_date;

    public UserResponseDTO(User user) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setName(user.getName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setCreated_date(user.getCreatedDate());
            userResponseDTO.setUpdated_date(user.getUpdatedDate());
    }
}
