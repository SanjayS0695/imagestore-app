package com.codecademy.imagestore.service;

import com.codecademy.imagestore.entity.RefreshToken;
import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.repository.RefreshTokenRepository;
import com.codecademy.imagestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpTime;

    public RefreshToken createRefreshToken(String username) {
        var userData = userRepository.findByEmail(username);
        if (userData.isPresent()) {
            RefreshToken refreshToken = RefreshToken.builder()
                    .userInfo(userData.get())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(new Date(System.currentTimeMillis() + refreshTokenExpTime))
                    .build();
            return refreshTokenRepository.save(refreshToken);
        } else {
            throw new GenericAPIException(HttpStatus.NOT_FOUND, String.format("Failed to create " +
                    "refresh token as the user with email: %s not found.", username));
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpirationDate(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(refreshToken);
            throw new GenericAPIException(HttpStatus.UNAUTHORIZED, "Refresh token is expired. Please login again!");
        }
        return refreshToken;
    }
}
