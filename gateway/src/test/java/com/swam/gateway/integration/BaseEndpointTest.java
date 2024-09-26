package com.swam.gateway.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.swam.commons.mongodb.MongodbTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseEndpointTest {
    protected WebTestClient client;
    private final String baseUrl;

    @BeforeEach
    void bindWebClient() {
        client = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    protected <T> void checkIfResponseIsEqualsToTO(String uri, MongodbTO<T> TOToCompare) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TOToCompare.getClass())
                .consumeWith(result -> {
                    assertEquals(result.getResponseBody(), TOToCompare);
                });

    }

    protected <T> void checkIfResponseIsEqualsToTOSet(String uri, Set<MongodbTO<T>> TOSetToCompare,
            Class<? extends MongodbTO<T>> TOClazz) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TOClazz)
                .consumeWith(result -> {
                    boolean areEqual = TOSetToCompare.size() == result.getResponseBody().size() &&
                            TOSetToCompare.stream()
                                    .allMatch(dto -> result.getResponseBody().stream().anyMatch(r -> r.equals(dto)));
                    assertTrue(areEqual);
                });

    }

    protected <T> void checkIfResponseContainsTO(String uri, MongodbTO<T> TOToBeContained) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TOToBeContained.getClass())
                .consumeWith(result -> {
                    assertTrue(result.getResponseBody().contains(TOToBeContained));
                });
    }

    @SuppressWarnings("unchecked")
    protected <T> T uncheckedCast(Object objectToCast) {
        return (T) objectToCast;
    }
}
