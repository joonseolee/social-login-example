package com.joonseolee.socialloginexample.config.security.oauth;

import com.joonseolee.socialloginexample.entity.OAuthAttribute;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class GithubStrategy extends OAuthStrategy {

    public GithubStrategy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public OAuthAttribute getOAuthAttribute(String userNameAttributeName, OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return OAuthAttribute.builder()
                .name((String) attributes.get("name"))
                .email(getEmail(userRequest.getAccessToken().getTokenValue()))
                .picture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .nameAttributeKey("id")
                .build();
    }

    private String getEmail(String accessToken) {
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
        return map.get("email");
    }
}
