package com.joonseolee.socialloginexample.config.security.oauth;

import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FacebookStrategy extends OAuthStrategy {

    public FacebookStrategy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public OAuthAttribute getOAuthAttribute(String userNameAttributeName, OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return OAuthAttribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
