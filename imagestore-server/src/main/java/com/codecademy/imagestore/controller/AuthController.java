package com.codecademy.imagestore.controller;

import com.codecademy.imagestore.dto.AuthRequest;
import com.codecademy.imagestore.dto.AuthResponse;
import com.codecademy.imagestore.dto.TokenRequest;
import com.codecademy.imagestore.dto.UserDTO;
import com.codecademy.imagestore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Resource", description = "Authentication related APIs")
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Register User",
            description = "Register user with the required details.",
            tags = {"post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        authService.registerUser(userDTO);
        return ResponseEntity.ok("User successfully registered.");
    }

    @Operation(
            summary = "Login User",
            description = "Api to login user with email and password. The response will have accessToken, expirationTime, email, and refreshToken.",
            tags = {"post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = AuthResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @Operation(
            summary = "Refresh token",
            description = "API to refresh the user access token when the token has expired.",
            tags = {"post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = AuthResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid data/request.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Auth information is missing/invalid.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Operation denied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content)
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }
}
