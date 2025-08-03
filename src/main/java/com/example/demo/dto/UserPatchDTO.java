package com.example.demo.dto;

import com.example.demo.model.enums.UserStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@Data
public class UserPatchDTO {
    private Integer id;
    private String name;
    private String email;
    private Instant createdDate;
    private Instant updatedDate;
    private Boolean deleted;
    private String password;
    private UserStatus status;

}
