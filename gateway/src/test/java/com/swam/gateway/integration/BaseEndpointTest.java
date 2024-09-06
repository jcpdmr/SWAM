package com.swam.gateway.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.swam.commons.mongodb.MongodbDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseEndpointTest {
    protected WebTestClient client;
    private final String baseUrl;

    @BeforeEach
    void bindWebClient() {
        client = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    protected <T> void checkIfResponseIsEqualsToDTO(String uri, MongodbDTO<T> DTOToCompare) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DTOToCompare.getClass())
                .consumeWith(result -> {
                    assertEquals(result.getResponseBody(), DTOToCompare);
                });

    }

    protected <T> void checkIfResponseContainsDTO(String uri, MongodbDTO<T> DTOToBeContained) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DTOToBeContained.getClass())
                .consumeWith(result -> {
                    assertTrue(result.getResponseBody().contains(DTOToBeContained));
                });
    }
}
