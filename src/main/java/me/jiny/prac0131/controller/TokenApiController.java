package me.jiny.prac0131.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.dto.CreateAccessTokenRequest;
import me.jiny.prac0131.dto.CreateAccessTokenResponse;
import me.jiny.prac0131.oAuth.CookieUtil;
import me.jiny.prac0131.service.TokenService;
import me.jiny.prac0131.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserService userService;


    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    @DeleteMapping("/api/logout")
    //쿠키 삭제
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.deleteCookie(request, response, "jwt_token");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/profile")
    public ResponseEntity<User> profile(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // "Bearer {token}"에서 실제 토큰만 추출
        String token = authorizationHeader.substring(7);

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long userId = tokenProvider.getUserId(token);
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 잘못된 토큰 처리
        }
    }
}
