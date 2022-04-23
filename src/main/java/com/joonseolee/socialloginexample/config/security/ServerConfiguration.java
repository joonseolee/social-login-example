package com.joonseolee.socialloginexample.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class ServerConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
