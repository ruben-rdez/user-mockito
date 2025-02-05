package com.user.mockito.service;

import org.springframework.stereotype.Service;

import com.user.mockito.entity.User;
import com.user.mockito.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

}
