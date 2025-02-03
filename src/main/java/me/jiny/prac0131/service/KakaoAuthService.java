package me.jiny.prac0131.service;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.dto.KakaoTokenResponse;
import me.jiny.prac0131.dto.KakaoUserResponse;
import me.jiny.prac0131.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    private final WebClient webClient = WebClient.builder().build();

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;


    public String kakaoLogin(String code){
        KakaoTokenResponse tokenResponse = getKakaoAccessToken(code);
        KakaoUserResponse kakaoUser = getKakaoUser(tokenResponse.getAccessToken());
        User user = userRepository.findByEmail(kakaoUser.getEmail())
                .map(existingUser -> updateUser(existingUser, kakaoUser))
                .orElseGet(()->registerNewUser(kakaoUser));
        return tokenProvider.generateToken(user, Duration.ofDays(2));
    }

    private User updateUser(User user, KakaoUserResponse kakaoUser) {
        user.update(kakaoUser.getNickname());  // 변경된 닉네임 업데이트
        return userRepository.save(user);
    }

    private KakaoTokenResponse getKakaoAccessToken(String code) {
        return webClient.post()
                .uri(KAKAO_TOKEN_URL)  // URL 설정
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)  // 헤더 설정
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoClientId)
                        .with("redirect_uri", kakaoRedirectUrl)
                        .with("code", code))  // 요청 바디 설정
                .retrieve()  // 응답 받기
                .bodyToMono(KakaoTokenResponse.class)  // 응답 타입 지정
                .block();  // 동기 방식 처리
    }


    private KakaoUserResponse getKakaoUser(String accessToken) {
        return webClient.get()
                .uri(KAKAO_USER_URL)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();
    }

    private User registerNewUser(KakaoUserResponse kakaoUser){
        User newUser = User.builder()
                .email(kakaoUser.getEmail())
                .username(kakaoUser.getNickname())
                .password("kakaoPw" + kakaoUser.getEmail()) //임의로 패스워드 설정
                .build();
        return userRepository.save(newUser);
    }
}