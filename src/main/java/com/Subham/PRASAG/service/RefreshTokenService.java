package com.Subham.PRASAG.service;

import com.Subham.PRASAG.model.RefreshToken;
import com.Subham.PRASAG.model.User;
import com.Subham.PRASAG.repo.RefreshTokenRepository;
import com.Subham.PRASAG.repo.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository,
                               RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email : " + username));

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            long refreshTokenValidity = 30 * 60 * 1000; // 30 minutes

            refreshToken = new RefreshToken();
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            refreshToken.setExpirationTime(
                    Instant.now().plusMillis(refreshTokenValidity)
            );
            refreshToken.setUser(user);

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByRefreshToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpirationTime().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}
