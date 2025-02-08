package me.jiny.prac0131.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    @GetMapping
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "home";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
}