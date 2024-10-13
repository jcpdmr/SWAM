package com.swam.gateway.endToEnd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.swam.commons.mongodb.MongodbEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseEndpointTest {
    protected WebTestClient client;
    private final String baseUrl;

    @BeforeEach
    void bindWebClient() {
        client = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    protected <T> void checkIfResponseIsEqualsToEntity(String uri, MongodbEntity<T> EntityToCompare) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EntityToCompare.getClass())
                .consumeWith(result -> {
                    assertEquals(result.getResponseBody(), EntityToCompare);
                });

    }

    protected <T> void checkIfResponseIsEqualsToEntitySet(String uri, Set<MongodbEntity<T>> EntitySetToCompare,
            Class<? extends MongodbEntity<T>> EntityClazz) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EntityClazz)
                .consumeWith(result -> {
                    boolean areEqual = EntitySetToCompare.size() == result.getResponseBody().size() &&
                            EntitySetToCompare.stream()
                                    .allMatch(dto -> result.getResponseBody().stream().anyMatch(r -> r.equals(dto)));
                    assertTrue(areEqual);
                });

    }

    protected <T> void checkIfResponseContainsEntity(String uri, MongodbEntity<T> EntityToBeContained) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EntityToBeContained.getClass())
                .consumeWith(result -> {
                    assertTrue(result.getResponseBody().contains(EntityToBeContained));
                });
    }

    @SuppressWarnings("unchecked")
    protected <T> T uncheckedCast(Object objectToCast) {
        return (T) objectToCast;
    }
}
