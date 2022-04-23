package com.joonseolee.socialloginexample.config.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SocialAuthenticationType {
    GOOGLE,
    NAVER,
    KAKAO,
    APPLE,
    GITHUB,
    FACEBOOK,
    PAYCO;

    public static SocialAuthenticationType getByName(String registrationId) {
        List<SocialAuthenticationType> names = Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(registrationId))
                .collect(Collectors.toList());

        if (names.isEmpty())
            throw new IllegalArgumentException("cannot find the name of registration - " + registrationId);

        return names.get(0);
    }
}
