package me.jiny.prac0131.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserResponse {
    private String id; // 구글에서 제공하는 유저 고유 ID

    @Getter
    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @Getter
    @JsonProperty("picture")
    private String profileImage; // 프로필 이미지 URL

    // 이름을 반환하는 메소드
    public String getUsername() {
        return name != null ? name : "Unknown";
    }

}


