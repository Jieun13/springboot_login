package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.dto.KakaoLoginRequest;
import me.jiny.prac0131.service.KakaoAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final WebClient webClient = WebClient.builder().build();

    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) {
        String jwtToken = kakaoAuthService.kakaoLogin(code);
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> kakaoLogout(@RequestHeader("Authorization") String accessToken) {
        return webClient.post()
                .uri("/v1/user/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok("로그아웃 성공"))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그아웃 실패: " + e.getMessage())));
    }
}
