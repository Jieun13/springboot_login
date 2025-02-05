package me.jiny.prac0131.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.oAuth.CookieUtil;
import me.jiny.prac0131.service.CookieService;
import me.jiny.prac0131.service.KakaoAuthService;
import me.jiny.prac0131.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class FEController {

    private final KakaoAuthService kakaoAuthService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final CookieService cookieService;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model){
        String loginUrl = kakaoAuthService.getKakaoLoginUrl();
        String logoutUrl = kakaoAuthService.getKakaoLogoutUrl();
        User user = cookieService.getUser(request);
        model.addAttribute("user", user);
        model.addAttribute("loginUrl", loginUrl);
        model.addAttribute("logoutUrl", logoutUrl);
        return "home";
    }

    @GetMapping("/auth/login/kakao")
    public String kakaoLogin(@RequestParam("code") String code, HttpServletResponse response, Model model) {
        String jwtToken = kakaoAuthService.kakaoLogin(code);
        User user = userService.findById(tokenProvider.getUserId(jwtToken));

        Cookie cookie = new Cookie("jwt_token", jwtToken);
        cookie.setPath("/");
        response.addCookie(cookie);

        model.addAttribute("jwtToken", jwtToken);
        model.addAttribute("user", user);
        return "test";
    }

    @GetMapping("/auth/profile")
    public String kakaoProfile(HttpServletRequest request, Model model) {
        User user = cookieService.getUser(request);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/auth/logout/kakao")
    //세션, 쿠키 삭제
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "jwt_token");
        request.getSession().invalidate();
        return "redirect:/";
    }
}