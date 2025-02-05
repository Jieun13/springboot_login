package me.jiny.prac0131.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.RefreshToken;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.repository.RefreshTokenRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    @Transactional
    public void delete(){
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);
        refreshTokenRepository.deleteByUserId(userId);
    }
}
