package com.formcrafter.auth.auth.configs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GoogleAuthPropertiesTest {

    @Test
    void storesConfiguredValues() {
        GoogleAuthProperties properties = new GoogleAuthProperties();
        properties.setClientId("client-id");
        properties.setClientSecret("client-secret");
        properties.setRedirectUri("http://localhost/callback");

        assertThat(properties.getClientId()).isEqualTo("client-id");
        assertThat(properties.getClientSecret()).isEqualTo("client-secret");
        assertThat(properties.getRedirectUri()).isEqualTo("http://localhost/callback");
    }
}
