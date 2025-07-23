package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String password;
}

