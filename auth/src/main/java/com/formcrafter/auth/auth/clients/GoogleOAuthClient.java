package com.formcrafter.auth.auth.clients;

import com.formcrafter.auth.auth.configs.GoogleAuthProperties;
import com.formcrafter.auth.auth.dtos.google_integrations.GoogleTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleOAuthClient {

    private final RestTemplate restTemplate;
    private final GoogleAuthProperties properties;

    public GoogleTokenDTO exchangeCode(String code) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("code", code);
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<GoogleTokenDTO> response =
                restTemplate.postForEntity(
                        "https://oauth2.googleapis.com/token",
                        new HttpEntity<>(body, headers),
                        GoogleTokenDTO.class
                );

        return response.getBody();
    }
}
