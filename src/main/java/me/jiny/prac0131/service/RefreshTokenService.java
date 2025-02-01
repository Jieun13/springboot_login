package me.jiny.prac0131.service;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.RefreshToken;
import me.jiny.prac0131.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }
}
