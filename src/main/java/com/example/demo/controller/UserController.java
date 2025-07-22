package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository usesrRepository) {
        this.userRepository = usesrRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        for(User user : userRepository.findAll()) {
            System.out.printf("User: %s \n", user);
        }
        return userRepository.findAll();
    }

}
