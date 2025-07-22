package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }
    public static String hashPassword(String plainPassword) {
        //TODO hash password
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder.encode(plainPassword);
        return plainPassword;
    }


}
