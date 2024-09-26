package com.swam.gateway.integration;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.ActiveProfiles;

import com.swam.commons.mongodb.template.WorkflowTemplateTO;
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
        checkIfResponseIsEqualsToTO("/catalog/test", DefaultWorkflows.getWorkflowTemplateTO2());

        WorkflowTemplateTO subWorkflowTemplateTO = null;
        try {
            subWorkflowTemplateTO = (WorkflowTemplateTO) DefaultWorkflows.getWorkflowTemplateTO2()
                    .getSubWorkflowTO("v2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkIfResponseIsEqualsToTO("/catalog/test?subworkflow=v2", subWorkflowTemplateTO);

        checkIfResponseContainsTO("/catalog", DefaultWorkflows.getWorkflowTemplateTO2());

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
                .bodyValue(DefaultWorkflows.getWorkflowTemplateTO2())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        newWorkflowId = response.split(": ")[1];

        // Get the workflowDto and check if it was posted correctly
        checkIfResponseIsEqualsToTO("/catalog/" + newWorkflowId, DefaultWorkflows.getWorkflowTemplateTO2());

        // Delete test
        client.delete().uri("/catalog/" + newWorkflowId)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void workflowPutTest() {
        // Put test
        client.put().uri("/catalog/random")
                .bodyValue(DefaultWorkflows.getWorkflowTemplateTO3())
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToTO("/catalog/random", DefaultWorkflows.getWorkflowTemplateTO3());
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

        checkIfResponseIsEqualsToTO("/operation/" + newWorkflowInstanceId, DefaultWorkflows.getWorkflowTemplateTO2());

        client.delete().uri("/operation/" + newWorkflowInstanceId)
                .exchange()
                .expectStatus().isOk();

    }
}
