package com.codecademy.imagestore.service;

import com.codecademy.imagestore.entity.RefreshToken;
import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.exception.ServiceError;
import com.codecademy.imagestore.repository.RefreshTokenRepository;
import com.codecademy.imagestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthTokenService {

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
            var existingRefreshToken = refreshTokenRepository.findByUserInfo(userData.get());
            existingRefreshToken.ifPresent(token -> refreshTokenRepository.delete(token));
            return refreshTokenRepository.save(refreshToken);
        } else {
            throw new GenericAPIException(ServiceError.REFRESH_TKN_NOT_CREATED, username);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpirationDate(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(refreshToken);
            throw new GenericAPIException(ServiceError.REFRESH_TKN_EXPIRED);
        }
        return refreshToken;
    }
}
