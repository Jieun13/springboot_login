package me.jiny.prac0131.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.dto.KakaoTokenResponse;
import me.jiny.prac0131.dto.KakaoUserResponse;
import me.jiny.prac0131.oAuth.CookieUtil;
import me.jiny.prac0131.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    private final WebClient webClient = WebClient.builder().build();

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private final String KAKAO_LOGOUT_URL = "https://kauth.kakao.com/oauth/logout";

    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUrI;

    @Value("${kakao.logout-uri}")
    private String kakaoLogoutUri;

    public String getKakaoLoginUrl() {
        return kakaoAuthUrl + "?client_id=" + kakaoClientId + "&redirect_uri=" + kakaoRedirectUrI + "&response_type=code";
    }

    public String getKakaoLogoutUrl() {
        return KAKAO_LOGOUT_URL + "?client_id=" + kakaoClientId + "&&logout_redirect_uri=" + kakaoLogoutUri;
    }

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
                .uri(KAKAO_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoClientId)
                        .with("redirect_uri", kakaoRedirectUrI)
                        .with("code", code))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            System.err.println("Error Response: " + errorBody);
                            return Mono.error(new RuntimeException("Kakao API 호출 오류: " + errorBody));
                        })
                )
                .bodyToMono(KakaoTokenResponse.class)
                .block();
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
                .build();
        return userRepository.save(newUser);
    }
}