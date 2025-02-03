package me.jiny.prac0131.config.jwt;

import me.jiny.prac0131.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(user.getId(), user.getEmail());
    }

    //토큰 생성 메소드
    private String makeToken(Long userId, String email) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .setSubject(email)
                .claim("id", userId)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //토큰 유효성 검증 메소드
    public boolean validToken(String token) {
        try{
            Jwts.parser().setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    //토큰 기반으로 인증 정보를 가져오는 메소드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<GrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);
    }

    //토큰 기반으로 유저 ID를 가져오는 메소드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}