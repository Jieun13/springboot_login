package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.dto.KakaoLoginRequest;
import me.jiny.prac0131.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest request){
        String jwtToken = kakaoAuthService.kakaoLogin(request.getCode());
        return ResponseEntity.ok(jwtToken);
    }
}
