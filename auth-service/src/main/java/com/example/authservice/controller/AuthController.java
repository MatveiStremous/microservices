package com.example.authservice.controller;

import com.example.authservice.dto.AuthDTO;
import com.example.authservice.dto.SignUpDTO;
import com.example.authservice.exception.BusinessException;
import com.example.authservice.model.User;
import com.example.authservice.security.JWTUtil;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authDTO.getLogin(), authDTO.getPassword());

        authenticationManager.authenticate(authToken);

        String token = jwtUtil.generateAccessToken(userService.findByLogin(authDTO.getLogin()));
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        throw new BusinessException(HttpStatus.BAD_REQUEST, "Wrong login or password.");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody SignUpDTO signUpDTO) {
        authService.signup(User.builder()
                .login(signUpDTO.getLogin())
                .password(signUpDTO.getPassword())
                .build());
        return ResponseEntity.ok("Successful registration.");
    }
}

