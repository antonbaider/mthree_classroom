package com.mthree.bankmthree.controller;

import com.mthree.bankmthree.dto.auth.LoginRequest;
import com.mthree.bankmthree.dto.auth.LoginResponse;
import com.mthree.bankmthree.dto.auth.RegisterRequest;
import com.mthree.bankmthree.dto.user.UserDTO;
import com.mthree.bankmthree.security.JwtUtils;
import com.mthree.bankmthree.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Operation(summary = "Create new user")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            UserDTO createdUser = userService.createUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User registered successfully!", createdUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error: " + e.getMessage()));
        }
    }

    @Operation(summary = "Log in")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());

            return ResponseEntity.ok(new ApiResponse("Login successful", new LoginResponse(jwt)));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Error: Invalid username or password!"));
        }
    }

    // Inner class for API responses to maintain a consistent response format
    @Getter
    public static class ApiResponse {
        private final String message;
        private Object data;

        public ApiResponse(String message) {
            this.message = message;
        }

        public ApiResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }
    }
}