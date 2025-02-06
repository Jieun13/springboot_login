package me.jiny.prac0131.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public User getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("kakao_token") || cookie.getName().equals("google_token")) {
                    String jwtToken = cookie.getValue();
                    return userService.findById(tokenProvider.getUserId(jwtToken));
                }
            }
        }
        return new User("Unknown", null, "");
    }
}