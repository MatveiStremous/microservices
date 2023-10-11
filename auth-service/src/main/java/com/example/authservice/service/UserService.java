package com.example.authservice.service;

import com.example.authservice.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
    User save(User user);
}
