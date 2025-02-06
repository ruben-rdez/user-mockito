package com.user.mockito.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import com.user.mockito.entity.User;
import com.user.mockito.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User updateUser(User user){
        return userRepository.findById(user.getId())
                .map(u -> userRepository.save(user))
                .orElse(null);
    }

    public User updateEmailUser(Long id, Map<String, Object> userMap){
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail((String) userMap.get("email"));
                    return userRepository.save(user);
                })
        .orElse(null);
    }

    public User updateNameUser(Long id, Map<String, Object> userMap){
        return userRepository.findById(id)
                .map(user -> {
                    userMap.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(User.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, user, value);
                        }
                    });
                    return userRepository.save(user);
                })
        .orElse(null);
    }

}
