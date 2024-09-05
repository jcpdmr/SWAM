package com.swam.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.swam.commons.mongodb.template.WorkflowTemplateDTO;

@ActiveProfiles("test")
@SpringBootTest()
public class WorkflowEndpointTest {

    private WebTestClient client;

    @BeforeEach
    void bindWebClient() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @Test
    public void prova() {
        client.get().uri("/api/workflow/catalog/test")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(WorkflowTemplateDTO.class)
                .consumeWith(result -> {
                    System.out.println(result.getResponseBody());
                });
    }
}
