package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.dto.KakaoLoginRequest;
import me.jiny.prac0131.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/auth/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest request){
        String jwtToken = kakaoAuthService.kakaoLogin(request.getCode());
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/cookie")
    public ResponseEntity<Map<String, String>> getCookies(@CookieValue(value = "kakao_token", required = false) String kakaoToken,
                                                          @CookieValue(value = "google_token", required = false) String googleToken) {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("kakao_token", kakaoToken);
        cookies.put("google_token", googleToken);

        return ResponseEntity.ok(cookies);
    }

}
