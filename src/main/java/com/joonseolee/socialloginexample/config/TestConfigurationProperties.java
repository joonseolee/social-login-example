package com.joonseolee.socialloginexample.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "test.sample")
@Getter
public final class TestConfigurationProperties {

    private final String username;
    private final String password;
    private final List<String> ownNames;

    @ConstructorBinding
    public TestConfigurationProperties(String username, String password, List<String> ownNames) {
        this.username = username;
        this.password = password;
        this.ownNames = ownNames;
    }
}
