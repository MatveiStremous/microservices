package com.example.authservice.service;


import com.example.authservice.dto.AuthResponse;
import com.example.authservice.exception.BusinessException;
import com.example.authservice.model.User;
import com.example.authservice.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(User user) {
        Optional<User> userFromDB = userService.findByLogin(user.getLogin());
        if (userFromDB.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userService.save(user);
            String token = jwtUtil.generateAccessToken(savedUser.getLogin());
            return new AuthResponse(savedUser.getLogin(), token);
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, "This login is already registered.");
        }
    }

    public AuthResponse login(String login, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login, password);

        try {
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Wrong login or password.");
        }

        String token = jwtUtil.generateAccessToken(login);
        return new AuthResponse(login, token);
    }
}
