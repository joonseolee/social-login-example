package com.joonseolee.socialloginexample.config.security;

import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import com.joonseolee.socialloginexample.entity.SessionUser;
import com.joonseolee.socialloginexample.entity.User;
import com.joonseolee.socialloginexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Import({ ServerConfiguration.class })
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        var oAuth2User = delegate.loadUser(userRequest);

        var registrationId = SocialAuthenticationType.getByName(userRequest.getClientRegistration().getRegistrationId());
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // oAuth2 service id (google, kakao, naver)
        OAuthAttribute attributes = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        // TODO: github 만 rest api 호출하여 email 데이터 가져와야하기때문에 소셜로그인별 로직 분리필요
        if (registrationId.equals(SocialAuthenticationType.GITHUB)) {
            attributes = setGithubEmail(attributes, userRequest.getAccessToken().getTokenValue());
        }

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

    private OAuthAttribute setGithubEmail(OAuthAttribute attribute, String accessToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);

        var uri = UriComponentsBuilder.fromHttpUrl("https://api.github.com/user/emails").build().toUri();
        ResponseEntity<List> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, List.class);

        if (responseEntity.getBody() == null || responseEntity.getBody().isEmpty()) {
            throw new RuntimeException("responseEntity is empty");
        }

        // TODO: 샘플이니 간단하게 처리
        Map<String, String> map = (Map<String, String>) responseEntity.getBody().get(0);
        String email = map.get("email");

        return new OAuthAttribute(attribute.getAttributes(),
                attribute.getNameAttributeKey(),
                attribute.getName(),
                email,
                attribute.getPicture());
    }
}
