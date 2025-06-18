package com.backend.dashboard_tool.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dashboard_tool.security.JwtTokenUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for handling authentication-related requests.
 * This class provides an endpoint for user login and token generation.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * The password hash for the user "metadata@Ret2025".
     * This hash is used to verify the password provided during login.
     */
    private static final String PASSWORD_HASH = "$2a$12$djVp7pPvgWxHSj9f8nVRjubnS5FMcyG0sjHkIn1hn.gFmmMLCG.26"; 

    /**
     * Password encoder for hashing and verifying passwords.
     * This instance uses BCrypt hashing algorithm.
     */
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * JwtTokenUtil instance for generating JWT tokens.
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Endpoint for user login.
     * This method checks the provided password against the stored hash and generates a JWT token if valid.
     * 
     * @param password the password provided by the user
     * @return a ResponseEntity containing the generated token or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String password) {
        System.out.println("Login attempt received");
        
        // Trim the password to handle JSON formatting
        password = password.trim();
        if (password.startsWith("\"") && password.endsWith("\"")) {
            password = password.substring(1, password.length() - 1);
        }
        
        if (passwordEncoder.matches(password, PASSWORD_HASH)) {
            String token = jwtTokenUtil.generateToken();
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid password");
        }
    }
}