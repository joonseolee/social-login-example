package com.joonseolee.socialloginexample.config.security.oauth;

import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoStrategy extends OAuthStrategy {

    public KakaoStrategy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public OAuthAttribute getOAuthAttribute(String userNameAttributeName, OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey("id")
                .build();
    }
}
