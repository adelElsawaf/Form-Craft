package com.formcrafter.auth.auth.services;

import com.formcrafter.auth.auth.configs.GoogleAuthProperties;
import com.formcrafter.auth.auth.dtos.google_integrations.GoogleUserDTO;
import com.formcrafter.auth.auth.exceptions.InvalidCredentialsException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleTokenVerifierService {

    private final GoogleAuthProperties properties;

    public GoogleUserDTO verify(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken token = buildVerifier().verify(idToken);
        if (token == null) {
            throw new InvalidCredentialsException();
        }

        GoogleIdToken.Payload payload = token.getPayload();

        return new GoogleUserDTO(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get("given_name"),
                (String) payload.get("family_name")
        );
    }

    GoogleIdTokenVerifier buildVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(List.of(properties.getClientId()))
                .build();
    }
}
