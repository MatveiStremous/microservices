package com.example.authservice.controller;

import com.example.authservice.dto.AuthDTO;
import com.example.authservice.dto.SignUpDTO;
import com.example.authservice.exception.BusinessException;
import com.example.authservice.model.User;
import com.example.authservice.security.JWTUtil;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("auth")
@ApiResponse(responseCode = "200", description = "Successful operation")
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
    @Operation(summary = "User login", description = "Authenticate a user and generate a JWT token")
    @ApiResponse(responseCode = "403", description = "Wrong login or password")
    public ResponseEntity<Map<String, String>> login(@RequestBody
                                                     @Parameter(description = "User login credentials", in = ParameterIn.DEFAULT) AuthDTO authDTO) {
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
    @Operation(summary = "User registration", description = "Register a new user")
    @ApiResponse(responseCode = "409", description = "This login is already registered")
    public ResponseEntity<String> register(@RequestBody
                                           @Parameter(description = "User registration data", in = ParameterIn.DEFAULT) SignUpDTO signUpDTO) {
        authService.signup(User.builder()
                .login(signUpDTO.getLogin())
                .password(signUpDTO.getPassword())
                .build());
        return ResponseEntity.ok("Successful registration.");
    }
}

