package com.codecademy.imagestore.service;

import com.codecademy.imagestore.auth.JwtUtil;
import com.codecademy.imagestore.dto.AuthRequest;
import com.codecademy.imagestore.dto.AuthResponse;
import com.codecademy.imagestore.dto.TokenRequest;
import com.codecademy.imagestore.dto.UserDTO;
import com.codecademy.imagestore.entity.RefreshToken;
import com.codecademy.imagestore.entity.UserData;
import com.codecademy.imagestore.enums.Role;
import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    /**
     * Method to register user on sign up
     *
     * @param request
     * @return
     */
    public AuthResponse registerUser(UserDTO request) {
        var user = UserData.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        this.userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    /**
     * Method to authenticate AuthRequest on login
     *
     * @param request
     * @return
     */
    public AuthResponse authenticate(AuthRequest request) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        if (authentication.isAuthenticated()) {
            var refreshToken = refreshTokenService.createRefreshToken(request.getEmail());
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtUtil.generateToken(user);
            var expirationDate = jwtUtil.extractExpiration(jwtToken);
            return AuthResponse.builder()
                    .accessToken(jwtToken)
                    .expirationDate(expirationDate)
                    .refreshToken(refreshToken.getToken())
                    .email(user.getEmail())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request.");
        }
    }

    /**
     * Method to refresh token on expiration using refresh token
     *
     * @param tokenRequest
     * @return
     */
    public AuthResponse refreshToken(TokenRequest tokenRequest) {
        return refreshTokenService.findByToken(tokenRequest.getRefreshToken())
                .map(refreshTokenService::verifyExpirationDate)
                .map(RefreshToken::getUserInfo)
                .map(userData -> {
                    String token = jwtUtil.generateToken(userData);
                    return AuthResponse.builder()
                            .accessToken(token)
                            .refreshToken(tokenRequest.getRefreshToken())
                            .expirationDate(jwtUtil.extractExpiration(token))
                            .email(userData.getEmail())
                            .build();
                })
                .orElseThrow(() -> new GenericAPIException(HttpStatus.NOT_FOUND, "Refresh token not found."));

    }

}
