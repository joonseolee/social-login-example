package com.joonseolee.socialloginexample.config.security.oauth;

import com.joonseolee.socialloginexample.config.security.SocialAuthenticationType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class OAuthFactory {

    private final List<OAuthStrategy> oAuthStrategies;
    private final Map<SocialAuthenticationType, OAuthStrategy> map;

    public OAuthFactory(List<OAuthStrategy> oAuthStrategies) {
        this.oAuthStrategies = oAuthStrategies;
        this.map = new HashMap<>();
        this.oAuthStrategies.forEach(it -> {
            var type = SocialAuthenticationType.getByName(
                    it.getClass().getSimpleName().toLowerCase());

            map.put(type, it);
        });
    }

    public OAuthStrategy getOAuthStrategyByType(SocialAuthenticationType type) {
        return this.map.get(type);
    }
}
