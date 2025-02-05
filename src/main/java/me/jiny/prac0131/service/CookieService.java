package me.jiny.prac0131.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public User getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt_token")) {
                    String jwtToken = cookie.getValue();
                    return userService.findById(tokenProvider.getUserId(jwtToken));
                }
            }
        }
        return new User("Unknown", null, "Unknown@mail.com");
    }
}