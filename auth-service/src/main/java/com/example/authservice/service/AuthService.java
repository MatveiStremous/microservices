package com.example.authservice.service;

import com.example.authservice.dto.AuthResponse;
import com.example.authservice.model.User;

public interface AuthService {
    AuthResponse signup(User user);

    AuthResponse login(String login, String password);
}
