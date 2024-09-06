package com.swam.gateway.integration;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.ActiveProfiles;

import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.utility.DefaultWorkflows;

@ActiveProfiles("test")
public class WorkflowEndpointTest extends BaseEndpointTest {

    public WorkflowEndpointTest() {
        super("http://localhost:8080/api/workflow");
    }

    // Test CRUD operations
    @Test
    public void workflowGetTest() {
        // GET test
        checkIfResponseIsEqualsToDTO("/catalog/test", DefaultWorkflows.getWorkflowTemplateDTO2());

        WorkflowTemplateDTO subWorkflowTemplateDTO = null;
        try {
            subWorkflowTemplateDTO = (WorkflowTemplateDTO) DefaultWorkflows.getWorkflowTemplateDTO2()
                    .getSubWorkflowDTO("v2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkIfResponseIsEqualsToDTO("/catalog/test?subworkflow=v2", subWorkflowTemplateDTO);

        checkIfResponseContainsDTO("/catalog", DefaultWorkflows.getWorkflowTemplateDTO2());

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
    public void workflowPostAndDeleteTest() {
        // Post test
        String newWorkflowId;
        String response = client.post().uri("/catalog")
                .bodyValue(DefaultWorkflows.getWorkflowTemplateDTO2())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        newWorkflowId = response.split(": ")[1];

        // Get the workflowDto and check if it was posted correctly
        checkIfResponseIsEqualsToDTO("/catalog/" + newWorkflowId, DefaultWorkflows.getWorkflowTemplateDTO2());

        // Delete test
        client.delete().uri("/catalog/" + newWorkflowId)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void workflowPutTest() {
        // Put test
        client.put().uri("/catalog/random")
                .bodyValue(DefaultWorkflows.getWorkflowTemplateDTO3())
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToDTO("/catalog/random", DefaultWorkflows.getWorkflowTemplateDTO3());
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

        checkIfResponseIsEqualsToDTO("/operation/" + newWorkflowInstanceId, DefaultWorkflows.getWorkflowTemplateDTO2());

        client.delete().uri("/operation/" + newWorkflowInstanceId)
                .exchange()
                .expectStatus().isOk();

    }
}
