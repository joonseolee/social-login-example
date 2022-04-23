package com.joonseolee.socialloginexample.config.security;

import com.joonseolee.socialloginexample.config.security.oauth.OAuthFactory;
import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import com.joonseolee.socialloginexample.entity.SessionUser;
import com.joonseolee.socialloginexample.entity.User;
import com.joonseolee.socialloginexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final OAuthFactory oAuthFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        var oAuth2User = delegate.loadUser(userRequest);

        var registrationId = SocialAuthenticationType.getByName(userRequest.getClientRegistration().getRegistrationId());
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // oAuth2 service id (for examples-> google, kakao, naver)
        var oAuthStrategy = oAuthFactory.getOAuthStrategyByType(registrationId);
        var attributes = oAuthStrategy.getOAuthAttribute(userNameAttributeName, userRequest, oAuth2User);

        var user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttribute attribute) {
        var user = userRepository.findByEmail(attribute.getEmail())
                .map(it -> it.update(attribute.getName(), attribute.getPicture()))
                .orElse(attribute.toEntity());

        return userRepository.save(user);
    }
}
