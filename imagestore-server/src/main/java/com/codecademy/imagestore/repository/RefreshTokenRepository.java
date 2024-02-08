package com.codecademy.imagestore.repository;

import com.codecademy.imagestore.entity.RefreshToken;
import com.codecademy.imagestore.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserInfo(UserData userData);

}
