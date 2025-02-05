package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.dto.UserRequest;
import me.jiny.prac0131.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserRequest request, RedirectAttributes redirectAttributes) {
        if (userService.checkUsernameDuplicate(request.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 사용자 이름입니다.");
            return "redirect:/signup";
        }
        if (userService.checkEmailDuplicate(request.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 사용 중인 이메일입니다.");
            return "redirect:/signup";
        }
        userService.save(request);
        return "redirect:/login";
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        CookieUtil.deleteCookie(request, response, "jwt_token");
//        request.getSession().invalidate();
//        return "redirect:/";
//    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}