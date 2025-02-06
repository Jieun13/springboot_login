package me.jiny.prac0131.service;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.dto.GoogleTokenResponse;
import me.jiny.prac0131.dto.GoogleUserResponse;
import me.jiny.prac0131.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    private final WebClient webClient = WebClient.builder().build();

    private final String GOOGLE_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String GOOGLE_AUTH_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value("${google.logout-uri}")
    private String GOOGLE_LOGOUT_URI;

    public String getGoogleLogoutUrl() {
        return GOOGLE_LOGOUT_URI;
    }

    private String generateState = new BigInteger(130, new SecureRandom()).toString(32);

    public String getGoogleLoginUrl(){
        return GOOGLE_AUTH_URL +
                "?response_type=code&" +
                "client_id=" + GOOGLE_CLIENT_ID + "&" +
                "scope=email%20profile&" +
                "state=" + generateState + "&" +
                "redirect_uri=" + GOOGLE_REDIRECT_URI;
    }

    public String googleLogin(String code){
        GoogleTokenResponse tokenResponse = getGoogleAccessToken(code);
        GoogleUserResponse googleUser = getGoogleUser(tokenResponse.getAccessToken());
        User user = userRepository.findByEmail(googleUser.getEmail())
                .map(existingUser -> updateUser(existingUser, googleUser))
                .orElseGet(()->registerNewUser(googleUser));
        return tokenProvider.generateToken(user, Duration.ofDays(2));
    }

    private User updateUser(User user, GoogleUserResponse googleUser) {
        user.update(googleUser.getUsername());
        return userRepository.save(user);
    }

    private GoogleTokenResponse getGoogleAccessToken(String code) {
        return webClient.post()
                .uri(GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", GOOGLE_CLIENT_ID)
                        .with("redirect_uri", GOOGLE_REDIRECT_URI)
                        .with("client_secret", GOOGLE_CLIENT_SECRET)
                        .with("code", code))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            System.err.println("Error Response: " + errorBody);
                            return Mono.error(new RuntimeException("Google API 호출 오류: " + errorBody));
                        })
                )
                .bodyToMono(GoogleTokenResponse.class)
                .block();
    }

    private GoogleUserResponse getGoogleUser(String accessToken) {
        return webClient.get()
                .uri(GOOGLE_USER_INFO_URL)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GoogleUserResponse.class)
                .block();
    }

    private User registerNewUser(GoogleUserResponse googleUser){
        User newUser = User.builder()
                .email(googleUser.getEmail())
                .username(googleUser.getUsername())
                .build();
        return userRepository.save(newUser);
    }
}
