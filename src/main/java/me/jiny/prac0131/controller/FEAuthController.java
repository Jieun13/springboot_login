package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.KakaoProperties;
import me.jiny.prac0131.config.jwt.JwtProperties;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.service.KakaoAuthService;
import me.jiny.prac0131.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
public class FEAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final KakaoProperties kakaoProperties;

    private final WebClient webClient = WebClient.builder().build();

    // 카카오 로그인 페이지를 제공
    @GetMapping("/auth/login")
    public String kakaoLogin(Model model) {
        return "login";
    }

    @GetMapping("/auth/login/kakao")
    public String kakaoLogin(@RequestParam String code, Model model) {
        String jwtToken = kakaoAuthService.kakaoLogin(code);
        model.addAttribute("jwtToken", jwtToken);
        Long userId = tokenProvider.getUserId(jwtToken);
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "info";
    }
}