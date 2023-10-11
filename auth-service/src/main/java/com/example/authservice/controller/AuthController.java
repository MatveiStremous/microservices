package com.example.authservice.controller;

import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.SignUpRequest;
import com.example.authservice.model.User;
import com.example.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@ApiResponse(responseCode = "200", description = "Successful operation")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate a user and generate a JWT token")
    @ApiResponse(responseCode = "403", description = "Wrong login or password")
    public AuthResponse login(@RequestBody
                              @Parameter(description = "User login credentials", in = ParameterIn.DEFAULT) AuthRequest authRequest) {
        return authService.login(authRequest.getLogin(), authRequest.getPassword());
    }

    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user")
    @ApiResponse(responseCode = "409", description = "This login is already registered")
    public AuthResponse register(@RequestBody
                                 @Parameter(description = "User registration data", in = ParameterIn.DEFAULT) SignUpRequest signUpRequest) {
        return authService.signup(User.builder()
                .login(signUpRequest.getLogin())
                .password(signUpRequest.getPassword())
                .build());
    }
}

