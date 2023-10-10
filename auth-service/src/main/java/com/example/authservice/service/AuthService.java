package com.example.authservice.service;


import com.example.authservice.exception.BusinessException;
import com.example.authservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void signup(User user) {
        User userFromDB = userService.findByLogin(user.getLogin());
        if (userFromDB == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
        } else {
            throw new BusinessException(HttpStatus.CONFLICT, "This login is already registered.");
        }
    }
}
