package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.dto.UserRequest;
import me.jiny.prac0131.service.UserService;
import me.jiny.prac0131.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class JwtLoginApiController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public String signup(@RequestBody UserRequest request) {

        if(userService.checkUsernameDuplicate(request.getUsername())) {
            return "아이디가 중복됩니다.";
        }
        if(userService.checkEmailDuplicate(request.getEmail())) {
            return "이메일이 중복됩니다.";
        }
        userService.save(request);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequest request) {

        User user = userService.findByUsername(request.getUsername());

        if(!userService.isIdPasswordValid(request.getUsername(), request.getPassword())) {
            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        return tokenProvider.generateToken(user, Duration.ofDays(14));
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.findByUsername(auth.getName());

        return String.format("id : %s\nusername : %s\nemail : %s\npassword : %s\n",
                loginUser.getId(), loginUser.getUsername(), loginUser.getEmail(), loginUser.getPassword());
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "관리자 페이지 접근 성공 ";
    }
}