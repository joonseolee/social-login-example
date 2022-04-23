package com.joonseolee.socialloginexample.config.security.oauth;

import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public abstract class OAuthStrategy {

    protected final RestTemplate restTemplate;

    public abstract OAuthAttribute getOAuthAttribute(String userNameAttributeName,
                                                     OAuth2UserRequest userRequest,
                                                     OAuth2User oAuth2User);
}
