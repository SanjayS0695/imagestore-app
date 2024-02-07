package com.codecademy.imagestore.controller;

import com.codecademy.imagestore.dto.AuthRequest;
import com.codecademy.imagestore.dto.AuthResponse;
import com.codecademy.imagestore.dto.TokenRequest;
import com.codecademy.imagestore.dto.UserDTO;
import com.codecademy.imagestore.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        authService.registerUser(userDTO);
        return ResponseEntity.ok("User successfully registered.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }
}
