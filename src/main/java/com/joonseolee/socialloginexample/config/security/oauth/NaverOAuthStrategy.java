package com.joonseolee.socialloginexample.config.security.oauth;

import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NaverOAuthStrategy extends OAuthStrategy {

    public NaverOAuthStrategy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public OAuthAttribute getOAuthAttribute(String userNameAttributeName, OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttribute.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }
}
