package me.jiny.prac0131.dto.oauthDto;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserResponse {

    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("name");
    }
}
