package com.example.hotel.room.service;

import com.example.hotel.room.model.User;
import com.example.hotel.room.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User registerUser(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            return null; // Username already exists
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); // passwordencoded
        user.setRole(role);
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && encoder.matches(password, userOpt.get().getPassword())) {
            return userOpt.get();
        }
        return null;
    }
    public User registerUser(String name,String username, String password, String email, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            return null; // Username already exists
        }
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);          
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

}
