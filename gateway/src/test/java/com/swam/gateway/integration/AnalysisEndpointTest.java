package com.swam.gateway.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
public class AnalysisEndpointTest {

    private WebTestClient client;

    @BeforeEach
    void bindWebClient() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:8080/api/workflow").build();
    }
}
