package me.jiny.prac0131.oAuth;

import lombok.RequiredArgsConstructor;
import me.jiny.prac0131.config.jwt.TokenProvider;
import me.jiny.prac0131.domain.User;
import me.jiny.prac0131.dto.oauthDto.GoogleUserInfo;
import me.jiny.prac0131.dto.oauthDto.KakaoUserInfo;
import me.jiny.prac0131.dto.oauthDto.OAuth2UserResponse;
import me.jiny.prac0131.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserResponse userInfo;
        if ("kakao".equals(registrationId)) {
            userInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if ("google".equals(registrationId)) {
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth 제공자입니다.");
        }

        User user = saveOrUpdate(userInfo);
        return oAuth2User;
    }

    private User saveOrUpdate(OAuth2UserResponse userInfo) {
        String email = userInfo.getEmail();
        String name = userInfo.getUsername();
        User user = userRepository.findByEmail(email)
                .map(entity->entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .username(name)
                        .build());
        return userRepository.save(user);
    }
}
