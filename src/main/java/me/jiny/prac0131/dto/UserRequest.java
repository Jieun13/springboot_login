package me.jiny.prac0131.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String password;
    private String email;
}
