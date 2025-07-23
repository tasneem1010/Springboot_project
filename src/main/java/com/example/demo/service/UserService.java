package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public User createUser(User user) {
        //TODO hash password
        //TODO return custom response?
        if(userExists(user))return null;
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if(userExists(user))return null;
        //TODO implement update user
        return user;
    }

    public Page<User> findUserByName(String name, Pageable pageable) {
        return userRepository.findByName(name, pageable);
    }
    public boolean userExists(User user) {
        return userRepository.findByEmail(user.getEmail())!=null;
    }
}
