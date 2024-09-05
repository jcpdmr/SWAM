package com.swam.gateway.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.utility.Defaults;

@ActiveProfiles("test")
public class WorkflowEndpointTest {

    private WebTestClient client;

    @BeforeEach
    void bindWebClient() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:8080/api/workflow").build();
    }

    // Test CRUD operations for catalog
    @Test
    public void catalogGetTest() {
        // GET test
        checkIfResponseIsEqualsToWorkflowDTO("/catalog/test", Defaults.getWorkflowTemplateDTO2());

        WorkflowTemplateDTO subWorkflowTemplateDTO = null;
        try {
            subWorkflowTemplateDTO = (WorkflowTemplateDTO) Defaults.getWorkflowTemplateDTO2().getSubWorkflowDTO("v2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkIfResponseIsEqualsToWorkflowDTO("/catalog/test?subworkflow=v2", subWorkflowTemplateDTO);

        checkIfResponseContainsWorkflowDTO("/catalog", Defaults.getWorkflowTemplateDTO2());

        client.get().uri("/catalog/test1")
                .exchange()
                .expectStatus().isNotFound();

        client.get().uri("/catalog/test?format=svg")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8");

        client.get().uri("/catalog/test?format=svg&subworkflow=v2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8");
    }

    @Test
    public void catalogPostAndDeleteTest() {
        // Post test
        String newWorkflowId;
        String response = client.post().uri("/catalog")
                .bodyValue(Defaults.getWorkflowTemplateDTO2())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        newWorkflowId = response.split(": ")[1];

        // Get the workflowDto and check if it was posted correctly
        checkIfResponseIsEqualsToWorkflowDTO("/catalog/" + newWorkflowId, Defaults.getWorkflowTemplateDTO2());

        // Delete test
        client.delete().uri("/catalog/" + newWorkflowId)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void catalogPutTest() {
        // Put test
        client.put().uri("/catalog/random")
                .bodyValue(Defaults.getWorkflowTemplateDTO3())
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToWorkflowDTO("/catalog/random", Defaults.getWorkflowTemplateDTO3());
    }

    // Test MakeInstance (and some CRUD for operation)
    @Test
    public void makeInstanceTest() {
        String newWorkflowInstanceId = null;

        String response = client.get().uri("/tobeinstantiated/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        newWorkflowInstanceId = response.split(": ")[1];

        checkIfResponseIsEqualsToWorkflowDTO("/operation/" + newWorkflowInstanceId, Defaults.getWorkflowTemplateDTO2());

        client.delete().uri("/operation/" + newWorkflowInstanceId)
                .exchange()
                .expectStatus().isOk();

    }

    private void checkIfResponseIsEqualsToWorkflowDTO(String uri, WorkflowTemplateDTO workflowToCompare) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkflowTemplateDTO.class)
                .consumeWith(result -> {
                    assertEquals(result.getResponseBody(), workflowToCompare);
                });

    }

    private void checkIfResponseContainsWorkflowDTO(String uri, WorkflowTemplateDTO workflowToBeContained) {
        client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(WorkflowTemplateDTO.class)
                .consumeWith(result -> {
                    assertTrue(result.getResponseBody().contains(workflowToBeContained));
                });
    }
}
