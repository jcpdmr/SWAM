package com.swam.gateway.integration;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oristool.eulero.modeling.stochastictime.DeterministicTime;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.ProductTemplate;
import com.qesm.WorkflowTemplate;
import com.swam.commons.mongodb.template.ProductTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;
import com.swam.commons.utility.DefaultProducts;
import com.swam.commons.utility.DefaultWorkflows;

@ActiveProfiles("test")
public class ProductEndpointTest extends BaseEndpointTest {

    public ProductEndpointTest() {
        super("http://localhost:8080/api/workflow");
    }

    // Test CRUD operations
    @Test
    public void productGetTest() {
        checkIfResponseIsEqualsToTO("/catalog/test/product/v1", new ProductTemplateTO(DefaultProducts.v1));

        checkIfResponseIsEqualsToTOSet("/catalog/test/product",
                Set.of(new ProductTemplateTO(DefaultProducts.v0), new ProductTemplateTO(DefaultProducts.v1),
                        new ProductTemplateTO(DefaultProducts.v2), new ProductTemplateTO(DefaultProducts.v3),
                        new ProductTemplateTO(DefaultProducts.v4), new ProductTemplateTO(DefaultProducts.v5),
                        new ProductTemplateTO(DefaultProducts.v6), new ProductTemplateTO(DefaultProducts.v7)),
                ProductTemplateTO.class);

        client.get().uri("/catalog/test1/product")
                .exchange()
                .expectStatus().isNotFound();

        client.get().uri("/catalog/test/product/v10")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void productPutAndDeleteTest() {
        // Update random workflow
        client.put().uri("/catalog/random")
                .bodyValue(DefaultWorkflows.getWorkflowTemplateTO2())
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToTO("/catalog/random", DefaultWorkflows.getWorkflowTemplateTO2());

        // Put test
        ProductTemplateTO v1Updated = new ProductTemplateTO(
                new ProductTemplate("v1", 150, new DeterministicTime(BigDecimal.valueOf(5.0))));

        client.put().uri("/catalog/random/product/v1")
                .bodyValue(v1Updated)
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToTO("/catalog/random/product/v1", v1Updated);

        // Delete test
        client.delete().uri("/catalog/random/product/v1")
                .exchange()
                .expectStatus().isOk();

        client.get().uri("/catalog/random/product/v1")
                .exchange()
                .expectStatus().isNotFound();

        try {
            WorkflowTemplate workflowTemplateUpdated = (WorkflowTemplate) DefaultWorkflows.getWorkflowTemplateTO2()
                    .convertAndValidate();
            workflowTemplateUpdated.removeVertex(workflowTemplateUpdated.findProduct("v1").get());

            checkIfResponseIsEqualsToTO("/catalog/random", new WorkflowTemplateTO(workflowTemplateUpdated));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
