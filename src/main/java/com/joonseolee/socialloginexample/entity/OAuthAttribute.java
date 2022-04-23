package com.joonseolee.socialloginexample.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class OAuthAttribute {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    public OAuthAttribute(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(RoleType.GUEST)
                .build();
    }
}
