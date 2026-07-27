package com.formcrafter.auth.auth.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.auth")
@Getter
@Setter
public class GoogleAuthProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
