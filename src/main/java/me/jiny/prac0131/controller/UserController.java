package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserController {
    @Value("${google.login-uri}")
    private String googleLoginUri;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("loginUrl", googleLoginUri);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginUrl", googleLoginUri);
        return "home";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
}