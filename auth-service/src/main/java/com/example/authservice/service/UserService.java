package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByLogin(String login) {
        Optional<User> foundUser = userRepository.findByLogin(login);
        return foundUser.orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
    }

}

