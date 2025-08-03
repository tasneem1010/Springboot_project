package com.example.demo.dto;

import com.example.demo.model.User;
import com.example.demo.model.enums.UserStatus;
import com.example.demo.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private Instant createdDate;
    private Instant updatedDate;
    private boolean deleted;
    private UserStatus status;


    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();
        this.deleted = user.isDeleted();
        this.status = user.getStatus();
    }
    public UserDTO(Claims claims) {
        this.id = (Integer) claims.get("id");
        this.name = (String) claims.get("name");
        this.email = (String) claims.get("email");
        String createdDateStr = (String) claims.get("createdDate");
        String updatedDateStr = (String) claims.get("updatedDate");
        this.createdDate = createdDateStr != null ? Instant.parse(createdDateStr) : null;
        this.updatedDate = updatedDateStr != null ? Instant.parse(updatedDateStr) : null;
        this.deleted = (Boolean) claims.get("deleted");
        // TODO add company to claims
    }

}
